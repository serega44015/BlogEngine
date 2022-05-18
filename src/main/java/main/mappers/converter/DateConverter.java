package main.mappers.converter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

@Component
public class DateConverter {

    @Named("convertRegDate")
    public long convertRegDate(Calendar calendarTime) {

        if (calendarTime == null) {
            return 0;
        }

        return calendarTime.getTime().getTime() / 1000;
    }

    public LocalDateTime longToDate(Long dateLong) {
        return null == dateLong ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.systemDefault());
    }

    public LocalDateTime ageToBirthDate(Integer age) {
        return null == age ? null : LocalDateTime.now().minusYears(age);
    }




}



