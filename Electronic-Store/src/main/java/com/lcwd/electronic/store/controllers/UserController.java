package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.response.ApiResponse;
import com.lcwd.electronic.store.response.PageableResponse;
import com.lcwd.electronic.store.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    // create
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        ApiResponse<UserDto> userCreatedSuccessfully = ApiResponse.success("User Created Successfully",user);
        return new ResponseEntity(userCreatedSuccessfully, HttpStatus.CREATED);
    }

    // update
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@RequestBody @Valid UserDto userDto, @PathVariable String userId){
        UserDto userDto1 = userService.updateUser(userDto, userId);
        ApiResponse<UserDto> userUpdatedSuccessfully = ApiResponse.success("User Updated Successfully",userDto1);
        return new ResponseEntity(userUpdatedSuccessfully, HttpStatus.OK);
    }

    // delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable(value = "userId") String Id){
        userService.deleteUser(Id);
        ApiResponse userDeletedSuccessfully = ApiResponse.success("User Deleted Successfully",null);
        return new ResponseEntity<ApiResponse<String>>(userDeletedSuccessfully,HttpStatus.NO_CONTENT);
    }

    // get all users
    @GetMapping
    public ResponseEntity<ApiResponse<PageableResponse<UserDto>>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<UserDto> allUsers= userService.getAllUsers(pageNumber-1, pageSize, sortBy, sortDir);
        ApiResponse<PageableResponse<UserDto>> allUsersFound = ApiResponse.success("All Users Found",allUsers);
        return new ResponseEntity<>(allUsersFound, HttpStatus.OK);
    }

    // get single user by id
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String userId){
        UserDto user = userService.getUser(userId);
        ApiResponse<UserDto> userFound = ApiResponse.success("User Found",user);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }

    // get single user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@PathVariable String email){
        UserDto user = userService.getUserByEmail(email);
        ApiResponse<UserDto> userFound = ApiResponse.success("User Found",user);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }

    // search user

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUser(@PathVariable String keyword){
        List<UserDto> userDtoList = userService.searchUser(keyword);
        ApiResponse<List<UserDto>> userFound = ApiResponse.success("User Found",userDtoList);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }
}
