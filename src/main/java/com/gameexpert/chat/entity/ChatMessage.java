package com.gameexpert.chat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.gameexpert.world.entity.World;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
// TODO Lv 2: 제공된 SQL과 같은 인덱스를 선언합니다.
// lv2 완료
// note : index는 빠르고 효율적인 검색을 위한 도구, columnlist의 좌측부터 순서대로 탐색한다.
// 여기서는 index가 world_id, created_at로 되어 있으므로
// 인덱스가 world_id순, 같은 world_id에선 created_at 순서로 정렬됨.
// Index에 저장된 순서
//
// (100, 10:00) → id 1
// (100, 11:00) → id 3
// (100, 12:00) → id 6
//
// (200, 10:00) → id 2
// (200, 11:00) → id 5
//
// (300, 10:00) → id 4
// 인덱스는 탐색할때 leftmost prefix 조건을 적용한다.
// 때문에 두번째, 세번째 컬럼으로 탐색하려고 한다면 탐색 시간이 효율적이지 않게된다.
@Table(name = "chat_messages",
indexes = @Index(
        name = "idx_chat_world_created_at",
        columnList = "world_id, created_at"
    )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "world_id", nullable = false)
    private World world;

    @Column(nullable = false, length = 16)
    private String senderNickname;

    @Column(nullable = false, length = 200)
    private String content;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public ChatMessage(World world, String senderNickname, String content) {
        this.world = world;
        this.senderNickname = senderNickname;
        this.content = content;
    }
}
