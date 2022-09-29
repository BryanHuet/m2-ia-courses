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

    public RewardFunction<S,A> backup(RewardFunction<S,A> initialRF){
        double currentMax = -10000.1;
        for (S state : this.mdp.states()){
            Double som = 0.1;
            for(A action: this.mdp.actions(state)){
                for (S nextState: this.mdp.getTransitionFunction().getNextStatesDistribution(state,action).support()){
                    som += this.mdp.getTransitionFunction().getTransitionProbability(state,action,nextState)
                        * ((initialRF.getReward(state,action,nextState)
                            + gamma * this.backup(this.mdp.getRewardFunction()).getReward(state,action,nextState)));
                }
            }
        }

        return null;
    }






}