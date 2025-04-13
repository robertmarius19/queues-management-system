package org.example.gui;

import org.example.bussinesLogic.SelectionPolicy;
import org.example.bussinesLogic.SimulationManager;
import org.example.model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationFrame extends JFrame {
    private JTextField numClientsField;
    private JTextField numQueuesField;
    private JTextField simIntervalField;
    private JTextField minArrivalField;
    private JTextField maxArrivalField;
    private JTextField minServiceField;
    private JTextField maxServiceField;
    private JComboBox<SelectionPolicy> strategyComboBox;
    private JButton startButton;
    private SimulationManager simulationManager;

    public SimulationFrame() {
        // Frame setup
        setTitle("Queue Management Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2)); // 9 rows, 2 columns

        inputPanel.add(new JLabel("Number of Clients:"));
        numClientsField = new JTextField("");
        inputPanel.add(numClientsField);

        inputPanel.add(new JLabel("Number of Queues:"));
        numQueuesField = new JTextField("");
        inputPanel.add(numQueuesField);

        inputPanel.add(new JLabel("Simulation Time:"));
        simIntervalField = new JTextField("");
        inputPanel.add(simIntervalField);

        inputPanel.add(new JLabel("Min Arrival Time:"));
        minArrivalField = new JTextField("");
        inputPanel.add(minArrivalField);

        inputPanel.add(new JLabel("Max Arrival Time:"));
        maxArrivalField = new JTextField("");
        inputPanel.add(maxArrivalField);

        inputPanel.add(new JLabel("Min Service Time:"));
        minServiceField = new JTextField("");
        inputPanel.add(minServiceField);

        inputPanel.add(new JLabel("Max Service Time:"));
        maxServiceField = new JTextField("");
        inputPanel.add(maxServiceField);

        inputPanel.add(new JLabel("Strategy:"));
        strategyComboBox = new JComboBox<>(SelectionPolicy.values());
        inputPanel.add(strategyComboBox);


        inputPanel.add(new JLabel());
        startButton = new JButton("Start Simulation");
        inputPanel.add(startButton);

        add(inputPanel, BorderLayout.CENTER);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
    }

    private void startSimulation() {
        // Getting the input values
        int numClients = Integer.parseInt(numClientsField.getText());
        int numQueues = Integer.parseInt(numQueuesField.getText());
        int simInterval = Integer.parseInt(simIntervalField.getText());
        int minArrival = Integer.parseInt(minArrivalField.getText());
        int maxArrival = Integer.parseInt(maxArrivalField.getText());
        int minService = Integer.parseInt(minServiceField.getText());
        int maxService = Integer.parseInt(maxServiceField.getText());
        SelectionPolicy selectedPolicy = (SelectionPolicy) strategyComboBox.getSelectedItem();

        SimulationManager simulationManager = new SimulationManager(this, numClients, numQueues, simInterval, minArrival, maxArrival, minService, maxService, selectedPolicy);
        Thread simulationThread = new Thread(simulationManager);
        simulationThread.start();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimulationFrame().setVisible(true);
            }
        });
    }
}
