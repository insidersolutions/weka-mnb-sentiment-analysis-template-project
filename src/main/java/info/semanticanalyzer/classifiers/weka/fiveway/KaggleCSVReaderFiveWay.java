package info.semanticanalyzer.classifiers.weka.fiveway;

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
class KaggleCSVReaderFiveWay {

    private String line;
    private CSVInstanceFiveWay csvInstanceFiveWay;
    private int step = 0;

    private BufferedReader br;

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

        if (csvInstanceFiveWay == null) {
            csvInstanceFiveWay = new CSVInstanceFiveWay();
        }
        csvInstanceFiveWay.phraseID = Integer.valueOf(attrs[0]);
        csvInstanceFiveWay.sentenceID = Integer.valueOf(attrs[1]);
        csvInstanceFiveWay.phrase = attrs[2];
        // there is additionally sentiment tag for training data
        if (attrs.length > 3) {
            Integer sentimentOrdinal = Integer.valueOf(attrs[3]);

            if (sentimentOrdinal <= 4) {
                csvInstanceFiveWay.sentiment = SentimentClass.FiveWayClazz.values()[sentimentOrdinal];
                csvInstanceFiveWay.isValidInstance = true;
            } else {
                // can't process the instance, because the sentiment ordinal is out of the acceptable range of two classes
                csvInstanceFiveWay.isValidInstance = false;
            }
        }
    }

    CSVInstanceFiveWay next() {
        if (step == 0) {
            step++;
            return csvInstanceFiveWay;
        }

        int showStatsAt = 1000;
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
            return csvInstanceFiveWay;
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

    class CSVInstanceFiveWay {
        int phraseID;
        int sentenceID;
        String phrase;
        SentimentClass.FiveWayClazz sentiment;
        boolean isValidInstance;

        @Override
        public String toString() {
            return "CSVInstanceFiveWay{" +
                    "phraseID=" + phraseID +
                    ", sentenceID=" + sentenceID +
                    ", phrase='" + phrase + '\'' +
                    ", sentiment=" + sentiment +
                    '}';
        }
    }

}
