package info.semanticanalyzer.classifiers.weka.threeway;

import info.semanticanalyzer.classifiers.weka.SentimentClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Created by dmitrykan on 27.04.2014.
 *
 * Reads a TAB separated file of the format:
 *
 * PhraseId	SentenceId	Phrase	Sentiment
 *
 */
class KaggleCSVReaderThreeWay {

    private String line;
    private CSVInstanceThreeWay csvInstanceThreeWay;
    private int step = 0;

    private BufferedReader br;

    private int showStatsAt = 1000;

    void readKaggleCSV(String csvFile) throws IOException {
        br = new BufferedReader(new FileReader(csvFile));

        line = br.readLine();

        if (line != null) {
            if (line.startsWith("PhraseId")) {
                line = br.readLine();
            }

            if (line != null) {
                extractInstance();
            }
        }
    }

    private void extractInstance() {
        String[] attrs = line.split("\t");

        if (csvInstanceThreeWay == null) {
            csvInstanceThreeWay = new CSVInstanceThreeWay();
        }
        csvInstanceThreeWay.phraseID = Integer.valueOf(attrs[0]);
        csvInstanceThreeWay.sentenceID = Integer.valueOf(attrs[1]);
        csvInstanceThreeWay.phrase = attrs[2];
        // there is additionally sentiment tag for training data
        if (attrs.length > 3) {
            Integer sentimentOrdinal = Integer.valueOf(attrs[3]);

            if (sentimentOrdinal <= 1) {
                csvInstanceThreeWay.sentiment = SentimentClass.ThreeWayClazz.values()[sentimentOrdinal];
                csvInstanceThreeWay.isValidInstance = true;
            } else {
                // can't process the instance, because the sentiment ordinal is out of the acceptable range of two classes
                csvInstanceThreeWay.isValidInstance = false;
            }
        }
    }

    CSVInstanceThreeWay next() {
        if (step == 0) {
            step++;
            return csvInstanceThreeWay;
        }

        if (step % showStatsAt == 0) {
            System.out.println("Processed instances: " + step);
        }

        try {
            line = br.readLine();
            if (line != null) {
                extractInstance();
            } else {
                return null;
            }
            step++;
            return csvInstanceThreeWay;
        } catch (IOException e) {
            return null;
        }
    }

    void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CSVInstanceThreeWay {
        int phraseID;
        int sentenceID;
        String phrase;
        SentimentClass.ThreeWayClazz sentiment;
        boolean isValidInstance;

        @Override
        public String toString() {
            return "CSVInstanceThreeWay{" +
                    "phraseID=" + phraseID +
                    ", sentenceID=" + sentenceID +
                    ", phrase='" + phrase + '\'' +
                    ", sentiment=" + sentiment +
                    '}';
        }
    }

}
