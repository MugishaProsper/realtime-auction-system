package com.webapp.realtimeauctionbackend.services;

import com.webapp.realtimeauctionbackend.models.*;
import com.webapp.realtimeauctionbackend.repositories.TransactionNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private TransactionNotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendTransactionNotification(Transaction transaction, String message) {
        Person user = transaction.getWallet().getUser();
        
        // Create and save notification
        TransactionNotification notification = new TransactionNotification();
        notification.setTransaction(transaction);
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);

        // Send WebSocket notification
        String destination = "/user/" + user.getId() + "/queue/notifications";
        messagingTemplate.convertAndSend(destination, notification);
    }

    @Transactional(readOnly = true)
    public List<TransactionNotification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(
            Person.builder().id(userId).build()
        );
    }

    @Transactional(readOnly = true)
    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadNotifications(
            Person.builder().id(userId).build()
        );
    }

    @Transactional
    public void markNotificationsAsRead(Long userId) {
        notificationRepository.markAllAsRead(
            Person.builder().id(userId).build()
        );
    }

    @Transactional(readOnly = true)
    public List<TransactionNotification> getRecentNotifications(Long userId, LocalDateTime since) {
        return notificationRepository.findRecentNotifications(
            Person.builder().id(userId).build(),
            since
        );
    }
} 