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

public class LCC_WRU_DataRecord_Handler {

    public LCC_WRU_DataRecord_Handler() {
    }

    public static void main(String[] args) {
       new LCC_WRU_DataRecord_Handler().run();
    }
    
    public void run() {
        String filename = "WelfareRights - Data Extract.csv";
        TreeMap<String, LCC_WRU_DataRecord> data;
        data = loadInputData(filename);
    }
    
    /**
     * Loads LCC_WRU data from a file in directory, filename.
     * @param filename
     * @return 
     */
    public TreeMap<String, LCC_WRU_DataRecord> loadInputData(
            String filename) {
        System.out.println("Loading " + filename);
        TreeMap<String, LCC_WRU_DataRecord> result;
        result = new TreeMap<String, LCC_WRU_DataRecord>();
        File directory = new File(
                DW_Files.getInputAdviceLeedsDir(),
                "LCC_WRU");
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
        String line = "";
        long RecordID = 0;
        int recordsLoaded = 0;
        int additionalRecordsForClientsThatAreIgnored = 0;
        try {
            // Skip the header
            int headerLines = 3;
            for (int i = 0; i < headerLines; i++) {
                Generic_StaticIO.skipline(st);
            }
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                
//                // For debugging
//                if (RecordID == 403) {
//                //if (RecordID % 400 == 0) {
//                //if (RecordID > 399) {
//                    System.out.println("RecordID " + RecordID);
//                    //int debug = 1;
//                }
                
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        try {
                            LCC_WRU_DataRecord record;
                                record = new LCC_WRU_DataRecord(RecordID, line, this);
                            //String enquiryReferenceNumber = record.getEnquiryReferenceNumber();
                                //1-102J,20-Sep-1936,Not Stated,Refused,LS6 1LS,2-464768375,2-7SFJ2M,Welfare Rights,Home Visit (MacMillan),05-Dec-2011,Blue Badge,-,75,77.11
    
                            String uniqueCustomerRef = record.getUniqueCustomerRef();
                            if (result.containsKey(uniqueCustomerRef)) {
//                                System.out.println("Additional record for client " + aClient_Ref);
                                additionalRecordsForClientsThatAreIgnored ++;
                            } else {
                                result.put(uniqueCustomerRef, record);
                                recordsLoaded ++;
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
