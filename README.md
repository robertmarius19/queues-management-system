Queue Management Simulation 🕒
A Java multithreaded simulation that models N clients arriving, queuing across Q service threads, being served, and exiting. It calculates:

Average Waiting Time – how long each client waits before being served

Average Service Time – the actual time taken to serve each client

Peak Hour – timestamp when the system has the most queued tasks

🧩 Features
Multi-threaded servers processing from BlockingQueue<Task>

Configurable via GUI: number of clients, queues, arrival/service ranges, simulation time, and scheduling policy

Early termination when there are no pending or in-service tasks

Output both in GUI window and SimulationOutput.txt

🚀 How to Run in an IDE
Pre-requisites:
Java 11+ JDK installed

Preferably IntelliJ IDEA (Community/Ultimate) or Eclipse

IntelliJ IDEA:
Clone the repository

bash
Copy
Edit
git clone https://...your-repo.git  
Open in IntelliJ
File → Open... → choose project root

Set SDK
File → Project Structure → Project → set Project SDK to Java 11+

Build project
Build → Build Project

Run main class
Run → Edit Configurations → + → “Application” → Main class: org.example.gui.SimulationFrame
Click “Run” (▶)

Eclipse:
Import project
File → Import → “Existing Maven Projects” (or simply a Java project)

Set Java compiler
Project → Properties → Java Compiler → enable “Use compliance from execution environment: JavaSE‑11”

Run main
Right-click SimulationFrame.java → Run As → Java Application

🖥️ Usage
Launch the app; it opens a window with fields:

Number of Clients – total tasks

Number of Queues – number of servers

Simulation Time – max seconds to simulate

Arrival Time Range – min/max arrival moments

Service Time Range – min/max processing time

Strategy – e.g. shortest queue

Click Start Simulation

A separate output window appears showing real-time queue states

Console + file SimulationOutput.txt will show final stats:

sql
Copy
Edit
Simulation completed.
Average Service Time: X.XX
Average Waiting Time: Y.YY
Peak Hour: Z
📂 Project Structure
pgsql
Copy
Edit
src/main/java/
├── org.example.gui
│   ├── SimulationFrame.java      ← GUI input form
│   └── SimulationOutputFrame.java ← Real-time text output
├── org.example.bussinesLogic
│   └── SimulationManager.java    ← Orchestrates arrivals and calculations
├── org.example.model
│   ├── Task.java                ← Represents client job + arrival/time tracking
│   └── Server.java              ← Runnable server managing queue + processing
└── org.example.bussinesLogic
    └── Scheduler.java          ← Dispatch logic based on policy
📊 Metrics
Metric	How it’s calculated
Waiting Time	(service start time) − (arrival time)
Service Time	The original task service duration
Peak Hour	Simulation second when total queued tasks was highest

🧩 Customize and Extend
Scheduling strategies: Add more policies to Scheduler

Visual output: Enhance SimulationOutputFrame (charts, tables)

Logging/export: Allow CSV/JSON output via SimulationManager
