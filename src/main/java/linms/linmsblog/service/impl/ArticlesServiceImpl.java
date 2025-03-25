package linms.linmsblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import linms.linmsblog.common.Result;
import linms.linmsblog.common.uilts.ImageUploadUtil;
import linms.linmsblog.common.uilts.MarkdownUtils;
import linms.linmsblog.mapper.ArticleCategoriesMapper;
import linms.linmsblog.mapper.ArticlesMapper;
import linms.linmsblog.mapper.JavaTechStacksMapper;
import linms.linmsblog.model.dto.ESArticlesDTO;
import linms.linmsblog.model.entity.ArticleCategories;
import linms.linmsblog.model.entity.Articles;
import linms.linmsblog.model.entity.JavaTechStacks;
import linms.linmsblog.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ArticlesServiceImpl implements ArticlesService {


    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private ArticleCategoriesMapper articleCategoriesMapper;

    @Autowired
    private JavaTechStacksMapper javaTechStacksMapper;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private RedisTemplate<String,List<String>> redisTemplateList;

    private final String TOP_ARTICLES_KEY = "top_articles:";

    private final String TOP_STACKS_KEY = "top_tech_stacks:";


    @Override
    public Result<String> articleUpload(MultipartFile markdown,MultipartFile cover,int techStackId) {
        try {
            // 读取文件内容
            String mdContent = new String(markdown.getBytes(), StandardCharsets.UTF_8);

            // 解析为 HTML
            String htmlContent = MarkdownUtils.mdToHtml(mdContent);

            // 提取摘要
            String summary = MarkdownUtils.extractSummary(mdContent);

            UUID uuid = UUID.randomUUID();
            // 构建实体
            Articles article = new Articles();
            article.setId(String.valueOf(uuid));
            article.setTitle(markdown.getOriginalFilename().replace(".md", ""));
            article.setContent(mdContent);          // 原始 Markdown
            article.setRenderedContent(htmlContent); // 渲染后的 HTML
            article.setSummary(summary);            // 摘要
            article.setAuthor("Mizuki");
            article.setImg(ImageUploadUtil.uploadImage(cover));
            article.setType(techStackId);

            //ES
            ESArticlesDTO esArticlesDTO = new ESArticlesDTO();
            esArticlesDTO.setId(String.valueOf(uuid));
            esArticlesDTO.setTitle(article.getTitle());
            esArticlesDTO.setContent(mdContent);
            esArticlesDTO.setAuthor("Mizuki");
            String javaTechStackName = javaTechStacksMapper.selectOne(new LambdaQueryWrapper<JavaTechStacks>().eq(JavaTechStacks::getId, techStackId)).getName();
            String categoryName = javaTechStacksMapper.selectOne(new LambdaQueryWrapper<JavaTechStacks>().eq(JavaTechStacks::getId, techStackId)).getCategory();
            esArticlesDTO.setArticleCategory(categoryName);
            esArticlesDTO.setJavaStacks(javaTechStackName);

            // 保存到数据库
            articlesMapper.insert(article);
            elasticsearchService.ingestDocument("1111",String.valueOf(uuid),esArticlesDTO);

            return Result.ok("文章保存成功");
        } catch (IOException e) {
            return Result.fail("文件处理失败");
        }
    }

    @Override
    public Result<Page<Articles>> getArticles(int page, int size) {
        Page<Articles> pageArticles = new Page<>(page, size);
//        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
//        Set<String> set = zSetOps.reverseRange(TOP_ARTICLES_KEY, 0, 4);
//        assert set != null;
//        List<String> list = new ArrayList<>(set);
//        LambdaQueryWrapper<Articles> wrapper = new LambdaQueryWrapper<Articles>().in(Articles::getId, list);
        return  Result.ok(articlesMapper.selectPage(pageArticles, null));
    }

    @Override
    public Result<Articles> getArticleById(String id) {
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        zSetOps.incrementScore(TOP_ARTICLES_KEY, id, 1);
        LambdaQueryWrapper<Articles> wrapper = new LambdaQueryWrapper<Articles>()
                .eq(Articles::getId, id);
        return Result.ok(articlesMapper.selectOne(wrapper));
    }

    @Override
    public Result<List<ArticleCategories>> getCategories() {
        return Result.ok(articleCategoriesMapper.selectList(null));
    }

    @Override
    public Result<List<JavaTechStacks>> getTechStacks(int id) {
        LambdaQueryWrapper<JavaTechStacks> wrapper = new LambdaQueryWrapper<JavaTechStacks>()
                .eq(JavaTechStacks::getCategoryId, id);
        List<JavaTechStacks> javaTechStacks = javaTechStacksMapper.selectList(wrapper);
        return Result.ok(javaTechStacks);
    }

    @Override
    public Result<List<Articles>> getArticlesByTechStack(int id) {
//        if(Boolean.TRUE.equals(redisTemplateList.hasKey(TOP_STACKS_KEY + id))){
//            List<String> articles = redisTemplateList.opsForValue().get(TOP_STACKS_KEY + id);
//            List<Articles> articlesList = articlesMapper.selectBatchIds(articles);
//            return Result.ok(articlesList);
//        }
        LambdaQueryWrapper<Articles> wrapper = new LambdaQueryWrapper<Articles>()
                .eq(Articles::getType, id);
        List<Articles> articlesList = articlesMapper.selectList(wrapper);
        //-----
//        List<String> articleIds = articlesList.stream()
//                .map(Articles::getId)
//                .toList();
//        ZSetOperations<String, List<String>> zSet = redisTemplateList.opsForZSet();
//        zSet.incrementScore(TOP_STACKS_KEY + id,articleIds,1);
        //----
        return Result.ok(articlesList);
    }

    @Override
    public Result<List<ESArticlesDTO>> getArticlesByKeyword(String keyword) throws IOException {
        List<String>  list = new ArrayList<>();
        list.add("title");
        list.add("content");
        list.add("javaStacks");
        list.add("articleCategory");
        List<ESArticlesDTO> esArticlesDTOS = elasticsearchService.searchDocuments("1111", list, keyword);
        return Result.ok(esArticlesDTOS);
    }

}
