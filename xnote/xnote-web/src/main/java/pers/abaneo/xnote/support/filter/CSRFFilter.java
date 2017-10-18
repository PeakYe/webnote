package pers.abaneo.xnote.support.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRFFilter implements Filter{
	private Logger logger = LoggerFactory.getLogger(CSRFFilter.class);
	private Pattern urlget;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String geturl="(://)(.*?)(/|$)";
		urlget = Pattern.compile(geturl);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		String referer = req.getHeader("referer");
//		System.out.println(referer);
		if(referer!=null){
			String refHost=getDomainName(referer);
			
			String Origin=req.getHeader("Origin");
			if(Origin!=null && refHost.equals(getDomainName(Origin))){
				chain.doFilter(request, response);
			}else{
				//使用代理，不包含端口
				String XHost = req.getHeader("X-Forwarded-Host");
				String refHostNoPort=getHost(refHost);
				if(XHost!=null && refHostNoPort!=null && refHostNoPort.equals(XHost)){
					chain.doFilter(request, response);
				}else{
					//不使用代理，包含端口
					String Host=req.getHeader("Host");
					if(Host!=null && refHost.equals(Host)){
						chain.doFilter(request, response);
					}else{
						logger.warn("拒绝请求:"+req.getRequestURL()+"\n请求Origin:"+Origin+"\n请求Host:"+Host+"\n请求referer:"+referer+"\n请求X-Forwarded-Host:"+XHost);
						((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NOT_FOUND);
						return;
					}
				}
			}
		}else{
			chain.doFilter(request, response);
		}
	}
	
	public String getDomainName(String url) {
		Matcher matcher = urlget.matcher(url);
		while(matcher.find()){
			return matcher.group(2);
		}
		return null;
    }
	
	public String getHost(String url) {
		String geturl="^(.*?)(:|$)";
		Pattern pattern = Pattern.compile(geturl);
		Matcher matcher = pattern.matcher(url);
		while(matcher.find()){
			return matcher.group(1);
		}
		return null;
    }
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
