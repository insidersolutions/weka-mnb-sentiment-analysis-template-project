package info.semanticanalyzer.classifiers.weka;

import info.semanticanalyzer.classifiers.weka.threeway.ThreeWayMNBTrainer;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;

/**
 * Created by Dmitry Kan on 27.04.2014.
 */
public class ThreeWayMNBTrainerTest {
    ThreeWayMNBTrainer threeWayMnbTrainer;
    String modelFile = "models/three-way-sentiment-mnb.model";
    private static final String PERFOMRANCE_TEST_CONTENT_FILE = "src/test/resources/en_imdb_sentences.txt";

    @org.junit.Before
    public void setUp() throws Exception {
        threeWayMnbTrainer = new ThreeWayMNBTrainer(modelFile);
    }

    @org.junit.Test
    public void testAddTrainingInstance() throws Exception {
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.NEGATIVE, new String[] {"dislike"});
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.POSITIVE, new String[] {"like"});
        threeWayMnbTrainer.showInstances();
    }

    @org.junit.Test
    public void testTrainModel() throws Exception {
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.NEGATIVE, new String[] {"dislike"});
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.POSITIVE, new String[] {"like"});
        threeWayMnbTrainer.trainModel();
        threeWayMnbTrainer.testModel();
    }

    @org.junit.Test
    public void testSaveModel() throws Exception {
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.NEGATIVE, new String[] {"dislike"});
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.POSITIVE, new String[] {"like"});
        threeWayMnbTrainer.trainModel();
        threeWayMnbTrainer.testModel();
        threeWayMnbTrainer.saveModel();
        System.out.println("===== Loading and testing model ====");
        threeWayMnbTrainer.loadModel(modelFile);
        threeWayMnbTrainer.testModel();
    }

    @org.junit.Test
    public void testExistingModel() throws Exception {
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.NEGATIVE, new String[] {"dislike"});
        threeWayMnbTrainer.addTrainingInstance(SentimentClass.ThreeWayClazz.POSITIVE, new String[] {"like"});
        threeWayMnbTrainer.loadModel(modelFile);
        threeWayMnbTrainer.testModel();
    }

    @org.junit.Test
    public void testArbitraryTextPositive() throws Exception {
        threeWayMnbTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.ThreeWayClazz.POSITIVE, threeWayMnbTrainer.classify("I like this weather"));
    }

    @org.junit.Test
    public void testArbitraryTextNegative() throws Exception {
        threeWayMnbTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.ThreeWayClazz.NEGATIVE, threeWayMnbTrainer.classify("I dislike this weather"));
    }

    @org.junit.Test
    public void testArbitraryTextMixed() throws Exception {
        threeWayMnbTrainer.loadModel(modelFile);
        Assert.assertEquals(SentimentClass.ThreeWayClazz.NEUTRAL, threeWayMnbTrainer.classify("I really don't know whether I like or dislike this weather"));
    }

    @Test
    public void testPerformance() throws Exception
    {
        String content = IOUtils.toString(new FileInputStream(PERFOMRANCE_TEST_CONTENT_FILE), "UTF-8");
        String[] lines = content.split("\n");

        int wordsCount = getWordsCount(lines);

        threeWayMnbTrainer.loadModel(modelFile);

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
            threeWayMnbTrainer.classify(str).name();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Time " + elapsedTime + " ms.");
        System.out.println("Speed " + ((double) totalLength / elapsedTime) + " chars/ms");
        System.out.println("Speed " + ((double) wordsCount / elapsedTime) + " words/ms");
        System.out.println("+++++++++=");
    }

}
