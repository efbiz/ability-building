package org.efbiz.ability.blog.spider.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.efbiz.ability.blog.spider.util.DateUtil;
import org.efbiz.ability.blog.spider.util.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: thisHJQS
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
  protected URL url;
  protected String title;
  protected String desc;
  protected String publishDate;
  protected List<String> categories;
  protected List<String> tags;
  protected String content;
  protected String rootChoise;
  protected String[] removeCss;

  public Blog(URL url) throws IOException {
    this.url = url;
    Document doc = Jsoup.parse(url, 5000);
    this.rootChoise = "body";
    this.publishDate = DateUtil.getNowDate();
    this.title = doc.title();
    this.desc = doc.title();
    String[] removeCss = {"javascript"};
    this.removeCss = removeCss;
    this.content = doc.select(this.rootChoise).toString();
  }

  public Blog(URL url, String rootChoise) throws IOException {
    this.url = url;
    Document doc = Jsoup.parse(url, 5000);
    this.rootChoise = rootChoise;
    this.publishDate = DateUtil.getNowDate();
    this.title = doc.title();
    this.desc = doc.title();
    String[] removeCss = {"javascript"};
    this.removeCss = removeCss;
    this.content = doc.select(rootChoise).toString();
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

  public StringBuffer getHexoDesc() {
    StringBuffer sb = new StringBuffer();
    sb.append("---");
    sb.append("\r\n");
    sb.append("title: " + this.getTitle());
    sb.append("\r\n");
    sb.append("date: " + this.getPublishDate() + ":00");
    sb.append("\r\n");
    sb.append("categories:");
    sb.append("\r\n");
    if(this.getCategories() != null && !this.getCategories().isEmpty()){
        for (String cat : this.getCategories()) {
            sb.append("- " + cat);
            sb.append("\r\n");
        }
    }

    sb.append("tags:");
    sb.append("\r\n");
    if( null != this.getTags() && !this.getTags().isEmpty()){
        for (String tag : this.getTags()) {
            sb.append("- " + tag);
            sb.append("\r\n");
        }
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

  public String getMdContent() {
    return JsoupUtil.convert(this.getContent());
  }
}
