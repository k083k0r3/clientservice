package io.turntabl.clientservice.client;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByNationalId(Long nationalId);
    boolean existsByEmail(String email);

    Optional<Client> findByNationalId(Long nationalId);

    Optional<Client> findByLastName(String LastName);
    Optional<Client> getClientById(Long id);
}


