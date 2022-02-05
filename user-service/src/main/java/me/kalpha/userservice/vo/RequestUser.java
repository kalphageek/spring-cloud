package me.kalpha.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {
    @NotNull(message = "Email can not be null")
    @Email
    private String email;

    @NotNull(message = "Password can not be null")
    @Size(min = 2, message = "Password not be less than 2 characters")
    private String pwd;

    @NotNull(message = "Name can not be null")
    @Size(min = 4, message = "Name not be less than 8 characters")
    private String name;
}
