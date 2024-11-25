package greencity.validator;

import greencity.annotations.ValidAddEventCommentDtoRequest;
import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;


public class EventCommentsValidator implements ConstraintValidator<ValidAddEventCommentDtoRequest, AddEventCommentDtoRequest> {
    private final Set<String> bannedWords;

    public EventCommentsValidator(@Value("${slug.filter.file.en}") String pathToFile) {
        this.bannedWords = loadBannedWords(pathToFile);
    }

    @Override
    public boolean isValid(AddEventCommentDtoRequest addEventCommentDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        return bannedWords.stream()
                .noneMatch(addEventCommentDtoRequest.getComment().toLowerCase()::contains);
    }

    private Set<String> loadBannedWords(String pathToFile) {
        try {
            ClassPathResource resource = new ClassPathResource(pathToFile);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                return reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toSet());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load banned words from file: " + pathToFile, e);
        }
    }
}

