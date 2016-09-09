package projectaurora.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.input.BOMInputStream;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.DimensionManager;

public class MusicManager {
    private static File musicDir;
    private static String jsonFilename;
    
    private static List<MusicTrack> allTracks;
    private static Map<MusicType, List<MusicTrack>> typeTracks;
    
    public static List<MusicTrack> getTracksForType(MusicType type) {
        List<MusicTrack> tracks = MusicManager.typeTracks.get(type);
        
        if (tracks == null) {
            tracks = new ArrayList<MusicTrack>();
            MusicManager.typeTracks.put(type, tracks);
        }
        
        return tracks;
    }
    
    public static void addTrackToType(MusicType type, MusicTrack track) {
        getTracksForType(type).add(track);
    }
    
    public static void loadMusicPacks(File mcDir) {
        MusicManager.musicDir = new File(mcDir, "auroramusic");
        
        if (!MusicManager.musicDir.exists()) {
            MusicManager.musicDir.mkdirs();
        }
        
        try {
            generateReadme();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (File file : MusicManager.musicDir.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                try {
                    ZipFile zipFile = new ZipFile(file);
                    loadMusicPack(zipFile);
                    zipFile.close();
                } catch (Exception e2) {
                    FMLLog.warning("Failed to load music pack " + file.getName(), new Object[0]);
                    e2.printStackTrace();
                }
            }
        }
        
        for (MusicTrack track : MusicManager.allTracks) {
            String[] trackInfo;
            String[] info = trackInfo = track.getTrackInfo();
            
            for (String s : trackInfo) {
                System.out.println(s);
            }
        }
    }
    
    private static void loadMusicPack(ZipFile zip) throws IOException {
        ZipEntry entry = zip.getEntry(MusicManager.jsonFilename);
        
        if (entry != null) {
            InputStream stream = zip.getInputStream(entry);
            JsonReader reader = new JsonReader(new InputStreamReader(new BOMInputStream(stream), Charsets.UTF_8.name()));
            JsonParser parser = new JsonParser();
            
            int trackCount = 0;
            JsonObject root = parser.parse(reader).getAsJsonObject();
            JsonArray rootArray = root.get("tracks").getAsJsonArray();
            
            for (JsonElement e : rootArray) {
                JsonObject trackData = e.getAsJsonObject();
                
                String filename = trackData.get("file").getAsString();
                MusicTrack track = new MusicTrack(filename);
                
                if (trackData.has("title")) {
                    String title = trackData.get("title").getAsString();
                    track.setTitle(title);
                }
                
                JsonArray types = trackData.get("types").getAsJsonArray();
                
                for (JsonElement r : types) {
                    JsonObject typeData = r.getAsJsonObject();
                    
                    String name = typeData.get("name").getAsString();
                    MusicType type = MusicType.forName(name);
                    
                    if (type != null) {
                        if (typeData.has("weight")) {
                            double weight = typeData.get("weight").getAsDouble();
                            track.addType(type, weight);
                        } else {
                            track.addType(type);
                        }
                    } else {
                        FMLLog.warning("Music: no type for name %s", new Object[] { name });
                    }
                }
                
                if (trackData.has("authors")) {
                    JsonArray authorList = trackData.get("authors").getAsJsonArray();
                    
                    for (JsonElement a : authorList) {
                        String author = a.getAsString();
                        track.addAuthor(author);
                    }
                }
                
                MusicManager.allTracks.add(track);
                
                for (MusicType type2 : track.getAllTypes()) {
                    addTrackToType(type2, track);
                }
                
                ++trackCount;
            }
            
            reader.close();
            FMLLog.info("Successfully loaded music pack " + zip.getName() + " with " + trackCount + " tracks", new Object[0]);
        }
    }
    
    private static void generateReadme() throws IOException {
        File readme = new File(MusicManager.musicDir, "readme.txt");
        readme.createNewFile();
        PrintStream writer = new PrintStream(new FileOutputStream(readme));
        
        Map<MusicType, List<projectaurora.world.biome.AuroraBiome>> typeBiomes = new HashMap<MusicType, List<projectaurora.world.biome.AuroraBiome>>();
        
        //for (DimensionManager.getWorld(0) : DimensionManager.getWorlds()) {
        for (projectaurora.world.biome.AuroraBiome biome : projectaurora.world.biome.AuroraBiome.auroraBiomeList) {
            if (biome != null) {
                MusicType type = biome.getMusicForBiome();
                    
                if (type != null) {
                    List<projectaurora.world.biome.AuroraBiome> list = typeBiomes.get(type);
                        
                    if (list == null) {
                        list = new ArrayList<projectaurora.world.biome.AuroraBiome>();
                        typeBiomes.put(type, list);
                    }
                        
                    list.add(biome);
                }
            }
        }
        //}
        
        writer.println("Music Packs");
        writer.println();
        writer.println("WARNING! This file is recreated at runtime.");
        writer.println("Any edits will not be saved!");
        writer.println();
        writer.println();
        writer.println("For your convenience, here are listed all the type-names that a music pack can use.");
        writer.println();
        
        for (MusicType type2 : MusicType.values()) {
            writer.println(type2.name);
        }
        
        writer.println();
        writer.println("And here follows a second list of the above types, paired with their respective biomes.");
        writer.println();
        
        for (MusicType type2 : MusicType.values()) {
            String s = "";
            
            List<projectaurora.world.biome.AuroraBiome> biomes = typeBiomes.get(type2);
            
            if (biomes != null && !biomes.isEmpty()) {
                for (projectaurora.world.biome.AuroraBiome biome : biomes) {
                    if (s.length() > 0) {
                        s += ", ";
                    }
                    
                    s += biome.biomeName;
                }
            }
            
            writer.println(type2.name + ": {" + s + "}");
        }
        
        writer.close();
    }
    
    static {
        MusicManager.jsonFilename = "music.json";
        MusicManager.allTracks = new ArrayList<MusicTrack>();
        MusicManager.typeTracks = new HashMap<MusicType, List<MusicTrack>>();
    }
    
    private static class MusicTrack {
        private String filename;
        private String title;
        private Map<MusicType, Double> types;
        private static double defaultWeight = 1.0;
        private List<String> authors;
        
        public MusicTrack(String file) {
            this.types = new HashMap<MusicType, Double>();
            this.authors = new ArrayList<String>();
            this.filename = file;
        }
        
        public String getFilename() {
            return this.filename;
        }
        
        public String getTitle() {
            if (this.title != null) {
                return this.title;
            }
            
            return this.filename;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public Set<MusicType> getAllTypes() {
            return this.types.keySet();
        }
        
        public double getTypeWeight(MusicType type) {
            if (this.types.containsKey(type)) {
                return this.types.get(type);
            }
            
            return 0.0;
        }
        
        public void addType(MusicType type, double weight) {
            this.types.put(type, weight);
        }
        
        public void addType(MusicType type) {
            this.addType(type, 1.0);
        }
        
        public void addAuthor(String author) {
            this.authors.add(author);
        }
        
        public List<String> getAuthors() {
            return this.authors;
        }
        
        public String[] getTrackInfo() {
            List<String> list = new ArrayList<String>();
            
            list.add("Title: " + this.getTitle());
            list.add("Filename: " + this.getFilename());
            list.add("Types:");
            
            for (MusicType type : this.getAllTypes()) {
                list.add(">" + type.name + "," + this.getTypeWeight(type));
            }
            
            list.add("Authors:");
            
            for (String auth : this.getAuthors()) {
                list.add(">" + auth);
            }
            
            return list.toArray(new String[0]);
        }
    }
}