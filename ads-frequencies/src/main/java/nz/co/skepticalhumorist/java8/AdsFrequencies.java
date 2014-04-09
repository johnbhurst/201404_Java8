// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-08

package nz.co.skepticalhumorist.java8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

public class AdsFrequencies implements Runnable {

  public static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
  private Path path;

  public static void main(String[] args) throws Exception {
    new AdsFrequencies(Paths.get(args[0])).run();
  }

  public AdsFrequencies(Path path) {
    this.path = path;
  }



  @Override
  public void run() {
    SortedMap<YearMonth, Integer> yearMonthCounts = new TreeMap<>();
    try {
      Files.list(path)
        .filter(p -> p.toFile().isFile())
        .filter(p -> p.toString().endsWith(".hdr"))
//        .limit(1000)
        .forEach(path -> {
          try (Stream<String> lines = Files.lines(path)) {
            lines.filter(line -> line.startsWith("STMT_TIME"))
              .forEach(line -> {
                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(Long.parseLong(line.substring(10))/1000, 0, ZoneOffset.UTC);
                YearMonth yearMonth = YearMonth.from(dateTime);
                yearMonthCounts.merge(yearMonth, 1, Math::addExact);
              });
          }
          catch (IOException ex) {
            throw new RuntimeException(ex);
          }
//          try {
////            System.out.println(String.format("File: [%s]", path.getFileName()));
//            Files.lines(path)
//              .filter(line -> line.startsWith("STMT_TIME"))
//              .forEach(line -> {
//                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(Long.parseLong(line.substring(10))/1000, 0, ZoneOffset.UTC);
////                DateTime dateTime =
////                LocalDate date = LocalDate.ofEpochDay(Long.parseLong(line.substring(10)));
//                YearMonth yearMonth = YearMonth.from(dateTime);
//                yearMonthCounts.merge(yearMonth, 1, Math::addExact);
//              });
//          }
//          catch (IOException ex) {
//            throw new RuntimeException(ex);
//          }
        });
      yearMonthCounts.forEach((yearMonth, count) -> {
        System.out.println(String.format("%s: %d", yearMonth.format(YEAR_MONTH_FORMAT), count));
      });
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
