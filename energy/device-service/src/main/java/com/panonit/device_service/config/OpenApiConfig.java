package com.panonit.device_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI deviceServiceApiDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Device Service API")
                        .description("Device Service API Documentation")
                        .contact(getContact())
                        .license(getLicense())
                        .version("0.0.1-SNAPSHOT")
                );
    }

    private Contact getContact() {
        var contact = new Contact();
        contact.setName("Panonit");
        contact.setUrl("https://panonit.com");
        contact.setEmail("panonit@panonit.com");

        return contact;
    }

    private License getLicense() {
        var licence = new License();
        licence.setName("GPLv3");
        licence.setUrl("https://www.gnu.org/licenses/gpl-3.0.en.html");

        return licence;
    }
}
