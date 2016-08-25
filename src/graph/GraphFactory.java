package graph;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GraphFactory {
    private Date date = new Date();
    private Random random = new Random(date.getTime());

    public enum VariationType{
        DELETION,
        INSERTION,
        MOBILE_ELEMENT,
        SNP,
        INVERSION,
        DUPLICATION;
    }

    public enum Law{
        UNIFORM,
        GAUSSIAN,
        EXPONENTIAL
    }

    final int referenceWeight = 15;
    final int alternativeWeight = 1;

    Graph graph;
    int start;
    int end;
    int nextNodeNumber;

    int variationStart;
    int variationEnd;

    boolean showVariations;

    public Graph getGraph() {
        return graph;
    }

    private void makeBackbone(int numberOfNodes){
        graph = new Graph(numberOfNodes,numberOfNodes-1,true,true);
        for (int i=0; i<numberOfNodes-1;i++){
            graph.addEdge(i,i+1,referenceWeight);
        }
        nextNodeNumber = numberOfNodes;
        start = 0;
        end = numberOfNodes-1;
    }

    private void addVariation(int from, int start, int end, int to){
        if (!graph.addWeightToEdge(from,start,0)) {
            graph.addEdge(from, start,alternativeWeight);
        }
        if (!graph.addWeightToEdge(end,to,0)) {
            graph.addEdge(end, to,alternativeWeight);
        }
    }

    private void addVariation(int from, int to) {
        if (!graph.addWeightToEdge(from,to,0)) {
            graph.addEdge(from, to,alternativeWeight);
        }
    }

    private void makeVariation(int length){
        for (int i=nextNodeNumber; i<nextNodeNumber+length; i++){
            graph.addVertex(i);
            if (i>nextNodeNumber){
                graph.addEdge(i-1,i);
            }
        }
        nextNodeNumber += length;
    }

    private ArrayList<Integer> getPath(){
        ArrayList<Integer> result = new ArrayList<>();
        result.add(start);
        int next = start;
        while (next!=end){
            Node node = graph.getVertices().get(next);
            int outCount = node.edgeKeys.size();
            int key = node.edgeKeys.get(random.nextInt(outCount));
            while (Edges.getEdge(key).isIn(next)){
                key = node.edgeKeys.get(random.nextInt(outCount));
            }
            next = Edges.getEdge(key).getOtherEnd(next);
            result.add(next);
        }
        return result;
    }

    private boolean findVariationPlace(ArrayList<Integer> path, int size){
        if (size < 0 || path.size() < size + 2){
            return false;
        }
        int difference = path.size() - size - 1;
        int place = random.nextInt(difference);
        variationStart = path.get(place);
        variationEnd = path.get(place + size + 1);
        return true;
    }

    public void addVariation(VariationType type){
        if (type==VariationType.SNP) {
            addVariation(type,1,0);
        }
    }

    private void addVariation(VariationType type, int length){
        if (type!=VariationType.MOBILE_ELEMENT){
            addVariation(type,length,0);
        }
    }

    private void addVariation(VariationType type, int length, int repeat){
        int start;
        int end;
        ArrayList<Integer> path = getPath();
        switch (type){
            case DELETION:
                if (findVariationPlace(path,length)) {
                    addVariation(variationStart, variationEnd);
                }
                break;
            case DUPLICATION:
                if (findVariationPlace(path,length)) {
                    addVariation(variationEnd, variationStart);
                }
                break;
            case SNP:
                length = 1;
                if (findVariationPlace(path,1)) {
                    start = nextNodeNumber;
                    makeVariation(length);
                    end = nextNodeNumber - 1;
                    addVariation(variationStart, start, end, variationEnd);
                }
                break;
            case INSERTION:
                if (findVariationPlace(path,0)) {
                    start = nextNodeNumber;
                    makeVariation(length);
                    end = nextNodeNumber - 1;
                    addVariation(variationStart, start, end, variationEnd);
                }
                break;
            case INVERSION:
                if (findVariationPlace(path,length)) {
                    start = nextNodeNumber;
                    makeVariation(length);
                    end = nextNodeNumber - 1;
                    addVariation(variationStart, start, end, variationEnd);
                }
                break;
            case MOBILE_ELEMENT:
                start = nextNodeNumber;
                makeVariation(length);
                end = nextNodeNumber-1;
                for (int i=0; i<repeat;i++){
                    if (findVariationPlace(path,0)) {
                        addVariation(variationStart, start, end, variationEnd);
                    }
                }
                break;
        }

        if (showVariations) {
            StringBuffer s = new StringBuffer(2000);
            s.append(" ");
            if (!(type == VariationType.DELETION || type == VariationType.DUPLICATION)) {
                for (int i = 0; i < length; i++) {
                    s.append(nextNodeNumber - length + i);
                    s.append(",");
                }
            }
            System.out.println(type + " =" + s + " : " + variationStart + ", " + variationEnd);
        }
    }

    /**
     * parameters = parameters of distribution
     * UNIFORM: [a, b]
     * parameter1 = a
     * parameter2 = b
     *
     * GAUSSIAN:
     * parameter1 = EV
     * parameter2 = SD
     *
     * EXPONENTIAL:
     * parameter1 = a
     * parameter2 = b
     * lambda
     */
    private int getLength(Law law, int parameter1, int parameter2){
        switch (law){
            case UNIFORM:
                int diff = parameter2 - parameter1;
                return parameter1 + random.nextInt(diff+1);
            case GAUSSIAN:
                Double gaussian = random.nextGaussian() * parameter2 + parameter1;
                return gaussian.intValue();
            case EXPONENTIAL:
                Double exp = parameter1 * Math.pow(parameter2 / parameter1, Math.pow(random.nextDouble(),2));
                return exp.intValue();
        }
        return -1;
    }

    /**
     * @param type      type of variation
     * @param law       distribution law with parameters:
     * @param parameter1
     * @param parameter2
     * @param repeat    number of variations for all except MOBILE_ELEMENT
     *                  number of MOBILE_ELEMENT insertions for MOBILE_ELEMENT
     */
    private void addVariations(VariationType type,
                              Law law, int parameter1, int parameter2,
                              int repeat){
        if (type==VariationType.MOBILE_ELEMENT){
            int length = getLength(law, parameter1, parameter2);
            addVariation(type, length, repeat);
        } else {
            for (int i = 0; i < repeat; i++) {
                int length = getLength(law, parameter1, parameter2);
                addVariation(type, length);
            }
        }
    }

    private int numberOfNodesInBackbone = 1000;

    private int numberOfMobileElements = 2;
    private int repeatOfMobileElements = 4;
    private int lengthStartOfMobileElements = 10;
    private int lengthEndOfMobileElements = 20;
    private Law lawOfMobileElements = Law.UNIFORM;

    private int numberOfLargeDeletions = 2;
    private int lengthStartOfLargeDeletions = 20;
    private int lengthEndOfLargeDeletions = 80;
    private Law lawOfLargeDeletions = Law.EXPONENTIAL;

    private int numberOfInversions = 2;
    private int lengthStartOfInversions = 50;
    private int lengthEndOfInversions = 80;
    private Law lawOfInversions = Law.EXPONENTIAL;

    private int numberOfDuplications = 4;
    private int lengthStartOfDuplications = 2;
    private int lengthEndOfDuplications = 40;
    private Law lawOfDuplications = Law.EXPONENTIAL;

    private int numberOfInsertions = 80;
    private int lengthStartOfInsertions = 1;
    private int lengthEndOfInsertions = 10;
    private Law lawOfInsertions = Law.UNIFORM;

    private int numberOfShortDeletions = 80;
    private int lengthStartOfShortDeletions = 1;
    private int lengthEndOfShortDeletions = 10;
    private Law lawOfShortDeletions = Law.UNIFORM;

    private int numberOfSNPs = 10;
    private int lengthStartOfSNPs = 1;
    private int lengthEndOfSNPs = 1;
    private Law lawOfSNPs = Law.UNIFORM;

    public void setParameters(int numberOfNodesInBackbone,
                              int numberOfMobileElements, int repeatOfMobileElements, int lengthStartOfMobileElements, int lengthEndOfMobileElements,
                              int numberOfLargeDeletions, int lengthStartOfLargeDeletions, int lengthEndOfLargeDeletions,
                              int numberOfInversions, int lengthStartOfInversions, int lengthEndOfInversions,
                              int numberOfDuplications, int lengthStartOfDuplications, int lengthEndOfDuplications,
                              int numberOfInsertions, int lengthStartOfInsertions, int lengthEndOfInsertions,
                              int numberOfShortDeletions, int lengthStartOfShortDeletions, int lengthEndOfShortDeletions,
                              int numberOfSNPs){
        this.numberOfNodesInBackbone = numberOfNodesInBackbone;
        this.numberOfMobileElements = numberOfMobileElements;
        this.repeatOfMobileElements = repeatOfMobileElements;
        this.lengthStartOfMobileElements = lengthStartOfMobileElements;
        this.lengthEndOfMobileElements = lengthEndOfMobileElements;
        this.numberOfLargeDeletions = numberOfLargeDeletions;
        this.lengthEndOfLargeDeletions = lengthEndOfLargeDeletions;
        this.lengthStartOfLargeDeletions = lengthStartOfLargeDeletions;
        this.numberOfInversions = numberOfInversions;
        this.lengthStartOfInversions = lengthStartOfInversions;
        this.lengthEndOfInversions = lengthEndOfInversions;
        this.numberOfDuplications = numberOfDuplications;
        this.lengthStartOfDuplications = lengthStartOfDuplications;
        this.lengthEndOfDuplications = lengthEndOfDuplications;
        this.numberOfInsertions = numberOfInsertions;
        this.lengthStartOfInsertions = lengthStartOfInsertions;
        this.lengthEndOfInsertions = lengthEndOfInsertions;
        this.numberOfShortDeletions = numberOfShortDeletions;
        this.lengthStartOfShortDeletions = lengthStartOfShortDeletions;
        this.lengthEndOfShortDeletions = lengthEndOfShortDeletions;
        this.numberOfSNPs = numberOfSNPs;
    }

    public void makeGraph(){
        makeGraph(false);
    }

    public void makeGraph(boolean showVariations){
        this.showVariations = showVariations;
        makeBackbone(numberOfNodesInBackbone);
        //MOBILE_ELEMENT
        for (int i=0; i<numberOfMobileElements; i++) {
            addVariations(VariationType.MOBILE_ELEMENT,
                    lawOfMobileElements, lengthStartOfMobileElements, lengthEndOfMobileElements,
                    repeatOfMobileElements);
        }
        //DELETION
        addVariations(VariationType.DELETION,
                    lawOfLargeDeletions,lengthStartOfLargeDeletions,lengthEndOfLargeDeletions,
                    numberOfLargeDeletions);
        //INVERSION
        addVariations(VariationType.INVERSION,
                lawOfInversions,lengthStartOfInversions,lengthEndOfInversions,
                numberOfInversions);
        //DUPLICATION
        addVariations(VariationType.DUPLICATION,
                lawOfDuplications,lengthStartOfDuplications,lengthEndOfDuplications,
                numberOfDuplications);
        //INSERTION
        addVariations(VariationType.INSERTION,
                lawOfInsertions,lengthStartOfInsertions,lengthEndOfInsertions,
                numberOfInsertions);
        //DELETION
        addVariations(VariationType.DELETION,
                lawOfShortDeletions,lengthStartOfShortDeletions,lengthEndOfShortDeletions,
                numberOfShortDeletions);
        //SNP
        addVariations(VariationType.SNP,
                lawOfSNPs,lengthStartOfSNPs,lengthEndOfSNPs,
                numberOfSNPs);
    }
}
