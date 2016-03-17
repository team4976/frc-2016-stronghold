package ca._4976;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Main extends IterativeRobot {

    public enum  AutoTypes { BREACH, SHOOT, NOTHING }
    public enum DefenceTypes { LOWBAR, PORTCULLIS, CHEVELDEFRIES, TERRAIN, NOTHING }

    AutoTypes state = AutoTypes.SHOOT;
    DefenceTypes defenceType = DefenceTypes.PORTCULLIS;

    int subState = 0;
    int startPosition = 0;

    NetworkTable autonomous = NetworkTable.getTable("Auto_Select");

    long autoTimeFlag = System.currentTimeMillis();

    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();
    Targeting targeting = new Targeting();

    @Override public void robotInit() {

        Input.Encoder.SHOOTER.setReversed(true);
        shooter.addTarget(targeting);
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

        if (Controller.Primary.Button.RIGHT_STICK.isDown() && !targeting.aim());

        else drive.teleopPeriodic();

        Output.Solenoid.CONTROLLER.periodic();

        intake.teleopPeriodic();
        shooter.teleopPeriodic();

        System.out.println(Input.Encoder.DRIVE_RIGHT.getDistance());

        if (Input.Encoder.SHOOTER.getVelocity() > 100) System.out.println(Input.Encoder.SHOOTER.getVelocity());
    }

    @Override public void autonomousInit() {

        Output.Motor.DRIVE_LEFT.set(0);
        Output.Motor.DRIVE_RIGHT.set(0);
        Output.Motor.INTAKE_ROLLERS.set(0);
        Output.Motor.INTAKE_WHEELS.set(0);
        Output.Motor.SHOOTER.set(0);
        Output.Motor.SCALER.set(0);

        Output.Solenoid.GEAR.set(true);

        subState = 0;

        if (autonomous.getBooleanArray("AutoState", new boolean[2])[0]) state = AutoTypes.BREACH;

        else if (autonomous.getBooleanArray("AutoState", new boolean[] {false, false})[1]) state = AutoTypes.SHOOT;

        else state = AutoTypes.NOTHING;


        if (autonomous.getBooleanArray("DefenceType", new boolean[4])[0]) defenceType = DefenceTypes.LOWBAR;

        else if (autonomous.getBooleanArray("DefenceType", new boolean[4])[1]) defenceType = DefenceTypes.PORTCULLIS;

        else if (autonomous.getBooleanArray("DefenceType", new boolean[4])[2]) defenceType = DefenceTypes.CHEVELDEFRIES;

        else if (autonomous.getBooleanArray("DefenceType", new boolean[4])[3]) defenceType = DefenceTypes.TERRAIN;

        else defenceType = DefenceTypes.NOTHING;
    }

    @Override public void autonomousPeriodic() {

        System.out.println(drive.hasTasks());

        drive.autonomousPeriodic();

        switch (state) {

            case BREACH:

                switch (defenceType) {

                    case LOWBAR:

                        switch (subState) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                subState++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(1);
                                    Output.Motor.DRIVE_RIGHT.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3000) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    subState++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                state = AutoTypes.SHOOT;
                                subState = 0;

                                break;
                        } break;

                    case PORTCULLIS:

                        switch (subState) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                subState++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.55);
                                    Output.Motor.DRIVE_RIGHT.set(-0.55);
                                    Output.Motor.INTAKE_WHEELS.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3900) { //38003ws3

                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    subState++;

                                } break;

                            case 3:

                                    autoTimeFlag = System.currentTimeMillis();
                                    Output.Motor.DRIVE_LEFT.set(-0.4 * autonomous.getNumber("direction", 0));
                                    Output.Motor.DRIVE_RIGHT.set(-0.4 * autonomous.getNumber("direction", 0));
                                    subState++;

                                break;
                            case 4:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    subState = 0;
                                    state = AutoTypes.SHOOT;

                                } break;
                        } break;
                    case CHEVELDEFRIES:

                        switch (subState) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                subState++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.30);
                                    Output.Motor.DRIVE_RIGHT.set(-0.30);
                                    Output.Motor.INTAKE_WHEELS.set(1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 4000) { //3800

                                    Output.Solenoid.INTAKE.set(true);
                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0.6);
                                    Output.Motor.DRIVE_RIGHT.set(-0.6);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;

                            case 3:

                                if (System.currentTimeMillis() - autoTimeFlag > 2500) { //3800

                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;

                            case 4:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Motor.DRIVE_LEFT.set(-0.4 * autonomous.getNumber("direction", 0));
                                Output.Motor.DRIVE_RIGHT.set(-0.4 * autonomous.getNumber("direction", 0));
                                subState++;

                                break;
                            case 5:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    subState = 0;
                                    state = AutoTypes.SHOOT;

                                } break;
                        } break;

                    case TERRAIN:

                        switch (subState) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                subState++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.7);
                                    Output.Motor.DRIVE_RIGHT.set(-0.7);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3600) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    subState++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                state = AutoTypes.SHOOT;
                                subState = 0;

                                break;
                        } break;

                } break;

            case SHOOT:

                if (!autonomous.getBooleanArray("AutoState", new boolean[2])[1]) {

                    subState = -1;
                    state = AutoTypes.NOTHING;
                }

                switch (subState) {

                    case 0:

                       targeting.aim();

                        if (targeting.onTarget()) subState++;

                        break;
                    case 1:

                        shooter.cock();
                        subState++;

                        break;
                    case 2:
                        if (shooter.shoot()) subState++;

                        break;
                } break;
        }
    }

}