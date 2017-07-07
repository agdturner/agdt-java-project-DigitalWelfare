/*
 * Copyright (C) 2016 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_CorrectedPostcodes extends DW_Object {

    protected transient DW_Postcode_Handler DW_Postcode_Handler;
    protected transient DW_Files DW_Files;
    protected transient DW_Strings DW_Strings;

    private HashMap<String, HashSet<String>> UnmappableToMappablePostcodes;
    private HashMap<String, ArrayList<String>> ClaimRefToOriginalPostcodes;
    private HashMap<String, ArrayList<String>> ClaimRefToCorrectedPostcodes;
    private HashSet<String> PostcodesCheckedAsMappable;

    public DW_CorrectedPostcodes() {
    }

    public DW_CorrectedPostcodes(DW_Environment env) {
        super(env);
        DW_Postcode_Handler = env.getDW_Postcode_Handler();
        DW_Files = env.getDW_Files();
        DW_Strings = env.getDW_Strings();
    }

    public static void main(String[] args) {
        new DW_CorrectedPostcodes(new DW_Environment(0)).run();
    }

    /**
     * Currently this reads in Postcodes2016-10.csv. These are postcodes that 
     * have been checked in Academy and either corrected, or checked and found 
     * to be mappable.
     */
    public void run() {
        DW_Files = getDW_Files();
        File dir;
        dir = new File(
                DW_Files.getInputLCCDir(),
                "AcademyPostcodeCorrections");
        File f;
        f = new File(
                dir,
                "Postcodes2016-10.csv");
        //Generic_ReadCSV.testRead(f, dir, 6);
        ArrayList<String> lines;
        lines = Generic_ReadCSV.read(f, dir, 6);
        Iterator<String> ite;
        ite = lines.iterator();
        String s;
        s = ite.next(); // Skip header
        String[] split;
        String ClaimRef;
        String OriginalPostcode;
        String CorrectedPostcode;
        String OriginalPostcodeF;
        String CorrectedPostcodeF;

        UnmappableToMappablePostcodes = new HashMap<String, HashSet<String>>();
        ClaimRefToOriginalPostcodes = new HashMap<String, ArrayList<String>>();
        ClaimRefToCorrectedPostcodes = new HashMap<String, ArrayList<String>>();
        PostcodesCheckedAsMappable = new HashSet<String>();

        while (ite.hasNext()) {
            s = ite.next();
            split = s.split(",");
            ClaimRef = split[1].trim();
            OriginalPostcode = split[2];
            OriginalPostcodeF = DW_Postcode_Handler.formatPostcode(OriginalPostcode);
            CorrectedPostcode = split[3];
            CorrectedPostcodeF = DW_Postcode_Handler.formatPostcode(CorrectedPostcode);
//            for (int i = 0; i < split.length; i ++ ){
//                System.out.print(split[i].trim() + " ");
//            }
            if (OriginalPostcodeF.equalsIgnoreCase(CorrectedPostcodeF)) {
                //System.out.println(ClaimRef + ", " + OriginalPostcode + ", " + CorrectedPostcode + ", N");
                PostcodesCheckedAsMappable.add(OriginalPostcodeF);
            } else {
                //System.out.println(ClaimRef + ", " + OriginalPostcode + ", " + CorrectedPostcode + ", Y");
                HashSet<String> MappablePostcodes;                
                if (UnmappableToMappablePostcodes.containsKey(OriginalPostcodeF)) {
                    MappablePostcodes = UnmappableToMappablePostcodes.get(OriginalPostcodeF);
                } else {
                    MappablePostcodes = new HashSet<String>();
                    UnmappableToMappablePostcodes.put(OriginalPostcodeF, MappablePostcodes);
                }
                MappablePostcodes.add(CorrectedPostcodeF);
                ArrayList<String> OriginalPostcodes;
                OriginalPostcodes = new ArrayList<String>();
                OriginalPostcodes.add(OriginalPostcodeF);
                getClaimRefToOriginalPostcodes().put(ClaimRef, OriginalPostcodes);
                ArrayList<String> CorrectedPostcodes;
                CorrectedPostcodes = new ArrayList<String>();
                CorrectedPostcodes.add(CorrectedPostcodeF);
                getClaimRefToCorrectedPostcodes().put(ClaimRef, CorrectedPostcodes);
            }
        }
        System.out.println("UnmappableToMappablePostcode.size() " + UnmappableToMappablePostcodes.size());

        System.out.println("UnmappableToMappablePostcode");
        ite = UnmappableToMappablePostcodes.keySet().iterator();
        while (ite.hasNext()) {
            OriginalPostcodeF = ite.next();
            System.out.print(OriginalPostcodeF);
            HashSet<String> CorrectedPostcodes = getUnmappableToMappablePostcodes().get(OriginalPostcodeF);
            Iterator<String> ite2;
            ite2 = CorrectedPostcodes.iterator();
            while (ite2.hasNext()) {
                CorrectedPostcodeF = ite2.next();
                //System.out.print(", " + CorrectedPostcodeF);
            }
            System.out.println();
        }
        //System.out.println();

        System.out.println("PostcodesCheckedAsMappable");
        ite = getPostcodesCheckedAsMappable().iterator();
        while (ite.hasNext()) {
            OriginalPostcodeF = ite.next();
            //System.out.println(OriginalPostcodeF);
        }

        File dirout;
        dirout = DW_Files.getGeneratedLCCDir();
        File fout;
        fout = new File(dirout,
        "DW_CorrectedPostcodes" + getDW_Strings().sBinaryFileExtension);
        Generic_StaticIO.writeObject(this, fout);
    }

    private DW_Files getDW_Files() {
        if (DW_Files == null) {
            DW_Files = new DW_Files();
        }
        return DW_Files;
    }

    private DW_Strings getDW_Strings() {
        if (DW_Strings == null) {
            DW_Strings = new DW_Strings();
        }
        return DW_Strings;
    }

    
    /**
     * @return the UnmappableToMappablePostcode
     */
    public HashMap<String, HashSet<String>> getUnmappableToMappablePostcodes() {
        return UnmappableToMappablePostcodes;
    }

    /**
     * @return the ClaimRefToOriginalPostcode
     */
    public HashMap<String, ArrayList<String>> getClaimRefToOriginalPostcodes() {
        return ClaimRefToOriginalPostcodes;
    }

    /**
     * @return the ClaimRefToCorrectedPostcode
     */
    public HashMap<String, ArrayList<String>> getClaimRefToCorrectedPostcodes() {
        return ClaimRefToCorrectedPostcodes;
    }

    /**
     * @return the PostcodesCheckedAsMappable
     */
    public HashSet<String> getPostcodesCheckedAsMappable() {
        return PostcodesCheckedAsMappable;
    }
    
    
}
