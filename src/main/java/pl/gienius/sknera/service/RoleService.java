package pl.gienius.sknera.service;

import pl.gienius.sknera.entity.Role;
import pl.gienius.sknera.repository.RoleRepository;

public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }
}
