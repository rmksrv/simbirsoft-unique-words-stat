package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.IOException;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

  @Autowired private ApiController apiController;
  private static final UrlValidator urlValidator = new UrlValidator();

  @GetMapping
  public String index(Model model) {
    model.addAttribute("url_string", "Введите адрес сайта");
    return "index";
  }

  @PostMapping
  public String stats(
      @RequestParam("url_string") String URLString,
      @RequestParam(value = "case_sensitive", defaultValue = "false") boolean caseSensitive,
      Model model)
      throws IOException {
    if (!urlValidator.isValid(URLString)) {
      model.addAttribute("error_msg", String.format("%s - некорректный URL", URLString));
      return "index";
    }
    model.addAttribute("url_string", URLString);
    model.addAttribute("words_frequency", apiController.wordsFrequency(URLString, caseSensitive));
    return "stats";
  }

  @GetMapping("/error")
  public String error() {
    return "error";
  }

}
