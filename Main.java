import java.util.Random;
import bw.*;
import bw.maps.*;
import bw.mdp.*;
import bw.visualization.*;
import algorithms.*;
import control.*;
import mdp.*;
import simulation.*;
import toyproblems.*;
import visualization.*;


public class Main{
    public static void main(String args[])
    
    {
        Random rand = new Random();

        // Problem definition
        System.out.print("Building problem...");
        System.out.flush();
        //BWMap map = new SeveralBridgesBWMap();
        BWMap map = new IslandsBWMap();
        int maxSpeed = 3;
        BWMDP mdp = BWMDP.makeStandardProblem(map, maxSpeed);
        System.out.println(" done.");

        // Policy computation
        System.out.print("Building policy...");
        System.out.flush();
        //StationaryPolicy<BWState, BWAction> policy = new UniformMixedPolicy<>(mdp, rand);
        AdHocIslandsPolicy policy = new AdHocIslandsPolicy(map);
        
        System.out.println(" done.");

        // Agent definition, at a random initial state      
        int row = rand.nextInt(map.height);
        int col = rand.nextInt(map.width);
        BWState initialState = mdp.stateFactory.getState(row, col, 0);
        Agent<BWState> agent = new Agent<>(initialState);
        System.out.println("Agent created at initial state " + initialState);       

        // Simulation
        System.out.println("Running simulation forever...");
        BWSimulation simu = new BWSimulation(mdp);
        simu.addAgent(agent, new PolicyBasedAgentController<>(policy, initialState));
        int sleep = 200;
        simu.run(sleep);
        

    }


/* 
    {
        Random rand = new Random();

        // Problem definition
        System.out.print("Building problem...");
        System.out.flush();
        BWMap map = new SeveralBridgesBWMap();
        int maxSpeed = 3;
        BWMDP mdp = BWMDP.makeStandardProblem(map, maxSpeed);
        System.out.println(" done.");

        // Policy computation
        System.out.print("Building policy...");
        System.out.flush();
        //StationaryPolicy<BWState, BWAction> policy = new UniformMixedPolicy<>(mdp, rand);
        AdHocIslandsPolicy policy = new AdHocIslandsPolicy(map);
        System.out.println(" done.");

        // Policy visualization
        for (int speed = 0; speed <= maxSpeed; speed++) {
            System.out.print("Displaying policy at speed " + speed);
            System.out.flush();
            BWPolicyViewer viewer = new BWPolicyViewer(mdp, policy, speed);
            viewer.showFrame("Bridge world policy at speed " + speed);
            System.out.println(" done.");
        }

    }
*/

}