package org.example.bussinesLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task) {
        int min=Integer.MAX_VALUE;
        Server server1=new Server();
            for(Server server : servers) {
                if(server.countTasks()<min) {
                    min = server.countTasks();
                    server1=server;
                }
            }
        task.setWaitingTime(server1.calculateWaitingTime());
           server1.addTask(task);
    }
}
