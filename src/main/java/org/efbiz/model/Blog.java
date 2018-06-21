package org.efbiz.model;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.commons.io.IOUtils;
import org.efbiz.util.HTML2Md;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: thisHJQS
 */
@Data
public class Blog {

  public final static String COMMON_TARGET_DIR = "D:\\data\\csdn_blogs";
  public final static String COMMON_CSDN_CHOICE_CSS = "#article_content";
  public final static String COMMON_CSDN_REMOVE_CSS = ".dp-highlighter";

  private String title;
  private String desc;
  private String publishDate;
  private List<String> categories;
  private List<String> tags;
  private String content;

  public String convert(URL url) throws IOException {
    return convert(url, "body", null);
  }

  public String convert(URL url, String choiceCss, String removeCss) throws IOException {
    Document doc = Jsoup.parse(url, 5000);
    doc.getElementsByTag("script").remove();
    String content = doc.select(choiceCss).toString();
    String result = HTML2Md.convertHtml2Content(content, "utf-8", choiceCss, removeCss);
    return result;
  }

  public String convert(String html) {
    return convert(html, "body", null);
  }

  public String convert(String html, String choiceCss, String removeCss) {
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

  @Override
  public String toString() {
    return "Blog{" +
        "title='" + title + '\'' +
        ", desc='" + desc + '\'' +
        ", publishDate='" + publishDate + '\'' +
        ", categories=" + categories +
        ", tags=" + tags +
        ", content='" + content + '\'' +
        '}';
  }

  public void buildHexo() throws IOException {
    StringBuffer sb = getHexoDesc();
    sb.append(HTML2Md.convertHtml2Content(this.getContent(), "UTF-8", COMMON_CSDN_CHOICE_CSS,
        COMMON_CSDN_REMOVE_CSS));
    IOUtils.write(sb.toString(), new FileOutputStream(
        new File(COMMON_TARGET_DIR + File.separator + this.getTitle() + ".md")));
  }

  public StringBuffer getHexoDesc() {
    System.out.println("保存博文--->[" + this.getTitle() + "]");
    StringBuffer sb = new StringBuffer();
    sb.append("---");
    sb.append("\r\n");
    sb.append("title: " + this.getTitle());
    sb.append("\r\n");
    sb.append("date: " + this.getPublishDate() + ":00");
    sb.append("\r\n");
    sb.append("categories:");
    sb.append("\r\n");
    for (String cat : this.getCategories()) {
      sb.append("- " + cat);
      sb.append("\r\n");
    }
    sb.append("tags:");
    sb.append("\r\n");
    for (String tag : this.getTags()) {
      sb.append("- " + tag);
      sb.append("\r\n");
    }
    sb.append("\r\n");
    sb.append("---");
    sb.append("\r\n");
    sb.append(this.getDesc() == null ? "" : this.getDesc());
    sb.append("\r\n");
    sb.append("<!--more-->");
    sb.append("\r\n");
    sb.append("\r\n");
    return sb;
  }
}
