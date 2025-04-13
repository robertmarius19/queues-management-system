package org.example.model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime;
    private int entryQueue;
    private int remainingServiceTime;


    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime;
        this.waitingTime = 0;
        this.entryQueue = 0;
    }
    public void decreaseServiceTime() {
        if (remainingServiceTime > 0) {
            remainingServiceTime--;
        }
    }

    public int getId() {
        return this.id;
    }

    public int getArrivalTime() {
        return this.arrivalTime;
    }

    public int getServiceTime() {
        return this.serviceTime;
    }

    public int getRemainingServiceTime() {
        return this.remainingServiceTime;
    }

    public boolean isCompleted() {
        return remainingServiceTime <= 0;
    }


    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public int getWaitingTime() {
        return this.waitingTime;
    }

    public int getOriginalServiceTime() {
        return this.serviceTime;
    }

    @Override
    public String toString() {
        return "( " + id + ", " + arrivalTime + ", " + remainingServiceTime + " )";
    }
}
