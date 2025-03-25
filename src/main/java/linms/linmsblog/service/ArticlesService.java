package linms.linmsblog.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import linms.linmsblog.common.Result;
import linms.linmsblog.model.dto.ESArticlesDTO;
import linms.linmsblog.model.dto.UploadDTO;
import linms.linmsblog.model.entity.ArticleCategories;
import linms.linmsblog.model.entity.Articles;
import linms.linmsblog.model.entity.JavaTechStacks;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ArticlesService {
    Result<String> articleUpload(MultipartFile markdown,MultipartFile cover,int techStackId);

    Result<Page<Articles>> getArticles(int page, int size);

    Result<Articles> getArticleById(String id);

    Result<List<ArticleCategories>> getCategories();

    Result<List<JavaTechStacks>> getTechStacks(int id);

    Result<List<Articles>> getArticlesByTechStack(int id);

    Result<List<ESArticlesDTO>> getArticlesByKeyword(String keyword) throws IOException;
}
