/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.nrg948.vision.visualizer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

/**
 * Add your docs here.
 */
public class AppConfiguration {
  private static final File CONFIG_DIR = new File(
      AppDirsFactory.getInstance().getUserDataDir("NRG948VisionTargetsVisualizer", null, "nrg948"));
  private static Properties config;

  public static File getInitialTargetsDirectory() {
    ensureLoaded();

    var dirName = config.getProperty("openTargetsInitialDir");

    if (dirName == null || dirName.isEmpty()) {
      return null;
    }

    var initialDir = new File(dirName);

    if (!initialDir.exists()) {
      return null;
    }

    return initialDir;
  }

  public static void setInitialTargetsDirectory(File initialDir) {
    ensureLoaded();

    config.setProperty("openTargetsInitialDir", initialDir.getAbsolutePath());
  }

  private static void ensureLoaded() {
    if (config == null) {
      try {
        load();
      } catch (IOException e) {
      }
    }
  }
  
  private static File getConfigFile() {
    return new File(CONFIG_DIR, "config.properties");
  }

  public static void save() throws IOException {
    if (config != null) {
      if (!CONFIG_DIR.exists()) {
        CONFIG_DIR.mkdirs();
      }  
      
      File configFile = getConfigFile();
      FileWriter writer = new FileWriter(configFile);
      
      config.store(writer, "application settings");
    }  
  }  
  
  private static void load() throws IOException {
    Properties newConfiguration = new Properties();
    File configFile = getConfigFile();

    if (configFile.exists()) {
      FileReader reader = new FileReader(configFile);

      newConfiguration.load(reader);
    }

    config = newConfiguration;
  }
}
