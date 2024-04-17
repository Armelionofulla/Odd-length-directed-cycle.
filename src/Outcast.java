import java.util.Objects;

public class Outcast {

    private WordNet worknet;

    //constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.worknet = wordnet;
    }

    //given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {

        String outcast = null;
        int max = 0;

        for (String noun : nouns) {
            int distance = 0;
            for (String noun2 : nouns) {
                if (!Objects.equals(noun, noun2)) {
                    distance += this.worknet.distance(noun, noun2);
                }
            }

            if (distance > max) {
                max = distance;
                outcast = noun;
            }
        }

        return outcast;

    }

    //test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);

        In in1 = new In("outcast1.txt");
        In in2 = new In("outcast2.txt");
        In in3 = new In("outcast3.txt");

        String[] nouns1 = in1.readAllStrings();
        String[] nouns2 = in2.readAllStrings();
        String[] nouns3 = in3.readAllStrings();

        StdOut.println("outcast1.txt" + ": " + outcast.outcast(nouns1));
        StdOut.println("outcast2.txt" + ": " + outcast.outcast(nouns2));
        StdOut.println("outcast3.txt" + ": " + outcast.outcast(nouns3));
    }

}
