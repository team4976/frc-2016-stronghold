package ca._4976.sub;

import ca._4976.io.Input;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

import static ca._4976.io.Controller.*;
import static ca._4976.io.Output.*;

public class DriveTrain {

    int turnCount = 0;
    int state = 0;

    PIDController turnPID = new PIDController(0, 0, 0, 0,
            Input.MXP.NAV_X, new MotorPair(Motor.DRIVE_LEFT, Motor.DRIVE_RIGHT, false, false));

    PIDController drivePID = new PIDController(0, 0, 0, 0,
            Input.MXP.NAV_X, new MotorPair(Motor.DRIVE_LEFT, Motor.DRIVE_RIGHT, false, true));

    public void teleopPeriodic() {

        if (Primary.DPad.EAST.isDownOnce()) turnCount++;
        else if (Primary.DPad.WEST.isDownOnce()) turnCount--;

        if (turnCount == 0) {

            double steering = Primary.Stick.LEFT.horizontal();

            steering = steering < 0 ? Math.pow(steering, 2) : -Math.pow(steering, 2);
            steering = Math.abs(steering) > 0.08 ? steering : 0;

            double forward = Primary.Trigger.RIGHT.value() - Primary.Trigger.LEFT.value();

            Motor.DRIVE_LEFT.set(-forward + steering);
            Motor.DRIVE_RIGHT.set(forward + steering);

            if (Primary.DPad.NORTH.isDownOnce()) Solenoid.GEAR.set(false);
            else if (Primary.DPad.SOUTH.isDownOnce()) Solenoid.GEAR.set(true);

        } else if (turnCount < 0) if (autoTurnLeft(90)) turnCount++;

        else if (turnCount > 0) if (autoTurnRight(90)) turnCount--;
    }

    public void disabledInit() {

        turnPID.disable();
        drivePID.disable();
        turnCount = 0;
        state = 0;
    }

    public boolean autoTurnLeft(int degree) {

        switch (state) {

            case 0:
                Input.MXP.NAV_X.reset();
                turnPID.setSetpoint(-degree);
                turnPID.enable();
                state++;
                return false;
            case 1:
                if (Input.Encoder.DRIVE_LEFT.hasStopped() && turnPID.getError() < 2) {

                    turnPID.disable();
                    state = 0;
                    return true;

                } else return false;
        }

        return false;
    }

    public boolean autoTurnRight(int degree) {

        switch (state) {

            case 0:
                Input.MXP.NAV_X.reset();
                turnPID.setSetpoint(degree);
                turnPID.enable();
                state++;
                return false;
            case 1:
                if (Input.Encoder.DRIVE_LEFT.hasStopped() && turnPID.getError() < 2) {

                    turnPID.disable();
                    state = 0;
                    return true;

                } else return false;
        }

        return false;
    }

    public boolean forward(double meters) {

        switch (state) {

            case 0:
                Input.MXP.NAV_X.reset();
                drivePID.setSetpoint(meters);
                drivePID.enable();
                state++;
                return false;
            case 1:
                if (Input.Encoder.DRIVE_LEFT.hasStopped() && turnPID.getError() < 2) {

                    drivePID.disable();
                    state = 0;
                    return true;

                } else return false;
        }

        return false;
    }

    public class MotorPair implements PIDOutput {

        Motor motor1;
        Motor motor2;

        boolean isMotor1Revered = false;
        boolean isMotor2Revered = false;

        public MotorPair(Motor motor1, Motor motor2, boolean isMotor1Revered, boolean isMotor2Revered) {

            this.motor1 = motor1;
            this.motor2 = motor2;
            this.isMotor1Revered = isMotor1Revered;
            this.isMotor2Revered = isMotor2Revered;
        }

        @Override
        public void pidWrite(double output) {

            motor1.set(isMotor1Revered ? -output : output);
            motor2.set(isMotor2Revered ? -output : output);
        }
    }
}