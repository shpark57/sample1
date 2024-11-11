package com.example.sample1.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter //Getter 메소드 자동 생성
@Setter //Setter 메소드 자동 생성
@ToString //toString 메소드 자동 생성
@EqualsAndHashCode //equals, hashCode 메소드 자동 생성
@Entity //JPA Entity 클래스임을 지정
@Table(name = "t_usr_info_mgnt", uniqueConstraints = @UniqueConstraint(columnNames = {"usrId", "tenantId"}))
public class UsrInfoMgnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 20)
    private String usrId;
    @Column(nullable = false, length = 100)
    private String scrtNo;
    @Column(nullable = false, length = 5)
    private String tenantId;

    private String usrGrd;

    private String acStCd;

    private LocalDateTime scrtNoLstUpdDtm;

    private Integer pwErrTcnt = 0;

    private LocalDateTime lstLginDtm;
}
