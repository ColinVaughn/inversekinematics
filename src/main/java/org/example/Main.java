package org.example;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;

public class Main extends SimpleApplication {
    private static final float ARM1_LENGTH = 2f;
    private static final float ARM2_LENGTH = 3f;
    private static final float ARM3_LENGTH = 4f;
    private static final float ARM_BASE_HEIGHT = 0.1f;

    private Geometry arm1;
    private Geometry arm2;
    private Geometry arm3;

    private float theta1;
    private float theta2;
    private float theta3;

    private Node armNode;

    private boolean draggingJoint1;
    private boolean draggingJoint2;
    private boolean draggingJoint3;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        armNode = new Node();

        // Create arm geometries
        Cylinder arm1Cylinder = new Cylinder(8, 16, 0.1f, ARM1_LENGTH);
        Cylinder arm2Cylinder = new Cylinder(8, 16, 0.1f, ARM2_LENGTH);
        Cylinder arm3Cylinder = new Cylinder(8, 16, 0.1f, ARM3_LENGTH);

        arm1 = new Geometry("Arm1", arm1Cylinder);
        arm2 = new Geometry("Arm2", arm2Cylinder);
        arm3 = new Geometry("Arm3", arm3Cylinder);

        Material armMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        armMaterial.setColor("Color", ColorRGBA.Blue);

        arm1.setMaterial(armMaterial);
        arm2.setMaterial(armMaterial);
        arm3.setMaterial(armMaterial);

        // Position the arms
        arm1.move(0, ARM1_LENGTH / 2, 0);
        arm2.move(0, ARM2_LENGTH / 2, 0);
        arm3.move(0, ARM3_LENGTH / 2, 0);

        // Rotate the arms to initial positions
        arm2.rotate(0, FastMath.HALF_PI, 0);
        arm3.rotate(0, FastMath.HALF_PI, 0);

        // Attach arms to the armNode
        armNode.attachChild(arm1);
        armNode.attachChild(arm2);
        armNode.attachChild(arm3);

        // Position the armNode
        armNode.move(0, ARM_BASE_HEIGHT, 0);

        // Attach armNode to the rootNode
        rootNode.attachChild(armNode);

        // Setup input controls
        inputManager.addMapping("RotateJoint1", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("RotateJoint2", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("RotateJoint3", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("DragJoint1", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("DragJoint2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("DragJoint3", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(analogListener, "RotateJoint1", "RotateJoint2", "RotateJoint3");
        inputManager.addListener(analogListener, "DragJoint1", "DragJoint2", "DragJoint3");
    }

    private AnalogListener analogListener = (name, value, tpf) -> {
        if (name.equals("RotateJoint1")) {
            float rotationAmount = -value * FastMath.PI;
            arm1.rotate(rotationAmount, 0, 0);
            theta1 += rotationAmount;
        } else if (name.equals("RotateJoint2")) {
            float rotationAmount = -value * FastMath.PI;
            arm2.rotate(rotationAmount, 0, 0);
            theta2 += rotationAmount;
        } else if (name.equals("RotateJoint3")) {
            float rotationAmount = -value * FastMath.PI;
            arm3.rotate(rotationAmount, 0, 0);
            theta3 += rotationAmount;
        } else if (name.equals("DragJoint1")) {
            draggingJoint1 = true;
        } else if (name.equals("DragJoint2")) {
            draggingJoint2 = true;
        } else if (name.equals("DragJoint3")) {
            draggingJoint3 = true;
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        if (draggingJoint1) {
            Vector2f mousePosition = inputManager.getCursorPosition();
            arm1.setLocalTranslation(mousePosition.x, ARM1_LENGTH / 2, mousePosition.y);
        } else if (draggingJoint2) {
            Vector2f mousePosition = inputManager.getCursorPosition();
            arm2.setLocalTranslation(mousePosition.x, ARM2_LENGTH / 2, mousePosition.y);
        } else if (draggingJoint3) {
            Vector2f mousePosition = inputManager.getCursorPosition();
            arm3.setLocalTranslation(mousePosition.x, ARM3_LENGTH / 2, mousePosition.y);
        }

        // Perform inverse kinematics calculations
        Vector3f armEndPosition = calculateArmEndPosition();
        updateArmNodePosition(armEndPosition);
    }

    private Vector3f calculateArmEndPosition() {
        // Perform inverse kinematics calculations to determine the position of the end effector

        // Calculate the position of the end effector based on the joint angles
        float x = (float) (Math.cos(theta1) * (ARM1_LENGTH + ARM2_LENGTH * Math.cos(theta2) + ARM3_LENGTH * Math.cos(theta2 + theta3)));
        float y = (float) (ARM_BASE_HEIGHT + ARM1_LENGTH * Math.sin(theta1) + ARM2_LENGTH * Math.sin(theta1 + theta2) + ARM3_LENGTH * Math.sin(theta1 + theta2 + theta3));
        float z = (float) (Math.sin(theta1) * (ARM1_LENGTH + ARM2_LENGTH * Math.cos(theta2) + ARM3_LENGTH * Math.cos(theta2 + theta3)));

        return new Vector3f(x, y, z);
    }

    private void updateArmNodePosition(Vector3f armEndPosition) {
        // Update the position of the armNode based on the calculated arm end position
        armNode.setLocalTranslation(armEndPosition);
    }
}
