package io.github.rmksrv.simbirsoftuniquewordsstat.models;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class WordsFrequencyStamp {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "request_id")
  private ApiRequest apiRequest;

  private String word;
  private Long frequency;
}
