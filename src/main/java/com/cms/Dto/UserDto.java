package com.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String Id;
    private String username;
    private String password;


}
