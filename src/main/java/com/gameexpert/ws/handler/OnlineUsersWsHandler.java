package com.gameexpert.ws.handler;

import java.util.ArrayList;
import java.util.List;
import com.gameexpert.ws.NicknameHandshakeInterceptor;
import com.gameexpert.ws.WorldBroadcaster;
import com.gameexpert.ws.WorldSessionRegistry;
import com.gameexpert.ws.WsMessageContext;
import com.gameexpert.ws.dto.OnlineUsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.JsonNode;

@Component
@RequiredArgsConstructor
public class OnlineUsersWsHandler implements WsMessageHandler {
    private final WorldSessionRegistry registry;
    private final WorldBroadcaster broadcaster;

    @Override
    public String type() {
        return "onlineUsers";
    }

    @Override
    public void handle(WsMessageContext context, JsonNode message) {
        // TODO Lv 15: 현재 월드의 열린 연결에서 닉네임을 조회하고 요청자에게 응답합니다.
        List<String> onlineUsers=new ArrayList<>();
        for(var x : registry.entries(context.worldId())){
            if(x.session().isOpen()){
                onlineUsers.add((String)x.session().getAttributes()
                        .get(NicknameHandshakeInterceptor.ATTR_NICKNAME));
            }
        }
        onlineUsers.sort(String::compareTo);
        OnlineUsersResponse onlineUsersResponse = new OnlineUsersResponse(onlineUsers,onlineUsers.size());
        broadcaster.sendTo(context.session(), onlineUsersResponse);
    }
}
