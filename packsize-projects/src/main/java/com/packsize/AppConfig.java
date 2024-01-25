package com.packsize;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.packsize.*")
public class AppConfig implements WebMvcConfigurer{
 
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/")
          .setViewName("forward:/login.xhtml");
      registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
	
}