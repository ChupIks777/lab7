package managers;

import java.util.ArrayList;
import java.util.List;
public class ScriptManager {
    private static final List<String> scriptPaths = new ArrayList<>();

    public static void addScript(String scriptPath) {
        scriptPaths.add(scriptPath);
    }

    public static void removeLastScript() {
        scriptPaths.removeLast();
    }

    public static List<String> getScriptPaths() {
        return scriptPaths;
    }
    
    
}
