package com.WebSiteChoiGame.DoAnJ2EE.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleID;

    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    // Getters and setters
    public Integer getRoleID() { return roleID; }
    public void setRoleID(Integer roleID) { this.roleID = roleID; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }
} 