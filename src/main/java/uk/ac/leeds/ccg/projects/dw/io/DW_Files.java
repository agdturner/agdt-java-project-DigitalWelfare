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
package uk.ac.leeds.ccg.projects.dw.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_Files;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;

/**
 * A basic convenience class that does not need a reference to the main
 * DW_Environment.
 *
 * @author geoagdt
 */
public class DW_Files extends Generic_Files {

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

    //private File generatedDir;
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

    //private File outputDir;
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

    public DW_Files(File dataDir) throws IOException {
        super(dataDir);
    }

    public File getInputAdviceLeedsDir() {
        if (inputAdviceLeedsDir == null) {
            inputAdviceLeedsDir = new File(getInputDir(), DW_Strings.sAdviceLeeds);
        }
        return inputAdviceLeedsDir;
    }

    public File getInputCensusDir() {
        if (inputCensusDir == null) {
            inputCensusDir = new File(getInputDir(), DW_Strings.sCensus);
        }
        return inputCensusDir;
    }

    public File getInputCensus2011Dir() {
        if (inputCensus2011Dir == null) {
            inputCensus2011Dir = new File(getInputCensusDir(), DW_Strings.s2011);
        }
        return inputCensus2011Dir;
    }

    public File getInputCensus2011Dir(Integer level) {
        return new File(getInputCensus2011Dir(), level.toString());
    }

    public File getInputCensus2011AttributeDataDir(Integer level) {
        return new File(getInputCensus2011Dir(level), DW_Strings.sAttributeData);
    }

    public File getInputCensus2011BoundaryDataDir(Integer level) {
        return new File(getInputCensus2011Dir(level), DW_Strings.sBoundaryData);
    }

    public File getInputCodePointDir(String year, String dirname) {
        if (inputCodePointDir == null) {
            inputCodePointDir = new File(getInputPostcodeDir(),
                    DW_Strings.sBoundaryData);
            inputCodePointDir = new File(inputCodePointDir,
                    DW_Strings.sCodePoint);
            inputCodePointDir = new File(inputCodePointDir, year);//"2015");
            inputCodePointDir = new File(inputCodePointDir, dirname);
        }
        return inputCodePointDir;
    }

