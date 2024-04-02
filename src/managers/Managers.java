package managers;
// видимо при запушивании произошла ошибка и классы продублировались, поэтому отправляю Вам архив
public class Managers {
    public  static HistoryManager getDefaultHistory () {
        return new InMemoryHistoryManager();
    }
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
        //здесь добавила этот метод
    }
}
