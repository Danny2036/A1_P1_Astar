package edu.cwru.sepia.agent.minimax;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.environment.model.state.State;
import edu.cwru.sepia.environment.model.state.State.StateView;
import edu.cwru.sepia.environment.model.state.Unit;
import edu.cwru.sepia.environment.model.state.Unit.UnitView;
import edu.cwru.sepia.util.Direction;

import java.util.*;

/**
 * This class stores all of the information the agent
 * needs to know about the state of the game. For example this
 * might include things like footmen HP and positions.
 *
 * Add any information or methods you would like to this class,
 * but do not delete or change the signatures of the provided methods.
 */
public class GameState {
	private final int xExtent;	// The x dimension of the board
	private final int yExtent;	// The y dimension of the board
	private final List<Integer> resourceIds;	// The list of resource IDs in the game
	private StateView parentState;	// The state view from which this game state was created
	private List<Integer> footmanUnitIds;	// The ids of all footmen in the game
	private List<Integer> archerUnitIds;	// The ids of all archers in the game
	private List<UnitView> units;	// The list of all units in the game
	
	private Map<UnitView, FootmanPosition> footmanPositions = new HashMap<UnitView, FootmanPosition>();	// Map locations to footmen

    /**
     * You will implement this constructor. It will
     * extract all of the needed state information from the built in
     * SEPIA state view.
     *
     * You may find the following state methods useful:
     *
     * state.getXExtent() and state.getYExtent(): get the map dimensions
     * state.getAllResourceIDs(): returns all of the obstacles in the map
     * state.getResourceNode(Integer resourceID): Return a ResourceView for the given ID
     *
     * For a given ResourceView you can query the position using
     * resource.getXPosition() and resource.getYPosition()
     *
     * For a given unit you will need to find the attack damage, range and max HP
     * unitView.getTemplateView().getRange(): This gives you the attack range
     * unitView.getTemplateView().getBasicAttack(): The amount of damage this unit deals
     * unitView.getTemplateView().getBaseHealth(): The maximum amount of health of this unit
     *
     * @param state Current state of the episode
     */
    public GameState(State.StateView state) {
    	this.xExtent = state.getXExtent();
    	this.yExtent = state.getYExtent();
    	this.resourceIds = state.getAllResourceIds();
    	this.units = state.getAllUnits();
    	this.parentState = state;

    	// Find all of the active units in this state
    	footmanUnitIds = new ArrayList<Integer>();
    	archerUnitIds = new ArrayList<Integer>();
    	
    	// Find all of the units in the game and add them to the appropriate list.
    	for (UnitView unit : this.units){
    		String unitTypeName = unit.getTemplateView().getName();
    		
    		if (unitTypeName.equals("Footman")){
    			footmanUnitIds.add(unit.getID());
    			footmanPositions.put(unit, new FootmanPosition(unit.getXPosition(), unit.getYPosition()));    		
    		}else if (unitTypeName.equals("Archer")){
    			archerUnitIds.add(unit.getID());
    		}    		
    	}   	
    }

    /**
     * You will implement this function.
     *
     * You should use weighted linear combination of features.
     * The features may be primitives from the state (such as hp of a unit)
     * or they may be higher level summaries of information from the state such
     * as distance to a specific location. Come up with whatever features you think
     * are useful and weight them appropriately.
     *
     * It is recommended that you start simple until you have your algorithm working. Then watch
     * your agent play and try to add features that correct mistakes it makes. However, remember that
     * your features should be as fast as possible to compute. If the features are slow then you will be
     * able to do less plys in a turn.
     *
     * Add a good comment about what is in your utility and why you chose those features.
     *
     * @return The weighted linear combination of the features
     */
    public double getUtility() {
        return 0.0;
    }

    /**
     * You will implement this function.
     *
     * This will return a list of GameStateChild objects. You will generate all of the possible
     * actions in a step and then determine the resulting game state from that action. These are your GameStateChildren.
     *
     * You may find it useful to iterate over all the different directions in SEPIA.
     *
     * for(Direction direction : Directions.values())
     *
     * To get the resulting position from a move in that direction you can do the following
     * x += direction.xComponent()
     * y += direction.yComponent()
     *
     * @return All possible actions and their associated resulting game state
     */
    public List<GameStateChild> getChildren(){
		return null;
    }
    
    /**
     * Get the cardinal directions for footman movement
     * @return an array of the cardinal directions
     */
    private Direction[] getCardinal(){
    	Direction[] cardinalDirections = new Direction[4];
    	cardinalDirections[0] = Direction.NORTH;
    	cardinalDirections[1] = Direction.SOUTH;
    	cardinalDirections[2] = Direction.EAST;
    	cardinalDirections[3] = Direction.WEST;    
    	
    	return cardinalDirections;
    }
    
    /**
     * Check if the position after a potential move/attack is still on the board
     * @param x
     * @param y
     * @return
     */
    private boolean inBounds(int x, int y){
    	return !(x > this.xExtent || y > this.yExtent || x < 0 || y < 0);    	
    }
    
    /**
     * Check if there is an archer that the footman can attack given its coordinates
     * @param x
     * @param y
     * @return
     */
    private Integer getArcherInRange(int x, int y){
    	for (Integer archerID : archerUnitIds){
    		UnitView archerUnit = this.parentState.getUnit(archerID);
    		
    		if (Math.abs(archerUnit.getXPosition() - x) <= 1 && Math.abs(archerUnit.getYPosition() - y) <= 1){
    			// Can evaluate an attack state
    			return archerID;
    		}
    	}
    	return null;
    }
    
    private class FootmanPosition{
    	public int xPosition;
    	public int yPosition;
    	
    	public FootmanPosition(int x, int y){
    		this.xPosition = x;
    		this.yPosition = y;
    	}
    }
}