    public File getInputPostcodeDir() {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = new File(getInputDir(), DW_Strings.sPostcode);
        }
        return inputPostcodeDir;
    }

    public File getInputLCCDir() {
        if (inputLCCDir == null) {
            inputLCCDir = new File(getInputDir(), DW_Strings.sLCC);
        }
        return inputLCCDir;
    }

    public File getInputSHBEDir() {
        if (inputSHBEDir == null) {
            inputSHBEDir = new File(getInputLCCDir(), DW_Strings.sSHBE);
        }
        return inputSHBEDir;
    }

    public File getInputUnderOccupiedDir() {
        if (inputUnderOccupiedDir == null) {
            inputUnderOccupiedDir = new File(getInputLCCDir(),
                    DW_Strings.sUnderOccupied);
        }
        return inputUnderOccupiedDir;
    }

    public File getSwapDir() {
        if (swapDir == null) {
            swapDir = new File(getGeneratedDir(), DW_Strings.sSwap);
            swapDir.mkdirs();
        }
        return swapDir;
    }

    public File getSwapLCCDir() {
        if (swapLCCDir == null) {
            swapLCCDir = new File(getSwapDir(), DW_Strings.sLCC);
            swapLCCDir.mkdirs();
        }
        return swapLCCDir;
    }

    public File getSwapSHBEDir() {
        if (swapSHBEDir == null) {
            swapSHBEDir = new File(getSwapLCCDir(), DW_Strings.sSHBE);
            swapSHBEDir.mkdirs();
        }
        return swapSHBEDir;
    }

    public File getGeneratedAdviceLeedsDir() {
        if (generatedAdviceLeedsDir == null) {
            generatedAdviceLeedsDir = new File(getGeneratedDir(),
                    DW_Strings.sAdviceLeeds);
            generatedAdviceLeedsDir.mkdirs();
        }
        return generatedAdviceLeedsDir;
    }

    public File getGeneratedGridsDir() {
        if (generatedGridsDir == null) {
            generatedGridsDir = new File(getGeneratedDir(),
                    DW_Strings.sGrids);
            generatedGridsDir.mkdirs();
        }
        return generatedGridsDir;
    }

    public File getGeneratedGridsGridDoubleFactoryDir() {
        if (generatedGridsGridDoubleFactoryDir == null) {
            generatedGridsGridDoubleFactoryDir = new File(
                    getGeneratedGridsDir(), DW_Strings.sGridDoubleFactory);
            generatedGridsGridDoubleFactoryDir.mkdirs();
        }
        return generatedGridsGridDoubleFactoryDir;
    }

    public File getGeneratedCensusDir() {
        if (generatedCensusDir == null) {
            generatedCensusDir = new File(getGeneratedDir(), DW_Strings.sCensus);
            generatedCensusDir.mkdirs();
        }
        return generatedCensusDir;
    }

    public File getGeneratedCensus2011Dir() {
        if (generatedCensus2011Dir == null) {
            generatedCensus2011Dir = new File(getGeneratedCensusDir(),
                    DW_Strings.s2011);
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
                    DW_Strings.sLUTs);
            generatedCensus2011LUTsDir.mkdirs();
        }
        return generatedCensus2011LUTsDir;
    }

    public File getGeneratedPostcodeDir() {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = new File(getGeneratedDir(),
                    DW_Strings.sPostcode);
            generatedPostcodeDir.mkdirs();
        }
        return generatedPostcodeDir;
    }

    public File getGeneratedCodePointDir() {
        if (generatedCodePointDir == null) {
            generatedCodePointDir = new File(getGeneratedPostcodeDir(),
                    DW_Strings.sBoundaryData);
            generatedCodePointDir = new File(generatedCodePointDir,
                    DW_Strings.sCodePoint);
            generatedCodePointDir.mkdirs();
        }
        return generatedCodePointDir;
    }

    public File getGeneratedONSPDDir() {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = new File(getGeneratedPostcodeDir(),
                    DW_Strings.S_ONSPD);
            generatedONSPDDir.mkdirs();
        }
        return generatedONSPDDir;
    }

    public File getGeneratedLCCDir() {
        if (generatedLCCDir == null) {
            generatedLCCDir = new File(getGeneratedDir(), DW_Strings.sLCC);
            generatedLCCDir.mkdirs();
        }
        return generatedLCCDir;
    }

    public File getGeneratedSHBEDir() {
        if (generatedSHBEDir == null) {
            generatedSHBEDir = new File(getGeneratedLCCDir(), DW_Strings.sSHBE);
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
        File r = new File(getGeneratedSHBEDir(), level);
        r = getUODir(r, doUnderOccupied, doCouncil);
        return r;
    }

    /**
     *
     * @param dir
     * @param doUnderOccupied
     * @param doCouncil
     * @return
     */
    public File getUODir(File dir, boolean doUnderOccupied, boolean doCouncil) {
        File r;
        if (doUnderOccupied) {
            r = new File(dir, DW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil) {
                r = new File(r, DW_Strings.sCouncil);
            } else {
                r = new File(r, DW_Strings.sRSL);
            }
        } else {
            r = new File(dir, DW_Strings.s_A);
        }
        return r;
    }

    public File getUOFile(File f, boolean doUnderOccupied, boolean doCouncil,
            boolean doRSL) {
        File r;
        if (doUnderOccupied) {
            r = new File(f, DW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil & doRSL) {
                r = new File(r, DW_Strings.s_B);
            } else if (doCouncil) {
                r = new File(r, DW_Strings.sCouncil);
            } else {
                r = new File(r, DW_Strings.sRSL);
            }
        } else {
            r = new File(f, DW_Strings.s_A);
        }
        return r;
    }

    public ArrayList<File> getOutputSHBELevelDirsArrayList(
            ArrayList<Integer> levels, boolean doUnderOccupied,
            boolean doCouncil) {
        ArrayList<File> r = new ArrayList<>();
        File dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            File d = new File(dir0, ite.next().toString());
            d = getUODir(d, doUnderOccupied, doCouncil);
            r.add(d);
        }
        return r;
    }

    public TreeMap<Integer, File> getOutputSHBELevelDirsTreeMap(
            ArrayList<Integer> levels, boolean doUnderOccupied, boolean doCouncil) {
        TreeMap<Integer, File> r = new TreeMap<>();
        File dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            Integer l = ite.next();
            File d = new File(dir0, l.toString());
            d = getUODir(d, doUnderOccupied, doCouncil);
            r.put(l, d);
        }
        return r;
    }

    public TreeMap<Integer, File> getOutputSHBELevelDirsTreeMap(
            ArrayList<Integer> levels, boolean doUnderOccupied,
            boolean doCouncil, boolean doRSL) {
        TreeMap<Integer, File> r = new TreeMap<>();
        File dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            Integer l = ite.next();
            File d = new File(dir0, l.toString());
            d = getUOFile(d, doUnderOccupied, doCouncil, doRSL);
            r.put(l, d);
        }
        return r;
    }

    public File getGeneratedUnderOccupiedDir() {
        if (generatedUnderOccupiedDir == null) {
            generatedUnderOccupiedDir = new File(getGeneratedLCCDir(),
                    DW_Strings.sUnderOccupied);
            generatedUnderOccupiedDir.mkdirs();
        }
        return generatedUnderOccupiedDir;
    }

    public File getOutputAdviceLeedsDir() {
        if (outputAdviceLeedsDir == null) {
            outputAdviceLeedsDir = new File(getOutputDir(),
                    DW_Strings.sAdviceLeeds);
            outputAdviceLeedsDir.mkdirs();
        }
        return outputAdviceLeedsDir;
    }

    public File getOutputAdviceLeedsMapsDir() {
        if (outputAdviceLeedsMapsDir == null) {
            outputAdviceLeedsMapsDir = new File(getOutputAdviceLeedsDir(),
                    DW_Strings.sMaps);
            outputAdviceLeedsMapsDir.mkdirs();

        }
        return outputAdviceLeedsMapsDir;
    }

    public File getOutputAdviceLeedsTablesDir() {
        if (outputAdviceLeedsTablesDir == null) {
            outputAdviceLeedsTablesDir = new File(
                    getOutputAdviceLeedsDir(), DW_Strings.sTables);
            outputAdviceLeedsTablesDir.mkdirs();
        }
        return outputAdviceLeedsTablesDir;
    }

    public File getOutputCensusDir() {
        if (outputCensusDir == null) {
            outputCensusDir = new File(getOutputDir(), DW_Strings.sCensus);
            outputCensusDir.mkdirs();
        }
        return outputCensusDir;
    }

    public File getOutputCensus2011Dir() {
        if (outputCensus2011Dir == null) {
            outputCensus2011Dir = new File(getOutputCensusDir(), DW_Strings.s2011);
            outputCensus2011Dir.mkdirs();
        }
        return outputCensus2011Dir;
    }

    public File getOutputCensus2011Dir(String level) {
        File r = new File(getOutputCensus2011Dir(), level);
        r.mkdirs();
        return r;
    }

    public File getOutputLCCDir() {
        if (outputLCCDir == null) {
            outputLCCDir = new File(getOutputDir(), DW_Strings.sLCC);
            outputLCCDir.mkdirs();
        }
        return outputLCCDir;
    }

    public File getOutputSHBEDir() {
        if (outputSHBEDir == null) {
            outputSHBEDir = new File(getOutputLCCDir(), DW_Strings.sSHBE);
            outputSHBEDir.mkdirs();
        }
        return outputSHBEDir;
    }

    public File getOutputSHBELogsDir() {
        if (outputSHBELogsDir == null) {
            outputSHBELogsDir = new File(getOutputSHBEDir(), DW_Strings.sLogs);
            outputSHBELogsDir.mkdirs();
        }
        return outputSHBELogsDir;
    }

    public File getOutputSHBEMapsDir() {
        if (outputSHBEMapsDir == null) {
            outputSHBEMapsDir = new File(getOutputSHBEDir(), DW_Strings.sMaps);
            outputSHBEMapsDir.mkdirs();
        }
        return outputSHBEMapsDir;
    }

    public File getOutputSHBETablesDir() {
        if (outputSHBETablesDir == null) {
            outputSHBETablesDir = new File(getOutputSHBEDir(), DW_Strings.sTables);
            outputSHBETablesDir.mkdirs();
        }
        return outputSHBETablesDir;
    }

    public File getOutputSHBETablesTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) {
        File r = getOutputDataDir(getOutputSHBETablesDir(),
                DW_Strings.sTenancy,
                //type,
                DW_Strings.sTenancyTypeTransition,
                checkPreviousTenancyType);
        return r;
    }

    public File getOutputSHBEPlotsDir() {
        if (outputSHBEPlotsDir == null) {
            outputSHBEPlotsDir = new File(getOutputSHBEDir(), DW_Strings.sPlots);
            outputSHBEPlotsDir.mkdirs();
        }
        return outputSHBEPlotsDir;
    }

    public File getOutputSHBEPlotsTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) {
        File r = getOutputDataDir(getOutputSHBEPlotsDir(),
                DW_Strings.sTenancy,
                //type,
                DW_Strings.sTenancyTypeTransitionLineGraph,
                checkPreviousTenancyType);
        return r;
    }

    public File getOutputDataDir(File baseDir, String dir1, String dir2,
            boolean checkPreviousTenancyType) {
        File r = new File(baseDir, dir1);
        r = new File(r, dir2);
        if (checkPreviousTenancyType) {
            r = new File(r, DW_Strings.sCheckedPreviousTenancyType);
        } else {
            r = new File(r, DW_Strings.sCheckedPreviousTenancyTypeNo);
        }
        return r;
    }

    public File getOutputSHBELineMapsDir() {
        return new File(getOutputSHBEMapsDir(), DW_Strings.sLine);
    }

    public File getOutputSHBEChoroplethDir() {
        return new File(getOutputSHBEMapsDir(), DW_Strings.sChoropleth);
    }

    public File getOutputSHBEChoroplethDir(String level) {
        return new File(getOutputSHBEChoroplethDir(), level);
    }

    public File getOutputUnderOccupiedDir() {
        if (outputUnderOccupiedDir == null) {
            outputUnderOccupiedDir = new File(getOutputLCCDir(),
                    DW_Strings.sUnderOccupied);
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
        File r = DW_Files.this.getOutputSHBETableDir(name, doUnderOccupancy);
        r = new File(r, includeKey);
        if (!r.exists()) {
            r.mkdirs();
        }
        return r;
    }

    /**
     *
     * @param name
     * @param doUnderOccupancy
     * @return
     */
    public File getOutputSHBETableDir(String name, boolean doUnderOccupancy) {
        File r;
        r = new File(getOutputSHBETablesDir(), name);
        if (doUnderOccupancy) {
            r = new File(r, DW_Strings.sU);
        } else {
            r = new File(r, DW_Strings.s_A);
        }
        if (!r.exists()) {
            r.mkdirs();
        }
        return r;
    }

    public String getDefaultBinaryFileExtension() {
        return DW_Strings.sBinaryFileExtension;
    }

}
