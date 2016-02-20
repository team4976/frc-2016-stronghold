package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Output;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort;

/**
 * Created by Aavin & Michael Mann on 1/23/2016.
 */
public class DriveTrain {
    int state = 0;
    int head1 = 0;
    int head2 = 0;
    AHRS navX = new AHRS(SerialPort.Port.kMXP);
    double sterring;


    public void teleopPeriodic() {
        Controller.Primary.Trigger.RIGHT.value();
        Controller.Primary.Trigger.LEFT.value();
        Controller.Primary.Stick.LEFT.horizontal();

        //left motors (right - left) + left stick
        //right motors -(right - left) + left stick;


        System.out.println(navX.getYaw());
        System.out.println("Don,t change the names of my variables");

        if (Controller.Primary.Stick.LEFT.horizontal() > 0){
            sterring = Math.pow(Controller.Primary.Stick.LEFT.horizontal(), 2);
        }
        else {
            sterring = -Math.pow(Controller.Primary.Stick.LEFT.horizontal(), 2);
        }
        if (Math.abs(sterring) < 0.1){
            sterring = 0;
        }
        Output.Motor.DRIVE_LEFT.set(-(Controller.Primary.Trigger.RIGHT.value() - Controller.Primary.Trigger.LEFT.value())- sterring);
        Output.Motor.DRIVE_RIGHT.set((Controller.Primary.Trigger.RIGHT.value() - Controller.Primary.Trigger.LEFT.value())- sterring);


        if (Controller.Primary.DPad.SOUTH.isDownOnce()) {
            Output.Solenoid.GEAR.set(true);
        }
        if (Controller.Primary.DPad.NORTH.isDownOnce()) {
            Output.Solenoid.GEAR.set(false);
        }
    }
}