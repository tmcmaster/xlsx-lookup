package au.id.mcmaster.xlsxlookup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
//@ComponentScan(basePackageClasses = {
//		XLSXLookupController.class
//})
public class XSLXLookupSpringBoot {

	public static void main(String[] args) {
		SpringApplication.run(XSLXLookupSpringBoot.class, args);
	}
}


@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.id.mcmaster.xlsxlookup"))
                .build();
             
    }
}