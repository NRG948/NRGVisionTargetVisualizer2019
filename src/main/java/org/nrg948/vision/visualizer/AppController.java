/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.nrg948.vision.visualizer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import com.google.gson.Gson;

import org.nrg948.vision.visualizer.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Add your docs here.
 */
public class AppController extends AnchorPane {

  @FXML
  private Button openButton;
  @FXML
  private Label statusText;
  @FXML
  private Canvas targetsCanvas;

  private VisionTargets targets = new VisionTargets();

  @FXML
  public void initialize() {
    statusText.setText("Press Open... to open a file.");
  }

  @FXML
  public void openButtonPressed(ActionEvent event) {
    try {
      FileChooser fileChooser = new FileChooser();

      fileChooser.getExtensionFilters().add(new ExtensionFilter("VisionTargets Files", "*.json"));

      File targetsFile = fileChooser.showOpenDialog(App.getMainStage());

      if (targetsFile != null) {
        this.targets.readTargetPairs(targetsFile);

        updateTargetsCanvas();

        App.getMainStage().setTitle("NRG948 Targets Visualizer - " + targetsFile.getName());

        String status = String.format("Distance: %.2f Angle: %.2f Count: %d", this.targets.getDistanceToTarget(),
            this.targets.getAngleToTarget(), this.targets.getTargetPairs().size());
        this.statusText.setText(status);
      }
    } catch (IOException ioException) {
      statusText.setText("ERROR: " + ioException.getMessage());
    }
  }

  private void updateTargetsCanvas() {
    var gc = this.targetsCanvas.getGraphicsContext2D();

    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, this.targetsCanvas.getWidth(), this.targetsCanvas.getHeight());

    drawTargetPairs(gc);
    drawCenterDot(gc);
    drawCenterOfClosestTarget(gc);
  }

  private void drawTargetPairs(GraphicsContext gc) {
    for (var targetPair : this.targets.getTargetPairs()) {
      drawTarget(gc, targetPair.left, Color.RED);
      drawTarget(gc, targetPair.right, Color.BLUE);
    }
  }
  
  
  private void drawTarget(GraphicsContext gc, Target target, Color color) {
    double[] xPoints = new double[] { target.getMinX().x, target.getMinX().x, target.getMinY().x, target.getMaxY().x };
    
    double[] yPoints = new double[] { -target.getMinX().y, -target.getMinX().y, -target.getMinY().y, -target.getMaxY().y };

    gc.setFill(color);
    gc.fillPolygon(xPoints, yPoints, 4);
  }
  
  private void drawCenterDot(GraphicsContext gc) {
    gc.setFill(Color.GREEN);
    gc.fillOval(this.targetsCanvas.getWidth() / 2, this.targetsCanvas.getHeight() / 2, 5, 5);
  }

  private void drawCenterOfClosestTarget(GraphicsContext gc) {
    var center = this.targets.getCenterOfTargets();

    gc.setStroke(Color.PURPLE);
    gc.strokeOval(center.x, center.y, 10, 10);
  }
}
