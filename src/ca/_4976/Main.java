package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.io.Utility;
import ca._4976.sub.DriveTrain;
import ca._4976.sub.Shooter;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain driveTrain = new DriveTrain();
    Shooter shooter = new Shooter();

    int autonomousPosition = 1;
    int autonomousState = 0;

    public void robotInit() {
        shooter.robotInit();
    }

    public void teleopInit() {
        Output.Solenoid.CONTROLLER.init();
    }

    public void autonomousInit() {
        //autonomousPosition = getAutoPosition();
        autonomousState = 0;
        Utility.removeDelay("StartAuto");
        Utility.removeDelay("LineCrossCancel");
        Utility.removeDelay("DefenseSpeed");
        Utility.removeDelay("Cock");
        Utility.removeDelay("Fire");
    }

    public void disabledInit() {
        Output.Solenoid.CONTROLLER.disabledInit();
    }

    public void teleopPeriodic() {
        Output.Solenoid.CONTROLLER.periodic();
        driveTrain.teleopPeriodic();
        shooter.teleopPeriodic();
        System.out.println(Input.I2C.LINE.crossed());
    }

    public void testPeriodic() {
        Input.I2C.LINE.callibrate();
    }

    public void autonomousPeriodic() {
        // Driving over defense and crossing line
        if (autonomousState == 0) {

            // Lower intake and hood if at lowbar
            if (autonomousPosition == 1) {
                Utility.startDelay(3000, "StartAuto");
                Output.Solenoid.INTAKE.set(true);
                Output.Solenoid.HOOD.set(false);
            } else {
                Utility.startDelay(0, "StartAuto");
            }

            if (Utility.checkDelay("StartAuto")) {
                // Drive into defense
                driveTrain.forward(0.5);
                Utility.startDelay(3000, "LineCrossCancel");
                Utility.startDelay(1000, "DefenseSpeed");
            }

            // After 1 second
            if (Utility.checkDelay("DefenseSpeed")) {

                // Drive slower to line
                driveTrain.forward(0.25);

                // Cock shooter
                Output.Motor.SHOOTER.set(1);
                Utility.startDelay(2000, "Cock");
            }

            // After line is crossed or after 3 seconds
            if (Input.I2C.LINE.crossed() || Utility.checkDelay("LineCrossCancel")) {
                // Stop and turn to goal
                driveTrain.stop();
                autonomousState = 1;
            }

            // Turn to find goal
        } else if (autonomousState == 1) {

            // Raise hood and intake incase they were lowered
            Output.Solenoid.HOOD.set(true);
            Output.Solenoid.INTAKE.set(false);

            // Goal is found
            if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                driveTrain.stop();

                // Shooter cocked
                if (Utility.checkDelay("Cock")) {

                    // Load ball and shoot
                    Utility.startDelay(1000, "Fire");
                    Output.Motor.INTAKE_ROLLERS.set(-0.3);
                }

                // Ball has been shot, stop autonomous
                if (Utility.checkDelay("Fire"))
                    autonomousState = 2;

                // Goal not yet found
            } else {

                // Turn right
                if (autonomousPosition >= 4) {
                    driveTrain.turnLeft(0.25);

                    // Turn left
                } else {
                    driveTrain.turnRight(0.25);
                }
            }

            // Autonomous complete
        } else if (autonomousState == 2) {

            // Stop everything
            Output.Motor.SHOOTER.set(0.0);
            Output.Motor.INTAKE_ROLLERS.set(0.0);
            driveTrain.stop();
        }
    }
}
