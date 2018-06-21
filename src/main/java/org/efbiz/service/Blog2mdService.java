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

    public  void convertAllBlogByUserName(String username) throws IOException {
        String url = CSDNBlog.HOST_URL + username + "/article/list/" + 1;
        Document parse = Jsoup.parse(new URL(url), 5000);
        Element element = parse.select("div#papelist span").get(0);
        String paperListInfo = element.text();
        // 有点暴力，需注意
        String totalPage = paperListInfo.split("共")[1].split("页")[0];
        int total = Integer.valueOf(totalPage);
        System.out.println(total);
        Map<String,String> map = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            String listUrl = CSDNBlog.HOST_URL + username + "/article/list/" + i;
            Document doc = Jsoup.parse(new URL(listUrl), 5000);
            Elements topSelect = doc.select(CSDNBlog.TOP_XPATH);
            Elements normalSelect = doc.select(CSDNBlog.NORMAL_QUERY);
            for (Element item : topSelect) {
                map.put(item.select(".article_title  h1  span  a").get(0).attr("href"),item.select(".article_description").get(0).text());
            }
            for (Element item : normalSelect) {
                map.put(item.select(".article_title  h1  span  a").get(0).attr("href"),item.select(".article_description").get(0).text());
            }
        }
        Set<String> strings = map.keySet();
        for(String item:strings){
            Document doc = Jsoup.parse(new URL(CSDNBlog.HOST_URL + item), 5000);
            Blog bm = new Blog();
            bm.setTitle(doc.select("#article_details > div.article_title > h1 > span > a").text());
            bm.setDesc(map.get(item));
            bm.setPublishDate(doc.select("#article_details > div.article_manage.clearfix > div.article_r > span.link_postdate").text());
            Elements select = doc.select("#article_details > div.category.clearfix > div.category_r");
            List<String> cat = new ArrayList<>();
            for(Element ele : select){
                String text = ele.select("label span").get(0).text();
                int i = text.lastIndexOf("（");
                if(i>=0){
                    String substring = text.substring(0, i);
                    cat.add(substring);
                }else{
                    cat.add(text);
                }
            }
            bm.setCategories(cat);
            Elements tagEles = doc.select("#article_details > div.article_manage.clearfix > div.article_l > span > a");
            List<String> tags = new ArrayList<>();
            for(Element ele : tagEles){
                String text = ele.text();
                tags.add(text);
            }
            bm.setTags(tags);
            doc.getElementsByTag("script").remove();

            bm.setContent(doc.select(CSDNBlog.CSDN_CHOICE_CSS).toString());
            try{
                buildHexo(bm);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public  void buildHexo(Blog bm) throws IOException {
        System.out.println("保存博文--->[" + bm.getTitle() + "]");
        StringBuffer sb = new StringBuffer();
        sb.append("---");
        sb.append("\r\n");
        sb.append("title: " + bm.getTitle());
        sb.append("\r\n");
        sb.append("date: " + bm.getPublishDate() + ":00");
        sb.append("\r\n");
        sb.append("categories:");
        sb.append("\r\n");
        for(String cat:bm.getCategories()){
            sb.append("- " + cat);
            sb.append("\r\n");
        }
        sb.append("tags:");
        sb.append("\r\n");
        for(String tag:bm.getTags()){
            sb.append("- " + tag);
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("---");
        sb.append("\r\n");
        sb.append(bm.getDesc()==null?"":bm.getDesc());
        sb.append("\r\n");
        sb.append("<!--more-->");
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(HTML2Md.convertHtml2Content(bm.getContent(),"UTF-8", CSDNBlog.CSDN_CHOICE_CSS,CSDNBlog.CSDN_REMOVE_CSS));
        IOUtils.write(sb.toString(),new FileOutputStream(new File(CSDNBlog.TARGET_DIR + File.separator + bm.getTitle() + ".md")));
    }

    public String convert(URL url) throws IOException {
        Blog blog = blogFactory.translateBLog(url);
        return blog.convert(url);
    }

    public String convert(String html) {
        return  new Blog().convert(html);
    }
}
