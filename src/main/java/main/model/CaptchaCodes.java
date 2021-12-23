package main.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "captcha_codes")
public class CaptchaCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date time;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private String code;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private String secretCode;

}
