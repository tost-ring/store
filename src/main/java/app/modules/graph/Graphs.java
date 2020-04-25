package app.modules.graph;

public class Graphs {

    public static<N> void printGraph(WeightlessGraph<N> graph){
        for(N it : graph.getNodes()){
            System.out.print(it.toString() + " [ ");
            for(N itt : graph.getNodes(it)){
                System.out.print(itt.toString() + " ");
            }
            System.out.println("]");
        }
    }

    public static<N, M> void printGraph(DupleGraph<N, M> graph){
        for(N it : graph.getBlacks()){
            System.out.print(it.toString() + " [ ");
            for(M itt : graph.getWhites(it)){
                System.out.print(itt.toString() + " ");
            }
            System.out.println("]");
        }
    }

    public static<N> String toString(WeightlessGraph<N> graph){
        StringBuilder stringBuilder = new StringBuilder();
        for(N it : graph.getNodes()){
            stringBuilder.append(it).append(" [ ");
            for(N itt : graph.getNodes(it)){
                stringBuilder.append(itt).append(" ");
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
