/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.nrg948.vision.visualizer;

import java.io.File;

import org.nrg948.vision.visualizer.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Add your docs here.
 */
public class AppController extends AnchorPane {

    @FXML private Button openButton;

    @FXML public void openButtonPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new ExtensionFilter("VisionTargets Files", "*.json"));

        File visionTargetsFile = fileChooser.showOpenDialog(App.getMainStage());
    }
}
