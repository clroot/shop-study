package io.clroot.study.shop.entity;

import io.clroot.study.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "test-user", roles = "USER")
    public void auditingTest() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member =
                memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);

        assertTrue(member.getRegTime().isBefore(LocalDateTime.now()));
        assertTrue(member.getUpdateTime().isBefore(LocalDateTime.now()));
        assertEquals(member.getCreatedBy(), "test-user");
        assertEquals(member.getModifiedBy(), "test-user");

        System.out.println("register time: " + member.getRegTime());
        System.out.println("update time: " + member.getUpdateTime());
        System.out.println("create member: " + member.getCreatedBy());
        System.out.println("modify member: " + member.getModifiedBy());
    }
}