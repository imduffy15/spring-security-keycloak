package springfox.documentation.swagger2.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.PropertySourcedMapping;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Controller
@ApiIgnore
public class Swagger2Controller {
    private final String authorizationUrlOverride;
    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;
    private final JsonSerializer jsonSerializer;
    private final ObjectMapper yaml;
    private final JsonNode node;

    @Autowired
    public Swagger2Controller(
        Environment environment,
        DocumentationCache documentationCache,
        ServiceModelToSwagger2Mapper mapper,
        JsonSerializer jsonSerializer) throws IOException {
        this.authorizationUrlOverride = environment.getProperty("security.default-resource.authorization-token-uri");
        this.documentationCache = documentationCache;
        this.mapper = mapper;
        this.jsonSerializer = jsonSerializer;
        this.yaml = new ObjectMapper(new YAMLFactory());
        this.node = yaml.readTree(getClass().getResourceAsStream(environment.getProperty("swagger.path")));
    }

    @RequestMapping(
        value = {"/v2/api-docs"},
        method = {RequestMethod.GET},
        produces = {"application/json", "application/hal+json"}
    )
    @PropertySourcedMapping(
        value = "${springfox.documentation.swagger.v2.path}",
        propertyKey = "springfox.documentation.swagger.v2.path"
    )
    @ResponseBody
    public ResponseEntity<Json> getDocumentation(@RequestParam(value = "group", required = false) String swaggerGroup, HttpServletRequest servletRequest) {
        String groupName = Optional.ofNullable(swaggerGroup).orElse("default");
        Documentation documentation = this.documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Swagger swagger = new SwaggerParser().read(node);

            Optional.ofNullable(swagger.getSecurityDefinitions()).ifPresent(securitySchemeDefinitionMap -> {
                SecuritySchemeDefinition updatedSchemeDefinition = Optional.ofNullable(securitySchemeDefinitionMap.get("openId")).map(securitySchemeDefinition -> {
                    if (securitySchemeDefinition.getType().equals("oauth2")) {
                        ((OAuth2Definition) securitySchemeDefinition).setAuthorizationUrl(authorizationUrlOverride);
                    }
                    return securitySchemeDefinition;
                }).orElse(null);

                securitySchemeDefinitionMap.replace("openId", updatedSchemeDefinition);
            });

            return new ResponseEntity(this.jsonSerializer.toJson(swagger), HttpStatus.OK);
        }
    }
}
