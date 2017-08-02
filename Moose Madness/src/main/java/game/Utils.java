package game;

import java.util.Random;

public class Utils {
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }

    public static double randDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }
}
