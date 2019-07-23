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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;

/**
 * For handling data from the Burley Lodge Centre/Better Leeds Communities.
 *
 * @author geoagdt
 */
public class DW_Data_BLC_Handler extends DW_Data_AbstractRecord {

    public DW_Data_BLC_Handler(DW_Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        new DW_Data_BLC_Handler(null).run();
    }

    public void run() {
        String filename = "Burley Lodge Amalgamated 2012-2013.csv";
        TreeMap<String, DW_Data_BLC_Record> data;
        data = loadInputData(filename);
    }

    /**
     * Loads BLC data from a file in directory, filename.
     *
     * @param filename
     * @return
     */
    public TreeMap<String, DW_Data_BLC_Record> loadInputData(
            String filename) {
        System.out.println("Loading " + filename);
        TreeMap<String, DW_Data_BLC_Record> r = new TreeMap<>();
        File directory = new File(env.files.getInputAdviceLeedsDir(),
                "BurleyLodgeCentre");
        File inputFile = new File(directory, filename);
        try (BufferedReader br = env.ge.io.getBufferedReader(inputFile)) {
            StreamTokenizer st = new StreamTokenizer(br);
            env.ge.io.setStreamTokenizerSyntax5(st);
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
            String line;
            long RecordID = 0;
            int recordsLoaded = 0;
            int additionalRecordsForClientsThatAreIgnored = 0;
            // Skip the header
            int headerLines = 8;
            for (int i = 0; i < headerLines; i++) {
                env.ge.io.skipline(st);
            }   // Read data
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
                            DW_Data_BLC_Record record;
                            record = new DW_Data_BLC_Record(env, RecordID, line, this);
                            String clientReference = record.getClientReference();
                            if (!clientReference.equalsIgnoreCase("")) {
                                if (r.containsKey(clientReference)) {
//                                System.out.println("Additional record for client " + aClient_Ref);
                                    additionalRecordsForClientsThatAreIgnored++;
                                } else {
                                    r.put(clientReference, record);
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
            System.out.println("Number of records loaded " + recordsLoaded);
            System.out.println("Number of records not loaded " + (RecordID - recordsLoaded));
            System.out.println("Number of additional records for clients that are ignored " + additionalRecordsForClientsThatAreIgnored);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

}
