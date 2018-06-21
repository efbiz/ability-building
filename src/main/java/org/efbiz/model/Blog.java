package org.efbiz.model;

import org.efbiz.util.HTML2Md;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
@Data
public class Blog {
    private String title;
    private String desc;
    private String publishDate;
    private List<String> categories;
    private List<String> tags;
    private String content;

    public String convert(URL url) throws IOException {
        return convert(url,"body",null);
    }

    public String convert(URL url,String choiceCss,String removeCss ) throws IOException {
        Document doc = Jsoup.parse(url,5000);
        doc.getElementsByTag("script").remove();
        String content = doc.select(choiceCss).toString();
        String result = HTML2Md.convertHtml2Content(content, "utf-8",choiceCss,removeCss);
        return result;
    }
    public String convert(String html  ){
        return  convert(html,"body",null);
    }

    public String convert(String html,String choiceCss,String removeCss ){
        Document doc = Jsoup.parse(html,"utf-8");
        doc.getElementsByTag("script").remove();
        Elements select = doc.select(choiceCss);
        if(select.isEmpty()){
            String convert = HTML2Md.convert(html, "utf-8");
            return convert;
        }else{
            String content = select.toString();
            String result = HTML2Md.convertHtml2Content(content, "utf-8",choiceCss,removeCss);
            return result;
        }
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
}
