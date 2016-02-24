package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import ca._4976.io.Utility;

public class DriveTrain {

    int autoAimState = AUTO_AIM_OFF;

    public static final int AUTO_AIM_OFF = 0;
    public static final int AUTO_AIM_LEFT = 1;
    public static final int AUTO_AIM_RIGHT = 2;

    public void teleopPeriodic() {

        if (autoAimState > AUTO_AIM_OFF) {
            Utility.startDelay(2000, "AutoAimTeleop");

            if ((Input.Digital.IR_L.get() && Input.Digital.IR_R.get()) || Utility.checkDelay("AutoAimTeleop")) {
                autoAimState = AUTO_AIM_OFF;
            } else {
                if (autoAimState == AUTO_AIM_LEFT)
                    turnLeft(0.2);
                else if (autoAimState == AUTO_AIM_RIGHT)
                    turnRight(0.2);
            }
        } else {
            double leftTrigger = Controller.Primary.Trigger.LEFT.value();
            double rightTrigger = Controller.Primary.Trigger.RIGHT.value();

            double steering = Controller.Primary.Stick.LEFT.horizontal();
            steering = (steering < 0 ? Math.pow(steering, 2) : -Math.pow(steering, 2));
            steering = (Math.abs(steering) > 0.1 ? steering : 0);

            double power = rightTrigger - leftTrigger;

            Output.Motor.DRIVE_LEFT.set(power + steering);
            Output.Motor.DRIVE_RIGHT.set(-power + steering);

            if (Controller.Primary.DPad.SOUTH.isDownOnce())
                Output.Solenoid.GEAR.set(true);
            if (Controller.Primary.DPad.NORTH.isDownOnce())
                Output.Solenoid.GEAR.set(false);
            if (Controller.Primary.Button.LEFT_BUMPER.isDownOnce()) {
                autoAimState = AUTO_AIM_LEFT;
                Utility.removeDelay("AutoAimTeleop");
            }
            if (Controller.Primary.Button.RIGHT_BUMPER.isDownOnce()) {
                autoAimState = AUTO_AIM_RIGHT;
                Utility.removeDelay("AutoAimTeleop");
            }
        }
    }

    public void forward(double power) {
        Output.Motor.DRIVE_LEFT.set(power);
        Output.Motor.DRIVE_RIGHT.set(-power);
    }

    public void backward(double power) {
        forward(-power);
    }

    public void turnLeft(double power) {
        Output.Motor.DRIVE_LEFT.set(power);
        Output.Motor.DRIVE_RIGHT.set(power);
    }

    public void turnRight(double power) {
        turnLeft(-power);
    }

    public void stop() {
        forward(0);
    }

}