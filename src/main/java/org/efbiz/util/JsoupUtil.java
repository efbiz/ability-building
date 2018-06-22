package org.efbiz.util;

import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupUtil {
  public static String convert(URL url) throws IOException {
    return convert(url, "body", null);
  }

  public static String convert(URL url, String choiceCss, String removeCss) throws IOException {
    Document doc = Jsoup.parse(url, 5000);
    doc.getElementsByTag("script").remove();
    String content = doc.select(choiceCss).toString();
    String result = HTML2Md.convertHtml2Content(content, "utf-8", choiceCss, removeCss);
    return result;
  }

  public static String convert(String html) {
    return convert(html, "body", null);
  }

  public static String convert(String html, String choiceCss, String removeCss) {
    Document doc = Jsoup.parse(html, "utf-8");
    doc.getElementsByTag("script").remove();
    Elements select = doc.select(choiceCss);
    if (select.isEmpty()) {
      String convert = HTML2Md.convert(html, "utf-8");
      return convert;
    } else {
      String content = select.toString();
      String result = HTML2Md.convertHtml2Content(content, "utf-8", choiceCss, removeCss);
      return result;
    }
  }
}
