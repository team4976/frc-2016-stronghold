package frc.team4976.rebellion.io;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import frc.team4976.rebellion.RobotModule;
import jaci.openrio.toast.lib.registry.Registrar;

import static frc.team4976.rebellion.io.Output.*;
import static edu.wpi.first.wpilibj.PIDSourceType.*;

public class Input {

    public static boolean get(Switch input) { return input.get(); }

    public static double get(QuadraticEncoder encoder) { return encoder.get(); }

    public static double get(MXP mxp) { return mxp.get(); }

    public static boolean hasStopped() { return MXP.NavX.hasStopped(); }

    public enum Switch {
        BALL_DETECT(0, false);

        DigitalInput input;
        boolean inverted = false;

        Switch(int pin, boolean inverted) {

            input = Registrar.digitalInput(pin);
            this.inverted = inverted;
        }

        private boolean get() {

            return input.get() == inverted; }
    }

    public enum QuadraticEncoder implements PIDSource {
        DRIVE(2, 3, 0.01),
        SHOOTER(Output.Motor.SHOOTER, 6.82 * 2);

        Object encoder;
        boolean isReversed = false;
        double scaler;
        PIDSourceType pidSourceType = kDisplacement;

        QuadraticEncoder(int a, int b, double dpp) {

            if (!RobotModule.inSimulation) {

                encoder = new Encoder(a, b);
                ((Encoder) encoder).setDistancePerPulse(dpp);
            }
        }

        QuadraticEncoder(Motor motor, double scaler) {

            this.scaler = scaler;
            motor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
            if (!RobotModule.inSimulation) encoder = motor;
        }

        public void setReverse(boolean reverse) {

            if (encoder instanceof Encoder) ((Encoder) encoder).setReverseDirection(reverse);

            isReversed = reverse;
        }

        private double get() {

            if (encoder instanceof Encoder)

                switch (((Encoder) encoder).getPIDSourceType()) {

                    case kDisplacement: return ((Encoder) encoder).getDistance();

                    case kRate: return ((Encoder) encoder).getRate();

                    default: return ((Encoder) encoder).getDistance();
                }

            else if (encoder instanceof Motor) {

                switch (pidSourceType) {

                    case kDisplacement: return ((Motor) encoder).getEncPosition() / scaler * (isReversed ? -1 : 1);

                    case kRate: return ((Motor) encoder).getEncVelocity() / scaler * (isReversed ? -1 : 1);

                    default: return ((Motor) encoder).getEncPosition() / scaler * (isReversed ? -1 : 1);
                }
            }

            return 0;
        }

        @Override public void setPIDSourceType(PIDSourceType pidSourceType) {

            if (encoder instanceof Encoder) ((Encoder) encoder).setPIDSourceType(pidSourceType);

            this.pidSourceType = pidSourceType;
        }

        @Override public PIDSourceType getPIDSourceType() { return pidSourceType; }

        @Override public double pidGet() { return get(); }
    }

    public enum MXP implements PIDSource {
        NavX;

        AHRS navX;

        long timeout = System.currentTimeMillis();

        MXP() { navX = new AHRS(SerialPort.Port.kMXP); }

        private double get() { return navX.getYaw(); }

        private boolean hasStopped() {

            double acceleration = Math.abs(navX.getRawAccelX()) + Math.abs(navX.getRawAccelY());
            acceleration += Math.abs(navX.getRawAccelZ());

            if (acceleration > 0.1) {

                if (System.currentTimeMillis() - timeout > 30) return true;

            } else timeout = System.currentTimeMillis();

            return false;
        }

        @Override public void setPIDSourceType(PIDSourceType pidSource) { navX.setPIDSourceType(pidSource); }

        @Override public PIDSourceType getPIDSourceType() { return navX.getPIDSourceType(); }

        @Override public double pidGet() { return navX.pidGet(); }
    }
}
