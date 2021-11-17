package io.clroot.study.shop.entity;

import io.clroot.study.shop.constant.Role;
import io.clroot.study.shop.dto.MemberFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDTO memberFormDTO,
                                      PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(memberFormDTO.getPassword());

        Member member = new Member();
        member.setName(memberFormDTO.getName());
        member.setEmail(memberFormDTO.getEmail());
        member.setAddress(memberFormDTO.getAddress());
        member.setPassword(password);
        member.setRole(Role.USER);

        return member;
    }
}
