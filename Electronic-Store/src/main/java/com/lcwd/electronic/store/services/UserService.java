package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.response.PageableResponse;

import java.io.IOException;
import java.util.List;

public interface UserService {
    // create
    UserDto createUser(UserDto userDto);

    // update
    UserDto updateUser(UserDto userDto,String userId);

    // delete
    void deleteUser(String userId) ;

    // get all users
    PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by id
    UserDto getUser(String userId);

    // get single user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser(String keyword);
}
