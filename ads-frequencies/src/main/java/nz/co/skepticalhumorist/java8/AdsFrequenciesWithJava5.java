// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-09

package nz.co.skepticalhumorist.java8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Long.parseLong;

public class AdsFrequenciesWithJava5 {

  public static void main(String[] args) throws IOException {
    File dir = new File(args[0]);
    SortedMap<String, Integer> yearMonthCounts = getYearMonthCounts(dir);
    for (String yearMonth : yearMonthCounts.keySet()) {
      int count = yearMonthCounts.get(yearMonth);
      String stars = stars(count);
      System.out.println(String.format("%s: %5d %s", yearMonth, count, stars));
    }
  }

  private static Date date(String s) {
    return new Date(parseLong(s));
  }

  private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM");

  private static String stars(int count) {
    return count < 10 ? "" : String.format("%0" + count/10 + "d", 0).replace("0", "*");
      //String.format(String.format("%%0%dd", count), 0).replace("0", "*");
  }

  private static SortedMap<String, Integer> getYearMonthCounts(File dir) throws IOException {
    SortedMap<String, Integer> result = new TreeMap<String, Integer>();
    for (File file : dir.listFiles(HdrFilenameFilter.INSTANCE)) {
      String yearMonth = getYearMonth(file);
      if (result.containsKey(yearMonth)) {
        result.put(yearMonth, result.get(yearMonth) + 1);
      }
      else {
        result.put(yearMonth, 1);
      }
    }
    return result;
  }

  private static String getYearMonth(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    try {
      String line = "";
      while (line != null) {
        line = reader.readLine();
        if (line != null && line.startsWith("STMT_TIME=")) {
          Date date = date(line.substring(10));
          return FORMAT.format(date);
        }
      }
    }
    finally {
      reader.close();
    }
    throw new RuntimeException("No STMT_TIME line found");
  }

  private static class HdrFilenameFilter implements FilenameFilter {
    public static final FilenameFilter INSTANCE = new HdrFilenameFilter();
    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(".hdr");
    }
  }
}
