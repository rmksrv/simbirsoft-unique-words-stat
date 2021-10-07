package io.github.rmksrv.simbirsoftuniquewordsstat;


import io.github.rmksrv.simbirsoftuniquewordsstat.controllers.ApiController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class SimbirsoftUniqueWordsStatApplicationTests {

  @Autowired private ApiController apiController;
  private static final File ONLY_SPACES_PAGE = new File("src/main/resources/templates/test_pages/only_spaces.html");
  private static final File SAMPLE_PAGE = new File("src/main/resources/templates/test_pages/sample.html");

  @Test
  void testSmoke() {
    Map<String, Map<String, Object>> response = apiController.wordsFrequencyHandler(ONLY_SPACES_PAGE, false);
    assertNull(response.get("error"));
    assertEquals(response.get("data").get("раз"), 1L);
    assertEquals(response.get("data").get("два"), 2L);
    assertEquals(response.get("data").get("три"), 3L);
  }

  @Test
  void testSmokeCaseSensitive() {
    Map<String, Map<String, Object>> response = apiController.wordsFrequencyHandler(ONLY_SPACES_PAGE, true);
    assertNull(response.get("error"));
    assertEquals(response.get("data").get("Раз"), 1L);
    assertEquals(response.get("data").get("два"), 1L);
    assertEquals(response.get("data").get("Два"), 1L);
    assertEquals(response.get("data").get("три"), 3L);
  }

  @Test
  void testDifferentDelimiters() {
    Map<String, Map<String, Object>> response = apiController.wordsFrequencyHandler(SAMPLE_PAGE, false);
    assertNull(response.get("error"));
    assertEquals(response.get("data").size(), 79);
    assertEquals(response.get("data").get("пол"), 3L);
    assertEquals(response.get("data").get("она"), 4L);
  }

  @ParameterizedTest
  @ValueSource(strings = {"asdf", "htps://asadfsdf"})
  void testInvalidUrl(String urlString) {
    Map<String, Map<String, Object>> response = apiController.wordsFrequencyHandler(urlString, false);
    assertNull(response.get("data"));
    assertEquals(response.get("error").get("code"), 400);
    assertEquals(response.get("error").get("message"), String.format("Введен некорректный URL: %s", urlString));
  }
}
