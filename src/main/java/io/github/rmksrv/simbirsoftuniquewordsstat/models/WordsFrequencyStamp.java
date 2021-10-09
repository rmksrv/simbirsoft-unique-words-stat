package io.github.rmksrv.simbirsoftuniquewordsstat.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
@Data
public class WordsFrequencyStamp {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  private ApiRequest apiRequest;

  private String word;
  private Long frequency;
}
