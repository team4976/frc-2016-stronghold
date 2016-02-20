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

    boolean IntakeState = false;    // false = down, true = up
    boolean SHOOTER = false;     // true = 75%, false = golden efficiency
    double GRIPPER = 0.0;    //true=on' false=off
    boolean HOOD = false;
    boolean justShot = false;
    long delay;

    public void robotInit() {

        pid.setSetpoint(6000);
    }

    public void teleopPeriodic() {

        if (IntakeState == true) {

//            if (Controller.Primary.Button.A.isDownOnce()) {
//                Output.Motor.INTAKE_ROLLERS.set(-1);
//                GRIPPER = -1;
//                System.out.println("The gripper is spinning at: " + GRIPPER);
//            }
//
//            if (Input.Digital.BALL_DETECTED.get()) {
//                Output.Motor.INTAKE_ROLLERS.set(0.0);
//                GRIPPER = -0.0;
//                System.out.println("The gripper is not spinning");
//            }

            if (Controller.Primary.Button.B.isDownOnce()) {
                Output.Solenoid.INTAKE.set(false);
                IntakeState = false;
                System.out.println("The intake is up");
                //Output.Solenoid.HOOD.set(false);
                //HOOD = false;

                if (SHOOTER == false) {
                    pid.enable();
                    SHOOTER = true;
                    System.out.println("The shooter is going");

                } else {
                    Output.Motor.INTAKE_ROLLERS.set(-1);
                    GRIPPER = -1;
                    System.out.println("The gripper is spinning at: " + GRIPPER);
                }
            }

            if (Controller.Secondary.Button.X.isDownOnce()) {
                Output.Solenoid.INTAKE.set(false);
                IntakeState = false;
                System.out.println("The intake is up");
            }

        }
        if (IntakeState == false) {

            if (Controller.Primary.Button.A.isDownOnce()) {

                Output.Solenoid.INTAKE.set(true);
                IntakeState = true;
                System.out.println("The intake is down");
                System.out.println("The gripper is spinning at: " + GRIPPER);
                pid.disable();
                Output.Motor.SHOOTER.set(0.0);
                SHOOTER = false;
                System.out.println("The shooter is off");
                Output.Motor.INTAKE_ROLLERS.set(-1);
                GRIPPER = -1;
                System.out.println("The gripper is spinning at: " + GRIPPER);

                if (Input.Digital.BALL_DETECTED.get()) {
                    Output.Motor.INTAKE_ROLLERS.set(0.0);
                    GRIPPER = -0.0;
                    System.out.println("The gripper is not spinning");
                }
                //Output.Solenoid.HOOD.set(true);
                //HOOD = true;

            }

            if (Controller.Primary.Button.X.isDownOnce()) {
                Output.Motor.SHOOTER.set(0.0);
                SHOOTER = false;
                Output.Motor.INTAKE_ROLLERS.set(0.0);
                IntakeState = false;
            }

            if (Controller.Primary.Button.B.isDownOnce()) {

                if (SHOOTER == true) {

                    Output.Motor.INTAKE_ROLLERS.set(-1);
                    GRIPPER = -1;
                    System.out.println("The gripper is spinning at: " + GRIPPER);


                } else {
                    pid.enable();
                    SHOOTER = true;
                    System.out.println("The shooter is spinning");
                }
            }

            if (Controller.Secondary.Button.X.isDownOnce()) {
                Output.Solenoid.INTAKE.set(true);
                IntakeState = true;
                System.out.println("The inatke is down");
            }

        }

        if(!Input.Digital.BALL_DETECTED.get()) {
            if (!justShot) {
                delay = System.currentTimeMillis();
                justShot = true;
            } else
                if (System.currentTimeMillis() - delay > 500) {
                    Output.Motor.SHOOTER.set(0);
                    justShot = false;
                }
        }

        if (Math.abs(Controller.Secondary.Stick.RIGHT.vertical()) > 0.1)
            Output.Motor.INTAKE_ROLLERS.set(Controller.Secondary.Stick.RIGHT.vertical());
    }
}