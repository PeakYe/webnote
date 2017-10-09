package pers.abaneo.xnote.support.filter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.util.WebUtils;

import pers.abaneo.web.utils.WebUtil;
import pers.abaneo.web.utils.security.EncryptUtil;

public class SessionSecurityFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse rep=(HttpServletResponse) response;
		
//		HttpSession session=req.getSession(true);
		Session ssession = SecurityUtils.getSubject().getSession();
		if(SecurityUtils.getSubject().isAuthenticated()){
//		if(ssession!=null){
			Cookie sessionid=WebUtils.getCookie(req, "sid");
			if(sessionid!=null){
				Cookie sign=WebUtils.getCookie(req, "jsign");
				if(sign==null){
					System.out.println("session id 没有签名");
					SecurityUtils.getSubject().logout();
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
			
		}else{
//			req.getRequestDispatcher("/user/login.html").forward(request, response);
			System.out.println(">>>>>>>>>>>> not login");
		}
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
