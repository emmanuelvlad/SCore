package com.ssomar.scoretestrecode.features.custom.commands;

import com.ssomar.scoretestrecode.features.FeatureAbstract;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureRequireSubTextEditorInEditor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandsAbstractFeature<T, Y extends FeatureInterface<T, Y>> extends FeatureAbstract<T, Y> implements FeatureRequireSubTextEditorInEditor {

    public CommandsAbstractFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription, Material editorMaterial, boolean requirePremium) {
        super(parent, name, editorName, editorDescription, editorMaterial, requirePremium);
    }

    public List<String> prepareActionbarArgs(List<String> commands, String objectName) {
        List<String> result = new ArrayList<>();
        int cpt = 0;
        int placeActionbar = -1;
        int delay = 0;
        boolean isInActionbar = false;

        for (String s : commands) {
            if (s.equalsIgnoreCase("ACTIONBAR ON")) {
                isInActionbar = true;
                placeActionbar = cpt;

            } else if (s.equalsIgnoreCase("ACTIONBAR OFF") && isInActionbar) {
                if (placeActionbar != -1) {
                    delay = delay / 20;
                    String actionbarWithArgs = "ACTIONBAR " + objectName + " " + delay;
                    result.set(placeActionbar, actionbarWithArgs);
                    placeActionbar = -1;
                    delay = 0;
                }
                continue;
            } else {
                if (s.contains("DELAYTICK ") && !s.contains("AROUND") && !s.contains("+++")) {
                    delay = delay + (Integer.valueOf(s.replaceAll("DELAYTICK ", "")));
                } else if (s.contains("DELAY ") && !s.contains("AROUND") && !s.contains("+++")) {
                    delay = delay + (Integer.valueOf(s.replaceAll("DELAY ", "")) * 20);
                }
            }
            result.add(s);
            cpt++;
        }
        if (placeActionbar != -1) {
            delay = delay / 20;
            String actionbarWithArgs = "ACTIONBAR " + objectName + " " + delay;
            result.set(placeActionbar, actionbarWithArgs);
            placeActionbar = -1;
            delay = 0;
        }
        return result;
    }
}
