package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.PIDController;


/**
 * Created by Grant on 1/23/2016.
 */
public class Shooter {

    PIDController pid = new PIDController(1.0e-4, 0, 0.001, 0, Input.Encoder.SHOOTER, Output.Motor.SHOOTER);

    boolean IntakeState = true;    // false = down, true = up
    boolean SHOOTER = false;     // true = 75%, false = golden efficiency
    double GRIPPER = 0.0;    //true=on' false=off
    boolean HOOD = false;
    boolean justShot = false;
    long delay;

    public void robotInit() {
        pid.setSetpoint(6000);
    }
    public void teleopPeriodic() {
        if (IntakeState == false) {
            if (Controller.Primary.Button.B.isDownOnce()) {
                Output.Solenoid.INTAKE.set(true);
                IntakeState = true;
                if (SHOOTER == false) {
                    System.out.println("PID enabled");
                    pid.enable();
                    SHOOTER = true;
                } else {
                    Output.Motor.INTAKE_ROLLERS.set(-1);
                    GRIPPER = -1;
                }
            }
            if (Input.Digital.BALL_DETECTED.get()) {
                Output.Motor.INTAKE_ROLLERS.set(0.0);
            }
            if (Controller.Secondary.Button.X.isDownOnce()) {
                Output.Solenoid.INTAKE.set(true);
                IntakeState = true;
            }
        }
        if (IntakeState == true) {
            if (Controller.Primary.Button.A.isDownOnce()) {
                Output.Solenoid.INTAKE.set(false);
                IntakeState = false;
                pid.disable();
                Output.Motor.SHOOTER.set(0.0);
                SHOOTER = false;
                Output.Motor.INTAKE_ROLLERS.set(-1);
            }
            if (Controller.Primary.Button.X.isDownOnce()){
                System.out.println("Setting the shooter motor to 0");
                pid.disable();
                Output.Motor.SHOOTER.set(0);
                Output.Motor.INTAKE_ROLLERS.set(0.0);
            }
            if (Controller.Primary.Button.B.isDownOnce()) {

                if (SHOOTER == true) {
                    Output.Motor.INTAKE_ROLLERS.set(-1);
                    GRIPPER = -1;
                } else {
                    System.out.println("PID enabled 2");
                    pid.enable();
                    SHOOTER = true;
                }
            }

        }
//        if(!Input.Digital.BALL_DETECTED.get()) {
//            if (!justShot) {
//                delay = System.currentTimeMillis();
//                justShot = true;
//            } else if (System.currentTimeMillis() - delay > 500) {
//                Output.Motor.SHOOTER.set(0);
//                justShot = false;
//            }
//        }
        if (Math.abs(Controller.Secondary.Stick.RIGHT.vertical()) > 0.1)
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Stick.RIGHT.vertical());

        if (Controller.Secondary.Button.A.isDownOnce()){
            Output.Solenoid.INTAKE.set(!IntakeState);
            IntakeState = !IntakeState;
        }
        if (Math.abs(Controller.Secondary.Stick.LEFT.vertical()) > 0.1){
            Output.Motor.SHOOTER.set(Controller.Secondary.Stick.LEFT.vertical());
        }

        if (Controller.Secondary.DPad.NORTH.isDownOnce()){
            Output.Solenoid.HOOD.set(true);
        }
        if (Controller.Secondary.DPad.SOUTH.isDownOnce()){
            Output.Solenoid.HOOD.set(false);
        }
        if (Controller.Secondary.Trigger.LEFT.value() > 0.1){
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Trigger.LEFT.value());
        }
        if (Controller.Secondary.Trigger.RIGHT.value() > 0.1){
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Trigger.RIGHT.value());
        }


    }

}