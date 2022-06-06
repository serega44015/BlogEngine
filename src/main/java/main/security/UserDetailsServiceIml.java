package main.security;

import main.model.User;
import main.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("userDetailsServiceIml")
public class UserDetailsServiceIml implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceIml(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("user" + email + " not found");
    }

    return SecurityUser.fromUser(user);
  }
}
