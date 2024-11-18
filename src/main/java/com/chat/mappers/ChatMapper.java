package com.chat.mappers;

import com.chat.api.controllers.resources.ChatResponse;
import com.chat.data.entities.Chat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> toChatResponse(List<Chat> chats);
    ChatResponse toChatResponse(Chat chat);
}
