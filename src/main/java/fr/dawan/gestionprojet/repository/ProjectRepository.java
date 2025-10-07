package fr.dawan.gestionprojet.repository;

import fr.dawan.gestionprojet.model.entity.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "members")
    List<Project> findAll();

    @EntityGraph(attributePaths = "members")
    Optional<Project> findById(Long id);
}
