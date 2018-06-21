package org.efbiz.factory;

import org.efbiz.model.Blog;
import org.efbiz.model.CSDNBlog;
import org.efbiz.model.GithubBlog;
import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class BlogFactory {
   public  static  final  String CSDN ="csdn";
   public  static  final  String GITHUB ="github";
   public Blog translateBLog(URL url){
     if(url.getHost().toLowerCase().contains(CSDN)){
       CSDNBlog csdnBlog = new CSDNBlog();
       csdnBlog.choiceRoot = CSDNBlog.CSDN_CHOICE_CSS;
       return  csdnBlog;
     }else if(url.getHost().toLowerCase().contains(GITHUB)){
       GithubBlog githubBlog = new GithubBlog();
       githubBlog.choiceRoot = GithubBlog.CSDN_CHOICE_CSS;
       return  githubBlog;
     }else {
       return  null;
     }
   }
}
