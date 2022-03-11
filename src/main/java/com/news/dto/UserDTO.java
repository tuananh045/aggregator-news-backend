package com.news.dto;

import com.news.model.User;
import lombok.Data;


@Data
public class UserDTO extends AbstractDTO<UserDTO>{
    private String email;
    private String fullName;
    private Integer display;

    public UserDTO() {
        super();
    }

    public UserDTO(User user) {
        super();
        this.setId(user.getId());
        this.email = user.getEmail();
        this.fullName = user.getFullname();
        this.display = user.getDisplay();
    }
}
