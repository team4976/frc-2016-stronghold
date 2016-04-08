package ca._4976.sub;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class Targeting {

    private static final double ERROR = 10;

    private ITable contours = NetworkTable.getTable("GRIP").getSubTable("GoalContours");
    private ITable lines = NetworkTable.getTable("GRIP").getSubTable("GoalLines");

    Double getLargestGoal() {

        Double[] centerX = contours.getNumberArray("centerX", new Double[]{0.0});
        Double[] centerY = contours.getNumberArray("centerY", new Double[]{0.0});
        Double[] area = contours.getNumberArray("area", new Double[]{0.0});
        Double[] height = contours.getNumberArray("height", new Double[]{0.0});
        Double[] width = contours.getNumberArray("height", new Double[]{0.0});
        Double[] x1 = lines.getNumberArray("x1", new Double[] {0.0});
        Double[] x2 = lines.getNumberArray("x2", new Double[] {0.0});
        Double[] y1 = lines.getNumberArray("y1", new Double[] {0.0});
        Double[] y2 = lines.getNumberArray("y2", new Double[] {0.0});

        int selectedLine = 0;

        double largestArea = 0;
        double longestLine = 0;
        double selectedGoalXPos = 0;
        double selectedGoalYPos = 0;
        double selectedGoalWidth = 0;
        double selectedGoalHeight = 0;


        if (area.length > 0 && centerX.length > 0) {

            for (int i = 0; i < area.length; i++)
                if (area[i] > largestArea) {

                    largestArea = area[i];
                    selectedGoalWidth = width[i];
                    selectedGoalHeight = height[i];
                    selectedGoalXPos = centerX[i] - selectedGoalWidth / 2;
                    selectedGoalYPos = centerY[i] - selectedGoalHeight / 2;
                }
        }


        if (x1.length > 0 && (x1.length == x2.length) == (y1.length == y2.length))
            for (int i = 0; i < x2.length; i++) {

                if (
                        x1[i] >= selectedGoalXPos - 1 &&
                        x1[i] <= selectedGoalXPos + selectedGoalWidth + 1 &&
                        x2[i] >= selectedGoalXPos - 1 &&
                        x2[i] <= selectedGoalXPos + selectedGoalWidth + 1 &&
                        y1[i] >= selectedGoalYPos - 1 &&
                        y1[i] <= selectedGoalYPos + selectedGoalHeight + 1 &&
                        y2[i] >= selectedGoalYPos - 1 &&
                        y2[i] <= selectedGoalYPos + selectedGoalHeight + 1
                    ) {
                    if (Math.abs(x1[i] - x2[i]) > longestLine) {

                        longestLine = Math.abs(x1[i] - x2[i]);
                        selectedLine = i;
                    }
                }

            }



        if (x1.length > 0) {

            double point = x1[selectedLine] > x2[selectedLine] ? x1[selectedLine] : x2[selectedLine];

            System.out.println("Point: " + point);
            System.out.println("X1: " + x1[selectedLine]);
            System.out.println("X2: " + x2k[selectedLine]);
            System.out.println("CenterX" + centerX[0]);
            System.out.println("LONGEST: " + longestLine);

            return (point + (longestLine * 0.5)) - 160;
        }

        else return null;
    }

    boolean onTarget() {

        if (getLargestGoal() != null) return Math.abs(getLargestGoal()) < ERROR;

        else return false;
    }

    Double[] centerX() { return contours.getNumberArray("centerX", new Double[]{0.0}); }
}
