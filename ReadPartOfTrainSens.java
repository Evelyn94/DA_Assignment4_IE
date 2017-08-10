package opennlp;

import org.apache.commons.collections.Buffer;

import java.io.*;

/**
 * Created by honggu on 8/10/16.
 */
public class ReadPartOfTrainSens {
    public static void main(String[] args) throws Exception{
        FileReader in1 = null;
        FileReader in2 = null;
        FileReader in3 = null;
        BufferedReader reader1 = null;
        BufferedReader reader2 = null;
        BufferedReader reader3 = null;

        FileWriter out1 = null;
        FileWriter out2 = null;
        FileWriter out3 = null;

        try {
            in1 = new FileReader("./esp.train");
            in2 = new FileReader("./esp.train");
            in3 = new FileReader("./esp.train");
            reader1 = new BufferedReader(in1);
            reader2 = new BufferedReader(in2);
            reader3 = new BufferedReader(in3);
            out1 = new FileWriter("./esp_4_1.train");
            out2 = new FileWriter("./esp_4_2.train");
            out3 = new FileWriter("./esp_4_3.train");

            for(int i = 0; i<60967; i++){
                String s = reader1.readLine();
                out1.write(s + "\n");
            }

            for(int i = 0; i < 121889; i++){
                String s = reader2.readLine();
                out2.write(s + "\n");
            }

            for(int i = 0; i < 182863; i++){
                String s = reader3.readLine();
                out3.write(s + "\n");
            }


        }finally {
            if(in1 != null){
                in1.close();
            }
            if(in2 != null){
                in2.close();
            }
            if(in3 != null){
                in3.close();
            }
            if(reader1 != null){
                reader1.close();
            }
            if(reader2 != null){
                reader2.close();
            }
            if(reader3 != null){
                reader3.close();
            }
            if(out1 != null){
                out1.close();
            }
            if(out2 != null){
                out2.close();
            }
            if(out3 != null){
                out3.close();
            }

        }

    }
}
