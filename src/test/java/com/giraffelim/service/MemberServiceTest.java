package com.giraffelim.service;

import com.giraffelim.dto.MemberFormDto;
import com.giraffelim.entity.Member;
import com.giraffelim.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, new BCryptPasswordEncoder());
    }

    @Test
    @DisplayName("회원가입 테스트")
    void save_member_test() {
        Member member = createMember();
        given(memberRepository.save(any())).willReturn(member);

        Member savedMember = memberService.saveMember(member);

        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(savedMember.getName()).isEqualTo(member.getName());
        assertThat(savedMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    void save_duplicate_member_test() {
        Member member1 = createMember();
        Member member2 = createMember();
        given(memberRepository.findByEmail(any())).willReturn(member1);

        assertThatIllegalStateException().isThrownBy(() -> {
            memberService.saveMember(member2);
        }).withMessage("이미 가입된 회원입니다.");
    }
}