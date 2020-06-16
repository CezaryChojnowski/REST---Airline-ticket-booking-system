package pl.edu.pb.mongodbapplication.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.edu.pb.mongodbapplication.interceptor.Interceptor;

@Component
public class InterceptorAppConfig extends WebMvcConfigurerAdapter {
    final
    Interceptor interceptor;

    public InterceptorAppConfig(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
