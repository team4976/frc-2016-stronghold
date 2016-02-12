package ca._4976.io;

import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

public class Output {

    public enum Motor implements PIDOutput {

        DRIVE_LEFT(new CANTalon[] {new CANTalon(11), new CANTalon(12)}, 1.0),
        DRIVE_RIGHT(new CANTalon[] {new CANTalon(13), new CANTalon(14)}, 1.0),
        SHOOTER(new Object[] {new CANTalon(15), new Talon(0)}, 1.0),
        INTAKE_WHEELS(new Talon(2), 1.0),
        INTAKE_ROLLERS(new Talon(3), 1.0);

        Object[] motors;

        Motor(Object motor, double modifier) {}

        Motor(Object[] motors, double modifier) {}

        public void set(double speed) {

            for (Object i : motors) {

                //TODO add limit from watchdog

                if (i instanceof CANTalon) ((CANTalon) i).set(speed);

                else if (i instanceof Talon) ((Talon) i).set(speed);
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

        @Override public void pidWrite(double speed) { set(speed); }
    }

    public enum Solenoid {

        GEAR(0, 1),
        INTAKE(2, 3),
        HOOD(4, 5),
        CONTROLLER;

        DoubleSolenoid solenoid;
        Compressor compressor;

        boolean isExtended;

        long onTimmerStart;

        Solenoid() { compressor = new Compressor(20); }

        Solenoid(int extend, int retract) { solenoid = new DoubleSolenoid(20, extend, retract); }

        public void set(boolean extended) {

            isExtended = extended;
            solenoid.set(extended ? kForward : kReverse);
        }

        public boolean get() { return isExtended; }

        public void init() { if (this == Solenoid.CONTROLLER) compressor.setClosedLoopControl(true); }

        public void disabledInit() { if (this == Solenoid.CONTROLLER) compressor.setClosedLoopControl(false); }

        public void periodic() {

            if (this == Solenoid.CONTROLLER) {

                Solenoid.GEAR.periodic();
                Solenoid.INTAKE.periodic();
                Solenoid.HOOD.periodic();

            } else

                if (solenoid.get() == kOff) onTimmerStart = System.currentTimeMillis();

                else if (System.currentTimeMillis() - onTimmerStart > 20) solenoid.set(kOff);
        }
    }
}
