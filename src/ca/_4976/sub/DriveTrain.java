package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Output;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

public class DriveTrain {
    int state = 0;
    int head1 = 0;
    int head2 = 0;

    AHRS navX = new AHRS(SerialPort.Port.kMXP);

    double steering;

    public void teleopPeriodic() {
        Controller.Primary.Trigger.RIGHT.value();
        Controller.Primary.Trigger.LEFT.value();
        Controller.Primary.Stick.LEFT.horizontal();

        //left motors (right - left) + left stick
        //right motors -(right - left) + left stick;

        System.out.println(navX.getYaw());

        if (Controller.Primary.Stick.LEFT.horizontal() > 0) {
            steering = Math.pow(Controller.Primary.Stick.LEFT.horizontal(), 2);
        } else {
            steering = -Math.pow(Controller.Primary.Stick.LEFT.horizontal(), 2);
        }
        if (Math.abs(steering) < 0.1) {
            steering = 0;
        }
        Output.Motor.DRIVE_LEFT.set(-(Controller.Primary.Trigger.RIGHT.value() - Controller.Primary.Trigger.LEFT.value()) - steering);
        Output.Motor.DRIVE_RIGHT.set((Controller.Primary.Trigger.RIGHT.value() - Controller.Primary.Trigger.LEFT.value()) - steering);

        if (Controller.Primary.DPad.SOUTH.isDownOnce()) {
            Output.Solenoid.GEAR.set(true);
        }
        if (Controller.Primary.DPad.NORTH.isDownOnce()) {
            Output.Solenoid.GEAR.set(false);
        }
    }
}