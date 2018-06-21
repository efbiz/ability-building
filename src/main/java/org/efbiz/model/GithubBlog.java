package org.efbiz.model;

import java.io.IOException;
import java.net.URL;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GithubBlog extends Blog {

  public final static String HOST_URL = "http://blog.csdn.net/";
  public final static String TOP_XPATH = "#article_toplist .list_item,.article_item";
  public final static String NORMAL_QUERY = "#article_list .list_item,.article_item";
  public final static String TARGET_DIR = "D:\\data\\csdn_blogs";
  public final static String CSDN_CHOICE_CSS = "article";
  public final static String CSDN_REMOVE_CSS = ".dp-highlighter";
  public String choiceRoot;

  @Override
  public String convert(String html) {
    return super.convert(html, CSDN_CHOICE_CSS, CSDN_REMOVE_CSS);
  }

  @Override
  public String convert(URL url) throws IOException {
    return super.convert(url, CSDN_CHOICE_CSS, "svg");
  }
}
