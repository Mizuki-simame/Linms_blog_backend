package linms.linmsblog.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import linms.linmsblog.common.Result;
import linms.linmsblog.model.dto.ESArticlesDTO;
import linms.linmsblog.model.dto.UploadDTO;
import linms.linmsblog.model.entity.ArticleCategories;
import linms.linmsblog.model.entity.Articles;
import linms.linmsblog.model.entity.JavaTechStacks;
import linms.linmsblog.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
public class ArticleController {

    @Autowired
    private ArticlesService articlesService;

    @PostMapping("article")
    public Result<String> articleUpload(@RequestParam("markdown") MultipartFile markdown,
                                        @RequestParam("cover") MultipartFile cover,
                                        @RequestParam("techStackId") int techStackId) {
        return articlesService.articleUpload(markdown,cover,techStackId);
    }

    @GetMapping("articles")
    public Result<Page<Articles>> getArticles(@RequestParam("page") int page,
                                              @RequestParam("size") int size) {
        return articlesService.getArticles(page, size);
    }

    @GetMapping("/articles/{id}")
    public Result<Articles> getArticlesById(@PathVariable("id") String id) {
        return articlesService.getArticleById(id);
    }

    @GetMapping("/categories")
    public Result<List<ArticleCategories>> getCategories() {
        return articlesService.getCategories();
    }

    @GetMapping("/categories/{id}/tech-stacks")
    public Result<List<JavaTechStacks>> getTechStacks(@PathVariable("id") int id) {
        return articlesService.getTechStacks(id);
    }

    @GetMapping("/tech-stacks/{id}/articles")
    public Result<List<Articles>> getArticlesByTechStack(@PathVariable("id") int id) {
        return articlesService.getArticlesByTechStack(id);
    }

    @GetMapping("/getArticles/{keyword}")
    public Result<List<ESArticlesDTO>> getArticlesByKeyword(@PathVariable("keyword") String keyword) throws IOException {
        return articlesService.getArticlesByKeyword(keyword);
    }
}
