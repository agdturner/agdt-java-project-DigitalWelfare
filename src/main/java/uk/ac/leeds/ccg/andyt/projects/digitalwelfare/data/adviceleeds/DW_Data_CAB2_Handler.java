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

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;

/**
 * For handling data from CASE.
 */
public class DW_Data_CAB2_Handler extends DW_Object {

    public DW_Data_CAB2_Handler() {
    }

    public DW_Data_CAB2_Handler(DW_Environment env) {
        super(env);
    }

    /**
     * Loads LeedsCAB data from a file in directory, filename.
     *
     * @param directory
     * @param filename
     * @return TreeMap<DW_ID_ClientID,DW_Data_CAB2_Record>
     * representing records
     */
    public TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> loadInputData(
            File directory,
            String filename,
            Object IDType) {
        System.out.println("Loading " + filename);
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> result;
        result = new TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record>();
        File inputFile = new File(
                directory,
                filename);
        try {
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st;
            st = getStreamTokenizerSyntax(br);
            String line = "";
            long RecordID = 0;
            // Skip the header
            int headerLines = 2;
            for (int i = 0; i < headerLines; i++) {
                Generic_StaticIO.skipline(st);
            }
            // Read data
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
//                // For debugging
//                if (RecordID == 10) {
//                    int debug = 1;
//                }
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        try {
                            DW_Data_CAB2_Record record = new DW_Data_CAB2_Record(env, RecordID, line, this);
                            String client_ref = record.getClient_ref();
                            String enquiry_ref = record.getEnquiry_ref();
                            //String bureau = record.getBureau();
                            String outlet = record.getOutlet();
                            DW_ID_ClientID id;
                            if (IDType instanceof DW_ID_ClientOutletEnquiryID) {
                                id = new DW_ID_ClientOutletEnquiryID(client_ref, outlet, enquiry_ref);
                            } else if (IDType instanceof DW_ID_ClientOutletID) {
                                id = new DW_ID_ClientOutletID(client_ref, outlet);
                            } else {
                                // IDType instance of DW_ID_ClientID
                                id = new DW_ID_ClientID(client_ref);
                            }
                            if (result.containsKey(id)) {
                                System.out.println("Additional record for " + id);
                                DW_Data_CAB2_Record existingRecord;
                                existingRecord = result.get(id);
                                System.out.println("Existing Record");
                                System.out.println(existingRecord);
                                System.out.println("Current Record");
                                System.out.println(record);
                                if (!existingRecord.getEnquiry_type().equalsIgnoreCase(record.getEnquiry_type())) {
                                    System.out.println("!existingRecord.getEnquiry_type().equalsIgnoreCase(record.getEnquiry_type())");
                                }
                                id.toString();
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

    public StreamTokenizer getStreamTokenizerSyntax(BufferedReader br) {
        StreamTokenizer result;
        result = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(result);
        result.wordChars('`', '`');
        result.wordChars('\'', '\'');
        result.wordChars('(', '(');
        result.wordChars(')', ')');
        result.wordChars('\'', '\'');
        result.wordChars('*', '*');
        result.wordChars('\\', '\\');
        result.wordChars('/', '/');
        result.wordChars('&', '&');
        result.wordChars('£', '£');
        result.wordChars('<', '<');
        result.wordChars('>', '>');
        result.wordChars('=', '=');
        result.wordChars('#', '#');
        result.wordChars(':', ':');
        result.wordChars(';', ';');
        return result;
    }
}
