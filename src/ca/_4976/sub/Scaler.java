package ca._4976.sub;

import ca._4976.io.Controller;
import ca._4976.io.Output;
import edu.wpi.first.wpilibj.DriverStation;

import java.sql.Driver;

public class Scaler {

    DriverStation station = DriverStation.getInstance();
    long scalerTimeFlag = System.currentTimeMillis();

    int state = 0;

    public void teleopPeriodic() {

        switch (state) {

            case 0:

                if (Controller.Secondary.Button.BACK.isDownOnce()) { //TODO add time only fire

                    scalerTimeFlag = System.currentTimeMillis();
                    state++;

                } break;
            case 1:
                if (System.currentTimeMillis() - scalerTimeFlag < 10) {

                    Output.Motor.SCALAR.set(0.1);

                } else {

                    Output.Motor.SCALAR.set(0);
                    Output.Solenoid.SCALER.set(true);
                    state++;

                } break;
            case 2:
                if (Controller.Secondary.Button.BACK.isDown()) Output.Motor.SCALAR.set(0.1);

                else Output.Motor.SCALAR.set(0);

                break;
        }
    }
}
