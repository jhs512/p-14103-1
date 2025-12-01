package com.back.domain.post.postChainItem.entity;

import com.back.domain.post.post.entity.Post;
import com.back.domain.post.postChain.entity.PostChain;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class PostChainItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private PostChain postChain;

    @ManyToOne(fetch = LAZY)
    private Post post;

    private int no;

    public PostChainItem(PostChain postChain, Post post, int no) {
        this.postChain = postChain;
        this.post = post;
        this.no = no;
    }
}
