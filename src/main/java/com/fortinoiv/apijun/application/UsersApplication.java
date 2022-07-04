package com.fortinoiv.apijun.application;

import com.fortinoiv.apijun.domain.entities.Users;
import com.fortinoiv.apijun.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UsersApplication {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public UsersApplication(
            UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Users saveUser(Users users){
        return this.usersRepository.save(users);
    }

    public Users getUser(Integer idUser) {
        return this.usersRepository.findByIdAndStatus(idUser,"ACTIVO");
    }

    public Users updateUser(Users users){
        Users user = this.usersRepository.findByIdAndStatus(users.getId(), "ACTIVO");
        user.setNombre(users.getNombre());
        user.setApellidoPaterno(users.getApellidoPaterno());
        user.setApellidoMaterno(users.getApellidoMaterno());
        user.setEmail(users.getEmail());
        return this.usersRepository.save(user);
    }

    public ResponseEntity<String> deleteUser(Map<String, Object> data){
        Users user = this.usersRepository.findByIdAndStatus((Integer) data.get("id"), "ACTIVO");
        user.setStatus("CANCELADO");
        this.usersRepository.save(user);
        return new ResponseEntity<>("Eliminado con éxito!", HttpStatus.OK);
    }

}
