package ca._4976.sub;

import ca._4976.io.Output;

import static ca._4976.io.Output.*;
import static ca._4976.io.Input.*;
import static ca._4976.io.Controller.*;

public class Intake {

    int state = 0;

    public void disabledInit() {

        state = 0;
        Motor.INTAKE_ROLLERS.set(0);
    }

    public void teleopPeriodic() {

        switch (state) {

            default:
                break;

            case 0:

                if (Primary.Button.A.isDownOnce())

                    if (!Digital.BALL_DETECTED.get()) state = 2;

                break;
            case 1:

                if (Digital.BALL_DETECTED.get()) {


                    Motor.INTAKE_ROLLERS.set(0);
                    state = 0;

                } else Motor.INTAKE_ROLLERS.set(1);

                break;
            case 2:

                if (!Digital.BALL_DETECTED.get()) {

                    Solenoid.INTAKE.set(false);
                    state = 1;

                } else state = 0;
        }

        if (Math.abs(Secondary.Trigger.LEFT.value() - Secondary.Trigger.RIGHT.value()) > 0.1) {

            state = -1;
            Motor.INTAKE_ROLLERS.set(Secondary.Trigger.RIGHT.value() - Secondary.Trigger.LEFT.value());

        }

        if (Math.abs(Secondary.Stick.RIGHT.vertical()) > 0.1) {

            state = -1;
            Motor.INTAKE_WHEELS.set(Secondary.Stick.RIGHT.vertical());

        }

        if (Math.abs(Secondary.Trigger.LEFT.value() - Secondary.Trigger.RIGHT.value()) < 0.1
                && Math.abs(Secondary.Stick.RIGHT.vertical()) < 0.1
                && state < 0) {

            state = 0;
            Motor.INTAKE_WHEELS.set(0);
            Motor.INTAKE_ROLLERS.set(0);
        }

        if (Secondary.Button.A.isDownOnce()) Solenoid.INTAKE.set(!Solenoid.INTAKE.get());
    }
}
