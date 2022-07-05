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
          this::testGetColors
  };

  CheckResult testGetColors() {
    HttpResponse response = get("/colors").send();

    throwIfIncorrectStatusCode(response, 200);

    expect(response.getContent()).asJson().check(
            isObject()
                    .value("colors", isArray()
                            .item(isObject()
                                    .value("color", isString("black"))
                                    .value("category", isString("hue"))
                                    .value("type", isString("primary"))
                                    .value("code", isObject()
                                            .value("rgba", isArray()
                                                    .item(0)
                                                    .item(0)
                                                    .item(0)
                                                    .item(1))
                                            .value("hex", "#000")))

                            .item(isObject()
                                    .value("color", isString("white"))
                                    .value("category", isString("value"))
                                    .value("type", isString("primary"))
                                    .value("code", isObject()
                                            .value("rgba", isArray()
                                                    .item(255)
                                                    .item(255)
                                                    .item(255)
                                                    .item(1))
                                            .value("hex", "#FFF")))
                    ));

    return correct();
  }
}