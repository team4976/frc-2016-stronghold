package ca._4976.sub;

import ca._4976.io.Input;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Autonomous {

    private enum  AutoTypes { BREACH, ALIGN, AIM, SHOOT, NOTHING }
    private enum DefenceTypes { LOWBAR, PORTCULLIS, CHEVELDEFRISE, TERRAIN, NOTHING }

    private AutoTypes autoType = AutoTypes.AIM;
    private DefenceTypes defenceType = DefenceTypes.PORTCULLIS;

    private int state = 0;

    private long autoTimeFlag = System.currentTimeMillis();

    private boolean[] autoConfig;

    private NetworkTable autoSelectionTable = NetworkTable.getTable("Autonomous Parameters");
    private Shooter shooter;
    private DriveTrain drive;
    private Targeting targeting;

    public void addShooter(Shooter shooter) { this.shooter = shooter; }

    public void addDrive(DriveTrain drive) { this.drive = drive; }

    public void addTargeting(Targeting targeting) { this.targeting = targeting; }

    public void autonomousInit() {

        Input.MXP.NAV_X.reset();

        Output.Motor.DRIVE_LEFT.set(0);
        Output.Motor.DRIVE_RIGHT.set(0);
        Output.Motor.INTAKE_ROLLERS.set(0);
        Output.Motor.INTAKE_WHEELS.set(0);
        Output.Motor.SHOOTER.set(0);
        Output.Motor.SCALAR.set(0);

        Output.Solenoid.GEAR.set(true);

        state = 0;

        autoConfig = autoSelectionTable.getBooleanArray("Autonomous Selection", new boolean[3]);
        double defenceConfig = autoSelectionTable.getNumber("Defence Selection", 0);

        if (autoConfig[0]) autoType = AutoTypes.BREACH;

        else if (autoConfig[1]) autoType = AutoTypes.ALIGN;

        else if (autoConfig[2]) autoType = AutoTypes.AIM;

        else if (autoConfig[3]) autoType = AutoTypes.SHOOT;

        else autoType = AutoTypes.NOTHING;

        if (defenceConfig == 1) defenceType = DefenceTypes.LOWBAR;

        else if (defenceConfig == 2) defenceType = DefenceTypes.PORTCULLIS;

        else if (defenceConfig == 3) defenceType = DefenceTypes.CHEVELDEFRISE;

        else if (defenceConfig == 4) defenceType = DefenceTypes.TERRAIN;

        else defenceType = DefenceTypes.NOTHING;
    }
    
    public void autonomousPeriodic() {
        
        switch (autoType) {

            case BREACH:

                if (!autoConfig[0]) {

                    state = 0;
                    autoType = AutoTypes.ALIGN;
                }

                switch (defenceType) {

                    case LOWBAR:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(-1);
                                    Output.Motor.DRIVE_RIGHT.set(1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 1000) {

                                    Output.Motor.DRIVE_LEFT.set(-0.09);
                                    Output.Motor.DRIVE_RIGHT.set(0.09);
                                    state++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                autoType = AutoTypes.ALIGN;
                                state = 0;

                                break;
                        } break;

                    case PORTCULLIS:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(-0.55);
                                    Output.Motor.DRIVE_RIGHT.set(0.55);
                                    Output.Motor.INTAKE_WHEELS.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3900) { //38003ws3

                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state = 0;
                                    autoType = AutoTypes.ALIGN;

                                } break;

                            case 3:
                        } break;
                    case CHEVELDEFRISE:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Solenoid.INTAKE.set(false);
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(-0.30);
                                    Output.Motor.DRIVE_RIGHT.set(0.30);
                                    Output.Motor.INTAKE_WHEELS.set(1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 4000) { //3800

                                    Output.Solenoid.INTAKE.set(true);
                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(-0.6);
                                    Output.Motor.DRIVE_RIGHT.set(0.6);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 3:

                                if (System.currentTimeMillis() - autoTimeFlag > 1500) { //3800

                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state = 0;
                                    autoType = AutoTypes.ALIGN;

                                } break;
                        } break;

                    case TERRAIN:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 500) {

                                    Output.Motor.DRIVE_LEFT.set(0.7);
                                    Output.Motor.DRIVE_RIGHT.set(-0.7);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3600) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    state++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                autoType = AutoTypes.ALIGN;
                                state = 0;

                                break;
                        } break;

                } break;

            case ALIGN:

                if (!autoConfig[1]) {

                    state = 0;
                    autoType = AutoTypes.AIM;
                }

                double alignSelection = (int) autoSelectionTable.getNumber("Align Selection", 0);

                int alignType = 0;
                int direction = 0;

                if (alignSelection == 2 || alignSelection == 4) {

                    alignType = 2;
                    direction = (int) alignSelection - 3;

                } else if (alignSelection != 0) alignType = 1;

                switch (alignType) {

                    case 1:

                        switch (state) {

                            case 0:

                                autoTimeFlag = System.currentTimeMillis();
                                state++;

                                break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > (alignSelection == 1 ? 1000 : 450)) {

                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);

                                    if (alignSelection == 1) state++;

                                    else {

                                        state = 0;
                                        autoType = AutoTypes.AIM;
                                    }

                                } else {

                                    Output.Motor.DRIVE_LEFT.set(-1);
                                    Output.Motor.DRIVE_RIGHT.set(1);

                                } break;
                            case 2:

                                if (targeting.centerX().length != 0) {

                                    state = 0;
                                    autoType = AutoTypes.AIM;
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);

                                } else {

                                    Output.Motor.DRIVE_LEFT.set(-0.3);
                                    Output.Motor.DRIVE_RIGHT.set(-0.3);

                                } break;

                        } break;

                    case 2:

                        switch (state) {

                            case 0:

                                System.out.println(Input.MXP.NAV_X.getYaw());

                                if (direction > 0 && Input.MXP.NAV_X.getYaw() < -40) {

                                    state++;
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();

                                } else if (direction < 0 && Input.MXP.NAV_X.getYaw() > 40) {

                                    state++;
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();

                                } else {

                                    Output.Motor.DRIVE_LEFT.set(-0.3 * direction);
                                    Output.Motor.DRIVE_RIGHT.set(-0.3 * direction);

                                } break;
                            case 1:

                                if (System.currentTimeMillis() - autoTimeFlag > 1000) {

                                    state++;
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);

                                } else {

                                    Output.Motor.DRIVE_LEFT.set(-1);
                                    Output.Motor.DRIVE_RIGHT.set(1);

                                } break;
                            case 2:

                                if (targeting.centerX().length != 0) {

                                    state = 0;
                                    autoType = AutoTypes.AIM;
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);

                                } else {

                                    Output.Motor.DRIVE_LEFT.set(0.3 * direction);
                                    Output.Motor.DRIVE_RIGHT.set(0.3 * direction);

                                } break;

                        } break;

                } break;

            case AIM:

                if (!autoConfig[2]) {

                    state = -1;
                    autoType = AutoTypes.SHOOT;
                }

                switch (state) {

                    case 0:

                        drive.ScheduleTask(DriveTrain.TaskType.AIM, 0d);

                        if (drive.hasTasks()) state++;

                        break;
                    case 1:

                        if (Output.Motor.DRIVE_LEFT.hasStopped()) {

                            autoType = AutoTypes.SHOOT;
                            state = 0;

                        } break;
                    case 2:

                        if (shooter.shoot()) {

                            state++;
                            shooter.disablePID();

                        } break;
                } break;

            case SHOOT: {

                if (!autoConfig[3]) {

                    state = -1;
                    autoType = AutoTypes.NOTHING;
                }

                switch (state) {

                    case 0:

                        state++;
                        shooter.cock();
                        break;
                    case 1:

                        if (shooter.shoot()) {

                            state++;
                            shooter.disablePID();

                        } break;
                }
            }
        }
    }
}
