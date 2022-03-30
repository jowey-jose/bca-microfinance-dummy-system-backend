package com.josephmbukudev.bcamicrofinancedummysystembackend.security.services;

import com.josephmbukudev.bcamicrofinancedummysystembackend.models.User;
import com.josephmbukudev.bcamicrofinancedummysystembackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// UserDetailsService is needed for getting UserDetails Object.
// UserDetailsService has only One Method,
// So here we Implement and Override the LoadByUsername() method.
@Service
public class UsrDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

//  Getting the full Custom 'User' object from UserRepository,
//    then build UserDetails object using static build() method.
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username" + username));
        return UserDetailsImpl.build(user);
    }
}
