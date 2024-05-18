package app.polirubro.user.controllers;

import app.polirubro.pagination.dto.PaginationInfo;
import app.polirubro.user.controllers.DTO.UserActivationResponse;
import app.polirubro.user.controllers.DTO.UserResponse;
import app.polirubro.user.controllers.DTO.UserUpdateRequest;
import app.polirubro.user.controllers.DTO.UserUpdateResponse;
import app.polirubro.user.controllers.Mappers.UserToUserResponse;
import app.polirubro.user.entities.User;
import app.polirubro.user.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //region Exists
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
    //endregion

    //region Get payment preferences

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<UserUpdateResponse> update(@RequestBody UserUpdateRequest request){
        return ResponseEntity.ok(this.userService.update(request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<UserActivationResponse> enabled(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.enabled(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<UserActivationResponse> disabled(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.disabled(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(new UserToUserResponse().apply(this.userService.findById(id)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/user-session")
    public ResponseEntity<UserResponse> findUserSession(){
        return ResponseEntity.ok(new UserToUserResponse().apply(this.userService.findUserSession()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber
    ){
        List<User> users = this.userService.findAll(pageNumber);
        List<UserResponse> userResponses = users.stream().map(new UserToUserResponse()).toList();

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/pagination")
    public ResponseEntity<PaginationInfo> getPaginationInfo() {
        PaginationInfo paginationInfo = this.userService.getPaginationInfo();

        return ResponseEntity.status(HttpStatus.OK).body(paginationInfo);
    }
}
