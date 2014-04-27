package info.semanticanalyzer.classifiers.weka;

import junit.framework.Assert;

/**
 * Created by Dmitry Kan on 27.04.2014.
 */
public class ThreeWayMNBTrainerTest {
    ThreeWayMNBTrainer threeWayMnbTrainer;
    String modelFile = "models/three-way-sentiment-mnb.model";

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
}
