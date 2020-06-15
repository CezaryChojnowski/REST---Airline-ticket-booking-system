package pl.edu.pb.mongodbapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class MongodbapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbapplicationApplication.class, args);
    }

}
