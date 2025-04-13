package org.example.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int serviceTime;

    private int nrClients;
    private List<Task> servedTasks;  // List to store served tasks

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.serviceTime = 0;
        this.nrClients = 0;
        this.servedTasks = new ArrayList<>(); // Initialize servedTasks list
    }

    public Server(BlockingQueue<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
        this.serviceTime = 0;
        this.nrClients = 0;
        this.servedTasks = new ArrayList<>(); // Initialize servedTasks list
    }

    public void addTask(Task task) {
        tasks.add(task);
        this.waitingPeriod.addAndGet(task.getServiceTime());  // Add task's service time to waiting period
        this.serviceTime += task.getServiceTime();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = tasks.peek();  // Look at the task at the front of the queue
                if (task != null && task.getRemainingServiceTime() > 0) {
                    task.decreaseServiceTime();  // Decrease the service time


                    if (task.getRemainingServiceTime() <= 0) {
                        tasks.poll();  // Remove completed task from the queue
                        this.nrClients++;
                        servedTasks.add(task);  // Add task to the servedTasks list after completion
                    }
                    Thread.sleep(1000);
                    waitingPeriod.addAndGet(-1);  // Decrease waiting period for processing
                } else {
                    Thread.sleep(500);  // Idle if no tasks
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error processing task: " + e.getMessage());
            }
        }
    }


    // Method to retrieve the list of served tasks
    public List<Task> getServedTasks() {
        return servedTasks;
    }

    public int countTasks() {
        return tasks.size();
    }

    public int getServiceTime() {
        return this.serviceTime;
    }

    public int getWaitingPeriod() {
        return this.waitingPeriod.get();
    }

    public BlockingQueue<Task> getTasks() {
        return this.tasks;
    }
    public int calculateWaitingTime(){
        int time=0;
        for(Task task: tasks){
            time += task.getRemainingServiceTime();
        }
        return time;
    }
    public int getNrClients() {
        return this.nrClients;
    }
}
