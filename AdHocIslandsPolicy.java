import control.*;
import bw.mdp.BWAction;
import bw.mdp.BWState;
import bw.*;
public class AdHocIslandsPolicy implements StationaryPolicy<BWState, BWAction>{

    private BWMap currentMap;

    public AdHocIslandsPolicy(BWMap map){
        this.currentMap = map;
    
    }



    public BWAction get(BWState state){
        if (state.speed == 0){
            return BWAction.ACCELERATE;
        }

        if (state.j < this.currentMap.width-1){
            return BWAction.EAST;
        } 
        
        return BWAction.SOUTH;
       
    }



}