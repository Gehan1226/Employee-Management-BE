package edu.icet.demo.service.impl;

import edu.icet.demo.dto.UserPrincipal;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity users = userRepository.findByUserName(username);
        if (users == null){
            throw new UsernameNotFoundException("User not found !");
        }
        return new UserPrincipal(users);
    }
}