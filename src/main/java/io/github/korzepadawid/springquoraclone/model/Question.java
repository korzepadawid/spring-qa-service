package io.github.korzepadawid.springquoraclone.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "questions")
public class Question extends BaseEntity implements Serializable {

  @NotBlank
  @Size(min = 3, max = 255)
  private String title;

  @NotBlank
  @Column(unique = true)
  @Size(min = 3, max = 255)
  private String description;

  @NotNull
  private Boolean anonymous = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_user_id")
  private AppUser author;

  @OneToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      mappedBy = "question"
  )
  private final Set<Answer> answers = new HashSet<>();

  @Embedded
  private final DateAudit dateAudit = new DateAudit();
}
