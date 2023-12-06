package com.freekson.tokenreader.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserDto {
    private String login;
    private String password;
}
