package io.github.rmksrv.simbirsoftuniquewordsstat.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
@Data
public class ApiRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @CreationTimestamp private Timestamp timestamp;

  private String url;

  @OneToMany(mappedBy = "apiRequest", cascade = CascadeType.ALL)
  private List<WordsFrequencyStamp> stamps = new ArrayList<>();
}
