/*
 * Copyright (C) 2015 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.generated;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;

/**
 * A class for methods dealing with tables of data.
 * @author geoagdt
 */
public class DW_Table {

    /**
     * Reads a CSV file and returns each line as a String in the result.
     * @param f
     * @return the contents of the file as an {@code ArrayList<String>} with 
     * the elements being consecutive lines in the file.
     */
    public static ArrayList<String> readCSV(File f) {
        ArrayList<String> result;
        result = new ArrayList<>();
        try {
            BufferedReader br;
            br = Generic_IO.getBufferedReader(f);
            StreamTokenizer st;
            st = new StreamTokenizer(br);
            Generic_IO.setStreamTokenizerSyntax1(st);
            String line = "";
            int tokenType;
            tokenType = st.nextToken();
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        result.add(line);
                        //System.out.println(line);
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(DW_Table.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
