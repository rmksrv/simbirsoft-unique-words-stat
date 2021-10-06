package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import java.io.IOException;
import java.util.Map;
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
    return "index";
  }

  @PostMapping
  public String indexViewStats(@RequestParam("urlstring") String URLString, Model model)
      throws IOException {
    Map<String, Long> wordsFrequency = apiController.wordsFrequency(URLString);
    model.addAttribute("wordsFrequency", wordsFrequency);
    return "index";
  }
}
