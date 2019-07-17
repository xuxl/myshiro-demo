package pres.xuxl.myshiro.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import pres.xuxl.myshiro.entity.UserInfo;

@Controller
public class OtherController {

	 /**
 	 * 个人中心，需认证可访问
 	 */
    @RequestMapping("/other/index")
    public String add(HttpServletRequest request){
    	
        UserInfo bean = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        if(bean!=null)
        	request.setAttribute("userName", bean.getName());
        return "/other/index";
    }

    /**
 	 * 会员中心，需认证且角色为vip可访问
 	 */
    @RequestMapping("/other/dodo")
    public String update(HttpServletRequest request){
    	UserInfo bean = (UserInfo) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute("userName", bean.getName());
        return "/other/dodo";
    }
}
