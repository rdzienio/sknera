package pl.gienius.sknera.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)//przechowywane w postaci łańcucha znaków
    private Types type;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(Types type){
        this.type = type;
    }

    public Role() {

    }

    public static enum Types{
        ROLE_ADMIN,
        ROLE_USER
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
