/*
 * Copyright (C) 2013 geoagdt.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;

/**
 * A simple class for aggregating data for Sarah Craven from unit postcodes to 
 * postcode sectors.
 * For Unit Postcode "LS2 9JT": Postcode Area = "LS"; Postcode District = "LS2";
 * Postcode Sector = "LS2 9"
 * @author geoagdt
 */
public class SarahAggregator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new SarahAggregator().run();
        } catch (IOException ex) {
            Logger.getLogger(SarahAggregator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() throws IOException {
        File directory = new File("/scratch02/DigitalWelfare/Miscellaneous/Sarah");
        TreeMap<String, DataRow> data = loadData(directory);
        File outfile = new File(directory,"total_pop_aggregated.csv");
        PrintWriter pw = new PrintWriter(outfile);
        Iterator<String> ite = data.keySet().iterator();
        while (ite.hasNext()) {
            String postcode = ite.next();
            DataRow dr = data.get(postcode);
            String line = postcode + ", " + dr.getC1() + ", " + dr.getC2() + ", " + dr.getC3() + ", " + dr.getC4();
            System.out.println(line);
            pw.println(line);
        }
        pw.close();
    }

    /**
     * @return @throws java.io.IOException
     */
    private TreeMap<String, DataRow> loadData(File directory) throws IOException {
        TreeMap<String, DataRow> result = new TreeMap<>();
        File infile = new File(directory, "total_pop.csv");
        BufferedReader br = Generic_IO.getBufferedReader(infile);
        StreamTokenizer st = new StreamTokenizer(br);
        Generic_IO.setStreamTokenizerSyntax1(st);
        int tokenType = st.nextToken();
        DataRow dr = null;
        while (tokenType != StreamTokenizer.TT_EOF) {
            switch (tokenType) {
                case StreamTokenizer.TT_EOL:
                    if (dr != null) {
                        String postcode = dr.getPostcode();
                        if (result.containsKey(postcode)) {
                            DataRow existingDR = result.get(postcode);
                            existingDR.aggregate(dr);
                            //result.put(postcode,)
                        } else {
                            result.put(postcode, dr);
                        }
                    }
                    break;
                case StreamTokenizer.TT_WORD:
                    String s = st.sval;
                    dr = new DataRow(s);
                    if (dr.postcode == null) {
                        dr = null;
                    }
                    break;
            }
            tokenType = st.nextToken();
        }
        br.close();
        return result;
    }
}

class DataRow {

    String postcode;
    long c1;
    long c2;
    long c3;
    long c4;

    public DataRow(String s) {
        String[] data = s.split(",");
        if (data.length == 5) {
            String postcode1 = data[0];
            int postcodeLength = postcode1.length();
            String firstPartPostcode = postcode1.substring(0, postcodeLength - 3).trim();
            //System.out.println("firstPartPostcode " + firstPartPostcode);
            String secondPartPostcode = postcode1.substring(postcodeLength - 3, postcodeLength);
            //System.out.println("secondPartPostcode " + secondPartPostcode);
            postcode = firstPartPostcode + " " + secondPartPostcode.substring(0,1);
            c1 = new Long(data[1]);
            c2 = new Long(data[2]);
            c3 = new Long(data[3]);
            c4 = new Long(data[4]);
        } else {
            System.out.println("Unexpected line " + s);
        }
    }

    public void aggregate(DataRow dr) {
        c1 += dr.getC1();
        c2 += dr.getC2();
        c3 += dr.getC3();
        c4 += dr.getC4();
    }

    public String getPostcode() {
        return postcode;
    }

    public long getC1() {
        return c1;
    }

    public void setC1(long c1) {
        this.c1 = c1;
    }

    public long getC2() {
        return c2;
    }

    public void setC2(long c2) {
        this.c2 = c2;
    }

    public long getC3() {
        return c3;
    }

    public void setC3(long c3) {
        this.c3 = c3;
    }

    public long getC4() {
        return c4;
    }

    public void setC4(long c4) {
        this.c4 = c4;
    }

}
