package fr.dawan.gestionprojet.service;

import fr.dawan.gestionprojet.DTO.UserDTO;
import fr.dawan.gestionprojet.model.entity.User;
import fr.dawan.gestionprojet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
        List<UserDTO> findAll();
        UserDTO findById(Long id);
        UserDTO create(UserDTO dto, String rawPassword); // or have separate auth service
        UserDTO update(Long id, UserDTO dto);
        void delete(Long id);
}
