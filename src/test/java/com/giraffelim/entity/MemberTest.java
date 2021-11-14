package com.giraffelim.entity;

import com.giraffelim.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yml")
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    void auditing_test() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);

        assertThat(findMember.getCreatedBy()).isEqualTo("gildong");
        assertThat(findMember.getModifiedBy()).isEqualTo("gildong");
        assertThat(findMember.getRegTime()).isNotNull();
        assertThat(findMember.getUpdateTime()).isNotNull();
    }
}
