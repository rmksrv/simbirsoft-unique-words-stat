package io.github.rmksrv.simbirsoftuniquewordsstat.models;

import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
@Data
public class WordsFrequencyStamp {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @CreationTimestamp private Timestamp timestamp;
  private String url;
  private String word;
  private Long frequency;
}
