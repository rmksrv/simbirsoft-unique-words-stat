package io.github.rmksrv.simbirsoftuniquewordsstat;

import java.util.Map;
import lombok.Data;

@Data
public class ApiResponse {
  private Map<String, Object> data;
  private Integer errorCode;
  private String errorMessage;
}
