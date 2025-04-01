package edu.icet.demo.service.impl;

import edu.icet.demo.dto.auth.UserPrincipal;
import edu.icet.demo.entity.UserEntity;
import edu.icet.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found !");
        }
        Hibernate.initialize(user.getRoleList());
        return new UserPrincipal(user);
    }
}