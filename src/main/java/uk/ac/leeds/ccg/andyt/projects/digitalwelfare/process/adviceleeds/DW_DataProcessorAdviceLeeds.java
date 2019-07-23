/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.adviceleeds;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.math.Math_BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB1_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_LCC_WRU_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientEnquiryID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientOutletID;
import uk.ac.leeds.ccg.andyt.census.Census_Age_EcoAct_LSOA_DataRecord;
import uk.ac.leeds.ccg.andyt.census.Census_Age_EcoAct_LSOA_DataRecord_Handler;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataHandler;
import uk.ac.leeds.ccg.andyt.census.Census_DeprivationDataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census.DW_Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Handler;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_MapsAdviceLeeds;

/**
 * For processing data from the Citizens Advice Bureau branches
 */
public class DW_DataProcessorAdviceLeeds extends DW_ProcessorAdviceLeeds {

    DW_MapsAdviceLeeds MapsAdviceLeeds;

    private DW_Data_CAB2_Handler Data_CAB2_Handler;
    private DW_Data_CAB1_Handler Data_CAB1_Handler;
    private DW_Data_CAB0_Handler Data_CAB0_Handler;
    private DW_Data_LCC_WRU_Handler Data_LCC_WRU_Handler;
    private final DW_Deprivation_DataHandler Deprivation_DataHandler;

    private String level;
    private ArrayList<String> levels;
    /**
     * The keys are levels and the values are the target property names for
     * those.
     */
    private HashMap<String, String> targetPropertyNames;

    /**
     * Either DW_ID_ClientOutletEnquiryID or ClientBureauOutletID
     */
    private Object IDType;

    public DW_DataProcessorAdviceLeeds(
            DW_Environment env,
            File tDW_directory) {
        super(env);
        Postcode_Handler = env.getONSPD_Handler();
        Deprivation_DataHandler = env.getDeprivation_DataHandler();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            File tDW_Directory = new File("/scratch02/DigitalWelfare/");
            new DW_DataProcessorAdviceLeeds(null, tDW_Directory).run();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    public void run() throws Exception, Error {
        initData_CAB2_Handler();
        initData_CAB0_Handler();
        initData_LCC_WRU_Handler();
        // Initialise tDW_ID_ClientTypes
        ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes;
        tDW_ID_ClientTypes = new ArrayList<>();
        tDW_ID_ClientTypes.add(new DW_ID_ClientID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientOutletEnquiryID());
//        tDW_ID_ClientTypes.add(new DW_ID_ClientEnquiryID());
        // Initialise levels and targetPropertyNames. 
        levels = new ArrayList<>();
        targetPropertyNames = new HashMap<>();
//        // OA
//        level = "OA";
//        levels.add(level);
//        targetPropertyNames.put(level, "CODE");
//        // LSOA
//        level = "LSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "LSOA11CD");
//        // MSOA
//        level = "MSOA";
//        levels.add(level);
//        targetPropertyNames.put(level, "MSOA11CD");
//        // Postcode Sector
//        level = "PostcodeSector";
//        levels.add(level);
//        targetPropertyNames.put(level, "RMSect");
        level = "PostcodeDistrict";
        levels.add(level);
        targetPropertyNames.put(level, "RMSect");
//        // Postcodes Unit 
//        level = "PostcodeUnit";
//        levels.add(level);
//        targetPropertyNames.put(level, "POSTCODE");
        run(tDW_ID_ClientTypes);
    }

    /**
     * @param tDW_ID_ClientTypes
     * @throws java.lang.Exception
     */
    public void run(ArrayList<DW_ID_ClientID> tDW_ID_ClientTypes) throws Exception, Error {
        Iterator<DW_ID_ClientID> ite;
        ite = tDW_ID_ClientTypes.iterator();
        while (ite.hasNext()) {
            IDType = ite.next();
            Iterator<String> ite2;
            ite2 = levels.iterator();
            while (ite2.hasNext()) {
                level = ite2.next();
                runLevel();
            }
        }
    }

    public void runLevel() throws Exception, Error {
        ONSPD_YM3 YM3;
        YM3 = new ONSPD_YM3("2011_May");
        int CensusYear = 2011;
        // Get deprivation data
        TreeMap<String, Census_DeprivationDataRecord> tDeprivationData;
        tDeprivationData = getDeprivation_Data();
        // Get postcode to LSOA lookup
        TreeMap<String, String> tLookupFromPostcodeToLSOACensusCode;
        tLookupFromPostcodeToLSOACensusCode = getClaimPostcodeF_To_LevelCode_Map(
                "LSOA", CensusYear, YM3);
        // Get postcode to level lookup
        TreeMap<String, String> tLookupFromPostcodeToCensusCode;
        if (level.equalsIgnoreCase("PostcodeDistrict")
                || level.equalsIgnoreCase("PostcodeSector")
                || level.equalsIgnoreCase("PostcodeUnit")
                || level.equalsIgnoreCase("LSOA")) {
            tLookupFromPostcodeToCensusCode = tLookupFromPostcodeToLSOACensusCode;
        } else {
            tLookupFromPostcodeToCensusCode = getClaimPostcodeF_To_LevelCode_Map(
                    level, CensusYear, YM3);
        }
        TreeMap<Integer, Integer> deprivationClasses;
        deprivationClasses = Census_DeprivationDataHandler.getDeprivationClasses(
                tDeprivationData);
        String year;
        String filename;
        String outputFilename;

        // Get Leeds CAB Data
        System.out.println("Get Leeds CAB Data for 2012 to 2013");
        // year 2012-2013
        year = "1213";
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        outputFilename = "Leeds CAB " + year;
        Object[] allLeedsCABData1213 = processLeedsCABData(filename,
                outputFilename, tDeprivationData,
                tLookupFromPostcodeToCensusCode, deprivationClasses);
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData1213;
        //TreeMap<String, DW_Data_CAB2_Record> tLeedsCABData1213;
        tLeedsCABData1213 = (TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record>) allLeedsCABData1213[0];
        TreeMap<String, Object[]> leedsCABData1213;
        leedsCABData1213 = (TreeMap<String, Object[]>) allLeedsCABData1213[1];
        Set<String> leedsCABOutlets1213 = leedsCABData1213.keySet();
        // Get Chapeltown CAB Data for 2012-2013
        System.out.println("Get Chapeltown CAB Data 2012 to 2013");

        String Q1Filename;
        String Q2Filename;
        String Q3Filename;
        String Q4Filename;
        year = "1213";
        Q1Filename = "Q1.csv";
        Q2Filename = "Q2.csv";
        Q3Filename = "Q3.csv";
        Q4Filename = "Q4.csv";
        Object[] allChapeltownCABData1213;
        allChapeltownCABData1213 = processChapeltownCABData(year, Q1Filename,
                Q2Filename, Q3Filename, Q4Filename,
                tLookupFromPostcodeToCensusCode,
                tLookupFromPostcodeToLSOACensusCode, tDeprivationData,
                deprivationClasses);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData1213;
        tChapeltownCABData1213 = (TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record>) allChapeltownCABData1213[0];
        TreeMap<String, Object[]> chapeltownCABData1213;
        chapeltownCABData1213 = (TreeMap<String, Object[]>) allChapeltownCABData1213[1];
        Set<String> chapeltownOutlets1213 = chapeltownCABData1213.keySet();

        // Load Leeds CAB Data
        System.out.println("Load Leeds CAB Data 2012 to 2013");
        filename = "Leeds CAb data 2012-13ProblemFieldsCleared.csv";
        //TreeMap<?, DW_Data_CAB2_Record> tLeedsCABData1213;
        tLeedsCABData1213 = loadLeedsCABData(env,
                filename,
                Data_CAB2_Handler,
                IDType);
        System.out.println("aggregateClientCountsBy " + level);

        // Load LCC WRU data
        System.out.println("Load LCC WRU Data 2012 to 2013");
        filename = "WelfareRights - Data Extract.csv";
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> tLCC_WRUData1213;
        tLCC_WRUData1213 = loadLCC_WRUData(
                filename, Data_LCC_WRU_Handler, IDType);

        // Create and writeout combined aggregate counts
        System.out.println("Create and writeout combined aggregate counts");
        TreeMap<String, TreeMap<String, Integer>> aggregateClientCountsByLevel;
        aggregateClientCountsByLevel = aggregateClientCountsByLevel(
                tLeedsCABData1213,
                tChapeltownCABData1213,
                tLCC_WRUData1213,
                tLookupFromPostcodeToCensusCode);
        File generatedAdviceLeedsDir;
        generatedAdviceLeedsDir = new File(
                files.getGeneratedAdviceLeedsDir(),
                "Combined");
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                level);
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                IDType.getClass().getSimpleName());
        generatedAdviceLeedsDir.mkdirs();
        // Write out some tables here to map. All this code should move. Instead 
        // of writing out data we want to produce some maps.
        writeOutAggregateCounts(
                year,
                generatedAdviceLeedsDir,
                aggregateClientCountsByLevel);

        // Output table
        // 2012-2013
        System.out.println("Output table 2012-2013");
        outputTable(deprivationClasses, leedsCABOutlets1213, leedsCABData1213);
        outputTable(deprivationClasses, chapeltownOutlets1213, chapeltownCABData1213);

