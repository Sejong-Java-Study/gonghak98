package com.example.gimmegonghakauth.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(name = "student_id",unique = true)
    private Long studentId;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Email
    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private MajorsDomain majorsDomain;

    @NotNull
    @Column(name = "name")
    private String name;

    @Builder
    public UserDomain(Long studentId, String password, String email, MajorsDomain majorsDomain,
        String name) {
        this.studentId = studentId;
        this.password = password;
        this.email = email;
        this.majorsDomain = majorsDomain;
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
