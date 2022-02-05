package me.kalpha.userservice.dto;

import lombok.Data;
import me.kalpha.userservice.vo.ResponseOrder;

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
