package ca._4976.io;

import ca._4976.color.ISL29125;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.PIDSourceType.*;

public class Input {

    public enum Digital {

        BALL_DETECTED(4, true),
        IR_L(0, false),
        IR_R(1, false);

        DigitalInput input;
        boolean inverted;

        Digital(int pin, boolean inverted) {
            input = new DigitalInput(pin);
            this.inverted = inverted;
        }

        public boolean get() {
            return inverted != input.get();
        }
    }

    public enum Encoder implements PIDSource {

        //DRIVE_LEFT(0, 1, 0.1),
        //DRIVE_RIGHT(2, 3, 0.1),
        SHOOTER(Output.Motor.SHOOTER, 6.82 * 2, kRate);

        Object encoder = false;
        boolean isReversed;
        double lastStateUpdate = 0;
        int hasNotMovedCounter = 0;
        double scale;

        Encoder(int a, int b, double dpp) {

            encoder = new edu.wpi.first.wpilibj.Encoder(a, b);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setDistancePerPulse(dpp);
        }

        Encoder(Output.Motor motor, double scaler, PIDSourceType pidSourceType) {

            motor.setPIDSourceType(pidSourceType);

            this.encoder = motor;
            this.scale = scaler;
        }

        Encoder(int a, int b, double dpp, PIDSourceType pidSourceType) {

            encoder = new edu.wpi.first.wpilibj.Encoder(a, b);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setDistancePerPulse(dpp);
            ((edu.wpi.first.wpilibj.Encoder) encoder).setPIDSourceType(pidSourceType);
        }

        public void setReversed(boolean reversed) {
            this.isReversed = reversed;
        }

        public boolean hasStopped() {

            if (getDistance() == lastStateUpdate) hasNotMovedCounter++;

            else hasNotMovedCounter = 0;

            return hasNotMovedCounter > 2;
        }

        public double getVelocity() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getRate();

            else
                return isReversed ? -((Output.Motor) encoder).getEncVelocity() / scale : ((Output.Motor) encoder).getEncVelocity() / scale;
        }

        public double getDistance() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getDistance();


            else return isReversed ? -((Output.Motor) encoder).getEncVelocity() / scale :
                    ((Output.Motor) encoder).getEncVelocity() / scale;
        }

        @Override
        public double pidGet() {

            if (getPIDSourceType() == kRate) return getVelocity();

            else return getDistance();
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSourceType) {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                ((edu.wpi.first.wpilibj.Encoder) encoder).setPIDSourceType(pidSourceType);

            else ((Output.Motor) encoder).setPIDSourceType(pidSourceType);
        }

        @Override
        public PIDSourceType getPIDSourceType() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getPIDSourceType();

            else return ((Output.Motor) encoder).getPIDSourceType();
        }
    }

    public enum MXP implements PIDSource {

        NAV_X;

        AHRS nav;

        MXP() {
            nav = new AHRS(SerialPort.Port.kMXP);
        }

        public void reset() {
            nav.reset();
        }

        public void getYaw() {
            nav.getYaw();
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSourceType) {
            nav.setPIDSourceType(pidSourceType);
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return nav.getPIDSourceType();
        }

        @Override
        public double pidGet() {
            return nav.pidGet();
        }
    }

    public enum I2C {

        LINE;

        ISL29125 colorSensor;

        I2C() {
            colorSensor = new ISL29125(edu.wpi.first.wpilibj.I2C.Port.kOnboard);
        }

        public void callibrate() {

        }

        public boolean crossed() {
            return false;
        }

    }

}
