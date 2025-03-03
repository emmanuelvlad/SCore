package com.ssomar.scoretestrecode.features;

import com.ssomar.scoretestrecode.editor.NewGUIManager;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface FeatureRequireMultipleMessageInEditor {

    void askInEditorFirstTime(Player editor, NewGUIManager guiManager);

    void askInEditor(Player editor, NewGUIManager guiManager);

    /**
     * return an error if there is one
     **/
    Optional<String> verifyMessageReceived(String message);

    void addMessageValue(Player editor, NewGUIManager manager, String message);

    void finishEditInEditor(Player editor, NewGUIManager manager, String message);
}
