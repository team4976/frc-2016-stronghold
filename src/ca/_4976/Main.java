package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;

public class Main extends IterativeRobot {

    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();

    Preferences preferences = Preferences.getInstance();

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
        Input.Encoder.DRIVE_RIGHT.reset();
    }

    @Override
    public void teleopPeriodic() {

        Output.Solenoid.CONTROLLER.periodic();
        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();
        System.out.println(Input.Encoder.DRIVE_RIGHT.getDistance());
    }
}
