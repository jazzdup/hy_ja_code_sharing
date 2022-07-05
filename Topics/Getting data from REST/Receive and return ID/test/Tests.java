import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;

import org.hyperskill.hstest.testcase.CheckResult;
import task.Main;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hyperskill.hstest.testcase.CheckResult.wrong;
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

  final Random rand = new Random();

  String getRandIntAsStr() {
    return Integer.toString(rand.nextInt());
  }

  String getRandString() {
    int numberOfLetters = 26;
    int firstSmallLetter = 97;
    int strLen = rand.nextInt(5) + 5;

    return IntStream
            .generate(() -> rand.nextInt(numberOfLetters) + firstSmallLetter)
            .limit(strLen)
            .mapToObj(Character::toString)
            .collect(Collectors.joining(""));
  }

  @DynamicTest
  DynamicTesting[] dt = new DynamicTesting[]{
          () -> testGet(getRandIntAsStr()),
          () -> testGet(getRandIntAsStr()),
          () -> testGet(getRandString()),
          () -> testGet(getRandString()),
  };

  CheckResult testGet(String arg) {
    HttpResponse response = get("/" + arg).send();

    throwIfIncorrectStatusCode(response, 200);

    if (arg.equals(response.getContent())) {
      return correct();
    }

    return wrong("Expected: \"" + arg + "\", received: \"" + response.getContent() + "\"");
  }
}