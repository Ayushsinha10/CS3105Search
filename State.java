import java.util.*;

public class State implements Comparable<State> {
    int row, col, pathCost, heuristic;
  

    public State(int row, int col, int pathCost, int heuristic) {
        this.row = row;
        this.col = col;
        this.pathCost = pathCost;
        this.heuristic = heuristic;
        
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.pathCost + this.heuristic, other.pathCost + other.heuristic);
    }

    public int getRow() {
        return this.row;
    }
    public int getCol(){
        return this.col;
    }
    public int getPathCost(){
        return this.pathCost;
    }
    public int getHeuristic(){
        return this.heuristic;
    }
    public String prints(){
       return "("+this.getRow() + "," + this.getCol()+"):"+this.getHeuristic();
    }
    public String pprints(){
        int fx = this.getPathCost() + this.getHeuristic();
        String ffx = Integer.toString(fx);
        return "("+this.getRow() + "," + this.getCol()+"):"+ffx;
     }
}

