package main.helpJPA;

import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

@Data
public class JpaSQLHelp {

    private String date;
    private Integer count;

    public JpaSQLHelp(){

    }

    public JpaSQLHelp(String date) {
        this.date = date;
        this.count = count;
    }


}
