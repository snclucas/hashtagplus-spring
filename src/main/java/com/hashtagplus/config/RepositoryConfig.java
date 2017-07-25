package com.hashtagplus.config;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Message.class);
        config.exposeIdsFor(Hashtag.class);
    }
}