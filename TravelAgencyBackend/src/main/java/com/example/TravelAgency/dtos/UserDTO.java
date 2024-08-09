package com.example.TravelAgency.dtos;

import com.example.TravelAgency.models.Role;
import com.example.TravelAgency.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -6512552723370663872L;

    public Long id;
    public String first_name;
    public String last_name;
    public String username;

    public String email;
    public String phone;
    public String password;

    public List<RoleDTO> roles;

    public UserDTO(User user) {  //without password and roles
        this(user,false);
    }

    public UserDTO(User user,boolean includeRoles){
        id=user.id;
        first_name=user.getFirstName();
        last_name= user.getLastName();
        username=user.getUsername();
        email=user.getEmail();
        phone=user.getPhone();


        if (includeRoles){
            roles=new ArrayList<>();
            for (Role r:user.getRoles()) {
                roles.add(new RoleDTO(r));
            }

        }

    }

    //role is customer - default

}
