package com.chat;

import com.chat.data.entities.Message;
import com.chat.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<Message> createMessage(){
        Message savedMessage = messageService.saveMessage(3, 1L, "mosdsmdteterwere");
        return ResponseEntity.ok(savedMessage);
    }
}