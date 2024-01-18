package com.packsize;

import java.util.Arrays;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main extends SpringBootServletInitializer{
	

    public static void main (String[] args) {
    	SpringApplication.run(Main.class, args);
    }
    
    @Bean
    ServletRegistrationBean jsfServletRegistration (ServletContext servletContext) {
        //spring boot only works if this is set
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

        //registration
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new FacesServlet());
        srb.setUrlMappings(Arrays.asList("*.xhtml"));
        srb.setLoadOnStartup(1);
        return srb;
    }
    
    @Bean
    public FilterRegistrationBean FileUploadFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new org.primefaces.webapp.filter.FileUploadFilter());
        registration.setName("PrimeFaces FileUpload Filter");
        return registration;
    }
    
    @Bean
    public ServletContextInitializer initializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext)
                    throws ServletException {
				/*
				 * servletContext.setInitParameter("primefaces.THEME", "bluesky");
				 * servletContext.setInitParameter( "javax.faces.FACELETS_SKIP_COMMENTS",
				 * "true"); servletContext.setInitParameter( "com.sun.faces.expressionFactory",
				 * "com.sun.el.ExpressionFactoryImpl");
				 */
            	servletContext.setInitParameter("primefaces.THEME", "start");
				/* servletContext.setInitParameter("primefaces.THEME", "luna-green"); */
                servletContext.setInitParameter("primefaces.UPLOADER",
                        "commons");
            }
        };
    }
}