package task;

import org.springframework.web.bind.annotation.*;

import java.util.*;


public class Controller {
    private final Map<Long, Long> secret_numbers = Map.of(
            1L, 214071234072L,
            2L, 234923493247L,
            3L, 949493939949L,
            4L, 383832238472L,
            5L, 222993947872L
    );

    public Map<String, Long> getNumberById(long id) {
        long result = -1;

        if (secret_numbers.containsKey(id)) {
            result = secret_numbers.get(id);
        }

        return Map.of("secret_number", result);
    }

}
