package ca._4976;

import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain drive = new DriveTrain();
    Intake intake = new Intake();
    Shooter shooter = new Shooter();

    @Override public void disabledInit() {

        drive.disabledInit();
        shooter.disabledInit();
    }

    @Override public void teleopPeriodic() {

        drive.teleopPeriodic();
        intake.teleopPeriodic();
        drive.teleopPeriodic();
    }
}
