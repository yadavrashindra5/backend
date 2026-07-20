package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.validation.ImageNameValidate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;
    @Size(min = 3, max = 15, message="Invalid Name!!")
    private String name;
//    @Email(message="Invalid Email!!")
//    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$",message="Invalid Email!!")
    @NotBlank(message="Email is required")
    private String email;
    @NotBlank(message="Password is required")
    private String password;
    @Size(min = 4, max = 6, message="Invalid Gender!!")
    private String gender;
    @NotBlank(message="Write something about yourself")
    private String about;

    @ImageNameValidate
    private String imageName;
    // pattern
    // custom validator
}
