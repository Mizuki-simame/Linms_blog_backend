package linms.linmsblog.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

import jakarta.persistence.JoinTable;
import lombok.Data;

/**
 * (Articles)表实体类
 *
 * @author makejava
 * @since 2025-03-08 22:57:44
 */
@Data
@TableName("Articles")
public class Articles{

    private String id;

    private String title;

    private String content;

    private String renderedContent;

    private String summary;

    private String author;

    private Date createdAt;

    private  String img;

    private Integer type;
}

