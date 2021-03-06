package com.key.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCSupport implements WebMvcConfigurer {

	@Autowired
    private VerifyAccessInterceptor verifyAccessInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(verifyAccessInterceptor).addPathPatterns("/**");
  }
  
//  @Override
//	public void addCorsMappings(CorsRegistry registry) {
//	  registry.addMapping("/**")
//      .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
//	}
  
}
