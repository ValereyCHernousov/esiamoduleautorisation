package ScribeJava;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

public class MyApi extends DefaultApi20 {

    private MyApi() {
    }

    private static class InstanceHolder {
        private static final MyApi INSTANCE = new MyApi();
    }

    public static MyApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://esia-portal1.test.gosuslugi.ru/aas/oauth2/te";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig oAuthConfig) {
        return null;
    }
}
