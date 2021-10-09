package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import io.github.rmksrv.simbirsoftuniquewordsstat.ApiResponse;
import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import io.github.rmksrv.simbirsoftuniquewordsstat.models.WordsFrequencyStamp;
import io.github.rmksrv.simbirsoftuniquewordsstat.repos.WordsFrequencyStampRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
  private final WordsFrequencyStampRepository wordsFrequencyStampRepository;

  @Autowired
  public ApiController(WordsFrequencyStampRepository wordsFrequencyStampRepository) {
    this.wordsFrequencyStampRepository = wordsFrequencyStampRepository;
  }

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

      Map<String, Object> wordsFrequency = wordsFrequency(words);
      apiResponse.setData(wordsFrequency);
      // TODO: запись в бд съедает много времени => хочется делать это после отдачи ответа (асинхронно?)
      for (Map.Entry<String, Object> wordFreq : wordsFrequency.entrySet()) {
        WordsFrequencyStamp stamp = new WordsFrequencyStamp();
        stamp.setUrl(urlString);
        stamp.setWord(wordFreq.getKey());
        stamp.setFrequency((Long) wordFreq.getValue());
        wordsFrequencyStampRepository.save(stamp);
      }

    } catch (Exception e) {
      LOGGER.error(Arrays.toString(e.getStackTrace()));
      apiResponse.setErrorCode(400);
      apiResponse.setErrorMessage(e.getMessage());
    }
    return apiResponse;
  }

  /**
   * пилилась в общем-то только для тестов
   */
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
      LOGGER.error(Arrays.toString(e.getStackTrace()));
      apiResponse.setErrorCode(400);
      apiResponse.setErrorMessage(e.getMessage());
    }
    return apiResponse;
  }
}
