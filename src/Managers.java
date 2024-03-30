public class Managers {
    public  static HistoryManager getDefaultHistory () {
        return new InMemoryHistoryManager();
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
        //здесь добавила этот метод
    }
}
