package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserDto;
import GroepswerkJava.DigitalPlanner.model.Role;
import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    public UserLoginRepository userLoginRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserLoginServiceImpl(UserLoginRepository userLoginRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        UserLogin user = new UserLogin();
        user.setEmail(userDto.getEmail());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByRoleName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userLoginRepository.save(user);
    }

    @Override
    public UserLogin findByEmail(String email) {
        return userLoginRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserLogin> users = userLoginRepository.findAll();
        return users.stream().map((userLogin) -> convertEntityToDto(userLogin))
                .collect(Collectors.toList());
    }

    private UserDto convertEntityToDto(UserLogin user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        return roleRepository.save(role);
    }
}
