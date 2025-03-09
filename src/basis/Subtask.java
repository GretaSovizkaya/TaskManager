package basis;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    } //исправила

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Subtask epic) {
        this.epic = epic.getEpic();
    }
}