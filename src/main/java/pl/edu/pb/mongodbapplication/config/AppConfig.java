package pl.edu.pb.mongodbapplication.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiKeyAuthDefinition;
import lombok.Value;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class AppConfig {
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean lfb) {
        return new ValidatingMongoEventListener(lfb);
    }

    @Configuration
    @EnableSwagger2
    public static class SwaggerConfiguration {

        @Bean
        public LinkDiscoverers discoverers() {
            List<LinkDiscoverer> plugins = new ArrayList<>();
            plugins.add(new CollectionJsonLinkDiscoverer());
            return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
        }

        @Bean
        public Docket jsonApi() {
            return new Docket(SWAGGER_2)
                    .securitySchemes(singletonList(new ApiKey("JWT", AUTHORIZATION, ApiKeyAuthDefinition.ApiKeyLocation.HEADER.name())))
                    .securityContexts(singletonList(
                            SecurityContext.builder()
                                    .securityReferences(
                                            singletonList(SecurityReference.builder()
                                                    .reference("JWT")
                                                    .scopes(new AuthorizationScope[0])
                                                    .build()
                                            )
                                    )
                                    .build())
                    )
                    .select()
                    .build();
        }
    }
}