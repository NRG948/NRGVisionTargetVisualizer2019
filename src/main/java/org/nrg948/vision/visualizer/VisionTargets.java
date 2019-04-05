package org.nrg948.vision.visualizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import com.google.gson.Gson;
import org.opencv.core.Point;

public class VisionTargets {

  private static final double HALF_IMAGE_FOV = (Math.atan(36.0 / 57.125));
  private static final double DEFAULT_HALF_IMAGE_WIDTH = 480 / 2;
  private static final double TARGET_WIDTH_INCHES = 8.0;

  private ArrayList<TargetPair> targetPairs = new ArrayList<TargetPair>();
  private double imageCenterX = DEFAULT_HALF_IMAGE_WIDTH;
  private Gson gson = new Gson();

  public void readTargetPairs(File targetsFile) throws IOException {
    var newTargets = new ArrayList<TargetPair>();

    try (var reader = Files.newBufferedReader(targetsFile.toPath())) {
      newTargets.add(gson.fromJson(reader.readLine(), TargetPair.class));
    }

    this.targetPairs = newTargets;
  }

  public ArrayList<TargetPair> getTargetPairs() {
    return this.targetPairs;
  }

  private TargetPair getDesiredTargets() {
    return this.targetPairs.get(0);
  }

  public Point getCenterOfTargets() {
    return getDesiredTargets().getCenterOfTargets();
  }

  public double getAngleToTarget() {
    double centerX = getCenterOfTargets().x;
    double deltaX = centerX - imageCenterX;
    return Math.toDegrees(Math.atan2(deltaX, imageCenterX / Math.tan(HALF_IMAGE_FOV)));
  }

  // returns the targets position in the feild of view, normalized to a range of 1
  // to -1.
  // this is the same as "zeta" in the 2017 robot.
  public double getNormalizedTargetPosition() {
    double centerX = getCenterOfTargets().x;
    double deltaX = centerX - imageCenterX;
    return deltaX / imageCenterX;
  }

  // public double getHeadingToTarget() {
  // return RobotMap.navx.getAngle() + getAngleToTarget() / 12;
  // }

  public double getDistanceToTarget() {
    TargetPair desiredTarget = getDesiredTargets();
    double targetWidth = (desiredTarget.right.getMinX().x - desiredTarget.left.getMaxX().x);
    double distance = (TARGET_WIDTH_INCHES * imageCenterX / (targetWidth * Math.tan(HALF_IMAGE_FOV)));

    return distance / Math.cos(Math.toRadians(this.getAngleToTarget()));
  }
}
