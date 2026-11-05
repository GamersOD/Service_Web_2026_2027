package rendu.georgesvalerian.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ExpressionJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.oauth2ResourceServer((oauth2) -> oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
        return http.build();
    }

    //crée un converter pour ajouter les roles au JWT
    public static ExpressionJwtGrantedAuthoritiesConverter getConverter(){

        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("['resource_access']['coursm2']['roles']");
        ExpressionJwtGrantedAuthoritiesConverter converter = new ExpressionJwtGrantedAuthoritiesConverter(expression);
        converter.setAuthorityPrefix("ROLE_");

        return converter;
    }

    //ajoute le converter a l'authentication converter et le rend disponible pour securityfilterchain
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        ExpressionJwtGrantedAuthoritiesConverter converter = getConverter();

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    }



}
