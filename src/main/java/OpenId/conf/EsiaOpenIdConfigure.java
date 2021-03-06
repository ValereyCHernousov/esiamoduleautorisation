package OpenId.conf;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import OpenId.utils.DateConverter;
import OpenId.utils.KeyConverter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Configuration
@EnableOAuth2Client
public class EsiaOpenIdConfigure {


    UUID uuid = UUID.randomUUID();
    String state = uuid.toString();
    Date date = new Date();
    String scope = "fullname";
    String timestamp = DateConverter.converterToTimestamp(date).toString();



    @Value("${esia.clientId}")
    private String clientId;

    private String clientSecret = KeyConverter.converter();

    @Value("${esia.accessTokenUri}")
    private String accessTokenUri;

    @Value("${esia.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${esia.redirectUri}")
    private String redirectUri;

    public EsiaOpenIdConfigure() throws GeneralSecurityException, IOException {
    }

    @Bean
    public OAuth2ProtectedResourceDetails esiaOpenId() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(Arrays.asList("fullname"));
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setGrantType("authorization_code");
        details.setUseCurrentUri(false);
        return details;
    }

    @Bean
    public OAuth2RestTemplate esiaOpenIdTemplate(OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(esiaOpenId(), clientContext);
    }


}

