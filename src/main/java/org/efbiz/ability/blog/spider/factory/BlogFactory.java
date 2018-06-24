package org.efbiz.ability.blog.spider.factory;

import lombok.extern.log4j.Log4j2;
import org.efbiz.ability.blog.spider.model.Blog;
import org.efbiz.ability.blog.spider.model.CSDNBlog;
import org.efbiz.ability.blog.spider.model.GithubBlog;

import java.io.IOException;
import java.net.URL;


@Log4j2
public class BlogFactory {

  public static final String CSDN = "csdn";
  public static final String GITHUB = "github";

  private BlogFactory() {}

  private static BlogFactory single=null;

  /**
   * 单例方式获取工厂
   * @return
   */
  public static BlogFactory getInstance() {
    if (single == null) {
      single = new BlogFactory();
    }
    return single;
  }

  public Blog translateBLog(URL url) throws IOException {
    if (url.getHost().toLowerCase().contains(CSDN)) {
      return new CSDNBlog(url);
    } else if (url.getHost().toLowerCase().contains(GITHUB)) {
      return new GithubBlog(url);
    } else {
      return new Blog(url);
    }
  }


}
