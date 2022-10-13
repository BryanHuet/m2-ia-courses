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

    public RewardFunction<S,A> backup (RewardFunction<A,S> initialReward){
        return new RewardFunction<S, A>() {
            @Override
            public double getReward(S s, A a, S s1) {
                return 0;
            }
        };
    }

    public StationaryPolicy<S,A> buildPolicy(){
        return new StationaryPolicy<S, A>() {
            @Override
            public A get(S s) {
                return null;
            }
        };
    }




}