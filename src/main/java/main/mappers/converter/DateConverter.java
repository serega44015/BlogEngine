package main.mappers.converter;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateConverter {
  private static Long thousand = 1000L;

  @Named("convertLong")
  public static LocalDateTime longToDate(Long dateLong) {
    return null == dateLong
        ? null
        : LocalDateTime.ofInstant(
            Instant.ofEpochMilli(dateLong * thousand), ZoneId.of("Europe/Moscow"));
  }

  @Named("convertDate")
  public static Long dateToLong(LocalDateTime localDateTime) {
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / thousand;
  }
}
