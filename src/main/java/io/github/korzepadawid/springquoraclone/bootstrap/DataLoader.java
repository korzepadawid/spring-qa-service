package io.github.korzepadawid.springquoraclone.bootstrap;

import io.github.korzepadawid.springquoraclone.model.AppUser;
import io.github.korzepadawid.springquoraclone.model.Question;
import io.github.korzepadawid.springquoraclone.repository.AppUserRepository;
import io.github.korzepadawid.springquoraclone.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

  private final AppUserRepository appUserRepository;
  private final QuestionRepository questionRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    AppUser bmurray = appUserRepository.save(AppUser.builder()
        .username("bmurray")
        .email("bmurray@mail.com")
        .password(passwordEncoder.encode("pass"))
        .build());

    AppUser jdoe = appUserRepository.save(AppUser.builder()
        .username("jdoe")
        .email("jdoe@mail.com")
        .password(passwordEncoder.encode("pass"))
        .build());

    questionRepository.save(Question.builder()
        .author(bmurray)
        .anonymous(false)
        .title("What are some books that will expand our mind?")
        .description("Please, share your list.")
        .build());

    questionRepository.save(Question.builder()
        .author(bmurray)
        .anonymous(true)
        .title("What are the most common mistakes first time entrepreneurs make?")
        .description("Please, be honest.")
        .build());

    questionRepository.save(Question.builder()
        .author(jdoe)
        .anonymous(false)
        .title("What is the most beautiful place on the earth?")
        .description("Please, share your traveling destinations.")
        .build());
  }
}
