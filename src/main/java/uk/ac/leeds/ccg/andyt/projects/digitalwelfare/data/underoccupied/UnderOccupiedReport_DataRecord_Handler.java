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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.underoccupied;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

/**
 *
 * @author geoagdt
 */
public class UnderOccupiedReport_DataRecord_Handler {

    private HashSet<String> RecordTypes;

    public UnderOccupiedReport_DataRecord_Handler() {
    }

    public HashSet<String> getRecordTypes() {
        return RecordTypes;
    }

    /**
     * HashSet<SHBE_DataRecord>
     *
     * @param directory
     * @param filename
     * @param pw
     * @return
     */
    public TreeMap<String, UnderOccupiedReport_DataRecord> loadInputData(
            File directory,
            String filename,
            PrintWriter pw) {
        TreeMap<String, UnderOccupiedReport_DataRecord> result = new TreeMap<String, UnderOccupiedReport_DataRecord>();
        File inputFile = new File(
                directory,
                filename);
        BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
        StreamTokenizer st
                = new StreamTokenizer(br);
        Generic_StaticIO.setStreamTokenizerSyntax5(st);
        st.wordChars('`', '`');
        st.wordChars('*', '*');
        String line = "";
        //int duplicateEntriesCount = 0;
        int replacementEntriesCount = 0;
        long RecordID = 0;
        try {
            // Read firstline and check format
            //int type = readAndCheckFirstLine(directory, filename);
            // Skip the first line
            Generic_StaticIO.skipline(st);
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
                            UnderOccupiedReport_DataRecord aUnderOccupiedReport_DataRecord = new UnderOccupiedReport_DataRecord(
                                    RecordID, line, this);
                            Object o = result.put(aUnderOccupiedReport_DataRecord.getClaimReferenceNumber(), aUnderOccupiedReport_DataRecord);
                            if (o != null) {
                                UnderOccupiedReport_DataRecord existingUnderOccupiedReport_DataRecord = (UnderOccupiedReport_DataRecord) o;
                                pw.println("existingUnderOccupiedReport_DataRecord " + existingUnderOccupiedReport_DataRecord);
                                pw.println("replacementUnderOccupiedReport_DataRecord " + aUnderOccupiedReport_DataRecord);
                                pw.println("RecordID " + RecordID);
                                replacementEntriesCount ++;
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
            pw.println("replacementEntriesCount " + replacementEntriesCount);                               
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(UnderOccupiedReport_DataRecord_Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
//    /**
//     * This will return a collation of all ClaimReferenceNumbers in the dataset 
//     * as a HashMap
//     */
//    public HashMap getRecordIDHashMap(TreeMap<String, UnderOccupiedReport_DataRecord> data) {
//        HashMap result;
//        Iterator ite = data.keySet();
//        
//        
//        return result;
//    }
}
