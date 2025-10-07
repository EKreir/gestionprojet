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
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User", id);

        userRepository.deleteById(id);
        userRepository.flush(); // force la synchro avec la DB
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

    Explications :

    Rôle :
    Gère la logique métier liée aux utilisateurs (création, mise à jour, suppression, récupération).

    Pourquoi cette logique ?

    Centraliser toute la logique liée aux utilisateurs dans une seule couche (évite la duplication dans les contrôleurs).

    Respecter le principe de séparation des responsabilités (architecture en couches).

    Principales méthodes :

    | Méthode                                   | Description                                            | Explication métier                                                                                       |
    | ----------------------------------------- | ------------------------------------------------------ | -------------------------------------------------------------------------------------------------------- |
    | `findAll()`                               | Récupère tous les utilisateurs sous forme de `UserDTO` | Utilise `userRepository.findAll()`, convertit chaque entité en DTO pour ne pas exposer les mots de passe |
    | `findById(Long id)`                       | Trouve un utilisateur par son ID                       | Permet d’afficher les détails d’un utilisateur                                                           |
    | `create(UserDTO dto, String rawPassword)` | Crée un nouvel utilisateur                             | Hash le mot de passe, affecte les rôles, enregistre en base et renvoie le DTO                            |
    | `update(Long id, UserDTO dto)`            | Met à jour un utilisateur existant                     | Charge depuis la base, modifie uniquement les champs fournis, met à jour les rôles si besoin             |
    | `delete(Long id)`                         | Supprime un utilisateur                                | Vérifie que l’utilisateur existe avant suppression                                                       |
    | `toDto(User u)`                           | Transforme une entité en DTO                           | Permet d’exposer uniquement les données nécessaires sans mot de passe                                    |

    En résumé :
    Ce service assure la gestion métier des utilisateurs, tout en protégeant les données sensibles (password, rôles).
    Il travaille main dans la main avec UserRepository et RoleRepository.

    */

}