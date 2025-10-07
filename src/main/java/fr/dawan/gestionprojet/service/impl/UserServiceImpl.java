package fr.dawan.gestionprojet.service.impl;

import fr.dawan.gestionprojet.DTO.UserDTO;
import fr.dawan.gestionprojet.exception.ResourceNotFoundException;
import fr.dawan.gestionprojet.model.entity.User;
import fr.dawan.gestionprojet.model.entity.Role;
import fr.dawan.gestionprojet.repository.RoleRepository;
import fr.dawan.gestionprojet.repository.UserRepository;
import fr.dawan.gestionprojet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User introuvable",id));
        return toDto(u);
    }

    @Override
    public UserDTO create(UserDTO dto, String rawPassword) {
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(passwordEncoder.encode(rawPassword));
        if(dto.getRoles() != null){
            Set<Role>roles=dto.getRoles().stream()
                    .map(name -> roleRepository.findByName(name).orElseGet(() -> {
                        Role r = new Role();
                        r.setName(name);
                        return roleRepository.save(r);
                    })).collect(Collectors.toSet());
            u.setRoles(roles);
        }
        User saved = userRepository.save(u);
        return toDto(saved);
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User introuvable",id));
        if (dto.getUsername() != null) u.setUsername(dto.getUsername());
        if (dto.getEmail() != null) u.setEmail(dto.getEmail());
        if (dto.getRoles() != null) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(name -> roleRepository.findByName(name)
                            .orElseThrow(() -> new RuntimeException("role non trouver" + name)))
                    .collect(Collectors.toSet());
            u.setRoles(roles);

        }
        User saved = userRepository.save(u);
        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) throw new ResourceNotFoundException("user introuvable", id);
        userRepository.existsById(id);
    }

    private UserDTO toDto(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        if (u.getRoles() != null) dto.setRoles(u.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return dto;
    }

    /*
    Notes :

    PasswordEncoder injecté -- si on n’a pas encore Spring Security, on peut commenter et stocker raw (fortement déconseillé).
    create() encodera le mot de passe via passwordEncoder.encode(rawPassword)
    (important pour sécurité).



    */

}