package com.ppdm.backend.user;

import com.ppdm.backend.user.dto.UserDto;
import com.ppdm.backend.user.dto.UserUpdateDto;
import com.ppdm.backend.user.exception.InvalidUserException;
import com.ppdm.backend.user.exception.UserAlreadyExistException;
import com.ppdm.backend.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id) {
        try{
            return ResponseEntity.ok(userService.findUserById(id));
        }catch (UserNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody UserDto user){
        try {
            return ResponseEntity.ok(userService.saveUser(user));
        }catch (InvalidUserException | UserAlreadyExistException e){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }catch(UserNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto user){
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        }catch (UserNotFoundException | InvalidUserException e){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
