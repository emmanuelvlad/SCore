package com.ssomar.scoretestrecode.sobject;

import com.google.common.io.ByteStreams;
import com.ssomar.score.splugin.SPlugin;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public abstract class NewSObjectLoader<T extends NewSObject> {

    private SPlugin sPlugin;
    private String defaultObjectsPath;
    @Getter
    private Map<String, String> randomIdsDefaultObjects;
    private NewSObjectManager sObjectManager;
    private int maxFreeObjects;
    private int cpt;
    private Logger logger;

    public NewSObjectLoader(SPlugin sPlugin, String defaultObjectsPath, NewSObjectManager<T> sObjectManager, int maxFreeObjects) {
        this.sPlugin = sPlugin;
        this.logger = sPlugin.getPlugin().getServer().getLogger();
        this.defaultObjectsPath = defaultObjectsPath;
        this.sObjectManager = sObjectManager;
        this.maxFreeObjects = maxFreeObjects;
        this.cpt = 0;
    }

    public static File searchFileOfObjectInFolder(String id, File folder) {
        List<String> listFiles = Arrays.asList(folder.list());
        Collections.sort(listFiles);

        for (String s : listFiles) {
            File fileEntry = new File(folder + "/" + s);
            if (fileEntry.isDirectory()) {
                File result = null;
                if ((result = searchFileOfObjectInFolder(id, fileEntry)) == null)
                    continue;
                else
                    return result;
            } else {
                if (!fileEntry.getName().contains(".yml") || fileEntry.getName().contains(".txt"))
                    continue;
                String currentId = fileEntry.getName().split(".yml")[0];
                if (id.equals(currentId))
                    return fileEntry;
            }
        }
        return null;
    }

    public abstract void load();

    public abstract Map<String, List<String>> getPremiumDefaultObjectsName();

    public abstract Map<String, List<String>> getFreeDefaultObjectsName();

    public Map<String, List<String>> getAllDefaultObjectsName() {
        Map<String, List<String>> defaultObjects = getFreeDefaultObjectsName();
        Map<String, List<String>> premDefaultObjects = getPremiumDefaultObjectsName();
        for (String key : defaultObjects.keySet()) {
            List<String> mergeList = defaultObjects.get(key);
            mergeList.addAll(premDefaultObjects.get(key));
            defaultObjects.put(key, mergeList);
        }
        return defaultObjects;
    }

    public void createDefaultObjectsFile(Boolean isPremiumLoading) {
        createDefaultObjectsFile(isPremiumLoading, false);
    }

    public void createDefaultObjectsFile(Boolean isPremiumLoading, boolean exists) {

        String objectName = sPlugin.getObjectName().toLowerCase();

        if (!exists)
            logger.severe(sPlugin.getNameDesign() + " CANT LOAD YOUR " + objectName.toUpperCase() + ", FOLDER '" + objectName + "' not found !");
        logger.severe(sPlugin.getNameDesign() + " DEFAULT " + objectName.toUpperCase() + " CREATED !");

        Map<String, List<String>> defaultObjects;
        if (!isPremiumLoading) defaultObjects = this.getFreeDefaultObjectsName();
        else defaultObjects = this.getAllDefaultObjectsName();


        for (String folder : defaultObjects.keySet()) {

            File fileFolder = new File(sPlugin.getPlugin().getDataFolder() + "/" + objectName + "/" + folder);

            fileFolder.mkdirs();

            for (String id : defaultObjects.get(folder)) {
                try {
                    File pdfile = new File(sPlugin.getPlugin().getDataFolder() + "/" + objectName + "/" + folder + "/" + id + ".yml");
                    InputStream in = this.getClass().getResourceAsStream(defaultObjectsPath + folder + "/" + id + ".yml");

                    if (!pdfile.exists()) {
                        sPlugin.getPlugin().getDataFolder().mkdirs();
                        pdfile.getParentFile().mkdirs();
                        pdfile.createNewFile();
                    } else
                        continue;

                    OutputStream out = new FileOutputStream(pdfile);
                    byte[] buffer = new byte[1024];
                    int current = 0;

                    while ((current = in.read(buffer)) > -1)
                        out.write(buffer, 0, current);

                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadDefaultPremiumObjects(Map<String, List<String>> defaultObjectsName) {

        /* SET RANDOM ID TO NOT INTERFER WITH OTHER EI and to make it, One time session (will not work after a restart) because only for test*/
        randomIdsDefaultObjects = new HashMap<>();
        for (String folder : defaultObjectsName.keySet()) {
            for (String id : defaultObjectsName.get(folder)) {
                randomIdsDefaultObjects.put(id, UUID.randomUUID().toString());
            }
        }

        for (String folder : defaultObjectsName.keySet()) {
            for (String id : defaultObjectsName.get(folder)) {

                InputStream in = this.getClass().getResourceAsStream(defaultObjectsPath + folder + "/" + id + ".yml");
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                Optional<T> oOpt;
                if (!(oOpt = this.getObjectByReader(reader, id, false, randomIdsDefaultObjects)).isPresent()) continue;

                T o = oOpt.get();
                o.setId(randomIdsDefaultObjects.get(o.getId()));
                sObjectManager.addDefaultLoadedObject(o);
            }
        }
    }

    public void loadDefaultEncodedPremiumObjects(Map<String, List<String>> defaultObjectsName) {

        /* SET RANDOM ID TO NOT INTERFER WITH OTHER EI and to make it, One time session (will not work after a restart) because only for test*/
        if (randomIdsDefaultObjects == null) randomIdsDefaultObjects = new HashMap<>();
        for (String folder : defaultObjectsName.keySet()) {
            for (String id : defaultObjectsName.get(folder)) {
                randomIdsDefaultObjects.put(id, UUID.randomUUID().toString());
            }
        }

        for (String folder : defaultObjectsName.keySet()) {
            for (String id : defaultObjectsName.get(folder)) {

                InputStream in = this.getClass().getResourceAsStream(defaultObjectsPath + folder + "/" + id + ".pack");
                InputStream decodedIn = null;
                int length = -1;
                try {
                    length = in.available();
                    byte[] fileBytes = new byte[length];
                    in.read(fileBytes, 0, fileBytes.length);
                    in.close();
                    byte[] decoded = Base64.getDecoder().decode(fileBytes);
                    decodedIn = new ByteArrayInputStream(decoded);

                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(decodedIn, StandardCharsets.UTF_8));
                Optional<T> oOpt;
                if (!(oOpt = this.getObjectByReader(reader, id, false, randomIdsDefaultObjects)).isPresent()) continue;

                T o = oOpt.get();
                o.setId(randomIdsDefaultObjects.get(o.getId()));
                sObjectManager.addDefaultLoadedObject(o);
            }
        }
    }

    public void loadObjectByFile(String filePath, boolean isPremiumLoading) {
        try {
            File fileEntry = new File(filePath);
            if (!fileEntry.getName().contains(".yml") || fileEntry.getName().contains(".txt")) return;
            String id = fileEntry.getName().split(".yml")[0];

            if (!isPremiumLoading && cpt >= maxFreeObjects) {
                logger.severe(sPlugin.getNameDesign() + " REQUIRE PREMIUM: to add more than " + maxFreeObjects + " " + sPlugin.getObjectName() + " you need the premium version");
                return;
            }

            Optional<T> oOpt;
            if (!(oOpt = this.getObjectByFile(fileEntry, id, true)).isPresent()) {
                logger.severe(sPlugin.getNameDesign() + " Error the file " + filePath + " can't be loaded !");
                return;
            }
            sObjectManager.addLoadedObject(oOpt.get());
            cpt++;
            logger.fine(sPlugin.getNameDesign() + " " + id + " was loaded !");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadObjectsInFolder(File folder, boolean isPremiumLoading) {
        List<String> listFiles = Arrays.asList(folder.list());
        Collections.sort(listFiles);

        for (String s : listFiles) {
            File fileEntry = new File(folder.getPath() + "/" + s);
            if (fileEntry.isDirectory()) {
                loadObjectsInFolder(fileEntry, isPremiumLoading);
            } else {
                loadObjectByFile(folder.getPath() + "/" + s, isPremiumLoading);
            }
        }
    }

    public File searchFileOfObject(String id) {

        List<String> listFiles = Arrays.asList(new File(sPlugin.getPlugin().getDataFolder() + "/" + sPlugin.getObjectName()).list());
        Collections.sort(listFiles);

        for (String s : listFiles) {
            File fileEntry = new File(sPlugin.getPlugin().getDataFolder() + "/" + sPlugin.getObjectName() + "/" + s);
            if (fileEntry.isDirectory()) {
                File result = null;
                if ((result = searchFileOfObjectInFolder(id, fileEntry)) == null)
                    continue;
                else
                    return result;
            } else {
                if (!fileEntry.getName().contains(".yml") || fileEntry.getName().contains(".txt"))
                    continue;
                String currentId = fileEntry.getName().split(".yml")[0];
                if (id.equals(currentId))
                    return fileEntry;

            }
        }
        return null;
    }

    public Optional<T> getObjectByFile(File file, String id, boolean showError) {
        try {
            if (this.CreateBackupFilIfNotValid(file)) return null;
            configVersionsConverter(file);
            FileConfiguration objectConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
            return getObject(objectConfig, id, showError);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<T> getObjectByReader(Reader reader, String id, boolean showError, Map<String, String> wordsToReplace) {
        try {
            FileConfiguration firstConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(reader);
            String toStr = firstConfig.saveToString();
            for (String word : wordsToReplace.keySet()) {
                toStr = toStr.replaceAll(word, wordsToReplace.get(word));
            }
            YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(toStr);
            FileConfiguration objectConfig = (FileConfiguration) config;

            return getDefaultObject(objectConfig, id, showError);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void configVersionsConverter(File file);

    public Optional<T> getObjectById(String id, boolean showError) {
        return getObjectByFile(searchFileOfObject(id), id, showError);
    }

    public Optional<T> getObject(FileConfiguration objectConfig, String id, boolean showError) {
        return getObject(objectConfig, id, showError, !sPlugin.isLotOfWork(), searchFileOfObject(id).getPath());
    }

    public Optional<T> getDefaultObject(FileConfiguration objectConfig, String id, boolean showError) {
        return getObject(objectConfig, id, showError, true, "");
    }

    public abstract Optional<T> getObject(FileConfiguration objectConfig, String id, boolean showError, boolean isPremiumLoading, String path);


    public boolean CreateBackupFilIfNotValid(File file) {

        YamlConfiguration loader = new YamlConfiguration();

        @SuppressWarnings("unused")
        FileConfiguration config;
        try {
            loader.load(file);
            config = (FileConfiguration) loader;
            return false;
        } catch (Exception e) {
            sPlugin.getPlugin().getLogger().severe("Error when loading " + file.getName() + ", your config is not made correctly ! this website can help you to resolve your problem: https://codebeautify.org/yaml-validator ");
            File fileBackup = new File(file.getParent() + "/" + file.getName() + ".txt");

            int i = 1;
            while (fileBackup.exists()) {
                fileBackup = new File(file.getParent() + "/" + file.getName() + i + ".txt");
                i++;
            }

            if (!fileBackup.exists()) {
                try {
                    fileBackup.getParentFile().mkdir();
                    fileBackup.createNewFile();
                    InputStream is = new FileInputStream(file);
                    OutputStream os = new FileOutputStream(fileBackup);
                    ByteStreams.copy(is, os);
                    is.close();
                    os.close();
                } catch (Exception e2) {
                    throw new RuntimeException("Unable to create the backup file: " + file.getName(), e2);
                }

            }
            return true;
        }
    }

    public List<String> getAllObjects() {
        ArrayList<String> result = new ArrayList<>();
        if (new File(sPlugin.getPlugin().getDataFolder() + "/" + sPlugin.getObjectName()).exists()) {
            List<String> listFiles = Arrays.asList(new File(sPlugin.getPlugin().getDataFolder() + "/" + sPlugin.getObjectName()).list());
            Collections.sort(listFiles);

            for (String s : listFiles) {
                File fileEntry = new File(sPlugin.getPlugin().getDataFolder() + "/" + sPlugin.getObjectName() + "/" + s);
                if (fileEntry.isDirectory()) result.addAll(getAllObjectsOfFolder(fileEntry));
                else {
                    if (!fileEntry.getName().contains(".yml")) continue;
                    String id = fileEntry.getName().split(".yml")[0];

                    result.add(id);
                }
            }
        }
        return result;
    }

    public List<String> getAllObjectsLowerCases() {
        ArrayList<String> result = new ArrayList<>();
        for (String s : getAllObjects()) {
            result.add(s.toLowerCase());
        }
        return result;
    }

    public List<String> getAllObjectsOfFolder(File folder) {
        ArrayList<String> result = new ArrayList<>();

        List<String> listFiles = Arrays.asList(folder.list());
        Collections.sort(listFiles);

        for (String s : listFiles) {
            File fileEntry = new File(folder + "/" + s);
            if (fileEntry.isDirectory()) result.addAll(getAllObjectsOfFolder(fileEntry));
            else {
                if (!fileEntry.getName().contains(".yml")) continue;
                String id = fileEntry.getName().split(".yml")[0];

                result.add(id);
            }
        }

        return result;
    }

    public void reload() {
        load();
    }

    public void resetCpt() {
        this.cpt = 0;
    }
}
