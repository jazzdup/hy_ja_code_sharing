import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;

import org.hyperskill.hstest.testcase.CheckResult;
import task.Main;

import java.util.*;

import static org.hyperskill.hstest.testcase.CheckResult.wrong;
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

  List<Integer> studentIds = new ArrayList<>();
  List<String> studentNames = new ArrayList<>();

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[]{
          this::testGetStudents,

          () -> testGetStudent(studentIds.get(0), studentNames.get(0)),
          () -> testGetStudent(studentIds.get(1), studentNames.get(1)),
          () -> testGetStudent(studentIds.get(2), studentNames.get(2)),
  };

  CheckResult testGetStudents() {
    HttpResponse response = get("/students").send();

    throwIfIncorrectStatusCode(response, 200);

    var item = isObject()
            .value("id", isInteger(id -> studentIds.add(id)))
            .value("name", isString(name -> {
              studentNames.add(name);
              return name.length() > 0;
            }));

    expect(response.getContent()).asJson().check(
            isArray()
                    .item(item)
                    .item(item)
                    .item(item)
    );

    if (studentIds.stream().distinct().count() != 3) {
      return wrong("Duplicate id detected.");
    }


    return correct();
  }

  CheckResult testGetStudent(int id, String name) {
    HttpResponse response = get("/students/" + id).send();

    throwIfIncorrectStatusCode(response, 200);

    expect(response.getContent()).asJson().check(
            isObject()
                    .value("id", id)
                    .value("name", name));

    return correct();
  }
}