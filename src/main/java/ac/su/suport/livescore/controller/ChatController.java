package ac.su.suport.livescore.controller;

import ac.su.suport.livescore.dto.ChatMessage;
import ac.su.suport.livescore.repository.ChatRoomRepository;
import ac.su.suport.livescore.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    @MessageMapping("/chat/message/{matchId}")
    public void message(@DestinationVariable String matchId, ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String nickname = (String) headerAccessor.getSessionAttributes().get("nickname");
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "Anonymous";
        }
        logger.debug("Received message from user: {}", nickname);

        message.setSender(nickname);
        message.setNickname(nickname);
        message.setRoomId(matchId);

        // 채팅방 인원수 세팅
        int userCount = (int) chatRoomRepository.getUserCount(matchId);
        logger.debug("Current user count in room {}: {}", matchId, userCount);
        message.setUserCount(userCount);

        // Websocket 에 발행된 메시지를 redis 로 발행 (publish)
        chatService.sendChatMessage(message);
        logger.info("Message sent to room {}: {}", matchId, message);
    }
}
