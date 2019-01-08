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
import uk.ac.leeds.ccg.andyt.generic.data.onspd.io.ONSPD_Files;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.io.SHBE_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;

/**
 * A basic convenience class that does not need a reference to the main
 * DW_Environment.
 *
 * @author geoagdt
 */
public class DW_Files extends DW_Object {

    public ONSPD_Files ONSPD_Files;
    public SHBE_Files SHBE_Files;

    /**
     * Provided for convenience. This is a reference to Env.DW_Strings.
     */
    protected DW_Strings Strings;

    /**
     * For storing the main directory location where the project files are
     * stored. This is initialised from DW_Environment.Directory.
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
    private File generatedGridsDir;
    private File generatedGridsGridDoubleFactoryDir;
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
    private File outputSHBELogsDir;
    private File outputSHBEMapsDir;
    private File outputSHBETablesDir;
    private File outputSHBEPlotsDir;
    private File outputAdviceLeedsTablesDir;
    private File outputUnderOccupiedDir;

    public DW_Files() {
    }

    public DW_Files(DW_Environment env) {
        super(env);
        if (env != null) {
            this.Strings = env.getStrings();
        } else {
            this.Strings = new DW_Strings();
        }
    }

    public File getDigitalWelfareDir() {
        if (dir == null) {
            dir = new File(Env.Directory);
        }
        return dir;
    }

    public File getInputDir() {
        if (inputDir == null) {
            inputDir = new File(getDigitalWelfareDir(), Strings.sInput);
        }
        return inputDir;
    }

    public File getInputAdviceLeedsDir() {
        if (inputAdviceLeedsDir == null) {
            inputAdviceLeedsDir = new File(getInputDir(), Strings.sAdviceLeeds);
        }
        return inputAdviceLeedsDir;
    }

    public File getInputCensusDir() {
        if (inputCensusDir == null) {
            inputCensusDir = new File(getInputDir(), Strings.sCensus);
        }
        return inputCensusDir;
    }

    public File getInputCensus2011Dir() {
        if (inputCensus2011Dir == null) {
            inputCensus2011Dir = new File(getInputCensusDir(), Strings.s2011);
        }
        return inputCensus2011Dir;
    }

    public File getInputCensus2011Dir(String level) {
        return new File(getInputCensus2011Dir(), level);
    }

    public File getInputCensus2011AttributeDataDir(String level) {
        return new File(getInputCensus2011Dir(level), Strings.sAttributeData);
    }

    public File getInputCensus2011BoundaryDataDir(String level) {
        return new File(getInputCensus2011Dir(level), Strings.sBoundaryData);
    }

    public File getInputCodePointDir(String year, String dirname) {
        if (inputCodePointDir == null) {
            inputCodePointDir = new File(getInputPostcodeDir(), 
                    Strings.sBoundaryData);
            inputCodePointDir = new File(inputCodePointDir, 
                    Strings.sCodePoint);
            inputCodePointDir = new File(inputCodePointDir, year);//"2015");
            inputCodePointDir = new File(inputCodePointDir, dirname);
        }
        return inputCodePointDir;
    }

    public File getInputPostcodeDir() {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = new File(getInputDir(), Strings.sPostcode);
        }
        return inputPostcodeDir;
    }

    public File getInputLCCDir() {
        if (inputLCCDir == null) {
            inputLCCDir = new File(getInputDir(), Strings.sLCC);
        }
        return inputLCCDir;
    }

    public File getInputSHBEDir() {
        if (inputSHBEDir == null) {
            inputSHBEDir = new File(getInputLCCDir(), Strings.sSHBE);
        }
        return inputSHBEDir;
    }

    public File getInputUnderOccupiedDir() {
        if (inputUnderOccupiedDir == null) {
            inputUnderOccupiedDir = new File(getInputLCCDir(), 
                    Strings.sUnderOccupied);
        }
        return inputUnderOccupiedDir;
    }

    public File getSwapDir() {
        if (swapDir == null) {
            swapDir = new File(getDigitalWelfareDir(), Strings.sSwap);
            swapDir.mkdirs();
        }
        return swapDir;
    }

    public File getSwapLCCDir() {
        if (swapLCCDir == null) {
            swapLCCDir = new File(getSwapDir(), Strings.sLCC);
            swapLCCDir.mkdirs();
        }
        return swapLCCDir;
    }

    public File getSwapSHBEDir() {
        if (swapSHBEDir == null) {
            swapSHBEDir = new File(getSwapLCCDir(), Strings.sSHBE);
            swapSHBEDir.mkdirs();
        }
        return swapSHBEDir;
    }

    public File getGeneratedDir() {
        if (generatedDir == null) {
            generatedDir = new File(getDigitalWelfareDir(), Strings.sGenerated);
            generatedDir.mkdirs();
        }
        return generatedDir;
    }

    public File getGeneratedAdviceLeedsDir() {
        if (generatedAdviceLeedsDir == null) {
            generatedAdviceLeedsDir = new File(getGeneratedDir(), 
                    Strings.sAdviceLeeds);
            generatedAdviceLeedsDir.mkdirs();
        }
        return generatedAdviceLeedsDir;
    }

    public File getGeneratedGridsDir() {
        if (generatedGridsDir == null) {
            generatedGridsDir = new File(getGeneratedDir(), Strings.sGrids);
            generatedGridsDir.mkdirs();
        }
        return generatedGridsDir;
    }

    public File getGeneratedGridsGridDoubleFactoryDir() {
        if (generatedGridsGridDoubleFactoryDir == null) {
            generatedGridsGridDoubleFactoryDir = new File(
                    getGeneratedGridsDir(), Strings.sGridDoubleFactory);
            generatedGridsGridDoubleFactoryDir.mkdirs();
        }
        return generatedGridsGridDoubleFactoryDir;
    }

    public File getGeneratedCensusDir() {
        if (generatedCensusDir == null) {
            generatedCensusDir = new File(getGeneratedDir(), Strings.sCensus);
            generatedCensusDir.mkdirs();
        }
        return generatedCensusDir;
    }

    public File getGeneratedCensus2011Dir() {
        if (generatedCensus2011Dir == null) {
            generatedCensus2011Dir = new File(getGeneratedCensusDir(),
                    Strings.s2011);
            generatedCensus2011Dir.mkdirs();
        }
        return generatedCensus2011Dir;
    }

    public File getGeneratedCensus2011Dir(String level) {
        return new File(getGeneratedCensus2011Dir(), level);
    }

    public File getGeneratedCensus2011LUTsDir() {
        if (generatedCensus2011LUTsDir == null) {
            generatedCensus2011LUTsDir = new File(getGeneratedCensus2011Dir(), 
                    Strings.sLUTs);
            generatedCensus2011LUTsDir.mkdirs();
        }
        return generatedCensus2011LUTsDir;
    }

    public File getGeneratedPostcodeDir() {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = new File(getGeneratedDir(), 
                    Strings.sPostcode);
            generatedPostcodeDir.mkdirs();
        }
        return generatedPostcodeDir;
    }

    public File getGeneratedCodePointDir() {
        if (generatedCodePointDir == null) {
            generatedCodePointDir = new File(getGeneratedPostcodeDir(), 
                    Strings.sBoundaryData);
            generatedCodePointDir = new File(generatedCodePointDir, 
                    Strings.sCodePoint);
            generatedCodePointDir.mkdirs();
        }
        return generatedCodePointDir;
    }

    public File getGeneratedONSPDDir() {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = new File(getGeneratedPostcodeDir(), 
                    Strings.S_ONSPD);
            generatedONSPDDir.mkdirs();
        }
        return generatedONSPDDir;
    }

    public File getGeneratedLCCDir() {
        if (generatedLCCDir == null) {
            generatedLCCDir = new File(getGeneratedDir(), Strings.sLCC);
            generatedLCCDir.mkdirs();
        }
        return generatedLCCDir;
    }

    public File getGeneratedSHBEDir() {
        if (generatedSHBEDir == null) {
            generatedSHBEDir = new File(getGeneratedLCCDir(), Strings.sSHBE);
            generatedSHBEDir.mkdirs();
        }
        return generatedSHBEDir;
    }

    public File getGeneratedSHBEDir(String dirname) {
        if (dirname == null) {
            return getGeneratedSHBEDir();
        }
        return new File(getGeneratedSHBEDir(), dirname);
    }

    public File getGeneratedSHBEDir(String level, boolean doUnderOccupied, 
            boolean doCouncil) {
        File result;
        result = new File(getGeneratedSHBEDir(), level);
        result = getUODir(result, doUnderOccupied, doCouncil);
        return result;
    }

    /**
     *
     * @param dir
     * @param doUnderOccupied
     * @param doCouncil
     * @return
     */
    public File getUODir(File dir, boolean doUnderOccupied, boolean doCouncil) {
        File result;
        if (doUnderOccupied) {
            result = new File(dir, Strings.sU);
            //UnderOccupiedString);
            if (doCouncil) {
                result = new File(result, Strings.sCouncil);
            } else {
                result = new File(result, Strings.sRSL);
            }
        } else {
            result = new File(dir, Strings.sA);
        }
        return result;
    }

