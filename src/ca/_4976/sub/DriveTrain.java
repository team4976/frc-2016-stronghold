package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Input;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.*;

import java.util.ArrayList;

public class DriveTrain implements PIDOutput, PIDSource {

    enum TaskType { DRIVE, TURN, AIM}

    private final Preferences table = Preferences.getInstance();

    private Double[][] pidConfiguration = {
        {0.0, 0.0, 0.0, 0.0},
        {0.0, 0.0, 0.0, 0.0},
        {0.003, 5.0e-5, 0.005, 0.0}
    };

    private ArrayList<Object[]> tasks = new ArrayList();

    private PIDController pid = new PIDController(0, 0, 0, this, this);

    private Targeting targeting;

    private int taskState = 0;

    public void addTargetingSubsystem(Targeting targeting) { this.targeting = targeting; }

    private void periodic() {

        switch (taskState) {

            case 0:

                if (tasks.size() > 0) {

                    switch ((TaskType)  (tasks.get(0))[0]) {

                        case DRIVE:

                            pid.free();

                            pid = new PIDController(pidConfiguration[0][0], pidConfiguration[0][1],
                                    pidConfiguration[0][2], this, this);

                            pid.setSetpoint((Double) (tasks.get(0))[1]);

                            break;
                        case TURN:

                            pid.free();

                            pid = new PIDController(pidConfiguration[1][0], pidConfiguration[1][1],
                                    pidConfiguration[1][2], this, this);

                            Input.MXP.NAV_X.reset();
                            pid.setSetpoint((Double) (tasks.get(0))[1]);

                            break;
                        case AIM:

                            pid.free();

                            pid = new PIDController(table.getDouble("A_P", 0), table.getDouble("A_I", 0),
                                    table.getDouble("A_D", 0), this, this);

                            pid.setSetpoint(0);
                            pid.setOutputRange(-0.4, 0.4);

                            break;
                    }

                    pid.enable();
                    taskState++;

                } break;
            case 1:

                if (tasks.size() > 0)
                    switch ((TaskType) (tasks.get(0))[0]) {

                        case DRIVE:

                            if (pid.getError() < 5 && Input.Encoder.DRIVE_RIGHT.hasStopped()) {

                                tasks.remove(0);
                                pid.disable();
                                taskState = 0;

                            } break;

                        case TURN:

                            if (pid.getError() < 5 && Input.Encoder.DRIVE_RIGHT.hasStopped()) {

                                tasks.remove(0);
                                pid.disable();
                                taskState = 0;

                            } break;
                        case AIM:

                            if (!pid.isEnabled()) break;

                            System.out.println(pid.getError());

                            if (
                                    (tasks.get(0))[1].equals(0)
                                            && Output.Motor.DRIVE_LEFT.hasStopped()
                                            && Math.abs(pid.getError()) < 2
                                    ) {

                                tasks.remove(0);
                                pid.disable();
                                taskState = 0;

                            } break;
                    }
        }
    }

    public void disabledInit() {

        taskState = 0;
        pid.disable();
        tasks.clear();
    }

    public void teleopPeriodic() {

        periodic();

        if (tasks.size() == 0) {

            double steering = Controller.Primary.Stick.LEFT.horizontal();
            steering = steering > 0 ? -steering * steering : steering * steering;
            steering = Math.abs(steering) > 0.08 ? steering : 0;

            double power = Controller.Primary.Trigger.RIGHT.value() - Controller.Primary.Trigger.LEFT.value();

            Output.Motor.DRIVE_LEFT.set(-power - steering);
            Output.Motor.DRIVE_RIGHT.set(power - steering);

            if (Controller.Primary.Button.RIGHT_BUMPER.isDown() && tasks.size() < 1)
                tasks.add(new Object[] {TaskType.AIM, 0});


            if (Controller.Primary.DPad.EAST.isDownOnce()) tasks.add(new Object[] {TaskType.TURN, 90});
            if (Controller.Primary.DPad.WEST.isDownOnce()) tasks.add(new Object[] {TaskType.TURN, -90});

            if (Controller.Primary.Button.Y.isDown() && Output.Solenoid.GEAR.get()) Output.Solenoid.GEAR.set(false);
            else if (!Controller.Primary.Button.Y.isDown() && !Output.Solenoid.GEAR.get()) Output.Solenoid.GEAR.set(true);
        }

        if (!Controller.Primary.Button.RIGHT_BUMPER.isDown()
                && tasks.size() > 0
                && tasks.get(0)[0] == TaskType.AIM) {

            taskState = 0;
            pid.disable();
            tasks.remove(0);
        }

        if (Controller.Primary.Button.BACK.isDownOnce()) {

            taskState = 0;
            pid.disable();
            tasks.clear();
        }
    }

    public void autonomousPeriodic() { periodic(); }

    void ScheduleTask(TaskType taskType, Double taskValue) { tasks.add(new Object[] {taskType, taskValue}); }

    boolean hasTasks() { return tasks.size() > 0; }

    @Override public void pidWrite(double output) {

        if (tasks.size() > 0)

            switch ((TaskType) tasks.get(0)[0])  {

                case DRIVE:

                    Output.Motor.DRIVE_LEFT.set(output);
                    Output.Motor.DRIVE_RIGHT.set(-output);

                    break;

                case TURN:

                    Output.Motor.DRIVE_LEFT.set(-output);
                    Output.Motor.DRIVE_RIGHT.set(-output);

                case AIM:

                    Output.Motor.DRIVE_LEFT.set(-output);
                    Output.Motor.DRIVE_RIGHT.set(-output);

                    break;
            }
    }

    @Override public void setPIDSourceType(PIDSourceType pidSource) { }

    @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }

    @Override public double pidGet() {

        switch ((TaskType) tasks.get(0)[0])  {

            default: return 0;

            case DRIVE: return Input.Encoder.DRIVE_RIGHT.pidGet();


            case TURN: return Input.MXP.NAV_X.pidGet();

            case AIM:

                Double value = targeting.pidGet();

                System.out.println("Error: " + value);

                if (value == null) {

                    return 0;

                } else return value;
        }
    }
}