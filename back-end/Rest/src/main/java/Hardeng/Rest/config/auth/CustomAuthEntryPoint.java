package Hardeng.Rest.config.auth;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/** Defines behaviour in case of unauthorized access to auth-protected endpoint */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = 2484418356758288365L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException 
    {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
