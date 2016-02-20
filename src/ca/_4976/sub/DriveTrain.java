package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Output;

public class DriveTrain {

    public void teleopPeriodic() {
        double leftTrigger = Controller.Primary.Trigger.LEFT.value();
        double rightTrigger = Controller.Primary.Trigger.RIGHT.value();

        double steering = Controller.Primary.Stick.LEFT.horizontal();
        steering = (steering > 0 ? Math.pow(steering, 2) : -Math.pow(steering, 2));
        steering = (Math.abs(steering) < 0.1 ? 0 : steering);

        double power = rightTrigger - leftTrigger;

        forward(power - steering);

        if (Controller.Primary.DPad.SOUTH.isDownOnce())
            Output.Solenoid.GEAR.set(true);
        if (Controller.Primary.DPad.NORTH.isDownOnce())
            Output.Solenoid.GEAR.set(false);
    }

    public void forward(double power) {
        Output.Motor.DRIVE_LEFT.set(-power);
        Output.Motor.DRIVE_RIGHT.set(power);
    }

    public void backward(double power) {
        forward(-power);
    }

    public void turnLeft(double power) {
        Output.Motor.DRIVE_LEFT.set(-power);
        Output.Motor.DRIVE_RIGHT.set(-power);
    }

    public void turnRight(double power) {
        turnLeft(-power);
    }

    public void stop() {
        forward(0);
    }

}