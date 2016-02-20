package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.io.Utility;
import ca._4976.sub.DriveTrain;
import ca._4976.sub.Shooter;
import ca._4976.sub.TurnAim;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain driveTrain = new DriveTrain();
    TurnAim turnaim = new TurnAim();
    Shooter shooter = new Shooter();

    int autonomousPosition = 1;

    public void robotInit() {
        shooter.robotInit();
    }

    public void teleopInit() {
        Output.Solenoid.CONTROLLER.init();
    }

    public void autonomousInit() {
        //TODO: write position checker
        //autonomousPosition = getAutoPosition();
    }

    public void disabledInit() {
        Output.Solenoid.CONTROLLER.disabledInit();
    }

    public void teleopPeriodic() {
        driveTrain.teleopPeriodic();
        turnaim.teleopPeriodic();
        shooter.teleopPeriodic();
    }

    public void autonomousPeriodic() {
        int state = 0;

        if (state == 0) {
            // Lower intake and hood to get under lowbar
            if (autonomousPosition == 1) {
                Output.Solenoid.INTAKE.set(true);
                Output.Solenoid.HOOD.set(false);
            }

            // Drive into defense
            Output.Motor.DRIVE_LEFT.set(1.0);
            Output.Motor.DRIVE_RIGHT.set(-1.0);

            Utility.startDelay(3000, "LineCrossCancel");
            Utility.startDelay(1000, "DefenseSpeed");

            if (Utility.checkDelay("DefenseSpeed")) {
                Output.Motor.DRIVE_LEFT.set(0.25);
                Output.Motor.DRIVE_RIGHT.set(-0.25);
                Output.Motor.SHOOTER.set(1);
                Utility.startDelay(2000, "Cock");
            }

            if (Input.I2C.LINE.crossed() || Utility.checkDelay("LineCrossCancel")) {
                Output.Motor.DRIVE_LEFT.set(0.0);
                Output.Motor.DRIVE_RIGHT.set(0.0);
                state = 1;
            }
        }

        if (state == 1) {   // Bot turning
            Output.Solenoid.HOOD.set(true);
            Output.Solenoid.INTAKE.set(false);

            if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                Output.Motor.DRIVE_LEFT.set(0.0);
                Output.Motor.DRIVE_RIGHT.set(0.0);

                if (Utility.checkDelay("Cock")) {
                    Utility.startDelay(1000, "Fire");
                    Output.Motor.INTAKE_ROLLERS.set(-0.3);
                }

                if (Utility.checkDelay("Fire"))
                    state = 2;
            } else {
                if (autonomousPosition >= 4) {
                    Output.Motor.DRIVE_LEFT.set(-0.4);
                    Output.Motor.DRIVE_RIGHT.set(-0.4);
                } else {
                    Output.Motor.DRIVE_LEFT.set(0.4);
                    Output.Motor.DRIVE_RIGHT.set(0.4);
                }
            }
        }

        if (state == 2) {
            Output.Motor.SHOOTER.set(0.0);
            Output.Motor.INTAKE_ROLLERS.set(0.0);
            Output.Motor.DRIVE_LEFT.set(0.0);
            Output.Motor.DRIVE_RIGHT.set(0.0);
        }
    }
}
