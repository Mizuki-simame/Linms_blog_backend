package linms.linmsblog.model.dto;


import lombok.Data;

@Data
public class CommentsDTO {

    // 文章ID
    private String articleId;

    // 评论内容
    private String content;

    // 父评论ID，回复某条评论时传入
    private int parentId;

}
