package com.chat.mappers;

import com.chat.api.controllers.resources.MessageResponse;
import com.chat.data.entities.Message;
import com.chat.elasticsearch.entities.MessageDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    List<MessageResponse> toMessageResponse(List<MessageDocument> messageDocuments);

    @Mapping(target = "message", source = "body")
    MessageResponse toMessageResponse(MessageDocument messageDocument);


    List<MessageResponse> mapEntityToResponse(List<Message> messages);
    @Mapping(target = "message", source = "body")
    MessageResponse mapEntityToResponse(Message message);

}
