package ca._4976.stronghold.gui;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import javax.swing.*;

public class DriverStationGUI extends JFrame {

    private MainForm mainForm = new MainForm();

    private NetworkTable selection;
    private NetworkTable status;

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
        status = NetworkTable.getTable("Status");

        long lastTick = System.currentTimeMillis();

        while (isVisible())

            if (System.currentTimeMillis() - lastTick >= 1000 / 20) {

                lastTick = System.currentTimeMillis();
                tick();
            }

        dispose();
    }

    private void tick() {

        mainForm.setBallDetected(status.getBoolean("Ball Detected", false));

        selection.putBooleanArray("Autonomous Selection", mainForm.getAutoSelection());
        selection.putNumber("Defence Selection", mainForm.getDefenceSelection());
        selection.putNumber("Align Selection", mainForm.getAlignSelection());
    }

    public static void main(String[] args) { new DriverStationGUI(); }
}
