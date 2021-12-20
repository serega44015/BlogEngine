package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(name = "is_active",nullable = false, columnDefinition = "TINYINT")
    private int isActive;

    @Column(name = "moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus = ModerationStatus.NEW;

    @Column(name = "moderator_id", columnDefinition = "INT")
    private int moderatorId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false, columnDefinition = "INT")
    private int viewCount;




}
