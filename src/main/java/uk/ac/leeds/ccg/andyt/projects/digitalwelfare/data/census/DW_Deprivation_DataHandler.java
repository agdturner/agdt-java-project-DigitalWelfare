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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.census;

import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataHandler;
import uk.ac.leeds.ccg.andyt.agdtcensus.Deprivation_DataRecord;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB0_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_Data_CAB2_Record;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds.DW_ID_ClientID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_Processor;

/**
 *
 * @author geoagdt
 */
public class DW_Deprivation_DataHandler extends Deprivation_DataHandler {

    /**
     *
     * @param type
     * @param tCABData
     * @param tDeprivationData
     * @param tLookupFromPostcodeToCensusCodes
     * @param deprivationClasses
     * @return
     */
    public static Object[] getDeprivationClassCountOfCABClients(
            int type,
            TreeMap tCABData,
            TreeMap<String, Deprivation_DataRecord> tDeprivationData,
            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
            TreeMap<Integer, Integer> deprivationClasses) {
        Object[] result = new Object[2];
        int recsWithoutARecognisedPostcode = 0;
        TreeMap<Integer, Integer> deprivationClassCountOfCABClients = new TreeMap<Integer, Integer>();
        if (type == 0) {
            Iterator<DW_ID_ClientID> ite;
            ite = tCABData.keySet().iterator();
            while (ite.hasNext()) {
                DW_ID_ClientID id = ite.next();
                DW_Data_CAB2_Record aLeedsCABData_DataRecord = (DW_Data_CAB2_Record) tCABData.get(id);
                String postcode = aLeedsCABData_DataRecord.getPostcode();
                postcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
                if (!postcode.isEmpty()) {
                    String SOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
                    if (SOACode == null) {
                        recsWithoutARecognisedPostcode++;
                    } else {
                        Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
                        if (aDeprivation_DataRecord != null) {
                            Integer deprivationClass = getDeprivationClass(deprivationClasses, aDeprivation_DataRecord);
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
                    recsWithoutARecognisedPostcode++;
                }
            }
        } else {
            Iterator<DW_ID_ClientID> ite;
            ite = tCABData.keySet().iterator();
            while (ite.hasNext()) {
                DW_ID_ClientID id = ite.next();
                DW_Data_CAB0_Record aLeedsCABData_DataRecord = (DW_Data_CAB0_Record) tCABData.get(id);
                String postcode = aLeedsCABData_DataRecord.getPostcode();
                postcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
                if (!postcode.isEmpty()) {
                    String SOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
                    if (SOACode == null) {
                        recsWithoutARecognisedPostcode++;
                    } else {
                        Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
                        if (aDeprivation_DataRecord != null) {
                            Integer deprivationClass = getDeprivationClass(deprivationClasses, aDeprivation_DataRecord);
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
                    recsWithoutARecognisedPostcode++;
                }
            }
        }
        result[0] = deprivationClassCountOfCABClients;
        result[1] = recsWithoutARecognisedPostcode;
        return result;
    }

//    /**
//     *
//     * @param tLeedsCABData
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @param deprivationClasses
//     * @return Object[2] result where: result[0] =
//     * deprivationClassCountOfCABClients is a TreeMap<Integer, Integer> where
//     * the keys are deprivationClasses and the values are counts of the number
//     * of clients in each deprivation class; result[1] =
//     * clientsWithoutARecognisedPostcode is an integer count of the number of
//     * client records that do not have a recognised postcode value.
//     */
//    public static Object[] getDeprivationClassCountOfCABClients(
//            TreeMap<EnquiryClientBureauOutletID, DW_Data_CAB2_Record> tLeedsCABData,
//            TreeMap<String, Deprivation_DataRecord> tDeprivationData,
//            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
//            TreeMap<Integer, Integer> deprivationClasses) {
//        Object[] result = new Object[2];
//        int clientsWithoutARecognisedPostcode = 0;
//        TreeMap<Integer, Integer> deprivationClassCountOfCABClients;
//        deprivationClassCountOfCABClients = new TreeMap<Integer, Integer>();
//        Iterator<EnquiryClientBureauOutletID> ite;
//        ite = tLeedsCABData.keySet().iterator();
//        while (ite.hasNext()) {
//            DW_ID_ClientOutletEnquiryID id = ite.next();
//            DW_Data_CAB2_Record aLeedsCABData_DataRecord = tLeedsCABData.get(id);
//            String postcode = aLeedsCABData_DataRecord.getPostcode();
//            postcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
//            if (!postcode.isEmpty()) {
//                String SOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
//                if (SOACode == null) {
//                    clientsWithoutARecognisedPostcode++;
//                } else {
//                    Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
//                    if (aDeprivation_DataRecord != null) {
//                        Integer deprivationClass = getDeprivationClass(deprivationClasses, aDeprivation_DataRecord);
//                        if (deprivationClassCountOfCABClients.containsKey(deprivationClass)) {
//                            Integer classCount = deprivationClassCountOfCABClients.get(deprivationClass);
//                            Integer newClassCount = classCount + 1;
//                            deprivationClassCountOfCABClients.put(deprivationClass, newClassCount);
//                        } else {
//                            deprivationClassCountOfCABClients.put(deprivationClass, 1);
//                        }
//                    } else {
//                        int debug = 1;
//                    }
//                }
//            } else {
//                int debug = 1;
//                clientsWithoutARecognisedPostcode++;
//            }
//        }
//        result[0] = deprivationClassCountOfCABClients;
//        result[1] = clientsWithoutARecognisedPostcode;
//        return result;
//    }
//
//    /**
//     *
//     * @param count
//     * @param percentageBand
//     * @param tLeedsCABData
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @param classWidth
//     * @param numberOfClasses
//     * @return Object[] result where: ------------------------------------------
//     * result[0] = deprivationClassCountOfCABClients; // TreeMap<Integer,
//     * Integer> // Key = Class; value = count ----------------------------------
//     * result[1] = clientsWithoutARecognisedPostcode; // An int count ----------
//     */
//    public static Object[] getDeprivationClassCountOfCABClients(
//            int count,
//            int percentageBand,
//            TreeMap<String, DW_Data_CAB2_Record> tLeedsCABData,
//            TreeMap<String, Deprivation_DataRecord> tDeprivationData,
//            TreeMap<String, String[]> tLookupFromPostcodeToCensusCodes,
//            int classWidth,
//            int numberOfClasses) {
//        Object[] result = new Object[2];
//        int clientsWithoutARecognisedPostcode = 0;
//        TreeMap<Integer, Integer> deprivationClassCountOfCABClients = new TreeMap<Integer, Integer>();
//        Iterator<String> ite = tLeedsCABData.keySet().iterator();
//        while (ite.hasNext()) {
//            String clientProfileID = ite.next();
//            DW_Data_CAB2_Record aLeedsCABData_DataRecord = tLeedsCABData.get(clientProfileID);
//            String postcode = aLeedsCABData_DataRecord.getPostcode();
//            postcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
//            if (!postcode.isEmpty()) {
//                String[] codes = tLookupFromPostcodeToCensusCodes.get(postcode);
//                if (codes == null) {
//                    clientsWithoutARecognisedPostcode++;
//                } else {
//                    String SOACode = codes[1];
//                    Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
//                    if (aDeprivation_DataRecord != null) {
//                        Integer deprivationClass = getDeprivationClass(count, percentageBand, classWidth, numberOfClasses, aDeprivation_DataRecord);
//                        if (deprivationClassCountOfCABClients.containsKey(deprivationClass)) {
//                            Integer classCount = deprivationClassCountOfCABClients.get(deprivationClass);
//                            Integer newClassCount = classCount + 1;
//                            deprivationClassCountOfCABClients.put(deprivationClass, newClassCount);
//                        } else {
//                            deprivationClassCountOfCABClients.put(deprivationClass, 1);
//                        }
//                    } else {
//                        int debug = 1;
//                    }
//                }
//            } else {
//                int debug = 1;
//                clientsWithoutARecognisedPostcode++;
//            }
//        }
//        result[0] = deprivationClassCountOfCABClients;
//        result[1] = clientsWithoutARecognisedPostcode;
//        return result;
//    }
//
//    /**
//     *
//     * @param tLeedsCABData
//     * @param tDeprivationData
//     * @param tLookupFromPostcodeToCensusCodes
//     * @param count
//     * @param percentageBand
//     * @param classWidth
//     * @param numberOfClasses
//     * @return Object[] result where: ------------------------------------------
//     * result[0] = deprivationClassCountOfCABClients; // TreeMap<Integer,
//     * Integer> // Key = Class; value = count ----------------------------------
//     * result[1] = clientsWithoutARecognisedPostcode; // An int count ----------
//     */
//    public static Object[] getDeprivationClassCountOfCABClients(
//            TreeMap<String, DW_Data_CAB1_Record> tLeedsCABData,
//            TreeMap<String, Deprivation_DataRecord> tDeprivationData,
//            TreeMap<String, String> tLookupFromPostcodeToCensusCodes,
//            int count,
//            int percentageBand,
//            int classWidth,
//            int numberOfClasses) {
//        Object[] result = new Object[2];
//        int clientsWithoutARecognisedPostcode = 0;
//        TreeMap<Integer, Integer> deprivationClassCountOfCABClients = new TreeMap<Integer, Integer>();
//        Iterator<String> ite = tLeedsCABData.keySet().iterator();
//        while (ite.hasNext()) {
//            String clientProfileID = ite.next();
//            DW_Data_CAB1_Record aLeedsCABData_DataRecord = tLeedsCABData.get(clientProfileID);
//            String postcode = aLeedsCABData_DataRecord.getPostalCode();
//            postcode = DW_Processor.formatPostcodeForONSPDLookup(postcode);
//            if (!postcode.isEmpty()) {
//                String SOACode = tLookupFromPostcodeToCensusCodes.get(postcode);
//                if (SOACode == null) {
//                    clientsWithoutARecognisedPostcode++;
//                } else {
//                    Deprivation_DataRecord aDeprivation_DataRecord = tDeprivationData.get(SOACode);
//                    if (aDeprivation_DataRecord != null) {
//                        Integer deprivationClass = getDeprivationClass(count, percentageBand, classWidth, numberOfClasses, aDeprivation_DataRecord);
//                        if (deprivationClassCountOfCABClients.containsKey(deprivationClass)) {
//                            Integer classCount = deprivationClassCountOfCABClients.get(deprivationClass);
//                            Integer newClassCount = classCount + 1;
//                            deprivationClassCountOfCABClients.put(deprivationClass, newClassCount);
//                        } else {
//                            deprivationClassCountOfCABClients.put(deprivationClass, 1);
//                        }
//                    } else {
//                        int debug = 1;
//                    }
//                }
//            } else {
//                int debug = 1;
//                clientsWithoutARecognisedPostcode++;
//            }
//        }
//        result[0] = deprivationClassCountOfCABClients;
//        result[1] = clientsWithoutARecognisedPostcode;
//        return result;
//    }

    // most deprived areas are those with highest IMDSCore and IMDRank
    public static Integer getDeprivationClass(
            int count,
            int percentageBand,
            int classWidth,
            int numberOfClasses,
            Deprivation_DataRecord aDeprivation_DataRecord) {
        String rankOfIMDScoreForEngland = aDeprivation_DataRecord.getRankOfIMDScoreForEngland();
        Integer rankOfIMDScoreForEnglandInteger;
        int thisRank = 0;
        if (rankOfIMDScoreForEngland != null) {
            rankOfIMDScoreForEnglandInteger = Integer.valueOf(rankOfIMDScoreForEngland);
            thisRank = rankOfIMDScoreForEnglandInteger;
        } else {
            int debug = 1;
        }
        int rank = 0;
        Integer DeprivationClass = percentageBand;
        for (int i = 0; i < numberOfClasses; i++) {
            rank += classWidth;
            if (thisRank <= rank) {
                return DeprivationClass;
            }
            DeprivationClass += percentageBand;
        }
        return DeprivationClass;
    }
}
