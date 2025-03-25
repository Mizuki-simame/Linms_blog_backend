package linms.linmsblog.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章分类表(ArticleCategories)表实体类
 *
 * @author makejava
 * @since 2025-03-09 15:07:11
 */
@Data
@TableName("article_categories")
public class ArticleCategories{

    private Integer id;
//分类名称（唯一）
    private String name;
//分类描述
    private String description;
//创建时间
    private Date createdAt;
//更新时间
    private Date updatedAt;
}

