package ca._4976.sub;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting {

    public static final double ERROR = 10;

    public static final double PID_SETPOINT = 0;

    public ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");

    public Double getLargestGoal() {

        Double[] centerXs = contours.getNumberArray("centerX", new Double[]{0.0});

        if (centerXs.length > 0) return contours.getNumberArray("centerX", new Double[]{0.0})[0] - 160;
        else return 0d;

        /*Double goalX = null;

        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});

        Double largestArea = 0.0;

        if (area.length > 0 && centerX.length > 0)
            for (int i = 0; i < area.length; i++)
                if (area[i] > largestArea) {
                    largestArea = area[i];
                    goalX = centerX[i] - 160;
                }

        return goalX;*/
    }

    public boolean onTarget() {
        Double edge = getLargestGoal();
        if (edge != null)
            return (edge >= 160 - ERROR && edge <= 160 + ERROR);
        return false;
    }

    public Double[] centerX() {
        return contours.getNumberArray("centerX", new Double[]{0.0});
    }

    public Double pidGet() {
        Double goalX = getLargestGoal();
        if (goalX != null)
            return goalX;
        return 0.0;
    }
}
