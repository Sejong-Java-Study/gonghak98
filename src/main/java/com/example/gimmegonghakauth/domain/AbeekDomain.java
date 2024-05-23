package com.example.gimmegonghakauth.domain;

import com.example.gimmegonghakauth.constant.AbeekTypeConst;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "abeek")
public class AbeekDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abeek_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private MajorsDomain majorsDomain;

    @NotNull
    @Range(min = 14,max = 24)
    @Column(name = "year")
    private int year;

    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "abeek_type")
    private AbeekTypeConst abeekType;

    @NotNull
    @Column(name = "min_credit")
    private int minCredit;

    @Lob
    @Column(name = "note")
    private String note;
}
