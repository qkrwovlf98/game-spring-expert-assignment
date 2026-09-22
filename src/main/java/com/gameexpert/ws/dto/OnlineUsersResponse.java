package com.gameexpert.ws.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OnlineUsersResponse {
    String type = "onlineUsers";
    List<String> users;
    int count;
    // TODO Lv 15: API 명세에 맞게 응답 필드와 생성자를 완성합니다.
    public OnlineUsersResponse(List<String> users, int count) {
        this.users=users;
        this.count=count;
    }
}
