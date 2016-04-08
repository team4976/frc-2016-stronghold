package ca._4976;

import static ca._4976.io.Input.*;
import static ca._4976.io.Output.*;

import ca._4976.sub.*;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Main extends IterativeRobot {

    private DriveTrain drive = new DriveTrain();
    private Intake intake = new Intake();
    private Shooter shooter = new Shooter();
    private Targeting targeting = new Targeting();
    private Autonomous autonomous = new Autonomous();
    private PowerDistributionPanel panel = new PowerDistributionPanel(10);

    @Override public void robotInit() {

        Encoder.SHOOTER.setReversed(true);
        shooter.addTargetingSubsystem(targeting);
        drive.addTargetingSubsystem(targeting);
        autonomous.addDrive(drive);
        autonomous.addShooter(shooter);
        autonomous.addTargeting(targeting);
        panel.clearStickyFaults();
    }

    @Override public void disabledInit() {

        drive.disabledInit();
        intake.disabledInit();
        shooter.disabledInit();
        Solenoid.CONTROLLER.disabledInit();
    }

    @Override public void teleopInit() {

        drive.disabledInit();

        Solenoid.CONTROLLER.init();
        Encoder.DRIVE_RIGHT.reset();
    }

    @Override public void teleopPeriodic() {

        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();

        Solenoid.CONTROLLER.periodic();

        if (Encoder.SHOOTER.getVelocity() > 100) System.out.println("Shooter RPM: " + (int) Encoder.SHOOTER.getVelocity());
    }

    @Override public void autonomousInit() {

        autonomous.autonomousInit();
    }

    @Override public void autonomousPeriodic() {

        drive.autonomousPeriodic();
        autonomous.autonomousPeriodic();
    }
}