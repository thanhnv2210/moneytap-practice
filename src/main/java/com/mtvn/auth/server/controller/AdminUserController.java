package com.mtvn.auth.server.controller;

import com.mtvn.auth.server.model.UserDetailModel;
import com.mtvn.auth.server.service.AdminUserService;
import com.mtvn.persistence.entities.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired private AdminUserService adminUserService;

    @PostMapping("/users")
    private ResponseEntity<User> create(@RequestBody UserDetailModel userDetailModel){
        return ResponseEntity.ok(adminUserService.createNewUser(userDetailModel));
    }

    @GetMapping("/users")
    private ResponseEntity<List<UserDetailModel>> getAll(){
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @GetMapping("/users/find")
    private ResponseEntity<User> find(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email){
        Optional<User> user = adminUserService.getUserByPhoneOrEmail(phone, email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    private ResponseEntity<?> delete(@PathVariable Integer id){
        adminUserService.delete(id);
        return ResponseEntity.ok().build();
    }
}
