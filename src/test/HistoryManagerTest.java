package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import managers.Managers;
class HistoryManagerTest { //добавила тесты
//надеюсь я верно поняла:))
    @Test
    public void addNewHistoryManager () {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    public void addNewTaskManager () {
        assertNotNull(Managers.getDefault());
    }

}