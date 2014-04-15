// Copyright 2014 SkepticalHumorist
// John Hurst (john.b.hurst@gmail.com)
// 2014-04-15

package nz.co.skepticalhumorist.java8;

import org.junit.Test;

import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class LambdaExamples {

  @Test
  public void runWithRunnable() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Hello, World.");
      }
    });
  }

  @Test
  public void runWithLambda() {
    new Thread(() -> {
      System.out.println("Hello, World.");
    });
  }

  public void lambdaUsages() throws Exception{
    Runnable runnable = () -> System.out.println("Hello");
    Callable<String> callable = () -> "Hello";
    Comparator<String> comparator = (a, b) -> a.toUpperCase().compareTo(b.toUpperCase());
    JTextArea textArea = new JTextArea();
    ActionListener actionListener = event -> textArea.setText("Hello");
  }

  @Test
  public void funnyIncrement() {
    IntFunction<Integer> postIncrement = x -> x++;
    int v = postIncrement.apply(0);
    assertEquals(0, v);

    IntFunction<Integer> preIncrement = x -> ++x;
    v = preIncrement.apply(0);
    assertEquals(1, v);
  }

  @Test
  public void functionExpressions() {
    Function<String, String> f = x -> x.toUpperCase();
    assertEquals("ONE", f.apply("one"));

    Function<String, String> g = String::toUpperCase;
    assertEquals("TWO", g.apply("two"));

    Supplier<Map<String, Integer>> creator = TreeMap::new;
    Map<String, Integer> map = creator.get();
    assertEquals(TreeMap.class, map.getClass());
  }

}
