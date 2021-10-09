package io.github.rmksrv.simbirsoftuniquewordsstat.models;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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
  private Set<WordsFrequencyStamp> stamps = new HashSet<>();
}
