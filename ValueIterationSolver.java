
import java.util.*;

import control.*;
import mdp.*;
import bw.mdp.BWAction;
import bw.mdp.BWState;
import bw.*;

import static java.lang.Math.abs;

public class ValueIterationSolver<S, A> {

    private MDP<S, A> mdp;
    private double epsilon;
    private double gamma;

    public ValueIterationSolver(MDP<S, A> mdp, double epsilon, double gamma) {
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
    public HashMap<S, Double> backup(HashMap<S, Double> initialValue, S state, A action) {

        double delta = 1000.0;
        double value = initialValue.get(state);
        double max_a_value = -100000.0;
        double som = 0.0;

        for (S nextState : mdp.getTransitionFunction().getNextStatesDistribution(state, action).support()) {

            double prob = this.mdp.getTransitionFunction().getTransitionProbability(state, action, nextState);
            double r = this.mdp.getRewardFunction().getReward(state, action, nextState);

            if (initialValue.containsKey(nextState)) {
                som += prob * (r + gamma * initialValue.get(nextState));
            } else {
                initialValue.put(nextState, 0.0);
                som += prob * (r + gamma * backup(initialValue, nextState, action).get(nextState));


            }

            delta = Math.min(delta, abs(value - initialValue.get(state)));
            max_a_value = Math.max(max_a_value, som);
            if (delta < epsilon) break;
        }

        initialValue.put(state, max_a_value);

        return initialValue;
    }

    public Double backup2(HashMap<S, Double> alreadyCalculate, S state){

        if (alreadyCalculate.containsKey(state)){
            return alreadyCalculate.get(state);
        }

        double current_max_a = -10000.0;
        double delta = 100000000;

        for (A action: this.mdp.actions(state)){
            double som = 0.0;
            for (S nextState : mdp.getTransitionFunction().getNextStatesDistribution(state, action).support()) {
                double prob = this.mdp.getTransitionFunction().getTransitionProbability(state, action, nextState);
                double r = this.mdp.getRewardFunction().getReward(state, action, nextState);

                if (alreadyCalculate.containsKey(nextState)){
                    som += prob * (r + gamma * alreadyCalculate.get(nextState));
                    alreadyCalculate.put(nextState,som);


                }else{
                    alreadyCalculate.put(nextState,0.0);
                    som += prob * (r + gamma * backup2(alreadyCalculate,nextState));
                }


            }

            delta = Math.min(delta, abs(som - current_max_a));
            if (delta < epsilon) break;
            current_max_a = Math.max(som,current_max_a);
        }


        return current_max_a;
    }

    public StationaryPolicy<S, A> buildPolicy() {
        return new StationaryPolicy<S, A>() {
            @Override
            public A get(S s) {

                System.out.println("######### START #########");
                A bestAction = null;
                double maxi = -1000.0;
                HashMap<S,Double> statesValues = new HashMap<>();


                for (A action: mdp.actions(s)){
                    double som = 0.0;
                    for (S nextState : mdp.getTransitionFunction().getNextStatesDistribution(s, action).support()) {
                        double prob = mdp.getTransitionFunction().getTransitionProbability(s, action, nextState);
                        double r = mdp.getRewardFunction().getReward(s, action, nextState);
                        if (statesValues.containsKey(nextState)){
                            som += prob * (r + gamma * statesValues.get(nextState));
                        }else{
                            statesValues.put(nextState,0.0);
                            som+= prob * (r + gamma * backup2(statesValues,nextState));
                        }

                    }

                    System.out.println("action : " + action + " som: " + abs(som) );

                    if (maxi<abs(som)){
                        maxi = abs(som);
                        bestAction=action;
                    }


                }


                System.out.println("######### " + bestAction + " #########\n");
                return bestAction;
            }
        };
    }


}