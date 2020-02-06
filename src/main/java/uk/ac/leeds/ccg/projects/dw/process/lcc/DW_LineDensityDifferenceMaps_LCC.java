package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import uk.ac.leeds.ccg.generic.io.Generic_Path;
import uk.ac.leeds.ccg.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.grids.d2.Grids_Dimensions;
import uk.ac.leeds.ccg.grids.d2.grid.Grids_GridNumber;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.grids.io.Grids_Files;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.process.DW_DensityMapsAbstract;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_AreaCodesAndShapefiles;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getYM3s;
////import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.maps.initONSPDLookups;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;
//import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_LineMapsLCC.getAllTenancyTypeChanges;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_LineDensityDifferenceMaps_LCC extends DW_DensityMapsAbstract {

    protected DW_LineMapsLCC LineMaps_LCC;

    private static final String targetPropertyNameLSOA = "LSOA11CD";
    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<Geotools_Shapefile> midgrounds;
    protected ArrayList<Geotools_Shapefile> foregrounds;

    public DW_LineDensityDifferenceMaps_LCC(DW_Environment env) throws IOException {
        super(env);
        LineMaps_LCC = new DW_LineMapsLCC(env);
    }

    //DW_StyleParameters styleParameters;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new DW_LineDensityDifferenceMaps_LCC(null).run();
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
        }
    }

    public void run() throws Exception, Error {
//        // If showMapsInJMapPane is true, the maps are presented in individual 
//        // JMapPanes
//        showMapsInJMapPane = false;
//        //showMapsInJMapPane = true;
//        //outputESRIAsciigrids = false;
//        imageWidth = 1000;
//        initONSPDLookups();
//        // Initialise styleParameters
//        /*
//         * YlOrRd,PRGn,PuOr,RdGy,Spectral,Grays,PuBuGn,RdPu,BuPu,YlOrBr,Greens,
//         * BuGn,Accents,GnBu,PuRd,Purples,RdYlGn,Paired,Blues,RdBu,Oranges,
//         * RdYlBu,PuBu,OrRd,Set3,Set2,Set1,Reds,PiYG,Dark2,YlGn,BrBG,YlGnBu,
//         * Pastel2,Pastel1
//         */
////        ColorBrewer brewer = ColorBrewer.instance();
////        //String[] paletteNames = brewer.getPaletteNames(0, nClasses);
////        String[] paletteNames = brewer.getPaletteNames();
////        for (int i = 0; i < paletteNames.length; i++) {
////            System.out.println(paletteNames[i]);
////        }
//        styleParameters = new Geotools_StyleParameters();
//        styleParameters.setnClasses(5);
//        styleParameters.setPaletteName("Reds");
//        styleParameters.setPaletteName2("Blues");
//        styleParameters.setAddWhiteForZero(true);
////        styleParameters.setForegroundStyleTitle0("Foreground Style 0");
////        styleParameters.setForegroundStyles(DW_Style.createDefaultPointStyle());
////        styleParameters.setForegroundStyles(DW_Style.createAdviceLeedsPointStyles());
//        styleParameters.setForegroundStyle1(DW_Style.createDefaultPolygonStyle(
//                Color.GREEN,
//                Color.WHITE));
//        styleParameters.setForegroundStyleTitle1("Foreground Style 1");
//
//        mapDirectory = Paths.get(
//                DW_Files.getOutputAdviceLeedsMapsDir(),
//                "density");
//        imageWidth = 1000;
//
////        foregroundDW_Shapefile0 = getAdviceLeedsPointDW_Shapefiles();
//        init();
//
//        /*Grid Parameters
//         *_____________________________________________________________________
//         */
//        handleOutOfMemoryErrors = true;
//        Path processorDir = Paths.get(
//                mapDirectory,
//                "processor");
//        processorDir.mkdirs();
//        ge = new Grids_Environment();
//        eage = new Grids_ESRIAsciiGridExporter();
//        ie = new ImageExporter(ge);
//        gp = new Grid2DSquareCellProcessorGWS(ge);
//        gp.setDirectory(processorDir, false, handleOutOfMemoryErrors);
//        gcf = new Grid2DSquareCellDoubleChunkArrayFactory();
//        chunkNRows = 300;//250; //64
//        chunkNCols = 350;//300; //64
//        gf = new Grids_GridDoubleFactory(
//                processorDir,
//                chunkNRows,
//                chunkNCols,
//                gcf,
//                -9999d,
//                ge,
//                handleOutOfMemoryErrors);
////        // Jenks runs
////        styleParameters.setClassificationFunctionName("Jenks");
////        commonStyling = true;
////        individualStyling = true;
////        runAll(IDType);
////        // Quantile runs
////        styleParameters.setClassificationFunctionName("Quantile");
////        styleParameters.setStylesNull();
////        commonStyling = true;
////        individualStyling = true;
////        runAll(tDW_ID_ClientTypes);
//        // Equal Interval runs
//        styleParameters.setClassificationFunctionName("EqualInterval");
//        styleParameters.setStylesNull();
////        commonStyling = true;
//        individualStyling = true;
//
//        //int resolutionMultiplier = 4;
//        cellsize = 50;
//        mcdg = 4; //8;
//        runAll();
////        for (mcdg = 4; mcdg <= 32; mcdg *= 2) {
////            cellsize = (1600 / mcdg) / resolutionMultiplier;
////            runAll(resolutionMultiplier);
////        }
    }

    //public void runAll(int resolutionMultiplier) {
    public void runAll() throws Exception, Error {
//        TreeMap<String, ArrayList<Integer>> includes;
//        includes = env.getDW_SHBE_Handler().getIncludes();
//        includes.remove("All");
////        includes.remove("Yearly");
////        includes.remove("6Monthly");
////        includes.remove("3Monthly");
////        includes.remove("MonthlyUO");
////        includes.remove("Monthly");
//
//        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChanges;
//        allTenancyTypeChanges = DW_LineMaps_LCC.getAllTenancyTypeChanges();
//
//        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesGrouped;
//        allTenancyTypeChangesGrouped = DW_LineMaps_LCC.getAllTenancyTypeChangesGrouped();
//
//        Iterator<String> includesIte;
//        includesIte = includes.keySet().iterator();
//        while (includesIte.hasNext()) {
//            String includeName;
//            includeName = includesIte.next();
//            ArrayList<Integer> include;
//            include = includes.get(includeName);
//
//            TreeMap<String, ArrayList<String>> yearMonths;
//            yearMonths = DW_LineMaps_LCC.getYM3s(include);
//
//            ArrayList<Boolean> b;
//            b = new ArrayList<Boolean>();
//            b.add(true);
//            b.add(false);
//
//            boolean scaleToFirst;
//
//            Iterator<Boolean> iteb;
//            iteb = b.iterator();
//            while (iteb.hasNext()) {
//                boolean doUnderOccupancyData;
//                doUnderOccupancyData = iteb.next();
//                if (doUnderOccupancyData) {
//                    Iterator<Boolean> iteb2;
//                    iteb2 = b.iterator();
//                    while (iteb2.hasNext()) {
//                        boolean doCouncil;
//                        doCouncil = iteb2.next();
//                        if (commonStyling) {
//                            scaleToFirst = true;
//                            postcodeMaps(
//                                    //resolutionMultiplier,
//                                    yearMonths,
//                                    includeName,
//                                    include,
//                                    allTenancyTypeChanges.get(doUnderOccupancyData),
//                                    doUnderOccupancyData,
//                                    doCouncil,
//                                    scaleToFirst);
//                        }
//                        if (individualStyling) {
//                            scaleToFirst = false;
//                            postcodeMaps(
//                                    //resolutionMultiplier,
//                                    yearMonths,
//                                    includeName,
//                                    include,
//                                    allTenancyTypeChanges.get(doUnderOccupancyData),
//                                    doUnderOccupancyData,
//                                    doCouncil,
//                                    scaleToFirst);
//                        }
//                    }
//                } else {
//                    if (commonStyling) {
//                        scaleToFirst = true;
//                        postcodeMaps(
//                                //resolutionMultiplier,
//                                yearMonths,
//                                includeName,
//                                include,
//                                allTenancyTypeChanges.get(doUnderOccupancyData),
//                                doUnderOccupancyData,
//                                false,
//                                scaleToFirst);
//                    }
//                    if (individualStyling) {
//                        scaleToFirst = false;
//                        postcodeMaps(
//                                //resolutionMultiplier,
//                                yearMonths,
//                                includeName,
//                                include,
//                                allTenancyTypeChanges.get(doUnderOccupancyData),
//                                doUnderOccupancyData,
//                                false,
//                                scaleToFirst);
//                    }
//                }
//            }
//        }
//        // Tidy up
//        if (!showMapsInJMapPane) {
////            Iterator<AGDT_Shapefile> ite1;
////            ite1 = midgrounds.iterator();
////            while (ite1.hasNext()) {
////                Geotools_Shapefile s = ite1.next();
////                s.dispose();
////            }
//            Iterator<AGDT_Shapefile> ite;
//            ite = foregrounds.iterator();
//            while (ite.hasNext()) {
//                ite.next().dispose();
////                tLSOACodesAndLeedsLSOAShapefile.dispose();
////            tMSOACodesAndLeedsMSOAShapefile.dispose();
//            }
//        }
    }

    private void init() throws IOException {
        //initStyleParameters();
//        maps.setMapDirectory(DW_Files.getOutputSHBELineMapsDir());

        foregrounds = new ArrayList<>();
        //midgrounds = new ArrayList<AGDT_Shapefile>();
//        backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                env, "LSOA", targetPropertyNameLSOA,
                maps.getShapefileDataStoreFactory());
        foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        foregroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

