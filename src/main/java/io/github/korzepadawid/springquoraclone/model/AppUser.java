package io.github.korzepadawid.springquoraclone.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
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
@Table(name = "app_users")
public class AppUser extends AbstractBaseEntity implements Serializable {

  @Email
  @NotBlank
  @Column(unique = true)
  @Size(min = 3, max = 255)
  private String email;

  @NotBlank
  @Column(unique = true)
  @Size(min = 3, max = 255)
  private String username;

  @NotBlank
  private String password;

  @Size(max = 255)
  private String description;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AppUser)) {
      return false;
    }

    AppUser appUser = (AppUser) o;

    if (!email.equals(appUser.email)) {
      return false;
    }
    return username.equals(appUser.username);
  }

  @Override
  public int hashCode() {
    int result = email.hashCode();
    result = 31 * result + username.hashCode();
    return result;
  }
}
