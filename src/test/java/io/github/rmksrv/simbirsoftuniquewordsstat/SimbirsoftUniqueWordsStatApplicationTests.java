package io.github.rmksrv.simbirsoftuniquewordsstat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.github.rmksrv.simbirsoftuniquewordsstat.controllers.ApiController;
import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimbirsoftUniqueWordsStatApplicationTests {

  @Autowired private ApiController apiController;
  private static final File ONLY_SPACES_PAGE =
      new File("src/main/resources/templates/test_pages/only_spaces.html");
  private static final File SAMPLE_PAGE =
      new File("src/main/resources/templates/test_pages/sample.html");

  @Test
  void testSmoke() {
    ApiResponse response = apiController.wordsFrequencyHandler(ONLY_SPACES_PAGE, false);
    assertNull(response.getErrorCode());
    assertNull(response.getErrorMessage());
    assertEquals(response.getData().get("раз"), 1L);
    assertEquals(response.getData().get("два"), 2L);
    assertEquals(response.getData().get("три"), 3L);
  }

  @Test
  void testSmokeCaseSensitive() {
    ApiResponse response = apiController.wordsFrequencyHandler(ONLY_SPACES_PAGE, true);
    assertNull(response.getErrorCode());
    assertNull(response.getErrorMessage());
    assertEquals(response.getData().get("Раз"), 1L);
    assertEquals(response.getData().get("два"), 1L);
    assertEquals(response.getData().get("Два"), 1L);
    assertEquals(response.getData().get("три"), 3L);
  }

  @Test
  void testDifferentDelimiters() {
    ApiResponse response = apiController.wordsFrequencyHandler(SAMPLE_PAGE, false);
    assertNull(response.getErrorCode());
    assertNull(response.getErrorMessage());
    assertEquals(response.getData().size(), 79);
    assertEquals(response.getData().get("пол"), 3L);
    assertEquals(response.getData().get("она"), 4L);
  }

  @ParameterizedTest
  @ValueSource(strings = {"asdf", "htps://asadfsdf"})
  void testInvalidUrl(String urlString) {
    ApiResponse response = apiController.wordsFrequencyHandler(urlString, false);
    assertNull(response.getData());
    assertEquals(response.getErrorCode(), 400);
  }
}
