package ca._4976.sub;

import ca._4976.io.Output;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting {

    public static final double ERROR = 10;

    public static final double ALIGNMENT_OFFSET = 2;

    public ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");

    public Double getEdge() {
        Double edge = null;

        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] width = contours.getNumberArray("width", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});

        Double largestArea = 149.0;
        int goalI = -1;

        if (area.length > 0 && centerX.length > 0 && width.length > 0)
            for (int i = 0; i < area.length; i++)
                if (area[i] > largestArea) {
                    largestArea = area[i];
                    goalI = i;
                }

        if (goalI >= 0)
            edge = centerX[goalI] + (width[goalI] / ALIGNMENT_OFFSET);

        return edge;
    }

    public boolean onTarget() {
        Double edge = getEdge();
        if (edge != null)
            return (edge >= 160 - ERROR && edge <= 160 + ERROR);
        return false;
    }

    public boolean aim() {
        Double edge = getEdge();
        if (!onTarget() && edge != null) {
            if (edge < 160 - ERROR) {
                Output.Motor.DRIVE_LEFT.set(0.3);
                Output.Motor.DRIVE_RIGHT.set(0.3);
            } else if (edge > 160 + ERROR) {
                Output.Motor.DRIVE_LEFT.set(-0.3);
                Output.Motor.DRIVE_RIGHT.set(-0.3);
            }
        } else {
            Output.Motor.DRIVE_LEFT.set(0.0);
            Output.Motor.DRIVE_RIGHT.set(0.0);
        }
        return onTarget();
    }

}
