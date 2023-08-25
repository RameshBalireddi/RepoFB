package post.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(name = "Ramesh",email = "ramesh@gmail.com"),
                description = "Post Fb",
                title = "Instagram"),
        servers = { @Server( description = "Local Server", url = "http://localhost:8080")
        }
)
@SecurityRequirement(name = "basicAuth")
public class OpenApiConfig {

}
