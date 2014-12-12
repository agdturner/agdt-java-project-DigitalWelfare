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
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.Deprivation_DataHandler;

/**
 * For handling data from CASE.
 */
public class CAB_DataRecord2_Handler {

    public CAB_DataRecord2_Handler() {}

    /**
     * Loads LeedsCAB data from a file in directory, filename reporting progress of
     * loading to PrintWriter pw.
     *
     * @param directory
     * @param filename
     * @param pw
     * @return TreeMap<String,CAB_DataRecord1> representing records
     */
    public TreeMap<EnquiryClientBureauOutletID,CAB_DataRecord2> loadInputData(
            File directory,
            String filename) {
        System.out.println("Loading " + filename);
        TreeMap<EnquiryClientBureauOutletID,CAB_DataRecord2> result;
        result = new TreeMap<EnquiryClientBureauOutletID,CAB_DataRecord2>();
        File inputFile = new File(
                directory,
                filename);
        BufferedReader br;
        br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st;
        st = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        st.wordChars('\'', '\'');
        st.wordChars('(', '(');
        st.wordChars(')', ')');
        st.wordChars('\'', '\'');
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
        st.wordChars(';', ';');
        String line = "";
        long RecordID = 0;
        try {
            // Skip the header
            int headerLines = 2;
            for (int i = 0; i < headerLines; i ++) {
                Generic_StaticIO.skipline(st);
            }
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {

                // For debugging
                if (RecordID == 10) {
                    int debug = 1;
                }

                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                            try { 
                                CAB_DataRecord2 record = new CAB_DataRecord2(RecordID, line, this);
                                String client_ref = record.getClient_ref();
                                String enquiry_ref = ""; //record.getEnquiry_ref();
                                String bureau = record.getBureau();
                                String outlet = record.getOutlet();
                                EnquiryClientBureauOutletID id;
                                id = new EnquiryClientBureauOutletID(enquiry_ref, client_ref, bureau, outlet);
                                if (result.containsKey(id)) {
                                    System.out.println("Additional record for " + id);
                                } else {
                                    result.put(id, record);
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
            Logger.getLogger(Deprivation_DataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
