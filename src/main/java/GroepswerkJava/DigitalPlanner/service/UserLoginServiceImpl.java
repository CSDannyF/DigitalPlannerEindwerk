package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.Role;
import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private UserLoginRepository userLoginRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private StaffRepository staffRepository;

    @Autowired
    public UserLoginServiceImpl(UserLoginRepository userLoginRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder,
                                StaffRepository staffRepository) {
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
    }


    @Override
    public void saveUser(UserLoginDto userLoginDto) {
        UserLogin user = new UserLogin();
        user.setEmail(userLoginDto.getEmail());
        user.setChangePassword(true);

        if(userLoginDto.getStaffDto() != null)
        {
            StaffServiceImpl staffService = new StaffServiceImpl(staffRepository);
            user.setStaff(staffService.mapToStaffModel(userLoginDto.getStaffDto()));
        }

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
    public UserLoginDto findByEmail(String email) {
        UserLoginDto userLoginDto = new UserLoginDto();
        if(userLoginRepository.findByEmail(email) != null) {
            userLoginDto = mapToUserLoginDto(userLoginRepository.findByEmail(email));
        }
        return userLoginDto;
    }

    @Override
    public UserLoginDto findById(Long id) {
        return mapToUserLoginDto(userLoginRepository.findById(id).get());
    }

    @Override
    public List<UserLoginDto> findAllUsers() {
        List<UserLogin> users = userLoginRepository.findAll();
        return users.stream().map((userLogin) -> mapToDtoWithId(userLogin))
                .collect(Collectors.toList());
    }

    private UserLoginDto mapToUserLoginDto(UserLogin user){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setChangePassword(user.getChangePassword());
        return userLoginDto;
    }

    private UserLoginDto mapToDtoWithId(UserLogin user){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setId(user.getUserLoginId());
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setRoles(user.getRoles());
        userLoginDto.setChangePassword(user.getChangePassword());
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
