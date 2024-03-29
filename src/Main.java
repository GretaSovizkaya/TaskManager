
public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        InMemoryTaskManager manager = new InMemoryTaskManager();


        Task task1 = new Task("Задача1", "описание задачи1");
        Task task2 = new Task("Задача2", "описание задачи2");

        Epic epic1 = new Epic(3, "Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic(4, "Эпик 2", "Описание эпика 2");

        Subtask subtask1 = new Subtask(5, "Подзадача 1", "Описание подзадачи 1",epic1);
        Subtask subtask2 = new Subtask(6, "Подзадача 2", "Описание подзадачи 2",epic1);
        Subtask subtask3 = new Subtask(7, "Подзадача эпика 2", "Описание подзадачи", epic2);

        manager.createNewTask(task1);
        manager.createNewTask(task2);

        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);


        manager.addSubtaskToEpic(subtask1, epic1);
        manager.addSubtaskToEpic(subtask2, epic1);
        manager.addSubtaskToEpic(subtask3, epic2);

        task2.setStatus(Status.IN_PROGRESS);
        epic2.setStatus(Status.DONE);

        manager.removeTaskById(1);
        manager.removeTaskById(3);


        for (Task task : manager.getAllTypesTasks()) {
            System.out.println("ID: " + task.getId() + ", Название: " + task.getName() + ", Описание: "
                    + task.getDescription() + ", Статус: " + task.getStatus());
        }


        System.out.println("Подзадачи для эпика 1:");
        for (Subtask subtask : manager.getSubtasksOfEpic(3)) {
            System.out.println("ID: " + subtask.getId() + ", Название: " + subtask.getName() + ", Описание: "
                    + subtask.getDescription() + ", Статус: " + subtask.getStatus());
        }
        subtask2.setStatus(Status.IN_PROGRESS); // Изменяем статус подзадачи
        manager.updateEpicStatus(epic1);

        System.out.println("Статус эпика 1 после изменения статуса подзадачи:");
        System.out.println("Статус: " + epic1.getStatus());

    }

}