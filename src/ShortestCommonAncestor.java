import java.util.ArrayDeque;
import java.util.Deque;

public class ShortestCommonAncestor {

    public Digraph G;

    //constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G){
        this.G = new Digraph(G);
    }

    //length of the shortest ancestral path between v and w
    public int length(int v, int w){

        if (!((v >= 0 && v <= this.G.V() - 1) && (w >= 0 && w <= this.G.V() - 1))) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        int ancestor = this.ancestor(v,w);
        int pathLength;
        if (ancestor == - 1) {
            pathLength = -1;
        } else {
            pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor);
        }


        return pathLength;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w){
        if (!((v >= 0 && v <= this.G.V() - 1) && (w >= 0 && w <= this.G.V() - 1))) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int shortesAncestor = -1;
        int shortesPath = Integer.MAX_VALUE;
        Deque<Integer> ancestors = new ArrayDeque<Integer>();

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                ancestors.push(i);
            }
        }

        for (Integer integer : ancestors) {
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < shortesPath) {
                shortesPath = (bfsV.distTo(integer) + bfsW.distTo(integer));
                shortesAncestor = integer;
            }
        }
        return shortesAncestor;
    }

    //length of the shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB){

        if (!isValid(subsetA, subsetB)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, subsetB);

        int ancestor = ancestor(subsetA.iterator().next(), subsetB.iterator().next());
        int pathLength;
        if (ancestor == - 1) {
            pathLength = -1;
        } else {
            pathLength = bfsV.distTo(ancestor) + bfsW.distTo(ancestor); //we start from -2 to exclude the edge vertices
        }


        return pathLength;

    }

    //a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB){

        int shortestPath = Integer.MAX_VALUE;
        Deque<Integer> ancestors = new ArrayDeque<Integer>();
        int ancestor = -1;

        if (!isValid(subsetA, subsetB)) {
            throw new IndexOutOfBoundsException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, subsetB);

        for (int i = 0; i < this.G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                ancestors.push(i);
            }
        }

        for (Integer integer : ancestors) {
            if ((bfsV.distTo(integer) + bfsW.distTo(integer)) < shortestPath) {
                shortestPath = (bfsV.distTo(integer) + bfsW.distTo(integer));
                ancestor = integer;
            }
        }
        return ancestor;

    }

    public boolean isValid(int v) {
        return (v >= 0 && v <= this.G.V() - 1);
    }

    private boolean isValid(Iterable<Integer> v, Iterable<Integer> w) {

        for (Integer integer : w) {
            if (!isValid(integer)) {
                return false;
            }
        }

        for (Integer integer : v) {
            if (!isValid(integer)) {
                return false;
            }
        }

        return true;
    }


    //unit testing (required)
    public static void main(String[] args) {

        In in = new In("digraph25.txt");
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while(!StdIn.isEmpty()){
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }

    }

}
