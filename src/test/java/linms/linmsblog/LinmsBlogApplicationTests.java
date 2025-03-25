package linms.linmsblog;


import linms.linmsblog.model.dto.ESArticlesDTO;
import linms.linmsblog.service.impl.ElasticsearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class LinmsBlogApplicationTests {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    void contextLoads() throws IOException {
//        String index = elasticsearchService.createIndex("1111");
//        System.out.println(index);
//        private String title;
//
//        private String content;
//
//        private String author;
//
//
//        private String javaStacks;
//
//        private String articleCategory;

        List<String> list = new ArrayList<>();
        list.add("title");
        list.add("content");
        list.add("javaStacks");
        list.add("articleCategory");

        List<ESArticlesDTO> esArticlesDTOS = elasticsearchService.searchDocuments("1111",list, "java");
        esArticlesDTOS.stream()
                .map(ESArticlesDTO::getId)
                .toList().forEach(System.out::println);
    }

}
