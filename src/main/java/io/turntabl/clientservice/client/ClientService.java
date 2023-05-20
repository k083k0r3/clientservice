package io.turntabl.clientservice.client;

import io.turntabl.clientservice.exception.CustomerAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createClient(String lastName, String firstName, String email, String password){
        Client client = new Client(lastName, firstName, email, password);
        registerClient(client);
    }
    private Client registerClient(Client client) {
        // Perform any necessary validations before saving the client
        // Check if the client already exists by ID
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new CustomerAlreadyExistsException("Client with National ID " + client.getEmail()+ " already exists.");
        }
        // Save the client to the database
        System.out.println(client.getLastName() + " " + client.getFirstName() + " Account created Successfully");
        return clientRepository.save(client);
    }

    public boolean validateClientCredentials(String email, String password) {
        Client client = findByEmail(email);
        return client != null && passwordEncoder.matches(password, client.getPassword());
    }

    // Find by Email: Retrieve a client by their email address.
    public Client findByEmail(String email) {
        Optional<Client> foundClient = clientRepository.findByEmail(email);
        return foundClient.orElse(null);
    }

    public boolean checkIfClientExistsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    public Client getClientById(Long id){
        Optional<Client> foundClient = clientRepository.getClientById(id);
        return foundClient.orElse(null);
    }
}
