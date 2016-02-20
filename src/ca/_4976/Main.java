package ca._4976;

import ca._4976.sub.DriveTrain;
import ca._4976.sub.Shooter;
import ca._4976.sub.TurnAim;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import ca._4976.io.*;
public class Main extends IterativeRobot {

    Compressor compressor = new Compressor(20);
    DriveTrain driveTrain = new DriveTrain();
    TurnAim turnaim = new TurnAim();
    Shooter shooter = new Shooter();

    public void robotInit() {

        shooter.robotInit();
    }

    public void teleopInit() {
        compressor.setClosedLoopControl(true);
    }

    public void autonomousInit() { }

    public void testInit() { }

    public void teleopPeriodic() {
        driveTrain.teleopPeriodic();
        turnaim.teleopPeriodic();
        shooter.teleopPeriodic();
    }

    public void autonomousPeriodic() { }

    public void testPeriodic() { }
}
