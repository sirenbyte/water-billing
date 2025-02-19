package org.example.waterbilling.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.waterbilling.model.annotation.FiledTitle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "fathersname")
    private String fathersname;

    @FiledTitle("Номер телефона")
    @Column(name = "phone_number")
    private String phoneNumber;

    @FiledTitle("Логин")
    @Column(name = "login")
    private String login;

    @FiledTitle("Почта")
    @Column(name = "email")
    private String email;

    @FiledTitle("Лицевой счет")
    @Column(name = "personal_account_number")
    private String personalAccountNumber;

    @Column(name = "password")
    private String password;

    @FiledTitle("Статус")
    @Column(name = "status")
    private String status;

    @FiledTitle("Тип пользователя")
    @Column(name = "position")
    private String position;

    @FiledTitle("Дата регистраций")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


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