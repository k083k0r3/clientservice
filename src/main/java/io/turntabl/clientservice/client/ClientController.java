package io.turntabl.clientservice.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;
//    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder) {
//        this.jwtTokenProvider = jwtTokenProvider;
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    registerClient: This method is mapped to the HTTP POST request at /clients/register. It receives a Client object in the
    request body. It checks if a client with the provided email already exists using the checkIfClientExistsByEmail method of the
    ClientService. If a client already exists, it returns a conflict response with the message "User already exists".
    Otherwise, it calls the createClient method of the ClientService to register the client, sets the client type to
    ClientType.BROKER, and returns an accepted response with the message "Client created successfully".
    */

    @PostMapping("/register")
    public ResponseEntity<String> registerClient(@RequestBody Client client) {
//        Client existingClient = clientService.checkIfClientExistsByEmail(client.getEmail());
        if (!clientService.checkIfClientExistsByEmail(client.getEmail())) {
            clientService.createClient(client.getLastName(), client.getFirstName(), client.getEmail(),
                    client.getPassword());
            client.setClientType(ClientType.BROKER);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Client created successfully");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User already exists");
    }


//    @PostMapping("/register")
//    public ResponseEntity<String> registerClient(@RequestBody Client client) {
//        if (clientService.findByEmail(client.getEmail()) != null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("User already exists");
//        }
//        try {
//            clientService.createClient(client.getLastName(), client.getFirstName(), client.getEmail(),
//                    client.getPassword());
//            client.setClientType(ClientType.BROKER);
//
//            // Generate JWT token
//            String token = jwtTokenProvider.generateToken(client.getEmail());
//
//            // Return token in the response
//            return ResponseEntity.ok(token);
//        } catch (CustomerAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while registering the client");
//        }
//    }



//    @PostMapping("/login")
//    public ResponseEntity<String> loginClient(@RequestBody Client client) {
//        Client existingClient = clientService.findByEmail(client.getEmail());
//
//        if (existingClient == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        }
//
//        if (!passwordEncoder.matches(client.getPassword(), existingClient.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid email or password");
//        }
//
//        // Generate JWT token
//        String token = jwtTokenProvider.generateToken(existingClient.getEmail());
//
//        // Return token in the response
//        return ResponseEntity.ok(token);
//    }

    /*
    loginClient: This method is mapped to the HTTP POST request at /clients/login. It receives a Client object
    containing the email and password in the request body. It uses the authenticationManager to authenticate the client
    by creating an Authentication object with the provided email and password. If the authentication is successful, it
    generates a JWT token using the jwtTokenProvider and creates a LoginResponse object with the token. It returns a
    successful response with the LoginResponse object. If the authentication fails, it catches the
    AuthenticationException and returns an unauthorized response with the message "Invalid email or password".
    */
    @PostMapping("/login")
    public ResponseEntity<String> loginClient(@RequestBody Client client) {
        // Retrieve the client from the database based on the provided email
        Client existingClient = clientService.findByEmail(client.getEmail());
        if (existingClient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
        // Check if the password matches
        if (!passwordEncoder.matches(client.getPassword(), existingClient.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
        // Return a successful response
        return ResponseEntity.ok("Login successful");
    }

}



//        try {
//            clientService.createClient(client.getLastName(), client.getFirstName(), client.getEmail(),
//                    client.getPassword());
//            client.setClientType(ClientType.BROKER);
//            return ResponseEntity.ok("Client created successfully");
//        } catch (CustomerAlreadyExistsException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("An error occurred while registering the client");
//        }