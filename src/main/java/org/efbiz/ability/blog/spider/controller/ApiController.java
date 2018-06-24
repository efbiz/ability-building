package org.efbiz.ability.blog.spider.controller;


import lombok.extern.log4j.Log4j2;
import org.efbiz.ability.blog.spider.factory.BlogFactory;
import org.efbiz.ability.blog.spider.model.Blog;
import org.efbiz.ability.blog.spider.service.Blog2mdService;
import org.efbiz.ability.blog.spider.util.BaseResp;
import org.efbiz.ability.blog.spider.util.JsoupUtil;
import org.efbiz.ability.blog.spider.util.ParamVo;
import org.efbiz.ability.blog.spider.util.ResultStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URL;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
@Controller
@RequestMapping("/tool")
@Log4j2
public class ApiController {

  @Resource
  private Blog2mdService blog2mdService;


  private BlogFactory blogFactory = BlogFactory.getInstance();

  @RequestMapping(value = "/html2md", method = RequestMethod.GET)
  public String toPage() {
    return "/html2md";
  }

  @RequestMapping(value = "/convert", method = RequestMethod.POST)
  @ResponseBody
  public BaseResp<?> convert(@RequestBody ParamVo paramVo) {
    try {
      String result = "";
      if (paramVo.getUrl().isEmpty() && paramVo.getHtml().isEmpty()) {
        return new BaseResp(ResultStatus.error_invalid_argument,
            ResultStatus.error_invalid_argument.getErrorMsg());
      }
      if (!paramVo.getUrl().isEmpty()) {
        // csdn2mdService.convert(new URL(paramVo.getUrl()));
        Blog blog  = blogFactory.translateBLog(new URL(paramVo.getUrl()));
        result = blog.getMdContent();
        blog2mdService.saveMdFile(blog);
        return new BaseResp(ResultStatus.SUCCESS, result);
      }
      if (!paramVo.getHtml().isEmpty()) {
        result = JsoupUtil.convert(paramVo.getHtml());
        return new BaseResp(ResultStatus.SUCCESS, result);
      }
    } catch (Exception e) {
      log.error("转换失败", e);
      return new BaseResp(ResultStatus.http_status_internal_server_error,
          ResultStatus.http_status_internal_server_error.getErrorMsg());
    }
    return null;
  }
}
