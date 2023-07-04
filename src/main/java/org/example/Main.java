package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InverseKinematicsGUI extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private JLabel xPosLabel;
    private JLabel yPosLabel;
    private JLabel theta1Label;
    private JLabel theta2Label;

    private JTextField xPosTextField;
    private JTextField yPosTextField;
    private JTextField theta1TextField;
    private JTextField theta2TextField;

    private JButton calculateButton;

    public InverseKinematicsGUI() {
        setTitle("Inverse Kinematics");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        xPosLabel = new JLabel("X Position:");
        xPosTextField = new JTextField(10);

        yPosLabel = new JLabel("Y Position:");
        yPosTextField = new JTextField(10);

        theta1Label = new JLabel("Theta 1:");
        theta1TextField = new JTextField(10);
        theta1TextField.setEditable(false);

        theta2Label = new JLabel("Theta 2:");
        theta2TextField = new JTextField(10);
        theta2TextField.setEditable(false);

        calculateButton = new JButton("Calculate");

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double xPos = Double.parseDouble(xPosTextField.getText());
                double yPos = Double.parseDouble(yPosTextField.getText());

                // Perform inverse kinematics calculations
                double L1 = 100.0; // Length of the first link
                double L2 = 150.0; // Length of the second link

                // Calculate theta2 using the law of cosines
                double c2 = (xPos * xPos + yPos * yPos - L1 * L1 - L2 * L2) / (2 * L1 * L2);
                double theta2 = Math.acos(c2);

                // Calculate theta1 using the law of sines
                double s2 = Math.sin(theta2);
                double s1 = ((L1 + L2 * c2) * yPos - L2 * s2 * xPos) / (xPos * xPos + yPos * yPos);
                double c1 = ((L1 + L2 * c2) * xPos + L2 * s2 * yPos) / (xPos * xPos + yPos * yPos);
                double theta1 = Math.atan2(s1, c1);

                // Update theta1TextField and theta2TextField with the calculated values
                theta1TextField.setText(String.valueOf(Math.toDegrees(theta1)));
                theta2TextField.setText(String.valueOf(Math.toDegrees(theta2)));
            }
        });

        add(xPosLabel);
        add(xPosTextField);
        add(yPosLabel);
        add(yPosTextField);
        add(theta1Label);
        add(theta1TextField);
        add(theta2Label);
        add(theta2TextField);
        add(calculateButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InverseKinematicsGUI();
            }
        });
    }
}
