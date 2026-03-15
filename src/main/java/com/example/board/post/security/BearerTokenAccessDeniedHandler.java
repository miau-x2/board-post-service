package com.example.board.post.security;

import com.example.board.post.commons.response.ApiResponse;
import com.example.board.post.commons.response.CommonErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.LinkedHashMap;

import static com.example.board.post.commons.utils.BearerHeaderUtils.computeWWWAuthenticateHeaderValue;

@Component
@RequiredArgsConstructor
public class BearerTokenAccessDeniedHandler implements AccessDeniedHandler {
    private final JsonMapper jsonMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        var parameters = new LinkedHashMap<String, String>();
        parameters.put("error", BearerTokenErrorCodes.INSUFFICIENT_SCOPE);
        parameters.put("error_description", "The request requires higher privileges than provided by the access token.");

        var wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
        var code = CommonErrorCode.ACCESS_DENIED;
        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.addHeader("WWW-Authenticate", wwwAuthenticate);

        var body = ApiResponse.error(code);
        jsonMapper.writeValue(response.getWriter(), body);
    }
}
