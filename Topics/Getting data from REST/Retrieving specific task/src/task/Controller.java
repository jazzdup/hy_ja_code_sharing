package task;

import org.springframework.web.bind.annotation.*;

import java.util.*;

class Task {
    private final int id;
    private final String name;
    private final String description;

    Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

@RestController
public class Controller {
    List<Task> tasks = List.of(
            new Task(405, "Improve UI", "implement ..."),
            new Task(406, "Color bug", "fix ...")
    );

    Task defaultTask = new Task(0, null, null);

    // Add your code below this line and do not change the code above the line.
    @GetMapping("/task/{id}")
    public Task getTask(@PathVariable String id){
        for (Task task : tasks) {
            if (Integer.parseInt(id) == task.getId()){
                return task;
            }
        }
        return defaultTask;
    }
}
