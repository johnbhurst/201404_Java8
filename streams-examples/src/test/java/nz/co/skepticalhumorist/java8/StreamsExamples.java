// Copyright 2014 Skeptical Humorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-08

package nz.co.skepticalhumorist.java8;

import org.junit.Test;

import java.lang.IllegalStateException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsExamples {

  @Test
  public void testOk() {
    List<Integer> v = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
  }

  @Test(expected = IllegalStateException.class)
  public void cannotReuseStreams() {
    Stream<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream();
    Predicate<Integer> isEven = v -> (v & 1) == 0;
    Predicate<Integer> isOdd = v -> (v & 1) == 1;
    List<Integer> evens = ints.filter(isEven).collect(Collectors.toList());
    List<Integer> odds = ints.filter(isOdd).collect(Collectors.toList());
  }


}
