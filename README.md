# 🧮 Queue Management Simulation

## 📘 Overview
This Java Swing application simulates a queuing system with multiple service queues, where **N clients** arrive, wait their turn, get served, and then leave. The simulation supports multi-threaded queue processing using the `Server` class, each running on its own thread and managed via a `BlockingQueue`. Results include **average waiting time**, **average service time**, and **peak hour** of maximum load.

---

## 🔧 Features

- **🏃 Multithreaded Servers**
  - Each service queue runs within its own `Thread`, ensuring realistic, concurrent client handling.
  - Threads continuously process tasks in parallel until service completes or the simulation ends.

- **📋 Task Scheduling**
  - New clients are dispatched based on either **shortest queue** or **shortest waiting time**, according to the selected `SelectionPolicy`.
  - `Scheduler` balances load across available servers.

- **📊 Metrics Generation**
  - **Peak Hour**: Tracks the simulation time with the highest number of in-queue tasks.
  - **Average Waiting Time**: Time each client spends waiting before service.
  - **Average Service Time**: Actual time taken to complete each service task.

---

## 🧠 How It Works

1. **Client Generation**  
   - N clients are generated with random **arrival** and **service** times within user-defined ranges.
   - Tasks are sorted by arrival time before dispatch.

2. **Simulation Loop**  
   - Runs up to `simInterval` seconds or stops early if all queues and waiting clients are clear.
   - Each second:
     - Dispatch incoming clients to queues.
     - Queue threads concurrently handle servicing using `BlockingQueue`.
     - Records current queue states, waiting lists, and updates metrics (peak hour).

3. **Task Handling**  
   - When assigned, a `Server.addTask(task)` records the client’s queue entry time.
   - In the server’s `run()` method (separate thread), tasks are serviced one time-unit per cycle.
   - Once service completes, the task is logged into `servedTasks` for final metrics.

4. **Results Output**  
   - The GUI “Simulation Output” and `SimulationOutput.txt` file show:
     - Time logs per tick
     - Final **Average Service Time**
     - Final **Average Waiting Time**
     - **Peak Hour**

---

## 🧩 Getting Started

1. **Import into IDE**
   - Clone the repo and import as a **Java project** in IntelliJ IDEA or Eclipse.

2. **Dependencies**
   - Uses standard Java JDK (no external libraries).
   - Includes Swing (`javax.swing`) for the GUI.

3. **Run the Simulation**
   - Launch `SimulationFrame` from IDE.
   - Input:
     - Number of clients & queues
     - Simulation time (seconds)
     - Min/max arrival & service times
     - Selection strategy (Shortest Queue or Shortest Waiting Time)
   - Click **Start Simulation** to run.

4. **View Results**
   - Real-time logs appear in the GUI output window.
   - Final metrics are saved in `SimulationOutput.txt` in the project root.

---

## ☕ Multithreading Details

- Each `Server` object implements `Runnable`, with its own internal queue and processing thread.
- Concurrency is managed by `BlockingQueue`, ensuring thread-safe task enqueuing/dequeuing.
- The GUI/main thread dispatches tasks in real time, while server threads work independently, simulating real-world simultaneous service points.
- Each server thread uses `Thread.sleep()` in its loop to simulate 1-second service intervals.

---

## 🗂️ Project Structure

