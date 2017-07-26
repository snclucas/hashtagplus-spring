package com.hashtagplus;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.hashtagplus.config.filter.APITokenFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

@SpringBootApplication
public class App {

    private static Log logger = LogFactory.getLog(App.class);

    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
                logger.info("ServletContext initialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                logger.info("ServletContext destroyed");
            }

        };
    }


    @Bean
    public FilterRegistrationBean apiTokenFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new APITokenFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }


    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                // Customize the default entries in errorAttributes to suit your needs

                errorAttributes.remove("exception");
                errorAttributes.remove("error");
                errorAttributes.remove("timestamp");

                return errorAttributes;
            }
        };
    }




    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}