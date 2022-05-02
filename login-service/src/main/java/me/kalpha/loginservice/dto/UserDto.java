package me.kalpha.loginservice.dto;

import lombok.Data;
import me.kalpha.loginservice.vo.ResponseOrder;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String pwd;
    private String name;
    private String userId;
    private Date createdAt;
    private List<ResponseOrder> orders;

    private String encryptedPwd;
}
