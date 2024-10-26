import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/********************Starter Code
 * 
 * This class contains some examples of required inputs and outputs
 * 
 * @author alice toniolo
 *
 * we assume parameters are valid, no need to check
 * 
 */

 public class P1main {

	public static final int NOT_IMPLEMENTED=-200;
	public static final int NOT_FOUND=-1;
	public static final int NOT_TERMINATED=-100;

	public static void main(String[] args) {

		if(args.length<4) {
			System.out.println("usage: java P1main <AStar|BestF|Alt|AStarOpt|BestFOpt> <D> <r,c> <coverage> [<verbose>]");
			System.exit(1);
		}

		//process input examples 
		boolean verbose=false;
		if(args[args.length-1].equals("verbose")) {
			verbose=true;
		}
		String algo=args[0];
		int d=Integer.parseInt(args[1]);
		
		String start=args[2];
		int perc = Integer.parseInt(args[3]);
		int coverage=Math.round(d*d*perc/100);
	
		int time_limit=30; //run at most for 30s
		//run your search algorithm p
		int path_cost=runSearch(algo, d, start, coverage, verbose, time_limit);


		/*
		 * The system must print the following information from your search methods
		 * All code below is for demonstration purposes only
		 */

		//Example: java P1main BestF 4 0,0 35



		if(false) {
			//assume 
			path_cost=13;
			if(path_cost>=0) {//path found

				// 1) Print the Frontier at each step before polling the node, using a brief representation of the state only, formatting is up to you

				String frontier_string="";

				//starting point (0,0) 
				//insert node in the frontier, then print the frontier:
				frontier_string="[(0,0):12]";

				System.out.println(frontier_string);

				//extract (0,0) 
				//insert successors in the frontier (3,0), (0,3), (2,2), then print the frontier,  and repeat for all steps until a path is found or not 


				frontier_string="[(3,0):9, (0,3):9, (2,2):9]\n"
						+ "[(3,3):6, (1,2):6, (2,2):9, (0,3):9]\n"
						+ "[(0,3):3, (1,1):3, (1,2):6, (2,2):9, (0,3):9]\n"
						+ "[(2,1):0, (1,1):3, (1,2):6, (2,2):9, (0,3):9]";

				System.out.println(frontier_string);
			}

			// 2) three lines representing the number of nodes explored, and the path and length of the path if any


			int n_explored=5;
			System.out.println(n_explored);


			String path="";
			int path_length=0;
			if(path_cost==NOT_FOUND || path_cost==NOT_TERMINATED) {
				//do nothing
			}else {
				path="(0,0)(3,0)(3,3)(0,3)(2,1)";
				path_length=5;
				System.out.println(path);
				System.out.println(path_length);
			}


		}

		//3) the path cost 

		System.out.println(path_cost);

	}
	private static int calculateHeuristic(int d, int coverage, int visitedCells) {
		return 3 * (coverage - visitedCells);
	}
	private static List<State> getValidMoves(State current, int d, int coverageRequired, int cellsVisited) {
		List<State> moves = new ArrayList<>();
		
		// Possible moves: horizontal/vertical (3 squares), diagonal (2 squares)
		int[][] possibleMoves = {
			{3, 0},  // Move 3 squares to the right
			{-3, 0}, // Move 3 squares to the left
			{0, 3},  // Move 3 squares down
			{0, -3}, // Move 3 squares up
			{2, 2},  // Move 2 squares diagonally (down-right)
			{2, -2}, // Move 2 squares diagonally (down-left)
			{-2, 2}, // Move 2 squares diagonally (up-right)
			{-2, -2},  // Move 2 squares diagonally (up-left)
			
		};
	
		// Current position of the pawn
		int currentRow = current.getRow();
		int currentCol = current.getCol();
	
		// Loop through each possible move
		for (int[] move : possibleMoves) {
			int newRow = currentRow + move[0];
			int newCol = currentCol + move[1];
			
			// Check if the new position is within bounds
			if (newRow >= 0 && newRow < d && newCol >= 0 && newCol < d) {
			    if(move[0] != 0 && move[1] != 0){
					int additionalCost = 4;
					int newPathCost = additionalCost;
					State newState = new State(newRow, newCol, newPathCost, calculateHeuristic(d, coverageRequired, cellsVisited+1));
				    moves.add(newState);
				}
				else {
					int additionalCost = 3;
					int newPathCost = additionalCost;
					State newState = new State(newRow, newCol, newPathCost, calculateHeuristic(d, coverageRequired, cellsVisited+1));
				    moves.add(newState);
				}
				
			}
		}
		
		return moves;
	}
	private static int bestFirst(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic);
        
        // Priority queue for Best-First Search (priority by heuristic)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();
		//Set<String> storedStates = new HashSet<>();
		

        // Add the initial state to the frontier
        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds in milliseconds
				
				return NOT_TERMINATED;
			}
			Iterator<State> it2 = frontier.iterator();
			if(verbose){
				System.out.print("[");
			while(it2.hasNext()){
				State stte = it2.next();
                if(!it2.hasNext()){
					System.out.print(stte.prints());

				}
				else{

					System.out.print(stte.prints()+", ");
				}

			}
			System.out.print("]");
			System.out.println();
		}
            // Get the state with the lowest heuristic
			nodesExplored++;
            State current = frontier.poll();
			total = total +  current.getPathCost();
			//frontier.clear();
			String currKey = "("+current.getRow() + "," + current.getCol()+")";
		//	System.out.println(currKey);
           

            if (verbose) {
            //    System.out.println("Expanding state: " + current + " with heuristic: " + current.getHeuristic());
            }

            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				out.add(currKey);
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
				//	storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
                }

            }
			out.add(currKey);
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
	private static int aStar(int d, String start, int coverageRequired, boolean verbose) {
        // Parse the starting position
		long startTime = System.currentTimeMillis();
        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

        // Initial state with path cost 0 and initial heuristic
        int initialHeuristic = calculateHeuristic(d, coverageRequired, 1);
        State initialState = new State(startRow, startCol, 0, initialHeuristic);
        
        // Priority queue for Best-First Search (priority by heuristic)
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingInt(state -> state.getPathCost() + state.getHeuristic()));
		Queue<String> out = new LinkedList<>();
        Set<String> explored = new HashSet<>();
		//Set<String> storedStates = new HashSet<>();
		

        // Add the initial state to the frontier
        frontier.add(initialState);

        int nodesExplored = 0;
		int total = 0;

        while (!frontier.isEmpty()) {
			long elapsedTime = System.currentTimeMillis() - startTime;
			if (elapsedTime > 30 * 1000) {  // 30 seconds in milliseconds
				
				return NOT_TERMINATED;
			}
			
			Iterator<State> it2 = frontier.iterator();
			if(verbose){
				System.out.print("[");
			while(it2.hasNext()){
				State stte = it2.next();
                if(!it2.hasNext()){
					System.out.print(stte.pprints());

				}
				else{

					System.out.print(stte.pprints()+", ");
				}

			}
			System.out.print("]");
			System.out.println();
		}
            // Get the state with the lowest heuristic
			nodesExplored++;
            State current = frontier.poll();
			total = total +  current.getPathCost();
			//frontier.clear();
			String currKey = "("+current.getRow() + "," + current.getCol()+")";
			//System.out.println(currKey);
			out.add(currKey);

            if (verbose) {
            //    System.out.println("Expanding state: " + current + " with heuristic: " + current.getHeuristic());
            }

            // Check if coverage requirement is met
            if (nodesExplored >= coverageRequired) {
				if(verbose){
				System.out.println(nodesExplored);
				
				Iterator<String> it = out.iterator();
				
				while(it.hasNext()){
					System.out.print(it.next());
				}
				System.out.println();
	
                System.out.println(nodesExplored);
            
			    } 
                return total;
            }

            // Generate valid moves from the current state
            List<State> moves = getValidMoves(current, d, coverageRequired, nodesExplored);

            for (State move : moves) {
                String moveKey = "("+move.getRow() + "," + move.getCol()+")";
				//System.out.print("---moves---");
			   // System.out.println(moveKey+":"+move.getHeuristic());
				
				

                // Only add the move if it hasn't been explored
                if (!explored.contains(moveKey)) {
					//System.out.print("---moves---");
					//System.out.println(moveKey+":"+move.getHeuristic());
					//storedStates.add(moveKey+":"+move.getHeuristic());
                    frontier.add(move);
                    explored.add(currKey);
                }

            }
			
			
	
        }

        // If the coverage requirement is not met and the frontier is empty, return NOT_FOUND
        return NOT_FOUND;
    }
    



	private static int runSearch(String algo, int d, String start, int coverage, boolean verbose, int time_limit) {

        String[] startCoords = start.split(",");
        int startRow = Integer.parseInt(startCoords[0]);
        int startCol = Integer.parseInt(startCoords[1]);

		switch(algo) {

		case "BestF": //run BestF
			return bestFirst(d, start, coverage, verbose);
		case "AStar": //run AStar
			return aStar(d, start, coverage, verbose);
		case "BestFOpt": //run BestF with additional heuristic
			return NOT_IMPLEMENTED;
		case "AStarOpt": //run AStar with additional heuristic
			return NOT_IMPLEMENTED;

		}
		return NOT_IMPLEMENTED;

	}




}