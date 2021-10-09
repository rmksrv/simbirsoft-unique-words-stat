package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import io.github.rmksrv.simbirsoftuniquewordsstat.models.ApiRequest;
import io.github.rmksrv.simbirsoftuniquewordsstat.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

  @Autowired private ApiController apiController;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("url_string", "Введите адрес сайта");
    model.addAttribute("last_requests", apiController.getLastRequestsHandler());
    return "index";
  }

  @PostMapping
  public String stats(
      @RequestParam("url") String url,
      @RequestParam(value = "case_sensitive", defaultValue = "false") boolean caseSensitive,
      Model model) {
    model.addAttribute("url", url);
    ApiResponse wordsFrequencyResponse =
        apiController.wordsFrequencyHandler(url, caseSensitive);
    if (wordsFrequencyResponse.getData() != null) {
      model.addAttribute("words_frequency", wordsFrequencyResponse.getData());
      return "stats";
    } else {
      model.addAttribute("error_code", wordsFrequencyResponse.getErrorCode());
      model.addAttribute("error_message", wordsFrequencyResponse.getErrorMessage());
      return "error";
    }
  }

  @PostMapping("stamp")
  public String statsDbStamps(@RequestParam("request_id") ApiRequest apiRequest, Model model) {
    model.addAttribute("url", apiRequest.getUrl());
    model.addAttribute("words_frequency", apiController.wordsFrequencyStampHandler(apiRequest).getData());
    return "stats";
  }
}
