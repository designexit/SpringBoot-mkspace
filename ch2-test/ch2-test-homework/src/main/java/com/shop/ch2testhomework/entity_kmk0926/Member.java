package com.shop.ch2testhomework.entity_kmk0926;

import com.shop.ch2testhomework.constant_kmk0926.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
public class Member {

    // PK
    @Id
    // 컬럼 이름 수동 변경
    @Column(name="user_id")
    // 각 데이터베이스에 있는 pk 의 자동 증가번호 생성 전략, 오토
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;       //멤버 코드


    @Column(nullable = false, length = 50)
    private String userNm; //유저명

    @Column(name="userEmail", nullable = false, unique = true)
    private String userEmail; //이메일

    // 대용량,
    @Lob
    @Column(nullable = false)
    private String userDescription; //유저소개

    @Enumerated(EnumType.STRING)
    private UserRole userRole; // 유저의 권한

    private LocalDateTime regTime; //등록 시간



}
