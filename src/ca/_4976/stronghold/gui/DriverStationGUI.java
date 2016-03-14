package ca._4976.stronghold.gui;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import javax.swing.*;

public class DriverStationGUI extends JFrame {

    MainForm mainForm = new MainForm();

    NetworkTable selection;
    NetworkTable status;

    private DriverStationGUI() {

        setName("DriverStation GUI");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(mainForm.panel);
        setResizable(false);
        pack();
        setVisible(true);

        NetworkTable.setClientMode();
        NetworkTable.setIPAddress("localhost");

        selection = NetworkTable.getTable("Autonomous Parameters");
        status = NetworkTable.getTable("System status");

        status.putBoolean("RoboRio", false);
        status.putBoolean("RaspberryPi", false);
        status.putBoolean("Camera", false);
        status.putBoolean("Grip", false);

        long lastTick = System.currentTimeMillis();

        while (isVisible())

            if (System.currentTimeMillis() - lastTick >= 1000 / 20) {

                lastTick = System.currentTimeMillis();
                tick();
            }

        dispose();
    }

    private void tick() {

        selection.putBooleanArray("Auto Config", mainForm.getSelections()[0]);
        selection.putBooleanArray("Defence Config", mainForm.getSelections()[1]);
        selection.putBooleanArray("Aim + Shoot Config", mainForm.getSelections()[2]);


        mainForm.setStatus(new boolean[] {
                status.getBoolean("RoboRio", false) && status.isConnected(),
                status.getBoolean("RaspberryPi", false) && status.isConnected(),
                status.getBoolean("Camera", false) && status.isConnected(),
                status.getBoolean("Grip", false) && status.isConnected()
        });
    }

    public static void main(String[] args) { new DriverStationGUI(); }
}
