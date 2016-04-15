package frc.team4976.rebellion.io;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {

    private static Joystick[] joysticks = new Joystick[4];

    public static Controller DRIVER = new Controller(0);
    public static Controller OPERATOR = new Controller(1);

    private int controllerPort;

    private Controller(int port) {

        controllerPort = port;
        joysticks[port] = new Joystick(port);
    }

    public boolean get(Button button) { return button.get(controllerPort); }//return button.get(controllerPort); }

    public boolean getOnce(Button button) { return button.getOnce(controllerPort); }

    public boolean get(POV pov) { return pov.get(controllerPort); }

    public double get(Axis axis) { return axis.get(controllerPort); }

    public void setRumble(float rumble) {

        joysticks[controllerPort].setRumble(Joystick.RumbleType.kLeftRumble,rumble);
        joysticks[controllerPort].setRumble(Joystick.RumbleType.kRightRumble,rumble);
    }

    public enum Button {
        A(1),
        B(2),
        X(3),
        Y(4),
        LEFT_BUMPER(5),
        RIGHT_BUMPER(6),
        BACK(7),
        START(8),
        LEFT_STICK(9),
        RIGHT_STICK(10);

        private int button;

        private boolean releaseFlag = true;

        Button(int button) { this.button = button; }

        private boolean get(int port) {

            return joysticks[port].getRawButton(button);
        }

        private boolean getOnce(int port) {

            if (get(port) && releaseFlag) {

                releaseFlag = false;
                return true;

            } else {

                releaseFlag = !get(port);
                return false;
            }
        }
    }

    public enum Axis {
        LEFT_HORIZONTAL(0),
        LEFT_VERTICAL(1),
        LEFT_TRIGGER(2),
        RIGHT_TRIGGER(3),
        RIGHT_HORIZONTAL(4),
        RIGHT_VERTICAL(5);

        int axis;

        Axis(int axis) { this.axis = axis; }

        private double get(int port) {

            return joysticks[port].getRawAxis(axis); }
    }

    public enum POV {
        UP(0),
        DOWN(180),
        LEFT(90),
        RIGHT(270);

        int degree;

        POV(int degree) { this.degree = degree; }

        private boolean get(int port) { return joysticks[port].getPOV() == degree ; }
    }
}
