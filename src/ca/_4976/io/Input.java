package ca._4976.io;

import ca._4976.color.ISL29125;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;

import static edu.wpi.first.wpilibj.PIDSourceType.*;

public class Input {

    public enum Digital {

        //TestBot: 4, 0, 1;
        //Competiton: 0, 1, 2;
        BALL_DETECTED(0, true),
        IR_L(1, false),
        IR_R(2, false);

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

    public enum Analog {

        POSITION_DIAL(0, 1650);

        AnalogInput input;
        int difference;

        Analog(int pin, int difference) {
            input = new AnalogInput(pin);
            this.difference = difference;
        }

        public int get() {
            if (difference > 0)
                return input.getValue() / difference;
            return input.getValue();
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

            if (getDistance() == lastStateUpdate)
                hasNotMovedCounter++;
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

            else
                return isReversed ? -((Output.Motor) encoder).getEncVelocity() / scale : ((Output.Motor) encoder).getEncVelocity() / scale;
        }

        @Override
        public double pidGet() {

            if (getPIDSourceType() == kRate)
                return getVelocity();

            else return getDistance();
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSourceType) {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                ((edu.wpi.first.wpilibj.Encoder) encoder).setPIDSourceType(pidSourceType);

            else
                ((Output.Motor) encoder).setPIDSourceType(pidSourceType);
        }

        @Override
        public PIDSourceType getPIDSourceType() {

            if (encoder instanceof edu.wpi.first.wpilibj.Encoder)
                return ((edu.wpi.first.wpilibj.Encoder) encoder).getPIDSourceType();

            else
                return ((Output.Motor) encoder).getPIDSourceType();
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
        int[] color1, color2;

        double error;
        Preferences prefs = Preferences.getInstance();

        I2C() {
            colorSensor = new ISL29125(edu.wpi.first.wpilibj.I2C.Port.kOnboard);

            // Load color1 calibration
            color1 = new int[3];
            color1[0] = prefs.getInt("color1R", 0);
            color1[1] = prefs.getInt("color1G", 0);
            color1[2] = prefs.getInt("color1B", 0);

            // Load color2 calibration
            color2 = new int[3];
            color2[0] = prefs.getInt("color2R", 0);
            color2[1] = prefs.getInt("color2G", 0);
            color2[2] = prefs.getInt("color2B", 0);

            error = prefs.getDouble("error", 0.0);
            colorSensor.init();
        }

        public void callibrate() {
            if (Controller.Primary.Button.A.isDownOnce()) {
                color1 = colorSensor.readColor();

                prefs.putInt("color1R", color1[0]);
                prefs.putInt("color1G", color1[1]);
                prefs.putInt("color1B", color1[2]);
            }
            if (Controller.Primary.Button.B.isDownOnce()) {
                color2 = colorSensor.readColor();

                prefs.putInt("color2R", color2[0]);
                prefs.putInt("color2G", color2[1]);
                prefs.putInt("color2B", color2[2]);
            }
            printCalibration();
        }

        public boolean onLine() {
            error = prefs.getDouble("error", 0.0);
            for (int i = 0; i < 3; i++)
                if (!withinError(i))
                    return false;
            return true;
        }

        private boolean withinError(int i) {
            double difference = Math.abs(color1[i] - color2[i]) * error;
            return (colorSensor.readColor()[i] + difference >= color2[i] && colorSensor.readColor()[i] - difference <= color2[i]);
        }

        public void printCalibration() {
            System.out.print("Color Sensor: ");
            for (int i = 0; i < 3; i++)
                System.out.print(colorSensor.readColor()[i] + ",");
            System.out.println();
            System.out.print("Color 1: ");
            if (color1 != null)
                for (int i = 0; i < 3; i++)
                    System.out.print(color1[i] + ",");
            System.out.println();
            System.out.print("Color 2: ");
            if (color2 != null)
                for (int i = 0; i < 3; i++)
                    System.out.print(color2[i] + ",");
            System.out.println();
        }
    }
}
