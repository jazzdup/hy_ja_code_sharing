import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;

import org.hyperskill.hstest.testcase.CheckResult;
import task.Main;

import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testcase.CheckResult.correct;


public class Tests extends SpringTest {

    public Tests() {
        super(Main.class);
    }

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
            () -> testGetTask(405, "Improve UI", "implement ..."),
            () -> testGetTask(406, "Color bug", "fix ..."),
            () -> testGetDefaultTask(3),
            () -> testGetDefaultTask(0),
            () -> testGetDefaultTask(999)
    };

    CheckResult testGetTask(int id, String name, String description) {
        HttpResponse response = get("/task/" + id).send();

        throwIfIncorrectStatusCode(response, 200);

        expect(response.getContent()).asJson().check(
                isObject()
                        .value("id", id)
                        .value("name", isString(name))
                        .value("description", isString(description))
        );

        return correct();
    }

    CheckResult testGetDefaultTask(int id) {
        HttpResponse response = get("/task/" + id).send();

        throwIfIncorrectStatusCode(response, 200);

        expect(response.getContent()).asJson().check(
                isObject()
                        .value("id", 0)
                        .value("name", isNull())
                        .value("description", isNull())
        );

        return correct();
    }
}