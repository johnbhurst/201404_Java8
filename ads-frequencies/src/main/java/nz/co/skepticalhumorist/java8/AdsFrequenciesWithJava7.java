// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-09

package nz.co.skepticalhumorist.java8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Long.parseLong;

public class AdsFrequenciesWithJava7 {

  private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM");

  public static void main(String[] args) throws IOException {
    Path dir = Paths.get(args[0]);
    SortedMap<String, Integer> yearMonthCounts = getYearMonthCounts(dir);
    for (String yearMonth : yearMonthCounts.keySet()) {
      int count = yearMonthCounts.get(yearMonth);
      String stars = stars(count);
      System.out.println(String.format("%s: %5d %s", yearMonth, count, stars));
    }
  }

  private static String stars(int count) {
    return count < 10 ? "" : String.format("%0" + count/10 + "d", 0).replace("0", "*");
  }

  private static Date date(String s) {
    return new Date(parseLong(s));
  }

  private static SortedMap<String, Integer> getYearMonthCounts(Path dir) throws IOException {
    SortedMap<String, Integer> result = new TreeMap<>();
    try (DirectoryStream<Path> paths = Files.newDirectoryStream(dir, "*.hdr")) {
      for (Path file : paths) {
        String yearMonth = getYearMonth(file);
        if (result.containsKey(yearMonth)) {
          result.put(yearMonth, result.get(yearMonth) + 1);
        }
        else {
          result.put(yearMonth, 1);
        }
      }
    }
    return result;
  }

  private static String getYearMonth(Path file) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      String line = "";
      while (line != null) {
        line = reader.readLine();
        if (line != null && line.startsWith("STMT_TIME=")) {
          Date date = date(line.substring(10));
          return FORMAT.format(date);
        }
      }
    }
    throw new RuntimeException("No STMT_TIME line found");
  }


}
