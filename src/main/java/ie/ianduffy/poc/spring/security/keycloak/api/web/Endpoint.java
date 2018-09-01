package ie.ianduffy.poc.spring.security.keycloak.api.web;

import ie.ianduffy.poc.spring.security.keycloak.api.web.model.PathVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
public class Endpoint implements DefaultApi {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${spring.application.name}")
    private String self;

    @Value("${downstream.url}")
    private String downstream;


    @Override
    public CompletableFuture<ResponseEntity<PathVM>> apiEndpointForwardGet(String from) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Handling request for '{}' on '{}' from '{}'",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
                self,
                from
            );
            PathVM pathVM = new PathVM();
            pathVM.setName(from);
            pathVM.setChild(oAuth2RestTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(downstream)
                    .queryParam("from", self)
                    .toUriString(),
                PathVM.class
            ).getBody());

            return new ResponseEntity<>(pathVM, HttpStatus.OK);
        });
    }

    @Override
    public CompletableFuture<ResponseEntity<PathVM>> apiEndpointBaseGet(String from) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Handling request for '{}' on '{}' from '{}'",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
                self,
                from
            );
            PathVM pathVM = new PathVM();
            pathVM.setName(from);

            PathVM selfPathVM = new PathVM();
            selfPathVM.setName(self);

            pathVM.setChild(selfPathVM);
            return new ResponseEntity<>(pathVM, HttpStatus.OK);
        });
    }
}
