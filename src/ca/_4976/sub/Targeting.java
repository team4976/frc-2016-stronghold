package ca._4976.sub;

import ca._4976.io.Output;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting {

    public static final double ERROR = 5;

    public ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");

    public boolean aim() {

        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] width = contours.getNumberArray("width", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});

        double largestArea = 0.5;
        int goalI = -1;

        for (int i = 0; i < area.length; i++)
            if (area[i] > largestArea) {
                largestArea = area[i];
                goalI = i;
            }

        if (goalI >= 0) {
            double edge = centerX[goalI] + (width[goalI] / 2);
            if (!(edge >= 160 - ERROR && edge <= 160 + ERROR)) {
                if (edge < 160 - ERROR) {
                    Output.Motor.DRIVE_LEFT.set(0.4);
                    Output.Motor.DRIVE_RIGHT.set(0.4);
                } else if (edge > 160 + ERROR) {
                    Output.Motor.DRIVE_LEFT.set(-0.4);
                    Output.Motor.DRIVE_RIGHT.set(-0.4);
                }
            } else {
                return true;
            }
        }

        return false;
    }

}
