package com.back.domain.member.member.service;

import com.back.domain.cart.cart.entity.Cart;
import com.back.domain.cart.cart.service.CartService;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.global.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CartService cartService;

    public long count() {
        return memberRepository.count();
    }

    public Member join(String username, String password, String nickname) {
        findByUsername(username)
                .ifPresent(_ -> {
                    throw new BusinessException("409-1", "해당 username(%s)은 이미 사용중 입니다.".formatted(username));
                });

        Member member = new Member(username, password, nickname);

        cartService.make(member);

        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
