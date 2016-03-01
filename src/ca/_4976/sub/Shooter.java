package ca._4976.sub;

import ca._4976.io.Output;
import ca._4976.io.Variables;
import edu.wpi.first.wpilibj.PIDController;

import java.awt.*;

import static ca._4976.io.Output.*;
import static ca._4976.io.Input.*;
import static ca._4976.io.Controller.*;

public class Shooter {

    PIDController pid = new PIDController(1e-10, 0, 0.00, 0, Encoder.SHOOTER, Motor.SHOOTER);

    int state = 0;
    long waitTimeFlag;

    public void disabledInit() {

        pid.reset();
        pid.setPID(Variables.getNumber("P", 0), Variables.getNumber("I", 0), Variables.getNumber("D", 0));
        state = 0;
        pid.disable();
        Motor.SHOOTER.set(0);
    }

    public void cock() {

        state = 0;
        pid.enable();
        pid.setSetpoint(Variables.getNumber("Shooter_RPM", 0));
        Solenoid.INTAKE.set(true);
    }

    public void disablePID() {

        pid.disable();
        pid.reset();
        Motor.SHOOTER.set(0);
    }

    public boolean shoot() {

        switch (state) {

            case 0:

                if (Math.abs(pid.getError()) < 80) {

                    Motor.INTAKE_ROLLERS.set(1);
                    state++;

                } return false;
            case 1:

                if (!Digital.BALL_DETECTED.get()) {

                    waitTimeFlag = System.currentTimeMillis();
                    state++;

                } return false;

            case 2:

                if (System.currentTimeMillis() - waitTimeFlag > 500) {

                    return true;

                } return false;
        }

        return false;
    }

    public void teleopPeriodic() {

        switch (state) {

            default:
                break;

            case 0:

                if (Primary.Button.B.isDownOnce()) state = 1;

                break;
            case 1:

                if (Digital.BALL_DETECTED.get()) {

                    pid.setSetpoint(-Variables.getNumber("Shooter_RPM", 0));
                    pid.enable();
                    Output.Solenoid.INTAKE.set(true);
                    state = 2;

                } else state = 0;

                break;
            case 2:

                if (Primary.Button.B.isDownOnce()) {

                    Motor.INTAKE_ROLLERS.set(1);
                    state = 3;

                } break;
            case 3:

                if (!Digital.BALL_DETECTED.get()) {

                    waitTimeFlag = System.currentTimeMillis();
                    state = 4;

                } break;
            case 4:

                if (System.currentTimeMillis() - waitTimeFlag > 500) {

                    pid.disable();
                    pid.reset();
                    Motor.SHOOTER.set(0);
                    Motor.INTAKE_ROLLERS.set(0);
                    state = 0;

                }
                break;
        }

        if (Math.abs(Secondary.Stick.LEFT.vertical()) > 0.2) {

            state = -1;
            pid.disable();
            Motor.SHOOTER.set(Secondary.Stick.LEFT.vertical());

        } else if (state == -1) {

            state = 0;
            Motor.SHOOTER.set(0);
        }

        if (Secondary.Button.X.isDownOnce()) {

            state = 0;
            Motor.SHOOTER.set(0);
        }
    }
}
