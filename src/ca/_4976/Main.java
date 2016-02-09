package ca._4976;

import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.sub.DriveTrain;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Main extends IterativeRobot {

    DriveTrain drive = new DriveTrain();
    PIDController pid = new PIDController(0,0,0,0, Input.Encoder.SHOOTER, Output.Motor.SHOOTER);
    NetworkTable table = NetworkTable.getTable("pid");


    @Override public void robotInit() {

        Input.Encoder.SHOOTER.setReversed(true);
        table.putNumber("p", 0);
        table.putNumber("i", 0);
        table.putNumber("d", 0);
        table.putNumber("setpoint", 0);
    }

    @Override public void disabledInit() {

        drive.disabledInit();
    }

    @Override public void autonomousInit() { }

    @Override public void teleopInit() {

        pid.setPID(table.getNumber("p", 0),table.getNumber("i", 0),table.getNumber("d", 0));
        pid.setSetpoint(table.getNumber("setpoint", 0));
    }

    @Override public void testInit() { }

    @Override public void disabledPeriodic() { }

    @Override public void autonomousPeriodic() { }


    @Override public void teleopPeriodic() {


    }

    @Override public void testPeriodic() { }

}
