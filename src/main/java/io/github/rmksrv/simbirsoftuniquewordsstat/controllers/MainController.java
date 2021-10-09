package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import io.github.rmksrv.simbirsoftuniquewordsstat.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@Controller
public class MainController {

  @Autowired private ApiController apiController;

  private static final int LAST_ASKED_URLS_AMOUNT = 20;
  private Queue<String> lastAskedUrls = new ArrayBlockingQueue<>(LAST_ASKED_URLS_AMOUNT);

  @GetMapping
  public String index(Model model) {
    model.addAttribute("url_string", "Введите адрес сайта");
    model.addAttribute("last_asked_urls", lastAskedUrls);
    return "index";
  }

  @PostMapping
  public String stats(
      @RequestParam("url_string") String URLString,
      @RequestParam(value = "case_sensitive", defaultValue = "false") boolean caseSensitive,
      Model model) {
    model.addAttribute("url_string", URLString);
    ApiResponse wordsFrequencyResponse =
        apiController.wordsFrequencyHandler(URLString, caseSensitive);
    if (wordsFrequencyResponse.getData() != null) {
      lastAskedUrls.add(URLString);
      model.addAttribute("words_frequency", wordsFrequencyResponse.getData());
      return "stats";
    } else {
      model.addAttribute("error_code", wordsFrequencyResponse.getErrorCode());
      model.addAttribute("error_message", wordsFrequencyResponse.getErrorMessage());
      return "error";
    }
  }
}
