package csc435.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexStore {
    private Map<String, List<Pair<String, Integer>>> mainIndex;

    public IndexStore() {
        mainIndex = new HashMap<>();
    }

    public synchronized void insertWord(String filePath, String word, int frequency) {
        List<Pair<String, Integer>> fileList = mainIndex.computeIfAbsent(word, k -> new ArrayList<>());
        fileList.add(new Pair<>(filePath, frequency));
    }

    public synchronized String lookupIndex(String query) {
        StringBuilder results = new StringBuilder();
        String[] words = query.split("\\s+AND\\s+");
        Map<String, Integer> combinedResults = new HashMap<>();

        for (String word : words) {
            if (mainIndex.containsKey(word)) {
                List<Pair<String, Integer>> fileList = mainIndex.get(word);
                for (Pair<String, Integer> pair : fileList) {
                    combinedResults.put(pair.first, combinedResults.getOrDefault(pair.first, 0) + pair.second);
                }
            }
        }

        combinedResults.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(10) // limit to top 10 results
            .forEach(entry -> results.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n"));

        return results.toString();
    }
}
