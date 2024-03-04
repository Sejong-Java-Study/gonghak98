package com.example.gimmegonghakauth.domain;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@Builder
@AllArgsConstructor
public class AbeekDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private MajorsDomain majorsDomain;

    @NotNull
    @Range(min = 14,max = 24)
    private int year;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    private AbeekTypeConst abeekType;

    @NotNull
    private int minCredit;

    @Lob
    private String note;

    public AbeekDomain() {

    }
}
