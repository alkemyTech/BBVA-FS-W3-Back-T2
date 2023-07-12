package com.bbva.wallet.config;

import com.bbva.wallet.exceptions.ErrorCodes;
import com.bbva.wallet.exceptions.Response;
import com.bbva.wallet.services.JwtService;
import com.bbva.wallet.services.AuthorizationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthorizationService authorizationService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserName(jwt);
            if (StringUtils.isNotEmpty(userEmail)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authorizationService.userDetailsService()
                        .loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        } catch (ExpiredJwtException ex) {
            handleJwtException(response, ErrorCodes.EXPIRED_JWT, "El JWT ha expirado");
            return;
        } catch (SignatureException ex) {
            handleJwtException(response, ErrorCodes.INVALID_JWT_SIGNATURE, "La firma del JWT es inválida");
            return;
        } catch (MalformedJwtException ex) {
            handleJwtException(response, ErrorCodes.MALFORMED_JWT, "El JWT está mal formado");
            return;
        } catch (UnsupportedJwtException ex) {
            handleJwtException(response, ErrorCodes.UNSUPPORTED_JWT, "El JWT no es compatible");
            return;
        } catch (IllegalArgumentException ex) {
            handleJwtException(response, ErrorCodes.INVALID_JWT_ARGUMENT, "Argumento inválido para el JWT");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleJwtException(HttpServletResponse response, ErrorCodes errorCode, String message) throws IOException {
        Response<String> errorResponse = new Response<>();
        errorResponse.addError(errorCode);
        errorResponse.setMessage(message);
        errorResponse.setData("JWT");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }

}