package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

import io.github.rmksrv.simbirsoftuniquewordsstat.ApiResponse;
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
  public ApiResponse wordsFrequencyHandler(String urlString, boolean caseSensitive) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      Document doc = Jsoup.connect(urlString).get();
      List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
      if (!caseSensitive) {
        words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
      }
      apiResponse.setData(wordsFrequency(words));
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse.setErrorCode(400);
      apiResponse.setErrorMessage(e.getMessage());
    }
    return apiResponse;
  }

  public ApiResponse wordsFrequencyHandler(File localPage, boolean caseSensitive) {
    ApiResponse apiResponse = new ApiResponse();
    try {
      Document doc = Jsoup.parse(localPage, null);
      List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
      if (!caseSensitive) {
        words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
      }
      apiResponse.setData(wordsFrequency(words));
    } catch (Exception e) {
      e.printStackTrace();
      apiResponse.setErrorCode(400);
      apiResponse.setErrorMessage(e.getMessage());
    }
    return apiResponse;
  }
}
