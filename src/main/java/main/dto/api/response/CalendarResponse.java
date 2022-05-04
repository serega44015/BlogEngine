package main.dto.api.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Data
public class CalendarResponse {

    private Set<Integer> years;
    private HashMap<String, Integer> posts;

}
