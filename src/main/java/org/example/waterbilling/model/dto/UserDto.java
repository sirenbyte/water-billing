package org.example.waterbilling.model.dto;

import jakarta.persistence.Column;
import lombok.Data;
import org.example.waterbilling.model.annotation.FiledTitle;

@Data
public class UserDto {
    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "fathersname")
    private String fathersname;

    @FiledTitle("Номер телефона")
    @Column(name = "phone_number")
    private String phoneNumber;

    @FiledTitle("Почта")
    @Column(name = "email")
    private String email;

    @FiledTitle("Лицевой счет")
    @Column(name = "personal_account_number")
    private String personalAccountNumber;

    @Column(name = "password")
    private String password;

    @FiledTitle("Тип пользователя")
    @Column(name = "position")
    private String position;

    @FiledTitle("Имя организаций")
    private String organization;

    @Column(name = "location")
    private String location;

    @FiledTitle("ИИН")
    @Column(name = "iin")
    private String iin;

    @FiledTitle("БИН")
    @Column(name = "bin")
    private String bin;
}
