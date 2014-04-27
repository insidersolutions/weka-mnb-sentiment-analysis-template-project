package info.semanticanalyzer.classifiers.weka;

/**
 * Created by dmitrykan on 27.04.2014.
 */
public class ThreeWayMNBTrainerRunner {
    public static void main(String[] args) throws Exception {
        KaggleCSVReader kaggleCSVReader = new KaggleCSVReader();
        kaggleCSVReader.readKaggleCSV("kaggle/train.tsv");
        KaggleCSVReader.CSVInstanceThreeWay csvInstanceThreeWay;

        String outputModel = "models/three-way-sentiment-mnb.model";

        ThreeWayMNBTrainer threeWayMNBTrainer = new ThreeWayMNBTrainer(outputModel);

        System.out.println("Adding training instances");
        int addedNum = 0;
        while ((csvInstanceThreeWay = kaggleCSVReader.next()) != null) {
            if (csvInstanceThreeWay.isValidInstance) {
                threeWayMNBTrainer.addTrainingInstance(csvInstanceThreeWay.sentiment, csvInstanceThreeWay.phrase.split("\\s+"));
                addedNum++;
            }
        }

        kaggleCSVReader.close();

        System.out.println("Added " + addedNum + " instances");

        System.out.println("Training and saving Model");
        threeWayMNBTrainer.trainModel();
        threeWayMNBTrainer.saveModel();

        System.out.println("Testing model");
        threeWayMNBTrainer.testModel();
    }
}
