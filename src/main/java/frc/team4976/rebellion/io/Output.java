package frc.team4976.rebellion.io;

import edu.wpi.first.wpilibj.*;
import frc.team4976.rebellion.RobotModule;
import jaci.openrio.module.blackbox.BlackBox;
import jaci.openrio.module.blackbox.BlackBoxContext;
import jaci.openrio.toast.lib.registry.Registrar;

import javax.naming.Context;

public class Output {

    private static BlackBoxContext motorsBlackBox = BlackBox.context("Motors");
    private static BlackBoxContext pneumaticsBlackBox = BlackBox.context("Pneumatics");


    public static void set(Motor motor, double output) { motor.set(output); }

    public static void set(Pneumatic solenoid, boolean extended) { solenoid.set(extended); }

    public static boolean get(Pneumatic solenoid) { return  solenoid.get(); }

    public static Double get(Motor motor) { return motor.get(); }


    public enum Motor implements PIDOutput {
        DRIVE_LEFT(new Object[] {Registrar.canTalon(11), Registrar.canTalon(12)}, 1d),
        DRIVE_RIGHT(new Object[] {Registrar.canTalon(13), Registrar.canTalon(14)}, 1d),
        SHOOTER(new Object[] {Registrar.canTalon(15), Registrar.victorSP(0)}, 1d),
        INTAKE_ROLLERS(Registrar.victorSP(1), 1d),
        INTAKE_WHEELS(Registrar.victorSP(2), 1d);

        Object[] motors;
        double modifier;

        Motor(Object[] motors, double modifier) {

            motorsBlackBox.add(this.toString(), this::get);
            this.motors = motors;
            this.modifier = modifier;
        }

        Motor(Object motors, double modifier) {

            motorsBlackBox.add(this.toString(), this::get);
            this.motors = new Object[] {motors};
            this.modifier = modifier;
        }

        double getEncPosition() {

            for (Object i : motors) {

                if (i instanceof CANTalon) return ((CANTalon) i).getEncPosition();
            }

            return 0;
        }

        void setFeedbackDevice(CANTalon.FeedbackDevice device) {

            for (Object i : motors) {

                if (i instanceof CANTalon) ((CANTalon) i).setFeedbackDevice(device);
            }

        }

        double getEncVelocity() {

            for (Object i : motors) {

                if (i instanceof CANTalon) return ((CANTalon) i).getEncVelocity();
            }

            return 0;
        }

        private void set(double output) {

            motorsBlackBox.tick();

            for (Object i : motors) {

                if (i instanceof CANTalon) ((CANTalon) i).set(output * modifier);

                else if (i instanceof VictorSP) ((VictorSP) i).set(output * modifier);
            }
        }

        private double get() {

            for (Object i : motors) {

                if (i instanceof CANTalon) return ((CANTalon) i).get();

                else if (i instanceof VictorSP) return ((VictorSP) i).get();
            }

            return 0;
        }

        @Override public void pidWrite(double output) { set(output); }
    }

    public enum Pneumatic {
        PCM(20),
        INTAKE(5, 4),
        SHIFTER(1, 0),
        SCALER(2, 3);

        DoubleSolenoid solenoid;
        Compressor compressor;

        Pneumatic(int id) { compressor = new Compressor(0); }

        Pneumatic(int extendedPort, int retractedPort) {

            if (RobotModule.inSimulation) solenoid = new DoubleSolenoid(0, extendedPort, retractedPort);

            else solenoid = new DoubleSolenoid(20, extendedPort, retractedPort);
        }

        private void set(boolean extended) {

            if (extended) solenoid.set(DoubleSolenoid.Value.kForward);

            else solenoid.set(DoubleSolenoid.Value.kReverse);
        }

        private boolean get() { return solenoid.get() == DoubleSolenoid.Value.kForward; }
    }
}