        // year 2011-2012
        year = "1112";
        filename = "Leeds CAb data 2011-12ProblemFieldsCleared.csv";
        outputFilename = "Leeds CAB " + year;
        Object[] allLeedsCABData1112 = processLeedsCABData(
                filename, outputFilename, tDeprivationData,
                tLookupFromPostcodeToCensusCode, deprivationClasses);
        TreeMap<String, DW_Data_CAB2_Record> tLeedsCABData1112;
        tLeedsCABData1112 = (TreeMap<String, DW_Data_CAB2_Record>) allLeedsCABData1112[0];
        TreeMap<String, Object[]> leedsCABData1112;
        leedsCABData1112 = (TreeMap<String, Object[]>) allLeedsCABData1112[1];
        Set<String> outlets1112 = leedsCABData1112.keySet();
        //outlets = leedsCABData1112.keySet();
        // Output table 2011-2012
        System.out.println("Output table 2011-2012");
        outputTable(deprivationClasses, outlets1112, leedsCABData1112);

//        Census_Age_EcoAct_LSOA_DataRecord_Handler aAge_EcoAct_LSOA_DataHandler;
//        aAge_EcoAct_LSOA_DataHandler = new Census_Age_EcoAct_LSOA_DataRecord_Handler();
//        String censusFilename = "Data_AGE_ECOACT_UNIT.csv";
//        TreeMap<String, Census_Age_EcoAct_LSOA_DataRecord> censusData;
//        censusData = aAge_EcoAct_LSOA_DataHandler.loadInputData(
//                censusFilename);
//
//        System.out.println("There are " + censusData.size() + " LSOAs in Leeds in 2011");
//        System.out.println("LSOACode,"
//                + "PopAge16to74,"
//                + "PopAge16to74LongTermSickOrDisabled,"
//                + "PopAge16to74LookingAfterHomeOrFamily,"
//                + "PopAge16to74Unemployed,"
//                + "IMDScore,"
//                + "RankOfIMDScoreForEngland,"
//                + "ChapeltownCABClientCount");
//        Iterator<String> ite = censusData.keySet().iterator();
//        while (ite.hasNext()) {
//            String LSOACode = ite.next();
//            Census_Age_EcoAct_LSOA_DataRecord aAge_EcoAct_LSOA_DataRecord;
//            aAge_EcoAct_LSOA_DataRecord = censusData.get(LSOACode);
//            Census_DeprivationDataRecord aDeprivation_DataRecord;
//            aDeprivation_DataRecord = tDeprivationData.get(LSOACode);
//            String outputline = LSOACode;
//            if (aAge_EcoAct_LSOA_DataRecord != null) {
//                outputline += ","
//                        + aAge_EcoAct_LSOA_DataRecord.getPopAge16to74() + ","
//                        + aAge_EcoAct_LSOA_DataRecord.getPopAge16to74LongTermSickOrDisabled() + ","
//                        + aAge_EcoAct_LSOA_DataRecord.getPopAge16to74LookingAfterHomeOrFamily() + ","
//                        + aAge_EcoAct_LSOA_DataRecord.getPopAge16to74Unemployed();
//            } else {
//                outputline += ",0,0,0,0";
//            }
//            if (aDeprivation_DataRecord != null) {
//                //System.out.println("tDeprivationData.get(LSOACode) == null, LSOACode = " + LSOACode);
//                outputline += "," + aDeprivation_DataRecord.getIMDScore() + ","
//                        + aDeprivation_DataRecord.getRankOfIMDScoreForEngland();
//            } else {
//                outputline += ",,";
//            }
//            Integer I = tChapeltownCABDatapeltownDataLSOACounts.get(LSOACode);
//            if (I != null) {
//                outputline += "," + I.toString();
//            } else {
//                outputline += ",";
//            }
//            System.out.println(outputline);
//        }
    }

    protected void outputTable(
            TreeMap<Integer, Integer> deprivationClasses,
            Set<String> outlets,
            TreeMap<String, Object[]> leedsCABData) {
        ArrayList<String> deprivationClassLabels = new ArrayList<>();
        //String[] outletValues = new String[outlets.size()];
        int[][] clientCounts = new int[deprivationClasses.size()][outlets.size()];
        int i = 0;
        int j = 0;
        Iterator<Integer> ite2 = deprivationClasses.keySet().iterator();
        Integer previousDeprivationClassValue = 0;
        while (ite2.hasNext()) {
            j = 0;
            Integer deprivationClassKey = ite2.next();
            Integer deprivationClassValue = deprivationClasses.get(deprivationClassKey);
            String deprivationClassLabel = previousDeprivationClassValue.toString() + "%-" + deprivationClassValue + "%";
            deprivationClassLabels.add(deprivationClassLabel);
            Integer count;
//            // get chapeltown data added
//            count = deprivationClassCountOfChapeltownCABClients.get(deprivationClassValue);
//            if (count == null) {
//                line += ",0";
//            } else {
//                line += "," + count;
//            }
            Iterator<String> ite3 = outlets.iterator();
            // add Leeds CAB data
            while (ite3.hasNext()) {
                String outlet = ite3.next();
                Object[] dummy = leedsCABData.get(outlet);
                TreeMap<Integer, Integer> deprivationClassCountOfCABClients;
                if (dummy == null) {
                    clientCounts[i][j] = 0;
                } else {
                    deprivationClassCountOfCABClients = (TreeMap<Integer, Integer>) dummy[0];
                    count = deprivationClassCountOfCABClients.get(deprivationClassValue);
                    if (count == null) {
                        //line += ",0";
                        clientCounts[i][j] = 0;
                    } else {
                        //line += "," + count;
                        clientCounts[i][j] = count;
                    }
                }
                j++;
            }
            //write out line
            //System.out.println(line);
            previousDeprivationClassValue = deprivationClassValue;
            i++;
        }
        System.out.print("Outlet\\LSOA Deprivation Class");
        Iterator<String> ite = deprivationClassLabels.iterator();
        while (ite.hasNext()) {
            String deprivationClassLabel = ite.next();
            System.out.print("," + deprivationClassLabel);
        }
        System.out.println("");
        ite = outlets.iterator();
        for (i = 0; i < outlets.size(); i++) {
            System.out.print(ite.next());
            for (j = 0; j < deprivationClassLabels.size(); j++) {
                System.out.print("," + clientCounts[j][i]);
            }
            System.out.println();
        }
    }

    /**
     * Processes data in Petra style
     *
     * @param year
     * @param Q1Filename
     * @param Q2Filename
     * @param Q3Filename
     * @param Q4Filename
     * @param tLookupFromPostcodeToLevelCensusCode
     * @param tLookupFromPostcodeToLSOACensusCode
     * @param tDeprivationData
     * @param deprivationClasses
     * @return
     */
    protected Object[] processChapeltownCABData(
            String year,
            String Q1Filename,
            String Q2Filename,
            String Q3Filename,
            String Q4Filename,
            TreeMap<String, String> tLookupFromPostcodeToLSOACensusCode,
            TreeMap<String, String> tLookupFromPostcodeToLevelCensusCode,
            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
            TreeMap<Integer, Integer> deprivationClasses) {
        System.out.println("<processChapeltownCABData>");
        Object[] result = new Object[3];

        String ChapeltownCAB_String = "ChapeltownCAB";

        // Load Chapeltown CAB data
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData;
        tChapeltownCABData = getChapeltownCABData(Data_CAB0_Handler, IDType);
        result[0] = tChapeltownCABData;

        // Generalise by LSOA Deprivation Class
        TreeMap<String, Object[]> chapeltownCABData0 = generaliseByLSOADeprivationClass(
                1,
                tChapeltownCABData,
                tDeprivationData,
                tLookupFromPostcodeToLSOACensusCode,
                deprivationClasses);
        result[1] = chapeltownCABData0;

        // Get Client count data for level
        TreeMap<String, TreeMap<String, Integer>> chapeltownCABData1;
        chapeltownCABData1 = aggregateClientCountsByLevel(
                1,
                tChapeltownCABData,
                tLookupFromPostcodeToLevelCensusCode);

        File generatedAdviceLeedsDir;
        generatedAdviceLeedsDir = new File(
                files.getGeneratedAdviceLeedsDir(),
                ChapeltownCAB_String);

        // Write out some tables here to map. All this code should move. Instead 
        // of writing out data we want to produce some maps.
        writeOutAggregateCounts(
                year,
                generatedAdviceLeedsDir,
                chapeltownCABData1);

        result[2] = chapeltownCABData1;

        System.out.println("</processChapeltownCABData>");
        return result;
    }

