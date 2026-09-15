package com.security.lab.repository;

import com.security.lab.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
