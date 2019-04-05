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

    private Gson gson = new Gson();
    private ArrayList<TargetPair> targetPairs = new ArrayList<TargetPair>();

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
                readTargetPairs(targetsFile);

                updateTargetsCanvas();

                // App.getMainStage().setTitle("NRG948 Targets Visualizer - " + targetsFile.getName());
                this.statusText.setText(this.targetPairs.size() + " target pairs found.");
            }
        } catch (IOException ioException) {
            statusText.setText("ERROR: " + ioException.getMessage());
        }
    }

    private void readTargetPairs(File targetsFile) throws IOException {
        var newTargets = new ArrayList<TargetPair>();

        try (var reader = Files.newBufferedReader(targetsFile.toPath())) {
            newTargets.add(gson.fromJson(reader.readLine(), TargetPair.class));
        }

        this.targetPairs = newTargets;
    }

    private void updateTargetsCanvas() {
        var context = this.targetsCanvas.getGraphicsContext2D();

        context.setFill(Color.BLACK);
        context.fill();

        drawTargetPairs();
        drawCenterDot();
    }

    private void drawTargetPairs() {
        for (var targetPair : this.targetPairs) {
            drawTarget(targetPair.left, Color.RED);
            drawTarget(targetPair.right, Color.BLUE);
        }
    }

    private void drawTarget(Target target, Color color) {
        var context = this.targetsCanvas.getGraphicsContext2D();

        double[] xPoints = new double[] { target.getMinX().x, target.getMinX().x, target.getMinY().x,
                target.getMaxY().x };

        double[] yPoints = new double[] { target.getMinX().y, target.getMinX().y, target.getMinY().y,
                target.getMaxY().y };

        context.setFill(color);
        context.fillPolygon(xPoints, yPoints, 4);
    }

    private void drawCenterDot() {
        var context = this.targetsCanvas.getGraphicsContext2D();

        context.setFill(Color.GREEN);
        context.fillOval(this.targetsCanvas.getWidth() / 2, this.targetsCanvas.getHeight() / 2, 5, 5);
    }
}
