package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
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
                                StaffRepository staffRepository)
    {
        this.userLoginRepository = userLoginRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.staffRepository = staffRepository;
    }

    @Override
    public void saveUser(UserLoginDto userLoginDto)
    {
        UserLogin user = new UserLogin();
        user.setEmail(userLoginDto.getEmail());


        if (userLoginDto.getStaffDto() != null)
        {
            StaffServiceImpl staffService = new StaffServiceImpl(staffRepository);
            user.setStaff(staffService.mapToStaffModel(userLoginDto.getStaffDto()));
        }
        if (userLoginDto.getId() != null)
        {
            user.setUserLoginId(userLoginDto.getId());
        }

        if (userLoginDto.getPassword() == null)
        {
            UserLogin temp = userLoginRepository.findByUserLoginId(userLoginDto.getId());
            user.setPassword(temp.getPassword());
            user.setChangePassword(temp.getChangePassword());
        } else {
            user.setPassword(passwordEncoder.encode(userLoginDto.getPassword()));
            user.setChangePassword(true);
        }


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

    public void updateUser(UserLoginDto userLoginDto)
    {
        UserLogin userLogin = mapToUserLoginModel(userLoginDto);
        userLoginRepository.save(userLogin);
    }

    @Override
    public void updateEmailAndPassword(UserLoginDto userLoginDto)
    {
        UserLogin userLogin = userLoginRepository.findByUserLoginId(userLoginDto.getId());
        userLogin.setEmail(userLoginDto.getEmail());
        userLogin.setPassword(passwordEncoder.encode(userLoginDto.getPassword()));
        userLogin.setChangePassword(false);
        userLoginRepository.save(userLogin);
    }

    @Override
    public UserLoginDto getByEmail(String email)
    {
        UserLoginDto userLoginDto = new UserLoginDto();
        if (userLoginRepository.findByEmail(email) != null)
        {
            userLoginDto = mapToDtoWithId(userLoginRepository.findByEmail(email));
        }
        return userLoginDto;
    }

    @Override
    public UserLoginDto getById(Long id) {
        return mapToDtoWithId(userLoginRepository.findById(id).get());
    }

    @Override
    public List<UserLoginDto> findAllUsers()
    {
        List<UserLogin> users = userLoginRepository.findAll();
        return users.stream().map((userLogin) -> mapToDtoWithId(userLogin))
                .collect(Collectors.toList());
    }


    private UserLoginDto mapToUserLoginDto(UserLogin user)
    {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setChangePassword(user.getChangePassword());
        return userLoginDto;
    }

    //map naar model: als object al bestaat gaat de Id geset worden om te kunnen updaten
    //als het object al bestaat gaat hij ook al Roles hebben
    //indien het wachtwoord niet wordt veranderd is hij null in de DTO
    protected UserLogin mapToUserLoginModel(UserLoginDto userLoginDto)
    {
        UserLogin userLogin = new UserLogin();

        if (userLoginDto.getId() != null)
        {
            userLogin.setUserLoginId(userLoginDto.getId());
            userLogin.setRoles(userLoginDto.getRoles());
        }
        if (userLoginDto.getPassword() != null)
        {
            userLogin.setPassword(passwordEncoder.encode(userLoginDto.getEmail()));
        } else {
            UserLoginDto hash = getById(userLoginDto.getId());
            userLogin.setPassword(hash.getPassword());
        }
        userLogin.setEmail(userLoginDto.getEmail());
        userLogin.setChangePassword(userLoginDto.getChangePassword());


        StaffServiceImpl staffService = new StaffServiceImpl(staffRepository);
        //userLogin.setStaff(staffService.mapToStaffModel(userLoginDto.getStaffDto()));

        return userLogin;
    }

    private UserLoginDto mapToDtoWithId(UserLogin user)
    {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setId(user.getUserLoginId());
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setRoles(user.getRoles());
        userLoginDto.setChangePassword(user.getChangePassword());
        return userLoginDto;
    }

    private void initiateRoles()
    {
        Role admin = new Role();
        admin.setRoleName("ROLE_ADMIN");
        Role user = new Role();
        user.setRoleName("ROLE_USER");
        roleRepository.save(admin);
        roleRepository.save(user);
    }
}
