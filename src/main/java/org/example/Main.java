package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
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
    private JTextField xPosTextField;
    private JTextField yPosTextField;
    private JTextField xPosTextField2;
    private JTextField yPosTextField2;
    private JTextField xPosTextField3;
    private JTextField yPosTextField3;
    private JTextField theta1TextField;
    private JTextField theta2TextField;
    private JTextField theta3TextField;
    private JTextField appliedForceTextField;
    private JButton calculateButton;

    private double theta1;
    private double theta2;
    private double theta3;

    private boolean draggingJoint1;
    private boolean draggingJoint2;
    private boolean draggingJoint3;

    private double forceOnJoint1;
    private double forceOnJoint2;
    private double forceOnJoint3;
    private JLabel joint1Label;
    private JLabel joint2Label;
    private JLabel joint3Label;

    public Main() {
        setTitle("Inverse Kinematics");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

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

        theta1TextField = new JTextField(10);
        theta1TextField.setEditable(false);

        theta2TextField = new JTextField(10);
        theta2TextField.setEditable(false);

        theta3TextField = new JTextField(10);
        theta3TextField.setEditable(false);

        joint1Label = new JLabel("Joint 1");
        joint2Label = new JLabel("Joint 2");
        joint3Label = new JLabel("Joint 3");

        add(joint1Label);
        add(joint2Label);
        add(joint3Label);

        appliedForceTextField = new JTextField(10);
        xPosTextField.setText("50");
        yPosTextField.setText("50");
        xPosTextField2.setText("200");
        yPosTextField2.setText("200");
        xPosTextField3.setText("61");
        yPosTextField3.setText("271");
        appliedForceTextField.setText("10");

        calculateButton = new JButton("Calculate");

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateInverseKinematics();
                repaint();
            }
        });

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
        add(appliedForceTextField);
        add(calculateButton);
        add(theta1TextField);
        add(theta2TextField);
        add(theta3TextField);

        setupMouseListener();

        setVisible(true);
    }

    private void setupMouseListener() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int arm1EndX = (int) (ARM_BASE_X + ARM1_LENGTH * Math.cos(theta1));
                int arm1EndY = (int) (ARM_BASE_Y - ARM1_LENGTH * Math.sin(theta1));

                int arm2EndX = (int) (arm1EndX + ARM2_LENGTH * Math.cos(theta1 + theta2));
                int arm2EndY = (int) (arm1EndY - ARM2_LENGTH * Math.sin(theta1 + theta2));

                int arm3EndX = (int) (arm2EndX + ARM3_LENGTH * Math.cos(theta1 + theta2 + theta3));
                int arm3EndY = (int) (arm2EndY - ARM3_LENGTH * Math.sin(theta1 + theta2 + theta3));

                if (distance(x, y, arm1EndX, arm1EndY) <= 10) {
                    draggingJoint1 = true;
                } else if (distance(x, y, arm2EndX, arm2EndY) <= 10) {
                    draggingJoint2 = true;
                } else if (distance(x, y, arm3EndX, arm3EndY) <= 10) {
                    draggingJoint3 = true;
                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                draggingJoint1 = false;
                draggingJoint2 = false;
                draggingJoint3 = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                double sensitivity = 0.1;

                if (draggingJoint1) {
                    int newX = (int) (Integer.parseInt(xPosTextField.getText()) + (e.getX() - Integer.parseInt(xPosTextField.getText())) * sensitivity);
                    int newY = (int) (Integer.parseInt(yPosTextField.getText()) + (e.getY() - Integer.parseInt(yPosTextField.getText())) * sensitivity);
                    xPosTextField.setText(String.valueOf(newX));
                    yPosTextField.setText(String.valueOf(newY));
                } else if (draggingJoint2) {
                    int newX = (int) (Integer.parseInt(xPosTextField2.getText()) + (e.getX() - Integer.parseInt(xPosTextField2.getText())) * sensitivity);
                    int newY = (int) (Integer.parseInt(yPosTextField2.getText()) + (e.getY() - Integer.parseInt(yPosTextField2.getText())) * sensitivity);
                    xPosTextField2.setText(String.valueOf(newX));
                    yPosTextField2.setText(String.valueOf(newY));
                } else if (draggingJoint3) {
                    int newX = (int) (Integer.parseInt(xPosTextField3.getText()) + (e.getX() - Integer.parseInt(xPosTextField3.getText())) * sensitivity);
                    int newY = (int) (Integer.parseInt(yPosTextField3.getText()) + (e.getY() - Integer.parseInt(yPosTextField3.getText())) * sensitivity);
                    xPosTextField3.setText(String.valueOf(newX));
                    yPosTextField3.setText(String.valueOf(newY));
                }

                calculateInverseKinematics();
                repaint();
            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void calculateInverseKinematics() {
        double xPos1 = Double.parseDouble(xPosTextField.getText());
        double yPos1 = Double.parseDouble(yPosTextField.getText());

        double xPos2 = Double.parseDouble(xPosTextField2.getText());
        double yPos2 = Double.parseDouble(yPosTextField2.getText());

        double xPos3 = Double.parseDouble(xPosTextField3.getText());
        double yPos3 = Double.parseDouble(yPosTextField3.getText());

        double appliedForce = Double.parseDouble(appliedForceTextField.getText());

        // Perform inverse kinematics calculations
        double L1 = ARM1_LENGTH;
        double L2 = ARM2_LENGTH;
        double L3 = ARM3_LENGTH;

        // Calculate theta1 using the law of cosines
        double c1 = (xPos1 * xPos1 + yPos1 * yPos1 - L1 * L1) / (2 * L1 * Math.sqrt(xPos1 * xPos1 + yPos1 * yPos1));
        theta1 = Math.acos(c1);
        theta1 = Math.max(0, Math.min(theta1, Math.PI));

        // Calculate theta2 using the law of cosines
        double c2 = (xPos2 * xPos2 + yPos2 * yPos2 - L2 * L2) / (2 * L2 * Math.sqrt(xPos2 * xPos2 + yPos2 * yPos2));
        theta2 = Math.acos(c2);
        theta2 = Math.max(0, Math.min(theta2, Math.PI));

        // Calculate theta3 using the law of cosines
        double c3 = (xPos3 * xPos3 + yPos3 * yPos3 - L3 * L3) / (2 * L3 * Math.sqrt(xPos3 * xPos3 + yPos3 * yPos3));
        theta3 = Math.acos(c3);
        theta3 = Math.max(0, Math.min(theta3, Math.PI));

        // Calculate the forces on each joint
        double totalForce = appliedForce / Math.sin(theta3);
        forceOnJoint3 = totalForce;
        forceOnJoint2 = forceOnJoint3 / Math.sin(theta2);
        forceOnJoint1 = forceOnJoint2 / Math.sin(theta1);

        // Update theta1TextField, theta2TextField, and theta3TextField with the calculated values
        theta1TextField.setText(String.valueOf(Math.toDegrees(theta1)));
        theta2TextField.setText(String.valueOf(Math.toDegrees(theta2)));
        theta3TextField.setText(String.valueOf(Math.toDegrees(theta3)));
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Calculate the endpoints of the arms based on theta1, theta2, and theta3
        int arm1EndX = (int) (ARM_BASE_X + ARM1_LENGTH * Math.cos(theta1));
        int arm1EndY = (int) (ARM_BASE_Y - ARM1_LENGTH * Math.sin(theta1));

        int arm2EndX = (int) (arm1EndX + ARM2_LENGTH * Math.cos(theta1 + theta2));
        int arm2EndY = (int) (arm1EndY - ARM2_LENGTH * Math.sin(theta1 + theta2));

        int arm3EndX = (int) (arm2EndX + ARM3_LENGTH * Math.cos(theta1 + theta2 + theta3));
        int arm3EndY = (int) (arm2EndY - ARM3_LENGTH * Math.sin(theta1 + theta2 + theta3));

        // Update the position of each joint label based on the endpoint coordinates
        joint1Label.setBounds(arm1EndX + 10, arm1EndY - 30, 60, 20);
        joint2Label.setBounds(arm2EndX + 10, arm2EndY - 30, 60, 20);
        joint3Label.setBounds(arm3EndX + 10, arm3EndY - 30, 60, 20);

        // Draw the arms
        g.setColor(Color.BLACK);
        g.drawLine(ARM_BASE_X, ARM_BASE_Y, arm1EndX, arm1EndY);
        g.drawLine(arm1EndX, arm1EndY, arm2EndX, arm2EndY);
        g.drawLine(arm2EndX, arm2EndY, arm3EndX, arm3EndY);

        // Draw the joints
        g.setColor(Color.RED);
        g.fillOval(arm1EndX - 5, arm1EndY - 5, 10, 10);
        g.fillOval(arm2EndX - 5, arm2EndY - 5, 10, 10);
        g.fillOval(arm3EndX - 5, arm3EndY - 5, 10, 10);

        // Display the forces on each joint
        g.setColor(Color.BLACK);
        g.drawString("force joint 1: " + forceOnJoint1, 10, 40);
        g.drawString("force joint 2: " + forceOnJoint2, 10, 60);
        g.drawString("force joint 3: " + forceOnJoint3, 10, 80);
    }

    public static void main(String[] args) {
        new Main();
    }
}
