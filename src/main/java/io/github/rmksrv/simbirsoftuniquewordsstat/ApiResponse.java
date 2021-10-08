package io.github.rmksrv.simbirsoftuniquewordsstat;

import lombok.Data;

import java.util.Map;

@Data
public class ApiResponse {
  private Map<String, Object> data;
  private Integer errorCode;
  private String errorMessage;
}
