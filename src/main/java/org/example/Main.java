package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int ARM_BASE_X = 400;
    private static final int ARM_BASE_Y = 400;
    private static final int ARM1_LENGTH = 100;
    private static final int ARM2_LENGTH = 150;
    private static final int ARM3_LENGTH = 200;

    private JLabel xPosLabel;
    private JLabel yPosLabel;
    private JLabel xPosLabel2;
    private JLabel yPosLabel2;
    private JLabel xPosLabel3;
    private JLabel yPosLabel3;
    private JLabel theta1Label;
    private JLabel theta2Label;
    private JLabel theta3Label;
    private JLabel loadLabel;
    private JLabel joint1LoadLabel;
    private JLabel joint2LoadLabel;
    private JLabel joint3LoadLabel;

    private JTextField xPosTextField;
    private JTextField yPosTextField;
    private JTextField xPosTextField2;
    private JTextField yPosTextField2;
    private JTextField xPosTextField3;
    private JTextField yPosTextField3;
    private JTextField theta1TextField;
    private JTextField theta2TextField;
    private JTextField theta3TextField;
    private JTextField loadTextField;

    private JButton calculateButton;

    public Main() {
        setTitle("Inverse Kinematics");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        initializeComponents();
        setupListeners();

        setVisible(true);
    }

    private void initializeComponents() {
        xPosLabel = new JLabel("X Position:");
        xPosTextField = new JTextField(10);

        yPosLabel = new JLabel("Y Position:");
        yPosTextField = new JTextField(10);

        xPosLabel2 = new JLabel("X Position 2:");
        xPosTextField2 = new JTextField(10);

        yPosLabel2 = new JLabel("Y Position 2:");
        yPosTextField2 = new JTextField(10);

        xPosLabel3 = new JLabel("X Position 3:");
        xPosTextField3 = new JTextField(10);

        yPosLabel3 = new JLabel("Y Position 3:");
        yPosTextField3 = new JTextField(10);

        theta1Label = new JLabel("Theta 1:");
        theta1TextField = new JTextField(10);
        theta1TextField.setEditable(false);

        theta2Label = new JLabel("Theta 2:");
        theta2TextField = new JTextField(10);
        theta2TextField.setEditable(false);

        theta3Label = new JLabel("Theta 3:");
        theta3TextField = new JTextField(10);
        theta3TextField.setEditable(false);

        loadLabel = new JLabel("Load (Newtons):");
        loadTextField = new JTextField(10);

        joint1LoadLabel = new JLabel("Joint 1 Load:");
        joint2LoadLabel = new JLabel("Joint 2 Load:");
        joint3LoadLabel = new JLabel("Joint 3 Load:");

        calculateButton = new JButton("Calculate");

        add(xPosLabel);
        add(xPosTextField);
        add(yPosLabel);
        add(yPosTextField);
        add(xPosLabel2);
        add(xPosTextField2);
        add(yPosLabel2);
        add(yPosTextField2);
        add(xPosLabel3);
        add(xPosTextField3);
        add(yPosLabel3);
        add(yPosTextField3);
        add(theta1Label);
        add(theta1TextField);
        add(theta2Label);
        add(theta2TextField);
        add(theta3Label);
        add(theta3TextField);
        add(loadLabel);
        add(loadTextField);
        add(calculateButton);
        add(joint1LoadLabel);
        add(joint2LoadLabel);
        add(joint3LoadLabel);
    }

    private void setupListeners() {
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateInverseKinematics();
                calculateJointLoads();
                repaint();
            }
        });
    }

    private void calculateInverseKinematics() {
        double xPos1 = Double.parseDouble(xPosTextField.getText());
        double yPos1 = Double.parseDouble(yPosTextField.getText());

        double xPos2 = Double.parseDouble(xPosTextField2.getText());
        double yPos2 = Double.parseDouble(yPosTextField2.getText());

        double xPos3 = Double.parseDouble(xPosTextField3.getText());
        double yPos3 = Double.parseDouble(yPosTextField3.getText());

        // Perform inverse kinematics calculations
        double L1 = ARM1_LENGTH;
        double L2 = ARM2_LENGTH;
        double L3 = ARM3_LENGTH;

        // Calculate theta1 using the law of cosines
        double c1 = (xPos1 * xPos1 + yPos1 * yPos1 - L1 * L1) / (2 * L1 * Math.sqrt(xPos1 * xPos1 + yPos1 * yPos1));
        double theta1 = Math.acos(c1);

        // Calculate theta2 using the law of cosines
        double c2 = (xPos2 * xPos2 + yPos2 * yPos2 - L2 * L2) / (2 * L2 * Math.sqrt(xPos2 * xPos2 + yPos2 * yPos2));
        double theta2 = Math.acos(c2);

        // Calculate theta3 using the law of cosines
        double c3 = (xPos3 * xPos3 + yPos3 * yPos3 - L3 * L3) / (2 * L3 * Math.sqrt(xPos3 * xPos3 + yPos3 * yPos3));
        double theta3 = Math.acos(c3);

        // Update theta1TextField, theta2TextField, and theta3TextField with the calculated values
        theta1TextField.setText(String.valueOf(Math.toDegrees(theta1)));
        theta2TextField.setText(String.valueOf(Math.toDegrees(theta2)));
        theta3TextField.setText(String.valueOf(Math.toDegrees(theta3)));
    }

    private void calculateJointLoads() {
        double load = Double.parseDouble(loadTextField.getText());
        double joint1Load = load / 3;
        double joint2Load = load / 3;
        double joint3Load = load / 3;

        // Update joint1LoadLabel, joint2LoadLabel, and joint3LoadLabel with the calculated values
        joint1LoadLabel.setText("Joint 1 Load: " + joint1Load + " N");
        joint2LoadLabel.setText("Joint 2 Load: " + joint2Load + " N");
        joint3LoadLabel.setText("Joint 3 Load: " + joint3Load + " N");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Calculate the endpoints of the arms based on theta1, theta2, and theta3
        double theta1 = Math.toRadians(Double.parseDouble(theta1TextField.getText()));
        double theta2 = Math.toRadians(Double.parseDouble(theta2TextField.getText()));
        double theta3 = Math.toRadians(Double.parseDouble(theta3TextField.getText()));

        int arm1EndX = (int) (ARM_BASE_X + ARM1_LENGTH * Math.cos(theta1));
        int arm1EndY = (int) (ARM_BASE_Y - ARM1_LENGTH * Math.sin(theta1));

        int arm2EndX = (int) (arm1EndX + ARM2_LENGTH * Math.cos(theta1 + theta2));
        int arm2EndY = (int) (arm1EndY - ARM2_LENGTH * Math.sin(theta1 + theta2));

        int arm3EndX = (int) (arm2EndX + ARM3_LENGTH * Math.cos(theta1 + theta2 + theta3));
        int arm3EndY = (int) (arm2EndY - ARM3_LENGTH * Math.sin(theta1 + theta2 + theta3));

        // Draw the arms
        g.setColor(Color.BLACK);
        g.drawLine(ARM_BASE_X, ARM_BASE_Y, arm1EndX, arm1EndY);
        g.drawLine(arm1EndX, arm1EndY, arm2EndX, arm2EndY);
        g.drawLine(arm2EndX, arm2EndY, arm3EndX, arm3EndY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
