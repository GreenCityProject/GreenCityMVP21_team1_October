package greencity.validator;

import greencity.annotations.NoProfanity;
import greencity.constant.ErrorMessage;
import greencity.exception.exceptions.InvalidCommentException;
import greencity.exception.exceptions.InvalidPathException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;

public class NoProfanityValidator implements ConstraintValidator<NoProfanity, String> {
    private static Set<String> bannedWords;
    @Value("${slug.filter.file.en}")
    private String pathToFile;

    @Override
    public void initialize(NoProfanity constraintAnnotation) {
        if (bannedWords == null) {
            try {
                bannedWords = loadBannedWords();
            } catch (IOException e) {
                throw new InvalidPathException(ErrorMessage.INVALID_FILE_PATH);
            }
        }
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = bannedWords.stream()
                .noneMatch(string.toLowerCase()::contains);
        if (isValid) {
            return true;
        } else throw new InvalidCommentException(ErrorMessage.COMMENT_CONTAINS_PROFANITY);
    }

    private Set<String> loadBannedWords() throws IOException {
        ClassPathResource resource = new ClassPathResource(pathToFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());
        }
    }
}
