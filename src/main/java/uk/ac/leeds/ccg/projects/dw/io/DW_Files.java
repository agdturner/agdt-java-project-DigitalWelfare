package uk.ac.leeds.ccg.projects.dw.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;

/**
 * A basic convenience class that does not need a reference to the main
 * DW_Environment.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Files extends Generic_Files {

    /**
     * For storing the input AdviceLeeds data directory inside
     * <code>inputDir</code>.
     */
    private Path inputAdviceLeedsDir;

    /**
     * For storing the input Census data directory inside the main input data
     * directory.
     */
    private Path inputCensusDir;

    /**
     * For storing the 2011 Census directory inside the Census data directory.
     */
    private Path inputCensus2011Dir;

    /**
     * For storing the Postcode directory inside the main input data directory.
     */
    private Path inputPostcodeDir;

    /**
     * For storing the CodePoint directory inside the main input data directory.
     */
    private Path inputCodePointDir;
    private Path inputLCCDir;
    private Path inputSHBEDir;
    private Path inputUnderOccupiedDir;

    private Path swapDir;
    private Path swapLCCDir;
    private Path swapSHBEDir;

    //private Path generatedDir;
    private Path generatedAdviceLeedsDir;
    private Path generatedCensusDir;
    private Path generatedCensus2011Dir;
    private Path generatedCensus2011LUTsDir;
    private Path generatedGridsDir;
    private Path generatedGridsGridDoubleFactoryDir;
    private Path generatedPostcodeDir;
    private Path generatedCodePointDir;
    private Path generatedONSPDDir;
    private Path generatedLCCDir;
    private Path generatedSHBEDir;
    private Path generatedUnderOccupiedDir;

    //private Path outputDir;
    private Path outputAdviceLeedsDir;
    private Path outputAdviceLeedsMapsDir;
    private Path outputCensusDir;
    private Path outputCensus2011Dir;
    private Path outputLCCDir;
    private Path outputSHBEDir;
    private Path outputSHBELogsDir;
    private Path outputSHBEMapsDir;
    private Path outputSHBETablesDir;
    private Path outputSHBEPlotsDir;
    private Path outputAdviceLeedsTablesDir;
    private Path outputUnderOccupiedDir;

    public DW_Files(Path dataDir) throws IOException {
        super(new Generic_Defaults(dataDir));
    }

    public Path getInputAdviceLeedsDir() throws IOException {
        if (inputAdviceLeedsDir == null) {
            inputAdviceLeedsDir = Paths.get(getInputDir().toString(),
                    DW_Strings.sAdviceLeeds);
        }
        return inputAdviceLeedsDir;
    }

    public Path getInputCensusDir() throws IOException {
        if (inputCensusDir == null) {
            inputCensusDir = Paths.get(getInputDir().toString()
                    , DW_Strings.sCensus);
        }
        return inputCensusDir;
    }

    public Path getInputCensus2011Dir() throws IOException {
        if (inputCensus2011Dir == null) {
            inputCensus2011Dir = Paths.get(getInputCensusDir().toString(),
                    DW_Strings.s2011);
        }
        return inputCensus2011Dir;
    }

    public Path getInputCensus2011Dir(String level) throws IOException {
        return Paths.get(getInputCensus2011Dir().toString(), level);
    }

    public Path getInputCensus2011AttributeDataDir(String level) throws IOException {
        return Paths.get(getInputCensus2011Dir(level).toString(),
                DW_Strings.sAttributeData);
    }

    public Path getInputCensus2011BoundaryDataDir(String level) throws IOException {
        return Paths.get(getInputCensus2011Dir(level).toString(),
                DW_Strings.sBoundaryData);
    }

    public Path getInputCodePointDir(String year, String dirname) throws IOException {
        if (inputCodePointDir == null) {
            inputCodePointDir = Paths.get(getInputPostcodeDir().toString(),
                    DW_Strings.sBoundaryData);
            inputCodePointDir = Paths.get(inputCodePointDir.toString(),
                    DW_Strings.sCodePoint);
            inputCodePointDir = Paths.get(inputCodePointDir.toString(), year);//"2015");
            inputCodePointDir = Paths.get(inputCodePointDir.toString(), dirname);
        }
        return inputCodePointDir;
    }

    public Path getInputPostcodeDir() throws IOException {
        if (inputPostcodeDir == null) {
            inputPostcodeDir = Paths.get(getInputDir().toString(), DW_Strings.sPostcode);
        }
        return inputPostcodeDir;
    }

    public Path getInputLCCDir() throws IOException {
        if (inputLCCDir == null) {
            inputLCCDir = Paths.get(getInputDir().toString(), DW_Strings.sLCC);
        }
        return inputLCCDir;
    }

    public Path getInputSHBEDir() throws IOException {
        if (inputSHBEDir == null) {
            inputSHBEDir = Paths.get(getInputLCCDir().toString(), DW_Strings.sSHBE);
        }
        return inputSHBEDir;
    }

    public Path getInputUnderOccupiedDir() throws IOException {
        if (inputUnderOccupiedDir == null) {
            inputUnderOccupiedDir = Paths.get(getInputLCCDir().toString(),
                    DW_Strings.sUnderOccupied);
        }
        return inputUnderOccupiedDir;
    }

    public Path getSwapDir() throws IOException {
        if (swapDir == null) {
            swapDir = Paths.get(getGeneratedDir().toString(), DW_Strings.sSwap);
            Files.createDirectories(swapDir);
        }
        return swapDir;
    }

    public Path getSwapLCCDir() throws IOException {
        if (swapLCCDir == null) {
            swapLCCDir = Paths.get(getSwapDir().toString(), DW_Strings.sLCC);
            Files.createDirectories(swapLCCDir);
        }
        return swapLCCDir;
    }

    public Path getSwapSHBEDir() throws IOException {
        if (swapSHBEDir == null) {
            swapSHBEDir = Paths.get(getSwapLCCDir().toString(), DW_Strings.sSHBE);
            Files.createDirectories(swapSHBEDir);
        }
        return swapSHBEDir;
    }

    public Path getGeneratedAdviceLeedsDir() throws IOException {
        if (generatedAdviceLeedsDir == null) {
            generatedAdviceLeedsDir = Paths.get(getGeneratedDir().toString(),
                    DW_Strings.sAdviceLeeds);
            Files.createDirectories(generatedAdviceLeedsDir);
        }
        return generatedAdviceLeedsDir;
    }

    public Path getGeneratedGridsDir() throws IOException {
        if (generatedGridsDir == null) {
            generatedGridsDir = Paths.get(getGeneratedDir().toString(),
                    DW_Strings.sGrids);
            Files.createDirectories(generatedGridsDir);
        }
        return generatedGridsDir;
    }

    public Path getGeneratedGridsGridDoubleFactoryDir() throws IOException {
        if (generatedGridsGridDoubleFactoryDir == null) {
            generatedGridsGridDoubleFactoryDir = Paths.get(
                    getGeneratedGridsDir().toString(), DW_Strings.sGridDoubleFactory);
            Files.createDirectories(generatedGridsGridDoubleFactoryDir);
        }
        return generatedGridsGridDoubleFactoryDir;
    }

    public Path getGeneratedCensusDir() throws IOException {
        if (generatedCensusDir == null) {
            generatedCensusDir = Paths.get(getGeneratedDir().toString(), DW_Strings.sCensus);
            Files.createDirectories(generatedCensusDir);
        }
        return generatedCensusDir;
    }

    public Path getGeneratedCensus2011Dir() throws IOException {
        if (generatedCensus2011Dir == null) {
            generatedCensus2011Dir = Paths.get(getGeneratedCensusDir().toString(),
                    DW_Strings.s2011);
            Files.createDirectories(generatedCensus2011Dir);
        }
        return generatedCensus2011Dir;
    }

    public Path getGeneratedCensus2011Dir(String level) throws IOException {
        return Paths.get(getGeneratedCensus2011Dir().toString(), level);
    }

    public Path getGeneratedCensus2011LUTsDir() throws IOException {
        if (generatedCensus2011LUTsDir == null) {
            generatedCensus2011LUTsDir = Paths.get(getGeneratedCensus2011Dir().toString(),
                    DW_Strings.sLUTs);
            Files.createDirectories(generatedCensus2011LUTsDir);
        }
        return generatedCensus2011LUTsDir;
    }

    public Path getGeneratedPostcodeDir() throws IOException {
        if (generatedPostcodeDir == null) {
            generatedPostcodeDir = Paths.get(getGeneratedDir().toString(),
                    DW_Strings.sPostcode);
            Files.createDirectories(generatedPostcodeDir);
        }
        return generatedPostcodeDir;
    }

    public Path getGeneratedCodePointDir() throws IOException {
        if (generatedCodePointDir == null) {
            generatedCodePointDir = Paths.get(getGeneratedPostcodeDir().toString(),
                    DW_Strings.sBoundaryData);
            generatedCodePointDir = Paths.get(generatedCodePointDir.toString(),
                    DW_Strings.sCodePoint);
            Files.createDirectories(generatedCodePointDir);
        }
        return generatedCodePointDir;
    }

    public Path getGeneratedONSPDDir() throws IOException {
        if (generatedONSPDDir == null) {
            generatedONSPDDir = Paths.get(getGeneratedPostcodeDir().toString(),
                    DW_Strings.S_ONSPD);
            Files.createDirectories(generatedONSPDDir);
        }
        return generatedONSPDDir;
    }

    public Path getGeneratedLCCDir() throws IOException {
        if (generatedLCCDir == null) {
            generatedLCCDir = Paths.get(getGeneratedDir().toString(), DW_Strings.sLCC);
            Files.createDirectories(generatedLCCDir);
        }
        return generatedLCCDir;
    }

    public Path getGeneratedSHBEDir() throws IOException {
        if (generatedSHBEDir == null) {
            generatedSHBEDir = Paths.get(getGeneratedLCCDir().toString(), DW_Strings.sSHBE);
            Files.createDirectories(generatedSHBEDir);
        }
        return generatedSHBEDir;
    }

    public Path getGeneratedSHBEDir(String dirname) throws IOException {
        if (dirname == null) {
            return getGeneratedSHBEDir();
        }
        return Paths.get(getGeneratedSHBEDir().toString(), dirname);
    }

    public Path getGeneratedSHBEDir(String level, boolean doUnderOccupied,
            boolean doCouncil) throws IOException {
        Path r = Paths.get(getGeneratedSHBEDir().toString(), level);
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
    public Path getUODir(Path dir, boolean doUnderOccupied, boolean doCouncil) {
        Path r;
        if (doUnderOccupied) {
            r = Paths.get(dir.toString(), DW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil) {
                r = Paths.get(r.toString(), DW_Strings.sCouncil);
            } else {
                r = Paths.get(r.toString(), DW_Strings.sRSL);
            }
        } else {
            r = Paths.get(dir.toString(), DW_Strings.s_A);
        }
        return r;
    }

    public Path getUOFile(Path f, boolean doUnderOccupied, boolean doCouncil,
            boolean doRSL) {
        Path r;
        if (doUnderOccupied) {
            r = Paths.get(f.toString(), DW_Strings.sU);
            //UnderOccupiedString);
            if (doCouncil & doRSL) {
                r = Paths.get(r.toString(), DW_Strings.s_B);
            } else if (doCouncil) {
                r = Paths.get(r.toString(), DW_Strings.sCouncil);
            } else {
                r = Paths.get(r.toString(), DW_Strings.sRSL);
            }
        } else {
            r = Paths.get(f.toString(), DW_Strings.s_A);
        }
        return r;
    }

    public ArrayList<Path> getOutputSHBELevelDirsArrayList(
            ArrayList<Integer> levels, boolean doUnderOccupied,
            boolean doCouncil) throws IOException {
        ArrayList<Path> r = new ArrayList<>();
        Path dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            Path d = Paths.get(dir0.toString(), ite.next().toString());
            d = getUODir(d, doUnderOccupied, doCouncil);
            r.add(d);
        }
        return r;
    }

    public TreeMap<Integer, Path> getOutputSHBELevelDirsTreeMap(
            ArrayList<Integer> levels, boolean doUnderOccupied, 
            boolean doCouncil) throws IOException {
        TreeMap<Integer, Path> r = new TreeMap<>();
        Path dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            Integer l = ite.next();
            Path d = Paths.get(dir0.toString(), l.toString());
            d = getUODir(d, doUnderOccupied, doCouncil);
            r.put(l, d);
        }
        return r;
    }

    public TreeMap<Integer, Path> getOutputSHBELevelDirsTreeMap(
            ArrayList<Integer> levels, boolean doUnderOccupied,
            boolean doCouncil, boolean doRSL) throws IOException {
        TreeMap<Integer, Path> r = new TreeMap<>();
        Path dir0 = getOutputSHBEDir();
        Iterator<Integer> ite = levels.iterator();
        while (ite.hasNext()) {
            Integer l = ite.next();
            Path d = Paths.get(dir0.toString(), l.toString());
            d = getUOFile(d, doUnderOccupied, doCouncil, doRSL);
            r.put(l, d);
        }
        return r;
    }

    public Path getGeneratedUnderOccupiedDir() throws IOException {
        if (generatedUnderOccupiedDir == null) {
            generatedUnderOccupiedDir = Paths.get(getGeneratedLCCDir().toString(),
                    DW_Strings.sUnderOccupied);
            Files.createDirectories(generatedUnderOccupiedDir);
        }
        return generatedUnderOccupiedDir;
    }

    public Path getOutputAdviceLeedsDir() throws IOException {
        if (outputAdviceLeedsDir == null) {
            outputAdviceLeedsDir = Paths.get(getOutputDir().toString(),
                    DW_Strings.sAdviceLeeds);
            Files.createDirectories(outputAdviceLeedsDir);
        }
        return outputAdviceLeedsDir;
    }

    public Path getOutputAdviceLeedsMapsDir() throws IOException {
        if (outputAdviceLeedsMapsDir == null) {
            outputAdviceLeedsMapsDir = Paths.get(getOutputAdviceLeedsDir().toString(),
                    DW_Strings.sMaps);
            Files.createDirectories(outputAdviceLeedsMapsDir);

        }
        return outputAdviceLeedsMapsDir;
    }

    public Path getOutputAdviceLeedsTablesDir() throws IOException {
        if (outputAdviceLeedsTablesDir == null) {
            outputAdviceLeedsTablesDir = Paths.get(
                    getOutputAdviceLeedsDir().toString(), DW_Strings.sTables);
            Files.createDirectories(outputAdviceLeedsTablesDir);
        }
        return outputAdviceLeedsTablesDir;
    }

    public Path getOutputCensusDir() throws IOException {
        if (outputCensusDir == null) {
            outputCensusDir = Paths.get(getOutputDir().toString(), DW_Strings.sCensus);
            Files.createDirectories(outputCensusDir);
        }
        return outputCensusDir;
    }

    public Path getOutputCensus2011Dir() throws IOException {
        if (outputCensus2011Dir == null) {
            outputCensus2011Dir = Paths.get(getOutputCensusDir().toString(), DW_Strings.s2011);
            Files.createDirectories(outputCensus2011Dir);
        }
        return outputCensus2011Dir;
    }

    public Path getOutputCensus2011Dir(String level) throws IOException {
        Path r = Paths.get(getOutputCensus2011Dir().toString(), level);
        Files.createDirectories(r);
        return r;
    }

    public Path getOutputLCCDir() throws IOException {
        if (outputLCCDir == null) {
            outputLCCDir = Paths.get(getOutputDir().toString(), DW_Strings.sLCC);
            Files.createDirectories(outputLCCDir);
        }
        return outputLCCDir;
    }

    public Path getOutputSHBEDir() throws IOException {
        if (outputSHBEDir == null) {
            outputSHBEDir = Paths.get(getOutputLCCDir().toString(), DW_Strings.sSHBE);
            Files.createDirectories(outputSHBEDir);
        }
        return outputSHBEDir;
    }

    public Path getOutputSHBELogsDir() throws IOException {
        if (outputSHBELogsDir == null) {
            outputSHBELogsDir = Paths.get(getOutputSHBEDir().toString(), DW_Strings.sLogs);
            Files.createDirectories(outputSHBELogsDir);
        }
        return outputSHBELogsDir;
    }

    public Path getOutputSHBEMapsDir() throws IOException {
        if (outputSHBEMapsDir == null) {
            outputSHBEMapsDir = Paths.get(getOutputSHBEDir().toString(), DW_Strings.sMaps);
            Files.createDirectories(outputSHBEMapsDir);
        }
        return outputSHBEMapsDir;
    }

    public Path getOutputSHBETablesDir() throws IOException {
        if (outputSHBETablesDir == null) {
            outputSHBETablesDir = Paths.get(getOutputSHBEDir().toString(), DW_Strings.sTables);
            Files.createDirectories(outputSHBETablesDir);
        }
        return outputSHBETablesDir;
    }

    public Path getOutputSHBETablesTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) throws IOException {
        Path r = getOutputDataDir(getOutputSHBETablesDir(),
                DW_Strings.sTenancy,
                //type,
                DW_Strings.sTenancyTypeTransition,
                checkPreviousTenancyType);
        return r;
    }

    public Path getOutputSHBEPlotsDir() throws IOException {
        if (outputSHBEPlotsDir == null) {
            outputSHBEPlotsDir = Paths.get(getOutputSHBEDir().toString(),
                    DW_Strings.sPlots);
            Files.createDirectories(outputSHBEPlotsDir);
        }
        return outputSHBEPlotsDir;
    }

    public Path getOutputSHBEPlotsTenancyTypeTransitionDir(
            //String type,
            boolean checkPreviousTenancyType) throws IOException {
        Path r = getOutputDataDir(getOutputSHBEPlotsDir(),
                DW_Strings.sTenancy,
                //type,
                DW_Strings.sTenancyTypeTransitionLineGraph,
                checkPreviousTenancyType);
        return r;
    }

    public Path getOutputDataDir(Path baseDir, String dir1, String dir2,
            boolean checkPreviousTenancyType) {
        Path r = Paths.get(baseDir.toString(), dir1);
        r = Paths.get(r.toString(), dir2);
        if (checkPreviousTenancyType) {
            r = Paths.get(r.toString(), DW_Strings.sCheckedPreviousTenancyType);
        } else {
            r = Paths.get(r.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
        }
        return r;
    }

    public Path getOutputSHBELineMapsDir() throws IOException {
        return Paths.get(getOutputSHBEMapsDir().toString(), DW_Strings.sLine);
    }

    public Path getOutputSHBEChoroplethDir() throws IOException {
        return Paths.get(getOutputSHBEMapsDir().toString(), DW_Strings.sChoropleth);
    }

    public Path getOutputSHBEChoroplethDir(String level) throws IOException {
        return Paths.get(getOutputSHBEChoroplethDir().toString(), level);
    }

    public Path getOutputUnderOccupiedDir() throws IOException {
        if (outputUnderOccupiedDir == null) {
            outputUnderOccupiedDir = Paths.get(getOutputLCCDir().toString(),
                    DW_Strings.sUnderOccupied);
            Files.createDirectories(outputUnderOccupiedDir);
        }
        return outputUnderOccupiedDir;
    }

    /**
     *
     * @param name
     * @param includeKey
     * @param doUnderOccupancy
     * @return
     * @throws java.io.IOException
     */
    public Path getOutputSHBETableDir(String name, String includeKey,
            boolean doUnderOccupancy) throws IOException {
        Path r = DW_Files.this.getOutputSHBETableDir(name, doUnderOccupancy);
        r = Paths.get(r.toString(), includeKey);
        if (!Files.exists(r)) {
            Files.createDirectories(r);
        }
        return r;
    }

    /**
     *
     * @param name
     * @param doUnderOccupancy
     * @return
     * @throws java.io.IOException
     */
    public Path getOutputSHBETableDir(String name, boolean doUnderOccupancy) 
    throws IOException {
        Path r = Paths.get(getOutputSHBETablesDir().toString(), name);
        if (doUnderOccupancy) {
            r = Paths.get(r.toString(), DW_Strings.sU);
        } else {
            r = Paths.get(r.toString(), DW_Strings.s_A);
        }
        if (!Files.exists(r)) {
            Files.createDirectories(r);
        }
        return r;
    }

    public String getDefaultBinaryFileExtension() {
        return DW_Strings.sBinaryFileExtension;
    }

}
