package org.efbiz.ability.blog.spider.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.efbiz.ability.blog.spider.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CSDNBlog extends Blog {

  public final static String HOST_URL = "http://blog.csdn.net/";
  public final static String TOP_XPATH = "#article_toplist .list_item,.article_item";
  public final static String NORMAL_QUERY = "#article_list .list_item,.article_item";
  public final static String CSDN_CHOICE_CSS = "#article_content";
  public final static String CSDN_REMOVE_CSS = ".dp-highlighter";

  public CSDNBlog(URL url) throws IOException {
    Document doc = Jsoup.parse(url, 5000);
    this.title = doc.select(".title-article").text();
    this.desc = doc.select("#article_details > div.article_title > h1 > span > a").text();
    if (doc.select(
        ".time")
        != null) {
      this.publishDate = doc.select(
          ".time")
          .text();
    } else {
      this.publishDate = DateUtil.getNowDate();
    }

    Elements select = doc.select("#article_details > div.category.clearfix > div.category_r");
    List<String> cat = new ArrayList<>();
    for (Element ele : select) {
      String text = ele.select("label span").get(0).text();
      int i = text.lastIndexOf("（");
      if (i >= 0) {
        String substring = text.substring(0, i);
        cat.add(substring);
      } else {
        cat.add(text);
      }
    }
    this.categories = cat;
    Elements tagEles = doc
        .select("#article_details > div.article_manage.clearfix > div.article_l > span > a");
    List<String> tags = new ArrayList<>();
    for (Element ele : tagEles) {
      String text = ele.text();
      tags.add(text);
    }
    this.tags = tags;
    doc.getElementsByTag("script").remove();
    this.content = doc.select(CSDNBlog.CSDN_CHOICE_CSS).toString();
  }


}
