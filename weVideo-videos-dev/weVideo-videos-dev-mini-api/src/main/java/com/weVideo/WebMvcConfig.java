package com.weVideo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.weVideo.controller.interceptor.MiniInterceptor;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("file:G:/wevideo_res/");
	}
	
	@Bean
	public MiniInterceptor miniInterceptor() {
		return new MiniInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
		.excludePathPatterns("/user/queryPublisher")
		.addPathPatterns("/bgm/**").addPathPatterns("/video/upload","/video/uploadCover",
													"/video/userLike","/video/userUnlike"
													,"video/saveComment");
		super.addInterceptors(registry);
		
	}

}
