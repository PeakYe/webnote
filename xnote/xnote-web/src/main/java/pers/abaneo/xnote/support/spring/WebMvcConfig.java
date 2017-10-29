package pers.abaneo.xnote.support.spring;


import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import pers.abaneo.xnote.support.interceptor.FreemarkInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserDetailHandlerMethodArgumentResolver());
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FreemarkInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
