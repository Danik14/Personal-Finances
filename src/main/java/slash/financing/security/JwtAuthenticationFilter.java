package slash.financing.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import slash.financing.dto.ErrorDto;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserDetailsServiceImpl userDetailsService;

    // Might need to remove @NonNulls because there are basic constraints
    // defined by the parent class, which should be considered when overriding
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            // If we have a user email and the user is not authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Get user details from the database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                // If the user is valid
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Updating a security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // Continue the chain of filters
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            response.getWriter().write(
                    objectMapper.writeValueAsString(ErrorDto.builder()
                            .timestamp(new Date())
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .path(request.getServletPath())
                            .message(e.getMessage())
                            .build()));
        }
        // No finally needed since if we catch an error, the chain should stop
    }

}