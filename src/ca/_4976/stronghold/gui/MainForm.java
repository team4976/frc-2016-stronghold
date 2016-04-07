package ca._4976.stronghold.gui;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    public JPanel panel;
    private JCheckBox breachCheckBox;
    private JCheckBox alignCheckBox;
    private JCheckBox aimCheckBox;
    private JCheckBox shootCheckBox;
    private JRadioButton lowbarRadioButton;
    private JRadioButton portcullisRadioButton;
    private JRadioButton chevalDeFriseRadioButton;
    private JRadioButton terrainRadioButton;
    private JRadioButton lowbarRadioButton1;
    private JRadioButton rightRadioButton;
    private JRadioButton straightRadioButton;
    private JRadioButton leftRadioButton;
    private JLabel ballDetected;

    private int defenceSelection = 0;
    private int alignSelection = 0;

    MainForm() {

        lowbarRadioButton.addActionListener(e -> breachConfigSelection(1));
        portcullisRadioButton.addActionListener(e -> breachConfigSelection(2));
        chevalDeFriseRadioButton.addActionListener(e -> breachConfigSelection(3));
        terrainRadioButton.addActionListener(e -> breachConfigSelection(4));

        lowbarRadioButton1.addActionListener(e -> alignConfigSelection(1));
        rightRadioButton.addActionListener(e -> alignConfigSelection(2));
        straightRadioButton.addActionListener(e -> alignConfigSelection(3));
        leftRadioButton.addActionListener(e -> alignConfigSelection(4));
    }

    private void breachConfigSelection(int selection) {

        defenceSelection = selection;

        lowbarRadioButton.setSelected(selection == 1);
        portcullisRadioButton.setSelected(selection == 2);
        chevalDeFriseRadioButton.setSelected(selection == 3);
        terrainRadioButton.setSelected(selection == 4);
    }

    private void alignConfigSelection(int selection) {

        alignSelection = selection;

        lowbarRadioButton1.setSelected(selection == 1);
        rightRadioButton.setSelected(selection == 2);
        straightRadioButton.setSelected(selection == 3);
        leftRadioButton.setSelected(selection == 4);
    }

    void setBallDetected(boolean isDetected) {

        ballDetected.setForeground(isDetected ? Color.GREEN : Color.red);
        ballDetected.setText(isDetected ? "TRUE" : "FALSE");
    }

    int getAlignSelection() { return alignSelection; }

    int getDefenceSelection() { return defenceSelection; }

    boolean[] getAutoSelection() { return new boolean[] {
            breachCheckBox.isSelected(),
            alignCheckBox.isSelected(),
            aimCheckBox.isSelected(),
            shootCheckBox.isSelected()
    }; }
}
