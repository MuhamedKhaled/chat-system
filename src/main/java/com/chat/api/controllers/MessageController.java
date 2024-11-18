package com.chat.api.controllers;

import com.chat.api.controllers.resources.ChatResponse;
import com.chat.api.controllers.resources.MessageRequest;
import com.chat.api.controllers.resources.MessageResponse;
import com.chat.services.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/applications/{token}/chats/{chat_number}/messages")
public class MessageController {

    private final MessageService messageService;


    @GetMapping
    public ResponseEntity<?> getMessages(@PathVariable String token, @PathVariable Integer chat_number,
            @RequestParam(required = false) String query,
                                         @RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        List<MessageResponse> messages = messageService.listMessages(token,chat_number, query, pageable);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@PathVariable String token,@PathVariable Integer chat_number,@Valid @RequestBody MessageRequest request){
        return ResponseEntity.ok(messageService.createMessage(token,chat_number,request));
    }

}
