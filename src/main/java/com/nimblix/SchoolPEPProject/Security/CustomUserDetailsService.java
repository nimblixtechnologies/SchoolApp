package com.nimblix.SchoolPEPProject.Security;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.ParentRepository;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ParentRepository parentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Parent parent = parentRepository.findByEmailId(email)
                .orElseThrow(() -> new UsernameNotFoundException("Parent not found"));

        return new org.springframework.security.core.userdetails.User(
                parent.getEmailId(),
                parent.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + parent.getRole().name()))
        );
    }
}
