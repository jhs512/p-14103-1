package com.back.domain.post.post.entity;

import com.back.domain.member.member.entity.Member;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
public class Post extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member author;

    private String subject;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    public Post(Member author, String subject, String content) {
        this.author = author;
        this.subject = subject;
        this.content = content;
    }
}
