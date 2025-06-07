package backend.backend.dao;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public interface UserDetailsInterface extends org.springframework.security.core.userdetails.UserDetails {
    Collection<? extends GrantedAuthority> getAuthorities();

}
