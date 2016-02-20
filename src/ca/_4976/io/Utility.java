package ca._4976.io;

import java.util.HashMap;

public class Utility {

    private static HashMap<String, Delay> delays = new HashMap<>();

    public static void startDelay(long delayTime, String name) {
        if (!delays.containsKey(name))
            delays.put(name, new Delay(System.currentTimeMillis(), delayTime));
        else if (delays.get(name).over())
            delays.put(name, new Delay(System.currentTimeMillis(), delayTime));
    }

    public static boolean checkDelay(String name) {
        return delays.get(name).over();
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
