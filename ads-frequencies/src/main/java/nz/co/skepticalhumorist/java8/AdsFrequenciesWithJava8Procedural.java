// Copyright 2014 
// John Hurst (john.b.hurst@gmail.com)
// 2014
package nz.co.skepticalhumorist.java8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

public class AdsFrequenciesWithJava8Procedural {

  public static void main(String[] args) throws IOException {
    Path dir = Paths.get(args[0]);
    SortedMap<YearMonth, Integer> yearMonthCounts = getYearMonthCounts(dir);
    yearMonthCounts.forEach((yearMonth, count) -> {
      String stars = stars(count);
      System.out.println(String.format("%s: %5d %s", yearMonth, count, stars));
    });
  }

  private static String stars(int count) {
    return count < 10 ? "" : String.format("%0" + count/10 + "d", 0).replace("0", "*");
  }

  private static YearMonth yearMonth(String s) {
    return YearMonth.from(
      LocalDateTime.ofEpochSecond(parseLong(s) / 1000, 0, ZoneOffset.UTC)
    );
  }

  private static YearMonth yearMonth(Date date) {
    return YearMonth.from(
      LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC)
    );
  }

  private static long timeMillis(String s) {
    return Long.parseLong(s);
  }

  private static SortedMap<YearMonth, Integer> getYearMonthCounts(Path dir) throws IOException {
    SortedMap<YearMonth, Integer> result = new TreeMap<>();
    try (Stream<Path> files = Files.list(dir)
      //.filter(file -> file.getFileName().endsWith(".hdr"))
      .filter(file -> file.toString().endsWith(".hdr"))) {
      files.forEach(file -> {
        YearMonth yearMonth = getYearMonth(file);
        result.merge(yearMonth, 1, Math::addExact);
      });
    }
    return result;
  }

  private static YearMonth getYearMonth1(Path file) {
    try (Stream<String> lines = Files.lines(file)) {
      lines.forEach(line -> {
        if (line.startsWith("STMT_TIME=")) {
          //return yearMonth(line.substring(10));
        }
      });
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    throw new RuntimeException("No STMT_TIME line found");
  }

  private static YearMonth getYearMonth2(Path file) {
    YearMonth result = null;
    try (Stream<String> lines = Files.lines(file)) {
      lines.forEach(line -> {
        if (line.startsWith("STMT_TIME=")) {
          //result = yearMonth(line.substring(10));
        }
      });
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    if (result == null) {
      throw new RuntimeException("No STMT_TIME line found");
    }
    return result;
  }

  private static YearMonth getYearMonth(Path file) {
    Date result = new Date(0);
    try (Stream<String> lines = Files.lines(file)) {
      lines.forEach(line -> {
        if (line.startsWith("STMT_TIME=")) {
          result.setTime(Long.parseLong(line.substring(10)));
        }
      });
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    if (result.getTime() == 0L) {
      throw new RuntimeException("No STMT_TIME line found");
    }
    return YearMonth.from(
      LocalDateTime.ofInstant(result.toInstant(), ZoneOffset.UTC)
    );
  }
}
