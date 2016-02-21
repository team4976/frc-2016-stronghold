package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.io.Utility;
import edu.wpi.first.wpilibj.PIDController;

public class Shooter {

    PIDController pid = new PIDController(1.0e-4, 0, 0.001, 0, Input.Encoder.SHOOTER, Output.Motor.SHOOTER);

    public void robotInit() {
        pid.setSetpoint(6000);
    }

    public void teleopPeriodic() {

        // Intake is lowered
        if (Output.Solenoid.INTAKE.get()) {
            System.out.println("down");
            if (Controller.Primary.Button.A.isDownOnce()) {
                Output.Motor.INTAKE_ROLLERS.set(1.0);
            }
            if (Controller.Primary.Button.B.isDownOnce()) {
                Output.Motor.SHOOTER.set(1.0);
                Output.Solenoid.INTAKE.set(false);
            }
            if (Controller.Primary.Button.X.isDownOnce()) {
                Output.Motor.SHOOTER.set(0);
                Output.Motor.INTAKE_ROLLERS.set(0);
                Output.Solenoid.INTAKE.set(false);
            }
            if (Input.Digital.BALL_DETECTED.get())
                Output.Motor.INTAKE_ROLLERS.set(0);
            // Intake is raised
        } else {
            if (Controller.Primary.Button.A.isDownOnce()) {
                Output.Solenoid.INTAKE.set(true);
                pid.disable();
                Output.Motor.SHOOTER.set(0.0);
                Output.Motor.INTAKE_ROLLERS.set(1);
            }
            if (Controller.Primary.Button.B.isDownOnce()) {
                Output.Motor.INTAKE_ROLLERS.set(1);
                Utility.startDelay(1000, "ShooterDelay");
            }
            if (Controller.Primary.Button.X.isDownOnce()) {
                Output.Motor.SHOOTER.set(0);
                Output.Motor.INTAKE_ROLLERS.set(0);
            }
        }

        if (Utility.checkDelay("ShooterDelay")) {
            Output.Motor.INTAKE_ROLLERS.set(0);
            Output.Motor.SHOOTER.set(0);
            Utility.removeDelay("ShooterDelay");
        }

        if (Math.abs(Controller.Secondary.Stick.RIGHT.vertical()) > 0.1)
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Stick.RIGHT.vertical());
        if (Controller.Secondary.Button.A.isDownOnce())
            Output.Solenoid.INTAKE.set(!Output.Solenoid.INTAKE.get());
        if (Math.abs(Controller.Secondary.Stick.LEFT.vertical()) > 0.1)
            Output.Motor.SHOOTER.set(Controller.Secondary.Stick.LEFT.vertical());
        if (Controller.Secondary.DPad.NORTH.isDownOnce())
            Output.Solenoid.HOOD.set(true);
        if (Controller.Secondary.DPad.SOUTH.isDownOnce())
            Output.Solenoid.HOOD.set(false);
        if (Controller.Secondary.Trigger.LEFT.value() > 0.1)
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Trigger.LEFT.value());
        if (Controller.Secondary.Trigger.RIGHT.value() > 0.1)
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Trigger.RIGHT.value());
    }

}