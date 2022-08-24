package com.rushuni.sh.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @author Marshall
 * @date 2022/8/9
 */
@Component
public class BeanConfig {
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/templates");
        freeMarkerConfigurer.setDefaultEncoding("UTF-8");
        return freeMarkerConfigurer;

    }
}
