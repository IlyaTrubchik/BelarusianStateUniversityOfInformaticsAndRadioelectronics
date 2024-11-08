package by.bsuir.ilya.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("by.bsuir.ilya")
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    //чтобы спринг автоматически внедрил контекст
    @Autowired
    public  WebConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
