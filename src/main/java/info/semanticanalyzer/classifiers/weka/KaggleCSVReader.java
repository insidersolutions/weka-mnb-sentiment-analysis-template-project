package info.semanticanalyzer.classifiers.weka;

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
public class KaggleCSVReader {

    String line;
    CSVInstanceThreeWay csvInstanceThreeWay;
    int step = 0;

    BufferedReader br;

    int showStatsAt = 1000;

    public void readKaggleCSV(String csvFile) throws IOException {
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

    public CSVInstanceThreeWay next() {
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

    public void close() {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class CSVInstanceThreeWay {
        public int phraseID;
        public int sentenceID;
        public String phrase;
        public SentimentClass.ThreeWayClazz sentiment;
        public boolean isValidInstance;

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
