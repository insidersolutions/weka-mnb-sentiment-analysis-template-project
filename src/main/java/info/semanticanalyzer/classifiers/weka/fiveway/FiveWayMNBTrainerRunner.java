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

        int sentimentPositiveCount = 0;
        int sentimentNegativeCount = 0;
        int sentimentOtherCount = 0;

        System.out.println("Adding training instances");
        int addedNum = 0;
        while ((csvInstanceFiveWay = kaggleCSVReaderFiveWay.next()) != null) {
            if (csvInstanceFiveWay.isValidInstance) {
            /*
                if (csvInstanceFiveWay.sentiment.equals(SentimentClass.ThreeWayClazz.POSITIVE) && sentimentPositiveCount < 7072) {
                    sentimentPositiveCount++;
                    threeWayMNBTrainer.addTrainingInstance(csvInstanceFiveWay.sentiment, csvInstanceFiveWay.phrase.split("\\s+"));
                    addedNum++;
                }
                else if (csvInstanceFiveWay.sentiment.equals(SentimentClass.ThreeWayClazz.NEGATIVE) && sentimentNegativeCount < 7072) {
                    sentimentNegativeCount++;
                    threeWayMNBTrainer.addTrainingInstance(csvInstanceFiveWay.sentiment, csvInstanceFiveWay.phrase.split("\\s+"));
                    addedNum++;
                }
                else {
                    sentimentOtherCount++;
                }

                if (sentimentPositiveCount >= 7072 && sentimentNegativeCount >= 7072)
                    break;
            */
                fiveWayMNBTrainer.addTrainingInstance(csvInstanceFiveWay.sentiment, csvInstanceFiveWay.phrase.split("\\s+"));
                addedNum++;
            }
        }

        kaggleCSVReaderFiveWay.close();

        System.out.println("Added " + addedNum + " instances");
        /*
        System.out.println("Of which " + sentimentPositiveCount + " positive instances, " +
                            sentimentNegativeCount + " negative instances and " +
                            sentimentOtherCount + " other sentiment instances");
                            */

        System.out.println("Training Model");
        fiveWayMNBTrainer.trainModel();
        System.out.println("Saving Model");
        fiveWayMNBTrainer.saveModel();

        System.out.println("Testing model");
        fiveWayMNBTrainer.testModel();
    }
}
