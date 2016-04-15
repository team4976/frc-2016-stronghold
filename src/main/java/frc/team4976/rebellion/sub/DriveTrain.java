package frc.team4976.rebellion.sub;

import edu.wpi.first.wpilibj.*;
import frc.team4976.rebellion.io.Input;
import frc.team4976.rebellion.io.Output;
import jaci.openrio.module.blackbox.BlackBox;
import jaci.openrio.module.blackbox.BlackBoxContext;

import static frc.team4976.rebellion.io.Output.*;
import static frc.team4976.rebellion.io.Input.*;
import static frc.team4976.rebellion.io.Controller.*;
import static frc.team4976.rebellion.sub.DriveTrain.TaskType.*;

public class DriveTrain implements PIDOutput, PIDSource {

    public enum TaskType { TURN, DRIVE, AIM, NOTHING }

    private PIDController pidController = new PIDController(0, 0, 0, this, this);
    private Targeting targeting;

    private Object[] task = new Object[] {TaskType.NOTHING, 0};

    private boolean normalGearState = false;

    private int taskState = 0;

    public void addTargeting(Targeting targeting) { this.targeting = targeting; }

    public void teleopPeriodic() {

        if (DRIVER.get(Button.BACK)) task[0] = NOTHING;

        if (task[0] == NOTHING && DRIVER.get(Button.RIGHT_BUMPER)) { task[0] = AIM; }

        else if (task[0] == AIM && !DRIVER.get(Button.RIGHT_BUMPER)) task[0] = NOTHING;

        if (task[0] == NOTHING && DRIVER.get(POV.RIGHT)) {

            task[0] = TURN;
            task[1] = 90;

        } else if (task[0] == NOTHING && DRIVER.get(POV.LEFT)) {

            task[0] = TURN;
            task[1] = -90;
        }

        if (task[0] == TaskType.NOTHING) {

            double forward = DRIVER.get(Axis.RIGHT_TRIGGER) - DRIVER.get(Axis.LEFT_TRIGGER);

            double steering = DRIVER.get(Axis.LEFT_HORIZONTAL);
            steering = steering > 0 ? steering * steering : -steering * steering;
            steering = Math.abs(steering) > 0.08 ? steering : 0;

            arcadeDrive(forward, steering);

            if (DRIVER.get(Button.Y) && !get(Pneumatic.SHIFTER)) set(Pneumatic.SHIFTER, !normalGearState);

            else if (!DRIVER.get(Button.Y) && get(Pneumatic.SHIFTER)) set(Pneumatic.SHIFTER, normalGearState);

            if (DRIVER.get(POV.UP)) {

                normalGearState = true;
                set(Pneumatic.SHIFTER, true);

            } else if (DRIVER.get(POV.DOWN)) {

                normalGearState = false;
                set(Pneumatic.SHIFTER, false);
            }

        } else taskHandler();
    }

    public void autonomousPeriodic() { taskHandler(); }

    private void taskHandler() {

        Preferences preferences = Preferences.getInstance();

        if (task[0] == NOTHING && pidController.isEnabled()) {

            taskState = 0;
            pidController.disable();
            pidController.free();
        }

        switch(taskState) {

            case 0:

                switch (((TaskType)(task[0]))) {

                    case AIM:

                        pidController.reset();

                        pidController.setPID(
                                preferences.getDouble("A_P", 0),
                                preferences.getDouble("A_I", 0),
                                preferences.getDouble("A_D", 0)
                        );

                        pidController.setSetpoint(0);
                        taskState++;

                        break;
                    case TURN:

                        pidController.reset();

                        pidController.setPID(
                                preferences.getDouble("T_P", 0),
                                preferences.getDouble("T_I", 0),
                                preferences.getDouble("T_D", 0)
                        );

                        pidController.setSetpoint(Double.valueOf((Integer) task[1]));
                        taskState++;

                        break;
                    case DRIVE:

                        pidController.reset();

                        pidController.setPID(
                                preferences.getDouble("D_P", 0),
                                preferences.getDouble("D_I", 0),
                                preferences.getDouble("D_D", 0)
                        );

                        pidController.setSetpoint(Double.valueOf((Integer) task[1]));
                        taskState++;

                        break;

                } break;
            case 1:

                if (task[0] == AIM && (Integer) task[1] == 1) break;

                else {

                    if (Math.abs(pidController.getAvgError()) < 10 && hasStopped()) {

                        task[0] = NOTHING;
                        taskState = 0;
                        pidController.disable();
                    }

                } break;
        }
    }

    public void setTask(TaskType task, int param) {

        this.task[0] = task;
        this.task[1] = param;
    }

    public boolean hasTask() { return task[0] == NOTHING; }

    private void arcadeDrive(double forward, double steering) {

        set(Motor.DRIVE_LEFT, forward - steering);
        set(Motor.DRIVE_RIGHT, -forward - steering);
    }

    @Override public void pidWrite(double output) {

    }

    @Override public void setPIDSourceType(PIDSourceType pidSource) {

    }

    @Override public PIDSourceType getPIDSourceType() {
        return null;
    }

    @Override public double pidGet() {

        switch (((TaskType) task[0])) {

            case NOTHING: return 0;

            case TURN: return MXP.NavX.pidGet();

            case DRIVE: return QuadraticEncoder.DRIVE.pidGet();

            case AIM: return targeting.pidGet();

            default: return 0;
        }
    }
}
