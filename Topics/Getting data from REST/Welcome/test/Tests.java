import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;

import org.hyperskill.hstest.testcase.CheckResult;
import task.Main;

import static org.hyperskill.hstest.testcase.CheckResult.correct;
import static org.hyperskill.hstest.testcase.CheckResult.wrong;


public class Tests extends SpringTest {

    public Tests() {
        super(Main.class);
    }

    static final String WELCOME_MSG = "Welcome!";
    static final String API_WELCOME = "/welcome";

    static void throwIfIncorrectStatusCode(HttpResponse response, int status) {
        if (response.getStatusCode() != status) {
            throw new WrongAnswer(response.getRequest().getMethod() +
                    " " + response.getRequest().getLocalUri() +
                    " should respond with status code " + status +
                    ", responded: " + response.getStatusCode() + "\n\n" +
                    "Response body:\n" + response.getContent());
        }
    }

    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
            this::testGetWelcome
    };

    CheckResult testGetWelcome() {
        HttpResponse response = get(API_WELCOME).send();

        throwIfIncorrectStatusCode(response, 200);

        return WELCOME_MSG.equals(response.getContent()) ? correct() :
                wrong("Expected string: \"" + WELCOME_MSG + "\", received: \"" + response.getContent() + "\".");
    }

}