package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

  @Autowired private ApiController apiController;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("url_string", "Введите адрес сайта");
    return "index";
  }

  @PostMapping
  public String stats(
      @RequestParam("url_string") String URLString,
      @RequestParam(value = "case_sensitive", defaultValue = "false") boolean caseSensitive,
      Model model) {
    model.addAttribute("url_string", URLString);
    Map<String, Map<String, Object>> wordsFrequencyResponse = apiController.wordsFrequencyHandler(URLString, caseSensitive);
    if (wordsFrequencyResponse.get("data") != null) {
      model.addAttribute("words_frequency", wordsFrequencyResponse.get("data"));
      return "stats";
    } else {
      model.addAttribute("error", wordsFrequencyResponse.get("error"));
      return "error";
    }
  }

}
