package ca._4976.sub;

import ca._4976.io.Output;
import edu.wpi.first.wpilibj.PIDController;

import static ca._4976.io.Output.*;
import static ca._4976.io.Input.*;
import static ca._4976.io.Controller.*;

public class Shooter {

    PIDController pid = new PIDController(0, 0, 0, 0, Encoder.SHOOTER, Motor.SHOOTER);

    int state = 0;
    long waitTimeFlag;

    public void disabledInit() {

        state = 0;
        pid.disable();
        Motor.SHOOTER.set(0);
    }

    public void teleopPeriodic() {

        switch (state) {

            case 0:

                if (Primary.Button.B.isDownOnce()) state = 1;

                break;
            case 1:

                if (Digital.BALL_DETECTED.get()) {

                    pid.setSetpoint(6000);
                    pid.enable();
                    Output.Solenoid.INTAKE.set(false);
                    state = 2;

                } else state = 0;

                break;
            case 2:

                if (Primary.Button.B.isDownOnce() && Math.abs(pid.getError()) < 100) {

                    Motor.INTAKE_ROLLERS.set(1);
                    state = 3;
                }

                break;
            case 3:

                if (!Digital.BALL_DETECTED.get()) { waitTimeFlag = System.currentTimeMillis(); }

                break;
            case 4:

                if (System.currentTimeMillis() - waitTimeFlag < 500) {

                    pid.disable();
                    Motor.SHOOTER.set(0);
                    state = 0;

                } break;
        }

        if (Solenoid.INTAKE.get()) { pid.disable(); Motor.SHOOTER.set(0); }
    }
}
