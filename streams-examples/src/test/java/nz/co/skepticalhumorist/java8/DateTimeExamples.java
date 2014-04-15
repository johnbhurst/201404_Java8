// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-15

package nz.co.skepticalhumorist.java8;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeExamples {

  @Test
  public void localDate() {
    GregorianCalendar cal = new GregorianCalendar(2014, 3, 15);
    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = cal.getTime();

    LocalDate ld = LocalDate.of(2014, 4, 15);

    assertEquals(ld, date.toInstant().atZone(ZoneOffset.UTC).toLocalDate());
    assertEquals(2014, ld.getYear());
    assertEquals("2014-04-15", ld.toString());
    assertEquals("15 Apr", ld.format(DateTimeFormatter.ofPattern("dd MMM")));
  }

  @Test
  public void localDateTime() {
    GregorianCalendar cal = new GregorianCalendar(2014, 3, 15, 11, 56, 43);
    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = cal.getTime();

    LocalDateTime ldt = LocalDateTime.of(2014, 4, 15, 11, 56, 43);

    assertEquals(ldt, date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
    assertEquals(11, ldt.getHour());
  }

}
