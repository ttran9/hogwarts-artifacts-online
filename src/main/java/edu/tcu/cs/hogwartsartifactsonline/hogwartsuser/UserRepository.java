package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<HogwartsUser, Integer> {

    /**
     * We use an optional so we don't need to perform null checks.
     * @param username
     * @return
     */
    Optional<HogwartsUser> findByUsername(String username);

}
