package com.security.lab.service;

import com.security.lab.dto.MessageRequestDTO;
import com.security.lab.dto.MessageResponseDTO;
import com.security.lab.entity.Message;
import com.security.lab.entity.User;
import com.security.lab.repository.MessageRepository;
import com.security.lab.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public MessageResponseDTO send(User sender, MessageRequestDTO request) {
        if (request == null
                || request.recipient() == null
                || request.recipient().isBlank()
                || request.text() == null
                || request.text().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "'recipient' and 'text' must not be blank");
        }

        var recipient =
                userRepository
                        .findUsersByLogin(request.recipient())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Recipient '"
                                                        + request.recipient()
                                                        + "' not found"));

        var message =
                messageRepository.save(
                        new Message(sender, recipient, request.text(), LocalDateTime.now()));
        return toResponseDTO(message);
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDTO> inbox(User owner) {
        return messageRepository.findByRecipientIdOrderByCreatedAtDesc(owner.getId()).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private MessageResponseDTO toResponseDTO(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getSender().getUsername(),
                message.getRecipient().getUsername(),
                message.getText(),
                message.getCreatedAt());
    }
}
