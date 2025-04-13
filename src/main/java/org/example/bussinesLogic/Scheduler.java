package org.example.bussinesLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;
    public ArrayList<Thread> threads;
    private int numQueues;
    private int numClients;

    // Constructor now takes both numQueues and numClients
    public Scheduler(int numQueues, int numClients) {
        this.numQueues = numQueues;
        this.numClients = numClients;
        this.servers = new ArrayList<>();
        this.threads = new ArrayList<>();
        for (int i = 0; i < numQueues; i++) {
            Server server = new Server();
            this.servers.add(server);
            Thread thread = new Thread(server);
            this.threads.add(thread);
        }
    }


    public void changeStrategy(SelectionPolicy policy) {
        switch (policy) {
            case SHORTEST_QUEUE:
                strategy = new ShortestQueueStrategy();
                break;
            case SHORTEST_TIME:
                strategy = new TimeStrategy();
                break;
            default:
                throw new IllegalArgumentException("Unsupported selection policy: " + policy);
        }
    }

    public void dispatchTask(Task task) {
        if (strategy != null) {
            strategy.addTask(servers, task);
        } else {
            System.err.println("Strategy not set. Please set the strategy before dispatching tasks.");
        }
    }

    public List<Server>
    getServers() {
        return servers;
    }
}

