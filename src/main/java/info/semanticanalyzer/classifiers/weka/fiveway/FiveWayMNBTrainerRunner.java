package info.semanticanalyzer.classifiers.weka.fiveway;

/**
 * Created by dmitrykan on 27.04.2014.
 */
public class FiveWayMNBTrainerRunner {
    public static void main(String[] args) throws Exception {
        KaggleCSVReaderFiveWay kaggleCSVReaderFiveWay = new KaggleCSVReaderFiveWay();
        kaggleCSVReaderFiveWay.readKaggleCSV("kaggle/train.tsv");
        KaggleCSVReaderFiveWay.CSVInstanceFiveWay csvInstanceFiveWay;

        String outputModel = "models/five-way-sentiment-mnb.model";

        FiveWayMNBTrainer fiveWayMNBTrainer = new FiveWayMNBTrainer(outputModel);

        System.out.println("Adding training instances");
        int addedNum = 0;
        while ((csvInstanceFiveWay = kaggleCSVReaderFiveWay.next()) != null) {
            if (csvInstanceFiveWay.isValidInstance) {
                fiveWayMNBTrainer.addTrainingInstance(csvInstanceFiveWay.sentiment, csvInstanceFiveWay.phrase.split("\\s+"));
                addedNum++;
            }
        }

        kaggleCSVReaderFiveWay.close();

        System.out.println("Added " + addedNum + " instances");

        System.out.println("Training Model");
        fiveWayMNBTrainer.trainModel();
        System.out.println("Saving Model");
        fiveWayMNBTrainer.saveModel();

        System.out.println("Testing model");
        fiveWayMNBTrainer.testModel();
    }
}
