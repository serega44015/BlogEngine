package main.helpJPA;

import lombok.Data;

@Data
public class JpaSQLHelp {

    private String date;
    private Integer count;


    public JpaSQLHelp(String date, Integer count) {
        this.date = date;
        this.count = count;
    }

}
