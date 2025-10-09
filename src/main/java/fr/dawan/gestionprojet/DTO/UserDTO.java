package fr.dawan.gestionprojet.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    // roles names (ex: "CHIEF", "MEMBER)
    private Set<String> roles;
   // private Long taskIds;


}
