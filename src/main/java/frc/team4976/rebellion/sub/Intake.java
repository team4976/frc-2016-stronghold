package frc.team4976.rebellion.sub;

import static frc.team4976.rebellion.io.Controller.*;
import static frc.team4976.rebellion.io.Input.*;
import static frc.team4976.rebellion.io.Output.*;

public class Intake {

    private int intakeState = 0;

    public void disabledInit() {

        set(Motor.INTAKE_WHEELS, 0);
        set(Motor.INTAKE_ROLLERS, 0);
        intakeState = 0;
    }

    public void teleopPeriodic() {

        if (OPERATOR.getOnce(Button.A)) set(Pneumatic.INTAKE, !get(Pneumatic.INTAKE));

        if (Math.abs(OPERATOR.get(Axis.RIGHT_TRIGGER) - OPERATOR.get(Axis.LEFT_TRIGGER)) > 0.1) {

            set(Motor.INTAKE_ROLLERS, (OPERATOR.get(Axis.RIGHT_TRIGGER) - OPERATOR.get(Axis.LEFT_TRIGGER)));
            intakeState = -1;

        } else if (Math.abs(OPERATOR.get(Axis.RIGHT_VERTICAL)) > 0.1) {

            set(Motor.INTAKE_WHEELS, (OPERATOR.get(Axis.RIGHT_VERTICAL)));
            intakeState = -1;

        } else if (intakeState == -1) {

            set(Motor.INTAKE_WHEELS, 0);
            set(Motor.INTAKE_ROLLERS, 0);
            intakeState = 0;
        }

        switch (intakeState) {

            case 0:

                if (DRIVER.get(Button.A)) intakeState++;

                break;
            case 1:

                if (!get(Switch.BALL_DETECT)) {

                    intakeState++;
                    set(Motor.INTAKE_ROLLERS, 1);
                    set(Pneumatic.INTAKE, false);

                } else intakeState = 0;

                break;
            case 2:
                if (get(Switch.BALL_DETECT)) {

                    intakeState = 0;
                    set(Motor.INTAKE_ROLLERS, 0);

                } break;
        }
    }
}
