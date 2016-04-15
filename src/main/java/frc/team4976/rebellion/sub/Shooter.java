package frc.team4976.rebellion.sub;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Preferences;

import static frc.team4976.rebellion.io.Controller.*;
import static frc.team4976.rebellion.io.Output.*;
import static frc.team4976.rebellion.io.Input.*;

public class Shooter {

    private Preferences preferences = Preferences.getInstance();
    private Targeting targeting;

    private long timeout = System.currentTimeMillis();

    private PIDController pidController = new PIDController(
            preferences.getDouble("S_P", 0),
            preferences.getDouble("S_I", 0),
            preferences.getDouble("S_D", 0),
            QuadraticEncoder.SHOOTER,
            Motor.SHOOTER
    );

    private int shooterState = 0;

    private boolean lastBallState = false;

    private long shooterTimeFlag = System.currentTimeMillis();

    public void addTargeting(Targeting targeting) { this.targeting = targeting; }

    public void disabledInit() {

        DRIVER.setRumble(0);
        shooterState = 0;
        pidController.disable();
        set(Motor.SHOOTER, 0);
    }

    public void teleopPeriodic() {

        if (Math.abs(OPERATOR.get(Axis.LEFT_VERTICAL)) > 0.1) {

            pidController.disable();
            set(Motor.SHOOTER, (OPERATOR.get(Axis.LEFT_VERTICAL)));
            DRIVER.setRumble(0.0f);
            shooterState = -1;

        } else if (shooterState == -1) {

            set(Motor.SHOOTER, 0);
            shooterState = 0;
        }

        if (DRIVER.get(Button.X) || OPERATOR.get(Button.X)) {

            shooterState = 0;
            pidController.disable();
            set(Motor.SHOOTER, 0);
            set(Motor.INTAKE_ROLLERS, 0);
            DRIVER.setRumble(0.0f);
        }

        switch (shooterState) {

            case 0:

                if (DRIVER.get(Button.B)) {

                    shooterState++;
                    set(Pneumatic.INTAKE, true);
                    pidController.setPID(
                            preferences.getDouble("S_P", 0),
                            preferences.getDouble("S_I", 0),
                            preferences.getDouble("S_D", 0)
                    );
                    pidController.reset();
                    pidController.setSetpoint(-preferences.getDouble("S_RPM", 0));
                    pidController.enable();


                } else if (OPERATOR.get(Button.B)) {

                    shooterState++;
                    pidController.setPID(
                            preferences.getDouble("S_P", 0),
                            preferences.getDouble("S_I", 0),
                            preferences.getDouble("S_D", 0)
                    );
                    pidController.reset();
                    pidController.setSetpoint(-preferences.getDouble("S_RPM", 0));
                    pidController.enable();
                }

                break;
            case 1:

                if (Math.abs(pidController.getError()) < 40) {

                    if (System.currentTimeMillis() - timeout > 100 && targeting.onTarget()) DRIVER.setRumble(0.7f);

                    else DRIVER.setRumble(0.7f);

                } else {

                    timeout = System.currentTimeMillis();
                    DRIVER.setRumble(0.0f);
                }

                if (DRIVER.get(Button.B) && Math.abs(pidController.getError()) < 40) {

                    shooterState++;
                    DRIVER.setRumble(0.0f);
                    set(Motor.INTAKE_ROLLERS, 1);

                } break;
            case 2:

                if (lastBallState && !get(Switch.BALL_DETECT)) {

                    lastBallState = false;
                    shooterTimeFlag = System.currentTimeMillis();
                    shooterState++;

                } else lastBallState = get(Switch.BALL_DETECT);

                break;
            case 3:
                if (System.currentTimeMillis() - shooterTimeFlag > 500) {

                    pidController.disable();
                    set(Motor.SHOOTER, 0);
                    set(Motor.INTAKE_ROLLERS, 0);
                    shooterState = 0;

                } break;
        }
    }
}
