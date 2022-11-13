package dependencies.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class DependenciesFinder {
    private Map<String, List<String>> dependencies = new HashMap<>();

    private void addDependencyIfAbsent(String name) {
        dependencies.putIfAbsent(name, new ArrayList<>());
    }

    private void addDependsOn(String dependency, String dependsOn) {
        dependencies.get(dependency).add(dependsOn);
    }

    private List<String> getDepencenciesOf(String dependency) {
        return dependencies.get(dependency);
    }

    private Set<String> findDependencies(String root) {
        Set<String> processed = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(root);
        processed.add(root);
        while (!queue.isEmpty()) {
            String currentDependency = queue.poll();
            for (String v : this.getDepencenciesOf(currentDependency)) {
                if (!processed.contains(v)) {
                    processed.add(v);
                    queue.add(v);
                }
            }
        }
        return processed;
    }

    public List<Set<String>> getGroupOfDependencies() {
        List<Set<String>> result = new ArrayList<>();
        for (Entry<String, List<String>> entry : dependencies.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result.add(this.findDependencies(entry.getKey()));
            }
        }
        return result;
    }
    
    public void printGroupOfDependencies() {
        for (Set<String> group : getGroupOfDependencies()) {
            System.out.println(group.toString());
        }
    }

    public Set<String> getElementsThatDependOnAllOther() {
        Set<String> result = new HashSet<>();
        Set<Entry<String, List<String>>> entries = dependencies.entrySet();
        for (Entry<String, List<String>> entry : entries) {
            if (!entry.getValue().isEmpty()) {
                Set<String> dependencies = this.findDependencies(entry.getKey());
                if (dependencies.size() == entries.size()) {
                    result.add(entry.getKey());
                }
            }
        }
        return result;
    }

    public void addRawDependenciesInput(String input) {
        String[] dependencies = input.split(" ");
        String dependency = dependencies[0];
        this.addDependencyIfAbsent(dependency);

        for (int i = 1; i < dependencies.length; i++) {
            String dependsOn = dependencies[i];
            this.addDependencyIfAbsent(dependsOn);
            this.addDependsOn(dependency, dependsOn);
        }
    }

    public static void main(String[] args) {
        DependenciesFinder dependencies = new DependenciesFinder();
        dependencies.addRawDependenciesInput("A B C");
        dependencies.addRawDependenciesInput("B C E");
        dependencies.addRawDependenciesInput("C G");
        dependencies.addRawDependenciesInput("D A F");
        dependencies.addRawDependenciesInput("E F");
        dependencies.addRawDependenciesInput("F H");
        System.out.println("All groups of dependencies are: ");
        dependencies.printGroupOfDependencies();
        System.out.println("Elements that depend on all other: " + dependencies.getElementsThatDependOnAllOther());
    }
}
