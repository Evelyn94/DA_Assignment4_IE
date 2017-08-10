package opennlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

import static java.lang.String.*;

/**
 * Created by honggu on 1/10/16.
 */
public class Extractor{
    public static void main(String[] args) throws Exception{
        FileReader in = null;
        FileWriter out = null;
        BufferedReader reader = null;

        ArrayList<String> c1 = new ArrayList<>();
        ArrayList<String> c2 = new ArrayList<>();
        ArrayList<String> c3 = new ArrayList<>();



        try {
            in = new FileReader("./test.eval");
            out = new FileWriter("./test.extract");
            reader = new BufferedReader(in);

            String line = "";
            while ((line = reader.readLine()) != null){
                if(line.equals("\n")) continue;
                String []temp = line.split("\t");
                if(temp.length == 3) {
                    c1.add(temp[0]);
                    c2.add(temp[1]);
                    c3.add(temp[2]);
                }
                else continue;
            }

            ArrayList<String> person = new ArrayList<>();
            ArrayList<String> location = new ArrayList<>();
            ArrayList<String> organization = new ArrayList<>();
            ArrayList<String> miscellany = new ArrayList<>();


            //recognize named entity
            for(int i = 0; i < c3.size();i++){
                String temp = c3.get(i);
                switch (temp){
                    case "B-LOC":{
                        String loc = c1.get(i);
                        int j = i + 1;
                        if(c3.get(j).equals("I-LOC")) {
                            while (c3.get(++i).equals("I-LOC")) {
                                loc = loc + " " + c1.get(i);
                            }
                            i--;
                        }
                        location.add(loc);
                        break;
                    }

                    case "B-ORG":{
                        String org = c1.get(i);
                        int j = i + 1;
                        if (c3.get(j).equals("I-ORG")) {
                            while (c3.get(++i).equals("I-ORG")) {
                                org = org + " " + c1.get(i);
                            }
                            i--;
                        }
                        organization.add(org);
                        break;
                    }

                    case "B-PER":{
                        String per = c1.get(i);
                        int j = i + 1;
                        if (c3.get(j).equals("I-PER")) {
                            while (c3.get(++i).equals("I-PER")) {
                                per = per + " " + c1.get(i);
                            }
                            i--;
                        }
                        person.add(per);
                        break;
                    }

                    case "B-MISC":{
                        String misc = c1.get(i);
                        int j = i + 1;
                        if (c3.get(j).equals("I-MISC")) {
                            while (c3.get(++i).equals("I-MISC")) {
                                misc = misc + " " + c1.get(i);
                            }
                            i--;
                        }
                        miscellany.add(misc);
                        break;
                    }
                }
            }


            //get the frequency
            HashSet<String> personS = new HashSet<>(person);
            HashSet<String> locationS = new HashSet<>(location);
            HashSet<String> organizationS = new HashSet<>(organization);
            HashSet<String> miscellanyS = new HashSet<>(miscellany);

            HashMap<String, Integer> personM = new HashMap<>();
            HashMap<String, Integer> locationM = new HashMap<>();
            HashMap<String, Integer> organizationM = new HashMap<>();
            HashMap<String, Integer> miscellanyM = new HashMap<>();

            for(String pers : personS){
                int num = 0;
                for (String pera : person){
                    if (pers.equals(pera)){
                        num ++;
                    }
                }
                personM.put(pers, num);
            }

            for(String locs : locationS){
                int num = 0;
                for (String loca : location){
                    if (locs.equals(loca)){
                        num ++;
                    }
                }
                locationM.put(locs, num);
            }

            for(String orgs : organizationS){
                int num = 0;
                for (String orga : organization){
                    if (orgs.equals(orga)){
                        num ++;
                    }
                }
                organizationM.put(orgs, num);
            }

            for(String miscs : miscellanyS){
                int num = 0;
                for (String misca : miscellany){
                    if (miscs.equals(misca)){
                        num ++;
                    }
                }
                miscellanyM.put(miscs, num);
            }


            person = new ArrayList<>(personS);
            location = new ArrayList<>(locationS);
            organization = new ArrayList<>(organizationS);
            miscellany = new ArrayList<>(miscellanyS);



            //order the output
            Collections.sort(person, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(location, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(organization, String.CASE_INSENSITIVE_ORDER);
            Collections.sort(miscellany, String.CASE_INSENSITIVE_ORDER);


            out.write("Location : \n");
            for (String s : location){
                out.write("\t" + s + "\t" + locationM.get(s) + "\n");
            }

            out.write("\n" + "Miscellany : \n");
            for (String s : miscellany){
                out.write("\t" + s + "\t" + miscellanyM.get(s) + "\n");
            }

            out.write("\n" + "Organization : \n");
            for (String s : organization){
                out.write("\t" + s + "\t" + organizationM.get(s) + "\n");
            }

            out.write("\n" + "Person : \n");
            for (String s : person){
                out.write("\t" + s + "\t" + personM.get(s) + "\n");
            }




        }finally {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            if(reader != null){
                reader.close();
            }
        }
    }

}
