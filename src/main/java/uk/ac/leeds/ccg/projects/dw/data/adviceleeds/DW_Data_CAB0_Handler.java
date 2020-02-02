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
package uk.ac.leeds.ccg.projects.dw.data.adviceleeds;

import uk.ac.leeds.ccg.projects.dw.core.DW_Object;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;

/**
 * For Handling data from Petra.
 *
 * @author geoagdt
 */
public class DW_Data_CAB0_Handler extends DW_Object {

    public DW_Data_CAB0_Handler(DW_Environment env) {
        super(env);
    }

    public TreeMap loadInputData(File dir, String filename, Object IDType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Loads Chapeltown CAB data.
     *
     * @param type if (type == 0) { record = new DW_Data_CAB0_Record(RecordID,
     * line, this); } else { record = new DW_Data_CAB0_Record1(RecordID, line,
     * this); }
     * @param filename
     * @param IDType
     * @return TreeMap<String,DW_Data_CAB0_Record> representing records
     */
    public TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> loadInputData(
            int type,
            String filename,
            Object IDType) throws IOException {
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> result;
        result = new TreeMap<>();
        Path directory = Paths.get(env.files.getInputAdviceLeedsDir().toString(),
                "ChapeltownCAB");
        Path inputFile = Paths.get(directory.toString(), filename);
        try {
            long RecordID;
            int recordsLoaded;
            int countOfAdditionalRecordsThatAreIgnored;
            try (BufferedReader br = env.ge.io.getBufferedReader(inputFile)) {
                StreamTokenizer st;
                st = getStreamTokenizer(br);
                String line;
                RecordID = 0;
                recordsLoaded = 0;
                countOfAdditionalRecordsThatAreIgnored = 0;
                // Skip the header
                int headerLines = 1;
                for (int i = 0; i < headerLines; i++) {
                    br.readLine();
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
                            try {
                                DW_Data_CAB0_Record rec;
                                if (type == 0) {
                                    rec = new DW_Data_CAB0_Record(env, RecordID, line, this);
                                } else {
                                    rec = new DW_Data_CAB0_Record1(env, RecordID, line, this);
                                }
                                String client_ref;
                                client_ref = rec.getClient_Ref();
                                DW_ID_ClientID id;
                                id = new DW_ID_ClientID(client_ref);
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
            }
            System.out.println("Number of records loaded " + recordsLoaded);
            System.out.println("Number of records not loaded " + (RecordID - recordsLoaded));
            System.out.println("Number of additional records for clients that are ignored " + countOfAdditionalRecordsThatAreIgnored);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
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
