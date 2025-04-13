package org.example.bussinesLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = servers.get(0);
        int min=Integer.MAX_VALUE;
        for(Server s: servers) {
            if(s.getWaitingPeriod()<min)
            {
                min=s.getWaitingPeriod();
                server=s;
            }
        }
        task.setWaitingTime(server.calculateWaitingTime());
        server.addTask(task);
    }
}
