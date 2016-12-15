package com.upkar.springdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ResourceInjection {

    private Resource resource;
    private final String path;
    private final ApplicationContext applicationContext;

    @Autowired
    public ResourceInjection(@Value("classpath:data.json") String path, ApplicationContext app){
        this.path = path;
        this.applicationContext = app;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return applicationContext.getResource(path);
    }
}