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
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;

/**
 * A basic convenience class that does not need a reference to the main 
 * DW_Environment.
 * @author geoagdt
 */
public class DW_Files extends DW_Object {

    /**
     * Provided for convenience. This is a reference to env.tDW_Strings.
     */
    protected DW_Strings tDW_Strings;
    
    /**
     * Short code for 2011.
     */
    public final String s2011 = "2011";
    
    /**
     * Short code for AdviceLeeds.
     */
    public final String sAdviceLeeds = "AdviceLeeds";

    /**
     * Short code for AttributeData.
     */
    public final String sAttributeData = "AttributeData";

    /**
     * Short code for BoundaryData.
     */
    public final String sBoundaryData = "BoundaryData";

    /**
     * Short code for Census.
     */
    public final String sCensus = "Census";

    /**
     * Short code for Choropleth.
     */
    public final String sChoropleth = "Choropleth";
    
    /**
     * Short code for CodePoint.
     */
    public final String sCodePoint = "CodePoint";

    /**
     * Short code for Generated.
     */
    public final String sGenerated = "Generated";

    /**
     * Short code for Input.
     */
    public final String sInput = "Input";

    /**
     * Short code for Look Up Tables.
     */
    public final String sLUTs = "LUTs";
    
    /**
     * Short code for LeedsCityCouncil.
     */
    public final String sLCC = "LCC";

    /**
     * Short code for Maps.
     */
    public final String sMaps = "Maps";

    /**
     * Short code for ONS Postcode Directory
     */
    public final String sONSPD = "ONSPD";

    /**
     * Short code for Output.
     */
    public final String sOutput = "Output";

    /**
     * Short code for Plots.
     */
    public final String sPlots = "Plots";

    /**
     * Short code for PostcodeChanged.
     */
    public final String sPostcode = "Postcode";
    
    /**
     * Short code for SHBE.
     */
    public final String sSHBE = "SHBE";

    /**
     * Short code for Swap.
     */
    public final String sSwap = "Swap";
    
    /**
     * Short code for UnderOccupied.
     */
    public final String sUnderOccupied = "UnderOccupied";

    /**
     * Short code for Tables.
     */
    public final String sTables = "Tables";

    protected final String sDotdat = ".dat";

    public String getsDotdat() {
        return sDotdat;
    }
    
    /**
     * For storing the main directory location where the project files are
     * stored. This is initialised from DW_Environment.sDigitalWelfareDir.
     */
    private File dir;

    /**
     * For storing the main input data directory.
     */
    private File inputDir;

    /**
     * For storing the input AdviceLeeds data directory inside
     * <code>inputDir</code>.
     */
    private File inputAdviceLeedsDir;
    
    /**
     * For storing the input Census data directory inside the main input data
     * directory.
     */
    private File inputCensusDir;
    
    /**
     * For storing the 2011 Census directory inside the Census data directory.
     */
    private File inputCensus2011Dir;
    
    /**
     * For storing the Postcode directory inside the main input data directory.
     */
    private File inputPostcodeDir;
    
    /**
     * For storing the CodePoint directory inside the main input data directory.
     */
    private File inputCodePointDir;
    private File inputONSPDDir;
    private File inputLCCDir;
    private File inputSHBEDir;
    private File inputUnderOccupiedDir;

    private File swapDir;
    private File swapLCCDir;
    private File swapSHBEDir;

    private File generatedDir;
    private File generatedAdviceLeedsDir;
    private File generatedCensusDir;
    private File generatedCensus2011Dir;
    private File generatedCensus2011LUTsDir;
    private File generatedPostcodeDir;
    private File generatedCodePointDir;
    private File generatedONSPDDir;
    private File generatedLCCDir;
    private File generatedSHBEDir;
    private File generatedUnderOccupiedDir;

