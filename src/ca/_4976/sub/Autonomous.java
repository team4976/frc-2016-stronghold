package ca._4976.sub;

import ca._4976.io.Input;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Autonomous {

    private enum  AutoTypes { BREACH, ALIGN, SHOOT, NOTHING }
    private enum DefenceTypes { LOWBAR, PORTCULLIS, CHEVELDEFRISE, TERRAIN, NOTHING }

    private AutoTypes autoType = AutoTypes.SHOOT;
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
        
        Output.Motor.DRIVE_LEFT.set(0);
        Output.Motor.DRIVE_RIGHT.set(0);
        Output.Motor.INTAKE_ROLLERS.set(0);
        Output.Motor.INTAKE_WHEELS.set(0);
        Output.Motor.SHOOTER.set(0);
        Output.Motor.SCALER.set(0);

        Output.Solenoid.GEAR.set(true);

        state = 0;

        autoConfig = autoSelectionTable.getBooleanArray("Auto Config", new boolean[3]);
        boolean[] defenceConfig = autoSelectionTable.getBooleanArray("Defence Config", new boolean[4]);

        if (autoConfig[0]) autoType = AutoTypes.BREACH;

        else if (autoConfig[1]) autoType = AutoTypes.ALIGN;

        else if (autoConfig[2]) autoType = AutoTypes.SHOOT;

        else autoType = AutoTypes.NOTHING;

        if (defenceConfig[0]) defenceType = DefenceTypes.LOWBAR;

        else if (defenceConfig[1]) defenceType = DefenceTypes.PORTCULLIS;

        else if (defenceConfig[2]) defenceType = DefenceTypes.CHEVELDEFRISE;

        else if (defenceConfig[3]) defenceType = DefenceTypes.TERRAIN;

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

                                    Output.Motor.DRIVE_LEFT.set(1);
                                    Output.Motor.DRIVE_RIGHT.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;
                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3000) {

                                    Output.Motor.DRIVE_LEFT.set(0.09);
                                    Output.Motor.DRIVE_RIGHT.set(-0.09);
                                    state++;

                                } break;
                            case 3:

                                Output.Motor.DRIVE_LEFT.set(0);
                                Output.Motor.DRIVE_RIGHT.set(0);
                                autoType = AutoTypes.SHOOT;
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

                                    Output.Motor.DRIVE_LEFT.set(0.55);
                                    Output.Motor.DRIVE_RIGHT.set(-0.55);
                                    Output.Motor.INTAKE_WHEELS.set(-1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 3900) { //38003ws3

                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state++;

                                } break;

                            case 3:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Motor.DRIVE_LEFT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                Output.Motor.DRIVE_RIGHT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                state++;

                                break;
                            case 4:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state = 0;
                                    autoType = AutoTypes.SHOOT;

                                } break;
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

                                    Output.Motor.DRIVE_LEFT.set(0.30);
                                    Output.Motor.DRIVE_RIGHT.set(-0.30);
                                    Output.Motor.INTAKE_WHEELS.set(1);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 2:

                                if (System.currentTimeMillis() - autoTimeFlag > 4000) { //3800

                                    Output.Solenoid.INTAKE.set(true);
                                    Output.Motor.INTAKE_WHEELS.set(0);
                                    Output.Motor.DRIVE_LEFT.set(0.6);
                                    Output.Motor.DRIVE_RIGHT.set(-0.6);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 3:

                                if (System.currentTimeMillis() - autoTimeFlag > 2500) { //3800

                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    autoTimeFlag = System.currentTimeMillis();
                                    state++;

                                } break;

                            case 4:

                                autoTimeFlag = System.currentTimeMillis();
                                Output.Motor.DRIVE_LEFT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                Output.Motor.DRIVE_RIGHT.set(-0.4 * autoSelectionTable.getNumber("direction", 0));
                                state++;

                                break;
                            case 5:

                                if (System.currentTimeMillis() - autoTimeFlag > 300) {


                                    Output.Motor.DRIVE_LEFT.set(0);
                                    Output.Motor.DRIVE_RIGHT.set(0);
                                    state = 0;
                                    autoType = AutoTypes.SHOOT;

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
                                autoType = AutoTypes.SHOOT;
                                state = 0;

                                break;
                        } break;

                } break;

            case ALIGN:

                if (!autoConfig[1]) {

                    state = 0;
                    autoType = AutoTypes.SHOOT;
                }


                double alignConfig = autoSelectionTable.getNumber("Align Config", 0);

                boolean far = autoSelectionTable.getBooleanArray("Aim + Shoot Config", new boolean[4])[3];

                System.out.println(far);

                if ((autoSelectionTable.getBooleanArray("Aim + Shoot Config", new boolean[0])[0] || far)
                        && state == 0) state = 10;

                switch (state) {

                    case 10:

                        autoTimeFlag = System.currentTimeMillis();
                        state++;

                        break;
                    case 11:

                        if (System.currentTimeMillis() - autoTimeFlag > (far ? 1000 : 450)) {

                            if (far) {

                                state++;

                            } else {

                                state = 0;
                                autoType = AutoTypes.SHOOT;
                            }

                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);

                        } else {

                            Output.Motor.DRIVE_LEFT.set(1);
                            Output.Motor.DRIVE_RIGHT.set(-1);

                        } break;
                    case 12:

                        if (targeting.centerX().length != 0) {

                            state = 0;
                            autoType = AutoTypes.SHOOT;
                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);

                        } else {

                            Output.Motor.DRIVE_LEFT.set(-0.3);
                            Output.Motor.DRIVE_RIGHT.set(-0.3);

                        } break;
                    case 0:

                        System.out.println(Input.MXP.NAV_X.getYaw());

                        if (alignConfig > 0 && Input.MXP.NAV_X.getYaw() < -40) {

                            state++;
                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);
                            autoTimeFlag = System.currentTimeMillis();

                        } else if (alignConfig < 0 && Input.MXP.NAV_X.getYaw() > 40) {

                            state++;
                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);
                            autoTimeFlag = System.currentTimeMillis();

                        } else {

                            Output.Motor.DRIVE_LEFT.set(-0.3 * alignConfig);
                            Output.Motor.DRIVE_RIGHT.set(-0.3 * alignConfig);

                        } break;
                    case 1:

                        if (System.currentTimeMillis() - autoTimeFlag > 1000) {

                            state++;
                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);

                        } else {

                            Output.Motor.DRIVE_LEFT.set(1);
                            Output.Motor.DRIVE_RIGHT.set(-1);

                        } break;
                    case 2:

                        if (targeting.centerX().length != 0) {

                            state = 0;
                            autoType = AutoTypes.SHOOT;
                            Output.Motor.DRIVE_LEFT.set(0);
                            Output.Motor.DRIVE_RIGHT.set(0);

                        } else {

                            Output.Motor.DRIVE_LEFT.set(0.3 * alignConfig);
                            Output.Motor.DRIVE_RIGHT.set(0.3 * alignConfig);

                        } break;

                } break;

            case SHOOT:

                if (!autoConfig[2]) {

                    state = -1;
                    autoType = AutoTypes.NOTHING;
                }

                switch (state) {

                    case 0:

                        drive.ScheduleTask(DriveTrain.TaskType.AIM, 0d);

                        if (drive.hasTasks()) state++;

                        break;
                    case 1:

                        if (Output.Motor.DRIVE_LEFT.hasStopped() && targeting.onTarget()) {

                            shooter.cock();
                            state++;

                        } break;
                    case 2:

                        if (shooter.shoot()) {

                            state++;
                            shooter.disablePID();

                        } break;
                } break;
        }
    }
}
