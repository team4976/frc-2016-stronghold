package frc.team4976.rebellion.sub;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting implements PIDSource {

    private class Contour{

        Double centerX = null;
        Double centerY = null;
        Double area = null;
        Double height = null;
        Double width = null;

        private Contour(

                Double centerX,
                Double centerY,
                Double area,
                Double height,
                Double width
        ) {

            this.centerX = centerX;
            this.centerY = centerY;
            this.area = area;
            this.width = width;
            this.height = height;
        }
    }

    private static final double ERROR = 10;

    private ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");
    private ITable lines = NetworkTable.getTable("GRIP").getSubTable("GoalLines");

    private Contour getLargestContour() {


        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] centerY = contours.getNumberArray("centerY", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});
        Double[] height = contours.getNumberArray("height", new Double[]{0.0});
        Double[] width = contours.getNumberArray("height", new Double[]{0.0});

        Double largestArea = 0.0;
        int selection = 0;


        for (int i = 0; i < area.length; i++)
            if (area[i] > largestArea) {

                largestArea = area[i];
                selection = i;
            }

        if (centerX.length <= selection && centerY.length <= selection &&
                height.length <= selection && width.length <= selection)

            return new Contour(centerX[selection],  centerY[selection],
                    area[selection], height[selection], width[selection]);

        return null;
    }

    boolean onTarget() { return hasTarget() && Math.abs(pidGet()) < ERROR; }

    public boolean hasTarget() { return getLargestContour() != null; }

    @Override public void setPIDSourceType(PIDSourceType pidSource) { }

    @Override public PIDSourceType getPIDSourceType() { return PIDSourceType.kDisplacement; }

    @Override public double pidGet() {

        Double[] x1 = lines.getNumberArray("x1", new Double[] {0.0});
        Double[] x2 = lines.getNumberArray("x2", new Double[] {0.0});
        Double[] y1 = lines.getNumberArray("y1", new Double[] {0.0});
        Double[] y2 = lines.getNumberArray("y2", new Double[] {0.0});

        Contour contour = getLargestContour();

        Double longestLine = 0.0;
        Integer selection = null;

        if ((x1.length == x2.length) == (y1.length == y2.length))
            for (int i = 0; i < x1.length; i++) {

                if (contour != null) {

                    boolean insideContour = true;

                    if (x1[i] < (contour.centerX - (contour.width / 2)) - 4 ||
                            x1[i] > (contour.centerX + (contour.width / 2)) - 4) insideContour = false;

                    if (x2[i] < (contour.centerX - (contour.width / 2)) - 4 ||
                            x2[i] > (contour.centerX + (contour.width / 2)) - 4) insideContour = false;

                    if (y1[i] < (contour.centerY - (contour.height / 2)) - 4 ||
                            y1[i] > (contour.centerY + (contour.height / 2)) - 4) insideContour = false;

                    if (y2[i] < (contour.centerY - (contour.height / 2)) - 4 ||
                            y2[i] > (contour.centerY + (contour.height / 2)) - 4) insideContour = false;

                    if (insideContour && Math.abs(x1[i] - x2[i]) > longestLine) {

                        longestLine = Math.abs(x1[i] - x2[i]);
                        selection = i;
                    }
                }
            }

        if (selection != null) return x2[selection] > x1[selection] ? x2[selection] : x1[selection];

        else return 0;
    }
}
