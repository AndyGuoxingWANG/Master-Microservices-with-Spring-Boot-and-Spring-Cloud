package com.in28minutes.res.webservices.restfulwebservices.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@RestController
public class UserResource {

   @Autowired
   private UserDaoService service;

   @GetMapping("/users")
   public List<User> retrieveAllUsers(){
      return service.findAll();
   }

   @GetMapping("/users/{id}")
   public EntityModel<User> retrieveUser(@PathVariable int id){
      User user = service.findOne(id);
      if (user == null){
         throw new UserNotFoundException("id-" + id);
      }
      EntityModel<User> model = EntityModel.of(user);
      WebMvcLinkBuilder linkToUsers =
              linkTo(methodOn(this.getClass()).retrieveAllUsers());
      return model;
   }

   @PostMapping("/users")
   public ResponseEntity<Object> createUser(@Valid @RequestBody User user){
      User savedUser = service.save(user);
      URI location = ServletUriComponentsBuilder
              .fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(savedUser.getId()).toUri();
      return ResponseEntity.created(location).build();
   }

   @DeleteMapping("/users/{id}")
   public void deleteUser(@PathVariable int id){
      User user = service.deleteById(id);
      if (user == null){
         throw new UserNotFoundException("id-" + id);
      }
   }

}
