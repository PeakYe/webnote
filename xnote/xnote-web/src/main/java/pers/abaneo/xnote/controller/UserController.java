package pers.abaneo.xnote.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pers.abaneo.web.utils.WebUtil;
import pers.abaneo.web.utils.model.ResultModel;
import pers.abaneo.web.utils.security.EncryptUtil;
import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.service.IUserService;

@Controller
@RequestMapping("service/user")
public class UserController extends BaseController{
	
	@Autowired IUserService service;
	
	@ResponseBody
	@RequestMapping("register")
	public ResultModel register(String name,String email,String password){
		User user=new User();
		user.setName(name);
		user.setPassword(password);
		user.setEmail(email);
		return service.registerUser(user);
	}
	
	@RequestMapping("exp")
	public ResultModel exp(String name,String email,String password){
		throw new RuntimeException();
	}
	
	@ResponseBody
	@RequestMapping("login")
	public ResultModel login(HttpSession session,HttpServletRequest req,HttpServletResponse rep,String name,String pwd,String code,String rememberMe) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		UsernamePasswordToken token =new UsernamePasswordToken(name, pwd);
		if("true".equals(rememberMe)){
			token.setRememberMe(true);
		}
		Subject subject= SecurityUtils.getSubject();
		try {
			subject.login(token);
			String ip=WebUtil.getRemoteHost(req);
			String agent=req.getHeader("User-Agent");
			String sign=EncryptUtil.EncoderByMd5(session.getId()+ip+agent);
			Cookie cookie=new Cookie("jsign", sign);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			rep.addCookie(cookie);
			SavedRequest savedReq=WebUtils.getSavedRequest(req);
			if(savedReq!=null){
				String url=savedReq.getRequestUrl();
				return new ResultModel(url);
			}
			return new ResultModel("/blog/index.html");
					
		} catch (AuthenticationException e) {
			logger.debug(e.getMessage());
			return new ResultModel(false,"用户名或密码错误");
		}
	}
	
//	@ResponseBody
//	@RequestMapping("logout")
//	public ResultModel logout(){
//		SecurityUtils.getSubject().logout();
//		return new ResultModel(true,"");
//	}
	
	@RequestMapping("logout")
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "redirect:/user/login.html";
	}
}
