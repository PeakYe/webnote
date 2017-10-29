package pers.abaneo.xnote;

import javax.servlet.DispatcherType;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import pers.abaneo.xnote.support.filter.XNUrlRewriteFilter;


@EnableAutoConfiguration
@MapperScan("pers.abaneo.xnote.api.dao")
@ComponentScan("pers.abaneo.xnote")
public class XnoteWeb {
	public static void main(String[] args) {
		SpringApplication.run(XnoteWeb.class, args);
	}
	
	
	
//	@Bean
//	public FilterRegistrationBean urlRewriteFilter() {
//		FilterRegistrationBean myFilter = new FilterRegistrationBean();
//		myFilter.addUrlPatterns("/*");
//		UrlRewriteFilter filter=new XNUrlRewriteFilter();
//		myFilter.addInitParameter("logLevel", "DEBUG");
//		myFilter.addInitParameter("confPath", "classpath*:/WEB-INF/classes/urlrewrite.xml");
//		myFilter.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.FORWARD);
//		myFilter.setFilter(filter);
////		myFilter.setOrder(0);
//		return myFilter;
//	}

//	@Bean  
//	public FilterRegistrationBean shiroWebFilter() {  
//		FilterRegistrationBean myFilter = new FilterRegistrationBean();  
//		myFilter.addUrlPatterns("/*");  
//		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
//	    proxy.setTargetFilterLifecycle(true);
//	    proxy.setTargetBeanName("shiroFilter");
//		myFilter.setFilter(proxy);
////		myFilter.setOrder(10000);
//		return myFilter;
//	}
//	
	
//	@Bean  
//    public FilterRegistrationBean sessionSecurityFilter() {  
//        FilterRegistrationBean myFilter = new FilterRegistrationBean();  
//        myFilter.addUrlPatterns("/*");  
//        myFilter.setFilter(new SessionSecurityFilter());
//        return myFilter;
//    }  
}
