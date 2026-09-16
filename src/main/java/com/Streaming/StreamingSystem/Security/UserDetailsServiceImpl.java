package com.Streaming.StreamingSystem.Security;

import com.Streaming.StreamingSystem.DTO.AuthCreateUserRequest;
import com.Streaming.StreamingSystem.DTO.AuthLoginRequest;
import com.Streaming.StreamingSystem.DTO.AuthResponse;
import com.Streaming.StreamingSystem.DTO.ChangePlanRequest;
import com.Streaming.StreamingSystem.Exception.Custom.ResourceAlreadyExistsException;
import com.Streaming.StreamingSystem.Exception.Custom.ResourceNotFoundException;
import com.Streaming.StreamingSystem.Model.Enums.PlanEnum;
import com.Streaming.StreamingSystem.Model.Enums.RoleEnum;
import com.Streaming.StreamingSystem.Model.RoleEntity;
import com.Streaming.StreamingSystem.Model.UserEntity;
import com.Streaming.StreamingSystem.Repository.RoleRepository;
import com.Streaming.StreamingSystem.Repository.UserRepository;
import jakarta.persistence.EnumType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("El usuario: " + username + " no existe"));

       List<GrantedAuthority>authorities = List.of(new SimpleGrantedAuthority("ROLE_".concat
               (user.getRoleEntity().getRoleEnum().name())));

        return new User(user.getUsername(),
                user.getPassword(),
                authorities
                );
    }

    public Authentication authentication (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Usuario invalido o contraseña");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Contraseña invalida");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse loginUser(AuthLoginRequest loginRequest){
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username, "Usuario verificado", token, true);

        return authResponse;
    }

    public AuthResponse createUser(AuthCreateUserRequest createUserRequest){

        String email = createUserRequest.email();
        String username = createUserRequest.username();
        String password = createUserRequest.password();
        PlanEnum userPlan = createUserRequest.plan();

        userRepository.findByUsername(username).ifPresent(user ->{
            throw new ResourceAlreadyExistsException("El usuario con nombre: " + username + " ya existe");
        });

        userRepository.findByEmail(email).ifPresent(userEmail ->{
            throw new ResourceAlreadyExistsException("el usuario con email: " +  email + " ya existe");
        });

       RoleEnum roleEnumToFind = (userPlan == PlanEnum.PREMIUM)
               ?RoleEnum.PREMIUM
               :RoleEnum.CUSTOMER;

       RoleEntity role = roleRepository.findByRoleEnum(roleEnumToFind)
              .orElseThrow(()-> new ResourceNotFoundException("Plan no encontrado"));

       UserEntity user =  UserEntity.builder()
               .username(username)
               .email(email)
               .password(passwordEncoder.encode(password))
               .roleEntity(role)
               .isEnabled(true)
               .build();

       UserEntity userCreated = userRepository.save(user);

       List<SimpleGrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name())));
       Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(),null,  authorityList );



       String token = jwtUtils.createToken(authentication);

       AuthResponse authResponse = new AuthResponse(userCreated.getUsername(), "Usuario creado ", token, true);

       return authResponse;

    }

    public AuthResponse changePlan (String username, ChangePlanRequest planRequest){

       UserEntity user = userRepository.findByUsername(username)
               .orElseThrow(()-> new ResourceNotFoundException("Usuario con nombre: " + username + " no encontrado"));

       RoleEnum role = (planRequest.changePlan() == PlanEnum.PREMIUM)
               ?RoleEnum.PREMIUM
               :RoleEnum.CUSTOMER;

       RoleEntity newRole = roleRepository.findByRoleEnum(role)
               .orElseThrow(()-> new ResourceNotFoundException("El rol solicitado no existe"));

       user.setRoleEntity(newRole);
       UserEntity userUpdate = userRepository.save(user);

       userRepository.save(user);

       List<GrantedAuthority>authorities = List.of(new SimpleGrantedAuthority("ROLE_".concat(newRole.getRoleEnum().name())));
       Authentication authenticationUser = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
       String newToken = jwtUtils.createToken(authenticationUser);

       return new AuthResponse(user.getUsername(), "Plan actualizado con exito", newToken, true);

    }
}
