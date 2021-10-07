package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

  private Map<String, Object> wordsFrequency(List<String> words) {
    return words.stream()
        .filter(w -> !w.equals(""))
        .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  @PostMapping(
      value = "/words-frequency",
      consumes = "application/json",
      produces = "application/json")
  public Map<String, Map<String, Object>> wordsFrequencyHandler(
      String urlString, boolean caseSensitive) {
    Map<String, Map<String, Object>> resp = new HashMap<>();
    try {
      Document doc = Jsoup.connect(urlString).get();
      List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
      if (!caseSensitive) {
        words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
      }
      resp.put("data", wordsFrequency(words));
    } catch (MalformedURLException | IllegalArgumentException e) {
      e.printStackTrace();
      resp.put(
          "error",
          Map.of("message", String.format("Введен некорректный URL: %s", urlString), "code", 400));
    } catch (Exception e) {
      e.printStackTrace();
      resp.put("error", Map.of("message", e.getMessage(), "code", 400));
    }
    return resp;
  }

  public Map<String, Map<String, Object>> wordsFrequencyHandler(
      File localPage, boolean caseSensitive) {
    Map<String, Map<String, Object>> resp = new HashMap<>();
    try {
      Document doc = Jsoup.parse(localPage, null);
      List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
      if (!caseSensitive) {
        words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
      }
      resp.put("data", wordsFrequency(words));
    } catch (Exception e) {
      e.printStackTrace();
      resp.put("error", Map.of("message", e.getMessage(), "code", 400));
    }
    return resp;
  }
}
