package org.efbiz.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.efbiz.model.Blog;
import org.efbiz.model.CSDNBlog;
import org.efbiz.model.GithubBlog;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class BlogFactory {

  public static final String CSDN = "csdn";
  public static final String GITHUB = "github";

  public Blog translateBLog(URL url) {
    if (url.getHost().toLowerCase().contains(CSDN)) {
      return createCSDNBlog(url);
    } else if (url.getHost().toLowerCase().contains(GITHUB)) {
      GithubBlog githubBlog = new GithubBlog();
      githubBlog.choiceRoot = GithubBlog.CSDN_CHOICE_CSS;
      return githubBlog;
    } else {
      return null;
    }
  }

  private Blog createCSDNBlog(URL url) {
    CSDNBlog csdnBlog = new CSDNBlog();
    csdnBlog.choiceRoot = CSDNBlog.CSDN_CHOICE_CSS;
    Document doc = null;
    try {
      doc = Jsoup.parse(url, 5000);
    } catch (IOException e) {
      e.printStackTrace();
    }

    csdnBlog.setTitle(doc.select("#article_details > div.article_title > h1 > span > a").text());
    csdnBlog.setDesc(doc.select("#article_details > div.article_title > h1 > span > a").text());
    csdnBlog.setPublishDate(doc.select(
        "#article_details > div.article_manage.clearfix > div.article_r > span.link_postdate")
        .text());
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
    csdnBlog.setCategories(cat);
    Elements tagEles = doc
        .select("#article_details > div.article_manage.clearfix > div.article_l > span > a");
    List<String> tags = new ArrayList<>();
    for (Element ele : tagEles) {
      String text = ele.text();
      tags.add(text);
    }
    csdnBlog.setTags(tags);
    doc.getElementsByTag("script").remove();

    csdnBlog.setContent(doc.select(CSDNBlog.CSDN_CHOICE_CSS).toString());

    return csdnBlog;
  }
}
