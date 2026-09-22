package com.gameexpert.ws.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatResponse {
    String type="chat";
    String sender;
    String content;
    LocalDateTime timestamp;
    // TODO Lv 13: API 명세에 맞게 응답 필드와 생성자를 완성합니다.
    public ChatResponse( String sender, String content, LocalDateTime timestamp) {
        this.sender=sender;
        this.content=content;
        this.timestamp=timestamp;
    }
}
