package ca._4976.io;

import java.util.HashMap;

public class Utility {

    private static HashMap<String, Delay> delays = new HashMap<>();

    public static void startDelay(long delayTime, String name) {
        if (!delays.containsKey(name))
            delays.put(name, new Delay(System.currentTimeMillis(), delayTime));
    }

    public static boolean checkDelay(String name) {
        if (delays.containsKey(name))
            return delays.get(name).over();
        return false;
    }

    public static void removeDelay(String name) {
        if (delays.containsKey(name))
            delays.remove(name);
    }

    public static class Delay {

        private long startTime, delayTime;

        public Delay(long startTime, long delayTime) {
            this.startTime = startTime;
            this.delayTime = delayTime;
        }

        public boolean over() {
            return System.currentTimeMillis() - startTime >= delayTime;
        }
    }

}
