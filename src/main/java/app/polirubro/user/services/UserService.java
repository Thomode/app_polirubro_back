package app.polirubro.user.services;


import app.polirubro.pagination.dto.PaginationInfo;
import app.polirubro.pagination.utils.PaginationUtility;
import app.polirubro.user.dto.UserActivationResponse;
import app.polirubro.user.dto.UserUpdateRequest;
import app.polirubro.user.dto.UserUpdateResponse;
import app.polirubro.user.entities.User;
import app.polirubro.user.repositories.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PaginationUtility paginationUtility;

    @Resource
    Environment env;

    //region Exists
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    //endregion

    //region Find
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //endregion

    //region Get payment preferences


    @Transactional
    public UserUpdateResponse update(UserUpdateRequest request) {
        // Get user
        User user = this.findUserSession();

        // Basic info
        if (request.getFirstname() != null) {
            user.setFirstname(request.getFirstname());
        }
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }

        this.userRepository.save(user);

        return UserUpdateResponse.builder()
                .message("User updated")
                .build();
    }

    private boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Transactional
    public UserActivationResponse enabled(Long id){
        User user = this.findById(id);
        user.setEnabled(true);

        this.userRepository.save(user);

        return UserActivationResponse.builder()
                .message("User enabled")
                .build();
    }

    @Transactional
    public UserActivationResponse disabled(Long id){
        User user = this.findById(id);
        user.setEnabled(false);

        this.userRepository.save(user);

        return UserActivationResponse.builder()
                .message("User disabled")
                .build();
    }

    public User findById(Long id){
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User findUserSession(){
        String usernameOrEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;

        if (this.isEmail(usernameOrEmail)) {
            user = userRepository.findByEmail(usernameOrEmail).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with the provided email"));
        } else {
            user = userRepository.findByUsername(usernameOrEmail).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "No user found with the provided username"));
        }

        return user;
    }

    public PaginationInfo getPaginationInfo() {
        return paginationUtility.getPaginationInfo("pagination.page-size", this.userRepository);
    }

    public List<User> findAll(int pageNumber){
        Pageable page = PageRequest.of(pageNumber, Integer.parseInt(env.getProperty("pagination.page-size")));

        return this.userRepository.findAll(page).stream().toList();
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return this.userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("user not logged in"));
    }

    public boolean isYourRegister(User user){
        return this.getCurrentUser().getUsername().equals(user.getUsername());
    }
}
