package basis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Task {
    protected String name;
    protected int id;
    protected String description;
    protected Status status;
    protected Duration duration; //field duration
    protected LocalDateTime startTime; // field startTime

    public Duration getDuration() { // getter
        return duration;
    }

    public void setDuration(Duration duration) { // setter
        this.duration = Duration.ofMinutes(duration.toMinutes());
    }

    public LocalDateTime getStartTime() { // getter
        return startTime;
    }

    public void setStartTime(LocalDateTime startime) { //setter
        this.startTime = startime;
    }

    public Task(String name, String description, Status aNew, Duration duration, LocalDateTime now) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task" +
                "name ='" + name + '\'' +
                ", id = " + id +
                ", description ='" + description + '\'' +
                ", status = " + status +
                "";
    }
}