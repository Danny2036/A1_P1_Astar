package edu.cwru.sepia.agent.minimax;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.environment.model.history.History;
import edu.cwru.sepia.environment.model.state.State;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class MinimaxAlphaBeta extends Agent {

    private final int numPlys;

    public MinimaxAlphaBeta(int playernum, String[] args)
    {
        super(playernum);

        if(args.length < 1)
        {
            System.err.println("You must specify the number of plys");
            System.exit(1);
        }

        numPlys = Integer.parseInt(args[0]);
    }

    @Override
    public Map<Integer, Action> initialStep(State.StateView newstate, History.HistoryView statehistory) {
        return middleStep(newstate, statehistory);
    }

    @Override
    public Map<Integer, Action> middleStep(State.StateView newstate, History.HistoryView statehistory) {
        GameStateChild bestChild = alphaBetaSearch(new GameStateChild(newstate),
                numPlys,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                true);

        return bestChild.action;
    }

    @Override
    public void terminalStep(State.StateView newstate, History.HistoryView statehistory) {

    }

    @Override
    public void savePlayerData(OutputStream os) {

    }

    @Override
    public void loadPlayerData(InputStream is) {

    }

    /**
     * You will implement this.
     *
     * This is the main entry point to the alpha beta search. Refer to the slides, assignment description
     * and book for more information.
     *
     * Try to keep the logic in this function as abstract as possible (i.e. move as much SEPIA specific
     * code into other functions and methods)
     *
     * @param node The action and state to search from
     * @param depth The remaining number of plys under this node
     * @param alpha The current best value for the maximizing node from this node to the root
     * @param beta The current best value for the minimizing node from this node to the root
     * @return The best child of this node with updated values
     */
    public GameStateChild alphaBetaSearch(GameStateChild node, int depth, double alpha, double beta, boolean isMax)
    {
    	//A-B Pruning pseudocode https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning#Pseudocode
    	//Base case
    	if (depth == 0){		
    		return node; // return best state
    	}
    	
    	//Get children in optimal expansion order
    	List<GameStateChild> children = orderChildrenWithHeuristics(node.state.getChildren());
    	
    	//If we want to maximize
    	if (isMax){
    		double v = Double.NEGATIVE_INFINITY;
    		for (GameStateChild child : children){
	    		GameStateChild tempNode = alphaBetaSearch(child, depth - 1, alpha, beta, false);
	    		double tempUtil = tempNode.state.getUtility();
	    		v = Math.max(v, tempUtil);	    		
	    		
		    	// Set node to child with v utility
	    		if (v == tempUtil){
	    			node = tempNode;
	    		}
	    		alpha = Math.max(alpha, v);
	    		
	    		if (beta <= alpha){ break;	/* Beta cutoff */ }
	    	}	    
	    } 
    	//If we want to minimize
    	else{	
	    	double v = Double.POSITIVE_INFINITY;	
	    	for (GameStateChild child : children){	
	    		GameStateChild tempNode = alphaBetaSearch(child, depth - 1, alpha, beta, true);
	    		double tempUtil = tempNode.state.getUtility();	    		
	    		v = Math.min(v, tempUtil);	    			    			    	
	    		
		    	// Set node to child with v utility
	    		if (v == tempUtil){	    			
	    			node = tempNode;
	    		}
	    		beta = Math.min(beta, v);
	    		
	    		if (beta <= alpha){ break;	/* Alpha cutoff */ }
	    	}
	    } 
        return node;
    }

    /**
     * You will implement this.
     *
     * Given a list of children you will order them according to heuristics you make up.
     * See the assignment description for suggestions on heuristics to use when sorting.
     *
     * Use this function inside of your alphaBetaSearch method.
     *
     * Include a good comment about what your heuristics are and why you chose them.
     *
     * @param children
     * @return The list of children sorted by your heuristic.
     */
    public List<GameStateChild> orderChildrenWithHeuristics(List<GameStateChild> children)
    {
    	PriorityQueue<GameStateChild> retList = new PriorityQueue<GameStateChild>(children.size(), new GameStateComparator());
    	retList.addAll(children);
    	return new ArrayList<GameStateChild>(retList);
    }
}

class GameStateComparator implements Comparator<GameStateChild>{

	@Override
	public int compare(GameStateChild arg0, GameStateChild arg1) {
		if(arg0.state.getUtility() > arg1.state.getUtility()){
			return 1;
		} else if(arg0.state.getUtility() < arg1.state.getUtility()){
			return -1;
		} else{
			return 0;
		}
	}
}
