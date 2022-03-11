package com.news.payload.request;

import com.news.dto.AbstractDTO;
import com.news.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SignupRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotBlank
    @Size(min = 6, max = 40, message = "Mật khẩu tối thiểu phải 6 ký tự hoặc tối đa bằng 40 ký tự")
    private String password;

    @NotBlank
    @Size(min = 6, max = 40, message = "Ten tối thiểu phải 5 ký tự hoặc tối đa bằng 25 ký tự")
    private String fullName;

    private List<String> role;

    public SignupRequest(String email, String password , String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }
}
