package org.efbiz.ability.blog.spider.model;

import java.io.IOException;
import java.net.URL;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GithubBlog extends Blog {

  public String choiceRoot ="article";

  public GithubBlog(URL url) throws IOException {
    super(url,"article");
  }
}
