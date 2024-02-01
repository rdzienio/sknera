package pl.gienius.sknera.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/signup").setViewName("registerUser");
        registry.addViewController("/url_error403").setViewName("accessDenied");
    }
    //…Tutaj można też zarejestrować globalne formatery
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //globalna obsługa daty w formularzach
        registry.addFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")//mapowanie
                .addResourceLocations("file:D://springimages/");//fizyczna lokaliz…
        //Dodanie obsługi pozostałych zasobów
    }

}
