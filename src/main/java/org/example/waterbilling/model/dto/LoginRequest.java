package org.example.waterbilling.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginRequest {

    @NotBlank
    private String login;
    private String email;

    @NotBlank
    private String password;
}
