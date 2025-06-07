package backend.backend.security;

import backend.backend.dao.UserDetailsInterface;
import backend.backend.services.UserDetailsInterfaceService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWtAuthenticatorFilter extends OncePerRequestFilter {

    @Autowired
    private JWUtils jwtService;

    @Autowired
    private UserDetailsInterfaceService userDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String dni;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        dni = jwtService.getDniFromToken(jwt);

        if(dni != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetailsInterface = userDetailsService.loadUserByUsername(dni);

            if(jwtService.validateToken(jwt, (UserDetailsInterface) userDetailsInterface)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetailsInterface,
                                null,
                                userDetailsInterface.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
        }

        filterChain.doFilter(request, response);
    }
}
