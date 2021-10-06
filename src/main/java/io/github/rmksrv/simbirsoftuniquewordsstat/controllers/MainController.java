package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.IOException;
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
  public String index(Model model) throws IOException {
    model.addAttribute("urlstring", "Введите адрес сайта");
    return "index";
  }

  @PostMapping
  public String stats(
      @RequestParam("urlstring") String URLString,
      @RequestParam(value = "case-sensitive", defaultValue = "false") boolean caseSensitive,
      Model model)
      throws IOException {
    model.addAttribute("urlstring", URLString);
    model.addAttribute("wordsFrequency", apiController.wordsFrequency(URLString, caseSensitive));
    return "stats";
  }
}
