package linms.linmsblog.common.uilts;


import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownUtils {

    /**
     * 将 Markdown 转换为 HTML
     */
    public static String mdToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 提取摘要（前100个字符）
     */
    public static String extractSummary(String markdown) {
        String[] paragraphs = markdown.split("\n\n");
        String firstParagraph = paragraphs.length > 0 ? paragraphs[0] : "";
        return firstParagraph.replaceAll("[#*`\\[\\]]", "").substring(0, 100);
    }
}