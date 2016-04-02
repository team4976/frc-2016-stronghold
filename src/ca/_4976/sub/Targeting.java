package ca._4976.sub;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting {

    public static final double ERROR = 10;

    public static final double PID_SETPOINT = 0;

    public ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");
    public ITable lines = NetworkTable.getTable("GRIP").getSubTable("GoalLines");

    public Double getLargestGoal() {

        Double goalX = null;

        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});
        Double[] x1 = lines.getNumberArray("x1", new Double[] {0.0});
        Double[] x2 = lines.getNumberArray("x2", new Double[] {0.0});

        Double largestArea = 0.0;

        double angleOffset = 0;

        if (x1.length > 0 && x1.length == x2.length)
            for (int i = 0; i < x1.length; i++)
                if (x2[i] - x1[i] > angleOffset) angleOffset = x2[i] - x1[i];

        System.out.println("Angle: " + angleOffset);


        if (area.length > 0 && centerX.length > 0)
            for (int i = 0; i < area.length; i++)
                if (area[i] > largestArea) {
                    largestArea = area[i];
                    goalX = (centerX[i] + angleOffset / 2) - 160;
                }

        return goalX;
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
