package com.example.TravelAgency.config.security.jwt;

import com.example.TravelAgency.config.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtTokenGenerator tokenGenerator;

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=getJWTFromRequest(request);

        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)){
            String username=tokenGenerator.getUsernameFromJWT(token);

            UserDetails userDetails=userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            System.out.println("-------> Authorities : "+authenticationToken.getAuthorities());

//            authenticationToken.setDetails(new AuthenticationDetailsSource() {
//                @Override
//                public Object buildDetails(Object context) {
//                    return null;
//                }
//            }.buildDetails(request));
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);



            authenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println("Username: " + userDetails.getUsername() + ", Roles: " + userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request,response);

    }

    private String getJWTFromRequest(HttpServletRequest request){
        String bearerToken=request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
