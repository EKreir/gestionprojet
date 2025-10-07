package fr.dawan.gestionprojet.repository;

import fr.dawan.gestionprojet.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // utile pour charger les roles en même temps (évite LAZY exception à l'auth)
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    /*
    *
    * @EntityGraph permet de charger roles en même temps
    * si nécessaire (pratique pour Spring Security).
    */

}