import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordNet {

    private Digraph G; // Graph
    private ShortestCommonAncestor sca; // SCA

    private int graphLength = 0;// Length of the graph

    private Map<String, ArrayList<Integer>> nouns = new HashMap<String, ArrayList<Integer>>();// List of nouns <noun, List<synsetIds>>
    private Map<Integer, String> synsets = new HashMap<Integer, String>(); // List of synsets <synsetId, List<nouns>>
    private Map<Integer, ArrayList<Integer>> edges = new HashMap<Integer, ArrayList<Integer>>(); // Edges between vertices


    //constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        // Process data files
        processSynsets(synsets);
        processHypernyms(hypernyms);

        // Construct the graph
        this.G = new Digraph(this.graphLength);

        for (Map.Entry<Integer, ArrayList<Integer>> entry : edges.entrySet()) {
            for (Integer w : entry.getValue()) {
                this.G.addEdge(entry.getKey(), w);
            }
        }

        // Check for cycles
        DirectedCycle cycle = new DirectedCycle(this.G);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("Not a valid DAG");
        }

        // Check if not rooted
        int rooted = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!this.G.adj(i).iterator().hasNext())
                rooted++;
        }

        if (rooted != 1) {
            throw new IllegalArgumentException("Not a rooted DAG");
        }

        this.sca = new ShortestCommonAncestor(this.G);

    }

    //the set of all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    //is the word a WordNet noun?
    public boolean isNoun(String word) {

        return this.nouns.containsKey(word);
    }

    //a synset (second field of synsets.txt) that is a shortest common ancestor
    //of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {

        if (!this.nouns.containsKey(noun1) || !this.nouns.containsKey(noun2))
            throw new IllegalArgumentException("Not a valid pair of nouns");

        int ancestor = this.sca.ancestor(this.nouns.get(noun1).get(0), this.nouns.get(noun2).get(0));

        return this.synsets.get(ancestor);
    }

    //distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {

        if (!this.nouns.containsKey(noun1) || !this.nouns.containsKey(noun2))
            throw new IllegalArgumentException("Not a valid pair of nouns");

        return this.sca.length(this.nouns.get(noun1).get(0), this.nouns.get(noun2).get(0));
    }

    private void processSynsets(String synsets) {
        In in = new In(synsets);
        String line = null;

        ArrayList<Integer> currentNounsList = null;
        String currentSynsetNouns = null;

        while ((line = in.readLine()) != null) {

            if (line.equals("")) {
                continue;
            }


            String[] lineElements = line.split(","); // split line
            String[] nouns = lineElements[1].split(" "); // get the second field
            int synsetId = Integer.parseInt(lineElements[0]);

            for (String noun : nouns) {
                // check if noun exists in list
                if (this.nouns.containsKey(noun)) {
                    currentNounsList = this.nouns.get(noun);
                } else {
                    currentNounsList = new ArrayList<Integer>();
                }

                // check if synsetId exists in list
                if (this.synsets.containsKey(synsetId)) {
                    currentSynsetNouns = this.synsets.get(synsetId);
                } else {
                    currentSynsetNouns = new String();
                }


                currentNounsList.add(synsetId);
                currentSynsetNouns = lineElements[1];

                this.nouns.put(noun, currentNounsList);
                this.synsets.put(synsetId, currentSynsetNouns);
            }

            this.graphLength++;
        }
    }

    private void processHypernyms(String hypernym) {
        In in = new In(hypernym);
        String line = null;
        ArrayList<Integer> edgeList;

        while ((line = in.readLine()) != null) {

            if (line.equals("")) {
                continue;
            }

            // split line
            String[] lineElements = line.split(",");

            if (edges.get(Integer.parseInt(lineElements[0])) != null) {
                edgeList = edges.get(Integer.parseInt(lineElements[0]));
            } else {
                edgeList = new ArrayList<Integer>();
            }

            for (int i = 1; i < lineElements.length; i++) {
                edgeList.add(Integer.parseInt(lineElements[i]));
            }

            edges.put(Integer.parseInt(lineElements[0]), edgeList);

        }
    }


    //unit testing (required)
    public static void main(String[] args) {

        WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(w.sca("Abies", "Aberdeen_Angus"));
        System.out.println(w.distance("Abies", "Aberdeen_Angus"));
    }

}
