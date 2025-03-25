package linms.linmsblog.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ESArticlesDTO {

    private String id;

    private String title;

    private String content;

    private String author;


    private String javaStacks;

    private String articleCategory;
}
