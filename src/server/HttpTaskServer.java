package server;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import handler.*;
import managers.Managers;
import managers.TaskManager;

import java.net.http.*;
import java.io.IOException;
import java.net.InetSocketAddress;
public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager manager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    public void startServer () throws IOException{
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));

        httpServer.start();
        System.out.println("На старт! \nPORT: " + PORT);
    }
    public void stopServer(){
        httpServer.stop(1);
        System.out.println("Стоп!");
    }
    public static void main(String[] args) throws IOException{
        HttpTaskServer httpsServer1 = new HttpTaskServer(Managers.getDefault());
        httpsServer1.startServer();
        httpsServer1.stopServer();
    }
}
