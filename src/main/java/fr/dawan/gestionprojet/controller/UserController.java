package fr.dawan.gestionprojet.controller;

import fr.dawan.gestionprojet.DTO.UserDTO;
import fr.dawan.gestionprojet.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

}
