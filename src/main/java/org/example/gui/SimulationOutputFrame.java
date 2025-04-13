package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class SimulationOutputFrame extends JFrame {
    private JTextArea outputTextArea;

    public SimulationOutputFrame(String initialOutput) {
        setTitle("Simulation Output");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        outputTextArea = new JTextArea();
        outputTextArea.setText(initialOutput);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateOutput(String output) {
        outputTextArea.append(output + "\n");
        outputTextArea.setCaretPosition(outputTextArea.getDocument().getLength());  // Scroll to the bottom
    }
}
