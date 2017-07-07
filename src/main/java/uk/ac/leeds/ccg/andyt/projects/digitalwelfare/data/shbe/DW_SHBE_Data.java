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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.shbe;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.DW_CorrectedPostcodes;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.utlis.DW_Collections;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Data extends DW_Object {

    /**
     * For convenience
     */
    transient DW_Postcode_Handler DW_Postcode_Handler;
    transient DW_Files DW_Files;
    transient DW_Strings DW_Strings;

    /**
     * A reference to all the Data for this Payment Type. The keys are YM3 and
     * the values are the respective collection.
     */
    protected HashMap<String, DW_SHBE_Records> Data;

    /**
     * File for storing Data
     */
    private File DataFile;

    /**
     * ClaimRef to ClaimRef DW_ID Lookup.
     */
    private HashMap<String, DW_ID> ClaimRefToClaimRefIDLookup;

    /**
     * ClaimRef DW_ID to ClaimRef Lookup.
     */
    private HashMap<DW_ID, String> ClaimRefIDToClaimRefLookup;

    private DW_CorrectedPostcodes CorrectedPostcodes;
    
    /**
     * National Insurance Number to NINO DW_ID Lookup.
     */
    private HashMap<String, DW_ID> NINOToNINOIDLookup;

    /**
     * NINO DW_ID to National Insurance Number Lookup.
     */
    private HashMap<DW_ID, String> NINOIDToNINOLookup;

    /**
     * Date Of Birth to DoB DW_ID Lookup.
     */
    private HashMap<String, DW_ID> DOBToDOBIDLookup;

    /**
     * DoB DW_ID to Date Of Birth Lookup.
     */
    private HashMap<DW_ID, String> DOBIDToDOBLookup;

    /**
     * DW_PersonID of Claimants
     */
    HashSet<DW_PersonID> ClaimantPersonIDs;

    /**
     * DW_PersonID of Partners
     */
    HashSet<DW_PersonID> PartnerPersonIDs;

    /**
     * DW_PersonID of Non-Dependents
     */
    HashSet<DW_PersonID> NonDependentPersonIDs;

    /**
     * DW_PersonID to ClaimRefIDs Lookup
     */
    HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimRefIDsLookup;

    /**
     * Postcode to Postcode DW_ID Lookup.
     */
    private HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;

    /**
     * Postcode DW_ID to Postcode Lookup.
     */
    private HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;

    /**
     * Postcode DW_ID to AGDT_Point Lookups. There is a different one for each
     * ONSPD File. The keys are Nearest YM3s for the respective ONSPD File.
     */
    private HashMap<String, HashMap<DW_ID, AGDT_Point>> PostcodeIDToPointLookups;

    /**
     * ClaimRefToClaimRefIDLookup File.
     */
    private File ClaimRefToClaimRefIDLookupFile;

    /**
     * ClaimRefIDToClaimRefLookupFile File.
     */
    private File ClaimRefIDToClaimRefLookupFile;

    /**
     * NINOIDToNINOLookup File.
     */
    private File NINOIDToNINOLookupFile;

    /**
     * IDToDOBLookup File.
     */
    private File DOBIDToDOBLookupFile;

    /**
     * ClaimantPersonIDs File.
     */
    private File ClaimantPersonIDsFile;

    /**
     * PartnerPersonIDs File.
     */
    private File PartnerPersonIDsFile;

    /**
     * NonDependentPersonIDs File.
     */
    private File NonDependentPersonIDsFile;

    /**
     * PersonIDToClaimRefIDsLookup File.
     */
    private File PersonIDToClaimRefIDsLookupFile;

    /**
     * CorrectedPostcodes File.
     */
    private File CorrectedPostcodesFile;
            
    /**
     * NINOToIDLookup File.
     */
    private File NINOToNINOIDLookupFile;

    /**
     * DOBToDW_IDLookup File.
     */
    private File DOBToDOBIDLookupFile;

    /**
     * PostcodeToPostcodeIDLookup File.
     */
    private File PostcodeToPostcodeIDLookupFile;

    /**
     * PostcodeIDToPostcodeLookup File.
     */
    private File PostcodeIDToPostcodeLookupFile;

    /**
     * PostcodeIDToPointLookup File.
     */
    private File PostcodeIDToPointLookupsFile;

    public DW_SHBE_Data() {
    }

    public DW_SHBE_Data(DW_Environment env) {
        super(env);
        DW_Postcode_Handler = env.getDW_Postcode_Handler();
        DW_Files = env.getDW_Files();
        DW_Strings = env.getDW_Strings();
    }

    /**
     * {@code
     * if (Data == null) {
     * if (f.exists()) {
     * Data = (HashMap<String, DW_SHBE_Records>) Generic_StaticIO.readObject(f);
     * } else {
     * Data = new HashMap<String, DW_SHBE_Records>(); } } return Data; }
     *
     * @param f
     * @return
     */
    public HashMap<String, DW_SHBE_Records> getData(File f) {
        if (Data == null) {
            if (f.exists()) {
                Data = (HashMap<String, DW_SHBE_Records>) Generic_StaticIO.readObject(f);
            } else {
                Data = new HashMap<String, DW_SHBE_Records>();
            }
        }
        return Data;
    }

    /**
     * {@code
     * DataFile = getDataFile();
     * return getData(DataFile);
     * }
     *
     * @return
     */
    public HashMap<String, DW_SHBE_Records> getData() {
        DataFile = getDataFile();
        return getData(DataFile);
    }

    /**
     * If DataFile is null, initialise it.
     *
     * @return DataFile
     */
    public final File getDataFile() {
        if (DataFile == null) {
            String filename = "Data_HashMap_String__DW_SHBE_Records"
                    + DW_Strings.sBinaryFileExtension;
            DataFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return DataFile;
    }

    /**
     * If ClaimRefToClaimRefIDLookup is null initialise it.
     *
     * @param f
     * @return ClaimRefToClaimRefIDLookup
     */
    public HashMap<String, DW_ID> getClaimRefToClaimRefIDLookup(File f) {
        if (ClaimRefToClaimRefIDLookup == null) {
            ClaimRefToClaimRefIDLookup = getStringToIDLookup(f);
        }
        return ClaimRefToClaimRefIDLookup;
    }

    /**
     * {@code HashMap<String, DW_ID> result;
     * if (f.exists()) {
     * result = (HashMap<String, DW_ID>) Generic_StaticIO.readObject(f);
     * } else {
     * result = new HashMap<String, DW_ID>(); } return result;}
     *
     * @param f
     * @return
     */
    public HashMap<String, DW_ID> getStringToIDLookup(
            File f) {
        HashMap<String, DW_ID> result;
        if (f.exists()) {
            result = (HashMap<String, DW_ID>) Generic_StaticIO.readObject(f);
        } else {
            result = new HashMap<String, DW_ID>();
        }
        return result;
    }

    /**
     * If ClaimRefIDToClaimRefLookup is null initialise it.
     *
     * @param f
     * @return ClaimRefIDToClaimRefLookup
     */
    public HashMap<DW_ID, String> getClaimRefIDToClaimRefLookup(File f) {
        if (ClaimRefIDToClaimRefLookup == null) {
            ClaimRefIDToClaimRefLookup = DW_Collections.getHashMap_DW_ID__String(f);
        }
        return ClaimRefIDToClaimRefLookup;
    }

    /**
     * {@code ClaimRefToClaimRefIDLookupFile = getClaimRefToClaimRefIDLookupFile();
     * return getClaimRefToClaimRefIDLookup(ClaimRefToClaimRefIDLookupFile);}
     *
     * @return
     */
    public HashMap<String, DW_ID> getClaimRefToClaimRefIDLookup() {
        ClaimRefToClaimRefIDLookupFile = getClaimRefToClaimRefIDLookupFile();
        return getClaimRefToClaimRefIDLookup(ClaimRefToClaimRefIDLookupFile);
    }

    /**
     * {@code ClaimRefToClaimRefIDLookupFile = getClaimRefToClaimRefIDLookupFile();
     * return getClaimRefToClaimRefIDLookup(ClaimRefToClaimRefIDLookupFile);}
     *
     * @return
     */
    public HashMap<DW_ID, String> getClaimRefIDToClaimRefLookup() {
        ClaimRefIDToClaimRefLookupFile = getClaimRefIDToClaimRefLookupFile();
        return getClaimRefIDToClaimRefLookup(ClaimRefIDToClaimRefLookupFile);
    }

    public final DW_CorrectedPostcodes getCorrectedPostcodes(
            File f) {
        if (CorrectedPostcodes == null) {
            if (f.exists()) {
                CorrectedPostcodes = (DW_CorrectedPostcodes) Generic_StaticIO.readObject(f);
            } else {
                new DW_CorrectedPostcodes(env).run();
                return getCorrectedPostcodes(f);
            }
        }
        return CorrectedPostcodes;
    }

    public DW_CorrectedPostcodes getCorrectedPostcodes() {
        CorrectedPostcodesFile = getCorrectedPostcodesFile();
        return getCorrectedPostcodes(CorrectedPostcodesFile);
    }
    
    /**
     * {@code if (NINOToDW_IDLookup == null) {
     * NINOToDW_IDLookup = getStringToIDLookup(f);
     * }
     * return NINOToDW_IDLookup;}
     *
     * @param f
     * @return NINOToDW_IDLookup
     */
    public final HashMap<String, DW_ID> getNINOToNINOIDLookup(
            File f) {
        if (NINOToNINOIDLookup == null) {
            NINOToNINOIDLookup = getStringToIDLookup(f);
        }
        return NINOToNINOIDLookup;
    }

    /**
     * {@code NINOToDW_IDLookupFile = getNINOToIDLookupFile();
     * return getNINOToNINOIDLookup(NINOToDW_IDLookupFile);}
     *
     * @return
     */
    public HashMap<String, DW_ID> getNINOToNINOIDLookup() {
        NINOToNINOIDLookupFile = getNINOToNINOIDLookupFile();
        return getNINOToNINOIDLookup(NINOToNINOIDLookupFile);
    }

    /**
     * {@code if (DOBToDOBIDLookup == null) {
     * DOBToDOBIDLookup = getStringToIDLookup(f);
     * }
     * return DOBToDOBIDLookup;}
     *
     * @param f
     * @return
     */
    public final HashMap<String, DW_ID> getDOBToDOBIDLookup(
            File f) {
        if (DOBToDOBIDLookup == null) {
            DOBToDOBIDLookup = getStringToIDLookup(f);
        }
        return DOBToDOBIDLookup;
    }

    /**
     * {@code DOBToDOBIDLookupFile = getDOBToDOBIDLookupFile();
     * return getDOBToDOBIDLookup(DOBToDOBIDLookupFile);}
     *
     * @return
     */
    public HashMap<String, DW_ID> getDOBToDOBIDLookup() {
        DOBToDOBIDLookupFile = getDOBToDOBIDLookupFile();
        return getDOBToDOBIDLookup(DOBToDOBIDLookupFile);
    }

    /**
     * {@code if (NINOIDToNINOLookup == null) {
     * NINOIDToNINOLookup = getHashMap_DW_ID__String(f);
     * }
     * return NINOIDToNINOLookup;}
     *
     * @param f
     * @return
     */
    public final HashMap<DW_ID, String> getNINOIDToNINOLookup(
            File f) {
        if (NINOIDToNINOLookup == null) {
            NINOIDToNINOLookup = DW_Collections.getHashMap_DW_ID__String(f);
        }
        return NINOIDToNINOLookup;
    }

    /**
     * {@code NINOIDToNINOLookupFile = getNINOIDToNINOLookupFile();
     * return getNINOIDToNINOLookup(NINOIDToNINOLookupFile);}
     *
     * @return
     */
    public HashMap<DW_ID, String> getNINOIDToNINOLookup() {
        NINOIDToNINOLookupFile = getNINOIDToNINOLookupFile();
        return getNINOIDToNINOLookup(NINOIDToNINOLookupFile);
    }

    /**
     * {@code if (DOBIDToDOBLookup == null) {
     * DOBIDToDOBLookup = getHashMap_DW_ID__String(f);
     * }
     * return DOBIDToDOBLookup;}
     *
     * @param f
     * @return
     */
    public final HashMap<DW_ID, String> getDOBIDToDOBLookup(
            File f) {
        if (DOBIDToDOBLookup == null) {
            DOBIDToDOBLookup = DW_Collections.getHashMap_DW_ID__String(f);
        }
        return DOBIDToDOBLookup;
    }

    /**
     * {@code DOBIDToDOBLookupFile = getDOBIDToDOBLookupFile();
     * return getDOBIDToDOBLookup(DOBIDToDOBLookupFile);}
     *
     * @return
     */
    public HashMap<DW_ID, String> getDOBIDToDOBLookup() {
        DOBIDToDOBLookupFile = getDOBIDToDOBLookupFile();
        return getDOBIDToDOBLookup(DOBIDToDOBLookupFile);
    }

    /**
     * @param f
     * @return
     */
    public final HashSet<DW_PersonID> getClaimantPersonIDs(
            File f) {
        if (ClaimantPersonIDs == null) {
            ClaimantPersonIDs = DW_Collections.getHashSet_DW_PersonID(f);
        }
        return ClaimantPersonIDs;
    }

    /**
     * @return
     */
    public HashSet<DW_PersonID> getClaimantPersonIDs() {
        ClaimantPersonIDsFile = getClaimantPersonIDsFile();
        return getClaimantPersonIDs(ClaimantPersonIDsFile);
    }

    /**
     * @param f
     * @return
     */
    public final HashSet<DW_PersonID> getPartnerPersonIDs(
            File f) {
        if (PartnerPersonIDs == null) {
            PartnerPersonIDs = DW_Collections.getHashSet_DW_PersonID(f);
        }
        return PartnerPersonIDs;
    }

    /**
     * @return
     */
    public HashSet<DW_PersonID> getPartnerPersonIDs() {
        PartnerPersonIDsFile = getPartnerPersonIDsFile();
        return getPartnerPersonIDs(PartnerPersonIDsFile);
    }

    /**
     * @param f
     * @return
     */
    public final HashSet<DW_PersonID> getNonDependentPersonIDs(
            File f) {
        if (NonDependentPersonIDs == null) {
            NonDependentPersonIDs = DW_Collections.getHashSet_DW_PersonID(f);
        }
        return NonDependentPersonIDs;
    }

    /**
     * @return
     */
    public HashSet<DW_PersonID> getNonDependentPersonIDs() {
        NonDependentPersonIDsFile = getNonDependentPersonIDsFile();
        return getNonDependentPersonIDs(NonDependentPersonIDsFile);
    }

    /**
     * @param f
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getPersonIDToClaimRefIDsLookup(
            File f) {
        if (PersonIDToClaimRefIDsLookup == null) {
            PersonIDToClaimRefIDsLookup = DW_Collections.getHashMap_DW_PersonID__HashSet_DW_ID(f);
        }
        return PersonIDToClaimRefIDsLookup;
    }
    
    /**
     * All DW_PersonID to ClaimRefIDs Lookup
     */
    public HashMap<DW_PersonID, HashSet<DW_ID>> getPersonIDToClaimRefIDsLookup() {
        PersonIDToClaimRefIDsLookupFile = getPersonIDToClaimRefIDsLookupFile();
        return getPersonIDToClaimRefIDsLookup(PersonIDToClaimRefIDsLookupFile);
    }

    /**
     * All DW_PersonID to ClaimRefIDs Lookup
     */
    HashMap<DW_PersonID, HashSet<DW_ID>> AllPersonIDToClaimRefsLookup;

    /**
     * {@code if (PostcodeToPostcodeIDLookup == null) {
     * PostcodeToPostcodeIDLookup = getStringToIDLookup(f);
     * }
     * return PostcodeToPostcodeIDLookup;}
     *
     * @param f
     * @return
     */
    public final HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup(
            File f) {
        if (PostcodeToPostcodeIDLookup == null) {
            PostcodeToPostcodeIDLookup = getStringToIDLookup(f);
        }
        return PostcodeToPostcodeIDLookup;
    }

    /**
     * {@code PostcodeToPostcodeDWLookupFile = getPostcodeToPostcodeIDLookupFile();
     * return getPostcodeToPostcodeIDLookup(PostcodeToPostcodeDWLookupFile);}
     *
     * @return
     */
    public HashMap<String, DW_ID> getPostcodeToPostcodeIDLookup() {
        PostcodeToPostcodeIDLookupFile = getPostcodeToPostcodeIDLookupFile();
        return getPostcodeToPostcodeIDLookup(PostcodeToPostcodeIDLookupFile);
    }

    /**
     * {@code if (PostcodeIDToPostcodeLookup == null) {
     * PostcodeIDToPostcodeLookup = getHashMap_DW_ID__String(f);
     * }
     * return PostcodeIDToPostcodeLookup;}
     *
     * @param f
     * @return
     */
    public final HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup(
            File f) {
        if (PostcodeIDToPostcodeLookup == null) {
            PostcodeIDToPostcodeLookup = DW_Collections.getHashMap_DW_ID__String(f);
        }
        return PostcodeIDToPostcodeLookup;
    }

    /**
     * {@code if (PostcodeIDToPointLookups == null) {
     * if (f.exists()) {
     * PostcodeIDToPointLookups = (HashMap<String, HashMap<DW_ID, AGDT_Point>>) Generic_StaticIO.readObject(f);
     * } else {
     * PostcodeIDToPointLookups = new HashMap<String, HashMap<DW_ID, AGDT_Point>>();
     * } } return PostcodeIDToPointLookups;}
     *
     * @param f
     * @return
     */
    public final HashMap<String, HashMap<DW_ID, AGDT_Point>> getPostcodeIDToPointLookups(
            File f) {
        if (PostcodeIDToPointLookups == null) {
            if (f.exists()) {
                PostcodeIDToPointLookups = (HashMap<String, HashMap<DW_ID, AGDT_Point>>) Generic_StaticIO.readObject(f);
            } else {
                PostcodeIDToPointLookups = new HashMap<String, HashMap<DW_ID, AGDT_Point>>();
            }
        }
        return PostcodeIDToPointLookups;
    }

    /**
     * {@code PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
     * return getPostcodeIDToPostcodeLookup(PostcodeIDToPostcodeLookupFile);}
     *
     * @return
     */
    public HashMap<DW_ID, String> getPostcodeIDToPostcodeLookup() {
        PostcodeIDToPostcodeLookupFile = getPostcodeIDToPostcodeLookupFile();
        return getPostcodeIDToPostcodeLookup(PostcodeIDToPostcodeLookupFile);
    }

    /**
     * {@code PostcodeIDToAGDT_PointLookupFile = getPostcodeIDToAGDT_PointLookupsFile();
     * return getPostcodeIDToPointLookups(PostcodeIDToAGDT_PointLookupFile);}
     *
     * @return
     */
    public HashMap<String, HashMap<DW_ID, AGDT_Point>> getPostcodeIDToPointLookups() {
        PostcodeIDToPointLookupsFile = getPostcodeIDToPointLookupsFile();
        return getPostcodeIDToPointLookups(PostcodeIDToPointLookupsFile);
    }

    /**
     * {@code PostcodeIDToAGDT_PointLookupFile = getPostcodeIDToAGDT_PointLookupsFile();
     * return getPostcodeIDToPointLookups(PostcodeIDToAGDT_PointLookupFile);}
     *
     * @param YM3
     * @return
     */
    public HashMap<DW_ID, AGDT_Point> getPostcodeIDToPointLookup(String YM3) {
        String NearestYM3ForONSPDLookup;
        NearestYM3ForONSPDLookup = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        HashMap<DW_ID, AGDT_Point> PostcodeIDToPointLookup;
        PostcodeIDToPointLookups = DW_SHBE_Data.this.getPostcodeIDToPointLookups();
        if (PostcodeIDToPointLookups.containsKey(NearestYM3ForONSPDLookup)) {
            PostcodeIDToPointLookup = PostcodeIDToPointLookups.get(NearestYM3ForONSPDLookup);
        } else {
            PostcodeIDToPointLookup = new HashMap<DW_ID, AGDT_Point>();
            PostcodeIDToPointLookups.put(NearestYM3ForONSPDLookup, PostcodeIDToPointLookup);
        }
        return PostcodeIDToPointLookup;
    }

    /**
     * {@code if (ClaimRefToClaimRefIDLookupFile == null) {
     * String filename = "ClaimRefToClaimRefID_HashMap_String__DW_ID"
     * + DW_Strings.sBinaryFileExtension;
     * PostcodeToPostcodeIDLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return ClaimRefToClaimRefIDLookupFile;}
     *
     * @return
     */
    public final File getClaimRefToClaimRefIDLookupFile() {
        if (ClaimRefToClaimRefIDLookupFile == null) {
            String filename = "ClaimRefToClaimRefID_HashMap_String__DW_ID"
                    + DW_Strings.sBinaryFileExtension;
            ClaimRefToClaimRefIDLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return ClaimRefToClaimRefIDLookupFile;
    }

    /**
     * {@code if (ClaimRefIDToClaimRefLookupFile == null) {
     * String filename = "ClaimRefIDToClaimRef_HashMap_DW_ID__String"
     * + DW_Strings.sBinaryFileExtension;
     * ClaimRefIDToClaimRefLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return ClaimRefIDToClaimRefLookupFile;}
     *
     * @return
     */
    public final File getClaimRefIDToClaimRefLookupFile() {
        if (ClaimRefIDToClaimRefLookupFile == null) {
            String filename = "ClaimRefIDToClaimRef_HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension;
            ClaimRefIDToClaimRefLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return ClaimRefIDToClaimRefLookupFile;
    }

    /**
     * {@code if (PostcodeToPostcodeDWLookupFile == null) {
     * String filename = "PostcodeToPostcodeID_HashMap_String__DW_ID"
     * + DW_Strings.class;
     * PostcodeToPostcodeDWLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return PostcodeToPostcodeDWLookupFile;}
     *
     * @return
     */
    public final File getPostcodeToPostcodeIDLookupFile() {
        if (PostcodeToPostcodeIDLookupFile == null) {
            String filename = "PostcodeToPostcodeID_HashMap_String__DW_ID"
                    + DW_Strings.sBinaryFileExtension;
            PostcodeToPostcodeIDLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return PostcodeToPostcodeIDLookupFile;
    }

    /**
     * {@code if (PostcodeIDToPostcodeLookupFile == null) {
     * String filename = "PostcodeIDToPostcode_HashMap_DW_ID__String"
     * + DW_Strings.sBinaryFileExtension;
     * PostcodeIDToPostcodeLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return PostcodeIDToPostcodeLookupFile;}
     *
     * @return
     */
    public final File getPostcodeIDToPostcodeLookupFile() {
        if (PostcodeIDToPostcodeLookupFile == null) {
            String filename = "PostcodeIDToPostcode_HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension;
            PostcodeIDToPostcodeLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return PostcodeIDToPostcodeLookupFile;
    }

    /**
     * {@code if (PostcodeIDToAGDT_PointLookupFile == null) {
     * String filename = "PostcodeIDToAGDT_Point_HashMap_DW_ID__AGDT_Point"
     * + DW_Strings.sBinaryFileExtension;
     * PostcodeIDToAGDT_PointLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return PostcodeIDToAGDT_PointLookupFile;}
     *
     * @return
     */
    public final File getPostcodeIDToPointLookupsFile() {
        if (PostcodeIDToPointLookupsFile == null) {
            String filename = "PostcodeIDToPoint_HashMap_String__HashMap_DW_ID__AGDT_Point"
                    + DW_Strings.sBinaryFileExtension;
            PostcodeIDToPointLookupsFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return PostcodeIDToPointLookupsFile;
    }
    
    public final File getCorrectedPostcodesFile() {
        if (CorrectedPostcodesFile == null) {
            String filename = "DW_CorrectedPostcodes"
                    + DW_Strings.sBinaryFileExtension;
            CorrectedPostcodesFile = new File(
                    DW_Files.getGeneratedLCCDir(),
                    filename);
        }
        return CorrectedPostcodesFile;
    }

    /**
     * {@code if (NINOToNINOIDLookupFile == null) {
     * String filename = "NINOToNINOID_HashMap_String__DW_ID"
     * + DW_Strings.sBinaryFileExtension;
     * NINOToNINOIDLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return NINOToNINOIDLookupFile;}
     *
     * @return
     */
    public final File getNINOToNINOIDLookupFile() {
        if (NINOToNINOIDLookupFile == null) {
            String filename = "NINOToNINOID_HashMap_String__DW_ID"
                    + DW_Strings.sBinaryFileExtension;
            NINOToNINOIDLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return NINOToNINOIDLookupFile;
    }

    /**
     * {@code if (DOBToDW_IDLookupFile == null) {
     * String filename = "DOBToID_HashMap_String__DW_ID"
     * + DW_Strings.sBinaryFileExtension;
     * DOBToDW_IDLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return DOBToDW_IDLookupFile;}
     *
     * @return
     */
    public final File getDOBToDOBIDLookupFile() {
        if (DOBToDOBIDLookupFile == null) {
            String filename = "DOBToDOBID_HashMap_String__DW_ID"
                    + DW_Strings.sBinaryFileExtension;
            DOBToDOBIDLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return DOBToDOBIDLookupFile;
    }

    /**
     * {@code if (NINOIDToNINOLookupFile == null) {
     * String filename = "NINOIDToNINO_HashMap_DW_ID__String"
     * + DW_Strings.sBinaryFileExtension;
     * NINOIDToNINOLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return NINOIDToNINOLookupFile;}
     *
     * @return
     */
    public final File getNINOIDToNINOLookupFile() {
        if (NINOIDToNINOLookupFile == null) {
            String filename = "NINOIDToNINO_HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension;
            NINOIDToNINOLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return NINOIDToNINOLookupFile;
    }

    /**
     * {@code if (DOBIDToDOBLookupFile == null) {
     * String filename = "DOBIDToDOB_HashMap_DW_ID__String"
     * + DW_Strings.sBinaryFileExtension;
     * DOBIDToDOBLookupFile = new File(
     * DW_Files.getGeneratedSHBEDir(),
     * filename);
     * }
     * return DOBIDToDOBLookupFile;}
     *
     * @return
     */
    public final File getDOBIDToDOBLookupFile() {
        if (DOBIDToDOBLookupFile == null) {
            String filename = "DOBIDToDOB_HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension;
            DOBIDToDOBLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return DOBIDToDOBLookupFile;
    }

    public final File getClaimantPersonIDsFile() {
        if (ClaimantPersonIDsFile == null) {
            String filename = "Claimant_HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension;
            ClaimantPersonIDsFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return ClaimantPersonIDsFile;
    }

    public final File getPartnerPersonIDsFile() {
        if (PartnerPersonIDsFile == null) {
            String filename = "Partner_HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension;
            PartnerPersonIDsFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return PartnerPersonIDsFile;
    }

    public final File getNonDependentPersonIDsFile() {
        if (NonDependentPersonIDsFile == null) {
            String filename = "NonDependent_HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension;
            NonDependentPersonIDsFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return NonDependentPersonIDsFile;
    }
    
    public final File getPersonIDToClaimRefIDsLookupFile() {
        if (PersonIDToClaimRefIDsLookupFile == null) {
            String filename = "PersonIDToClaimRefIDsLookup_HashMap_DW_PersonID__HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension;
            PersonIDToClaimRefIDsLookupFile = new File(
                    DW_Files.getGeneratedSHBEDir(),
                    filename);
        }
        return PersonIDToClaimRefIDsLookupFile;
    }

    /**
     * If getData().get(YM3) != null then return it. Otherwise try to load it
     * from file and return it. Failing that return null.
     *
     * @param YM3
     * @return
     */
    public DW_SHBE_Records getDW_SHBE_Records(
            String YM3) {
        DW_SHBE_Records DW_SHBE_Records;
        DW_SHBE_Records = getData().get(YM3);
        if (DW_SHBE_Records == null) {
            File f;
            f = getFile(YM3);
            if (f.exists()) {
                DW_SHBE_Records = (DW_SHBE_Records) Generic_StaticIO.readObject(f);
                return DW_SHBE_Records;
            }
        }
        return DW_SHBE_Records;
    }

    /**
     * {@code return new File(
     * DW_Files.getGeneratedSHBEDir(),
     * YM3);}
     *
     * @param YM3
     * @return
     */
    protected File getDir(String YM3) {
        return new File(
                DW_Files.getGeneratedSHBEDir(),
                YM3);
    }

    /**
     * {@code File result;
     * File dir;
     * dir = getDir(YM3);
     * if (!dir.exists()) {
     * dir.mkdirs();
     * }
     * result = new File(
     * dir,
     * "DW_SHBE_Records"
     * + DW_Strings.sBinaryFileExtension);
     * return result;}
     *
     * @param YM3
     * @return
     */
    protected File getFile(String YM3) {
        File result;
        File dir;
        dir = getDir(YM3);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        result = new File(
                dir,
                "DW_SHBE_Records"
                + DW_Strings.sBinaryFileExtension);
        return result;
    }

    /**
     * Clears all DW_SHBE_Records in Data from fast access memory.
     *
     * @return The number of DW_SHBE_Records cleared.
     */
    public int clearAllCache() {
        int result = 0;
        Iterator<String> ite;
        ite = Data.keySet().iterator();
        String YM3;
        DW_SHBE_Records recs;
        while (ite.hasNext()) {
            YM3 = ite.next();
            recs = Data.get(YM3);
            if (recs != null) {
                recs = null;
                result++;
            }
        }
        return result;
    }

    /**
     * Clears all DW_SHBE_Records in Data from fast access memory with the
     * exception of that for YM3.
     *
     * @param YM3 The Year_Month key of for the DW_SHBE_Records not to be
     * cleared from fast access memory.
     * @return The number of DW_SHBE_Records cleared.
     */
    public int clearAllCacheExcept(String YM3) {
        int result = 0;
        Iterator<String> ite;
        ite = Data.keySet().iterator();
        String aYM3;
        DW_SHBE_Records recs;
        while (ite.hasNext()) {
            aYM3 = ite.next();
            if (!aYM3.equalsIgnoreCase(YM3)) {
                recs = Data.get(YM3);
                if (recs != null) {
                    recs = null;
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Clears some DW_SHBE_Records in Data from fast access memory.
     *
     * @return true iff some DW_SHBE_Records were cleared and false otherwise.
     */
    public boolean clearSomeCache() {
        Iterator<String> ite;
        ite = Data.keySet().iterator();
        String YM3;
        DW_SHBE_Records recs;
        while (ite.hasNext()) {
            YM3 = ite.next();
            recs = Data.get(YM3);
            if (recs != null) {
                recs = null;
                return true;
            }
        }
        return false;
    }

    /**
     * Clears some DW_SHBE_Records in Data from fast access memory except the
     * DW_SHBE_Records in Data indexed by YM3.
     *
     * @param YM3
     * @return true iff some DW_SHBE_Records were cleared and false otherwise.
     */
    public boolean clearSomeCacheExcept(String YM3) {
        Iterator<String> ite;
        ite = Data.keySet().iterator();
        String aYM3;
        DW_SHBE_Records recs;
        while (ite.hasNext()) {
            aYM3 = ite.next();
            if (!YM3.equalsIgnoreCase(aYM3)) {
                recs = Data.get(YM3);
                if (recs != null) {
                    recs = null;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clears DW_SHBE_Records for YM3 in Data from fast access memory.
     *
     * @param YM3 The Year_Month key for accessing the DW_SHBE_Records to be
     * cleared from fast access memory.
     * @return true iff the data were cleared and false otherwise (when the data
     * is already cleared).
     */
    public boolean clearCache(String YM3) {
        DW_SHBE_Records recs;
        recs = Data.get(YM3);
        if (recs != null) {
            recs = null;
            return true;
        }
        return false;
    }
}