//    private void initStyleParameters() {
//        styleParameters = new DW_StyleParameters();
//        styleParameters.setForegroundStyle(
//                DW_Style.createDefaultPolygonStyle(
//                        Color.GREEN,
//                        Color.WHITE), 0);
//        styleParameters.setMidgroundStyle(
//                DW_Style.createDefaultLineStyle(Color.LIGHT_GRAY), 0);
//    }
    public void postcodeMaps(
            //int resolutionMultiplier,
            TreeMap<String, ArrayList<String>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean scaleToFirst) throws IOException, Exception {

        // Single run
        Path dirIn1 = Paths.get("/scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/");
        Path dirOut1 = Paths.get(dirIn1.toString(), "Density");
//        grida Path /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/1-3/1-3E/Density/IndividualStyle/1-3EE/1-3EE_554_ncols_680_cellsize_50.0.asc does not exist!
//grida Path /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_January_TO_2015_September/1-3/3E-1/Density/IndividualStyle/1-3ES/1-3ES_554_ncols_680_cellsize_50.0.asc does not exist!
        String name;
        name = "2013_January_TO_2015_September";
        String namea;
        namea = "1-3";
        String nameb;
        nameb = getReverseName(namea);
        doDensity(dirIn1, name, namea, nameb, scaleToFirst);
        System.exit(0);

//        //        backgroundDW_Shapefile = tMSOACodesAndLeedsMSOAShapefile.getLeedsLevelDW_Shapefile();
//        //        foregroundDW_Shapefile1 = tMSOACodesAndLeedsMSOAShapefile.getLeedsLADDW_Shapefile();
//        // Other variables for selecting and output
//        Path dirIn = Paths.get(
//                mapDirectory,
//                "Tenancy");
//        dirIn = Paths.get(
//                dirIn,
//                "All");
//        dirIn = Paths.get(
//                dirIn,
//                "TenancyTypeTransition");
//        dirIn = Paths.get(
//                dirIn,
//                "CheckedPreviousTenure");
//        dirIn = Paths.get(
//                dirIn,
//                "TenancyAndPostcodeChanges");
//        if (doUnderOccupancyData) {
//            dirIn = Paths.get(
//                    dirIn,
//                    "UnderOccupied");
//            if (doCouncil) {
//                dirIn = Paths.get(
//                        dirIn,
//                        "Council");
//            } else {
//                dirIn = Paths.get(
//                        dirIn,
//                        "RSL");
//            }
//        } else {
//            dirIn = Paths.get(
//                    dirIn,
//                    "All");
//        }
//        dirIn = Paths.get(
//                dirIn,
//                "PostcodeChanged");
//        dirIn = Paths.get(
//                dirIn,
//                includeName);
//        dirIn = Paths.get(
//                dirIn,
//                "Ungrouped");
//        dirIn = Paths.get(
//                dirIn,
//                "PostcodeChanges");
//        dirIn = Paths.get(
//                dirIn,
//                "CheckedPreviousPostcode");
//        //dirOut.mkdirs(); // done later
//        //filename = "PostcodeChanges.csv";
//        Iterator<String> yearMonthsIte;
//        yearMonthsIte = yearMonths.keySet().iterator();
//        while (yearMonthsIte.hasNext()) {
//            String name;
//            name = yearMonthsIte.next();
//            Path dirIn2;
//            dirIn2 = Paths.get(
//                    dirIn,
//                    name);
//            // Totals for each tenancy type
//            Iterator<ArrayList<String>> allTenancyTypeChangesIte;
//            allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
//            while (allTenancyTypeChangesIte.hasNext()) {
//                ArrayList<String> tenancyTypeChanges;
//                tenancyTypeChanges = allTenancyTypeChangesIte.next();
//                String namea;
//                namea = DW_LineMapsLCC.getName(tenancyTypeChanges);
//                String nameb;
//                nameb = getReverseName(namea);
//                doDensity(
//                        dirIn2,
//                        name,
//                        namea,
//                        nameb,
//                        scaleToFirst);
//            }
//        }
    }

    protected String getReverseName(String namea) {
        String result;
        String[] split;
        split = namea.split("And");
        if (split.length == 1) {
            split = namea.split("-");
            result = split[1] + "-" + split[0];
        } else {
            result = "";
            String[] split2;
            split2 = split[0].split("-");
            result += split2[1] + "-" + split2[0];
            for (int i = 1; i < split.length; i++) {
                split2 = split[0].split("-");
                result += "And" + split2[1] + "-" + split2[0];
            }
        }
        return result;
    }

    protected void doDensity(Path dirIn, String name, String namea,
            String nameb, boolean scaleToFirst) throws IOException, Exception {
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/All/PostcodeChanged/All/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2008_October/1-3/1-3E.shp/1-3E.shp
        // /scratch02/DigitalWelfare/Output/LCC/SHBE/Maps/Line/Tenancy/All/TenancyTypeTransition/CheckedPreviousTenure/TenancyAndPostcodeChanges/UnderOccupied/Council/PostcodeChanged/Monthly/Ungrouped/PostcodeChanges/CheckedPreviousPostcode/2013_April_TO_2013_May_TO_2013_May/1-3/1-3S.shp/1-3S.shp
        Path dirIna = Paths.get(dirIn.toString(), namea, "Density");
        Path dirInb = Paths.get(dirIn.toString(), nameb, "Density");
        Path dirOut = Paths.get(dirIn.toString(), namea + "-" + nameb);
        if (scaleToFirst) {
            dirIna = Paths.get(dirIna.toString(), "CommonStyle");
            dirInb = Paths.get(dirInb.toString(), "CommonStyle");
        } else {
            dirIna = Paths.get(dirIna.toString(), "IndividualStyle");
            dirInb = Paths.get(dirInb.toString(), "IndividualStyle");
        }
        String name1;
        String name2;
        name1 = namea + "E";
        name2 = nameb + "S";
        doDensity(dirOut, dirIna, dirInb, name, name1, name2, scaleToFirst);
        name1 = namea + "S";
        name2 = nameb + "E";
        doDensity(dirOut, dirInb, dirIna, name, name1, name2, scaleToFirst);
    }

    protected void doDensity(Path dirOut, Path dirIna, Path dirInb, String name,
            String name1, String name2,
            boolean scaleToFirst) throws IOException, Exception {
        dirIna = Paths.get(dirIna.toString(), name1);
        dirInb = Paths.get(dirInb.toString(), name2);
        String outputName = name1 + "-" + name2;
        Path dirOut2 = Paths.get(dirOut.toString(), outputName);
        Files.createDirectories(dirOut2);

        // Set grid dimensions    
//            int multiplier;
//            multiplier = (int) (400 / cellsize);
//        maps.setBackgroundDW_Shapefile(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
//        maps.setForegroundDW_Shapefile1(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
        nrows = 554;//70 * multiplier * resolutionMultiplier; //139 * multiplier; //277 * multiplier;
        ncols = 680;//85 * multiplier * resolutionMultiplier; //170 * multiplier; //340 * multiplier;
        xllcorner = 413000;
        //minX 413220.095
        //minXRounded = 413200
        //maxX = 446875.313
        //maxXRounded = 446900
        //maxXRounded - minXRounded = 33700
        //((maxXRounded - minXRounded) / cellsize) + 6 = 680 // 6 added for display purposes
        // minY = 422595.31
        // minYRounded = 422500
        // maxY = 450175.312
        // maxYrounded = 450200
        // maxYRounded - minYRounded = 27700
        // (maxYRounded - minYRounded) / cellsize = 554
        yllcorner = 422500;
        dimensions = new Grids_Dimensions(
                BigDecimal.valueOf(xllcorner),
                BigDecimal.valueOf(yllcorner),
                BigDecimal.valueOf(xllcorner + (cellsize * ncols)),
                BigDecimal.valueOf(yllcorner + (cellsize * nrows)),
                BigDecimal.valueOf(cellsize));
        String outputNameE = name1 + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        String outputNameS = name2 + "_" + nrows + "_ncols_" + ncols + "_cellsize_" + cellsize;
        // Generate grids
        Path grida = Paths.get(dirIna.toString(), outputNameE + ".asc");
        if (!Files.exists(grida)) {
            System.out.println("grida Path " + grida + " does not exist!");
            return;
        }
        Path gridb = Paths.get(dirInb.toString(), outputNameS + ".asc");
        if (!Files.exists(gridb)) {
            System.out.println("gridb Path " + gridb + " does not exist! So the "
                    + "difference is all shown in grida Path " + grida);
            return;
        }
        Grids_Files gfiles = env.gridsEnv.files;
        //Path dir = env.ge.io.createNewFile(gfiles.getGeneratedGridDoubleDir());

        Grids_GridDouble ga = (Grids_GridDouble) gf.create(new Generic_Path(grida));
        System.out.println("ga " + ga.toString());
        //dir = env.ge.io.createNewFile(gfiles.getGeneratedGridDoubleDir());
        Grids_GridDouble gb = (Grids_GridDouble) gf.create(new Generic_Path(gridb));
        System.out.println("gb " + gb.toString());
        int dp = 2;
        RoundingMode rm = RoundingMode.HALF_UP;
        gp.addToGrid(ga, gb, BigDecimal.valueOf(-1.0d), dp, rm);
        System.out.println("ga-gb " + ga.toString());
        // output grid
//            ImageExporter ie;
//            ie = new ImageExporter(ge);
//            Path fout = Paths.get(
//                    dirOut2,
//                    name + ".PNG");
//            ie.toGreyScaleImage(g, gp, fout, "PNG", handleOutOfMemoryErrors);
        Path asciigridFile = Paths.get(dirOut2.toString(), outputName + ".asc");
        eage.toAsciiFile(ga, asciigridFile);
        // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
        int index = 0;
        outputGridToImageUsingGeoToolsAndSetCommonStyle(
                100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                ga,
                asciigridFile,
                dirOut2,
                outputName,
                index,
                scaleToFirst);
        if (!scaleToFirst) {
            maps.getStyleParameters().setStyle(name, null, index);
        }

        // Generalise the grid
        // Generate some geographically weighted statsitics
        List<String> stats = new ArrayList<>();
        stats.add("WSum");
        BigDecimal wi = BigDecimal.ONE;
        int wf = 2;
        //int cellDistanceForGeneralisation = mcdg;
        for (int cdfg = mcdg; cdfg > 1; cdfg /= 2) {
            index++;
            BigDecimal dist = BigDecimal.valueOf(cdfg * cellsize);
//                    // GeometricDensity
//                    Grids_GridDouble[] gws;
//                    gws = gp.geometricDensity(g, distance, gf);
//                    for (int gwsi = 0; gwsi < gws.length; gwsi++) {
//                        imageFile = Paths.get(
//                                outletmapDirectory,
//                                nameOfGrid + "GWS" + gwsi + ".png");
//                        ie.toGreyScaleImage(gws[gwsi], gp, imageFile, "png", false);
//                        asciigridFile = Paths.get(
//                                outletmapDirectory,
//                                nameOfGrid + "GWS" + gwsi + ".asc");
//                        eage.toAsciiFile(gws[gwsi], asciigridFile, false);
//                        System.out.println(gws[gwsi]);
//                    }
            // RegionUnivariateStatistics
            List<Grids_GridNumber> gws = gp.regionUnivariateStatistics(
                    ga, stats, dist, wi, wf, gf, dp, rm);

            Iterator<Grids_GridNumber> itegws = gws.iterator();
            // Skip over the normaliser part of the result
            itegws.next();
            Grids_GridNumber gwsgrid = itegws.next();

            System.out.println(gwsgrid);

            String outputName2 = outputName + "GWS_" + cdfg;// + "_" + i;
//                        imageFile = Paths.get(
//                                outletmapDirectory,
//                                outputName + ".png");
//                        ie.toGreyScaleImage(gwsgrid, gp, imageFile, "png", handleOutOfMemoryErrors);

            asciigridFile = Paths.get(dirOut2.toString(),                    outputName2 + ".asc");
            eage.toAsciiFile(gwsgrid, asciigridFile);

            System.out.println(asciigridFile);
            //System.exit(0);
            // outputGridToImageUsingGeoToolsAndSetCommonStyle - this styles everything too
            outputGridToImageUsingGeoToolsAndSetCommonStyle(
                    100.0d,//(double) Math.PI * cellDistanceForGeneralisation * cellDistanceForGeneralisation,
                    gwsgrid,
                    asciigridFile,
                    dirOut2,
                    outputName2,
                    index,
                    scaleToFirst);
            if (!scaleToFirst) {
                maps.getStyleParameters().setStyle(name, null, index);
            }
        }
    }
}
