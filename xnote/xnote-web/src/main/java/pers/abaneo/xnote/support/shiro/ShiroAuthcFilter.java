package pers.abaneo.xnote.support.shiro;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.mysql.jdbc.Security;

import pers.abaneo.web.utils.WebUtil;
import pers.abaneo.web.utils.security.EncryptUtil;

/**
 * 表单认证过滤器，可以在这里记录登录成功后的日志记录等操作
 * 
 * @author dj
 * 
 */
public class ShiroAuthcFilter extends FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {

		return captchaParam;

	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse rep, FilterChain arg2)
			throws ServletException, IOException {
		HttpServletRequest req=(HttpServletRequest) request;
		Cookie sessionid=org.springframework.web.util.WebUtils.getCookie(req, "sid");
		if(sessionid!=null){
			Cookie sign=org.springframework.web.util.WebUtils.getCookie(req, "jsign");
			if(sign==null){
				System.out.println("session id 没有签名");
//				SecurityUtils.getSubject().logout();
			}else{
				String agent=req.getHeader("User-Agent");
				String ip=WebUtil.getRemoteHost(req);
				String ssign=null;
				try {
					ssign=EncryptUtil.EncoderByMd5(sessionid.getValue()+ip+agent);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!sign.getValue().equals(ssign)){
					System.out.println("签名校验失败：");
					System.out.println(sign.getValue());
					System.out.println(ssign);
					SecurityUtils.getSubject().logout();
				}
			}
		}
		super.doFilterInternal(req, rep, arg2);
	}
	

	// 登录成功操作,这里设置了代理商常用信息
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse rep=(HttpServletResponse) response;
		String agent=req.getHeader("User-Agent");
		String ip=getRemoteHost(req);
		String ssign=null;
		try {
			ssign=EncryptUtil.EncoderByMd5(req.getSession().getId()+ip+agent);
			Cookie cookie=new Cookie("jsign", ssign);
			rep.addCookie(cookie);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}