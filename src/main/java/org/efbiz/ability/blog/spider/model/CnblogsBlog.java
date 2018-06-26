package org.efbiz.ability.blog.spider.model;

import org.efbiz.ability.blog.spider.util.DateUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

/**
 * 博客园
 * @author  谭智军
 */
public class CnblogsBlog extends Blog {
    public CnblogsBlog(URL url) throws IOException {
        this.url = url;
        Document doc = Jsoup.parse(url, 5000);
        this.rootChoise = "#post_detail";
        this.publishDate = DateUtil.getNowDate();
        this.title = doc.title();
        this.desc = doc.title();
        doc.getElementsByTag("script").remove();
        this.content = doc.select(this.rootChoise).toString();

    }
}
