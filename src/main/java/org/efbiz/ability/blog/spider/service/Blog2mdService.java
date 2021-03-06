package org.efbiz.ability.blog.spider.service;

import org.efbiz.ability.blog.spider.factory.BlogFactory;
import org.efbiz.ability.blog.spider.model.Blog;
import org.efbiz.ability.blog.spider.model.CSDNBlog;
import org.efbiz.ability.blog.spider.util.FilesUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
@Service
public class Blog2mdService {

  public final static String COMMON_TARGET_DIR = "D:\\data";


  private BlogFactory blogFactory = BlogFactory.getInstance();

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
      saveMdFile(blog);
    }
  }

  public void saveMdFile(Blog blog) throws IOException {
    System.out.println("保存博文--->[" + blog.getTitle() + "]");
    StringBuffer sb = blog.getHexoDesc();
    sb.append(blog.getMdContent());
    FilesUtil.newFile(COMMON_TARGET_DIR + File.separator + blog.getTitle() + ".md",sb.toString(),false);
  }


}
