package me.kalpha.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RequestLogin {
    @NotNull(message = "Email이 필요합니다.")
    @Email
    private String email;
    @NotNull(message = "Password가 필요합니다.")
    private String pwd;
}
