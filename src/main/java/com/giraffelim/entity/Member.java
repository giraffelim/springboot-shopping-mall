package com.giraffelim.entity;

import com.giraffelim.constant.Role;
import com.giraffelim.dto.MemberFormDto;
import com.giraffelim.entity.audit.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        member.setRole(Role.USER);
        return member;
    }
}
