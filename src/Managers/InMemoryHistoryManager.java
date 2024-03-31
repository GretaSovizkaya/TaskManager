package Managers;
import Basis.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>(10); //добавила final

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() >= 10) {
                history.remove(0);
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(this.history);
    }
}