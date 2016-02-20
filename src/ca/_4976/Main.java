package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.DriveTrain;
import ca._4976.sub.Shooter;
import ca._4976.sub.TurnAim;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain driveTrain = new DriveTrain();
    TurnAim turnaim = new TurnAim();
    Shooter shooter = new Shooter();

    public void robotInit() {
        shooter.robotInit();
    }

    public void teleopInit() {
        Output.Solenoid.CONTROLLER.init();
    }

    public void autonomousInit() {
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
        int defence = 0;
        int state = 0;
        int position = 0;

        switch (defence) {

            default:

            switch (position) {

                case 0://Lowbar
                    Output.Solenoid.INTAKE.set(false);
                    Output.Solenoid.HOOD.set(false);
                    Output.Motor.DRIVE_LEFT.set(1.0);
                    Output.Motor.DRIVE_RIGHT.set(1.0);

                    if (Input.I2C.LINE.crossed()) {
                        Output.Motor.DRIVE_LEFT.set(0.0);
                        Output.Motor.DRIVE_RIGHT.set(0.0);
                    }

                    if (state == 0) {
                        if (position == 0) {//needs to be compatable with a switch or network table
                            state = 1;  //Bot on left side Turning right
                        }
                        if (position == 1) {
                            state = 2;  //Bot on right side Turning left
                        }
                    }

                    if (state == 1) {   //Bot on left side Turning right
                        if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                            Output.Motor.DRIVE_LEFT.set(0.0);
                            Output.Motor.DRIVE_RIGHT.set(0.0);
                            System.out.println("it worked");
                            Output.Motor.SHOOTER.set(1);
                            Output.Motor.INTAKE_ROLLERS.set(-0.3);
                            state = 3;  //Now bot is done
                        } else {
                            Output.Motor.DRIVE_LEFT.set(-0.4);
                            Output.Motor.DRIVE_RIGHT.set(-0.4);
                        }

                        

                    }

                    if (state == 2) {   //Bot on right side Turning left
                        if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                            Output.Motor.DRIVE_LEFT.set(0.0);
                            Output.Motor.DRIVE_RIGHT.set(0.0);
                            System.out.println("it worked");
                            Output.Motor.SHOOTER.set(1);
                            Output.Motor.INTAKE_ROLLERS.set(-0.3);
                            state = 3;  //Now bot is done
                        } else {
                            Output.Motor.DRIVE_LEFT.set(0.4);
                            Output.Motor.DRIVE_RIGHT.set(0.4);
                        }
                        
                    

                    }
                    if (state == 3) {   //Now bot is done
                        Output.Motor.SHOOTER.set(0.0);
                        Output.Motor.INTAKE_ROLLERS.set(-0.0);
                        Output.Motor.DRIVE_LEFT.set(0.0);
                        Output.Motor.DRIVE_RIGHT.set(0.0);
                    }

                    break;

                case 1:
                    Output.Motor.DRIVE_LEFT.set(1.0);
                    Output.Motor.DRIVE_RIGHT.set(1.0);

                    if (Input.I2C.LINE.crossed()) {

                        Output.Motor.DRIVE_LEFT.set(0.0);
                        Output.Motor.DRIVE_RIGHT.set(0.0);

                    }

            }
        }
    }
}
