package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.RoleDto;
import GroepswerkJava.DigitalPlanner.model.Role;
import GroepswerkJava.DigitalPlanner.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void save(RoleDto roleDto) {

    }

    @Override
    public RoleDto getByRoleName(String roleName) {
        return null;
    }

    @Override
    public RoleDto getByRoleId(long roleId) {
        return null;
    }

    @Override
    public List<RoleDto> getAll()
    {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map((role) -> mapToRoleDto(role))
                .collect(Collectors.toList());
    }

    protected RoleDto mapToRoleDto(Role role)
    {
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleName(role.getRoleName());
        roleDto.setRoleId(role.getRoleId());
        return roleDto;
    }
}
