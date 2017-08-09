package Game;

import actors.Actor;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Utils {

    /**
     * checkCollision method used to determine if an actor has collided with another actor.
     * Uses the base class Rectangle and its method "intersects" to determine if collision has occured.
     * @param actor
     * @param otherActorList
     */
    public static void checkCollision(Actor actor, List<Actor> otherActorList) {
        Rectangle actorBounds = actor.getBounds();

        for (int i = 0; i < otherActorList.size(); i++) {
            Actor otherActor = otherActorList.get(i);

            if (otherActor == null || actor.equals(otherActor)) {
                continue;
            }

            if (actorBounds.intersects(otherActor.getBounds())) {
                actor.collision(otherActor);
                otherActor.collision(actor);
            }
        }
    }

    /**
     * safeSpawn method determines if it is ok to place a motorist at a given location.
     * This helps to avoid having two motorists created on top of one another.
     * @param actor
     * @param otherActorList
     */
    public static boolean safeSpawn(Actor actor, List<Actor> otherActorList) {
        Rectangle actorBounds = actor.getBounds();

        for (int i = 0; i < otherActorList.size(); i++) {
            Actor otherActor = otherActorList.get(i);
            if (actorBounds.intersects(otherActor.getBounds())) return false;
        }
        return true;
    }

    /**
     * randInt method to quickly generate a random integer between a range.
     * @param min
     * @param max
     * @return
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
    }

    /**
     * randFloat method similar to the randInt method but returns a float.
     * @param min
     * @param max
     * @return
     */
    public static float randFloat(float min, float max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextFloat();
    }

    /**
     * keep the gameLoop running at a desired fps
     */
    public static void calculateSleepTime(long usedTime, int targetFPS) {
        long startTime = System.currentTimeMillis();
        usedTime = System.currentTimeMillis() - startTime;

        //calculate sleep time
        if (usedTime == 0) {
            usedTime = 1;
        }

        int timeDiff = (int) ((1000 / usedTime) - targetFPS);

        if (timeDiff > 0) {
            try {
                Thread.sleep(timeDiff / 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * wav looper to handle playing "pleasant" music loops.
     * @param name
     */
    public static void loopMusic(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).loop();
            }
        }).start();
    }


    /**
     * playSound method used to play a wav one time.
     * @param name
     */
    public static void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                ResourceLoader.getInstance().getSound(name).play();
            }
        }).start();
    }
}
