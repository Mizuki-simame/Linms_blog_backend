package linms.linmsblog.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;

/**
 * (Comments)表实体类
 *
 * @author makejava
 * @since 2025-03-10 09:11:42
 */
@Data
@TableName("Comments")
public class Comments{

    private Integer id;

    private String content;

    private Date createdAt;

    private String username;

    private String avatar;

    private Integer  parentId;

    private String articleId;

    private Integer userId;
}

