/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.adviceleeds;

import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataRecord;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Point;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Data;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe.DW_SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ProcessorAbstract;

/**
 * This is the main class for the Digital Welfare Project. For more details of
 * the project visit:
 * http://www.geog.leeds.ac.uk/people/a.turner/projects/DigitalWelfare/
 *
 * @author geoagdt
 */
public class DW_ProcessorAdviceLeeds extends DW_ProcessorAbstract {

    public DW_ProcessorAdviceLeeds() {
    }

    public DW_ProcessorAdviceLeeds(DW_Environment env) {
        super(env);
    }

    /**
     * Returns a transformation of outlet. The first part of outlet (part before
     * the first space) with a upper case first letter and the rest in lower
     * case.
     *
     * @param outlet May not be null.
     * @return
     */
    public String getCABOutletString(String outlet) {
        String result;
        String outletInUpperCase = outlet.split(" ")[0];
        String outletInUpperCaseFirstLetter;
        outletInUpperCaseFirstLetter = outletInUpperCase.substring(0, 1);
        String outletInUpperCaseNotFirstLetter;
        outletInUpperCaseNotFirstLetter = outletInUpperCase.substring(1);
        String outletInLowerCaseNotFirstLetter;
        outletInLowerCaseNotFirstLetter = Generic_StaticString.getLowerCase(outletInUpperCaseNotFirstLetter);
        result = outletInUpperCaseFirstLetter + outletInLowerCaseNotFirstLetter;
        return result;
    }

    /**
     * Initialise a file and to write rectangular csv records to.
     *
     * @param dir
     * @param aSHBEgeneralisation The data to be written out in order of the
     * keys.
     * @param name The first part of the filename.
     * @param var1 The name for column 0
     * @param var2 The name for column 1
     */
    public void createCSV(
            File dir,
            TreeMap<String, Integer> aSHBEgeneralisation,
            String name,
            String var1,
            String var2) {
        PrintWriter printWriter = init_OutputTextFilePrintWriter(
                dir,
                name + ".csv");
        printWriter.println(var1 + "," + var2);
        Iterator<String> ite = aSHBEgeneralisation.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Integer value = aSHBEgeneralisation.get(key);
            printWriter.println(key + "," + value);
        }
        printWriter.close();
    }

    /**
     * Initialise a file and to write rectangular csv records to. In addition to
     * writing out the data in aSHBEgeneralisation,
     * aCouncilRecordgeneralisation, and aRSLRecordgeneralisation some sums and
     * proportions are calculated.
     *
     * @param dir
     * @param aSHBEgeneralisation Data to be written out in order of the keys.
     * @param aCouncilRecordgeneralisation Data to be written out in order of
     * the keys.
     * @param aRSLRecordgeneralisation Data to be written out in order of the
     * keys.
     * @param name The first part of the filename.
     * @param header The header to be written to the file.
     */
    public void createCSV(
            File dir,
            TreeMap<String, Integer> aSHBEgeneralisation,
            TreeMap<String, Integer> aCouncilRecordgeneralisation,
            TreeMap<String, Integer> aRSLRecordgeneralisation,
            String name,
            String header) {
        PrintWriter pw;
        pw = init_OutputTextFilePrintWriter(
                dir,
                name + ".csv");
        pw.println(header);
        Iterator<String> ite = aSHBEgeneralisation.keySet().iterator();
        while (ite.hasNext()) {
            String key = ite.next();
            Integer value1 = aSHBEgeneralisation.get(key);
            Integer value2;
            if (aCouncilRecordgeneralisation.get(key) == null) {
                value2 = 0;
            } else {
                value2 = aCouncilRecordgeneralisation.get(key);
            }
            Integer value3;
            if (aRSLRecordgeneralisation.get(key) == null) {
                value3 = 0;
            } else {
                value3 = aRSLRecordgeneralisation.get(key);
            }
            Integer value4 = value2 + value3;
            double proportion = ((double) value4 / (double) value1) * 100;
            pw.println(key + "," + value1 + ", " + value2 + ", " + value3 + ", " + value4 + ", " + proportion);
        }
        pw.close();
    }


    public TreeMap<String, String> getOutletsAndPostcodes() {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        // Postcodes in data from Leeds CAB
        result.put("Otley", "LS21 1BG");
        result.put("Morley", "LS27 9DY");
        result.put("Crossgates", "LS15 8QR");
        result.put("Pudsey", "LS28 7AB");
        result.put("City", "LS2 7DT");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.put("Chapeltown", "LS7 4BZ");
        return result;
    }

    public TreeMap<String, Geotools_Point> getOutletsAndPoints() {
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getPostcode_Handler();
        TreeMap<String, Geotools_Point> result;
        result = DW_Postcode_Handler.postcodeToPoints(getOutletsAndPostcodes(),
                DW_Postcode_Handler.getDefaultYM3());
        return result;
    }

    public TreeMap<String, String> getAdviceLeedsNamesAndPostcodes() {
        TreeMap<String, String> result;
        result = new TreeMap<String, String>();
        ArrayList<String> tAdviceLeedsServiceNames;
        tAdviceLeedsServiceNames = getAdviceLeedsServiceNames();
        ArrayList<String> tAdviceLeedsServicePostcodes;
        tAdviceLeedsServicePostcodes = getAdviceLeedsServicePostcodes();
        for (int i = 0; i < tAdviceLeedsServiceNames.size(); i++) {
            result.put(tAdviceLeedsServiceNames.get(i),
                    tAdviceLeedsServicePostcodes.get(i));
        }
        return result;
    }

    /**
     * Postcodes are in the same order as names from
     * getAdviceLeedsServiceNames().
     *
     * @return
     */
    public ArrayList<String> getAdviceLeedsServicePostcodes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        // Postcodes in data from Leeds CAB
        result.add("LS21 1BG");
        result.add("LS27 9DY");
        result.add("LS15 8QR");
        result.add("LS28 7AB");
        result.add("LS2 7DT");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.add("LS7 4BZ");
        // http://www.adviceleeds.org.uk/partmembers.htm
        result.add("LS9 7UT");
        result.add("LS9 9AA");
        result.add("LS6 1QF");
        result.add("LS9 7BG");
        result.add("LS8 4HS");
        return result;
    }

    /**
     * Names are in the same order as postcodes from
     * getAdviceLeedsServiceNames().
     *
     * @return
     */
    public ArrayList<String> getAdviceLeedsServiceNames() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        // Postcodes in data from Leeds CAB
        result.add("Leeds CAB Otley");
        result.add("Leeds CAB Morley");
        result.add("Leeds CAB Crossgates");
        result.add("Leeds CAB Pudsey");
        result.add("Leeds CAB City");
        // http://www.citizensadvice.org.uk/bureau_detail.htm?serialnumber=100610
        result.add("Chapeltown CAB");
        // http://www.adviceleeds.org.uk/partmembers.htm
        result.add("Ebor Gardens");
        result.add("St Vincents");
        result.add("Better Leeds Communities");
        result.add("LCC - Welfare Rights Unit");
        result.add("Leeds Law Centre");
        return result;
    }

    public TreeMap<String, Geotools_Point> getAdviceLeedsNamesAndPoints() {
        TreeMap<String, Geotools_Point> result;
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getPostcode_Handler();
        result = DW_Postcode_Handler.postcodeToPoints(getAdviceLeedsNamesAndPostcodes(),
                DW_Postcode_Handler.getDefaultYM3());
        return result;
    }


}
