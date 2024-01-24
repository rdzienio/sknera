package pl.gienius.sknera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.gienius.sknera.entity.Role;
import pl.gienius.sknera.repository.RoleRepository;

@Controller
public class RoleController {
    private pl.gienius.sknera.repository.RoleRepository RoleRepository;

    @Autowired
    public RoleController(RoleRepository roleReps){
        RoleRepository=roleReps;
    }

    public void saveRole(Role role) {
        RoleRepository.save(role);
    }
    public Iterable<Role> returnAll(){
        return RoleRepository.findAll();
    }
    public Role initUser(){
        for(Role userRole:RoleRepository.findAll()){
            if(userRole.getType().toString().equals("ROLE_USER")){
                return userRole;
            }
        }
        return new Role();
    }
}
