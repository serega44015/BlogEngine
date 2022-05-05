package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_votes")
@Data
@NoArgsConstructor
public class PostVotes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false, columnDefinition = "INT")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post postId;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date time;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private int value;

    public PostVotes(Date time, int value) {
        this.time = time;
        this.value = value;
    }

}
