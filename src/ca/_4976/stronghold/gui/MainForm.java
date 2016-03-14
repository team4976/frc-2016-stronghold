package ca._4976.stronghold.gui;

import sun.applet.Main;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    
    public JPanel panel;
    private JCheckBox breachCheckBox;
    private JCheckBox shootCheckBox;
    private JRadioButton lowbarRadioButton;
    private JRadioButton portcullisRadioButton;
    private JRadioButton chevalDeFriseRadioButton;
    private JRadioButton terrainRadioButton;
    private JRadioButton turnLeftRadioButton;
    private JRadioButton turnRightRadioButton;
    private JRadioButton dontTurnRadioButton;
    private JCheckBox moveCloserCheckBox;
    private JLabel roboRioStatus;
    private JLabel piStatus;
    private JLabel cameraStatus;
    private JLabel gripStatus;

    public MainForm() {
        
        lowbarRadioButton.addActionListener(e -> onClickLowbarRadioButton());
        portcullisRadioButton.addActionListener(e -> clickPortcullisRadioButton());
        chevalDeFriseRadioButton.addActionListener(e -> onClickChevalDeFriseRadioButton());
        terrainRadioButton.addActionListener(e -> onClickTerrainRadioButton());
        dontTurnRadioButton.addActionListener(e -> onClickDontTurnRadioButton());
        turnLeftRadioButton.addActionListener(e -> onClickTurnLeftRadioButton());
        turnRightRadioButton.addActionListener(e -> onClickTurnRightRadioButton());
    }
    
    public boolean[][] getSelections() {

        return new boolean[][] {
                {breachCheckBox.isSelected(), shootCheckBox.isSelected()},
                {lowbarRadioButton.isSelected(), portcullisRadioButton.isSelected(),
                        chevalDeFriseRadioButton.isSelected(), terrainRadioButton.isSelected()},
                {dontTurnRadioButton.isSelected(), turnLeftRadioButton.isSelected(),
                        turnRightRadioButton.isSelected(), moveCloserCheckBox.isSelected()},
        };
    }

    public void setStatus(boolean[] status) {

        roboRioStatus.setText(status[0] ? "OK" : "WAITING");
        roboRioStatus.setForeground(status[0] ? Color.GREEN : Color.RED);

        piStatus.setText(status[1] ? "OK" : "WAITING");
        piStatus.setForeground(status[1] ? Color.GREEN : Color.RED);
        
        cameraStatus.setText(status[2] ? "OK" : "WAITING");
        cameraStatus.setForeground(status[2] ? Color.GREEN : Color.RED);

        gripStatus.setText(status[3] ? "OK" : "WAITING");
        gripStatus.setForeground(status[3] ? Color.GREEN : Color.RED);
    }
    
    private void onClickLowbarRadioButton() {
        
        portcullisRadioButton.setSelected(false);
        chevalDeFriseRadioButton.setSelected(false);
        terrainRadioButton.setSelected(false);
    }
    
    private void clickPortcullisRadioButton() {

        lowbarRadioButton.setSelected(false);
        chevalDeFriseRadioButton.setSelected(false);
        terrainRadioButton.setSelected(false);
    }
    
    private void onClickChevalDeFriseRadioButton() {
        
        lowbarRadioButton.setSelected(false);
        portcullisRadioButton.setSelected(false);
        terrainRadioButton.setSelected(false);
    }

    private void onClickTerrainRadioButton() {
        
        lowbarRadioButton.setSelected(false);
        portcullisRadioButton.setSelected(false);
        chevalDeFriseRadioButton.setSelected(false);
    }

    private void onClickDontTurnRadioButton() {
        
        turnRightRadioButton.setSelected(false);
        turnLeftRadioButton.setSelected(false);
    }
    
    private void onClickTurnLeftRadioButton() {
        
        dontTurnRadioButton.setSelected(false);
        turnRightRadioButton.setSelected(false);
    }

    private void onClickTurnRightRadioButton() {
        
        dontTurnRadioButton.setSelected(false);
        turnLeftRadioButton.setSelected(false);
    }
}