package org.efbiz.service;

import org.efbiz.factory.BlogFactory;
import org.efbiz.model.Blog;
import org.efbiz.model.CSDNBlog;
import org.efbiz.util.HTML2Md;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
@Service
public class Blog2mdService {

  @Autowired
  private BlogFactory blogFactory;

  public void convertAllCSDNBlogByUserName(String username) throws IOException {
    String url = CSDNBlog.HOST_URL + username + "/article/list/" + 1;
    Document parse = Jsoup.parse(new URL(url), 5000);
    Element element = parse.select("div#papelist span").get(0);
    String paperListInfo = element.text();
    // 有点暴力，需注意
    String totalPage = paperListInfo.split("共")[1].split("页")[0];
    int total = Integer.valueOf(totalPage);
    System.out.println(total);
    Map<String, String> map = new HashMap<>();
    for (int i = 1; i <= total; i++) {
      String listUrl = CSDNBlog.HOST_URL + username + "/article/list/" + i;
      Document doc = Jsoup.parse(new URL(listUrl), 5000);
      Elements topSelect = doc.select(CSDNBlog.TOP_XPATH);
      Elements normalSelect = doc.select(CSDNBlog.NORMAL_QUERY);
      for (Element item : topSelect) {
        map.put(item.select(".article_title  h1  span  a").get(0).attr("href"),
            item.select(".article_description").get(0).text());
      }
      for (Element item : normalSelect) {
        map.put(item.select(".article_title  h1  span  a").get(0).attr("href"),
            item.select(".article_description").get(0).text());
      }
    }
    Set<String> strings = map.keySet();
    for (String item : strings) {
      Blog blog = blogFactory.translateBLog(new URL(item));
      blog.buildHexo();
    }
  }

  public String convert(URL url) throws IOException {
    Blog blog = blogFactory.translateBLog(url);
    blog.buildHexo();
    return blog.convert(url);
  }

  public String convert(String html) {
    return new Blog().convert(html);
  }
}
