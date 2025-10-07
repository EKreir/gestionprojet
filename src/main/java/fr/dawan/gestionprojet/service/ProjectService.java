package fr.dawan.gestionprojet.service;

import fr.dawan.gestionprojet.DTO.ProjectDTO;
import fr.dawan.gestionprojet.DTO.UserDTO;


import java.util.List;
import java.util.Set;

public interface ProjectService {
    List<ProjectDTO> findAll();
    ProjectDTO findById(Long id);
    ProjectDTO create(ProjectDTO dto); // or have separate auth service
    ProjectDTO update(Long id, ProjectDTO dto);
    void delete(Long id);
    ProjectDTO addMember(Long projectId, Long userId);
    ProjectDTO removeMember(Long projectId, Long userId);
    Set<UserDTO> getMembers(Long projectId);


}
