package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.RoleDto;

import java.util.List;

public interface RoleService
{
    void save(RoleDto roleDto);
    RoleDto getByRoleName(String roleName);
    RoleDto getByRoleId(long roleId);
    List<RoleDto> getAll();
}
