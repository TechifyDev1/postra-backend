import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
    public static List<String> extractImgUrls(String htmlContent) {
        List<String> imgUrls = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);
        Element images = doc.select("img");
        for (Element img : images) {
            String src = img.attr("src");
            if(src != null && !src.isEmpty()) {
                imgUrls.add(src);
            }
        }
        return imgUrls;
    }
}
