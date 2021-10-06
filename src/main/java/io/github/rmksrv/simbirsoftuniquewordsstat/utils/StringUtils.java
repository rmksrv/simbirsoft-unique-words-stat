package io.github.rmksrv.simbirsoftuniquewordsstat.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

  public static List<String> splitMultipleDelimiters(
      String target, Iterable<Character> delimiters) {
    String regex = "[" + delimiters.toString() + "]";
    return Arrays.asList(target.split(regex));
  }
}
