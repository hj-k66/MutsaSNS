package com.sns.mutsasns.domain.entity;

import com.sns.mutsasns.domain.dto.posts.PostDto;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Where(clause = "deleted_at IS NULL")
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;
    private String title;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    public PostDto toDto(){
        return PostDto.builder()
                .postId(this.id)
                .title(this.title)
                .body(this.body)
                .userName(this.user.getUserName())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

}
