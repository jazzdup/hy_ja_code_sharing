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

  final Long[][] SECRET_NUMS = new Long[][]{
          {1L, 214071234072L},
          {2L, 234923493247L},
          {3L, 949493939949L},
          {4L, 383832238472L},
          {5L, 222993947872L}
  };


  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[]{
          () -> testGetSecretNumber(SECRET_NUMS[0][0], SECRET_NUMS[0][1]),
          () -> testGetSecretNumber(SECRET_NUMS[1][0], SECRET_NUMS[1][1]),
          () -> testGetSecretNumber(SECRET_NUMS[2][0], SECRET_NUMS[2][1]),
          () -> testGetSecretNumber(SECRET_NUMS[3][0], SECRET_NUMS[3][1]),
          () -> testGetSecretNumber(SECRET_NUMS[4][0], SECRET_NUMS[4][1]),

          () -> testGetSecretNumber(9, -1),
          () -> testGetSecretNumber(100, -1),
          () -> testGetSecretNumber(101, -1),
  };

  CheckResult testGetSecretNumber(long id, long number) {
    HttpResponse response = get("/secret_numbers/" + id).send();

    throwIfIncorrectStatusCode(response, 200);

    expect(response.getContent()).asJson().check(
            isObject()
                    .value("secret_number", number)
    );

    return correct();
  }

}