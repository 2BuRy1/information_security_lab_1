package com.security.lab.controller;

import com.security.lab.dto.MessageRequestDTO;
import com.security.lab.dto.MessageResponseDTO;
import com.security.lab.entity.User;
import com.security.lab.service.MessageService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageResponseDTO> send(
            @AuthenticationPrincipal User user, @RequestBody MessageRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.send(user, request));
    }

    @GetMapping
    public List<MessageResponseDTO> inbox(@AuthenticationPrincipal User user) {
        return messageService.inbox(user);
    }
}
