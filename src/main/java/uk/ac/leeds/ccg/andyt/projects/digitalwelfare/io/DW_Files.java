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

    private static File _DW_dir;
    private static File _DW_inputDir;
    private static File _DW_inputAdviceLeedsDir;
    private static File _DW_inputCensusDir;
    private static File _DW_inputCensus2011Dir;
    private static File _DW_inputPostcodeDir;
    private static File _DW_inputCodePointDir;
    private static File _DW_inputONSPDDir;
    private static File _DW_inputLCCDir;
    private static File _DW_inputSHBEDir;
    private static File _DW_inputUnderOccupiedDir;

    private static File _DW_swapDir;
    private static File _DW_swapLCCDir;
    private static File _DW_swapSHBEDir;

    private static File _DW_generatedDir;
    private static File _DW_generatedAdviceLeedsDir;
    private static File _DW_generatedCensusDir;
    private static File _DW_generatedCensus2011Dir;
    private static File _DW_generatedCensus2011LUTsDir;
    private static File _DW_generatedPostcodeDir;
    private static File _DW_generatedCodePointDir;
    private static File _DW_generatedONSPDDir;
    private static File _DW_generatedLCCDir;
    private static File _DW_generatedSHBEDir;
    private static File _DW_generatedUnderOccupiedDir;

    private static File _DW_outputDir;
    private static File _DW_outputAdviceLeedsDir;
    private static File _DW_outputAdviceLeedsMapsDir;
    private static File _DW_outputCensusDir;
    private static File _DW_outputCensus2011Dir;
    private static File _DW_outputLCCDir;
    private static File _DW_outputSHBEDir;
    private static File _DW_outputSHBEMapsDir;
    private static File _DW_outputSHBETablesDir;
    private static File _DW_outputSHBEPlotsDir;
    private static File _DW_outputAdviceLeedsTablesDir;
    private static File _DW_outputUnderOccupiedDir;

    public static File getDigitalWelfareDir() {
        if (_DW_dir == null) {
            _DW_dir = new File("/scratch02/DigitalWelfare");
        }
        return _DW_dir;
    }

    public static File getInputDir() {
        if (_DW_inputDir == null) {
            _DW_inputDir = new File(
                    getDigitalWelfareDir(),
                    "Input");
        }
        return _DW_inputDir;
    }

    public static File getInputAdviceLeedsDir() {
        if (_DW_inputAdviceLeedsDir == null) {
            _DW_inputAdviceLeedsDir = new File(
                    getInputDir(),
                    "AdviceLeeds");
        }
        return _DW_inputAdviceLeedsDir;
    }

    public static File getInputCensusDir() {
        if (_DW_inputCensusDir == null) {
            _DW_inputCensusDir = new File(
                    getInputDir(),
                    "Census");
        }
        return _DW_inputCensusDir;
    }

    public static File getInputCensus2011Dir() {
        if (_DW_inputCensus2011Dir == null) {
            _DW_inputCensus2011Dir = new File(
                    getInputCensusDir(),
                    "2011");
        }
        return _DW_inputCensus2011Dir;
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
                "BoundaryData");
    }

    public static File getInputCodePointDir(
            String year,
            String dirname) {
        if (_DW_inputCodePointDir == null) {
            _DW_inputCodePointDir = new File(
                    getInputPostcodeDir(),
                    "BoundaryData");
            _DW_inputCodePointDir = new File(
                    _DW_inputCodePointDir,
                    "CodePoint");
            _DW_inputCodePointDir = new File(
                    _DW_inputCodePointDir,
                    year);//"2015");
            _DW_inputCodePointDir = new File(
                    _DW_inputCodePointDir,
                    dirname);
        }
        return _DW_inputCodePointDir;
    }

    public static File getInputPostcodeDir() {
        if (_DW_inputPostcodeDir == null) {
            _DW_inputPostcodeDir = new File(getInputDir(),
                    "Postcode");
        }
        return _DW_inputPostcodeDir;
    }

    public static File getInputONSPDDir() {
        if (_DW_inputONSPDDir == null) {
            _DW_inputONSPDDir = new File(
                    getInputPostcodeDir(),
                    "ONSPD");
        }
        return _DW_inputONSPDDir;
    }

    public static File getInputLCCDir() {
        if (_DW_inputLCCDir == null) {
            _DW_inputLCCDir = new File(
                    getInputDir(),
                    "LCC");
        }
        return _DW_inputLCCDir;
    }

    public static File getInputSHBEDir() {
        if (_DW_inputSHBEDir == null) {
            _DW_inputSHBEDir = new File(
                    getInputLCCDir(),
                    "SHBE");
        }
        return _DW_inputSHBEDir;
    }

    public static File getInputUnderOccupiedDir() {
        if (_DW_inputUnderOccupiedDir == null) {
            _DW_inputUnderOccupiedDir = new File(
                    getInputLCCDir(),
                    "UnderOccupied");
        }
        return _DW_inputUnderOccupiedDir;
    }

    public static File getSwapDir() {
        if (_DW_swapDir == null) {
            _DW_swapDir = new File(
                    getDigitalWelfareDir(),
                    "Swap");
            _DW_swapDir.mkdirs();
        }
        return _DW_swapDir;
    }

    public static File getSwapLCCDir() {
        if (_DW_swapLCCDir == null) {
            _DW_swapLCCDir = new File(
                    getSwapDir(),
                    "LCC");
            _DW_swapLCCDir.mkdirs();
        }
        return _DW_swapLCCDir;
    }

    public static File getSwapSHBEDir() {
        if (_DW_swapSHBEDir == null) {
            _DW_swapSHBEDir = new File(
                    getSwapLCCDir(),
                    "SHBE");
            _DW_swapSHBEDir.mkdirs();
        }
        return _DW_swapSHBEDir;
    }

    public static File getGeneratedDir() {
        if (_DW_generatedDir == null) {
            _DW_generatedDir = new File(
                    getDigitalWelfareDir(),
                    "Generated");
            _DW_generatedDir.mkdirs();
        }
        return _DW_generatedDir;
    }

    public static File getGeneratedAdviceLeedsDir() {
        if (_DW_generatedAdviceLeedsDir == null) {
            _DW_generatedAdviceLeedsDir = new File(
                    getGeneratedDir(),
                    "AdviceLeeds");
            _DW_generatedAdviceLeedsDir.mkdirs();
        }
        return _DW_generatedAdviceLeedsDir;
    }

    public static File getGeneratedCensusDir() {
        if (_DW_generatedCensusDir == null) {
            _DW_generatedCensusDir = new File(
                    getGeneratedDir(),
                    "Census");
            _DW_generatedCensusDir.mkdirs();
        }
        return _DW_generatedCensusDir;
    }

    public static File getGeneratedCensus2011Dir() {
        if (_DW_generatedCensus2011Dir == null) {
            _DW_generatedCensus2011Dir = new File(
                    getGeneratedCensusDir(),
                    "2011");
            _DW_generatedCensus2011Dir.mkdirs();
        }
        return _DW_generatedCensus2011Dir;
    }

    public static File getGeneratedCensus2011Dir(
            String level) {
        return new File(getGeneratedCensus2011Dir(),
                level);
    }

    public static File getGeneratedCensus2011LUTsDir() {
        if (_DW_generatedCensus2011LUTsDir == null) {
            _DW_generatedCensus2011LUTsDir = new File(
                    getGeneratedCensus2011Dir(),
                    "LUTs");
            _DW_generatedCensus2011LUTsDir.mkdirs();
        }
        return _DW_generatedCensus2011LUTsDir;
    }

    public static File getGeneratedPostcodeDir() {
        if (_DW_generatedPostcodeDir == null) {
            _DW_generatedPostcodeDir = new File(
                    getGeneratedDir(),
                    "Postcode");
            _DW_generatedPostcodeDir.mkdirs();
        }
        return _DW_generatedPostcodeDir;
    }

    public static File getGeneratedCodePointDir() {
        if (_DW_generatedCodePointDir == null) {
            _DW_generatedCodePointDir = new File(
                    getGeneratedPostcodeDir(),
                    "BoundaryData");
            _DW_generatedCodePointDir = new File(
                    _DW_generatedCodePointDir,
                    "CodePoint");
            _DW_generatedCodePointDir.mkdirs();
        }
        return _DW_generatedCodePointDir;
    }

    public static File getGeneratedONSPDDir() {
        if (_DW_generatedONSPDDir == null) {
            _DW_generatedONSPDDir = new File(
                    getGeneratedPostcodeDir(),
                    "ONSPD");
            _DW_generatedONSPDDir.mkdirs();
        }
        return _DW_generatedONSPDDir;
    }

    public static File getGeneratedLCCDir() {
        if (_DW_generatedLCCDir == null) {
            _DW_generatedLCCDir = new File(
                    getGeneratedDir(),
                    "LCC");
            _DW_generatedLCCDir.mkdirs();
        }
        return _DW_generatedLCCDir;
    }

    public static File getGeneratedSHBEDir() {
        if (_DW_generatedSHBEDir == null) {
            _DW_generatedSHBEDir = new File(
                    getGeneratedLCCDir(),
                    "SHBE");
            _DW_generatedSHBEDir.mkdirs();
        }
        return _DW_generatedSHBEDir;
    }

    public static File getGeneratedSHBEDir(
            String level) {
        return new File(
                getGeneratedSHBEDir(),
                level);
    }

    public static ArrayList<File> getGeneratedSHBELevelDirsArrayList(
            ArrayList<String> levels) {
        ArrayList<File> result;
        result = new ArrayList<File>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File outputDir = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    level);
            result.add(outputDir);
        }
        return result;
    }

    public static TreeMap<String, File> getGeneratedSHBELevelDirsTreeMap(
            ArrayList<String> levels) {
        TreeMap<String, File> result;
        result = new TreeMap<String, File>();
        Iterator<String> ite;
        ite = levels.iterator();
        while (ite.hasNext()) {
            String level = ite.next();
            File outputDir = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    level);
            result.put(level, outputDir);
        }
        return result;
    }

    public static File getGeneratedUnderOccupiedDir() {
        if (_DW_generatedUnderOccupiedDir == null) {
            _DW_generatedUnderOccupiedDir = new File(
                    getGeneratedLCCDir(),
                    "UnderOccupied");
            _DW_generatedUnderOccupiedDir.mkdirs();
        }
        return _DW_generatedUnderOccupiedDir;
    }

    public static File getOutputDir() {
        if (_DW_outputDir == null) {
            _DW_outputDir = new File(
                    getDigitalWelfareDir(),
                    "Output");
            _DW_outputDir.mkdirs();
        }
        return _DW_outputDir;
    }

    public static File getOutputAdviceLeedsDir() {
        if (_DW_outputAdviceLeedsDir == null) {
            _DW_outputAdviceLeedsDir = new File(
                    getOutputDir(),
                    "AdviceLeeds");
            _DW_outputAdviceLeedsDir.mkdirs();
        }
        return _DW_outputAdviceLeedsDir;
    }

    public static File getOutputAdviceLeedsMapsDir() {
        if (_DW_outputAdviceLeedsMapsDir == null) {
            _DW_outputAdviceLeedsMapsDir = new File(
                    getOutputAdviceLeedsDir(),
                    "Maps");
            _DW_outputAdviceLeedsMapsDir.mkdirs();

        }
        return _DW_outputAdviceLeedsMapsDir;
    }

    public static File getOutputAdviceLeedsTablesDir() {
        if (_DW_outputAdviceLeedsTablesDir == null) {
            _DW_outputAdviceLeedsTablesDir = new File(
                    getOutputAdviceLeedsDir(),
                    "Tables");
            _DW_outputAdviceLeedsTablesDir.mkdirs();
        }
        return _DW_outputAdviceLeedsTablesDir;
    }

    public static File getOutputCensusDir() {
        if (_DW_outputCensusDir == null) {
            _DW_outputCensusDir = new File(
                    getOutputDir(),
                    "Census");
            _DW_outputCensusDir.mkdirs();
        }
        return _DW_outputCensusDir;
    }

    public static File getOutputCensus2011Dir() {
        if (_DW_outputCensus2011Dir == null) {
            _DW_outputCensus2011Dir = new File(
                    getOutputCensusDir(),
                    "2011");
            _DW_outputCensus2011Dir.mkdirs();
        }
        return _DW_outputCensus2011Dir;
    }

    public static File getOutputCensus2011Dir(String level) {
        File result;
        result = new File(
                getOutputCensus2011Dir(),
                level);
        _DW_outputDir.mkdirs();
        return result;
    }

    public static File getOutputLCCDir() {
        if (_DW_outputLCCDir == null) {
            _DW_outputLCCDir = new File(
                    getOutputDir(),
                    "LCC");
            _DW_outputLCCDir.mkdirs();
        }
        return _DW_outputLCCDir;
    }

    public static File getOutputSHBEDir() {
        if (_DW_outputSHBEDir == null) {
            _DW_outputSHBEDir = new File(
                    getOutputLCCDir(),
                    "SHBE");
            _DW_outputSHBEDir.mkdirs();
        }
        return _DW_outputSHBEDir;
    }

    public static File getOutputSHBEMapsDir() {
        if (_DW_outputSHBEMapsDir == null) {
            _DW_outputSHBEMapsDir = new File(
                    getOutputSHBEDir(),
                    "Maps");
            _DW_outputSHBEMapsDir.mkdirs();
        }
        return _DW_outputSHBEMapsDir;
    }

    public static File getOutputSHBETablesDir() {
        if (_DW_outputSHBETablesDir == null) {
            _DW_outputSHBETablesDir = new File(
                    getOutputSHBEDir(),
                    "Tables");
            _DW_outputSHBETablesDir.mkdirs();
        }
        return _DW_outputSHBETablesDir;
    }

    public static File getOutputSHBETablesTenancyTypeTransitionDir(
            String type,
            boolean checkPreviousTenure) {
        File result = new File(
                DW_Files.getOutputSHBETablesDir(),
                "Tenancy");
        result = new File(
                result,
                type);
        result = new File(
                result,
                "TenancyTypeTransition");
        if (checkPreviousTenure) {
            result = new File(
                    result,
                    "CheckedPreviousTenure");
        } else {
            result = new File(
                    result,
                    "NotCheckedPreviousTenure");
        }
        return result;
    }

    public static File getOutputSHBEPlotsDir() {
        if (_DW_outputSHBEPlotsDir == null) {
            _DW_outputSHBEPlotsDir = new File(
                    getOutputSHBEDir(),
                    "Plots");
            _DW_outputSHBEPlotsDir.mkdirs();
        }
        return _DW_outputSHBEPlotsDir;
    }

    public static File getOutputSHBEPlotsTenancyTypeTransitionDir(
            String type,
            boolean checkPreviousTenure,
            String type2) {
        File result = new File(
                DW_Files.getOutputSHBEPlotsDir(),
                "Tenancy");
        result = new File(
                result,
                type);
        result = new File(
                result,
                "TenancyTypeTransitionLineGraphs");
        if (checkPreviousTenure) {
            result = new File(
                    result,
                    "CheckedPreviousTenure");
        } else {
            result = new File(
                    result,
                    "NotCheckedPreviousTenure");
        }
        result = new File(
                result,
                type2);
        return result;
    }

    public static File getOutputSHBELineMapsDir() {
        return new File(
                getOutputSHBEMapsDir(),
                "Line");
    }
    
    public static File getOutputSHBEChoroplethDir() {
        return new File(
                getOutputSHBEMapsDir(),
                "Choropleth");
    }

    public static File getOutputSHBEChoroplethDir(
            String level) {
        return new File(
                getOutputSHBEChoroplethDir(),
                level);
    }

    public static File getOutputUnderOccupiedDir() {
        if (_DW_outputUnderOccupiedDir == null) {
            _DW_outputUnderOccupiedDir = new File(
                    getOutputLCCDir(),
                    "UnderOccupied");
            _DW_outputUnderOccupiedDir.mkdirs();
        }
        return _DW_outputUnderOccupiedDir;
    }

}
