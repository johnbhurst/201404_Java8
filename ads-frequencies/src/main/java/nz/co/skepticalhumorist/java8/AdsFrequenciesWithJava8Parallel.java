// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-15

package nz.co.skepticalhumorist.java8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

public class AdsFrequenciesWithJava8Parallel {

  public static void main(String[] args) throws IOException {
    Path dir = Paths.get(args[0]);
    Map<YearMonth, Long> yearMonthCounts = getYearMonthCounts(dir);
    yearMonthCounts.forEach((yearMonth, count) -> {
      String stars = stars(count);
      System.out.println(String.format("%s: %5d %s", yearMonth, count, stars));
    });
  }

  private static String stars(Long count) {
    return count< 10 ? "" : Stream.generate(() -> "*").limit(count/10).reduce(String::concat).get();
  }

  private static YearMonth yearMonth(String s) {
    return YearMonth.from(
      LocalDateTime.ofEpochSecond(parseLong(s) / 1000, 0, ZoneOffset.UTC)
    );
  }

  private static String slow(String s) {
    try {
      Thread.sleep(1);
    }
    catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    return s;
  }

  private static Map<YearMonth, Long> getYearMonthCounts(Path dir) throws IOException {
    try (Stream<Path> files = Files.list(dir).parallel()) {
      return files
        .filter(file -> file.toString().endsWith(".hdr"))
        .collect(Collectors.groupingBy(
          AdsFrequenciesWithJava8Parallel::getYearMonth,
          TreeMap::new,
          Collectors.counting()
        ));
    }
  }

  private static YearMonth getYearMonth(Path file) {
    try (Stream<String> lines = Files.lines(file)) {
      return lines
        .filter(line -> line.startsWith("STMT_TIME="))
        .map(s -> s.substring(10))
        .map(AdsFrequenciesWithJava8Parallel::slow)
        .map(AdsFrequenciesWithJava8Parallel::yearMonth)
        .findAny()
        .orElseThrow(RuntimeException::new);
    }
    catch (IOException ex) {
      throw new RuntimeException(String.format("Exception reading file %s", file), ex);
    }
  }
}
