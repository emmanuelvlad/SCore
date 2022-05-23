package com.ssomar.testRecode.features;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface FeatureParentInterface {

    List<FeatureInterface> getFeatures();

    String getParentInfo();

    ConfigurationSection getConfigurationSection();

    File getFile();

    void reload();

    void openEditor(@NotNull Player player);

    void openBackEditor(@NotNull Player player);

    void save();

}
