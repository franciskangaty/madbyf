package dev.madbyf.auth.notification.query.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "notifications")
public class NotificationView {
    @Id String id;
    String userId;
    String message;
    
}
