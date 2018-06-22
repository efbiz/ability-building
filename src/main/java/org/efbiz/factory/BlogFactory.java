package org.efbiz.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class BlogFactory {

  public static final String CSDN = "csdn";
  public static final String GITHUB = "github";

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
