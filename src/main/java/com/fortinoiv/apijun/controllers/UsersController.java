package com.fortinoiv.apijun.controllers;

import com.fortinoiv.apijun.application.UsersApplication;
import com.fortinoiv.apijun.domain.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UsersController  {

    @Autowired
    private UsersApplication usersApplication;

    @Autowired
    public UsersController(
            UsersApplication usersApplication){
        this.usersApplication = usersApplication;
    }

    @PostMapping(value = "saveUser")
    public Users saveUser(@RequestBody Users users) {
        return this.usersApplication.saveUser(users);
    }

    @GetMapping("/{idUser}")
    public Users getUser(@PathVariable(value = "idUser") Integer idUser) {
        return this.usersApplication.getUser(idUser);
    }

    @PutMapping("/updateUser")
    public Users updateUser(@RequestBody Users users){
        return this.usersApplication.updateUser(users);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody Map<String, Object> data){
        return this.usersApplication.deleteUser(data);
    }
}