//    /**
//     *
//     * @param type if type == 0 then DW_Data_CAB0_Record is loaded otherwise
//     * CAB_DataRecord01 is loaded.
//     * @param dir
//     * @param year
//     * @param Q1Filename
//     * @param Q2Filename
//     * @param Q3Filename
//     * @param Q4Filename
//     * @param outputFilename
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @return
//     */
//    @Deprecated
//    protected void processChapeltownCABData(
//            int type,
//            File dir,
//            String year,
//            String Q1Filename,
//            String Q2Filename,
//            String Q3Filename,
//            String Q4Filename,
//            String outputFilename,
//            TreeMap<String, String[]> tLookupFromPostcodeToCensusCodes,
//            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
//            TreeMap<Integer, Integer> deprivationClasses) {
//
//        init_OutputTextFiles(outputFilename);
//
//        TreeMap<String, DW_Data_CAB0_Record> tQ1ChapeltownCABData;
//        tQ1ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData_EnquiryClientOutletID(
//                type, dir, Q1Filename, pw);
//        TreeMap<String, DW_Data_CAB0_Record> tQ2ChapeltownCABData;
//        tQ2ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData_EnquiryClientOutletID(
//                type, dir, Q2Filename, pw);
//        TreeMap<String, DW_Data_CAB0_Record> tQ3ChapeltownCABData;
//        tQ3ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData_EnquiryClientOutletID(
//                type, dir, Q3Filename, pw);
//        TreeMap<String, DW_Data_CAB0_Record> tQ4ChapeltownCABData;
//        tQ4ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData_EnquiryClientOutletID(
//                type, dir, Q4Filename, pw);
//
//////        System.out.println(LeedsCABData.firstKey());
//////        System.out.println(LeedsCABData.firstEntry().getValue());
////        int countRecordsWithNoPostcode = 0;
////        Iterator<String> ite = tQ1ChapeltownCABData.keySet().iterator();
////        while (ite.hasNext()) {
////            String SOACode = ite.next();
////            DW_Data_CAB0_Record aLeedsCABData_DataRecord = tQ1ChapeltownCABData.get(SOACode);
////            String postcode = aLeedsCABData_DataRecord.getPostcode();
////            if (postcode.isEmpty()) {
////                countRecordsWithNoPostcode++;
////            }
////        }
////        //System.out.println(name);
////        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
////        System.out.println("countRecordsWithPostcode " + (tQ1ChapeltownCABData.size() - countRecordsWithNoPostcode));
////        int count = tDeprivationData.size();
////        int percentageBand = 5;
////        int classWidth = (count / 100) * percentageBand;
////        //int numberOfClasses = 100/percentageBand;
////        int numberOfClasses = 10;
//        Object[] deprivationClassCountOfQ1CABClientsETC;
////        deprivationClassCountOfQ1CABClientsETC
////                = getDeprivationClassCountOfCABClients(
////                        count,
////                        tQ1ChapeltownCABData,
////                        tDeprivationData,
////                        tLookupFromPostcodeToCensusCodes,
////                        percentageBand,
////                        classWidth,
////                        numberOfClasses);
//        deprivationClassCountOfQ1CABClientsETC
//                = getDeprivationClassCountOfCABClients(
//                        tQ1ChapeltownCABData,
//                        tDeprivationData,
//                        tLookupFromPostcodeToCensusCodes,
//                        deprivationClasses,
//                        false);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ1CABClients;
//        deprivationClassCountOfQ1CABClients
//                = (TreeMap<Integer, Integer>) deprivationClassCountOfQ1CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ2CABClientsETC;
////        deprivationClassCountOfQ2CABClientsETC
////                = getDeprivationClassCountOfCABClients(
////                        count,
////                        tQ2ChapeltownCABData,
////                        tDeprivationData,
////                        tLookupFromPostcodeToCensusCodes,
////                        percentageBand,
////                        classWidth,
////                        numberOfClasses);
//        deprivationClassCountOfQ2CABClientsETC
//                = getDeprivationClassCountOfCABClients(
//                        tQ2ChapeltownCABData,
//                        tDeprivationData,
//                        tLookupFromPostcodeToCensusCodes,
//                        deprivationClasses,
//                        false);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ2CABClients;
//        deprivationClassCountOfQ2CABClients
//                = (TreeMap<Integer, Integer>) deprivationClassCountOfQ2CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ3CABClientsETC;
////        deprivationClassCountOfQ3CABClientsETC
////                = getDeprivationClassCountOfCABClients(
////                        count,
////                        tQ3ChapeltownCABData,
////                        tDeprivationData,
////                        tLookupFromPostcodeToCensusCodes,
////                        percentageBand,
////                        classWidth,
////                        numberOfClasses);
//        deprivationClassCountOfQ3CABClientsETC
//                = getDeprivationClassCountOfCABClients(
//                        tQ3ChapeltownCABData,
//                        tDeprivationData,
//                        tLookupFromPostcodeToCensusCodes,
//                        deprivationClasses,
//                        false);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ3CABClients;
//        deprivationClassCountOfQ3CABClients
//                = (TreeMap<Integer, Integer>) deprivationClassCountOfQ3CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ4CABClientsETC;
////        deprivationClassCountOfQ4CABClientsETC
////                = getDeprivationClassCountOfCABClients(
////                        count,
////                        tQ4ChapeltownCABData,
////                        tDeprivationData,
////                        tLookupFromPostcodeToCensusCodes,
////                        percentageBand,
////                        classWidth,
////                        numberOfClasses);
//        deprivationClassCountOfQ4CABClientsETC
//                = getDeprivationClassCountOfCABClients(
//                        tQ4ChapeltownCABData,
//                        tDeprivationData,
//                        tLookupFromPostcodeToCensusCodes,
//                        deprivationClasses,
//                        false);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ4CABClients;
//        deprivationClassCountOfQ4CABClients
//                = (TreeMap<Integer, Integer>) deprivationClassCountOfQ4CABClientsETC[0];
//
//        Iterator<Integer> ite2 = deprivationClassCountOfQ1CABClients.keySet().iterator();
//        System.out.println(year);
//        System.out.println("DeprivationClass,Q1,Q2,Q3,Q4");
//        Integer previousDeprivationClass = 0;
//        while (ite2.hasNext()) {
//            Integer deprivationClass = ite2.next();
//            Integer countOfQ1CABClients = deprivationClassCountOfQ1CABClients.get(deprivationClass);
//            Integer countOfQ2CABClients = deprivationClassCountOfQ2CABClients.get(deprivationClass);
//            Integer countOfQ3CABClients = deprivationClassCountOfQ3CABClients.get(deprivationClass);
//            Integer countOfQ4CABClients = deprivationClassCountOfQ4CABClients.get(deprivationClass);
//            if (ite2.hasNext()) {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to " + deprivationClass.toString() + "% deprived LSOAs"
//                        + "," + countOfQ1CABClients
//                        + "," + countOfQ2CABClients
//                        + "," + countOfQ3CABClients
//                        + "," + countOfQ4CABClients);
//            } else {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to 100% deprived LSOAs"
//                        + "," + countOfQ1CABClients
//                        + "," + countOfQ2CABClients
//                        + "," + countOfQ3CABClients
//                        + "," + countOfQ4CABClients);
//            }
//            previousDeprivationClass = deprivationClass;
//        }
//        System.out.println("Clients Without a recognised postcode"
//                + "," + ((Integer) deprivationClassCountOfQ1CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ2CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ3CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ4CABClientsETC[1]).toString()
//        );
//        close_OutputTextFiles();
//    }
//    protected void processLeedsCABData(
//            File dir,
//            String Q1Filename,
//            String Q2Filename,
//            String Q3Filename,
//            String Q4Filename,
//            String outputFilename) {
//        init_OutputTextFiles("outputFilename");
//        TreeMap<String, Census_DeprivationDataRecord> tDeprivationData;
//        tDeprivationData = getDeprivation_Data();
//
//        String level = "MSOA";
//        TreeMap<String, String> tLookupFromPostcodeToCensusCodes;
//        tLookupFromPostcodeToCensusCodes = getClaimPostcodeF_To_LevelCode_Map(level, 2011);
//        
//        TreeMap<String, CAB_DataRecord1> tQ11314LeedsCABData;
//        tQ11314LeedsCABData = tCAB_DataRecord1_Handler.loadInputData_EnquiryClientOutletID(
//                dir, Q1Filename, pw);
//        TreeMap<String, CAB_DataRecord1> tQ21314LeedsCABData;
//        tQ21314LeedsCABData = tCAB_DataRecord1_Handler.loadInputData_EnquiryClientOutletID(
//                dir, Q2Filename, pw);
//        TreeMap<String, CAB_DataRecord1> tQ31314LeedsCABData;
//        tQ31314LeedsCABData = tCAB_DataRecord1_Handler.loadInputData_EnquiryClientOutletID(dir, Q3Filename, pw);
//        TreeMap<String, CAB_DataRecord1> tQ41314LeedsCABData;
//        tQ41314LeedsCABData = tCAB_DataRecord1_Handler.loadInputData_EnquiryClientOutletID(dir, Q4Filename, pw);
//
////        System.out.println(LeedsCABData.firstKey());
////        System.out.println(LeedsCABData.firstEntry().getValue());
//        int countRecordsWithNoPostcode = 0;
//        Iterator<String> ite = tQ41314LeedsCABData.keySet().iterator();
//        while (ite.hasNext()) {
//            String SOACode = ite.next();
//            CAB_DataRecord1 aLeedsCABData_DataRecord = tQ41314LeedsCABData.get(SOACode);
//            String postcode = aLeedsCABData_DataRecord.getPostalCode();
//            if (postcode.isEmpty()) {
//                countRecordsWithNoPostcode++;
//            }
//        }
//        //System.out.println(name);
//        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
//        System.out.println("countRecordsWithPostcode " + (tQ41314LeedsCABData.size() - countRecordsWithNoPostcode));
//
//        int count = tDeprivationData.size();
//        int percentageBand = 5;
//        int classWidth = (count / 100) * percentageBand;
//        //int numberOfClasses = 100/percentageBand;
//        int numberOfClasses = 10;
//
//        Object[] deprivationClassCountOfQ11314CABClientsETC;
//        deprivationClassCountOfQ11314CABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                tQ11314LeedsCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                count,
//                percentageBand,
//                classWidth,
//                numberOfClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ11314CABClients;
//        deprivationClassCountOfQ11314CABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfQ11314CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ21314CABClientsETC;
//        deprivationClassCountOfQ21314CABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                tQ21314LeedsCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                count,
//                percentageBand,
//                classWidth,
//                numberOfClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ21314CABClients;
//        deprivationClassCountOfQ21314CABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfQ21314CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ31314CABClientsETC;
//        deprivationClassCountOfQ31314CABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                tQ31314LeedsCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                count,
//                percentageBand,
//                classWidth,
//                numberOfClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ31314CABClients;
//        deprivationClassCountOfQ31314CABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfQ31314CABClientsETC[0];
//
//        Object[] deprivationClassCountOfQ41314CABClientsETC;
//        deprivationClassCountOfQ41314CABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                tQ41314LeedsCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                count,
//                percentageBand,
//                classWidth,
//                numberOfClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfQ41314CABClients;
//        deprivationClassCountOfQ41314CABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfQ41314CABClientsETC[0];
//
//        Iterator<Integer> ite2 = deprivationClassCountOfQ11314CABClients.keySet().iterator();
//        System.out.println("DeprivationClass,Q1 1314,Q2 1314,Q3 1314,Q4 1314");
//        Integer previousDeprivationClass = 0;
//        while (ite2.hasNext()) {
//            Integer deprivationClass = ite2.next();
//            Integer countOfQ11314CABClients = deprivationClassCountOfQ11314CABClients.get(deprivationClass);
//            Integer countOfQ21314CABClients = deprivationClassCountOfQ21314CABClients.get(deprivationClass);
//            Integer countOfQ31314CABClients = deprivationClassCountOfQ31314CABClients.get(deprivationClass);
//            Integer countOfQ41314CABClients = deprivationClassCountOfQ41314CABClients.get(deprivationClass);
//            if (ite2.hasNext()) {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to " + deprivationClass.toString() + "% deprived LSOAs"
//                        + "," + countOfQ11314CABClients
//                        + "," + countOfQ21314CABClients
//                        + "," + countOfQ31314CABClients
//                        + "," + countOfQ41314CABClients);
//            } else {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to 100% deprived LSOAs"
//                        + "," + countOfQ11314CABClients
//                        + "," + countOfQ21314CABClients
//                        + "," + countOfQ31314CABClients
//                        + "," + countOfQ41314CABClients);
//            }
//            previousDeprivationClass = deprivationClass;
//        }
//        System.out.println("Clients without a recognised postcode"
//                + "," + ((Integer) deprivationClassCountOfQ11314CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ21314CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ31314CABClientsETC[1]).toString()
//                + "," + ((Integer) deprivationClassCountOfQ41314CABClientsETC[1]).toString()
//        );
//        close_OutputTextFiles();
//    }
    /**
     * @param deprivationClasses
     * @TODO Empty result returned
     * @param filename
     * @param outputFilename
     * @param tDeprivationData
     * @param tLookupFromPostcodeToLSOACensusCode
     * @return Object[3] result where: result[0] is a
     * TreeMap<DW_ID_ClientOutletEnquiryID, DW_Data_CAB2_Record> of LeedsCAB
     * data; result[1] = dataGeneralisedByLSOADeprivationClass is a TreeMap<String,
     * Object[]> (where the keys are outlets and the values are Object[2] value
     * where: value[0] = each value[0] = deprivationClassCountOfCABClients is a
     * TreeMap<Integer, Integer> where the keys are deprivationClasses and the
     * values are counts of the number of clients in each deprivation class;
     * values[1] = clientsWithoutARecognisedPostcode is an integer count of the
     * number of client records that do not have a recognised postcode value.);
     * result[2] is a TreeMap<String, TreeMap<String, Integer>> counts where the
     * keys are outlets and the values are counts of clients for each LSOA.
     *
     * leedsCABData1
     */
    protected Object[] processLeedsCABData(
            String filename,
            String outputFilename,
            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
            TreeMap<String, String> tLookupFromPostcodeToLSOACensusCode,
            TreeMap<Integer, Integer> deprivationClasses) {
        System.out.println("<processLeedsCABData>");
        Object[] result;
        result = new Object[3];

        String LeedsCAB_String = "LeedsCAB";

        // Load Leeds CAB Data
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData;
        tLeedsCABData = loadLeedsCABData(env,
                filename, Data_CAB2_Handler, IDType);
        result[0] = tLeedsCABData;

        File outputAdviceLeedsTablesDir;
        outputAdviceLeedsTablesDir = new File(
                files.getOutputAdviceLeedsTablesDir(),
                LeedsCAB_String);

        // Generalise by LSOA Deprivation Class
        TreeMap<String, Object[]> leedsCABData0 = generaliseByLSOADeprivationClass(
                0,
                tLeedsCABData,
                tDeprivationData,
                tLookupFromPostcodeToLSOACensusCode,
                deprivationClasses);
        result[1] = leedsCABData0;

        // Get Client count data for level
        TreeMap<String, TreeMap<String, Integer>> tAdviceLeedsData;
        tAdviceLeedsData = aggregateClientCountsByLevel(
                0,
                tLeedsCABData,
                tLookupFromPostcodeToLSOACensusCode);

        File generatedAdviceLeedsDir;
        generatedAdviceLeedsDir = new File(
                files.getGeneratedAdviceLeedsDir(),
                LeedsCAB_String);
        generatedAdviceLeedsDir = new File(
                generatedAdviceLeedsDir,
                IDType.getClass().getSimpleName());
        // Write out some tables here to map. All this code should move. Instead 
        // of writing out data we want to produce some maps.
        writeOutAggregateCounts(
                outputFilename,
                generatedAdviceLeedsDir,
                tAdviceLeedsData);

        result[2] = tAdviceLeedsData;
        System.out.println("</processLeedsCABData>");
        return result;
    }

    public void writeOutAggregateCounts(
            String firstPartFilename,
            File dir,
            TreeMap<String, TreeMap<String, Integer>> tAdviceLeedsData) {
        dir.mkdirs();
        // Write out some tables here to map. All this code should move. Instead 
        // of writing out data we want to produce some maps.
        // Calculate max and min and mean values for mapping
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        TreeMap<String, Integer> sums;
        sums = new TreeMap<>();
        TreeMap<String, Integer> mins;
        mins = new TreeMap<>();
        TreeMap<String, Integer> maxs;
        maxs = new TreeMap<>();

        Iterator<String> outletIterator = tAdviceLeedsData.keySet().iterator();
        String line;
        while (outletIterator.hasNext()) {
            String outlet = outletIterator.next();
            String filenameOut = firstPartFilename + outlet + ".csv";
            File outFile = new File(dir, filenameOut);
            PrintWriter pw3;
            pw3 = env.ge.io.getPrintWriter(outFile, false);
            TreeMap<String, Integer> counts = tAdviceLeedsData.get(outlet);
            Iterator<String> ite = counts.keySet().iterator();
            System.out.println("Outlet " + outlet);
            line = "Area, Count";
            System.out.println(line);
            pw3.println(line);
            while (ite.hasNext()) {
                String area = ite.next();
                Integer count = counts.get(area);
                line = "" + area + ", " + count;
                System.out.println(line);
                pw3.println(line);
                if (!(outlet.equalsIgnoreCase("AllLeedsCAB"))) {
                    max = Math.max(max, count);
                    min = Math.min(min, count);
                    Generic_Collections.addToTreeMapValueInteger(
                            sums, area, count);
                    Generic_Collections.setMaxValueTreeMapStringInteger(
                            maxs, area, count);
                    Generic_Collections.setMinValueTreeMapStringInteger(
                            mins, area, count);
                }
            }
            pw3.close();
        }
        TreeMap<String, Integer> midminmaxs;
        midminmaxs = new TreeMap<>();

        double n = tAdviceLeedsData.keySet().size() - 1;
        int maxSum;
        maxSum = Integer.MIN_VALUE;
        int minSum;
        minSum = Integer.MAX_VALUE;
        Iterator<String> ite;
        ite = sums.keySet().iterator();
        while (ite.hasNext()) {
            String area = ite.next();
            Integer sum = sums.get(area);
            maxSum = Math.max(maxSum, sum);
            minSum = Math.min(minSum, sum);
            Integer minArea = mins.get(area);
            Integer maxArea = maxs.get(area);
            BigDecimal midminmax = new BigDecimal(
                    (double) (minArea + maxArea) / (double) 2);
            midminmaxs.put(area,
                    Math_BigDecimal.roundIfNecessary(
                            midminmax, 0, RoundingMode.HALF_EVEN).intValue());
        }
        String minArea = "";
        String maxArea = "";
        ite = sums.keySet().iterator();
        while (ite.hasNext()) {
            String LSOA = ite.next();
            Integer sum = sums.get(LSOA);
            if (sum == maxSum) {
                maxArea = LSOA;
            }
            if (sum == minSum) {
                minArea = LSOA;
            }
        }
        ite = sums.keySet().iterator();
        while (ite.hasNext()) {
            String area = ite.next();
            Integer sum = sums.get(area);
            BigDecimal mean;
            mean = new BigDecimal(((double) sum / (double) n));
            sums.put(area,
                    Math_BigDecimal.roundIfNecessary(
                            mean, 0, RoundingMode.HALF_EVEN).intValue());
            if (area.equalsIgnoreCase(maxArea)) {
                sums.put(area, max);
                midminmaxs.put(area, max);
            }
            if (area.equalsIgnoreCase(minArea)) {
                sums.put(area, min);
                midminmaxs.put(area, min);
            }
        }
        String outlet = "Scale";
        String filenameOut = firstPartFilename + outlet + ".csv";
        File outFile = new File(dir, filenameOut);
        PrintWriter pw3;
        pw3 = env.ge.io.getPrintWriter(outFile, false);
        System.out.println("Outlet " + outlet);
        line = "Area, Count";
        System.out.println(line);
        pw3.println(line);
        ite = sums.keySet().iterator();
        while (ite.hasNext()) {
            String area = ite.next();
            // Using the value half way between the min and the max here instead
            // of the mean, so calculation of the mean is unnecessary as things
            // are. It might be better to use the median or a truncated mean or
            // some other mid range value. 
//            Integer count = sums.get(area);
            Integer count = midminmaxs.get(area);
            line = "" + area + ", " + count;
            System.out.println(line);
            pw3.println(line);
        }
        pw3.close();
    }

    /**
     * Generalise by LSOA Deprivation Class.
     *
     * @param tLeedsCABData
     * @param tChapeltownCABData
     * @param tLCC_WRUData
     * @param tLookupFromPostcodeToCensusCode
     * @return
     */
    protected TreeMap<String, TreeMap<String, Integer>> aggregateClientCountsByLevel(
            TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> tLeedsCABData,
            TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tChapeltownCABData,
            TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> tLCC_WRUData,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode) {
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        String outlet;

        TreeMap<String, String> outletsAndPostcodes;
        outletsAndPostcodes = getOutletsAndPostcodes();

        // AllAdviceLeeds
        String allAdviceLeedsString = "AllAdviceLeeds";
        TreeMap<String, Integer> allAdviceLeedsCounts;
        allAdviceLeedsCounts = new TreeMap<>();
        result.put(allAdviceLeedsString, allAdviceLeedsCounts);
        Set<DW_ID_ClientID> allAdviceLeedsIDs;
        allAdviceLeedsIDs = new HashSet<>();

        // AllCABCounts
        String allCABString = "AllCAB";
        TreeMap<String, Integer> allCABCounts;
        allCABCounts = new TreeMap<>();
        result.put(allCABString, allCABCounts);
        Set<DW_ID_ClientID> allCABIDs;
        allCABIDs = new HashSet<>();

        // AllCABOutletCounts
        String allCABOutletString = "AllCABOutlet";
        TreeMap<String, Integer> allCABOutletCounts;
        allCABOutletCounts = new TreeMap<>();
        result.put(allCABOutletString, allCABOutletCounts);
        Set<DW_ID_ClientID> allCABOutletIDs;
        allCABOutletIDs = new HashSet<>();

        // AllCABNonOutletCounts
        String allCABNonOutletString = "AllCABNonOutlet";
        TreeMap<String, Integer> allCABNonOutletCounts;
        allCABNonOutletCounts = new TreeMap<>();
        result.put(allCABNonOutletString, allCABNonOutletCounts);
        Set<DW_ID_ClientID> allCABNonOutletIDs;
        allCABNonOutletIDs = new HashSet<>();

        // Add from tLeedsCABData
        Iterator<DW_ID_ClientID> ite;
        ite = tLeedsCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_CAB2_Record aLeedsCABData_DataRecord = tLeedsCABData.get(id);
            outlet = aLeedsCABData_DataRecord.getOutlet();
            String postcode = aLeedsCABData_DataRecord.getPostcode();
            String key = getKey(
                    Postcode_Handler.getDefaultYM3(),
                    postcode,
                    tLookupFromPostcodeToCensusCode);
            // Add to counts for specific outlet
            if (result.containsKey(outlet)) {
                TreeMap<String, Integer> d;
                d = result.get(outlet);
                if (d.keySet().contains(key)) {
                    int count = d.get(key);
                    count++;
                    d.put(key, count);
                } else {
                    d.put(key, 1);
                }
            } else {
                TreeMap<String, Integer> d;
                d = new TreeMap<>();
                d.put(key, 1);
                result.put(outlet, d);
            }
            // Add to allAdviceLeedsCounts
            if (!allAdviceLeedsIDs.contains(id)) {
                allAdviceLeedsIDs.add(id);
                if (allCABCounts.keySet().contains(key)) {
                    int count = allCABCounts.get(key);
                    count++;
                    allCABCounts.put(key, count);
                } else {
                    allCABCounts.put(key, 1);
                }
            }
            // Add to allCABCounts
            if (!allCABIDs.contains(id)) {
                allCABIDs.add(id);
                if (allCABCounts.keySet().contains(key)) {
                    int count = allCABCounts.get(key);
                    count++;
                    allCABCounts.put(key, count);
                } else {
                    allCABCounts.put(key, 1);
                }
            }
            // Add to allCABOutletCounts or allCABNonOutletCounts
            String tCABOutletString = getCABOutletString(outlet);
            if (outletsAndPostcodes.keySet().contains(tCABOutletString)) {
                // Add to allCABOutletCounts
                if (!allCABOutletIDs.contains(id)) {
                    allCABOutletIDs.add(id);
                    if (allCABOutletCounts.keySet().contains(key)) {
                        int count = allCABOutletCounts.get(key);
                        count++;
                        allCABOutletCounts.put(key, count);
                    } else {
                        allCABOutletCounts.put(key, 1);
                    }
                }
            } else {
                // Add to allCABNonOutletCounts
                if (!allCABNonOutletIDs.contains(id)) {
                    allCABNonOutletIDs.add(id);
                    if (allCABNonOutletCounts.keySet().contains(key)) {
                        int count = allCABNonOutletCounts.get(key);
                        count++;
                        allCABNonOutletCounts.put(key, count);
                    } else {
                        allCABNonOutletCounts.put(key, 1);
                    }
                }
            }
        }

        // Add from tChapeltownCABData
        ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_CAB0_Record aCAB_DataRecord0 = tChapeltownCABData.get(id);
            //outlet = aCAB_DataRecord0.getOutlet();
            String postcode = aCAB_DataRecord0.getPostcode();
            String key = getKey(
                    Postcode_Handler.getDefaultYM3(),
                    postcode,
                    tLookupFromPostcodeToCensusCode);
            // Add to counts for specific outlet
            outlet = "CHAPELTOWN"; // Outlet always Chapeltown for Chapeltown data
            if (result.containsKey(outlet)) {
                TreeMap<String, Integer> d;
                d = result.get(outlet);
                if (d.keySet().contains(key)) {
                    int count = d.get(key);
                    count++;
                    d.put(key, count);
                } else {
                    d.put(key, 1);
                }
            } else {
                TreeMap<String, Integer> d;
                d = new TreeMap<>();
                d.put(key, 1);
                result.put(outlet, d);
            }
            // Add to allAdviceLeedsCounts
            if (allAdviceLeedsCounts.keySet().contains(key)) {
                int count = allCABCounts.get(key);
                count++;
                allAdviceLeedsCounts.put(key, count);
            } else {
                allAdviceLeedsCounts.put(key, 1);
            }
            // Add to allCABCounts
            if (allCABCounts.keySet().contains(key)) {
                int count = allCABCounts.get(key);
                count++;
                allCABCounts.put(key, count);
            } else {
                allCABCounts.put(key, 1);
            }
            // Add to allCABOutletCounts or allCABNonOutletCounts
            String tCABOutletString = getCABOutletString(outlet);
            if (outletsAndPostcodes.keySet().contains(tCABOutletString)) {
                // Add to allCABOutletCounts
                if (allCABOutletCounts.keySet().contains(key)) {
                    int count = allCABOutletCounts.get(key);
                    count++;
                    allCABOutletCounts.put(key, count);
                } else {
                    allCABOutletCounts.put(key, 1);
                }
            } else {
                // Add to allCABNonOutletCounts
                if (allCABNonOutletCounts.keySet().contains(key)) {
                    int count = allCABNonOutletCounts.get(key);
                    count++;
                    allCABNonOutletCounts.put(key, count);
                } else {
                    allCABNonOutletCounts.put(key, 1);
                }
            }
        }

        // Add from tLCC_WRUData
        ite = tLCC_WRUData.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_LCC_WRU_Record rec;
            rec = tLCC_WRUData.get(id);
            //outlet = aCAB_DataRecord0.getOutlet();
            String postcode = rec.getPostcode();
            String key = getKey(
                    Postcode_Handler.getDefaultYM3(),
                    postcode,
                    tLookupFromPostcodeToCensusCode);
            // Add to counts for specific outlet
            outlet = "LCC_WRU"; // Outlet always LCC_WRU
            if (result.containsKey(outlet)) {
                TreeMap<String, Integer> d;
                d = result.get(outlet);
                if (d.keySet().contains(key)) {
                    int count = d.get(key);
                    count++;
                    d.put(key, count);
                } else {
                    d.put(key, 1);
                }
            } else {
                TreeMap<String, Integer> d;
                d = new TreeMap<>();
                d.put(key, 1);
                result.put(outlet, d);
            }
            // Add to allAdviceLeedsCounts
            if (allAdviceLeedsCounts.keySet().contains(key)) {
                int count = allAdviceLeedsCounts.get(key);
                count++;
                allAdviceLeedsCounts.put(key, count);
            } else {
                allAdviceLeedsCounts.put(key, 1);
            }
        }
        return result;
    }

    /**
     *
     * @param postcode
     * @param tLookupFromPostcodeToCensusCode
     * @return
     */
    private String getKey(
            ONSPD_YM3 yM3,
            String postcode,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode) {
        ONSPD_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getONSPD_Handler();
        String key = "";
        if (level.equalsIgnoreCase("PostcodeDistrict")
                || level.equalsIgnoreCase("PostcodeSector")
                || level.equalsIgnoreCase("PostcodeUnit")) {
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                    key = Postcode_Handler.getPostcodeDistrict(postcode);
//                    } else {
//                        key = "";
                }
            }
            if (level.equalsIgnoreCase("PostcodeSector")) {
                if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                    key = Postcode_Handler.getPostcodeSector(postcode);
//                    } else {
//                        key = "";
                }
            }
            if (level.equalsIgnoreCase("PostcodeUnit")) {
                if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                    key = Postcode_Handler.formatPostcodeForMapping(postcode);
//                    } else {
//                        key = "";
                }
            }
        } else {
            String formattedPostcode = Postcode_Handler.formatPostcode(postcode);
            key = tLookupFromPostcodeToCensusCode.get(formattedPostcode);
            if (key == null) {
                key = "";
            }
        }
        return key;
    }

    /**
     * @param type
     * @param data
     * @param tLookupFromPostcodeToCensusCode
     * @return
     */
    protected TreeMap<String, TreeMap<String, Integer>> aggregateClientCountsByLevel(
            int type,
            //String outputFilename,
            TreeMap data,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode) {
        ONSPD_Handler tDW_Postcode_Handler;
        tDW_Postcode_Handler = env.getONSPD_Handler();
        TreeMap<String, TreeMap<String, Integer>> result;
        result = new TreeMap<>();
        String outlet;
        String outletAllLeedsCAB = "AllLeedsCAB";
        TreeMap<String, Integer> allAdviceLeedsCounts;
        allAdviceLeedsCounts = new TreeMap<>();
        result.put(outletAllLeedsCAB, allAdviceLeedsCounts);
        ONSPD_YM3 yM3;
        yM3 = Postcode_Handler.getDefaultYM3();
        Iterator<Object> ite;
        ite = data.keySet().iterator();
        if (level.equalsIgnoreCase("PostcodeDistrict")
                || level.equalsIgnoreCase("PostcodeSector")
                || level.equalsIgnoreCase("PostcodeUnit")) {
            if (level.equalsIgnoreCase("PostcodeDistrict")) {
                if (type == 0) {
                    while (ite.hasNext()) {
                        Object id = ite.next();
                        DW_Data_CAB2_Record aLeedsCABData_DataRecord;
                        aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) data.get(id);
                        outlet = aLeedsCABData_DataRecord.getOutlet();
                        String postcode = aLeedsCABData_DataRecord.getPostcode();
                        if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                            String postcodeDistrict;
                            postcodeDistrict = Postcode_Handler.getPostcodeDistrict(postcode);
                            // Add to counts
                            addToCounts(
                                    result,
                                    allAdviceLeedsCounts,
                                    outlet,
                                    postcodeDistrict);
                        }
                    }
                } else {
                    while (ite.hasNext()) {
                        Object id = ite.next();
                        DW_Data_CAB0_Record aLeedsCABData_DataRecord;
                        aLeedsCABData_DataRecord = (DW_Data_CAB0_Record) data.get(id);
                        outlet = "CHAPELTOWN";
                        String postcode = aLeedsCABData_DataRecord.getPostcode();
                        if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                            String postcodeDistrict;
                            postcodeDistrict = Postcode_Handler.getPostcodeDistrict(postcode);
                            // Add to counts
                            addToCounts(
                                    result,
                                    allAdviceLeedsCounts,
                                    outlet,
                                    postcodeDistrict);
                        }
                    }
                }
            } else {
                if (level.equalsIgnoreCase("PostcodeSector")) {
                    if (type == 0) {
                        while (ite.hasNext()) {
                            Object id = ite.next();
                            DW_Data_CAB2_Record aLeedsCABData_DataRecord;
                            aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) data.get(id);
                            outlet = aLeedsCABData_DataRecord.getOutlet();
                            String postcode = aLeedsCABData_DataRecord.getPostcode();
                            if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                                String postcodeSector;
                                postcodeSector = Postcode_Handler.getPostcodeSector(postcode);
                                // Add to counts
                                addToCounts(
                                        result,
                                        allAdviceLeedsCounts,
                                        outlet,
                                        postcodeSector);
                            }
                        }
                    } else {
                        while (ite.hasNext()) {
                            Object id = ite.next();
                            DW_Data_CAB0_Record aLeedsCABData_DataRecord;
                            aLeedsCABData_DataRecord = (DW_Data_CAB0_Record) data.get(id);
                            outlet = "CHAPELTOWN";
                            String postcode = aLeedsCABData_DataRecord.getPostcode();
                            if (tDW_Postcode_Handler.isMappablePostcode(yM3, postcode)) {
                                String postcodeSector;
                                postcodeSector = Postcode_Handler.getPostcodeSector(postcode);
                                // Add to counts
                                addToCounts(
                                        result,
                                        allAdviceLeedsCounts,
                                        outlet,
                                        postcodeSector);
                            }
                        }
                    }
                } else {
                    if (type == 0) {
                        while (ite.hasNext()) {
                            Object id = ite.next();
                            DW_Data_CAB2_Record aLeedsCABData_DataRecord;
                            aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) data.get(id);
                            outlet = aLeedsCABData_DataRecord.getOutlet();
                            String postcode = aLeedsCABData_DataRecord.getPostcode();
                            postcode = tDW_Postcode_Handler.formatPostcodeForMapping(postcode);
                            // Add to counts
                            addToCounts(
                                    result,
                                    allAdviceLeedsCounts,
                                    outlet,
                                    postcode);
                        }
                    } else {
                        while (ite.hasNext()) {
                            Object id = ite.next();
                            DW_Data_CAB0_Record aLeedsCABData_DataRecord;
                            aLeedsCABData_DataRecord = (DW_Data_CAB0_Record) data.get(id);
                            outlet = "CHAPELTOWN";
                            String postcode = aLeedsCABData_DataRecord.getPostcode();
                            postcode = tDW_Postcode_Handler.formatPostcodeForMapping(postcode);
                            // Add to counts
                            addToCounts(
                                    result,
                                    allAdviceLeedsCounts,
                                    outlet,
                                    postcode);
                        }
                    }
                }
            }
        } else {
            if (type == 0) {
                while (ite.hasNext()) {
                    Object id = ite.next();
                    DW_Data_CAB2_Record aLeedsCABData_DataRecord;
                    aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) data.get(id);
                    outlet = aLeedsCABData_DataRecord.getOutlet();
                    String postcode = aLeedsCABData_DataRecord.getPostcode();
                    String censusCode = getCensusCode(
                            postcode, tLookupFromPostcodeToCensusCode);
                    if (censusCode == null) {
                        // unrecognised postcode
                        if (!postcode.isEmpty()) {
                            System.out.println("Unrecognised postcode \"" + postcode + "\"");
                        }
                    } else {
                        // Add to counts
                        addToCounts(
                                result,
                                allAdviceLeedsCounts,
                                outlet,
                                censusCode);
                    }
                }
            } else {
                while (ite.hasNext()) {
                    Object id = ite.next();
                    DW_Data_CAB0_Record aLeedsCABData_DataRecord;
                    aLeedsCABData_DataRecord = (DW_Data_CAB0_Record) data.get(id);
                    outlet = "CHAPELTOWN";
                    String postcode = aLeedsCABData_DataRecord.getPostcode();
                    String censusCode = getCensusCode(
                            postcode, tLookupFromPostcodeToCensusCode);
                    if (censusCode == null) {
                        // unrecognised postcode
                        if (!postcode.isEmpty()) {
                            System.out.println("Unrecognised postcode \"" + postcode + "\"");
                        }
                    } else {
                        // Add to counts
                        addToCounts(
                                result,
                                allAdviceLeedsCounts,
                                outlet,
                                censusCode);
                    }
                }
            }
        }
        return result;
    }

    private String getCensusCode(String postcode,
            TreeMap<String, String> tLookupFromPostcodeToCensusCode) {
        String result;
        String formattedPostcode = Postcode_Handler.formatPostcode(postcode);
        result = tLookupFromPostcodeToCensusCode.get(formattedPostcode);
        return result;
    }

    private void addToCounts(
            TreeMap<String, TreeMap<String, Integer>> aggregateCounts,
            TreeMap<String, Integer> allLeedsCABCounts,
            String outlet,
            String areaCode) {
        // Add to counts for specific outlet
        if (aggregateCounts.containsKey(outlet)) {
            TreeMap<String, Integer> d;
            d = aggregateCounts.get(outlet);
            if (d.keySet().contains(areaCode)) {
                int count = d.get(areaCode);
                count++;
                d.put(areaCode, count);
            } else {
                d.put(areaCode, 1);
            }
        } else {
            TreeMap<String, Integer> d;
            d = new TreeMap<>();
            d.put(areaCode, 1);
            aggregateCounts.put(outlet, d);
        }
        // Add to outletAllLeedsCAB
        if (allLeedsCABCounts.keySet().contains(areaCode)) {
            int count = allLeedsCABCounts.get(areaCode);
            count++;
            allLeedsCABCounts.put(areaCode, count);
        } else {
            allLeedsCABCounts.put(areaCode, 1);
        }
    }

    /**
     * Generalise by LSOA Deprivation Class.
     *
     * @param type
     * @param tCABData
     * @param tDeprivationData
     * @param tLookupFromPostcodeToCensusCodes
     * @param deprivationClasses
     * @return TreeMap<String, Object[]> where the keys are outlets and the
     * values are Object[2] value where: value[0] = each value[0] =
     * deprivationClassCountOfCABClients is a TreeMap<Integer, Integer> where
     * the keys are deprivationClasses and the values are counts of the number
     * of clients in each deprivation class; values[1] =
     * clientsWithoutARecognisedPostcode is an integer count of the number of
     * client records that do not have a recognised postcode value.
     */
    protected TreeMap<String, Object[]> generaliseByLSOADeprivationClass(
            int type,
            TreeMap<DW_ID_ClientID, ?> tCABData,
            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            TreeMap<Integer, Integer> deprivationClasses) {
        TreeMap<String, Object[]> result = new TreeMap<>();
        //init_OutputTextFiles("outputFilename");

        String outlet;
//        int count = tDeprivationData.size();
//        int percentageBand = 5;
//        int classWidth = (count / 100) * percentageBand;
//        //int numberOfClasses = 100/percentageBand;
//        int numberOfClasses = 10;
//        getAndWriteOutDeprivationClassCounts(count, percentageBand, tLeedsCABData, tDeprivationData,
//                tLookupFromPostcodeToCensusCodes, classWidth, numberOfClasses);

        // getDeprivationClassCounts
        outlet = "AllLeedsCAB";
        Object[] deprivationClassCountOfAllLeedsCABClientsETC;
        deprivationClassCountOfAllLeedsCABClientsETC = getAndWriteOutDeprivationClassCounts(
                type,
                tCABData,
                tDeprivationData,
                tLookupFromPostcodeToCensusCodes,
                deprivationClasses);
        result.put(outlet, deprivationClassCountOfAllLeedsCABClientsETC);

        if (type == 0) {
            // Initialise outletLeedsCABData which has keys of Outlets and values as
            // TreeMap<String, TreeMap<Object, DW_Data_CAB2_Record>>
            // where the keys are EnquiryClientBureauOutletIDs or 
            // ClientBureauOutletIDs and the values are CAB_DataRecord2s
            Iterator<DW_ID_ClientID> ite;
            TreeMap<String, TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record>> outletLeedsCABData;
            outletLeedsCABData = new TreeMap<>();
            ite = tCABData.keySet().iterator();
            while (ite.hasNext()) {
                DW_ID_ClientID id = ite.next();
                DW_Data_CAB2_Record aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) tCABData.get(id);
                outlet = aLeedsCABData_DataRecord.getOutlet();
                if (outletLeedsCABData.containsKey(outlet)) {
                    TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> d;
                    d = outletLeedsCABData.get(outlet);
                    d.put(id, aLeedsCABData_DataRecord);
                } else {
                    TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> d;
                    d = new TreeMap<>();
                    d.put(id, aLeedsCABData_DataRecord);
                    outletLeedsCABData.put(outlet, d);
                }
            }
            Iterator<String> ite2 = outletLeedsCABData.keySet().iterator();
            while (ite2.hasNext()) {
                outlet = ite2.next();
                tCABData = outletLeedsCABData.get(outlet);
                System.out.println("Outlet " + outlet);
//            getAndWriteOutDeprivationClassCounts(count, percentageBand, tLeedsCABData, tDeprivationData,
//                    tLookupFromPostcodeToCensusCodes, classWidth, numberOfClasses);
                Object[] deprivationClassCountOfCABClientsETC;
                deprivationClassCountOfCABClientsETC = getAndWriteOutDeprivationClassCounts(
                        type,
                        tCABData,
                        tDeprivationData,
                        tLookupFromPostcodeToCensusCodes,
                        deprivationClasses);
                result.put(outlet, deprivationClassCountOfCABClientsETC);
            }
        } else {
            Iterator<DW_ID_ClientID> ite;
            TreeMap<String, TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record>> outletChapeltownCABData;
            outletChapeltownCABData = new TreeMap<>();
            ite = tCABData.keySet().iterator();
            while (ite.hasNext()) {
                DW_ID_ClientID id = ite.next();
                DW_Data_CAB0_Record aCABData_DataRecord = (DW_Data_CAB0_Record) tCABData.get(id);
                outlet = "Chapeltown";
                if (outletChapeltownCABData.containsKey(outlet)) {
                    TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> d;
                    d = outletChapeltownCABData.get(outlet);
                    d.put(id, aCABData_DataRecord);
                } else {
                    TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> d;
                    d = new TreeMap<>();
                    d.put(id, aCABData_DataRecord);
                    outletChapeltownCABData.put(outlet, d);
                }
            }
            Iterator<String> ite2 = outletChapeltownCABData.keySet().iterator();
            while (ite2.hasNext()) {
                outlet = ite2.next();
                tCABData = outletChapeltownCABData.get(outlet);
                System.out.println("Outlet " + outlet);
//            getAndWriteOutDeprivationClassCounts(count, percentageBand, tLeedsCABData, tDeprivationData,
//                    tLookupFromPostcodeToCensusCodes, classWidth, numberOfClasses);
                Object[] deprivationClassCountOfCABClientsETC;
                deprivationClassCountOfCABClientsETC = getAndWriteOutDeprivationClassCounts(
                        type,
                        tCABData,
                        tDeprivationData,
                        tLookupFromPostcodeToCensusCodes,
                        deprivationClasses);
                result.put(outlet, deprivationClassCountOfCABClientsETC);
            }
        }
        return result;
    }

