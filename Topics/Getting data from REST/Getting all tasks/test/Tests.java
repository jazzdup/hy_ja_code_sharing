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
            this::testGetTasksAll
    };

    CheckResult testGetTasksAll() {
        HttpResponse response = get("/tasks/all").send();

        throwIfIncorrectStatusCode(response, 200);

        expect(response.getContent()).asJson().check(
                isArray()
                        .item(isObject()
                                .value("name", isString("Improve UI"))
                                .value("description", isString("implement ...")))
                        .item(isObject()
                                .value("name", isString("Color bug"))
                                .value("description", isString("fix ...")))
        );

        return correct();
    }
}