package ca._4976.io;

import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class Output {

    public enum Motor implements PIDOutput {

        DRIVE_LEFT(getDriveLeft(), 1.0),
        DRIVE_RIGHT(getDriveRight(), 1.0),
        SHOOTER(getShooter(), 1.0),
        INTAKE_WHEELS(getIntakeWheels(), 1.0),
        INTAKE_ROLLERS(getIntakeRollers(), 1.0);

        Object[] motors;
        double modifier;

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
                if (i instanceof CANTalon)
                    ((CANTalon) i).setPIDSourceType(pidSourceType);
            }
        }

        public PIDSourceType getPIDSourceType() {

            for (Object i : motors) {
                if (i instanceof CANTalon)
                    return ((CANTalon) i).getPIDSourceType();
            }

            return null;
        }

        public void set(double speed) {

            for (Object i : motors) {

                if (i instanceof CANTalon)
                    ((CANTalon) i).set(speed * modifier);

                else if (i instanceof Talon)
                    ((Talon) i).set(speed * modifier);
            }

        }

        public double getOutputCurrent() {

            double current = 0;

            for (Object i : motors) {
                if (i instanceof CANTalon)
                    current += ((CANTalon) i).getOutputCurrent();
                else return -1;
            }

            return current;
        }

        public double getOutputVoltage() {

            double voltage = 0;

            for (Object i : motors) {
                if (i instanceof CANTalon)
                    voltage += ((CANTalon) i).getOutputVoltage();
                else return -1;
            }

            return voltage / motors.length;
        }

        @Override
        public void pidWrite(double speed) {
            set(speed);
        }

        public double getEncVelocity() {

            for (Object i : motors) {
                if (i instanceof CANTalon)
                    return ((CANTalon) i).getEncVelocity();
            }

            return 0;
        }

        public static Object[] getDriveLeft() {
            return new Talon[]{new Talon(3)};
        }

        public static Object[] getDriveRight() {
            return new Talon[]{new Talon(1)};
        }

        public static Object[] getShooter() {
            return new Object[]{new CANTalon(11), new Talon(2)};
        }

        public static Object getIntakeWheels() {
            return new Talon(4);
        }

        public static Object getIntakeRollers() {
            return new Talon(5);
        }
    }

    public enum Solenoid {

        GEAR(0, 1),
        INTAKE(3, 2),
        HOOD(4, 5),
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
            if (this == Solenoid.CONTROLLER)
                compressor.setClosedLoopControl(true);
        }

        public void disabledInit() {
            if (this == Solenoid.CONTROLLER)
                compressor.setClosedLoopControl(false);
        }

        public void periodic() {

            if (this == Solenoid.CONTROLLER) {

                Solenoid.GEAR.periodic();
                Solenoid.INTAKE.periodic();
                Solenoid.HOOD.periodic();

            } else if (solenoid.get() == kOff)
                onTimerStart = System.currentTimeMillis();

            else if (System.currentTimeMillis() - onTimerStart > 60)
                solenoid.set(kOff);
        }
    }
}
