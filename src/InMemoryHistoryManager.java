import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>(10);

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