package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.Role;
import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    public void saveUser(UserLoginDto userLoginDto) {
        UserLogin user = new UserLogin();
        user.setEmail(userLoginDto.getEmail());

        user.setPassword(passwordEncoder.encode(userLoginDto.getPassword()));
        Role role;
        if(userLoginRepository.findAll().isEmpty()){
            initiateRoles();
            role = roleRepository.findByRoleName("ROLE_ADMIN");

        } else {
            role = roleRepository.findByRoleName("ROLE_USER");

        }

        user.getRoles().add(role);
        //TODO delete? user.setRoles(Arrays.asList(role));
        userLoginRepository.save(user);
    }

    @Override
    public UserLogin findByEmail(String email) {
        return userLoginRepository.findByEmail(email);
    }

    @Override
    public UserLogin findById(Long id) {
        return userLoginRepository.findById(id).get();
    }

    @Override
    public List<UserLoginDto> findAllUsers() {
        List<UserLogin> users = userLoginRepository.findAll();
        return users.stream().map((userLogin) -> convertEntityToDtoWithId(userLogin))
                .collect(Collectors.toList());
    }

    private UserLoginDto convertEntityToDto(UserLogin user){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        return userLoginDto;
    }

    private UserLoginDto convertEntityToDtoWithId(UserLogin user){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setId(user.getUserLoginId());
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setRoles(user.getRoles());
        return userLoginDto;
    }

    private void initiateRoles() {
        Role admin = new Role();
        admin.setRoleName("ROLE_ADMIN");
        Role user = new Role();
        user.setRoleName("ROLE_USER");
        roleRepository.save(admin);
        roleRepository.save(user);
    }
}