    private File outputDir;
    private File outputAdviceLeedsDir;
    private File outputAdviceLeedsMapsDir;
    private File outputCensusDir;
    private File outputCensus2011Dir;
    private File outputLCCDir;
    private File outputSHBEDir;
    private File outputSHBEMapsDir;
    private File outputSHBETablesDir;
    private File outputSHBEPlotsDir;
    private File outputAdviceLeedsTablesDir;
    private File outputUnderOccupiedDir;
    /**
     * Short code for Line.
     */
    public final String sLine = "Line";
    /**
     * Short code for Density.
     */
    public final String sDensity = "Density";
    

    /**
     * Create a new instance.
     */
    public DW_Files(DW_Environment env){
        this.env = env;
        if (env != null) {
            this.tDW_Strings = env.getDW_Strings();
        } else {
            this.tDW_Strings = new DW_Strings();
        }
    }
    
    public File getDigitalWelfareDir() {
        if (dir == null) {
            dir = new File(env.sDigitalWelfareDir);
        }
        return dir;
    }

    public File getInputDir() {
        if (inputDir == null) {
            inputDir = new File(
                    getDigitalWelfareDir(),
                    sInput);
        }
        return inputDir;
    }

    public File getInputAdviceLeedsDir() {
        if (inputAdviceLeedsDir == null) {
            inputAdviceLeedsDir = new File(
                    getInputDir(),
                    sAdviceLeeds);
        }
        return inputAdviceLeedsDir;
    }

    public File getInputCensusDir() {
        if (inputCensusDir == null) {
            inputCensusDir = new File(
                    getInputDir(),
                    sCensus);
        }
        return inputCensusDir;
    }

    public File getInputCensus2011Dir() {
        if (inputCensus2011Dir == null) {
            inputCensus2011Dir = new File(
                    getInputCensusDir(),
                    s2011);
        }
        return inputCensus2011Dir;
    }

    public File getInputCensus2011Dir(
            String level) {
        return new File(getInputCensus2011Dir(),
                level);
    }

    public File getInputCensus2011AttributeDataDir(
            String level) {
        return new File(
                getInputCensus2011Dir(level),
                sAttributeData);
    }

    public File getInputCensus2011BoundaryDataDir(
            String level) {
        return new File(
                getInputCensus2011Dir(level),
                sBoundaryData);
    }

    public File getInputCodePointDir(
            String year,
            String dirname) {
        if (inputCodePointDir == null) {
            inputCodePointDir = new File(
                    getInputPostcodeDir(),
                    sBoundaryData);
            inputCodePointDir = new File(
                    inputCodePointDir,
                    sCodePoint);
            inputCodePointDir = new File(
                    inputCodePointDir,
                    year);//"2015");
            inputCodePointDir = new File(
                    inputCodePointDir,
                    dirname);
        }
        return inputCodePointDir;
    }

