package info.semanticanalyzer.classifiers.weka;

import info.semanticanalyzer.classifiers.weka.fiveway.FiveWayMNBTrainer;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by Dmitry Kan on 27.04.2014.
 */
public class FiveWayMNBTrainerTest {
    FiveWayMNBTrainer fiveWayMNBTrainer;
    String modelFile = "models/five-way-sentiment-mnb.model";
    private static final String PERFOMRANCE_TEST_CONTENT_FILE = "src/test/resources/en_imdb_sentences.txt";

    @org.junit.Before
    public void setUp() throws Exception {
        fiveWayMNBTrainer = new FiveWayMNBTrainer(modelFile);
    }

    @Test
    public void testAddTrainingInstance() throws Exception {
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.NEGATIVE, new String[] {"dislike"});
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.POSITIVE, new String[] {"like"});
        fiveWayMNBTrainer.showInstances();
    }

    @Test
    public void testTrainModel() throws Exception {
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.NEGATIVE, new String[] {"dislike"});
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.POSITIVE, new String[] {"like"});
        fiveWayMNBTrainer.trainModel();
        fiveWayMNBTrainer.testModel();
    }

    @Test
    public void testSaveModel() throws Exception {
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.NEGATIVE, new String[] {"dislike"});
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.POSITIVE, new String[] {"like"});
        fiveWayMNBTrainer.trainModel();
        fiveWayMNBTrainer.testModel();
        fiveWayMNBTrainer.saveModel();
        System.out.println("===== Loading and testing model ====");
        fiveWayMNBTrainer.loadModel(modelFile);
        fiveWayMNBTrainer.testModel();
    }

    @Test
    public void testExistingModel() throws Exception {
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.NEGATIVE, new String[] {"dislike"});
        fiveWayMNBTrainer.addTrainingInstance(SentimentClass.FiveWayClazz.POSITIVE, new String[] {"like"});
        fiveWayMNBTrainer.loadModel(modelFile);
        fiveWayMNBTrainer.testModel();
    }

    @Test
    public void testArbitraryTextPositive() throws Exception {
        fiveWayMNBTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.FiveWayClazz.POSITIVE, fiveWayMNBTrainer.classify("I like this weather"));
    }

    @Test
    public void testArbitraryTextNegative() throws Exception {
        fiveWayMNBTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.FiveWayClazz.NEGATIVE, fiveWayMNBTrainer.classify("I dislike this weather"));
    }

    @Test
    public void testArbitraryTextMixed() throws Exception {
        fiveWayMNBTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.FiveWayClazz.NEGATIVE, fiveWayMNBTrainer.classify("I really don't know whether I like or dislike this weather"));
    }

    @Test
    public void testArbitraryTextSomewhatNegative() throws Exception {
        fiveWayMNBTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.FiveWayClazz.NEGATIVE, fiveWayMNBTrainer.classify("It was a bit annoying experience"));
    }

    @Test
    public void testPerformance() throws Exception
    {
        String content = IOUtils.toString(new FileInputStream(PERFOMRANCE_TEST_CONTENT_FILE), "UTF-8");
        String[] lines = content.split("\n");

        int wordsCount = getWordsCount(lines);

        fiveWayMNBTrainer.loadModel(modelFile);

        test(lines, wordsCount, content.length()); // warm up

        test(lines, wordsCount, content.length()); // test
    }

    private int getWordsCount(String[] texts)
    {
        int count = 0;
        for (String str : texts) {
            count += str.split("\\s+").length;
        }
        return count;
    }

    private void test(String[] texts, int wordsCount, int totalLength) throws Exception {
        System.out.println("Testing on " + texts.length + " samples, " + wordsCount + " words, " + totalLength
                + " characters...");

        long startTime = System.currentTimeMillis();
        for (String str : texts) {
            // to print out the predicted labels, uncomment the line:
            //System.out.println(threeWayMnbTrainer.classify(str).name());
            fiveWayMNBTrainer.classify(str).name();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Time " + elapsedTime + " ms.");
        System.out.println("Speed " + ((double) totalLength / elapsedTime) + " chars/ms");
        System.out.println("Speed " + ((double) wordsCount / elapsedTime) + " words/ms");
        System.out.println("+++++++++=");
    }

}