//    /**
//     * Generalise by LSOA Deprivation Class.
//     *
//     * @param dir
//     * @param tDeprivationData
//     * @param tChapeltownCABData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @param outputFilename
//     * @param deprivationClasses
//     * @return TreeMap<String, Object[]> where the keys are outlets and the
//     * values are Object[2] value where: value[0] = each value[0] =
//     * deprivationClassCountOfCABClients is a TreeMap<Integer, Integer> where
//     * the keys are deprivationClasses and the values are counts of the number
//     * of clients in each deprivation class; values[1] =
//     * clientsWithoutARecognisedPostcode is an integer count of the number of
//     * client records that do not have a recognised postcode value.
//     */
//    protected TreeMap<String, Object[]> generaliseByLSOADeprivationClass(
//            File dir,
//            String outputFilename,
//            TreeMap<String, DW_Data_CAB0_Record> tChapeltownCABData,
//            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
//            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
//            TreeMap<Integer, Integer> deprivationClasses) {
//        TreeMap<String, Object[]> result = new TreeMap<String, Object[]>();
//        //init_OutputTextFiles("outputFilename");
//
//        String outlet;
////        int count = tDeprivationData.size();
////        int percentageBand = 5;
////        int classWidth = (count / 100) * percentageBand;
////        //int numberOfClasses = 100/percentageBand;
////        int numberOfClasses = 10;
////        getAndWriteOutDeprivationClassCounts(count, percentageBand, tLeedsCABData, tDeprivationData,
////                tLookupFromPostcodeToCensusCodes, classWidth, numberOfClasses);
//
//        // getDeprivationClassCounts
//        outlet = "AllLeedsCAB";
//        Object[] deprivationClassCountOfAllLeedsCABClientsETC;
//        deprivationClassCountOfAllLeedsCABClientsETC = getAndWriteOutDeprivationClassCounts(
//                tChapeltownCABData, tDeprivationData,
//                tLookupFromPostcodeToCensusCodes, deprivationClasses);
//        result.put(outlet, deprivationClassCountOfAllLeedsCABClientsETC);
//
//        // Initialise outletLeedsCABData which has keys of Outlets and values as
//        // TreeMap<String, TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record>>
//        // where the keys are EnquiryClientBureauOutletIDs and the values are 
//        // CAB_DataRecord2s
//        Iterator<EnquiryClientBureauOutletID> ite;
//        TreeMap<String, TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record>> outletLeedsCABData;
//        outletLeedsCABData = new TreeMap<String, TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record>>();
//        ite = tChapeltownCABData.keySet().iterator();
//        while (ite.hasNext()) {
//            DW_ID_ClientOutletEnquiryID id = ite.next();
//            DW_Data_CAB2_Record aLeedsCABData_DataRecord = tChapeltownCABData.get(id);
//            outlet = aLeedsCABData_DataRecord.getOutlet();
//            if (outletLeedsCABData.containsKey(outlet)) {
//                TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> d;
//                d = outletLeedsCABData.get(outlet);
//                d.put(id, aLeedsCABData_DataRecord);
//            } else {
//                TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> d;
//                d = new TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record>();
//                d.put(id, aLeedsCABData_DataRecord);
//                outletLeedsCABData.put(outlet, d);
//            }
//        }
//
//        Iterator<String> ite2 = outletLeedsCABData.keySet().iterator();
//        while (ite2.hasNext()) {
//            outlet = ite2.next();
//            tChapeltownCABData = outletLeedsCABData.get(outlet);
//            System.out.println("Outlet " + outlet);
////            getAndWriteOutDeprivationClassCounts(count, percentageBand, tLeedsCABData, tDeprivationData,
////                    tLookupFromPostcodeToCensusCodes, classWidth, numberOfClasses);
//            Object[] deprivationClassCountOfCABClientsETC;
//            deprivationClassCountOfCABClientsETC = getAndWriteOutDeprivationClassCounts(
//                    tChapeltownCABData, tDeprivationData, tLookupFromPostcodeToCensusCodes, deprivationClasses);
//            result.put(outlet, deprivationClassCountOfCABClientsETC);
//        }
//
//        //close_OutputTextFiles();
//        return result;
//    }
    /**
     * Load LeedsCABData.
     *
     * @param filename
     * @param IDType
     * @param tCAB_DataRecord2_Handler
     * @return
     */
    public TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> loadLeedsCABData(
            String filename,
            Object IDType,
            DW_Data_CAB2_Handler tCAB_DataRecord2_Handler) {
        File dir = new File(
                files.getGeneratedAdviceLeedsDir(),
                "LeedsCAB");
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> result;
        result = tCAB_DataRecord2_Handler.loadInputData(
                dir, filename, IDType);
//        System.out.println(LeedsCABData.firstKey());
//        System.out.println(LeedsCABData.firstEntry().getValue());
        int countRecordsWithNoPostcode = 0;
        Iterator<DW_ID_ClientID> ite;
        ite = result.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_CAB2_Record aCABData_DataRecord = result.get(id);
            String postcode = aCABData_DataRecord.getPostcode();
            if (postcode.isEmpty()) {
                countRecordsWithNoPostcode++;
            }
        }
        //System.out.println(name);
        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
        System.out.println("countRecordsWithPostcode " + (result.size() - countRecordsWithNoPostcode));
        return result;
    }

    /**
     * Load LeedsCABData.
     *
     * @param filename
     * @param handler
     * @param IDType
     * @return
     */
    public TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> loadLCC_WRUData(
            String filename,
            DW_Data_LCC_WRU_Handler handler,
            Object IDType) {
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> result;
//        File dir = new File(
//                DW_Files.getGeneratedAdviceLeedsDir(),
//                "LCC - Welfare Rights Unit");
        File dir = new File(
                files.getInputAdviceLeedsDir(),
                "LCC_WRU");
        result = handler.loadInputData(
                dir, filename, IDType);
//        System.out.println(LeedsCABData.firstKey());
//        System.out.println(LeedsCABData.firstEntry().getValue());
        int countRecordsWithNoPostcode = 0;
        Iterator<DW_ID_ClientID> ite;
        ite = result.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_LCC_WRU_Record rec = result.get(id);
            String postcode = rec.getPostcode();
            if (postcode.isEmpty()) {
                countRecordsWithNoPostcode++;
            }
        }
        //System.out.println(name);
        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
        System.out.println("countRecordsWithPostcode " + (result.size() - countRecordsWithNoPostcode));
        return result;
    }

    /**
     * Load LeedsCABData.
     *
     * @param env
     * @param filename
     * @param tCAB_DataRecord2_Handler
     * @param IDType
     * @return
     */
    public static TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> loadLeedsCABData(
            DW_Environment env,
            String filename,
            DW_Data_CAB2_Handler tCAB_DataRecord2_Handler,
            Object IDType) {
        File dir = new File(                env.files.getGeneratedAdviceLeedsDir(),                "LeedsCAB");
        TreeMap<DW_ID_ClientID, DW_Data_CAB2_Record> result;
        result = tCAB_DataRecord2_Handler.loadInputData(
                dir, filename, IDType);
//        System.out.println(LeedsCABData.firstKey());
//        System.out.println(LeedsCABData.firstEntry().getValue());
        int countRecordsWithNoPostcode = 0;
        Iterator<DW_ID_ClientID> ite;
        ite = result.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id = ite.next();
            DW_Data_CAB2_Record aCABData_DataRecord = result.get(id);
            String postcode = aCABData_DataRecord.getPostcode();
            if (postcode.isEmpty()) {
                countRecordsWithNoPostcode++;
            }
        }
        //System.out.println(name);
        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
        System.out.println("countRecordsWithPostcode " + (result.size() - countRecordsWithNoPostcode));
        return result;
    }

    /**
     * Load LeedsCABData.
     *
     * @param filename
     * @param tCAB_DataRecord0_Handler
     * @param IDType
     * @return
     */
    //public TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> loadChapeltownCABData(
    public TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> loadChapeltownCABData(
            String filename,
            DW_Data_CAB0_Handler tCAB_DataRecord0_Handler,
            Object IDType) {
        File dir = new File(
                files.getGeneratedAdviceLeedsDir(),
                "ChapeltownCAB");
        //TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> result;
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> result;
        int type = 0;
        result = tCAB_DataRecord0_Handler.loadInputData(
                type,
                filename,
                IDType);
//        System.out.println(LeedsCABData.firstKey());
//        System.out.println(LeedsCABData.firstEntry().getValue());
        int countRecordsWithNoPostcode = 0;
        //Iterator<EnquiryClientBureauOutletID> ite;
        Iterator<DW_ID_ClientID> ite;
        ite = result.keySet().iterator();
        while (ite.hasNext()) {
            DW_ID_ClientID id;
            id = ite.next();
            DW_Data_CAB0_Record aCABData_DataRecord;
            aCABData_DataRecord = result.get(id);
            String postcode = aCABData_DataRecord.getPostcode();
            if (postcode.isEmpty()) {
                countRecordsWithNoPostcode++;
            }
        }
        //System.out.println(name);
        System.out.println("countRecordsWithNoPostcode " + countRecordsWithNoPostcode);
        System.out.println("countRecordsWithPostcode " + (result.size() - countRecordsWithNoPostcode));
        return result;
    }

