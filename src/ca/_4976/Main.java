package ca._4976;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Main extends IterativeRobot {

    public enum  AutoTypes { BREACH, SHOOT, NOTHING }
    public enum DefenceTypes { LOWBAR, PORTCULLIS, CHEVELDEFRISE, TERRAIN, NOTHING }

    AutoTypes autoType = AutoTypes.SHOOT;
    DefenceTypes defenceType = DefenceTypes.PORTCULLIS;

    int state = 0;

    long autoTimeFlag = System.currentTimeMillis();

    NetworkTable autoSelectionTable = NetworkTable.getTable("Auto_Select");
    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();
    Targeting targeting = new Targeting();
    PowerDistributionPanel panel = new PowerDistributionPanel(10);

    @Override public void robotInit() {

        Input.Encoder.SHOOTER.setReversed(true);
        shooter.addTargetingSubsystem(targeting);
        drive.addTargetingSubsystem(targeting);
        panel.clearStickyFaults();
    }

    @Override public void disabledInit() {

        drive.disabledInit();
        intake.disabledInit();
        shooter.disabledInit();
        Output.Solenoid.CONTROLLER.disabledInit();
    }

    @Override public void teleopInit() {

        Output.Solenoid.CONTROLLER.init();
        Input.Encoder.DRIVE_RIGHT.reset();
    }

    @Override public void teleopPeriodic() {

        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();

        Output.Solenoid.CONTROLLER.periodic();

        if (Input.Encoder.SHOOTER.getVelocity() > 100) System.out.println("Shooter RPM: " + (int) Input.Encoder.SHOOTER.getVelocity());
    }

    @Override public void autonomousInit() {

        Output.Motor.DRIVE_LEFT.set(0);
        Output.Motor.DRIVE_RIGHT.set(0);
        Output.Motor.INTAKE_ROLLERS.set(0);
        Output.Motor.INTAKE_WHEELS.set(0);
        Output.Motor.SHOOTER.set(0);
        Output.Motor.SCALER.set(0);

        Output.Solenoid.GEAR.set(true);

        state = 0;

        if (autoSelectionTable.getBooleanArray("AutoState", new boolean[2])[0]) autoType = AutoTypes.BREACH;

        else if (autoSelectionTable.getBooleanArray("AutoState", new boolean[] {false, false})[1]) autoType = AutoTypes.SHOOT;

        else autoType = AutoTypes.NOTHING;


        if (autoSelectionTable.getBooleanArray("DefenceType", new boolean[4])[0]) defenceType = DefenceTypes.LOWBAR;

        else if (autoSelectionTable.getBooleanArray("DefenceType", new boolean[4])[1]) defenceType = DefenceTypes.PORTCULLIS;

        else if (autoSelectionTable.getBooleanArray("DefenceType", new boolean[4])[2]) defenceType = DefenceTypes.CHEVELDEFRISE;

        else if (autoSelectionTable.getBooleanArray("DefenceType", new boolean[4])[3]) defenceType = DefenceTypes.TERRAIN;

        else defenceType = DefenceTypes.NOTHING;
    }

    @Override public void autonomousPeriodic() {

        System.out.println(drive.hasTasks());

        drive.autonomousPeriodic();

        switch (autoType) {

            case BREACH:

                switch (defenceType) {

                    case LOWBAR:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(1);
                                    Output.Motor.DRIVE_RIGHT.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3000) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    state++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                autoType = AutoTypes.SHOOT;
                                state = 0;

                                break;
                        } break;

                    case PORTCULLIS:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.55);
                                    Output.Motor.DRIVE_RIGHT.set(-0.55);
                                    Output.Motor.INTAKE_WHEELS.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3900) { //38003ws3

                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state++;

                                } break;

                            case 3:

                                    autoTimeFlag = System.currentTimeMillis();
                                    Output.Motor.DRIVE_LEFT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                    Output.Motor.DRIVE_RIGHT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                    state++;

                                break;
                            case 4:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state = 0;
                                    autoType = AutoTypes.SHOOT;

                                } break;
                        } break;
                    case CHEVELDEFRISE:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.30);
                                    Output.Motor.DRIVE_RIGHT.set(-0.30);
                                    Output.Motor.INTAKE_WHEELS.set(1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 4000) { //3800

                                    Output.Solenoid.INTAKE.set(true);
                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0.6);
                                    Output.Motor.DRIVE_RIGHT.set(-0.6);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 3:

                                if (System.currentTimeMillis() - autoTimeFlag > 2500) { //3800

                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 4:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Motor.DRIVE_LEFT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                Output.Motor.DRIVE_RIGHT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                state++;

                                break;
                            case 5:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state = 0;
                                    autoType = AutoTypes.SHOOT;

                                } break;
                        } break;

                    case TERRAIN:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.7);
                                    Output.Motor.DRIVE_RIGHT.set(-0.7);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3600) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    state++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                autoType = AutoTypes.SHOOT;
                                state = 0;

                                break;
                        } break;

                } break;

            case SHOOT:

                if (!autoSelectionTable.getBooleanArray("AutoState", new boolean[2])[1]) {

                    state = -1;
                    autoType = AutoTypes.NOTHING;
                }

                switch (state) {

                    case 0:

                       targeting.aim();

                        if (targeting.onTarget()) state++;

                        break;
                    case 1:

                        shooter.cock();
                        state++;

                        break;
                    case 2:
                        if (shooter.shoot()) state++;

                        break;
                } break;
        }
    }

}