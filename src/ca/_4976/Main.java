package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    public enum  AutoTypes { BREACH, SHOOT }
    public enum  DefenceType { LOWBAR, PORTCULLIS }

    AutoTypes state = AutoTypes.SHOOT;
    DefenceType defenceType = DefenceType.PORTCULLIS;

    int subState = 0;
    int startPosition = 0;

    long autoTimeFlag = System.currentTimeMillis();

    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();

    @Override public void robotInit() {

        Input.Encoder.SHOOTER.setReversed(false);
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

        Output.Solenoid.CONTROLLER.periodic();
        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();

        System.out.println(Input.Encoder.DRIVE_RIGHT.getDistance());

        if (Input.Encoder.SHOOTER.getVelocity() < -100) System.out.println(Input.Encoder.SHOOTER.getVelocity());
    }

    @Override public void autonomousInit() {

        subState = 0;
        state = AutoTypes.SHOOT;
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

                                    Output.Motor.DRIVE_LEFT.set(0.6);
                                    Output.Motor.DRIVE_RIGHT.set(-0.6);
                                    Output.Motor.INTAKE_WHEELS.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    subState++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3800) {

                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    subState++;

                                } break;

                            case 3:

                                    autoTimeFlag = System.currentTimeMillis();
                                    Output.Motor.DRIVE_LEFT.set(-0.4);
                                    Output.Motor.DRIVE_RIGHT.set(-0.4);
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
                } break;

            case SHOOT:

                switch (subState) {

                    case 0:

                        do {

                            drive.ScheduleTask(DriveTrain.TaskType.AIM, startPosition < 2 ? 1d : -1d);

                        } while (!drive.hasTasks());
                        subState++;
                        break;
                    case 1:

                        shooter.cock();

                        break;
                    case 2:
                        if (shooter.shoot()) subState++;

                        break;

                } break;
        }
    }
}
