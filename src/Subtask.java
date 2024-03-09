public class Subtask extends Task {
    private Epic epic;

    public Subtask(int id, String name, String description, Status status, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}