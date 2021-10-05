package io.github.rmksrv.simbirsoftuniquewordsstat.controllers;

import io.github.rmksrv.simbirsoftuniquewordsstat.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class ApiController {

  @PostMapping(value = "/words-frequency", consumes = "application/json", produces = "application/json")
  public Map<String, Long> wordsFrequency(@RequestParam("urlstring") String URLString) throws IOException {
    List<Character> delimiters = List.of(' ', ',', '.', '!', '?', '\n', '\t', '\r');  // TODO: вынести в параметры
    Document doc = Jsoup.connect(URLString).get();
    String content= doc.text();
    List<String> words = StringUtils.splitMultipleDelimiters(content, delimiters);
    var foo = words.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    return foo.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1, LinkedHashMap::new)
            );
  }

}
