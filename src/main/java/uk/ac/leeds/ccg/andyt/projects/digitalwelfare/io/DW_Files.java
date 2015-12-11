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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author geoagdt
 */
public class DW_Files {

    private static File dir;
    private static File inputDir;
    private static File inputAdviceLeedsDir;
    private static File inputCensusDir;
    private static File inputCensus2011Dir;
    private static File inputPostcodeDir;
    private static File inputCodePointDir;
    private static File inputONSPDDir;
    private static File inputLCCDir;
    private static File inputSHBEDir;
    private static File inputUnderOccupiedDir;

    private static File swapDir;
    private static File swapLCCDir;
    private static File swapSHBEDir;

    private static File generatedDir;
    private static File generatedAdviceLeedsDir;
    private static File generatedCensusDir;
    private static File generatedCensus2011Dir;
    private static File generatedCensus2011LUTsDir;
    private static File generatedPostcodeDir;
    private static File generatedCodePointDir;
    private static File generatedONSPDDir;
    private static File generatedLCCDir;
    private static File generatedSHBEDir;
    private static File generatedUnderOccupiedDir;

    private static File outputDir;
    private static File outputAdviceLeedsDir;
    private static File outputAdviceLeedsMapsDir;
    private static File outputCensusDir;
    private static File outputCensus2011Dir;
    private static File outputLCCDir;
    private static File outputSHBEDir;
    private static File outputSHBEMapsDir;
    private static File outputSHBETablesDir;
    private static File outputSHBEPlotsDir;
    private static File outputAdviceLeedsTablesDir;
    private static File outputUnderOccupiedDir;
    
    public static final String sTenancy = "Tenancy";
    public static final String sBoundaryData = "BoundaryData";
    public static final String sONSPD = "ONSPD";
    public static final String sSHBE = "SHBE";
    public static final String sUO = "UO";
    public static final String sUnderOccupied = "UnderOccupied";
    public static final String sGrouped = "Grouped";
    public static final String sUngrouped = "Ungrouped";
    public static final String sPostcode = "Postcode";
    public static final String sPostcodeChanges = "PostcodeChanges";
    public static final String sPostcodeChanged = "PostcodeChanged";
    public static final String sPostcodeUnchanged = "PostcodeUnchanged";
    public static final String sTenancyTypeTransition = "TenancyTypeTransition";
    public static final String sTenancyAndPostcodeChanges = "TenancyAndPostcodeChanges";
    public static final String sCheckedPreviousTenure = "CheckedPreviousTenure";
    public static final String sNotCheckedPreviousTenure = "NotCheckedPreviousTenure";
    public static final String sCheckedPreviousPostcode = "CheckedPreviousPostcode";
    public static final String sNotCheckedPreviousPostcode = "NotCheckedPreviousPostcode";
    public static final String sLCC = "LCC";
    public static final String sCouncil = "Council";
    public static final String sRSL = "RSL";
    public static final String sMaps = "Maps";
    public static final String sPlots = "Plots";
    public static final String sTenancyTypeTransitionLineGraphs = "TenancyTypeTransitionLineGraphs";
    public static final String sTables = "Tables";
    public static final String sLine = "Line";
    public static final String sChoropleth = "Choropleth";
    public static final String sAll = "All";
    public static final String sInput = "Input";
    public static final String sCensus = "Census";
    public static final String s2011 = "2011";
    public static final String sGenerated = "Generated";
    public static final String sOutput = "Output";
    public static final String sAdviceLeeds = "AdviceLeeds";    
    public static final String sDigitalWelfareDir = "/scratch02/DigitalWelfare";
    
    public static File getDigitalWelfareDir() {
        if (dir == null) {
            dir = new File(sDigitalWelfareDir);
        }
        return dir;
    }

    public static File getInputDir() {
        if (inputDir == null) {
            inputDir = new File(
                    getDigitalWelfareDir(),
                    sInput);
        }
        return inputDir;
    }

    public static File getInputAdviceLeedsDir() {
        if (inputAdviceLeedsDir == null) {
            inputAdviceLeedsDir = new File(
                    getInputDir(),
                    sAdviceLeeds);
        }
        return inputAdviceLeedsDir;
    }

    public static File getInputCensusDir() {
        if (inputCensusDir == null) {
            inputCensusDir = new File(
                    getInputDir(),
                    sCensus);
        }
        return inputCensusDir;
    }

    public static File getInputCensus2011Dir() {
        if (inputCensus2011Dir == null) {
            inputCensus2011Dir = new File(
                    getInputCensusDir(),
                    s2011);
        }
        return inputCensus2011Dir;
    }

