package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.enums.ModerationStatus;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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
    private Integer moderatorId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private Calendar time;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", columnDefinition = "INT")
    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostVotes> postVotesList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComments> postCommentsList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tags2post",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> tagList;

    public int getLikesAmount(){
        return postVotesList.stream()
                .filter(p -> p.getValue() == 1)
                .collect(Collectors.toList()).size();
    }

    public int getDislikesAmount(){
        return postVotesList.stream()
                .filter(p -> p.getValue() == -1)
                .collect(Collectors.toList()).size();
    }

    public int getCommentCount(){
        return postCommentsList.size();
    }

    public boolean getIsActiveResult(){
        return isActive == 1 ? true : false;
    }

    public List<String> getTagNameList(){
        return tagList.stream()
                .map(t -> t.getName()).collect(Collectors.toList());
    }







}
