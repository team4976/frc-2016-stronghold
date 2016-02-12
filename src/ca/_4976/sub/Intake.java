package ca._4976.sub;

import static ca._4976.io.Output.*;
import static ca._4976.io.Input.*;
import static ca._4976.io.Controller.*;

public class Intake {

    int state = 0;

    public void teleopPeriodic() {

        switch (state) {

            case 0:

                if (Primary.Button.A.isDownOnce())

                    if (Solenoid.INTAKE.get() && !Digital.BALL_DETECTED.get()) state = 1;


                    else if (!Solenoid.INTAKE.get()) state = 0;

                if (Primary.Button.B.isDownOnce() && !Solenoid.INTAKE.get()) Solenoid.INTAKE.set(true);

                break;
            case 1:

                if (Digital.BALL_DETECTED.get()) {

                    Motor.INTAKE_ROLLERS.set(0);
                    state = 0;

                } else Motor.INTAKE_ROLLERS.set(1);

                break;
            case 2:

                if (!Digital.BALL_DETECTED.get()) {

                    Solenoid.INTAKE.set(true);
                    state = 1;

                } else state = 0;
        }
    }
}
