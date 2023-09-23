package slash.financing.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import slash.financing.data.User;
import slash.financing.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with such username"));

        return build(user);
    }

    public UserDetails build(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.isActive(),
                user.isActive(),
                user.isActive(),
                user.isActive(),
                user.getAuthorities());
    }
}