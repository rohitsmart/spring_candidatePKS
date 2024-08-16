package com.candidate.pks.security;

import com.candidate.pks.exception.ErrorPOJA;
import com.candidate.pks.repeat.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final List<String> excludedURLs = Arrays.asList(
            "/test/**",
            "/api/v1/public/**",
            "/swagger-ui/index.html",
            "/v3/**"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;
            if (authHeader == null ||!authHeader.startsWith("Bearer ")) {

                filterChain.doFilter(request, response);
                    return;

            }
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            filterChain.doFilter(request, response);
        }
        catch(Exception e)
        {
            ErrorPOJA errorPOJO= new ErrorPOJA();
            errorPOJO.setError_description(e.getLocalizedMessage());
            errorPOJO.setUser_description("token error");
            String str= e.getLocalizedMessage();
            boolean findInString= str.contains("JWT expired");
            boolean logOut= str.contains("UserToken.getToken()\" is null");
            boolean tokenExistOrNot= str.contains("token not found");
            boolean isToken = str.contains("Full authentication is required to access this resource");
            if(logOut)
            {
                errorPOJO.setCode("351");
                errorPOJO.setError_description("token has been removed by system , log in again");
                errorPOJO.setUser_description("you have been log out ,");
            }
            else if (isToken)
            {
                errorPOJO.setError_description("Bearer token is missing or invalid.");
                errorPOJO.setUser_description("Please provide a valid Bearer token.");
                errorPOJO.setCode("401");
            }
          else  if(findInString)
            {
                errorPOJO.setUser_description("token expired");
                errorPOJO.setCode("350");
            }
          else if(tokenExistOrNot)
            {
                errorPOJO.setCode("352");
                errorPOJO.setUser_description("you have been hard log out try login again");
            }
            else
            {
                errorPOJO.setCode(String.valueOf(response.getStatus()));
            }
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            errorPOJO.setRequest("jwt configuration ");
            errorPOJO.setDate(DateUtils.sdfErrorPOJO.format(new Date()));
            new ObjectMapper().writeValue(response.getOutputStream(),errorPOJO);
        }
    }
}