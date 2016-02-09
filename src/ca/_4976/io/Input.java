package ca._4976.io;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.PIDSourceType.*;

public class Input {

    public enum Digital {

        BALL_DETECTED(4);

        DigitalInput input;

        Digital(int pin) { input = new DigitalInput(pin); }
    }

    public enum Encoder implements PIDSource {

        DRIVE_LEFT(0, 1, 0.1),
        DRIVE_RIGHT(2, 3, 0.1),
        SHOOTER(5, 6, 0.1, kRate);

        Object encoder;
        boolean isReversed;
        double lastStateUpdate = 0;
        int hasNotMovedCounter = 0;

        Encoder(int a, int b, double dpp) {

            encoder = new edu.wpi.first.wpilibj.Encoder(a, b);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setDistancePerPulse(dpp);
        }

        Encoder(CANTalon.FeedbackDevice feedbackDevice, double scaler) {


        }

        Encoder(CANTalon.FeedbackDevice feedbackDevice, double scaler, PIDSourceType pidSourceType) {


        }

        Encoder(int a, int b, double dpp, PIDSourceType pidSourceType) {

            encoder = new edu.wpi.first.wpilibj.Encoder(a, b);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setDistancePerPulse(dpp);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setPIDSourceType(pidSourceType);
        }

        public boolean hasStopped() {

            if (getDistance() == lastStateUpdate) hasNotMovedCounter++;

            else hasNotMovedCounter = 0;

            if (hasNotMovedCounter > 2) return true;

            else return false;
        }

        public double getVelocity() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getRate();

            else return isReversed ? -((CANTalon) encoder).getEncVelocity() : ((CANTalon) encoder).getEncVelocity();
        }

        public double getDistance() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getDistance();

            else return isReversed ? -((CANTalon) encoder).getEncVelocity() : ((CANTalon) encoder).getEncPosition();
        }

        @Override public double pidGet() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).get();

            else {

                if (((CANTalon) encoder).getPIDSourceType() == kRate)
                    return isReversed ? -((CANTalon) encoder).getEncVelocity() : ((CANTalon) encoder).getEncVelocity();

                else return isReversed ? -((CANTalon) encoder).getEncVelocity() : ((CANTalon) encoder).getEncVelocity();
            }
        }

        @Override public void setPIDSourceType(PIDSourceType pidSourceType) {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                ((edu.wpi.first.wpilibj.Encoder)encoder).setPIDSourceType(pidSourceType);

            else ((CANTalon) encoder).setPIDSourceType(pidSourceType);
        }

        @Override public PIDSourceType getPIDSourceType() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder)encoder).getPIDSourceType();

            else return ((CANTalon) encoder).getPIDSourceType();
        }
    }

    public enum MXP implements PIDSource {
        NAV_X;

        AHRS nav;

        MXP() { nav = new AHRS(SerialPort.Port.kMXP); }

        public void reset() { nav.reset(); }

        public void getYaw() { nav.getYaw(); }

        @Override public void setPIDSourceType(PIDSourceType pidSourceType) { nav.setPIDSourceType(pidSourceType); }

        @Override public PIDSourceType getPIDSourceType() { return nav.getPIDSourceType(); }

        @Override public double pidGet() { return nav.pidGet(); }
    }
}
