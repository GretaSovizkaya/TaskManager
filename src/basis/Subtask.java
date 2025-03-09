package basis;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description, Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Subtask epic) {
        this.epic = epic.getEpic();
    }
}