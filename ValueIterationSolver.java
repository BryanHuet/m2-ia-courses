
import java.util.*;
import control.*;
import mdp.*;
import bw.mdp.BWAction;
import bw.mdp.BWState;
import bw.*;
public class ValueIterationSolver<S, A>{

    private MDP<S, A> mdp;
    private double epsilon;
    private double gamma;

    public ValueIterationSolver(MDP<S, A> mdp, double epsilon, double gamma){
        this.mdp = mdp;
        this.epsilon = epsilon;
        this.gamma = gamma;

    }
    /*
        public Double backup1(S initialState){
            double currentMax = -10000.1;
            double som = 0.0;

            for(A action: this.mdp.actions(initialState)){
                for (S nextState: this.mdp.getTransitionFunction().getNextStatesDistribution(initialState,action).support()){
                    som += this.mdp.getTransitionFunction().getTransitionProbability(initialState,action,nextState)
                        * ((this.mdp.getRewardFunction().getReward(initialState,action,nextState)
                            + gamma * this.backup1(nextState)));
                }


                if (som >= currentMax){
                    currentMax=som;
                }
            }

            return currentMax;
        }
    */
    public HashMap<A, Double> backup(HashMap<A, Double> initialReward, S initialState, A action){

        HashMap<A, Double> rewards = new HashMap<>();
        double som = 0.0;

        if (initialReward.containsKey(action)){
            som += initialReward.get(action);
        } else {

            for (S nextState: this.mdp.getTransitionFunction().getNextStatesDistribution(initialState,action).support()){
                rewards.put(action,som);
                som += this.mdp.getTransitionFunction().getTransitionProbability(initialState,action,nextState)
                        * ((this.mdp.getRewardFunction().getReward(initialState,action,nextState)
                        + gamma * backup(rewards,nextState,action).get(action)));
            }
        }

        rewards.put(action,som);

        return rewards;
    }

    public StationaryPolicy<S,A> buildPolicy(){
        return new StationaryPolicy<S, A>() {
            @Override
            public A get(S s) {
                double maxReward = -10000.1;
                A bestAction = null;
                HashMap<A, Double> rewards = new HashMap<>();

                for(A action: mdp.actions(s)){

                    rewards = backup(rewards, s, action);



                }

                for (Map.Entry<A, Double> entry : rewards.entrySet()) {
                    if(entry.getValue()> maxReward){
                        maxReward = entry.getValue();
                        bestAction=entry.getKey();
                    }
                }




                return bestAction;
            }
        };
    }




}