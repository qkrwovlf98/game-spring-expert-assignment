package com.gameexpert.chat.service;

import com.gameexpert.chat.entity.ChatMessage;
import com.gameexpert.chat.event.ChatSavedEvent;
import org.springframework.context.ApplicationEventPublisher;
import com.gameexpert.chat.repository.ChatMessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gameexpert.chat.dto.ChatMessageResponse;
import com.gameexpert.common.NotFoundException;
import com.gameexpert.world.repository.WorldRepository;
import com.gameexpert.world.entity.World;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final int MAX_LIMIT = 100;

    private final ChatMessageRepository chatMessageRepository;
    private final WorldRepository worldRepository;
    private final ApplicationEventPublisher events;

    @Transactional
    public ChatMessageResponse saveMessage(Long worldId, String sender, String content) {
        // TODO Lv 5: 채팅을 저장하고 savedResponse(worldId, saved)의 결과를 반환합니다.
        // 0.worldId로 월드를 조회하고, 없으면 NotFoundException으로 WORLD_NOT_FOUND 에러를 던집니다.
        // 1.조회한 월드와 전달받은 닉네임, 내용으로 ChatMessage를 만들어
        // 2.chatMessageRepository.save()로 저장
        // 3.저장 결과를 saved에 담고
        // 4.savedResponse(worldId, saved)에 결과담아 반환.(여기에 이벤트 발생시키는 코드가 있다면서요? 뭔코드인가 싶어서 GPT한테 물어봤어요)

        //0
        World world = worldRepository.findById(worldId)
                .orElseThrow(() -> new NotFoundException("WORLD_NOT_FOUND"));

        //1
        ChatMessage chatMessage=new ChatMessage(
                world,
                sender,
                content
        );

        //2+3
        ChatMessage saved = chatMessageRepository.save(chatMessage);

        //4
        return savedResponse(worldId,saved);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getRecentMessages(Long worldId, int limit) {
        if (!worldRepository.existsById(worldId)) {
            throw new NotFoundException("WORLD_NOT_FOUND");
        }

        int capped = Math.min(Math.max(limit, 1), MAX_LIMIT);

        List<ChatMessage> recent = chatMessageRepository
                .findByWorldIdOrderByCreatedAtDescIdDesc(worldId, PageRequest.of(0, capped));

        // TODO Lv 5: recent를 오래된 순서로 바꾸고 응답 DTO 목록으로 반환합니다.
        Collections.reverse(recent);

        List<ChatMessageResponse> responses = new ArrayList<>();

        for (ChatMessage message : recent){
            responses.add(new ChatMessageResponse(
                    message.getSenderNickname(),
                    message.getContent(),
                    message.getCreatedAt()
                    ));
        }
        return responses;
    }

    private ChatMessageResponse savedResponse(Long worldId, ChatMessage saved) {
        events.publishEvent(new ChatSavedEvent(
                worldId,
                saved.getSenderNickname(),
                saved.getContent(),
                saved.getCreatedAt()
        ));
        return new ChatMessageResponse(
                saved.getSenderNickname(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }
}
