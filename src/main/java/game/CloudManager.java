package game;

import actors.Actor;
import actors.Environment.Cloud;

import java.util.List;

/**
 * CloudManager classs handles when and where to spawn clouds. Chances of cloud spawn increase as sessionRunTime increases.
 */
public class CloudManager {
    private Stage stage;
    private List<Actor> clouds;

    /**
     * CloudManager constructor. Takes in stage and current cloud list
     * @param stage
     * @param clouds
     */
    public CloudManager(Stage stage, List<Actor> clouds) {
        this.stage = stage;
        this.clouds = clouds;
    }

    /**
     * randomCloud method. Decides if a cloud should be created at random positons and speed then added to the cloud list.
     * @param sessionRunTime
     */
    public void randomCloud(float sessionRunTime) {
        int cloudSpawnUpper = (int) sessionRunTime / 10 + 1;
//        int cloudSpawnUpper = 150 - (int) sessionRunTime;
//        if (cloudSpawnUpper < 20) cloudSpawnUpper = 20;
//
//        if (Utils.randInt(1, cloudSpawnUpper) == 1) {
        if (clouds.size() < cloudSpawnUpper) {
            Actor cloud = new Cloud(stage);
            cloud.setPosX(Utils.randInt(0, stage.getWidth()));
            cloud.setPosY((int) Utils.randFloat(-stage.getHeight(), 0 - cloud.getHeight()));

            if (cloud.getPosX() > stage.getWidth()/2) cloud.setVx(Utils.randFloat(-3f, -1f));
            else cloud.setVx(Utils.randFloat(1f, 3f));

//            cloud.setVx(Utils.randFloat(1f, 5f));
            cloud.setVy(Utils.randFloat(-1, 1));
            clouds.add(cloud);
        }
    }
}

