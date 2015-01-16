/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

public class BLC_DataRecord_Handler {

    public BLC_DataRecord_Handler() {
    }

    public static void main(String[] args) {
        new BLC_DataRecord_Handler().run();
    }

    public void run() {
        String filename = "Burley Lodge Amalgamated 2012-2013.csv";
        TreeMap<String, BLC_DataRecord> data;
        data = loadInputData(filename);
    }

    /**
     * Loads BLC data from a file in directory, filename.
     *
     * @param filename
     * @return
     */
    public TreeMap<String, BLC_DataRecord> loadInputData(
            String filename) {
        System.out.println("Loading " + filename);
        TreeMap<String, BLC_DataRecord> result;
        result = new TreeMap<String, BLC_DataRecord>();
        File directory = new File(
                DW_Files.getInputAdviceLeedsDir(),
                "BurleyLodgeCentre");
        File inputFile = new File(
                directory,
                filename);
        BufferedReader br;
        br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st;
        st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        st.wordChars('(', '(');
        st.wordChars(')', ')');
        st.wordChars('\'', '\'');
        st.wordChars('\"', '\"');
        st.wordChars('*', '*');
        st.wordChars('\\', '\\');
        st.wordChars('/', '/');
        st.wordChars('&', '&');
        st.wordChars('£', '£');
        st.wordChars('<', '<');
        st.wordChars('>', '>');
        st.wordChars('=', '=');
        st.wordChars('#', '#');
        st.wordChars(':', ':');
        st.wordChars('�', '�');
        st.wordChars('!', '!');
        String line = "";
        long RecordID = 0;
        int recordsLoaded = 0;
        int additionalRecordsForClientsThatAreIgnored = 0;
        try {
            // Skip the header
            int headerLines = 8;
            for (int i = 0; i < headerLines; i++) {
                Generic_StaticIO.skipline(st);
            }
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {

                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        //434,Yes, , ,,,,,,,02/04/2012,03/04/2012,Female,D. 35-49,A. White: British,Yes,B. Buying Home (mortgage etc.),B. Married /cohabiting/civil partnership,C. Part-Time,35855,No ,Yes,No ,No ,No ,No ,No ,No ,No ,No ,A/C,None,No ,Yes,Yes,No ,No ,LS10 4,3,A. Face to Face,B. One-off,Yes,,,Male,,Yes,A. Own outright,A. Face to Face,,,,,,,,,,

                        // For debugging
                        //if (RecordID == 20) {
                        if (RecordID % 100 == 0) {
                            System.out.println("RecordID " + RecordID);
                        }

                        try {
                            BLC_DataRecord record;
                            record = new BLC_DataRecord(RecordID, line, this);
                            String clientReference = record.getClientReference();
                            if (!clientReference.equalsIgnoreCase("")) {
                                if (result.containsKey(clientReference)) {
//                                System.out.println("Additional record for client " + aClient_Ref);
                                    additionalRecordsForClientsThatAreIgnored++;
                                } else {
                                    result.put(clientReference, record);
                                    recordsLoaded++;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println(line);
                            System.err.println("RecordID " + RecordID);
                            System.err.println(e.getLocalizedMessage());
                        }
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Number of records loaded " + recordsLoaded);
        System.out.println("Number of records not loaded " + (RecordID - recordsLoaded));
        System.out.println("Number of additional records for clients that are ignored " + additionalRecordsForClientsThatAreIgnored);
        return result;
    }

}
