package com.back.domain.post.postChain.entity;

import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class PostChain extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;

    private String subject;

    private boolean isAuthorConsistent;

    public PostChain(Member author, String subject, boolean isAuthorConsistent) {
        this.author = author;
        this.subject = subject;
        this.isAuthorConsistent = isAuthorConsistent;
    }
}
