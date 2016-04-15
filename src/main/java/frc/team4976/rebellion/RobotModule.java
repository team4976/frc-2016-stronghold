package frc.team4976.rebellion;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import frc.team4976.rebellion.io.Controller;
import frc.team4976.rebellion.io.Input;
import frc.team4976.rebellion.sub.*;
import jaci.openrio.toast.lib.log.LogLevel;
import jaci.openrio.toast.lib.log.Logger;
import jaci.openrio.toast.lib.module.IterativeModule;

import static frc.team4976.rebellion.io.Input.*;
import static edu.wpi.first.wpilibj.PIDSourceType.*;

public class RobotModule extends IterativeModule {

    private static Logger logger;
    private static LogLevel info = new LogLevel("INFO");

    private DriveTrain drive;
    private Intake intake;
    private Shooter shooter;
    private Targeting targeting;

    private NetworkTable status = NetworkTable.getTable("Status");

    public static final boolean inSimulation = false;

    @Override public String getModuleName() {
        return "Rebellion";
    }

    @Override public String getModuleVersion() {
        return "0.0.2";
    }

    @Override public void robotInit() {

        logger = new Logger("Stronghold_TOAST", Logger.ATTR_DEFAULT);

        QuadraticEncoder.SHOOTER.setPIDSourceType(kRate);
        QuadraticEncoder.SHOOTER.setReverse(false);

        drive = new DriveTrain();
        intake = new Intake();
        shooter = new Shooter();
        targeting = new Targeting();

        drive.addTargeting(targeting);
        shooter.addTargeting(targeting);

        logger.log("Robot init completed successfully.", info);
    }

    @Override public void disabledInit() {

        logger.log("The robot has been disabled.", info);
        shooter.disabledInit();
        intake.disabledInit();
    }

    @Override public void teleopInit() { logger.log("The robot has been enabled in teleop.", info); }

    @Override public void autonomousInit() { logger.log("The robot has been enabled in auto.", info); }

    @Override public void testInit() { logger.log("The robot has been enabled in test.", info); }

    @Override public void teleopPeriodic() {

        status.putBoolean("Ball Detected", get(Switch.BALL_DETECT));
        status.putNumber("Shooter RPM", get(QuadraticEncoder.SHOOTER));

        drive.teleopPeriodic();
        intake.teleopPeriodic();
        shooter.teleopPeriodic();

        if (get(QuadraticEncoder.SHOOTER) > 10) {

            System.out.print("Shooter DATA (RPM, HAS GOAL) = {" + Math.round(get(QuadraticEncoder.SHOOTER)));
            System.out.println(" RPM, " + targeting.hasTarget() + "}");
        }
    }

    @Override public void autonomousPeriodic() {

        drive.autonomousPeriodic();
    }
}
