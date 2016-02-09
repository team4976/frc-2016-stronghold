package ca._4976.io;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {

    public static Joystick[] joystick = {new Joystick(0), new Joystick(1)};

    public static boolean getResetOnce() {

        return Primary.Button.START.isDownOnce() || Secondary.Button.START.isDownOnce();
    }

    public static boolean getReset() {

        return Primary.Button.START.isDown() || Secondary.Button.START.isDown();
    }

    public static class Primary {

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

            private int port;
            private boolean wasReleased;

            Button(int port) {
                this.port = port;
                wasReleased = true;
            }

            public boolean isDown() {
                return joystick[0].getRawButton(port);
            }

            public boolean isDownOnce() {
                if (wasReleased && isDown()) {
                    wasReleased = false;
                    return true;
                } else if (!isDown())
                    wasReleased = true;

                return false;
            }
        }

        public enum Trigger {
            LEFT(2),
            RIGHT(3);

            public int axis;

            Trigger(int axis) {
                this.axis = axis;
            }

            public double value() {
                return joystick[0].getRawAxis(axis);
            }

            public static double totalValue(Trigger one, Trigger two) {
                return (joystick[0].getRawAxis(one.axis) - joystick[0].getRawAxis(two.axis));
            }

            public double value(double min, double max) {
                double value = joystick[0].getRawAxis(axis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

            public static double totalValue(Trigger one, Trigger two, double min, double max) {
                double value = (joystick[0].getRawAxis(one.axis) - joystick[0].getRawAxis(two.axis));
                if (value > min && value < max)
                    return 0;
                return value;
            }
        }

        public enum Stick {
            LEFT(0, 1),
            RIGHT(4, 5);

            public int hAxis, vAxis;

            Stick(int hAxis, int vAxis) {
                this.hAxis = hAxis;
                this.vAxis = vAxis;
            }

            public double horizontal() {
                return joystick[0].getRawAxis(hAxis);
            }

            public double vertical() {
                return joystick[0].getRawAxis(vAxis);
            }

            public double horizontal(double min, double max) {
                double value = joystick[0].getRawAxis(hAxis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

            public double vertical(double min, double max) {
                double value = joystick[0].getRawAxis(vAxis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

        }

        public enum DPad {
            NOT(-1),
            NORTH(0),
            NORTH_EAST(45),
            EAST(90),
            SOUTH_EAST(135),
            SOUTH(180),
            SOUTH_WEST(225),
            WEST(270),
            NORTH_WEST(315);

            public int angle;
            public boolean wasReleased;

            DPad(int angle) {
                this.angle = angle;
                wasReleased = true;
            }

            public boolean isDown() {
                return isDown(0);
            }

            public boolean isDown(int port) {
                return (joystick[0].getPOV(port) == angle);
            }

            public boolean isDownOnce() {
                return isDownOnce(0);
            }

            public boolean isDownOnce(int port) {
                if (wasReleased && isDown(port)) {
                    wasReleased = false;
                    return true;
                } else if (!isDown(port))
                    wasReleased = true;

                return false;
            }
        }
    }

    public static class Secondary {

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

            private int port;
            private boolean wasReleased;

            Button(int port) {
                this.port = port;
                wasReleased = true;
            }

            public boolean isDown() {
                return joystick[1].getRawButton(port);
            }

            public boolean isDownOnce() {
                if (wasReleased && isDown()) {
                    wasReleased = false;
                    return true;
                } else if (!isDown())
                    wasReleased = true;

                return false;
            }
        }

        public enum Trigger {
            LEFT(2),
            RIGHT(3);

            public int axis;

            Trigger(int axis) {
                this.axis = axis;
            }

            public double value() {
                return joystick[1].getRawAxis(axis);
            }

            public static double totalValue(Trigger one, Trigger two) {
                return (joystick[1].getRawAxis(one.axis) - joystick[1].getRawAxis(two.axis));
            }

            public double value(double min, double max) {
                double value = joystick[1].getRawAxis(axis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

            public static double totalValue(Trigger one, Trigger two, double min, double max) {
                double value = (joystick[1].getRawAxis(one.axis) - joystick[1].getRawAxis(two.axis));
                if (value > min && value < max)
                    return 0;
                return value;
            }
        }

        public enum Stick {
            LEFT(0, 1),
            RIGHT(4, 5);

            public int hAxis, vAxis;

            Stick(int hAxis, int vAxis) {
                this.hAxis = hAxis;
                this.vAxis = vAxis;
            }

            public double horizontal() {
                return joystick[1].getRawAxis(hAxis);
            }

            public double vertical() {
                return joystick[1].getRawAxis(vAxis);
            }

            public double horizontal(double min, double max) {
                double value = joystick[1].getRawAxis(hAxis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

            public double vertical(double min, double max) {
                double value = joystick[1].getRawAxis(vAxis);
                if (value > min && value < max)
                    return 0;
                return value;
            }

        }

        public enum DPad {
            NOT(-1),
            NORTH(0),
            NORTH_EAST(45),
            EAST(90),
            SOUTH_EAST(135),
            SOUTH(180),
            SOUTH_WEST(225),
            WEST(270),
            NORTH_WEST(315);

            public int angle;
            public boolean wasReleased;

            DPad(int angle) {
                this.angle = angle;
                wasReleased = true;
            }

            public boolean isDown() {
                return isDown(0);
            }

            public boolean isDown(int port) {
                return (joystick[1].getPOV(port) == angle);
            }

            public boolean isDownOnce() {
                return isDownOnce(0);
            }

            public boolean isDownOnce(int port) {
                if (wasReleased && isDown(port)) {
                    wasReleased = false;
                    return true;
                } else if (!isDown(port))
                    wasReleased = true;

                return false;
            }
        }
    }
}
