package edu.tcu.cs.hogwartsartifactsonline.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This class is responsible for handling unsuccessful basic authentication. If a basic authentication fails then
 * the commence method is called. A header is first added and then the work is delegated to the HandlerExceptionResolver
 * and the resolver will resolve the exception so the exception can be handled by an exceptionHandler in this application's
 * @ControllerAdvice class.
 */
@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*
     * Here we've injected the DefaultHandlerExceptionResolver and delegated the handler to this resolver.
     * This security exception can now be handled with controller advice with an exception handler method.
     */
    private final HandlerExceptionResolver resolver;

    /**
     * We need to use the @Qualifier annotation because there may be multiple beans of type "HandlerExceptionResolver"
     * so we need to explicitly tell Spring which one to look for.
     * @param resolver
     */
    public CustomBasicAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"Realm\"");
        this.resolver.resolveException(request, response, null, authException);
    }
}