//    /**
//     *
//     * @param count
//     * @param percentageBand
//     * @param tCABData
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @param classWidth
//     * @param numberOfClasses
//     */
//    @Deprecated
//    public void getAndWriteOutDeprivationClassCounts(
//            int count,
//            int percentageBand,
//            TreeMap<String, DW_Data_CAB2_Record> tCABData,
//            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
//            TreeMap<String, String[]> tLookupFromPostcodeToCensusCodes,
//            int classWidth,
//            int numberOfClasses) {
//        Object[] deprivationClassCountOfCABClientsETC;
//        deprivationClassCountOfCABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                count,
//                percentageBand,
//                tCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                classWidth,
//                numberOfClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfCABClients;
//        deprivationClassCountOfCABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfCABClientsETC[0];
//
//        Iterator<Integer> ite2 = deprivationClassCountOfCABClients.keySet().iterator();
//        System.out.println("DeprivationClass,1213");
//        Integer previousDeprivationClass = 0;
//        while (ite2.hasNext()) {
//            Integer deprivationClass = ite2.next();
//            Integer countOfCABClients = deprivationClassCountOfCABClients.get(deprivationClass);
//            if (ite2.hasNext()) {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to " + deprivationClass.toString() + "% deprived LSOAs"
//                        + "," + countOfCABClients);
//            } else {
//                System.out.println("Clients in the " + previousDeprivationClass.toString() + "% to 100% deprived LSOAs"
//                        + "," + countOfCABClients);
//            }
//            previousDeprivationClass = deprivationClass;
//        }
//        System.out.println("Clients without a recognised postcode"
//                + "," + ((Integer) deprivationClassCountOfCABClientsETC[1]).toString()
//        );
//    }
//    /**
//     * Gets and Writes out Deprivation Class Counts
//     *
//     * @param tCABData
//     * @param deprivationClasses
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @return Object[2] result where: result[0] =
//     * deprivationClassCountOfCABClients is a TreeMap<Integer, Integer> where
//     * the keys are deprivationClasses and the values are counts of the number
//     * of clients in each deprivation class; result[1] =
//     * clientsWithoutARecognisedPostcode is an integer count of the number of
//     * client records that do not have a recognised postcode value.
//     */
//    public Object[] getAndWriteOutDeprivationClassCounts(
//            TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> tCABData,
//            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
//            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
//            TreeMap<Integer, Integer> deprivationClasses) {
//        Object[] deprivationClassCountOfCABClientsETC;
//        deprivationClassCountOfCABClientsETC = Census_DeprivationDataHandler.getDeprivationClassCountOfCABClients(
//                tCABData,
//                tDeprivationData,
//                tLookupFromPostcodeToCensusCodes,
//                deprivationClasses);
//        TreeMap<Integer, Integer> deprivationClassCountOfCABClients;
//        deprivationClassCountOfCABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfCABClientsETC[0];
//
//        Iterator<Integer> ite2 = deprivationClasses.keySet().iterator();
//
//        //Iterator<Integer> ite2 = deprivationClassCountOfCABClients.keySet().iterator();
//        System.out.println("DeprivationClass,1213");
//        Integer previousDeprivationClass = 0;
//        while (ite2.hasNext()) {
//            Integer deprivationClassKey = ite2.next();
//            Integer deprivationClassValue = deprivationClasses.get(deprivationClassKey);
//            Integer countOfCABClients = deprivationClassCountOfCABClients.get(deprivationClassValue);
//            if (countOfCABClients == null) {
//                System.out.println(
//                        "Clients in the " + previousDeprivationClass.toString()
//                        + "% to " + deprivationClassValue.toString() + "% deprived LSOAs"
//                        + ",0");
//            } else {
//                System.out.println(
//                        "Clients in the " + previousDeprivationClass.toString()
//                        + "% to " + deprivationClassValue.toString() + "% deprived LSOAs"
//                        + "," + countOfCABClients);
//            }
//            previousDeprivationClass = deprivationClassValue;
//        }
//        System.out.println("Clients without a recognised postcode"
//                + "," + ((Integer) deprivationClassCountOfCABClientsETC[1]).toString()
//        );
//        return deprivationClassCountOfCABClientsETC;
//    }
    /**
     * Gets and Writes out Deprivation Class Counts
     *
     * @param type
     * @param tCABData
     * @param deprivationClasses
     * @param tDeprivationData
     * @param tLookupFromPostcodeToCensusCodes
     * @return Object[2] result where: result[0] =
     * deprivationClassCountOfCABClients is a TreeMap<Integer, Integer> where
     * the keys are deprivationClasses and the values are counts of the number
     * of clients in each deprivation class; result[1] =
     * clientsWithoutARecognisedPostcode is an integer count of the number of
     * client records that do not have a recognised postcode value.
     */
    public Object[] getAndWriteOutDeprivationClassCounts(
            int type,
            TreeMap tCABData,
            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            TreeMap<Integer, Integer> deprivationClasses) {
        Object[] deprivationClassCountOfCABClientsETC;
        deprivationClassCountOfCABClientsETC = Deprivation_DataHandler.getDeprivationClassCountOfCABClients(
                type,
                tCABData,
                tDeprivationData,
                tLookupFromPostcodeToCensusCodes,
                deprivationClasses);

        TreeMap<Integer, Integer> deprivationClassCountOfCABClients;
        deprivationClassCountOfCABClients = (TreeMap<Integer, Integer>) deprivationClassCountOfCABClientsETC[0];

        Iterator<Integer> ite2 = deprivationClasses.keySet().iterator();

        //Iterator<Integer> ite2 = deprivationClassCountOfCABClients.keySet().iterator();
        System.out.println("DeprivationClass, 1213");
        Integer previousDeprivationClass = 0;
        while (ite2.hasNext()) {
            Integer deprivationClassKey = ite2.next();
            Integer deprivationClassValue = deprivationClasses.get(deprivationClassKey);
            Integer countOfCABClients = deprivationClassCountOfCABClients.get(deprivationClassValue);
            if (countOfCABClients == null) {
                System.out.println(
                        "Count in the " + previousDeprivationClass.toString()
                        + "% to " + deprivationClassValue.toString() + "% deprived LSOAs"
                        + ", 0");
            } else {
                System.out.println(
                        "Count in the " + previousDeprivationClass.toString()
                        + "% to " + deprivationClassValue.toString() + "% deprived LSOAs"
                        + ", " + countOfCABClients);
            }
            previousDeprivationClass = deprivationClassValue;
        }
        System.out.println("Count without a recognised postcode"
                + ", " + ((Integer) deprivationClassCountOfCABClientsETC[1]).toString()
        );
        return deprivationClassCountOfCABClientsETC;
    }

    /**
     *
     * @param count
     * @param tChapeltownCABData
     * @param tDeprivationData
     * @param tLookupFromPostcodeToCensusCodes
     * @param percentageBand
     * @param classWidth
     * @param numberOfClasses
     * @return
     */
    @Deprecated
    protected Object[] getDeprivationClassCountOfCABClients(
            int count,
            TreeMap<String, DW_Data_CAB0_Record> tChapeltownCABData,
            TreeMap<String, Census_DeprivationDataRecord> tDeprivationData,
            TreeMap<String, String[]> tLookupFromPostcodeToCensusCodes,
            int percentageBand,
            int classWidth,
            int numberOfClasses) {
        Object[] result = new Object[2];
        int clientsWithoutARecognisedPostcode = 0;
        TreeMap<Integer, Integer> deprivationClassCountOfCABClients = new TreeMap<>();
        Iterator<String> ite = tChapeltownCABData.keySet().iterator();
        while (ite.hasNext()) {
            String clientProfileID = ite.next();
            DW_Data_CAB0_Record aLeedsCABData_DataRecord = tChapeltownCABData.get(clientProfileID);
            String postcode = aLeedsCABData_DataRecord.getPostcode();
            postcode = Postcode_Handler.formatPostcode(postcode);
            if (!postcode.isEmpty()) {
                String[] codes = tLookupFromPostcodeToCensusCodes.get(postcode);
                if (codes == null) {
//                    System.out.println("Unrecognised postcode " + postcode);
                    clientsWithoutARecognisedPostcode++;
                } else {
                    String SOACode = codes[1];
                    Census_DeprivationDataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
                    if (aDeprivation_DataRecord != null) {
                        Integer deprivationClass = Census_DeprivationDataHandler.getDeprivationClass(
                                count,
                                percentageBand,
                                classWidth,
                                numberOfClasses,
                                aDeprivation_DataRecord);
                        if (deprivationClassCountOfCABClients.containsKey(deprivationClass)) {
                            Integer classCount = deprivationClassCountOfCABClients.get(deprivationClass);
                            Integer newClassCount = classCount + 1;
                            deprivationClassCountOfCABClients.put(deprivationClass, newClassCount);
                        } else {
                            deprivationClassCountOfCABClients.put(deprivationClass, 1);
                        }
                    } else {
                        int debug = 1;
                    }
                }
            } else {
                int debug = 1;
                clientsWithoutARecognisedPostcode++;
            }
        }
        result[0] = deprivationClassCountOfCABClients;
        result[1] = clientsWithoutARecognisedPostcode;
        return result;
    }

