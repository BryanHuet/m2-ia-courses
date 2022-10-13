import java.util.Random;
import java.util.*;
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
        //AdHocIslandsPolicy policy = new AdHocIslandsPolicy(map);

        double gamma = 0.9;
        double epsilon = 0.1;
        ValueIterationSolver<BWState, BWAction> solver = new ValueIterationSolver<>(mdp, gamma, epsilon);
        StationaryPolicy<BWState, BWAction> policy = solver.buildPolicy();


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
        double value = 5;
        double gamma = 0.9;
        double epsilon = 0.1;
        TinyMDP tinyMDP = new TinyMDP(value);

        ValueIterationSolver<String, String> solver = new ValueIterationSolver<>(tinyMDP, gamma, epsilon);
        StationaryPolicy<String, String> policy = solver.buildPolicy();

        boolean ok = true;
        for (String state : tinyMDP.states()) {
            Set<String> optimalActions = tinyMDP.optimalActions(state, gamma);
            String computedAction = policy.get(state);
            if (!optimalActions.contains(computedAction)) {
                System.out.println("ERROR: VISolver computed action " + computedAction);
                System.out.println(
                   "Problem is TinyMDP with value " + value + ", gamma is " + gamma + ", epsilon is " + epsilon);
                System.out.println("True set of optimal actions is " + optimalActions);
                ok = false;
                break;
            }
        }

        if (ok) {
            System.out.println("Test passed");
        }


    }
*/

}