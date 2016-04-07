package ca._4976.io;

import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class Output {

    public enum Motor implements PIDOutput {

        DRIVE_LEFT(new Object[] {new CANTalon(11), new CANTalon(12)}, -1.0),
        DRIVE_RIGHT(new Object[] {new CANTalon(13), new CANTalon(14)}, -1.0),
        SHOOTER(new Object[]{new CANTalon(15), new Talon(0)}, -1.0),
        INTAKE_WHEELS(new Talon(2), -1.0),
        INTAKE_ROLLERS(new Talon(1), 1.0),
        SCALAR(new Talon(3), 1.0);

        Object[] motors;
        double modifier;

        long timeFlag = System.currentTimeMillis();

        Motor(Object motor, double modifier) {

            this.motors = new Object[]{motor};
            this.modifier = modifier;
        }

        Motor(Object[] motors, double modifier) {

            this.motors = motors;
            this.modifier = modifier;
        }

        public void setPIDSourceType(PIDSourceType pidSourceType) {

            for (Object i : motors) {

                if (i instanceof CANTalon) ((CANTalon) i).setPIDSourceType(pidSourceType);
            }
        }

        public void setFeedbackDevice(CANTalon.FeedbackDevice feedbackDevice) {

            for (Object i : motors) {

                if (i instanceof CANTalon) ((CANTalon) i).setFeedbackDevice(feedbackDevice);
            }
        }

        public PIDSourceType getPIDSourceType() {

            for (Object i : motors) {

                if (i instanceof CANTalon) return ((CANTalon) i).getPIDSourceType();
            }

            return null;
        }

        public void set(double speed) {

            System.out.println("Speed" + speed);

            if (Math.abs(speed) < 0.2) timeFlag = System.currentTimeMillis();

            for (Object i : motors) {

                //TODO add limit from watchdog

                if (i instanceof CANTalon) ((CANTalon) i).set(speed * modifier);

                else if (i instanceof Talon) ((Talon) i).set(speed * modifier);
            }
        }

        public void reset() {

            for (Object i : motors) {

                //TODO add limit from watchdog

                if (i instanceof CANTalon) ((CANTalon) i).reset();
            }
        }

        public double getOutputCurrent() {

            double current = 0;

            for (Object i : motors) {

                if (i instanceof CANTalon) current += ((CANTalon) i).getOutputCurrent();

                else return -1;
            }

            return current;
        }

        public double getOutputVoltage() {

            double voltage = 0;

            for (Object i : motors) {

                if (i instanceof CANTalon) voltage += ((CANTalon) i).getOutputVoltage();

                else return -1;
            }

            return voltage / motors.length;
        }

        public boolean hasStopped() { return timeFlag > 100; }

        @Override
        public void pidWrite(double speed) {
            set(speed);
        }

        public double getEncVelocity() {

            for (Object i : motors) {

                if (i instanceof CANTalon) return ((CANTalon) i).getEncVelocity();
            }

            return 0;
        }
    }

    public enum Solenoid {

        GEAR(0, 1),
        INTAKE(5, 4),
        CONTROLLER;

        DoubleSolenoid solenoid;
        Compressor compressor;

        boolean isExtended = false;

        long onTimerStart;

        Solenoid() {
            compressor = new Compressor(20);
        }

        Solenoid(int extend, int retract) {
            solenoid = new DoubleSolenoid(20, extend, retract);
        }

        public void set(boolean extended) {

            isExtended = extended;
            solenoid.set(extended ? kForward : kReverse);
        }

        public boolean get() {
            return isExtended;
        }

        public void init() {
            if (this == Solenoid.CONTROLLER) compressor.setClosedLoopControl(true);
        }

        public void disabledInit() {
            if (this == Solenoid.CONTROLLER) compressor.setClosedLoopControl(false);
        }

        public void periodic() {

            if (this == Solenoid.CONTROLLER) {

                Solenoid.GEAR.periodic();
                Solenoid.INTAKE.periodic();

            } else if (solenoid.get() == kOff) onTimerStart = System.currentTimeMillis();

            else if (System.currentTimeMillis() - onTimerStart > 500) solenoid.set(kOff);
        }
    }
}
