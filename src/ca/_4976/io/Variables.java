package ca._4976.io;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Variables {

    static private Preferences preferences = Preferences.getInstance();
    static private NetworkTable table = NetworkTable.getTable("Preferences");

    public static void robotInit() {

        table.putNumber("Shooter_RPM", preferences.getDouble("Shooter_RPM", 0));
        table.putNumber("P", preferences.getDouble("P", 0));
        table.putNumber("I", preferences.getDouble("I", 0));
        table.putNumber("D", preferences.getDouble("D", 0));
    }

    public static double getNumber(String key, double fallback) { return table.getNumber(key, fallback); }

    public static boolean getBoolean(String key, boolean fallback) { return table.getBoolean(key, fallback); }

    public static void putNumber(String key, double number) {

        table.putNumber(key, number);
        preferences.putDouble(key, number);
    }

    public static void putBoolean(String key, boolean bool) {

        table.putBoolean(key, bool);
        preferences.putBoolean(key, bool);
    }
}
