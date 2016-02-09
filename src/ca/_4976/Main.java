package ca._4976;

import ca._4976.sub.DriveTrain;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Main extends IterativeRobot {

    DriveTrain drive = new DriveTrain();

    @Override public void robotInit() { }

    @Override public void disabledInit() {

        drive.disabledInit();
    }

    @Override public void autonomousInit() { }

    @Override public void teleopInit() { }

    @Override public void testInit() { }

    @Override public void disabledPeriodic() { }

    @Override public void autonomousPeriodic() { }

    @Override public void teleopPeriodic() {

        drive.teleopPeriodic();
    }

    @Override public void testPeriodic() { }

}