    public static File getInputCensus2011Dir(
            String level) {
        return new File(getInputCensus2011Dir(),
                level);
    }

    public static File getInputCensus2011AttributeDataDir(
            String level) {
        return new File(
                getInputCensus2011Dir(level),
                "AttributeData");
    }

    public static File getInputCensus2011BoundaryDataDir(
            String level) {
        return new File(
                getInputCensus2011Dir(level),
                sBoundaryData);
    }

    public static File getInputCodePointDir(
            String year,
            String dirname) {
        if (inputCodePointDir == null) {
            inputCodePointDir = new File(
                    getInputPostcodeDir(),
                    sBoundaryData);
            inputCodePointDir = new File(
                    inputCodePointDir,
                    "CodePoint");
            inputCodePointDir = new File(
                    inputCodePointDir,
                    year);//"2015");
            inputCodePointDir = new File(
                    inputCodePointDir,
                    dirname);
        }
        return inputCodePointDir;
    }

    public static File getInputPostcodeDir() {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = new File(getInputDir(),
                    sPostcode);
        }
        return inputPostcodeDir;
    }

    public static File getInputONSPDDir() {
        if (inputONSPDDir == null) {
            inputONSPDDir = new File(
                    getInputPostcodeDir(),
                    sONSPD);
        }
        return inputONSPDDir;
    }

    public static File getInputLCCDir() {
        if (inputLCCDir == null) {
            inputLCCDir = new File(
                    getInputDir(),
                    sLCC);
        }
        return inputLCCDir;
    }

    public static File getInputSHBEDir() {
        if (inputSHBEDir == null) {
            inputSHBEDir = new File(
                    getInputLCCDir(),
                    sSHBE);
        }
        return inputSHBEDir;
    }

    public static File getInputUnderOccupiedDir() {
        if (inputUnderOccupiedDir == null) {
            inputUnderOccupiedDir = new File(
                    getInputLCCDir(),
                    sUnderOccupied);
        }
        return inputUnderOccupiedDir;
    }

    public static File getSwapDir() {
        if (swapDir == null) {
            swapDir = new File(
                    getDigitalWelfareDir(),
                    "Swap");
            swapDir.mkdirs();
        }
        return swapDir;
    }

    public static File getSwapLCCDir() {
        if (swapLCCDir == null) {
            swapLCCDir = new File(
                    getSwapDir(),
                    sLCC);
            swapLCCDir.mkdirs();
        }
        return swapLCCDir;
    }

    public static File getSwapSHBEDir() {
        if (swapSHBEDir == null) {
            swapSHBEDir = new File(
                    getSwapLCCDir(),
                    sSHBE);
            swapSHBEDir.mkdirs();
        }
        return swapSHBEDir;
    }

    public static File getGeneratedDir() {
        if (generatedDir == null) {
            generatedDir = new File(
                    getDigitalWelfareDir(),
                    sGenerated);
            generatedDir.mkdirs();
        }
        return generatedDir;
    }

    public static File getGeneratedAdviceLeedsDir() {
        if (generatedAdviceLeedsDir == null) {
            generatedAdviceLeedsDir = new File(
                    getGeneratedDir(),
                    sAdviceLeeds);
            generatedAdviceLeedsDir.mkdirs();
        }
        return generatedAdviceLeedsDir;
    }

    public static File getGeneratedCensusDir() {
        if (generatedCensusDir == null) {
            generatedCensusDir = new File(
                    getGeneratedDir(),
                    sCensus);
            generatedCensusDir.mkdirs();
        }
        return generatedCensusDir;
    }

    public static File getGeneratedCensus2011Dir() {
        if (generatedCensus2011Dir == null) {
            generatedCensus2011Dir = new File(
                    getGeneratedCensusDir(),
                    s2011);
            generatedCensus2011Dir.mkdirs();
        }
        return generatedCensus2011Dir;
    }

    public static File getGeneratedCensus2011Dir(
            String level) {
        return new File(getGeneratedCensus2011Dir(),
                level);
    }

    public static File getGeneratedCensus2011LUTsDir() {
        if (generatedCensus2011LUTsDir == null) {
            generatedCensus2011LUTsDir = new File(
                    getGeneratedCensus2011Dir(),
                    "LUTs");
            generatedCensus2011LUTsDir.mkdirs();
        }
        return generatedCensus2011LUTsDir;
    }

    public static File getGeneratedPostcodeDir() {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = new File(
                    getGeneratedDir(),
                    sPostcode);
            generatedPostcodeDir.mkdirs();
        }
        return generatedPostcodeDir;
    }

    public static File getGeneratedCodePointDir() {
        if (generatedCodePointDir == null) {
            generatedCodePointDir = new File(
                    getGeneratedPostcodeDir(),
                    sBoundaryData);
            generatedCodePointDir = new File(
                    generatedCodePointDir,
                    "CodePoint");
            generatedCodePointDir.mkdirs();
        }
        return generatedCodePointDir;
    }

    public static File getGeneratedONSPDDir() {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = new File(
                    getGeneratedPostcodeDir(),
                    sONSPD);
            generatedONSPDDir.mkdirs();
        }
        return generatedONSPDDir;
    }

    public static File getGeneratedLCCDir() {
        if (generatedLCCDir == null) {
            generatedLCCDir = new File(
                    getGeneratedDir(),
                    sLCC);
            generatedLCCDir.mkdirs();
        }
        return generatedLCCDir;
    }

    public static File getGeneratedSHBEDir() {
        if (generatedSHBEDir == null) {
            generatedSHBEDir = new File(
                    getGeneratedLCCDir(),
                    sSHBE);
            generatedSHBEDir.mkdirs();
        }
        return generatedSHBEDir;
    }

    public static File getGeneratedSHBEDir(
            String dirname) {
        if (dirname == null) {
            return getGeneratedSHBEDir();
        }
        return new File(
                getGeneratedSHBEDir(),
                dirname);
    }

    public static File getGeneratedSHBEDir(
            String level,
            boolean doUnderOccupied,
            boolean doCouncil) {
        File result;
        result = new File(
                getGeneratedSHBEDir(),
                level);
        result = getUOFile(
                result,
                doUnderOccupied,
                doCouncil);
        return result;
    }

    public static File getUOFile(
            File f,
            boolean doUnderOccupied,
            boolean doCouncil) {
        File result;
        if (doUnderOccupied) {
            result = new File(
                    f,
                    sUO);
            //UnderOccupiedString);
            if (doCouncil) {
                result = new File(
                        result,
                        sCouncil);
            } else {
                result = new File(
                        result,
                        sRSL);
            }
        } else {
            result = new File(
                    f,
                    sAll);
        }
        return result;
    }

    public static File getUOFile(
            File f,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL) {
        File result;
        if (doUnderOccupied) {
            result = new File(
                    f,
                    sUO);
            //UnderOccupiedString);
            if (doCouncil & doRSL) {
                result = new File(
                        result,
                        sAll);
            } else {
                if (doCouncil) {
                    result = new File(
                            result,
                            sCouncil);
                } else {
                    result = new File(
                            result,
                            sRSL);
                }
            }
        } else {
            result = new File(
                    f,
                    sAll);
        }
        return result;
    }

    public static ArrayList<File> getGeneratedSHBELevelDirsArrayList(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil) {
        ArrayList<File> result;
        result = new ArrayList<File>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File outputDir = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    level);
            outputDir = getUOFile(outputDir, doUnderOccupied, doCouncil);
            result.add(outputDir);
        }
        return result;
    }

    public static TreeMap<String, File> getGeneratedSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil) {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File outputDir = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    level);
            outputDir = getUOFile(outputDir, doUnderOccupied, doCouncil);
            result.put(level, outputDir);
        }
        return result;
    }

    public static TreeMap<String, File> getGeneratedSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL) {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File outputDir = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    level);
            outputDir = getUOFile(outputDir, doUnderOccupied, doCouncil);
            result.put(level, outputDir);
        }
        return result;
    }

    public static File getGeneratedUnderOccupiedDir() {
        if (generatedUnderOccupiedDir == null) {
            generatedUnderOccupiedDir = new File(
                    getGeneratedLCCDir(),
                    sUnderOccupied);
            generatedUnderOccupiedDir.mkdirs();
        }
        return generatedUnderOccupiedDir;
    }

    public static File getOutputDir() {
        if (outputDir == null) {
            outputDir = new File(
                    getDigitalWelfareDir(),
                    sOutput);
            outputDir.mkdirs();
        }
        return outputDir;
    }

    public static File getOutputAdviceLeedsDir() {
        if (outputAdviceLeedsDir == null) {
            outputAdviceLeedsDir = new File(
                    getOutputDir(),
                    sAdviceLeeds);
            outputAdviceLeedsDir.mkdirs();
        }
        return outputAdviceLeedsDir;
    }

    public static File getOutputAdviceLeedsMapsDir() {
        if (outputAdviceLeedsMapsDir == null) {
            outputAdviceLeedsMapsDir = new File(
                    getOutputAdviceLeedsDir(),
                    sMaps);
            outputAdviceLeedsMapsDir.mkdirs();

        }
        return outputAdviceLeedsMapsDir;
    }

    public static File getOutputAdviceLeedsTablesDir() {
        if (outputAdviceLeedsTablesDir == null) {
            outputAdviceLeedsTablesDir = new File(
                    getOutputAdviceLeedsDir(),
                    sTables);
            outputAdviceLeedsTablesDir.mkdirs();
        }
        return outputAdviceLeedsTablesDir;
    }

    public static File getOutputCensusDir() {
        if (outputCensusDir == null) {
            outputCensusDir = new File(
                    getOutputDir(),
                    sCensus);
            outputCensusDir.mkdirs();
        }
        return outputCensusDir;
    }

    public static File getOutputCensus2011Dir() {
        if (outputCensus2011Dir == null) {
            outputCensus2011Dir = new File(
                    getOutputCensusDir(),
                    s2011);
            outputCensus2011Dir.mkdirs();
        }
        return outputCensus2011Dir;
    }

    public static File getOutputCensus2011Dir(String level) {
        File result;
        result = new File(
                getOutputCensus2011Dir(),
                level);
        outputDir.mkdirs();
        return result;
    }

    public static File getOutputLCCDir() {
        if (outputLCCDir == null) {
            outputLCCDir = new File(
                    getOutputDir(),
                    sLCC);
            outputLCCDir.mkdirs();
        }
        return outputLCCDir;
    }

    public static File getOutputSHBEDir() {
        if (outputSHBEDir == null) {
            outputSHBEDir = new File(
                    getOutputLCCDir(),
                    sSHBE);
            outputSHBEDir.mkdirs();
        }
        return outputSHBEDir;
    }

    public static File getOutputSHBEMapsDir() {
        if (outputSHBEMapsDir == null) {
            outputSHBEMapsDir = new File(
                    getOutputSHBEDir(),
                    sMaps);
            outputSHBEMapsDir.mkdirs();
        }
        return outputSHBEMapsDir;
    }

    public static File getOutputSHBETablesDir() {
        if (outputSHBETablesDir == null) {
            outputSHBETablesDir = new File(
                    getOutputSHBEDir(),
                    sTables);
            outputSHBETablesDir.mkdirs();
        }
        return outputSHBETablesDir;
    }

    public static File getOutputSHBETablesTenancyTypeTransitionDir(
            String type,
            String paymentType,
            boolean checkPreviousTenure) {
        File result = new File(
                DW_Files.getOutputSHBETablesDir(),
                sTenancy);
        result = new File(
                result,
                paymentType);
        result = new File(
                result,
                type);
        result = new File(
                result,
                sTenancyTypeTransition);
        if (checkPreviousTenure) {
            result = new File(
                    result,
                    sCheckedPreviousTenure);
        } else {
            result = new File(
                    result,
                    sNotCheckedPreviousTenure);
        }
        return result;
    }

    public static File getOutputSHBEPlotsDir() {
        if (outputSHBEPlotsDir == null) {
            outputSHBEPlotsDir = new File(
                    getOutputSHBEDir(),
                    sPlots);
            outputSHBEPlotsDir.mkdirs();
        }
        return outputSHBEPlotsDir;
    }

    public static File getOutputSHBEPlotsTenancyTypeTransitionDir(
            String type,
            String paymentType,
            boolean checkPreviousTenure) {
        File result = new File(
                DW_Files.getOutputSHBEPlotsDir(),
                sTenancy);
        result = new File(
                result,
                paymentType);
        result = new File(
                result,
                type);
        result = new File(
                result,
                sTenancyTypeTransitionLineGraphs);
        if (checkPreviousTenure) {
            result = new File(
                    result,
                    sCheckedPreviousTenure);
        } else {
            result = new File(
                    result,
                    sNotCheckedPreviousTenure);
        }
        return result;
    }

    public static File getOutputSHBELineMapsDir() {
        return new File(
                getOutputSHBEMapsDir(),
                sLine);
    }

    public static File getOutputSHBEChoroplethDir() {
        return new File(
                getOutputSHBEMapsDir(),
                sChoropleth);
    }

    public static File getOutputSHBEChoroplethDir(
            String level) {
        return new File(
                getOutputSHBEChoroplethDir(),
                level);
    }

    public static File getOutputUnderOccupiedDir() {
        if (outputUnderOccupiedDir == null) {
            outputUnderOccupiedDir = new File(
                    getOutputLCCDir(),
                    sUnderOccupied);
            outputUnderOccupiedDir.mkdirs();
        }
        return outputUnderOccupiedDir;
    }

}
