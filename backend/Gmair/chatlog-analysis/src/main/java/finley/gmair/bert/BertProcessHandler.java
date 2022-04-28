package finley.gmair.bert;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class BertProcessHandler {
    static volatile Boolean isBertProcessOn = false;

    @Value("${python.env}")
    String pythonEnv;

    @Value("${python.bert-entry}")
    String bertEntry;

    @Autowired
    BertProperties bertProperties;

    @Value("${kafka.topics.to-bert}")
    String topicToAnalysis;

    @Value("${kafka.topics.after-bert}")
    String topicAfterAnalysis;


    public boolean checkIfBertProcessOn() {
        synchronized (this) {
            return isBertProcessOn;
        }
    }

    public boolean runBertProcess() {
        synchronized (this) {
            if (isBertProcessOn) return false;
            else isBertProcessOn = true;
        }
        try {
            String[] commands = new String[]{pythonEnv, bertEntry, JSON.toJSONString(bertProperties),
                    topicToAnalysis, topicAfterAnalysis};
            System.out.println(pythonEnv + bertEntry + JSON.toJSONString(bertProperties));

            Process process = Runtime.getRuntime().exec(commands);
            PythonOutStream error = new PythonOutStream(process.getErrorStream());
            PythonOutStream output = new PythonOutStream(process.getInputStream());
            error.start();
            output.start();
        } catch (IOException e) {
            e.printStackTrace();
            synchronized (this) {
                isBertProcessOn = false;
            }
            return false;
        }
        synchronized (this) {
            isBertProcessOn = false;
        }
        return true;
    }

    static class PythonOutStream extends Thread {
        InputStream is;

        public PythonOutStream(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
