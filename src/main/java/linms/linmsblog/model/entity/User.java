package linms.linmsblog.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2025-03-07 19:33:12
 */
@Data
@TableName("User")
public class User{

    private Integer userId;

    private String username;

    private String password;

    private String email;

    private String avatarUrl;

    private String oauthProvider;

    private String oauthId;

    private String role;

    private Date createdAt;

    private Date lastLogin;

    private Integer isLocked;
}

