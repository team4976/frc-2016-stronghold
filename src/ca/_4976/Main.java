package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();

    @Override
    public void robotInit() {

        Input.Encoder.SHOOTER.setReversed(true);
    }

    @Override
    public void disabledInit() {

        drive.disabledInit();
        intake.disabledInit();
        shooter.disabledInit();
        Output.Solenoid.CONTROLLER.disabledInit();
    }

    @Override
    public void teleopInit() {

        Output.Solenoid.CONTROLLER.init();
    }

    @Override
    public void teleopPeriodic() {

        System.out.println(Input.Encoder.SHOOTER.getVelocity());
        Output.Solenoid.CONTROLLER.periodic();
        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();
    }
}