    public File getUOFile(File f, boolean doUnderOccupied, boolean doCouncil, 
            boolean doRSL) {
        File result;
        if (doUnderOccupied) {
            result = new File(f, Strings.sU);
            //UnderOccupiedString);
            if (doCouncil & doRSL) {
                result = new File(result, Strings.sB);
            } else if (doCouncil) {
                result = new File(result, Strings.sCouncil);
            } else {
                result = new File(result, Strings.sRSL);
            }
        } else {
            result = new File(f, Strings.sA);
        }
        return result;
    }

    public ArrayList<File> getOutputSHBELevelDirsArrayList(
            ArrayList<String> levels, boolean doUnderOccupied, 
            boolean doCouncil) {
        ArrayList<File> result;
        result = new ArrayList<>();
        File dir0;
        dir0 = getOutputSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d = new File(dir0, level);
            d = getUODir(d, doUnderOccupied, doCouncil);
            result.add(d);
        }
        return result;
    }

    public TreeMap<String, File> getOutputSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil) {
        TreeMap<String, File> result;
        result = new TreeMap<>();
        File dir0;
        dir0 = getOutputSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d;
            d = new File(dir0, level);
            d = getUODir(d, doUnderOccupied, doCouncil);
            result.put(level, d);
        }
        return result;
    }

    public TreeMap<String, File> getOutputSHBELevelDirsTreeMap(
            ArrayList<String> levels,
            boolean doUnderOccupied,
            boolean doCouncil,
            boolean doRSL) {
        TreeMap<String, File> result;
        result = new TreeMap<>();
        File dir0;
        dir0 = getOutputSHBEDir();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File d = new File(dir0, level);
            d = getUOFile(d, doUnderOccupied, doCouncil, doRSL);
            result.put(level, d);
        }
        return result;
    }

    public File getGeneratedUnderOccupiedDir() {
        if (generatedUnderOccupiedDir == null) {
            generatedUnderOccupiedDir = new File(getGeneratedLCCDir(),
                    Strings.sUnderOccupied);
            generatedUnderOccupiedDir.mkdirs();
        }
        return generatedUnderOccupiedDir;
    }

    public File getOutputDir() {
        if (outputDir == null) {
            outputDir = new File(getDigitalWelfareDir(), Strings.sOutput);
            outputDir.mkdirs();
        }
        return outputDir;
    }

    public File getOutputAdviceLeedsDir() {
        if (outputAdviceLeedsDir == null) {
            outputAdviceLeedsDir = new File(getOutputDir(), 
                    Strings.sAdviceLeeds);
            outputAdviceLeedsDir.mkdirs();
        }
        return outputAdviceLeedsDir;
    }

    public File getOutputAdviceLeedsMapsDir() {
        if (outputAdviceLeedsMapsDir == null) {
            outputAdviceLeedsMapsDir = new File(getOutputAdviceLeedsDir(), 
                    Strings.sMaps);
            outputAdviceLeedsMapsDir.mkdirs();

        }
        return outputAdviceLeedsMapsDir;
    }

    public File getOutputAdviceLeedsTablesDir() {
        if (outputAdviceLeedsTablesDir == null) {
            outputAdviceLeedsTablesDir = new File(
                    getOutputAdviceLeedsDir(), Strings.sTables);
            outputAdviceLeedsTablesDir.mkdirs();
        }
        return outputAdviceLeedsTablesDir;
    }

    public File getOutputCensusDir() {
        if (outputCensusDir == null) {
            outputCensusDir = new File(getOutputDir(), Strings.sCensus);
            outputCensusDir.mkdirs();
        }
        return outputCensusDir;
    }

    public File getOutputCensus2011Dir() {
        if (outputCensus2011Dir == null) {
            outputCensus2011Dir = new File(getOutputCensusDir(), Strings.s2011);
            outputCensus2011Dir.mkdirs();
        }
        return outputCensus2011Dir;
    }

    public File getOutputCensus2011Dir(String level) {
        File result;
        result = new File(getOutputCensus2011Dir(), level);
        outputDir.mkdirs();
        return result;
    }

    public File getOutputLCCDir() {
        if (outputLCCDir == null) {
            outputLCCDir = new File(getOutputDir(), Strings.sLCC);
            outputLCCDir.mkdirs();
        }
        return outputLCCDir;
    }

    public File getOutputSHBEDir() {
        if (outputSHBEDir == null) {
            outputSHBEDir = new File(getOutputLCCDir(), Strings.sSHBE);
            outputSHBEDir.mkdirs();
        }
        return outputSHBEDir;
    }

    public File getOutputSHBELogsDir() {
        if (outputSHBELogsDir == null) {
            outputSHBELogsDir = new File(getOutputSHBEDir(), Strings.sLogs);
            outputSHBELogsDir.mkdirs();
        }
        return outputSHBELogsDir;
    }

    public File getOutputSHBEMapsDir() {
        if (outputSHBEMapsDir == null) {
            outputSHBEMapsDir = new File(getOutputSHBEDir(), Strings.sMaps);
            outputSHBEMapsDir.mkdirs();
        }
        return outputSHBEMapsDir;
    }

    public File getOutputSHBETablesDir() {
        if (outputSHBETablesDir == null) {
            outputSHBETablesDir = new File(getOutputSHBEDir(), Strings.sTables);
            outputSHBETablesDir.mkdirs();
        }
        return outputSHBETablesDir;
    }

    public File getOutputSHBETablesTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) {
        File result;
        result = getOutputDir(getOutputSHBETablesDir(),
                Strings.sTenancy,
                //type,
                Strings.sTenancyTypeTransition,
                checkPreviousTenancyType);
        return result;
    }

    public File getOutputSHBEPlotsDir() {
        if (outputSHBEPlotsDir == null) {
            outputSHBEPlotsDir = new File(getOutputSHBEDir(), Strings.sPlots);
            outputSHBEPlotsDir.mkdirs();
        }
        return outputSHBEPlotsDir;
    }

    public File getOutputSHBEPlotsTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) {
        File result;
        result = getOutputDir(getOutputSHBEPlotsDir(),
                Strings.sTenancy,
                //type,
                Strings.sTenancyTypeTransitionLineGraph,
                checkPreviousTenancyType);
        return result;
    }

    public File getOutputDir(
            File baseDir,
            String dir1,
            String dir2,
            boolean checkPreviousTenancyType) {
        File result;
        result = new File(baseDir, dir1);
        result = new File(result, dir2);
        if (checkPreviousTenancyType) {
            result = new File(result, Strings.sCheckedPreviousTenancyType);
        } else {
            result = new File(result, Strings.sCheckedPreviousTenancyTypeNo);
        }
        return result;
    }

    public File getOutputSHBELineMapsDir() {
        return new File(getOutputSHBEMapsDir(), Strings.sLine);
    }

    public File getOutputSHBEChoroplethDir() {
        return new File(getOutputSHBEMapsDir(), Strings.sChoropleth);
    }

    public File getOutputSHBEChoroplethDir(String level) {
        return new File(getOutputSHBEChoroplethDir(), level);
    }

    public File getOutputUnderOccupiedDir() {
        if (outputUnderOccupiedDir == null) {
            outputUnderOccupiedDir = new File(getOutputLCCDir(), 
                    Strings.sUnderOccupied);
            outputUnderOccupiedDir.mkdirs();
        }
        return outputUnderOccupiedDir;
    }

    /**
     *
     * @param name
     * @param includeKey
     * @param doUnderOccupancy
     * @return
     */
    public File getOutputSHBETableDir(String name, String includeKey, 
            boolean doUnderOccupancy) {
        File result;
        result = DW_Files.this.getOutputSHBETableDir(name, doUnderOccupancy);
        result = new File(result, includeKey);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    /**
     *
     * @param name
     * @param doUnderOccupancy
     * @return
     */
    public File getOutputSHBETableDir(String name, boolean doUnderOccupancy) {
        File result;
        result = new File(getOutputSHBETablesDir(), name);
        if (doUnderOccupancy) {
            result = new File(result, Strings.sU);
        } else {
            result = new File(result, Strings.sA);
        }
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    public String getDefaultBinaryFileExtension() {
        return Strings.sBinaryFileExtension;
    }
}
