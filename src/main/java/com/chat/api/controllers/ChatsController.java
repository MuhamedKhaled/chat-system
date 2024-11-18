package com.chat.api.controllers;


import com.chat.api.controllers.resources.ChatResponse;
import com.chat.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/applications/{token}/chats")
public class ChatsController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> listChats(
            @PathVariable String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        List<ChatResponse> chats = chatService.getAllChats(token, page, limit);
        return ResponseEntity.ok(chats);
    }
    @PostMapping
    public ResponseEntity<ChatResponse> create(@PathVariable String token){
        return ResponseEntity.ok(chatService.createChat(token));
    }

    @GetMapping("/{number}")
    public ResponseEntity<ChatResponse> getChat(@PathVariable String token, @PathVariable Integer number) {
        ChatResponse chatResponse = chatService.getChatByApplicationIdAndNumber(token, number);
        return ResponseEntity.ok(chatResponse);
    }

}