    public File getInputPostcodeDir() {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = new File(getInputDir(),
                    sPostcode);
        }
        return inputPostcodeDir;
    }

    public File getInputONSPDDir() {
        if (inputONSPDDir == null) {
            inputONSPDDir = new File(
                    getInputPostcodeDir(),
                    sONSPD);
        }
        return inputONSPDDir;
    }

    public File getInputLCCDir() {
        if (inputLCCDir == null) {
            inputLCCDir = new File(
                    getInputDir(),
                    sLCC);
        }
        return inputLCCDir;
    }

    public File getInputSHBEDir() {
        if (inputSHBEDir == null) {
            inputSHBEDir = new File(
                    getInputLCCDir(),
                    sSHBE);
        }
        return inputSHBEDir;
    }

    public File getInputUnderOccupiedDir() {
        if (inputUnderOccupiedDir == null) {
            inputUnderOccupiedDir = new File(
                    getInputLCCDir(),
                    sUnderOccupied);
        }
        return inputUnderOccupiedDir;
    }

    public File getSwapDir() {
        if (swapDir == null) {
            swapDir = new File(
                    getDigitalWelfareDir(),
                    sSwap);
            swapDir.mkdirs();
        }
        return swapDir;
    }

    public File getSwapLCCDir() {
        if (swapLCCDir == null) {
            swapLCCDir = new File(
                    getSwapDir(),
                    sLCC);
            swapLCCDir.mkdirs();
        }
        return swapLCCDir;
    }

    public File getSwapSHBEDir() {
        if (swapSHBEDir == null) {
            swapSHBEDir = new File(
                    getSwapLCCDir(),
                    sSHBE);
            swapSHBEDir.mkdirs();
        }
        return swapSHBEDir;
    }

    public File getGeneratedDir() {
        if (generatedDir == null) {
            generatedDir = new File(
                    getDigitalWelfareDir(),
                    sGenerated);
            generatedDir.mkdirs();
        }
        return generatedDir;
    }

    public File getGeneratedAdviceLeedsDir() {
        if (generatedAdviceLeedsDir == null) {
            generatedAdviceLeedsDir = new File(
                    getGeneratedDir(),
                    sAdviceLeeds);
            generatedAdviceLeedsDir.mkdirs();
        }
        return generatedAdviceLeedsDir;
    }

    public File getGeneratedCensusDir() {
        if (generatedCensusDir == null) {
            generatedCensusDir = new File(
                    getGeneratedDir(),
                    sCensus);
            generatedCensusDir.mkdirs();
        }
        return generatedCensusDir;
    }

    public File getGeneratedCensus2011Dir() {
        if (generatedCensus2011Dir == null) {
            generatedCensus2011Dir = new File(
                    getGeneratedCensusDir(),
                    s2011);
            generatedCensus2011Dir.mkdirs();
        }
        return generatedCensus2011Dir;
    }

    public File getGeneratedCensus2011Dir(
            String level) {
        return new File(getGeneratedCensus2011Dir(),
                level);
    }

    public File getGeneratedCensus2011LUTsDir() {
        if (generatedCensus2011LUTsDir == null) {
            generatedCensus2011LUTsDir = new File(
                    getGeneratedCensus2011Dir(),
                    sLUTs);
            generatedCensus2011LUTsDir.mkdirs();
        }
        return generatedCensus2011LUTsDir;
    }

    public File getGeneratedPostcodeDir() {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = new File(
                    getGeneratedDir(),
                    sPostcode);
            generatedPostcodeDir.mkdirs();
        }
        return generatedPostcodeDir;
    }

    public File getGeneratedCodePointDir() {
        if (generatedCodePointDir == null) {
            generatedCodePointDir = new File(
                    getGeneratedPostcodeDir(),
                    sBoundaryData);
            generatedCodePointDir = new File(
                    generatedCodePointDir,
                    sCodePoint);
            generatedCodePointDir.mkdirs();
        }
        return generatedCodePointDir;
    }

    public File getGeneratedONSPDDir() {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = new File(
                    getGeneratedPostcodeDir(),
                    sONSPD);
            generatedONSPDDir.mkdirs();
        }
        return generatedONSPDDir;
    }

    public File getGeneratedLCCDir() {
        if (generatedLCCDir == null) {
            generatedLCCDir = new File(
                    getGeneratedDir(),
                    sLCC);
            generatedLCCDir.mkdirs();
        }
        return generatedLCCDir;
    }

    public File getGeneratedSHBEDir() {
        if (generatedSHBEDir == null) {
            generatedSHBEDir = new File(
                    getGeneratedLCCDir(),
                    sSHBE);
            generatedSHBEDir.mkdirs();
        }
        return generatedSHBEDir;
    }

    public File getGeneratedSHBEDir(
            String dirname) {
        if (dirname == null) {
            return getGeneratedSHBEDir();
        }
        return new File(
                getGeneratedSHBEDir(),
                dirname);
    }

    public File getGeneratedSHBEDir(
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

    public File getUOFile(
            File f,
            boolean doUnderOccupied,
            boolean doCouncil) {
        File result;
        if (doUnderOccupied) {
            result = new File(
                    f,
                    tDW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil) {
                result = new File(
                        result,
                        tDW_Strings.sCouncil);
            } else {
                result = new File(
                        result,
                        tDW_Strings.sRSL);
            }
        } else {
            result = new File(
                    f,
                    tDW_Strings.sA);
        }
        return result;
    }

    public File getUOFile(
            File f,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL) {
        File result;
        if (doUnderOccupied) {
            result = new File(
                    f,
                    tDW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil & doRSL) {
                result = new File(
                        result,
                        tDW_Strings.sB);
            } else if (doCouncil) {
                result = new File(
                        result,
                        tDW_Strings.sCouncil);
            } else {
                result = new File(
                        result,
                        tDW_Strings.sRSL);
            }
        } else {
            result = new File(
                    f,
                    tDW_Strings.sA);
        }
        return result;
    }

    public ArrayList<File> getGeneratedSHBELevelDirsArrayList(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil) {
        ArrayList<File> result;
        result = new ArrayList<File>();
        File tGeneratedSHBEDir;
        tGeneratedSHBEDir = getGeneratedSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d = new File(
                    tGeneratedSHBEDir,
                    level);
            d = getUOFile(d, doUnderOccupied, doCouncil);
            result.add(d);
        }
        return result;
    }

    public TreeMap<String, File> getGeneratedSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil) {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        File tGeneratedSHBEDir;
        tGeneratedSHBEDir = getGeneratedSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d;
            d = new File(
                    tGeneratedSHBEDir,
                    level);
            d = getUOFile(d, doUnderOccupied, doCouncil);
            result.put(level, d);
        }
        return result;
    }

    public TreeMap<String, File> getGeneratedSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL) {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        File tGeneratedSHBEDir;
        tGeneratedSHBEDir = getGeneratedSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d = new File(
                    tGeneratedSHBEDir,
                    level);
            d = getUOFile(d, doUnderOccupied, doCouncil, doRSL);
            result.put(level, d);
        }
        return result;
    }

    public File getGeneratedUnderOccupiedDir() {
        if (generatedUnderOccupiedDir == null) {
            generatedUnderOccupiedDir = new File(
                    getGeneratedLCCDir(),
                    sUnderOccupied);
            generatedUnderOccupiedDir.mkdirs();
        }
        return generatedUnderOccupiedDir;
    }

    public File getOutputDir() {
        if (outputDir == null) {
            outputDir = new File(
                    getDigitalWelfareDir(),
                    sOutput);
            outputDir.mkdirs();
        }
        return outputDir;
    }

    public File getOutputAdviceLeedsDir() {
        if (outputAdviceLeedsDir == null) {
            outputAdviceLeedsDir = new File(
                    getOutputDir(),
                    sAdviceLeeds);
            outputAdviceLeedsDir.mkdirs();
        }
        return outputAdviceLeedsDir;
    }

    public File getOutputAdviceLeedsMapsDir() {
        if (outputAdviceLeedsMapsDir == null) {
            outputAdviceLeedsMapsDir = new File(
                    getOutputAdviceLeedsDir(),
                    sMaps);
            outputAdviceLeedsMapsDir.mkdirs();

        }
        return outputAdviceLeedsMapsDir;
    }

    public File getOutputAdviceLeedsTablesDir() {
        if (outputAdviceLeedsTablesDir == null) {
            outputAdviceLeedsTablesDir = new File(
                    getOutputAdviceLeedsDir(), sTables);
            outputAdviceLeedsTablesDir.mkdirs();
        }
        return outputAdviceLeedsTablesDir;
    }

    public File getOutputCensusDir() {
        if (outputCensusDir == null) {
            outputCensusDir = new File(
                    getOutputDir(),
                    sCensus);
            outputCensusDir.mkdirs();
        }
        return outputCensusDir;
    }

    public File getOutputCensus2011Dir() {
        if (outputCensus2011Dir == null) {
            outputCensus2011Dir = new File(
                    getOutputCensusDir(),
                    s2011);
            outputCensus2011Dir.mkdirs();
        }
        return outputCensus2011Dir;
    }

    public File getOutputCensus2011Dir(String level) {
        File result;
        result = new File(
                getOutputCensus2011Dir(),
                level);
        outputDir.mkdirs();
        return result;
    }

    public File getOutputLCCDir() {
        if (outputLCCDir == null) {
            outputLCCDir = new File(
                    getOutputDir(),
                    sLCC);
            outputLCCDir.mkdirs();
        }
        return outputLCCDir;
    }

    public File getOutputSHBEDir() {
        if (outputSHBEDir == null) {
            outputSHBEDir = new File(
                    getOutputLCCDir(),
                    sSHBE);
            outputSHBEDir.mkdirs();
        }
        return outputSHBEDir;
    }

    public File getOutputSHBEMapsDir() {
        if (outputSHBEMapsDir == null) {
            outputSHBEMapsDir = new File(
                    getOutputSHBEDir(),
                    sMaps);
            outputSHBEMapsDir.mkdirs();
        }
        return outputSHBEMapsDir;
    }

    public File getOutputSHBETablesDir() {
        if (outputSHBETablesDir == null) {
            outputSHBETablesDir = new File(
                    getOutputSHBEDir(), sTables);
            outputSHBETablesDir.mkdirs();
        }
        return outputSHBETablesDir;
    }

    public File getOutputSHBETablesTenancyTypeTransitionDir(
            String type,
            String paymentType,
            boolean checkPreviousTenancyType) {
        File result = new File(
                getOutputSHBETablesDir(),
                tDW_Strings.sTenancy);
        result = new File(
                result,
                paymentType);
        result = new File(
                result,
                type);
        result = new File(
                result,
                tDW_Strings.sTenancyTypeTransition);
        if (checkPreviousTenancyType) {
            result = new File(
                    result,
                    tDW_Strings.sCheckedPreviousTenancyType);
        } else {
            result = new File(
                    result,
                    tDW_Strings.sCheckedPreviousTenancyTypeNo);
        }
        return result;
    }

    public File getOutputSHBEPlotsDir() {
        if (outputSHBEPlotsDir == null) {
            outputSHBEPlotsDir = new File(
                    getOutputSHBEDir(),
                    sPlots);
            outputSHBEPlotsDir.mkdirs();
        }
        return outputSHBEPlotsDir;
    }

    public File getOutputSHBEPlotsTenancyTypeTransitionDir(
            String type,
            String paymentType,
            boolean checkPreviousTenancyType) {
        File result = new File(
                getOutputSHBEPlotsDir(),
                tDW_Strings.sTenancy);
        result = new File(
                result,
                paymentType);
        result = new File(
                result,
                type);
        result = new File(
                result,
                tDW_Strings.sTenancyTypeTransitionLineGraphs);
        if (checkPreviousTenancyType) {
            result = new File(
                    result,
                    tDW_Strings.sCheckedPreviousTenancyType);
        } else {
            result = new File(
                    result,
                    tDW_Strings.sCheckedPreviousTenancyTypeNo);
        }
        return result;
    }

    public File getOutputSHBELineMapsDir() {
        return new File(
                getOutputSHBEMapsDir(), sLine);
    }

    public File getOutputSHBEChoroplethDir() {
        return new File(
                getOutputSHBEMapsDir(),
                sChoropleth);
    }

    public File getOutputSHBEChoroplethDir(
            String level) {
        return new File(
                getOutputSHBEChoroplethDir(),
                level);
    }

    public File getOutputUnderOccupiedDir() {
        if (outputUnderOccupiedDir == null) {
            outputUnderOccupiedDir = new File(
                    getOutputLCCDir(),
                    sUnderOccupied);
            outputUnderOccupiedDir.mkdirs();
        }
        return outputUnderOccupiedDir;
    }

    /**
     * 
     * @param name
     * @param paymentType
     * @param includeKey
     * @param doUnderOccupancy
     * @return 
     */
    public File getTableDir(
            String name,
            String paymentType,
            String includeKey,
            boolean doUnderOccupancy) {
        File result;
        result = new File(
                getOutputSHBETablesDir(),
                name);
        if (doUnderOccupancy) {
            result = new File(result, tDW_Strings.sU);
        } else {
            result = new File(result, tDW_Strings.sA);
        }
        result = new File(result, paymentType);
        result = new File(result, includeKey);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }
}