//    // most deprived areas are those with highest IMDSCore and IMDRank  
//    public Integer getDeprivationClass(
//            int count,
//            int percentageBand,
//            int classWidth,
//            int numberOfClasses,
//            Census_DeprivationDataRecord aDeprivation_DataRecord) {
//        String rankOfIMDScoreForEngland = aDeprivation_DataRecord.getRankOfIMDScoreForEngland();
//        Integer rankOfIMDScoreForEnglandInteger;
//        int thisRank = 0;
//        if (rankOfIMDScoreForEngland != null) {
//            rankOfIMDScoreForEnglandInteger = Integer.valueOf(rankOfIMDScoreForEngland);
//            thisRank = rankOfIMDScoreForEnglandInteger.intValue();
//        } else {
//            int debug = 1;
//        }
//        int rank = count;
//        Integer DeprivationClass = percentageBand;
//        for (int i = 0; i < numberOfClasses; i++) {
//            rank -= classWidth;
//            if (thisRank >= rank) {
//                return DeprivationClass;
//            }
//            DeprivationClass += percentageBand;
//        }
//        return DeprivationClass;
//    }
    public void initData_CAB0_Handler() {
        Data_CAB0_Handler = new DW_Data_CAB0_Handler(env);
    }

    public void initData_CAB1_Handler() {
        Data_CAB1_Handler = new DW_Data_CAB1_Handler(env);
    }

    public void initData_CAB2_Handler() {
        Data_CAB2_Handler = new DW_Data_CAB2_Handler(env);
    }

    public void initData_LCC_WRU_Handler() {
        Data_LCC_WRU_Handler = new DW_Data_LCC_WRU_Handler(env);
    }

    public static TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> getLCC_WRUData(
            DW_Data_LCC_WRU_Handler h,
            Object IDType) {
        TreeMap<DW_ID_ClientID, DW_Data_LCC_WRU_Record> result;
        String filename = "WelfareRights - Data Extract.csv";
        result = h.loadInputData(
                filename, IDType);
        return result;
    }

    public static TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> getChapeltownCABData(
            DW_Data_CAB0_Handler tCAB_DataRecord0_Handler,
            Object IDType) {
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> result;
        String Q1Filename = "Q1.csv";
        String Q2Filename = "Q2.csv";
        String Q3Filename = "Q3.csv";
        String Q4Filename = "Q4.csv";
        int type = 0;
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tQ1ChapeltownCABData;
        tQ1ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData(
                type, Q1Filename, IDType);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tQ2ChapeltownCABData;
        tQ2ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData(
                type, Q2Filename, IDType);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tQ3ChapeltownCABData;
        tQ3ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData(
                type, Q3Filename, IDType);
        TreeMap<DW_ID_ClientID, DW_Data_CAB0_Record> tQ4ChapeltownCABData;
        tQ4ChapeltownCABData = tCAB_DataRecord0_Handler.loadInputData(
                type, Q4Filename, IDType);
        result = new TreeMap<>();
        result.putAll(tQ1ChapeltownCABData);
        result.putAll(tQ2ChapeltownCABData);
        result.putAll(tQ3ChapeltownCABData);
        result.putAll(tQ4ChapeltownCABData);
        return result;
    }

}
