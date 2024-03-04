package mx.edu.utez.firstapp.services.person;

import mx.edu.utez.firstapp.config.ApiResponse;
import mx.edu.utez.firstapp.models.person.Person;
import mx.edu.utez.firstapp.models.person.PersonRepository;
import mx.edu.utez.firstapp.models.role.Role;
import mx.edu.utez.firstapp.models.role.RoleRepository;
import mx.edu.utez.firstapp.models.user.User;
import mx.edu.utez.firstapp.models.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {
    private final PersonRepository repository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public PersonService(PersonRepository repository, UserRepository userRepository,
                         RoleRepository roleRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(
                new ApiResponse(repository.findAll(), HttpStatus.OK),
                HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> save(Person person) {
        person.setStatus(true);
        Optional<Person> foundPerson = repository.findByCurp(person.getCurp());
        if (foundPerson.isPresent())
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "RecordAlreadyExist"),
                    HttpStatus.BAD_REQUEST);
        if (person.getUser() != null) {
            Optional<User> foundUser = userRepository.findByUsername(person.getUser().getUsername());
            if (foundUser.isPresent())
                return new ResponseEntity<>(
                        new ApiResponse(
                                HttpStatus.BAD_REQUEST, true, "RecordAlreadyExist"
                        ), HttpStatus.BAD_REQUEST);
            person.getUser().setPerson(person);
            Set<Role> roles = person.getUser().getRoles();
            person.getUser().setRoles(null);
            person = repository.saveAndFlush(person);
            User savedUser = person.getUser();
            for (Role role : roles) {
                if (roleRepository.saveUserRole(role.getId(), savedUser.getId()) <= 0)
                    return new ResponseEntity<>(
                            new ApiResponse(
                                    HttpStatus.BAD_REQUEST, true, "RoleNotAttached"
                            ), HttpStatus.BAD_REQUEST);
            }
        } else {
            person = repository.saveAndFlush(person);
        }
        return new ResponseEntity<>(new ApiResponse(person, HttpStatus.OK), HttpStatus.OK);
    }

}
