// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-08

package nz.co.skepticalhumorist.java8;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class StreamsExamples {

  @Test
  public void testOk() {
    Stream<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream();

//    int sum = 0;
//    ints.forEach(value -> {
//      sum += value;
//    });
//    int sum = ints.collect(Collectors.summingInt(x -> x));
//    assertEquals(55, sum);


    int sumEvens = ints.filter(x -> x % 2 ==0).collect(Collectors.summingInt(x -> x));
    assertEquals(30, sumEvens);


//    ints.forEach(value -> {
//      System.out.println("value = " + value);
//    });

  }

  @Test(expected = IllegalStateException.class)
  public void cannotReuseStreams() {
    Stream<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream();
    Predicate<Integer> isEven = v -> (v & 1) == 0;
    Predicate<Integer> isOdd = v -> (v & 1) == 1;
    List<Integer> evens = ints.filter(isEven).collect(Collectors.toList());
    List<Integer> odds = ints.filter(isOdd).collect(Collectors.toList());
  }

  @Test
  public void charactersStream() {
    assertEquals('*', (char) 42);
    // assertEquals("*****", IntStream.generate(() -> 42).limit(5).map((i) -> (char) i).reduce())
    assertEquals("*****", Stream.generate(() -> "*").limit(5).reduce((x, y) -> x + y).get());
    assertEquals("*****", Stream.generate(() -> "*").limit(5).reduce(String::concat).get());
  }



  @Test
  public void functionalSetProcessing() {
    Map<String, List<Integer>> oldList = new TreeMap<>();
    Map<String, List<Integer>> newList = new TreeMap<>();
    oldList.put("ONE", Arrays.asList(1, 2, 3));
    oldList.put("TWO", Arrays.asList(4, 5, 6));
    oldList.put("THREE", Arrays.asList(7, 8, 9));
    newList.put("TWO", Arrays.asList(10, 11, 12));
    newList.put("THREE", Arrays.asList(13, 14, 15));
    newList.put("FOUR", Arrays.asList(16, 17, 18));

  }


}
