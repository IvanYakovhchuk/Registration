package org.project.database_ui.controller;

import org.project.database_ui.model.Person;
import org.project.database_ui.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/start")
    public String start() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("person", new Person());
        return "registration";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("person") Person person) {
        userRepository.save(person);
        return "redirect:/register";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<Person> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/userById/{id}")
    public Person getUserById(@PathVariable("id") long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @GetMapping("/userByUsername/{username}")
    public Object getUsersByUsername(@PathVariable("username") String username) {
        return userRepository.findByUsername(username).size() == 1 ?
                userRepository.findByUsername(username).getFirst() :
                userRepository.findByUsername(username);
    }

    @PutMapping("/user/{id}")
    public void updateUserById(@RequestBody Person person, @PathVariable("id") long id) {
        userRepository.updateUserById(person, id);
    }

    @GetMapping("/delete")
    public String deleteUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users_delete";
    }

    @PostMapping("/delete")
    public String deleteUsers(@ModelAttribute("selectedUsers") List<Long> selectedUsersIds) {
        if (!selectedUsersIds.isEmpty()) {
            userRepository.deleteAllById(selectedUsersIds);
        }
        return "redirect:/users";
    }
}