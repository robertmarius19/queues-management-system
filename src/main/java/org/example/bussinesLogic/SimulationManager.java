package org.example.bussinesLogic;

import org.example.gui.SimulationFrame;
import org.example.gui.SimulationOutputFrame;
import org.example.model.Server;
import org.example.model.Task;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class SimulationManager implements Runnable {
    private Scheduler scheduler;
    private SimulationFrame view;
    private ArrayList<Task> tasks = new ArrayList<>();
    private SimulationOutputFrame outputFrame;
    private int numClients;
    private int numQueues;
    private int simInterval;
    private int minArrival;
    private int maxArrival;
    private int minService;
    private int maxService;
    private double averageWaitingTime;
    private double averageServiceTime;
    private int peakHour = 0;
    private int maxTasksInQueues = 0;
    private PrintWriter writer;
    private List<Server> servers;
    private SelectionPolicy selectionPolicy;

    public SimulationManager(SimulationFrame view, int numClients, int numQueues, int simInterval,
                             int minArrival, int maxArrival, int minService, int maxService,
                             SelectionPolicy selectionPolicy) {
        this.view = view;
        this.numClients = numClients;
        this.numQueues = numQueues;
        this.simInterval = simInterval;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.selectionPolicy = selectionPolicy;

        this.scheduler = new Scheduler(numQueues, numClients);
        scheduler.changeStrategy(selectionPolicy);

        try {
            writer = new PrintWriter("SimulationOutput.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Thread thread : scheduler.threads) {
            thread.start();
        }

        generateTasks();
        servers = scheduler.getServers();

        outputFrame = new SimulationOutputFrame("Simulation Output:\n");
        outputFrame.setVisible(true);
    }

    private void generateTasks() {
        Random random = new Random();
        for (int i = 0; i < numClients; i++) {
            int serviceTime = random.nextInt(maxService - minService + 1) + minService;
            int arrivalTime = random.nextInt(maxArrival - minArrival + 1) + minArrival;
            Task task = new Task(i + 1, arrivalTime, serviceTime);
            tasks.add(task);
        }
        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }


    @Override
    public void run() {
        int currentTime = 0;
        boolean simulationEnded = false;

        while (currentTime < simInterval) {
            // Dispatch tasks to the scheduler
            List<Task> toDispatch = new ArrayList<>();
            for (Task task : new ArrayList<>(tasks)) {
                if (task.getArrivalTime() == currentTime) {
                    toDispatch.add(task);
                }
            }

            for (Task task : toDispatch) {
                tasks.remove(task);
                scheduler.dispatchTask(task);
            }


            displayQueue(currentTime);
            trackPeakHour(currentTime);



            if (shouldEndSimulation()) {
                if (!simulationEnded) {
                    simulationEnded = true;
                    System.out.println("Simulation ended early at time " + currentTime);
                }
            } else {
                simulationEnded = false;
            }


            if (simulationEnded) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Simulation interrupted: " + e.getMessage());
            }

            currentTime++;
        }

        calculateAverages();
        displayQueue(currentTime);
        outputFrame.updateOutput("Simulation completed.\nAverage Service Time: " + averageServiceTime +
                "\nAverage Waiting Time: " + averageWaitingTime +
                "\nPeak Hour: " + peakHour);
        writer.println("\nSimulation completed.");
        writer.println("Average Service Time: " + averageServiceTime);
        writer.println("Average Waiting Time: " + averageWaitingTime);
        writer.println("Peak Hour: " + peakHour);
        writer.close();
    }

    private boolean shouldEndSimulation() {
        if (!tasks.isEmpty()) {
            return false;
        }

        for (Server server : servers) {
            if (!server.getTasks().isEmpty()) {
                return false;
            }
        }

        return true;
    }


    private void displayQueue(int currentTime) {
        StringBuilder output = new StringBuilder();
        output.append("Time ").append(currentTime).append("\n");

        StringBuilder waitingClients = getWaitingList(currentTime);
        if (!waitingClients.isEmpty()) {
            output.append("Waiting clients: ").append(waitingClients).append("\n");
        }

        int queueNo = 1;
        for (Server server : servers) {
            output.append("Queue ").append(queueNo).append(": ");
            BlockingQueue<Task> serverQueue = server.getTasks();

            boolean hasActiveTasks = serverQueue.stream().anyMatch(task -> task.getRemainingServiceTime() > 0);

            if (serverQueue.isEmpty() || !hasActiveTasks) {
                output.append("closed\n");
            } else {
                output.append("[ ");
                for (Task task : serverQueue) {
                    if (task.getRemainingServiceTime() > 0) {
                        output.append("( ").append(task.getId()).append(", ")
                                .append(task.getArrivalTime()).append(", ")
                                .append(task.getRemainingServiceTime()).append(" ) ");
                    }
                }
                output.append("]\n");
            }
            queueNo++;
        }

        SwingUtilities.invokeLater(() -> outputFrame.updateOutput(output.toString()));
        writer.println(output.toString());
        writer.flush();
    }

    private StringBuilder getWaitingList(int currentTime) {
        StringBuilder waitingList = new StringBuilder();
        for (Task task : tasks) {
            if (task.getArrivalTime() > currentTime) {
                waitingList.append("( ").append(task.getId()).append(", ")
                        .append(task.getArrivalTime()).append(", ")
                        .append(task.getServiceTime()).append(" ) ");
            }
        }
        return waitingList;
    }

    private void calculateAverages() {
        int totalWaitingTime = 0;
        int totalServiceTime = 0;
        int totalTasks =0;

        for (Server server : servers) {
            List<Task> servedTasks = server.getServedTasks();
            for (Task task : servedTasks) {
                totalWaitingTime += task.getWaitingTime();
                totalServiceTime += task.getOriginalServiceTime();
                System.out.println(task.getWaitingTime()+"\n");
                totalTasks++;
            }
        }

        if (totalTasks > 0) {
            averageWaitingTime = (double) totalWaitingTime / totalTasks;
            averageServiceTime = (double) totalServiceTime / totalTasks;
        }
    }

    private void trackPeakHour(int currentTime) {
        int currentTasks = 0;
        for (Server server : servers) {
            currentTasks += server.getTasks().size();
        }

        if (currentTasks > maxTasksInQueues) {
            maxTasksInQueues = currentTasks;
            peakHour = currentTime;
        }
    }
}
