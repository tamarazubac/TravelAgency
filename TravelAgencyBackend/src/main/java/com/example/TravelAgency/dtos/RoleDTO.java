package com.example.TravelAgency.dtos;

import com.example.TravelAgency.models.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = -6512552723370663872L;

    public String roleName;

    public RoleDTO(Role role) {
        roleName=role.getRoleName();
    }
}
