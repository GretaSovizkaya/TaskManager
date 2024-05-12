package managers;
import basis.*;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> taskNodes = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            Node node = taskNodes.get(task.getId());
            if (node != null) {
                removeNode(node);
            }
            node = new Node(task);
            linkLast(task);
            taskNodes.put(task.getId(), node);
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.getTask());
            current = current.getNext();
        }
        return tasks;
    }

    @Override
    public void remove(int id) {
        Node node = taskNodes.get(id);
        if (node != null) {
            removeNode(node);
            taskNodes.remove(id);
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        taskNodes.put(task.getId(), tail);

    }

    private void removeNode(Node node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
        taskNodes.remove(node.getTask().getId());
    }
}