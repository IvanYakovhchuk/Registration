package org.project.database_ui.repository;

import jakarta.persistence.EntityNotFoundException;
import org.project.database_ui.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Person, Long> {

    default Optional<Person> findById(long id) {
        return findAll().stream()
                .filter(p -> id == p.getId())
                .findFirst();
    }

    default List<Person> findByUsername(String username) {
        return findAll().stream()
                .filter(p -> ((p.getFirstName() + p.getLastName()).equalsIgnoreCase(username) ||
                        p.getFirstName().equalsIgnoreCase(username) ||
                        p.getLastName().equalsIgnoreCase(username)))
                .toList();
    }

    default void updateUserById(Person person, long id) {
        Person oldPerson = findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found"));
        oldPerson.setFirstName(person.getFirstName());
        oldPerson.setLastName(person.getLastName());
        oldPerson.setEmail(person.getEmail());
        save(oldPerson);
    }

    default void deleteById(long id) {
        if (findById(id).isPresent()) {
            delete(findById(id).get());
        }
    }

    default void deleteByUsername(String username) {
        if (!findByUsername(username).isEmpty()) {
            deleteAll(findByUsername(username));
        }
    }
}
