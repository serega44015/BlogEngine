package main.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(name = "is_moderator", nullable = false, columnDefinition = "TINYINT")
    private int isModerator;

    @Column(name = "reg_time", nullable = false, columnDefinition = "DATETIME")
    private Date regTime;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(nullable = true, columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String photo;


}
