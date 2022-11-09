package Clips.backend.security.jwt;

import Clips.backend.security.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Define a filter that executes once per request
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Makes a single execution for each request to our API:
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Get JWT by parsing the HTTP Cookies:
            String jwt = parseJwt(request);

            // If the request has JWT, validate it:
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Parse username from it:
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // From username, get UserDetails:
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Use UserDetails to create an Authentication object:
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        // Checking Authorization:
                        userDetails.getAuthorities()
                    );

                // Set user current request details:
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // set the current UserDetails with the user's request in SecurityContext
                // using setAuthentication(authentication) method.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        // Causes the next filter in the chain to be invoked,
        // or if the calling filter is the last filter in the chain,
        // causes the resource at the end of the chain to be invoked.
        // request - the request to pass along the chain.
        // response - the response to pass along the chain.
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        return jwt;
    }
}
