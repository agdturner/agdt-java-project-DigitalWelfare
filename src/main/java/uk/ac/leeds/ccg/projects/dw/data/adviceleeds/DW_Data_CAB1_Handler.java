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
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;

/**
 * For handling data from CASE.
 */
public class DW_Data_CAB1_Handler extends DW_Object {

    public DW_Data_CAB1_Handler(DW_Environment env) {
        super(env);
    }

    /**
     * Loads LeedsCAB data from a file in directory, filename reporting progress
     * of loading to PrintWriter pw.
     *
     * @param directory
     * @param filename
     * @param pw
     * @return TreeMap<String,DW_Data_CAB1_Record> representing records
     */
    public TreeMap<String, DW_Data_CAB1_Record> loadInputData(
            Path directory, String filename, PrintWriter pw) throws IOException {
        System.out.println("Loading " + filename);
        TreeMap<String, DW_Data_CAB1_Record> r = new TreeMap<>();
        Path inputFile = Paths.get(directory.toString(), filename);
        try (BufferedReader br = Generic_IO.getBufferedReader(inputFile)) {
            StreamTokenizer st;
            st = new StreamTokenizer(br);
            Generic_IO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
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
            String line = "";
            long RecordID = 0;
            // Skip the header
            int headerLines = 16;
            for (int i = 0; i < headerLines; i++) {
                br.readLine();
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
                            DW_Data_CAB1_Record record = new DW_Data_CAB1_Record(env, RecordID, line, this);
                            String clientProfileID = record.getClientProfileID();
                            if (r.containsKey(clientProfileID)) {
                                System.out.println("Additional record for client " + clientProfileID);
                            } else {
                                r.put(clientProfileID, record);
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
        return r;
    }

}
