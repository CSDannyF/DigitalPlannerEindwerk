package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ShiftDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
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
    public UserLoginDto saveUser(UserLoginDto userLoginDto) {
        UserLogin user = new UserLogin();
        user.setEmail(userLoginDto.getEmail());

        if (userLoginDto.getStaffDto() != null) {
            StaffServiceImpl staffService = new StaffServiceImpl(staffRepository);
            user.setStaff(staffService.mapToStaffModel(userLoginDto.getStaffDto()));
        }
        if (userLoginDto.getId() != null) {
            user.setUserLoginId(userLoginDto.getId());
        }

        if (userLoginDto.getPassword() == null) {
            UserLogin temp = userLoginRepository.findByUserLoginId(userLoginDto.getId());
            user.setPassword(temp.getPassword());
            user.setChangePassword(temp.getChangePassword());
        } else {
            user.setPassword(passwordEncoder.encode(userLoginDto.getPassword()));
            user.setChangePassword(true);
        }

        //setChangePassword op false want Hier is de ADMIN de eerste geregistreerde en is het geen dummy wachtwoord
        Role role;
        if (userLoginRepository.findAll().isEmpty()) {
            initiateRoles();
            role = roleRepository.findByRoleName("ROLE_ADMIN");
            user.setChangePassword(false);
        } else {
            role = roleRepository.findByRoleName("ROLE_USER");
        }

        user.getRoles().add(role);

        byte[] image = new byte[0];
        try {
            image = convertDefaultImage("src/main/resources/static/images/dashboard/logo.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.setUserImageData(image);

        UserLogin newUserLogin = userLoginRepository.save(user);
        return mapToUserLoginDto(newUserLogin);
    }

    //deze enkel gebruiken bij het updaten van de email en het wachtwoord
    @Override
    public UserLoginDto updateUserLogin(UserLoginDto userLoginDto) {
        UserLogin userLogin = userLoginRepository.findByUserLoginId(userLoginDto.getId());
        userLogin.setEmail(userLoginDto.getEmail());
        userLogin.setPassword(passwordEncoder.encode(userLoginDto.getPassword()));
        userLogin.setChangePassword(false);
        UserLogin updatedUserLogin = userLoginRepository.save(userLogin);
        return mapToUserLoginDto(updatedUserLogin);
    }

    @Override
    public UserLoginDto saveImage(UserLoginDto userLoginDto) throws IOException {
        UserLogin userLogin = userLoginRepository.findByUserLoginId(userLoginDto.getId());

        // Verwerken van de avatar
        byte[] imageData = userLoginDto.getAvatar().getBytes();
        userLoginDto.setUserImageData(imageData);

        if (userLoginDto.getUserImageData() != null) {
            userLogin.setUserImageData(userLoginDto.getUserImageData());
        }
        UserLogin updatedUserLogin = userLoginRepository.save(userLogin);
        return mapToUserLoginDto(updatedUserLogin);
    }

    @Override
    public UserLoginDto getByEmail(String email) {
        UserLoginDto userLoginDto = new UserLoginDto();
        if (userLoginRepository.findByEmail(email) != null) {
            userLoginDto = mapToDtoWithId(userLoginRepository.findByEmail(email));
        }
        return userLoginDto;
    }

    @Override
    public UserLoginDto getById(Long id) {
        return mapToDtoWithId(userLoginRepository.findById(id).get());
    }

    @Override
    public List<UserLoginDto> findAllUsers() {
        List<UserLogin> users = userLoginRepository.findAll();
        return users.stream().map((userLogin) -> mapToDtoWithId(userLogin))
                .collect(Collectors.toList());
    }


    //returns list of shiftDtos, ordered by weeknumber, to show personal schedule to staff
    @Override
    public Map<Integer, List<ShiftDto>> getUserPlanningDetails(String email) {
        UserLogin userLogin = userLoginRepository.findByEmail(email);
        Staff staff = userLogin.getStaff();
        Map<Integer, List<ShiftDto>> shiftsByWeek = new HashMap<>();

        for (PlanningDetail planningDetail : staff.getPlanningDetails()) {
            ShiftDto shiftDto = new ShiftDto();
            LocalDate date = planningDetail.getPlanning().getWorkingDay().getDate();
            shiftDto.setDate(date);
            shiftDto.setStartTime(planningDetail.getStartTime());
            shiftDto.setEndTime(planningDetail.getEndTime());
            shiftDto.setDay(String.valueOf(date.getDayOfWeek()));

            int weekNumber = date.get(WeekFields.ISO.weekOfWeekBasedYear());
            shiftsByWeek.computeIfAbsent(weekNumber, k -> new ArrayList<>()).add(shiftDto);
        }
        return shiftsByWeek;
    }

    @Override
    public Staff getStaffByUserEmail(String email) {
        UserLogin userLogin = userLoginRepository.findByEmail(email);
        System.out.println(userLogin);
        Staff staff = userLogin.getStaff();
        System.out.println(staff);
        return staff;
    }

    private UserLoginDto mapToUserLoginDto(UserLogin user) {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setChangePassword(user.getChangePassword());

        // Convert the avatar to a Base64 string
        if (user.getUserImageData() != null) {
            String base64Avatar = ImageConverter.convertByteArrayToBase64(user.getUserImageData());
            userLoginDto.setUserImage(base64Avatar);
        }

        userLoginDto.setUserImageData(userLoginDto.getUserImageData());
        return userLoginDto;
    }

    //map naar model: als object al bestaat gaat de Id geset worden om te kunnen updaten
    //als het object al bestaat gaat hij ook al Roles hebben
    //indien het wachtwoord niet wordt veranderd is hij null in de DTO
    protected UserLogin mapToUserLoginModel(UserLoginDto userLoginDto) {
        UserLogin userLogin = new UserLogin();

        if (userLoginDto.getId() != null) {
            userLogin.setUserLoginId(userLoginDto.getId());
            userLogin.setRoles(userLoginDto.getRoles());
        }
        if (userLoginDto.getPassword() != null) {
            userLogin.setPassword(passwordEncoder.encode(userLoginDto.getEmail()));
        } else {
            UserLoginDto hash = getById(userLoginDto.getId());
            userLogin.setPassword(hash.getPassword());
        }
        userLogin.setEmail(userLoginDto.getEmail());
        userLogin.setChangePassword(userLoginDto.getChangePassword());

        // Verwerken van de avatar als deze niet leeg is
        if (userLoginDto.getAvatar() != null && !userLoginDto.getAvatar().isEmpty()) {
            try {
                byte[] imageData = userLoginDto.getAvatar().getBytes();
                userLogin.setUserImageData(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (userLoginDto.getUserImageData() != null) {
            userLogin.setUserImageData(userLoginDto.getUserImageData());
        }
        return userLogin;
    }

    private UserLoginDto mapToDtoWithId(UserLogin user) {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setId(user.getUserLoginId());
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setRoles(user.getRoles());
        userLoginDto.setChangePassword(user.getChangePassword());

        StaffServiceImpl staffService = new StaffServiceImpl(staffRepository);

        if (user.getStaff() != null) {
            userLoginDto.setStaffDto(staffService.mapToStaffDto(user.getStaff()));
        }
        if (user.getUserImageData() != null) {
            userLoginDto.setUserImageData(user.getUserImageData());
        }
        // Convert the avatar to a Base64 string
        if (user.getUserImageData() != null) {
            String base64Avatar = ImageConverter.convertByteArrayToBase64(user.getUserImageData());
            userLoginDto.setUserImage(base64Avatar);
        }
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

    private byte[] convertDefaultImage(String url) throws IOException {
        File file = new File(url);
        byte[] image = Files.readAllBytes(file.toPath());
        return image;
    }
}
