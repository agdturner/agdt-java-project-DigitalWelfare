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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;

public class DW_Data_LCC_WRU_Handler extends DW_Object {

    public DW_Data_LCC_WRU_Handler(DW_Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        new DW_Data_LCC_WRU_Handler(null).run();
    }

    public void run() {
        String filename = "WelfareRights - Data Extract.csv";
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> data;
        data = loadInputData(filename, null);
    }

    /**
     * Loads LCC_WRU data from a file in directory, filename.
     *
     * @param filename
     * @param IDType
     * @return
     */
    public TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> loadInputData(
            String filename, Object IDType) {
        File directory = new File(env.files.getInputAdviceLeedsDir(), "LCC_WRU");
        return loadInputData(directory, filename, IDType);
    }

    /**
     * Loads LCC_WRU data from a file in directory, filename.
     *
     * @param dir
     * @param filename
     * @param IDType
     * @return
     */
    public TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> loadInputData(
            File dir, String filename, Object IDType) {
        System.out.println("Loading " + filename);
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> result;
        result = new TreeMap<>();
        File inputFile = new File(dir, filename);
        long RecordID = 0;
        int recordsLoaded = 0;
        int countOfAdditionalRecordsThatAreIgnored = 0;
        BufferedReader br;
        try {
            br = env.ge.io.getBufferedReader(inputFile);
            StreamTokenizer st;
            st = getStreamTokenizer(br);
            String line;
            // Skip the header
            int headerLines = 3;
            for (int i = 0; i < headerLines; i++) {
                env.ge.io.skipline(st);
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
                        try {
                            DW_Data_LCC_WRU_Record rec;
                            rec = new DW_Data_LCC_WRU_Record(env, RecordID, line, this);
                            //String enquiryReferenceNumber = record.getEnquiryReferenceNumber();
                            //1-102J,20-Sep-1936,Not Stated,Refused,LS6 1LS,2-464768375,2-7SFJ2M,Welfare Rights,Home Visit (MacMillan),05-Dec-2011,Blue Badge,-,75,77.11
                            String client_ref;
                            client_ref = rec.getUniqueCustomerRef();
                            DW_ID_ClientID id;
                            if (IDType instanceof DW_ID_ClientEnquiryID) {
                                String enquiry_ref;
                                enquiry_ref = rec.getEnquiryReferenceNumber();
                                id = new DW_ID_ClientEnquiryID(enquiry_ref, client_ref);
                            } else {
                                // IDType instance of DW_ID_ClientID
                                id = new DW_ID_ClientID(client_ref);
                            }
                            if (result.containsKey(id)) {
//                                System.out.println("Additional record for client " + aClient_Ref);
                                countOfAdditionalRecordsThatAreIgnored++;
                            } else {
                                result.put(id, rec);
                                recordsLoaded++;
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
        System.out.println("Number of additional records for clients that are ignored " + countOfAdditionalRecordsThatAreIgnored);
        return result;
    }

    public StreamTokenizer getStreamTokenizer(BufferedReader br) {
        StreamTokenizer result;
        result = new StreamTokenizer(br);
        env.ge.io.setStreamTokenizerSyntax5(result);
        result.wordChars('`', '`');
        result.wordChars('(', '(');
        result.wordChars(')', ')');
        result.wordChars('\'', '\'');
        result.wordChars('\"', '\"');
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
        result.wordChars('�', '�');
        return result;
    }
}
