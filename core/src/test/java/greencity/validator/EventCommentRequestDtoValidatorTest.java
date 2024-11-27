package greencity.validator;


import greencity.dto.eventcomment.AddEventCommentDtoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class EventCommentRequestDtoValidatorTest {
    private static EventCommentsValidator validator;

    @BeforeAll
    public static void setUp() {
        String pathToTestFile = "bannedwords/bannedWords.txt";
        validator = new EventCommentsValidator(pathToTestFile);
    }

    @Test
    void testSlug() {
        AddEventCommentDtoRequest request = new AddEventCommentDtoRequest();
        request.setComment("Some smut comment, faggot");
        Assertions.assertFalse(validator.isValid(request, null));
    }
    @Test
    void testValidComment() {
        AddEventCommentDtoRequest request = new AddEventCommentDtoRequest();
        request.setComment("Some valid comment");
        Assertions.assertTrue(validator.isValid(request, null));
    }
}

