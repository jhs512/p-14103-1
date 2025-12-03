package com.back.domain.cash.wallet.service;

import com.back.domain.cash.wallet.entity.Wallet;
import com.back.domain.cash.wallet.repository.WalletRepository;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final MemberService memberService;

    public Wallet make(Member holder) {
        Wallet wallet = new Wallet(holder);

        walletRepository.save(wallet);

        return wallet;
    }

    public Optional<Wallet> findByHolder(Member holder) {
        return walletRepository.findByHolder(holder);
    }

    public Optional<Wallet> findSystemWallet() {
        Member systemMember = memberService.findByUsername("system").get();

        return findByHolder(systemMember);
    }
}
