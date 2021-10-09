package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import io.github.rmksrv.simbirsoftuniquewordsstat.models.ApiRequest;
import io.github.rmksrv.simbirsoftuniquewordsstat.ApiResponse;
import io.github.rmksrv.simbirsoftuniquewordsstat.models.WordsFrequencyStamp;
import io.github.rmksrv.simbirsoftuniquewordsstat.repos.ApiRequestRepository;
import io.github.rmksrv.simbirsoftuniquewordsstat.repos.WordsFrequencyStampRepository;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

  private final WordsFrequencyStampRepository wordsFrequencyStampRepository;
  private final ApiRequestRepository apiRequestRepository;

  private static final int LAST_REQUESTS_AMOUNT = 20;

  @Autowired
  public ApiController(WordsFrequencyStampRepository wordsFrequencyStampRepository, ApiRequestRepository apiRequestRepository) {
    this.wordsFrequencyStampRepository = wordsFrequencyStampRepository;
    this.apiRequestRepository = apiRequestRepository;
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
      value = "words-frequency",
      consumes = "application/json",
      produces = "application/json")
  public ApiResponse wordsFrequencyHandler(String url, boolean caseSensitive) {
    ApiResponse response = new ApiResponse();
    try {
      Document doc = Jsoup.connect(url).get();
      List<String> words = Arrays.asList(doc.text().split("\\P{L}+"));
      if (!caseSensitive) {
        words = words.stream().map(String::toLowerCase).collect(Collectors.toList());
      }

      Map<String, Object> wordsFrequency = wordsFrequency(words);
      response.setData(wordsFrequency);

      // TODO: запись в бд съедает много времени => хочется делать это после отдачи ответа
      //  (асинхронно?)
      ApiRequest request = new ApiRequest();
      request.setUrl(url);
      apiRequestRepository.save(request);
      for (Map.Entry<String, Object> wordFreq : wordsFrequency.entrySet()) {
        WordsFrequencyStamp stamp = new WordsFrequencyStamp();
        stamp.setApiRequest(request);
        stamp.setWord(wordFreq.getKey());
        stamp.setFrequency((Long) wordFreq.getValue());
        wordsFrequencyStampRepository.save(stamp);
      }
    } catch (Exception e) {
      LOGGER.error(Arrays.toString(e.getStackTrace()));
      response.setErrorCode(400);
      response.setErrorMessage(e.getMessage());
    }
    return response;
  }

  /** пилилась в общем-то только для тестов */
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

  @GetMapping(
          value = "get-last-requests",
          produces = "application/json")
  public List<ApiRequest> getLastRequestsHandler() {
    return apiRequestRepository.findAll(
            PageRequest.of(0, LAST_REQUESTS_AMOUNT, Sort.by(Sort.Direction.DESC, "timestamp"))
    ).getContent();
  }

  @PostMapping(
          value = "word-frequency-stamp",
          consumes = "application/json",
          produces = "application/json")
  public ApiResponse wordsFrequencyStampHandler(ApiRequest apiRequest) {

    return new ApiResponse();
  }
}
