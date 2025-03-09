package basis;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    @SerializedName("epic_duration")
    private Duration duration;

    @SerializedName("epic_startTime")
    private LocalDateTime startTime;

    @SerializedName("epic_endTime")
    private LocalDateTime endTime;

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(duration, epic.duration) &&
                Objects.equals(startTime, epic.startTime) &&
                Objects.equals(endTime, epic.endTime) &&
                Objects.equals(subtasks, epic.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), duration, startTime, endTime, subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", subtasks=" + subtasks +
                "} " + super.toString();
    }
}
