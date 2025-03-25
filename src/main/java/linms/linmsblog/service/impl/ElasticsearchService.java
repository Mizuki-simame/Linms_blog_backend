package linms.linmsblog.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import linms.linmsblog.model.dto.ESArticlesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    public String createIndex(String indexName) throws IOException {
    return elasticsearchClient.indices()
        .create(c -> c
                .index(indexName)
                .settings(s -> s
                        // 先配置分析器相关组件
                        .analysis(a -> a
                                // 2. 定义自定义分析器（必须在过滤器之后）
                                .analyzer("ik_english_combined", ana -> ana
                                        .custom(cus -> cus
                                                .tokenizer("ik_max_word") // 直接使用 IK 插件提供的分词器
                                        )
                                )
                        )
                )
                .mappings(m -> m
                        .properties("title", p -> p
                                .text(t -> t
                                        .analyzer("ik_max_word")
                                        .fields("en", f -> f
                                                .text(f2 -> f2.analyzer("standard"))
                                        )
                                        .fields("search", f -> f
                                                .text(f2 -> f2.analyzer("ik_english_combined")) // 正确引用自定义分析器
                                        )
                                )
                        )
                        // 其他字段（javaStacks、articleCategory、content）配置方式与 title 相同
                        .properties("javaStacks", p -> p
                                .text(t -> t
                                        .analyzer("ik_max_word")
                                        .fields("en", f -> f.text(f2 -> f2.analyzer("standard")))
                                        .fields("search", f -> f.text(f2 -> f2.analyzer("ik_english_combined")))
                                )
                        )
                        .properties("articleCategory", p -> p
                                .text(t -> t
                                        .analyzer("ik_max_word")
                                        .fields("en", f -> f.text(f2 -> f2.analyzer("standard")))
                                )
                        )
                        .properties("content", p -> p
                                .text(t -> t
                                        .analyzer("ik_smart")
                                        .fields("en", f -> f.text(f2 -> f2.analyzer("standard")))
                                        .fields("search", f -> f.text(f2 -> f2.analyzer("ik_english_combined")))
                                )
                        )
                )
        ).index();
    }

    public Result ingestDocument(String indexName, String id, ESArticlesDTO esArticlesDTO) throws IOException {
        IndexRequest<Object> indexRequest = IndexRequest.of(
                request -> request
                        .index(indexName)
                        .id(id)
                        .document(esArticlesDTO)
        );
        return elasticsearchClient.index(indexRequest).result();
    }

    public Object getProductById(String indexName, String id) throws IOException {
        GetResponse<ESArticlesDTO> response = elasticsearchClient.get(
                g -> g.index(indexName).id(id),
                ESArticlesDTO.class
        );
        if (response.found()) {
            return response.source();
        } else {
            return "Product Not Found";
        }
    }

    public List<ESArticlesDTO> searchDocuments(String indexName, List<String> fieldNames, String text) throws IOException {
        List<ESArticlesDTO> responseList = new ArrayList<>();
        SearchResponse<ESArticlesDTO> searchResponse = elasticsearchClient.search(s -> {
            s.index(indexName);
            List<Query> termQueries = fieldNames.stream()
                    .map(field -> Query.of(q -> q.term(t -> t.field(field).value(v -> v.stringValue(text)))))
                    .collect(Collectors.toList());
            s.query(q -> q.bool(b -> b.should(termQueries)));
            return s;
        }, ESArticlesDTO.class);
        for (Hit<ESArticlesDTO> hit : searchResponse.hits().hits()) {
            responseList.add(hit.source());
        }
        return responseList;
    }

}