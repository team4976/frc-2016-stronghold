package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * Created by Aavin & Michael Mann on 1/23/2016.
 */
public class TurnAim {
    int state = 0;

    public void teleopPeriodic() {

        if (state == 0) {
                    if (Controller.Primary.DPad.WEST.isDownOnce() == true) {
                        state = 1;
                    }
                    if (Controller.Primary.Button.LEFT_STICK.isDown() == true) {
                        state = 2;
            }
        }
        if (state == 1) {
            if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                Output.Motor.DRIVE_LEFT.set(0);
                Output.Motor.DRIVE_RIGHT.set(0);
                System.out.println("it worked");
                state = 0;
            } else {
                Output.Motor.DRIVE_LEFT.set(-0.4);
                Output.Motor.DRIVE_RIGHT.set(-0.4);
            }

        }
        if (state == 2) {
            if (Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) {
                Output.Motor.DRIVE_LEFT.set(0);
                Output.Motor.DRIVE_RIGHT.set(0);
                System.out.println("it worked");
                state = 0;
            } else {
                Output.Motor.DRIVE_LEFT.set(0.4);
                Output.Motor.DRIVE_RIGHT.set(0.4);
            }

        }
    }
}
