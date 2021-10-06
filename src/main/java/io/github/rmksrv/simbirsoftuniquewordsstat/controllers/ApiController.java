package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

  @PostMapping(
      value = "/words-frequency",
      consumes = "application/json",
      produces = "application/json")
  public Map<String, Long> wordsFrequency(
      @RequestParam("url-string") String URLString, boolean caseSensitive) throws IOException {
    Document doc = Jsoup.connect(URLString).get();
    List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
    if (!caseSensitive) {
      words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
    }
    return words.stream()
        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }
}
