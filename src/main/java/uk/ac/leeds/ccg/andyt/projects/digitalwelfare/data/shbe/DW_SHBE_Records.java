/*
 * Copyright (C) 2015 geoagdt.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Point;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.DW_CorrectedPostcodes;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_YM3;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Records extends DW_Object implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * For convenience.
     */
    private transient DW_Strings Strings;
    private transient DW_Postcode_Handler Postcode_Handler;
    

    /**
     * Call this method to initialise fields declared transient after having
     * read this back as a Serialized Object.
     *
     * @param e
     */
    public void init(DW_Environment e) {
        Env = e;
        Strings = e.getStrings();
        Postcode_Handler = e.getPostcode_Handler();
    }

    /**
     * Keys are ClaimIDs, values are DW_SHBE_Record.
     */
    private HashMap<DW_ID, DW_SHBE_Record> Records;

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
     * A store for ClaimIDs for Cottingley Springs Caravan Park where there are
     * two claims for a claimant, one for a pitch and the other for the rent of
     * a caravan.
     */
    private HashSet<DW_ID> CottingleySpringsCaravanParkPairedClaimIDs;

    /**
     * A store for ClaimIDs where: StatusOfHBClaimAtExtractDate = 1 (In
     * Payment).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfHBAtExtractDateInPayment;

    /**
     * A store for ClaimIDs where: StatusOfHBClaimAtExtractDate = 2 (Suspended).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfHBAtExtractDateSuspended;

    /**
     * A store for ClaimIDs where: StatusOfHBClaimAtExtractDate = 0 (Suspended).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfHBAtExtractDateOther;

    /**
     * A store for ClaimIDs where: StatusOfCTBClaimAtExtractDate = 1 (In
     * Payment).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfCTBAtExtractDateInPayment;

    /**
     * A store for ClaimIDs where: StatusOfCTBClaimAtExtractDate = 2
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfCTBAtExtractDateSuspended;

    /**
     * A store for ClaimIDs where: StatusOfCTBClaimAtExtractDate = 0
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimIDsWithStatusOfCTBAtExtractDateOther;

    /**
     * SRecordsWithoutDRecords indexed by ClaimRef DW_ID. Once the SHBE data is
     * loaded from source, this only contains those SRecordsWithoutDRecords that
     * are not linked to a DRecord.
     */
    private HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>> SRecordsWithoutDRecords;

    /**
     * For storing the ClaimIDs of Records that have SRecords along with the
     * count of those SRecordsWithoutDRecords.
     */
    private HashMap<DW_ID, Integer> ClaimIDAndCountOfRecordsWithSRecords;

    /**
     * For storing the Year_Month of this. This is an identifier for these data.
     */
    private DW_YM3 YM3;

    /**
     * For storing the NearestYM3ForONSPDLookup of this. This is derived from
     * YM3.
     */
    private DW_YM3 NearestYM3ForONSPDLookup;

    /**
     * Holds a reference to the original input data file from which this was
     * created.
     */
    private File InputFile;

    /**
     * Directory where this is stored.
     */
    private File Dir;

    /**
     * File for storing this.
     */
    private File File;

    /**
     * File for storing Data.
     */
    private File RecordsFile;

    /**
     * File for storing ClaimIDs of new SHBE claims.
     */
    private File ClaimIDsOfNewSHBEClaimsFile;

    /**
     * File for storing ClaimIDs of new SHBE claims where Claimant was a
     * Claimant before.
     */
    private File ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile;

    /**
     * File for storing ClaimIDs of new SHBE claims where Claimant was a Partner
     * before.
     */
    private File ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile;

    /**
     * File for storing ClaimIDs of new SHBE claims where Claimant was a
     * NonDependent before.
     */
    private File ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile;

    /**
     * File for storing ClaimIDs of new SHBE claims where Claimant is new.
     */
    private File ClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile;

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
     * File for storing Cottingley Springs Caravan Park paired ClaimIDs.
     */
    private File CottingleySpringsCaravanParkPairedClaimIDsFile;

    /**
     * File for storing ClaimIDs with status of HB at extract date InPayment.
     */
    private File ClaimIDsWithStatusOfHBAtExtractDateInPaymentFile;

    /**
     * File for storing ClaimIDs with status of HB at extract date Suspended.
     */
    private File ClaimIDsWithStatusOfHBAtExtractDateSuspendedFile;

    /**
     * File for storing ClaimIDs with status of HB at extract date Other.
     */
    private File ClaimIDsWithStatusOfHBAtExtractDateOtherFile;

    /**
     * File for storing ClaimIDs with status of CTB at extract date InPayment.
     */
    private File ClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile;

    /**
     * File for storing ClaimIDs with status of CTB at extract date Suspended.
     */
    private File ClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile;

    /**
     * File for storing ClaimIDs with status of CTB at extract date Other.
     */
    private File ClaimIDsWithStatusOfCTBAtExtractDateOtherFile;

    /**
     * File for storing SRecordsWithoutDRecords.
     */
    private File SRecordsWithoutDRecordsFile;

    /**
     * File for storing ClaimIDs and count of records with SRecords.
     */
    private File ClaimIDAndCountOfRecordsWithSRecordsFile;

    /**
     * For storing the ClaimID of Records without a mappable Claimant Postcode.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithoutAMappableClaimantPostcode;

    /**
     * File for storing ClaimIDs of claims without a mappable claimant postcode.
     */
    private File ClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile;

    /**
     * ClaimIDs mapped to PersonIDs of Claimants.
     */
    private HashMap<DW_ID, DW_PersonID> ClaimIDToClaimantPersonIDLookup;

    /**
     * ClaimIDs mapped to PersonIDs of Partners. If there is no main Partner for
     * the claim then there is no mapping.
     */
    private HashMap<DW_ID, DW_PersonID> ClaimIDToPartnerPersonIDLookup;

    /**
     * ClaimIDs mapped to {@code HashSet<DW_PersonID>} of Dependents. If there
     * are no Dependents for the claim then there is no mapping.
     */
    private HashMap<DW_ID, HashSet<DW_PersonID>> ClaimIDToDependentPersonIDsLookup;

    /**
     * ClaimIDs mapped to {@code HashSet<DW_PersonID>} of NonDependents. If
     * there are no NonDependents for the claim then there is no mapping.
     */
    private HashMap<DW_ID, HashSet<DW_PersonID>> ClaimIDToNonDependentPersonIDsLookup;

    /**
     * ClaimIDs of Claims with Claimants that are Claimants in another claim.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim;

    /**
     * ClaimIDs of Claims with Claimants that are Partners in another claim.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim;

    /**
     * ClaimIDs of Claims with Partners that are Claimants in another claim.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim;

    /**
     * ClaimIDs of Claims with Partners that are Partners in multiple claims.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim;

    /**
     * ClaimIDs of Claims with NonDependents that are Claimants or Partners in
     * another claim.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim;

    /**
     * DW_PersonIDs of Claimants that are in multiple claims in a month mapped
     * to a set of ClaimIDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;

    /**
     * DW_PersonIDs of Partners that are in multiple claims in a month mapped to
     * a set of ClaimIDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;

    /**
     * DW_PersonIDs of NonDependents that are in multiple claims in a month
     * mapped to a set of ClaimIDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;

    /**
     * ClaimIDs mapped to Postcode DW_IDs.
     */
    private HashMap<DW_ID, DW_ID> ClaimIDToPostcodeIDLookup;

    /**
     * ClaimIDs of the claims that have had PostcodeF updated from the future.
     * This is only to be stored if the postcode was previously of an invalid
     * format.
     */
    private HashSet<DW_ID> ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture;

    /**
     * ClaimIDs. This is only used when reading the data to check that ClaimIDs
     * are unique.
     */
    private HashSet<DW_ID> ClaimIDs;

    /**
     * For storing ClaimIDs of new SHBE claims.
     */
    private HashSet<DW_ID> ClaimIDsOfNewSHBEClaims;

    /**
     * For storing ClaimIDs of new SHBE claims where Claimant was a Claimant
     * before.
     */
    private HashSet<DW_ID> ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore;

    /**
     * For storing ClaimIDs of new SHBE claims where Claimant was a Partner
     * before.
     */
    private HashSet<DW_ID> ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore;

    /**
     * For storing ClaimIDs of new SHBE claims where Claimant was a NonDependent
     * before.
     */
    private HashSet<DW_ID> ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore;

    /**
     * For storing ClaimIDs of new SHBE claims where Claimant is new.
     */
    private HashSet<DW_ID> ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew;

    /**
     * ClaimIDs mapped to TenancyType.
     */
    private HashMap<DW_ID, Integer> ClaimIDToTenancyTypeLookup;

    /**
     * LoadSummary
     */
    private HashMap<String, Number> LoadSummary;

    /**
     * The line numbers of records that for some reason could not be loaded.
     */
    private ArrayList<Long> RecordIDsNotLoaded;

    /**
     * For storing ClaimIDs of all Claims where Claimant National Insurance
     * Number is invalid.
     */
    private HashSet<DW_ID> ClaimIDsOfInvalidClaimantNINOClaims;

    /**
     * // * For storing ClaimID mapped to Claim Postcodes that are not
     * (currently) mappable.
     */
    private HashMap<DW_ID, String> ClaimantPostcodesUnmappable;

    /**
     * For storing ClaimID mapped to Claim Postcodes that have been
     * automatically modified to make them mappable.
     */
    private HashMap<DW_ID, String[]> ClaimantPostcodesModified;

    /**
     * For storing ClaimID mapped to Claimant Postcodes Checked by local
     * authority to be mappable, but not found in the subsequent or the latest
     * ONSPD.
     */
    private HashMap<DW_ID, String> ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes;

    /**
     * ClaimIDToClaimantPersonIDLookupFile File.
     */
    private File ClaimIDToClaimantPersonIDLookupFile;

    /**
     * ClaimIDToPartnerPersonIDLookup File.
     */
    private File ClaimIDToPartnerPersonIDLookupFile;

    /**
     * ClaimIDToDependentPersonIDsLookupFile File.
     */
    private File ClaimIDToDependentPersonIDsLookupFile;

    /**
     * ClaimIDToNonDependentPersonIDsLookupFile File.
     */
    private File ClaimIDToNonDependentPersonIDsLookupFile;

    /**
     * ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile File.
     */
    private File ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile;

    /**
     * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile File.
     */
    private File ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile;

    /**
     * ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile File.
     */
    private File ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile;

    /**
     * ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile File.
     */
    private File ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile;

    /**
     * ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile File.
     */
    private File ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile;

    /**
     * ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile File.
     */
    private File ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;

    /**
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile File.
     */
    private File PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;

    /**
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile File.
     */
    private File NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;

    /**
     * ClaimIDToPostcodeIDLookupFile File.
     */
    private File ClaimIDToPostcodeIDLookupFile;

    /**
     * ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile File.
     */
    private File ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile;

    /**
     * ClaimIDToTenancyTypeLookupFile File.
     */
    private File ClaimIDToTenancyTypeLookupFile;

    /**
     * LoadSummary File.
     */
    private File LoadSummaryFile;

    /**
     * RecordIDsNotLoaded File.
     */
    private File RecordIDsNotLoadedFile;

    /**
     * ClaimIDsOfInvalidClaimantNINOClaimsFile File.
     */
    private File ClaimIDsOfInvalidClaimantNINOClaimsFile;

    /**
     * ClaimantPostcodesUnmappableFile File.
     */
    private File ClaimantPostcodesUnmappableFile;

    /**
     * ClaimantPostcodesModifiedFile File.
     */
    private File ClaimantPostcodesModifiedFile;

    /**
     * ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile File.
     */
    private File ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile;

    /**
     * If not initialised, initialises Records then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_SHBE_Record> getClaimIDToDW_SHBE_RecordMap(boolean handleOutOfMemoryError) {
        try {
            return getRecords();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToDW_SHBE_RecordMap(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises Records then returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, DW_SHBE_Record> getRecords() {
        if (Records == null) {
            File f;
            f = getRecordsFile();
            if (f.exists()) {
                Records = (HashMap<DW_ID, DW_SHBE_Record>) Generic_StaticIO.readObject(f);
            } else {
                Records = new HashMap<DW_ID, DW_SHBE_Record>();
            }
        }
        return Records;
    }

    /**
     * If not initialised, initialises ClaimIDsOfNewSHBEClaims then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNewSHBEClaims(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfNewSHBEClaims();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNewSHBEClaims(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDsOfNewSHBEClaims then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfNewSHBEClaims() {
        if (ClaimIDsOfNewSHBEClaims == null) {
            File f;
            f = getClaimIDsOfNewSHBEClaimsFile();
            if (f.exists()) {
                ClaimIDsOfNewSHBEClaims = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfNewSHBEClaims = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfNewSHBEClaims;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore == null) {
            File f;
            f = getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile();
            if (f.exists()) {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore == null) {
            File f;
            f = getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile();
            if (f.exists()) {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore == null) {
            File f;
            f = getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile();
            if (f.exists()) {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore;
    }

    /**
     * If not initialised, initialises ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantIsNew(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfNewSHBEClaimsWhereClaimantIsNew();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNewSHBEClaimsWhereClaimantIsNew(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew
     * then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfNewSHBEClaimsWhereClaimantIsNew() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew == null) {
            File f;
            f = getClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile();
            if (f.exists()) {
                ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew;
    }

    /**
     * If not initialised, initialises
     * CottingleySpringsCaravanParkPairedClaimIDs then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getCottingleySpringsCaravanParkPairedClaimIDs(boolean handleOutOfMemoryError) {
        try {
            return getCottingleySpringsCaravanParkPairedClaimIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getCottingleySpringsCaravanParkPairedClaimIDs(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * CottingleySpringsCaravanParkPairedClaimIDs then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getCottingleySpringsCaravanParkPairedClaimIDs() {
        if (CottingleySpringsCaravanParkPairedClaimIDs == null) {
            File f;
            f = getCottingleySpringsCaravanParkPairedClaimIDsFile();
            if (f.exists()) {
                CottingleySpringsCaravanParkPairedClaimIDs = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                CottingleySpringsCaravanParkPairedClaimIDs = new HashSet<DW_ID>();
            }
        }
        return CottingleySpringsCaravanParkPairedClaimIDs;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfHBAtExtractDateInPayment then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateInPayment(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfHBAtExtractDateInPayment();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfHBAtExtractDateInPayment(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfHBAtExtractDateInPayment then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateInPayment() {
        if (ClaimIDsWithStatusOfHBAtExtractDateInPayment == null) {
            File f;
            f = getClaimIDsWithStatusOfHBAtExtractDateInPaymentFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfHBAtExtractDateInPayment = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfHBAtExtractDateInPayment = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfHBAtExtractDateInPayment;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfHBAtExtractDateSuspended then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateSuspended(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfHBAtExtractDateSuspended();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfHBAtExtractDateSuspended(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfHBAtExtractDateSuspended then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateSuspended() {
        if (ClaimIDsWithStatusOfHBAtExtractDateSuspended == null) {
            File f;
            f = getClaimIDsWithStatusOfHBAtExtractDateSuspendedFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfHBAtExtractDateSuspended = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfHBAtExtractDateSuspended = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfHBAtExtractDateSuspended;
    }

    /**
     * If not initialised, initialises ClaimIDsWithStatusOfHBAtExtractDateOther
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateOther(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfHBAtExtractDateOther();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfHBAtExtractDateOther(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDsWithStatusOfHBAtExtractDateOther
     * then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateOther() {
        if (ClaimIDsWithStatusOfHBAtExtractDateOther == null) {
            File f;
            f = getClaimIDsWithStatusOfHBAtExtractDateOtherFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfHBAtExtractDateOther = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfHBAtExtractDateOther = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfHBAtExtractDateOther;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfCTBAtExtractDateInPayment then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateInPayment(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfCTBAtExtractDateInPayment();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfCTBAtExtractDateInPayment(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfCTBAtExtractDateInPayment then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateInPayment() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateInPayment == null) {
            File f;
            f = getClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfCTBAtExtractDateInPayment = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfCTBAtExtractDateInPayment = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateInPayment;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfCTBAtExtractDateSuspended then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateSuspended(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfCTBAtExtractDateSuspended();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfCTBAtExtractDateSuspended(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsWithStatusOfCTBAtExtractDateSuspended then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateSuspended() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateSuspended == null) {
            File f;
            f = getClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfCTBAtExtractDateSuspended = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfCTBAtExtractDateSuspended = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateSuspended;
    }

    /**
     * If not initialised, initialises ClaimIDsWithStatusOfCTBAtExtractDateOther
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateOther(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsWithStatusOfCTBAtExtractDateOther();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsWithStatusOfCTBAtExtractDateOther(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDsWithStatusOfCTBAtExtractDateOther
     * then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateOther() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateOther == null) {
            File f;
            f = getClaimIDsWithStatusOfCTBAtExtractDateOtherFile();
            if (f.exists()) {
                ClaimIDsWithStatusOfCTBAtExtractDateOther = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsWithStatusOfCTBAtExtractDateOther = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateOther;
    }

    /**
     * If not initialised, initialises SRecordsWithoutDRecords then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>> getSRecordsWithoutDRecords(boolean handleOutOfMemoryError) {
        try {
            return getSRecordsWithoutDRecords();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getSRecordsWithoutDRecords(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the SRecordsWithoutDRecords
     */
    protected HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>> getSRecordsWithoutDRecords() {
        if (SRecordsWithoutDRecords == null) {
            File f;
            f = getSRecordsWithoutDRecordsFile();
            if (f.exists()) {
                SRecordsWithoutDRecords = (HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>>) Generic_StaticIO.readObject(f);
            } else {
                SRecordsWithoutDRecords = new HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>>();
            }
        }
        return SRecordsWithoutDRecords;
    }

    /**
     * If not initialised, initialises ClaimIDAndCountOfRecordsWithSRecords then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, Integer> getClaimIDAndCountOfRecordsWithSRecords(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDAndCountOfRecordsWithSRecords();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDAndCountOfRecordsWithSRecords(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithoutAMappableClaimantPostcode then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithoutAValidClaimantPostcode(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithoutAMappableClaimantPostcode();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithoutAValidClaimantPostcode(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimIDAndCountOfRecordsWithSRecords
     */
    protected HashMap<DW_ID, Integer> getClaimIDAndCountOfRecordsWithSRecords() {
        if (ClaimIDAndCountOfRecordsWithSRecords == null) {
            File f;
            f = getClaimIDAndCountOfRecordsWithSRecordsFile();
            if (f.exists()) {
                ClaimIDAndCountOfRecordsWithSRecords = (HashMap<DW_ID, Integer>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDAndCountOfRecordsWithSRecords = new HashMap<DW_ID, Integer>();
            }
        }
        return ClaimIDAndCountOfRecordsWithSRecords;
    }

    /**
     * @return the ClaimIDsOfClaimsWithoutAMappableClaimantPostcode
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithoutAMappableClaimantPostcode() {
        if (ClaimIDsOfClaimsWithoutAMappableClaimantPostcode == null) {
            File f;
            f = getClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithoutAMappableClaimantPostcode = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithoutAMappableClaimantPostcode = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithoutAMappableClaimantPostcode;
    }

    /**
     * @return YM3
     */
    public DW_YM3 getYM3() {
        return YM3;
    }

    /**
     * @return NearestYM3ForONSPDLookup
     */
    public DW_YM3 getNearestYM3ForONSPDLookup() {
        return NearestYM3ForONSPDLookup;
    }

    /**
     * Write this to file.
     */
    public void write() {
        Generic_StaticIO.writeObject(
                this,
                getFile());
    }

    /**
     * If Dir is null, it is initialised.
     *
     * @return Dir.
     */
    protected File getDir() {
        if (Dir == null) {
            Dir = new File(
                    Env.getFiles().getGeneratedSHBEDir(), getYM3().toString());
            Dir.mkdirs();
        }
        return Dir;
    }

    /**
     * @param filename
     * @return The File in Dir given by filename.
     */
    public File getFile(
            String filename) {
        return new File(
                getDir(),
                filename);
    }

    public DW_SHBE_Records() {
    }

    /**
     * For loading an existing collection.
     *
     * @param env
     * @param YM3
     */
    public DW_SHBE_Records(
            DW_Environment env,
            DW_YM3 YM3
    ) {
        super(env);
        this.YM3 = YM3;
        NearestYM3ForONSPDLookup = Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        env.logO("YM3 " + YM3, true);
        env.logO("NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup, true);
        Strings = env.getStrings();
        Records = getClaimIDToDW_SHBE_RecordMap(env.HOOME);
        ClaimIDsOfNewSHBEClaims = getClaimIDsOfNewSHBEClaims(env.HOOME);
        ClaimantPersonIDs = getClaimantPersonIDs(env.HOOME);
        PartnerPersonIDs = getPartnerPersonIDs(env.HOOME);
        NonDependentPersonIDs = getNonDependentPersonIDs(env.HOOME);
        CottingleySpringsCaravanParkPairedClaimIDs = getCottingleySpringsCaravanParkPairedClaimIDs(env.HOOME);
        ClaimIDsWithStatusOfHBAtExtractDateInPayment = getClaimIDsWithStatusOfHBAtExtractDateInPayment(env.HOOME);
        ClaimIDsWithStatusOfHBAtExtractDateSuspended = getClaimIDsWithStatusOfHBAtExtractDateSuspended(env.HOOME);
        ClaimIDsWithStatusOfHBAtExtractDateOther = getClaimIDsWithStatusOfHBAtExtractDateOther(env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateInPayment = getClaimIDsWithStatusOfCTBAtExtractDateInPayment(env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateSuspended = getClaimIDsWithStatusOfCTBAtExtractDateSuspended(env.HOOME);
        ClaimIDsWithStatusOfCTBAtExtractDateOther = getClaimIDsWithStatusOfCTBAtExtractDateOther(env.HOOME);
        SRecordsWithoutDRecords = getSRecordsWithoutDRecords(env.HOOME);
        ClaimIDAndCountOfRecordsWithSRecords = getClaimIDAndCountOfRecordsWithSRecords(env.HOOME);
        ClaimIDsOfClaimsWithoutAMappableClaimantPostcode = getClaimIDsOfClaimsWithoutAValidClaimantPostcode(env.HOOME);
        ClaimIDToClaimantPersonIDLookup = getClaimIDToClaimantPersonIDLookup(env.HOOME);
        ClaimIDToPartnerPersonIDLookup = getClaimIDToPartnerPersonIDLookup(env.HOOME);
        ClaimIDToDependentPersonIDsLookup = getClaimIDToDependentPersonIDsLookup(env.HOOME);
        ClaimIDToNonDependentPersonIDsLookup = getClaimIDToNonDependentPersonIDsLookup(env.HOOME);
        ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(env.HOOME);
        ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(env.HOOME);
        ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(env.HOOME);
        ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(env.HOOME);
        ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = getClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(env.HOOME);
        ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(env.HOOME);
        PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(env.HOOME);
        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(env.HOOME);
        ClaimIDToPostcodeIDLookup = getClaimIDToPostcodeIDLookup(env.HOOME);
        ClaimIDToTenancyTypeLookup = getClaimIDToTenancyTypeLookup(env.HOOME);
        LoadSummary = getLoadSummary(env.HOOME);
        RecordIDsNotLoaded = getRecordIDsNotLoaded(env.HOOME);
        ClaimIDsOfInvalidClaimantNINOClaims = getClaimIDsOfInvalidClaimantNINOClaims(env.HOOME);
        ClaimantPostcodesUnmappable = getClaimantPostcodesUnmappable(env.HOOME);
        ClaimantPostcodesModified = getClaimantPostcodesModified(env.HOOME);
        ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes(env.HOOME);
        ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(env.HOOME);
    }

    /**
     * Loads Data from source.
     *
     * @param env
     * @param inputFilename
     * @param inputDirectory
     * @param LatestYM3ForONSPDFormat
     * @param logDir
     */
    public DW_SHBE_Records(
            DW_Environment env,
            File inputDirectory,
            String inputFilename,
            DW_YM3 LatestYM3ForONSPDFormat,
            File logDir
    ) {
        super(env);
        DW_SHBE_Handler DW_SHBE_Handler;
        DW_SHBE_Handler = env.getSHBE_Handler();
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getPostcode_Handler();
        DW_SHBE_Data DW_SHBE_Data;
        DW_SHBE_Data = env.getSHBE_Data();
        InputFile = new File(inputDirectory, inputFilename);
        YM3 = DW_SHBE_Handler.getYM3(inputFilename);
        NearestYM3ForONSPDLookup = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        Strings = env.getStrings();
        Records = new HashMap<DW_ID, DW_SHBE_Record>();
        ClaimIDs = new HashSet<DW_ID>();
        ClaimIDsOfNewSHBEClaims = new HashSet<DW_ID>();
        ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = new HashSet<DW_ID>();
        ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = new HashSet<DW_ID>();
        ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = new HashSet<DW_ID>();
        ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew = new HashSet<DW_ID>();
        ClaimantPersonIDs = new HashSet<DW_PersonID>();
        PartnerPersonIDs = new HashSet<DW_PersonID>();
        NonDependentPersonIDs = new HashSet<DW_PersonID>();
        CottingleySpringsCaravanParkPairedClaimIDs = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfHBAtExtractDateInPayment = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfHBAtExtractDateSuspended = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfHBAtExtractDateOther = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfCTBAtExtractDateInPayment = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfCTBAtExtractDateSuspended = new HashSet<DW_ID>();
        ClaimIDsWithStatusOfCTBAtExtractDateOther = new HashSet<DW_ID>();
        SRecordsWithoutDRecords = new HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>>();
        ClaimIDAndCountOfRecordsWithSRecords = new HashMap<DW_ID, Integer>();
        ClaimIDsOfClaimsWithoutAMappableClaimantPostcode = new HashSet<DW_ID>();
        ClaimIDToClaimantPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
        ClaimIDToPartnerPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
        ClaimIDToDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
        ClaimIDToNonDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
        ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
        ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
        ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        ClaimIDToPostcodeIDLookup = new HashMap<DW_ID, DW_ID>();
        ClaimIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
        LoadSummary = new HashMap<String, Number>();
        RecordIDsNotLoaded = new ArrayList<Long>();
        ClaimIDsOfInvalidClaimantNINOClaims = new HashSet<DW_ID>();
        ClaimantPostcodesUnmappable = new HashMap<DW_ID, String>();
        ClaimantPostcodesModified = new HashMap<DW_ID, String[]>();
        ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = new HashMap<DW_ID, String>();
        ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = new HashSet<DW_ID>();
        env.log("----------------------");
        env.log("Load " + YM3);
        env.log("----------------------");
        env.log("NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup);
        env.log("LatestYM3ForONSPDLookup " + LatestYM3ForONSPDFormat);
        if (!LatestYM3ForONSPDFormat.equals(NearestYM3ForONSPDLookup)) {
            env.log("The " + LatestYM3ForONSPDFormat + " ONSPD may be used "
                    + "if the Claimant Postcode does not have a lookup in the "
                    + NearestYM3ForONSPDLookup + " ONSPD.");
        }
        /**
         * Check the postcodes against these to see if we should report them
         * again as unmappable.
         */
        DW_CorrectedPostcodes DW_CorrectedPostcodes;
        HashMap<String, ArrayList<String>> ClaimRefToOriginalPostcodes;
        HashMap<String, ArrayList<String>> ClaimRefToCorrectedPostcodes;
        HashSet<String> PostcodesCheckedAsMappable;
        //HashMap<String, HashSet<String>> UnmappableToMappablePostcodes;
        /**
         * Mapping of National Insurance Numbers to simple DW_IDs.
         */
        HashMap<String, DW_ID> NINOToNINOIDLookup;
        /**
         * Mapping of DW_IDs to National Insurance Numbers.
         */
        HashMap<DW_ID, String> NINOIDToNINOLookup;
        /**
         * Mapping of Dates of Birth to simple DW_IDs.
         */
        HashMap<String, DW_ID> DOBToDOBIDLookup;
        /**
         * Mapping of DW_IDs to Dates of Birth.
         */
        HashMap<DW_ID, String> DOBIDToDOBLookup;
        /**
         * Mapping of Unit Postcodes to simple DW_IDs.
         */
        HashMap<String, DW_ID> PostcodeToPostcodeIDLookup;
        /**
         * Mapping of DW_ID to a Unit Postcode.
         */
        HashMap<DW_ID, String> PostcodeIDToPostcodeLookup;
        /**
         * Mapping of DW_ID to a Unit Postcode.
         */
        HashMap<DW_ID, Geotools_Point> PostcodeIDToPointLookup;
        /**
         * Mapping of ClaimRef String to Claim DW_ID.
         */
        HashMap<String, DW_ID> ClaimRefToClaimIDLookup;
        /**
         * Mapping of Claim DW_ID to ClaimRef String.
         */
        HashMap<DW_ID, String> ClaimIDToClaimRefLookup;

        /**
         * DW_PersonID of All Claimants
         */
        HashSet<DW_PersonID> AllClaimantPersonIDs;

        /**
         * DW_PersonID of All Partners
         */
        HashSet<DW_PersonID> AllPartnerPersonIDs;

        /**
         * DW_PersonID of All Non-Dependents
         */
        HashSet<DW_PersonID> AllNonDependentIDs;

        /**
         * All DW_PersonID to ClaimIDs Lookup
         */
        HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimIDsLookup;

        /**
         * Initialise mappings from DW_SHBE_Data.
         */
        DW_CorrectedPostcodes = DW_SHBE_Data.getCorrectedPostcodes();
        ClaimRefToOriginalPostcodes = DW_CorrectedPostcodes.getClaimRefToOriginalPostcodes();
        ClaimRefToCorrectedPostcodes = DW_CorrectedPostcodes.getClaimRefToCorrectedPostcodes();
        PostcodesCheckedAsMappable = DW_CorrectedPostcodes.getPostcodesCheckedAsMappable();
        //UnmappableToMappablePostcodes = DW_CorrectedPostcodes.getUnmappableToMappablePostcodes();

        NINOToNINOIDLookup = DW_SHBE_Data.getNINOToNINOIDLookup();
        NINOIDToNINOLookup = DW_SHBE_Data.getNINOIDToNINOLookup();
        DOBToDOBIDLookup = DW_SHBE_Data.getDOBToDOBIDLookup();
        DOBIDToDOBLookup = DW_SHBE_Data.getDOBIDToDOBLookup();
        AllClaimantPersonIDs = DW_SHBE_Data.getClaimantPersonIDs();
        AllPartnerPersonIDs = DW_SHBE_Data.getPartnerPersonIDs();
        AllNonDependentIDs = DW_SHBE_Data.getNonDependentPersonIDs();
        PersonIDToClaimIDsLookup = DW_SHBE_Data.getPersonIDToClaimIDLookup();
        PostcodeToPostcodeIDLookup = DW_SHBE_Data.getPostcodeToPostcodeIDLookup();
        PostcodeIDToPostcodeLookup = DW_SHBE_Data.getPostcodeIDToPostcodeLookup();
        PostcodeIDToPointLookup = DW_SHBE_Data.getPostcodeIDToPointLookup(YM3);
        ClaimRefToClaimIDLookup = DW_SHBE_Data.getClaimRefToClaimIDLookup();
        ClaimIDToClaimRefLookup = DW_SHBE_Data.getClaimIDToClaimRefLookup();
        // Initialise statistics
        int CountOfNewMappableClaimantPostcodes = 0;
        int CountOfMappableClaimantPostcodes = 0;
        int CountOfNewClaimantPostcodes = 0;
        int CountOfNonMappableClaimantPostcodes = 0;
        int CountOfValidFormatClaimantPostcodes = 0;
        int totalCouncilTaxBenefitClaims = 0;
        int totalCouncilTaxAndHousingBenefitClaims = 0;
        int totalHousingBenefitClaims = 0;
        int countSRecords = 0;
        int SRecordNotLoadedCount = 0;
        int NumberOfIncompleteDRecords = 0;
        long totalIncome = 0;
        long grandTotalIncome = 0;
        int totalIncomeGreaterThanZeroCount = 0;
        long totalWeeklyEligibleRentAmount = 0;
        long grandTotalWeeklyEligibleRentAmount = 0;
        int totalWeeklyEligibleRentAmountGreaterThanZeroCount = 0;
        // Read data
        env.log("<Read data>");
        try {
            BufferedReader br;
            br = Generic_StaticIO.getBufferedReader(InputFile);
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            st.wordChars('*', '*');
            String line = "";
            long RecordID = 0;
            int lineCount = 0;
            // Declare Variables
            DW_SHBE_S_Record SRecord;
            String ClaimRef;

            DW_SHBE_D_Record DRecord;
            int TenancyType;
            boolean doLoop;
            DW_ID ClaimID;
            DW_SHBE_Record record;
            int StatusOfHBClaimAtExtractDate;
            int StatusOfCTBClaimAtExtractDate;
            String Postcode;
            String ClaimantNINO;
            String ClaimantDOB;
            DW_PersonID ClaimantPersonID;
            boolean addToNew;
            Object key;
            DW_ID otherClaimID = null;
            String otherClaimRef;
            DW_SHBE_Record otherRecord;
            /**
             * There are two types of SHBE data encountered so far. Each has
             * slightly different field definitions.
             */
            int type;
            type = readAndCheckFirstLine(inputDirectory, inputFilename);
            Generic_StaticIO.skipline(st);
            // Read collections
            int tokenType;
            tokenType = st.nextToken();
            int counter = 0;
            while (tokenType != StreamTokenizer.TT_EOF) {
                switch (tokenType) {
                    case StreamTokenizer.TT_EOL:
                        if (counter % 10000 == 0) {
                            //env.logO(line);
                            env.logO("Read line " + counter, true);
                        }
                        counter++;
                        break;
                    case StreamTokenizer.TT_WORD:
                        line = st.sval;
                        if (line.startsWith("S")) {
                            try {
                                SRecord = new DW_SHBE_S_Record(
                                        env, RecordID, type, line);
                                ClaimRef = SRecord.getClaimRef();
                                if (ClaimRef == null) {
                                    env.logE("SRecord without a ClaimRef "
                                            + this.getClass().getName()
                                            + ".DW_SHBE_Records(DW_Environment, File, String)");
                                    env.logE("SRecord: " + SRecord.toString());
                                    env.logE("Line: " + line);
                                    env.logE("RecordID " + RecordID);
                                    RecordIDsNotLoaded.add(RecordID);
                                    SRecordNotLoadedCount++;
                                } else {
                                    ClaimID = DW_SHBE_Handler.getIDAddIfNeeded(
                                            ClaimRef,
                                            ClaimRefToClaimIDLookup,
                                            ClaimIDToClaimRefLookup);
                                    ArrayList<DW_SHBE_S_Record> recs;
                                    recs = SRecordsWithoutDRecords.get(ClaimID);
                                    if (recs == null) {
                                        recs = new ArrayList<DW_SHBE_S_Record>();
                                        SRecordsWithoutDRecords.put(ClaimID, recs);
                                    }
                                    recs.add(SRecord);
                                }
                            } catch (Exception e) {
                                env.logE("Line not loaded in "
                                        + this.getClass().getName()
                                        + ".DW_SHBE_Records(DW_Environment, File, String)");
                                env.logE("Line: " + line);
                                env.logE("RecordID " + RecordID);
                                env.logE(e.getLocalizedMessage());
                                env.logE(e);
                                RecordIDsNotLoaded.add(RecordID);
                            }
                            countSRecords++;
                        } else if (line.startsWith("D")) {
                            try {
                                DRecord = new DW_SHBE_D_Record(
                                        env, RecordID, type, line);
                                /**
                                 * For the time being, if for some reason the
                                 * record does not load correctly, then do not
                                 * load this record. Ideally those that do not
                                 * load will be investigated and a solution for
                                 * loading them found.
                                 */
                                TenancyType = DRecord.getTenancyType();
                                if (TenancyType == 0) {
                                    env.logE("Incomplete record "
                                            + this.getClass().getName()
                                            + ".DW_SHBE_Records(DW_Environment, File, String)");
                                    env.logE("Line: " + line);
                                    env.logE("RecordID " + RecordID);
                                    NumberOfIncompleteDRecords++;
                                    RecordIDsNotLoaded.add(RecordID);
                                    lineCount++;
                                    RecordID++;
                                    break;
                                } else {
                                    ClaimRef = DRecord.getClaimRef();
                                    if (ClaimRef == null) {
                                        RecordIDsNotLoaded.add(RecordID);
                                    } else {
                                        doLoop = false;
                                        ClaimID = DW_SHBE_Handler.getIDAddIfNeeded(ClaimRef,
                                                ClaimRefToClaimIDLookup,
                                                ClaimIDToClaimRefLookup,
                                                ClaimIDs,
                                                ClaimIDsOfNewSHBEClaims);
                                        if (DW_SHBE_Handler.isHBClaim(DRecord)) {
                                            if (DRecord.getCouncilTaxBenefitClaimReferenceNumber() != null) {
                                                totalCouncilTaxAndHousingBenefitClaims++;
                                            } else {
                                                totalHousingBenefitClaims++;
                                            }
                                        }
                                        if (DW_SHBE_Handler.isCTBOnlyClaim(DRecord)) {
                                            totalCouncilTaxBenefitClaims++;
                                        }
                                        /**
                                         * Get or initialise DW_SHBE_Record
                                         * record
                                         */
                                        record = Records.get(ClaimID);
                                        if (record == null) {
                                            record = new DW_SHBE_Record(
                                                    env, YM3, ClaimID, DRecord);
                                            Records.put(ClaimID, record);
                                            doLoop = true;
                                        } else {
                                            env.logE("Two records have the same ClaimRef "
                                                    + this.getClass().getName()
                                                    + ".DW_SHBE_Records(DW_Environment, File, String)");
                                            env.logE("Line: " + line);
                                            env.logE("RecordID " + RecordID);
                                            env.logE("ClaimRef " + ClaimRef);
                                        }
                                        StatusOfHBClaimAtExtractDate = DRecord.getStatusOfHBClaimAtExtractDate();
                                        /**
                                         * 0 = Other; 1 = InPayment; 2 =
                                         * Suspended.
                                         */
                                        switch (StatusOfHBClaimAtExtractDate) {
                                            case 0: {
                                                ClaimIDsWithStatusOfHBAtExtractDateOther.add(ClaimID);
                                                break;
                                            }
                                            case 1: {
                                                ClaimIDsWithStatusOfHBAtExtractDateInPayment.add(ClaimID);
                                                break;
                                            }
                                            case 2: {
                                                ClaimIDsWithStatusOfHBAtExtractDateSuspended.add(ClaimID);
                                                break;
                                            }
                                            default:
                                                env.logE("Unexpected StatusOfHBClaimAtExtractDate "
                                                        + this.getClass().getName()
                                                        + ".DW_SHBE_Records(DW_Environment, File, String)");
                                                env.logE("Line: " + line);
                                                env.logE("RecordID " + RecordID);
                                                break;
                                        }
                                        StatusOfCTBClaimAtExtractDate = DRecord.getStatusOfCTBClaimAtExtractDate();
                                        /**
                                         * 0 = Other; 1 = InPayment; 2 =
                                         * Suspended.
                                         */
                                        switch (StatusOfCTBClaimAtExtractDate) {
                                            case 0: {
                                                ClaimIDsWithStatusOfCTBAtExtractDateOther.add(ClaimID);
                                                break;
                                            }
                                            case 1: {
                                                ClaimIDsWithStatusOfCTBAtExtractDateInPayment.add(ClaimID);
                                                break;
                                            }
                                            case 2: {
                                                ClaimIDsWithStatusOfCTBAtExtractDateSuspended.add(ClaimID);
                                                break;
                                            }
                                            default:
                                                env.logE("Unexpected StatusOfCTBClaimAtExtractDate "
                                                        + this.getClass().getName()
                                                        + ".DW_SHBE_Records(DW_Environment, File, String)");
                                                env.logE("Line: " + line);
                                                env.logE("RecordID " + RecordID);
                                                break;
                                        }
                                        if (doLoop) {
                                            Postcode = DRecord.getClaimantsPostcode();
                                            record.ClaimPostcodeF = DW_Postcode_Handler.formatPostcode(Postcode);
                                            record.ClaimPostcodeFManModified = false;
                                            record.ClaimPostcodeFAutoModified = false;
                                            // Do man modifications (modifications using lookups provided by LCC based on a manual checking of addresses)
                                            if (ClaimRefToOriginalPostcodes.keySet().contains(ClaimRef)) {
                                                ArrayList<String> OriginalPostcodes;
                                                OriginalPostcodes = ClaimRefToOriginalPostcodes.get(ClaimRef);
                                                if (OriginalPostcodes.contains(record.ClaimPostcodeF)) {
                                                    ArrayList<String> CorrectedPostcodes;
                                                    CorrectedPostcodes = ClaimRefToCorrectedPostcodes.get(ClaimRef);
                                                    record.ClaimPostcodeF = CorrectedPostcodes.get(OriginalPostcodes.indexOf(record.ClaimPostcodeF));
                                                    record.ClaimPostcodeFManModified = true;
                                                }
                                            } else {
                                                // Do auto modifications ()
                                                if (record.ClaimPostcodeF.length() > 5) {
                                                    /**
                                                     * Remove any 0 which
                                                     * probably should not be
                                                     * there in the first part
                                                     * of the postcode. For
                                                     * example "LS02 9JT" should
                                                     * probably be "LS2 9JT".
                                                     */
                                                    if (record.ClaimPostcodeF.charAt(record.ClaimPostcodeF.length() - 5) == '0') {
                                                        //System.out.println("record.ClaimPostcodeF " + record.ClaimPostcodeF);
                                                        record.ClaimPostcodeF = record.ClaimPostcodeF.replaceFirst("0", "");
                                                        //System.out.println("Postcode " + Postcode);
                                                        //System.out.println("record.ClaimPostcodeF " + record.ClaimPostcodeF);
                                                        record.ClaimPostcodeFAutoModified = true;
                                                    }
                                                    /**
                                                     * Change any "O" which
                                                     * should be a "0" in the
                                                     * second part of the
                                                     * postcode. For example
                                                     * "LS2 OJT" should probably
                                                     * be "LS2 0JT".
                                                     */
                                                    if (record.ClaimPostcodeF.charAt(record.ClaimPostcodeF.length() - 3) == 'O') {
                                                        //System.out.println("record.ClaimPostcodeF " + record.ClaimPostcodeF);
                                                        record.ClaimPostcodeF = record.ClaimPostcodeF.substring(0, record.ClaimPostcodeF.length() - 3)
                                                                + "0" + record.ClaimPostcodeF.substring(record.ClaimPostcodeF.length() - 2);
                                                        //System.out.println("Postcode " + Postcode);
                                                        //System.out.println("record.ClaimPostcodeF " + record.ClaimPostcodeF);
                                                        record.ClaimPostcodeFAutoModified = true;
                                                    }
                                                }
                                            }
                                            // Check if record.ClaimPostcodeF is mappable
                                            boolean isMappablePostcode;
                                            isMappablePostcode = DW_Postcode_Handler.isMappablePostcode(NearestYM3ForONSPDLookup, record.ClaimPostcodeF);
                                            boolean isMappablePostcodeLastestYM3 = false;
                                            if (!isMappablePostcode) {
                                                isMappablePostcodeLastestYM3 = DW_Postcode_Handler.isMappablePostcode(LatestYM3ForONSPDFormat, record.ClaimPostcodeF);
                                                if (isMappablePostcodeLastestYM3) {
                                                    env.logO(env.DEBUG_Level_FINEST,
                                                            "Postcode " + Postcode + " is not in the " + NearestYM3ForONSPDLookup + " ONSPD, "
                                                            + "but is in the " + LatestYM3ForONSPDFormat + " ONSPD!");
                                                    isMappablePostcode = isMappablePostcodeLastestYM3;
                                                }
                                            }
                                            // For those that are mappable having been modified, store the modification
                                            if (isMappablePostcode) {
                                                if (record.ClaimPostcodeFAutoModified) {
                                                    String claimPostcodeFNoSpaces = record.ClaimPostcodeF.replaceAll(" ", "");
                                                    if (!Postcode.replaceAll(" ", "").equalsIgnoreCase(claimPostcodeFNoSpaces)) {
                                                        int l;
                                                        l = record.ClaimPostcodeF.length();
                                                        String[] p;
                                                        p = new String[2];
                                                        p[0] = Postcode;
                                                        p[1] = claimPostcodeFNoSpaces.substring(0, l - 3) + " " + claimPostcodeFNoSpaces.substring(l - 3);
                                                        ClaimantPostcodesModified.put(ClaimID, p);
                                                    }
                                                }
                                            }
                                            record.ClaimPostcodeFValidPostcodeFormat = DW_Postcode_Handler.isValidPostcodeForm(record.ClaimPostcodeF);
                                            if (PostcodeToPostcodeIDLookup.containsKey(record.ClaimPostcodeF)) {
                                                CountOfMappableClaimantPostcodes++;
                                                record.ClaimPostcodeFMappable = true;
                                                record.PostcodeID = PostcodeToPostcodeIDLookup.get(record.ClaimPostcodeF);
                                                // Add the point to the lookup
                                                Geotools_Point AGDT_Point;
                                                AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                        NearestYM3ForONSPDLookup,
                                                        Strings.sUnit,
                                                        record.ClaimPostcodeF);
                                                PostcodeIDToPointLookup.put(record.PostcodeID, AGDT_Point);
                                            } else if (isMappablePostcode) {
                                                CountOfMappableClaimantPostcodes++;
                                                CountOfNewClaimantPostcodes++;
                                                CountOfNewMappableClaimantPostcodes++;
                                                record.ClaimPostcodeFMappable = true;
                                                record.PostcodeID = DW_SHBE_Handler.getPostcodeIDAddIfNeeded(
                                                        record.ClaimPostcodeF,
                                                        PostcodeToPostcodeIDLookup,
                                                        PostcodeIDToPostcodeLookup);
                                                // Add the point to the lookup
                                                Geotools_Point AGDT_Point;
                                                if (isMappablePostcodeLastestYM3) {
                                                    AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                            LatestYM3ForONSPDFormat,
                                                            Strings.sUnit,
                                                            record.ClaimPostcodeF);
                                                } else {
                                                    AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                            NearestYM3ForONSPDLookup,
                                                            Strings.sUnit,
                                                            record.ClaimPostcodeF);
                                                }
                                                PostcodeIDToPointLookup.put(record.PostcodeID, AGDT_Point);
                                            } else {
                                                CountOfNonMappableClaimantPostcodes++;
                                                CountOfNewClaimantPostcodes++;
                                                if (record.ClaimPostcodeFValidPostcodeFormat) {
                                                    CountOfValidFormatClaimantPostcodes++;
                                                }
                                                record.ClaimPostcodeFMappable = false;
                                                ClaimIDsOfClaimsWithoutAMappableClaimantPostcode.add(ClaimID);
                                                boolean PostcodeCheckedAsMappable = false;
                                                PostcodeCheckedAsMappable = PostcodesCheckedAsMappable.contains(record.ClaimPostcodeF);
                                                if (PostcodeCheckedAsMappable) {
                                                    ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes.put(ClaimID, Postcode);
                                                } else {
                                                    // Store unmappable claimant postcode.
                                                    ClaimantPostcodesUnmappable.put(ClaimID, Postcode);
                                                }
                                            }
                                            ClaimIDToPostcodeIDLookup.put(ClaimID, record.PostcodeID);
                                            ClaimIDToTenancyTypeLookup.put(ClaimID, TenancyType);
                                            totalIncome = DW_SHBE_Handler.getClaimantsAndPartnersIncomeTotal(DRecord);
                                            grandTotalIncome += totalIncome;
                                            if (totalIncome > 0) {
                                                totalIncomeGreaterThanZeroCount++;
                                            }
                                            totalWeeklyEligibleRentAmount = DRecord.getWeeklyEligibleRentAmount();
                                            grandTotalWeeklyEligibleRentAmount += totalWeeklyEligibleRentAmount;
                                            if (totalWeeklyEligibleRentAmount > 0) {
                                                totalWeeklyEligibleRentAmountGreaterThanZeroCount++;
                                            }
                                        }
                                        /**
                                         * Get ClaimantDW_PersonID
                                         */
                                        ClaimantNINO = DRecord.getClaimantsNationalInsuranceNumber();
                                        if (ClaimantNINO.trim().equalsIgnoreCase("")
                                                || ClaimantNINO.trim().startsWith("XX999")) {
                                            ClaimIDsOfInvalidClaimantNINOClaims.add(ClaimID);
                                        }
                                        ClaimantDOB = DRecord.getClaimantsDateOfBirth();
                                        ClaimantPersonID = DW_SHBE_Handler.getPersonID(
                                                ClaimantNINO,
                                                ClaimantDOB,
                                                NINOToNINOIDLookup,
                                                NINOIDToNINOLookup,
                                                DOBToDOBIDLookup,
                                                DOBIDToDOBLookup);
                                        /**
                                         * If this is a new claim then add to
                                         * appropriate index if the person was
                                         * previously a Claimant, Partner,
                                         * NonDependent or if the Person is
                                         * "new".
                                         */
                                        if (ClaimIDsOfNewSHBEClaims.contains(ClaimID)) {
                                            addToNew = true;
                                            if (AllClaimantPersonIDs.contains(ClaimantPersonID)) {
                                                ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore.add(ClaimID);
                                                addToNew = false;
                                            }
                                            if (AllPartnerPersonIDs.contains(ClaimantPersonID)) {
                                                ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.add(ClaimID);
                                                addToNew = false;
                                            }
                                            if (AllNonDependentIDs.contains(ClaimantPersonID)) {
                                                ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore.add(ClaimID);
                                                addToNew = false;
                                            }
                                            if (addToNew) {
                                                ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew.add(ClaimID);
                                            }
                                        }
                                        /**
                                         * If ClaimantDW_PersonID is already in
                                         * ClaimIDToClaimantPersonIDLookup.
                                         * then ClaimantDW_PersonID has multiple
                                         * claims in a month.
                                         */
                                        if (ClaimIDToClaimantPersonIDLookup.containsValue(ClaimantPersonID)) {
                                            /**
                                             * This claimant is in multiple
                                             * claims in this SHBE data. This
                                             * can happen and is expected to
                                             * happen for some travellers. Some
                                             * claimants have their NINO set to
                                             * a default like XX999999XX and it
                                             * is possible that multiple have
                                             * this default and the same date of
                                             * birth. In such cases this program
                                             * does not distinguish them, but
                                             * there may be other
                                             * characteristics such as gender,
                                             * age and ethnicity which could be
                                             * used to help differentiate.
                                             */
                                            key = Generic_Collections.getKeyFromValue(ClaimIDToClaimantPersonIDLookup,
                                                    ClaimantPersonID);
                                            Postcode = DRecord.getClaimantsPostcode();
                                            if (key != null) {
                                                otherClaimID = (DW_ID) key;
                                                otherClaimRef = ClaimIDToClaimRefLookup.get(otherClaimID);
                                                // Treat those paired records for Cottingley Springs Caravan Park differently
                                                if (Postcode.equalsIgnoreCase(Strings.CottingleySpringsCaravanParkPostcode)) {
//                                                    Env.logO("Cottingley Springs Caravan Park "
//                                                            + DW_Strings.CottingleySpringsCaravanParkPostcode
//                                                            + " ClaimRef " + ClaimRef + " paired with " + otherClaimRef
//                                                            + " one claim is for the pitch, the other is for rent of "
//                                                            + "a mobile home. ");
                                                    CottingleySpringsCaravanParkPairedClaimIDs.add(ClaimID);
                                                    CottingleySpringsCaravanParkPairedClaimIDs.add(otherClaimID);
                                                } else {
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    Env.logO(
//                                                            "Claimant with NINO " + ClaimantNINO
//                                                            + " DoB " + ClaimantDOB
//                                                            + " has mulitple claims. "
//                                                            + "The Claimant has had a second claim set up and the "
//                                                            + "previous claim is still on the system for some reason.");
//                                                    Env.logO("Current ClaimRef " + ClaimRef);
//                                                    Env.logO("Other ClaimRef " + otherClaimRef);
                                                    otherRecord = Records.get(otherClaimID);
                                                    if (otherRecord == null) {
                                                        env.logE("Unexpected error xx: This should not happen. "
                                                                + this.getClass().getName()
                                                                + ".DW_SHBE_Records(DW_Environment, File, String)");
                                                    } else {
//                                                        Env.logO("This D Record");
//                                                        Env.logO(DRecord.toStringBrief());
//                                                        Env.logO("Other D Record");
//                                                        Env.logO(otherRecord.DRecord.toStringBrief());
                                                        /**
                                                         * Add to
                                                         * ClaimantsWithMultipleClaimsInAMonth.
                                                         */
                                                        ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.add(ClaimID);
                                                        ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.add(otherRecord.getClaimID());
                                                        HashSet<DW_ID> set;
                                                        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.containsKey(ClaimantPersonID)) {
                                                            set = ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(ClaimantPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(ClaimantPersonID, set);
                                                        }
                                                        set.add(ClaimID);
                                                        set.add(otherClaimID);
                                                    }
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                            }
                                        }
                                        /**
                                         * If ClaimantPersonID is in
                                         * ClaimIDToPartnerPersonIDLookup,
                                         * then claimant is a partner in another
                                         * claim. Add to
                                         * ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim
                                         * and
                                         * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.
                                         */
                                        if (ClaimIDToPartnerPersonIDLookup.containsValue(ClaimantPersonID)) {
                                            /**
                                             * Ignore if this is a
                                             * CottingleySpringsCaravanParkPairedClaimIDs.
                                             * It may be that there are partners
                                             * shared in these claims, but such
                                             * a thing is ignored for now.
                                             */
                                            if (!CottingleySpringsCaravanParkPairedClaimIDs.contains(ClaimID)) {
                                                /**
                                                 * If Claimant is a Partner in
                                                 * another claim add to
                                                 * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim
                                                 * and
                                                 * ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.
                                                 */
                                                key = Generic_Collections.getKeyFromValue(ClaimIDToPartnerPersonIDLookup,
                                                        ClaimantPersonID);
                                                if (key != null) {
                                                    otherClaimID = (DW_ID) key;
                                                    HashSet<DW_ID> set;
                                                    ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.add(otherClaimID);
                                                }
                                                ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.add(ClaimID);
//                                                Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                Env.logO("Claimant with NINO " + ClaimantNINO
//                                                        + " DOB " + ClaimantDOB
//                                                        + " in ClaimRef " + ClaimRef
//                                                        + " is a Partner in " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                                Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                            }
                                        }
                                        DW_PersonID PartnerPersonID;
                                        PartnerPersonID = null;
                                        if (DRecord.getPartnerFlag() > 0) {
                                            /**
                                             * Add Partner.
                                             */
                                            PartnerPersonID = DW_SHBE_Handler.getPersonID(
                                                    DRecord.getPartnersNationalInsuranceNumber(),
                                                    DRecord.getPartnersDateOfBirth(),
                                                    NINOToNINOIDLookup,
                                                    NINOIDToNINOLookup,
                                                    DOBToDOBIDLookup,
                                                    DOBIDToDOBLookup);
                                            /**
                                             * If Partner is a Partner in
                                             * another claim add to
                                             * ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim
                                             * and
                                             * PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.
                                             */
                                            if (ClaimIDToPartnerPersonIDLookup.containsValue(PartnerPersonID)) {
                                                /*
                                             * Ignore if this is a CottingleySpringsCaravanParkPairedClaimIDs.
                                             * It may be that there are partners shared in these claims, but such
                                             * a thing is ignored for now.
                                                 */
                                                if (!CottingleySpringsCaravanParkPairedClaimIDs.contains(ClaimID)) {
                                                    key = Generic_Collections.getKeyFromValue(ClaimIDToPartnerPersonIDLookup,
                                                            PartnerPersonID);
                                                    if (key != null) {
                                                        otherClaimID = (DW_ID) key;
                                                        HashSet<DW_ID> set;
                                                        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimID);
                                                        set.add(otherClaimID);
                                                        ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.add(otherClaimID);
                                                    }
                                                    ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.add(ClaimID);
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    Env.logO("Partner with NINO " + NINOIDToNINOLookup.get(PartnerPersonID.getNINO_ID())
//                                                            + " DOB " + DOBIDToDOBLookup.get(PartnerPersonID.getDOB_ID())
//                                                            + " in ClaimRef " + ClaimRef
//                                                            + " is a Partner in " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                            }
                                            /**
                                             * If Partner is a Claimant in
                                             * another claim add to
                                             * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim
                                             * and
                                             * ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.
                                             */
                                            if (ClaimIDToClaimantPersonIDLookup.containsValue(PartnerPersonID)) {
                                                /**
                                                 * Ignore if this is a
                                                 * CottingleySpringsCaravanParkPairedClaimIDs.
                                                 * It may be that there are
                                                 * partners shared in these
                                                 * claims, but such a thing is
                                                 * ignored for now.
                                                 */
                                                if (!CottingleySpringsCaravanParkPairedClaimIDs.contains(ClaimID)) {
                                                    key = Generic_Collections.getKeyFromValue(ClaimIDToClaimantPersonIDLookup,
                                                            PartnerPersonID);
                                                    if (key != null) {
                                                        otherClaimID = (DW_ID) key;

                                                        HashSet<DW_ID> set;
                                                        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimID);
                                                        set.add(otherClaimID);
                                                        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimID);
                                                        set.add(otherClaimID);
                                                        ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.add(otherClaimID);
                                                    }
                                                    ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.add(ClaimID);
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    Env.logO("Partner with NINO " + NINOIDToNINOLookup.get(PartnerPersonID.getNINO_ID())
//                                                            + " DOB " + DOBIDToDOBLookup.get(PartnerPersonID.getDOB_ID())
//                                                            + " in ClaimRef " + ClaimRef
//                                                            + " is a Claimant in " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                                ClaimIDToPartnerPersonIDLookup.put(ClaimID, PartnerPersonID);
                                            }
                                        }
                                        /**
                                         * Add to
                                         * ClaimIDToClaimantPersonIDLookup.
                                         */
                                        ClaimIDToClaimantPersonIDLookup.put(ClaimID, ClaimantPersonID);

                                        /**
                                         * Add to AllClaimantPersonIDs and
                                         * AllPartnerPersonIDs.
                                         */
                                        AllClaimantPersonIDs.add(ClaimantPersonID);
                                        ClaimantPersonIDs.add(ClaimantPersonID);
                                        addToPersonIDToClaimRefsLookup(
                                                ClaimID,
                                                ClaimantPersonID,
                                                PersonIDToClaimIDsLookup);
                                        if (PartnerPersonID != null) {
                                            AllPartnerPersonIDs.add(PartnerPersonID);
                                            PartnerPersonIDs.add(PartnerPersonID);
                                            ClaimIDToPartnerPersonIDLookup.put(ClaimID, PartnerPersonID);
                                            addToPersonIDToClaimRefsLookup(
                                                    ClaimID,
                                                    PartnerPersonID,
                                                    PersonIDToClaimIDsLookup);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                env.logE(line);
                                env.logE("RecordID " + RecordID);
                                env.logE(e.getLocalizedMessage());
                                env.logE(e);
                                RecordIDsNotLoaded.add(RecordID);
                            }
                        }
                        lineCount++;
                        RecordID++;
                        break;
                }
                tokenType = st.nextToken();
            }
            env.log("</Read data>");

            br.close();

            /**
             * Add SRecords to Records. Add ClaimantDW_IDs from SRecords.
             */
            DW_ID DW_ID;
            DW_SHBE_Record DW_SHBE_Record;
            Iterator<DW_ID> ite;
            env.log("<Add SRecords>");
            ite = Records.keySet().iterator();
            while (ite.hasNext()) {
                DW_ID = ite.next();
                DW_SHBE_Record = Records.get(DW_ID);
                initSRecords(
                        DW_SHBE_Handler,
                        DW_SHBE_Record,
                        NINOToNINOIDLookup,
                        NINOIDToNINOLookup,
                        DOBToDOBIDLookup,
                        DOBIDToDOBLookup,
                        AllNonDependentIDs,
                        PersonIDToClaimIDsLookup,
                        ClaimIDToClaimRefLookup);
            }
            env.log("</Add SRecords>");

            env.log("<Summary Statistics>");
            /**
             * Add statistics to LoadSummary.
             */
            /**
             * Statistics on New SHBE Claims
             */
            addLoadSummaryCount(Strings.sCountOfNewSHBEClaims,
                    ClaimIDsOfNewSHBEClaims.size());
            addLoadSummaryCount(Strings.sCountOfNewSHBEClaimsWhereClaimantWasClaimantBefore,
                    ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore.size());
            addLoadSummaryCount(Strings.sCountOfNewSHBEClaimsWhereClaimantWasPartnerBefore,
                    ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.size());
            addLoadSummaryCount(Strings.sCountOfNewSHBEClaimsWhereClaimantWasNonDependentBefore,
                    ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore.size());
            addLoadSummaryCount(Strings.sCountOfNewSHBEClaimsWhereClaimantIsNew,
                    ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew.size());
            /**
             * Statistics on Postcodes
             */
            addLoadSummaryCount(
                    Strings.sCountOfNewClaimantPostcodes,
                    CountOfNewClaimantPostcodes);
            addLoadSummaryCount(
                    Strings.sCountOfNewValidMappableClaimantPostcodes,
                    CountOfNewMappableClaimantPostcodes);
            addLoadSummaryCount(Strings.sCountOfMappableClaimantPostcodes,
                    CountOfMappableClaimantPostcodes);
            addLoadSummaryCount(
                    Strings.sCountOfNonMappableClaimantPostcodes,
                    CountOfNonMappableClaimantPostcodes);
            addLoadSummaryCount(
                    Strings.sCountOfInvalidFormatClaimantPostcodes,
                    CountOfValidFormatClaimantPostcodes);
            /**
             * General count statistics
             */
            addLoadSummaryCount(
                    Strings.sCountOfClaims,
                    Records.size());
            addLoadSummaryCount(
                    Strings.sCountOfCTBClaims,
                    totalCouncilTaxBenefitClaims);
            addLoadSummaryCount(
                    Strings.sCountOfCTBAndHBClaims,
                    totalCouncilTaxAndHousingBenefitClaims);
            addLoadSummaryCount(
                    Strings.sCountOfHBClaims,
                    totalHousingBenefitClaims);
            addLoadSummaryCount(
                    Strings.sCountOfRecords,
                    Records.size());
            addLoadSummaryCount(
                    Strings.sCountOfSRecords,
                    countSRecords);
            addLoadSummaryCount(
                    Strings.sCountOfSRecordsNotLoaded,
                    SRecordNotLoadedCount);
            addLoadSummaryCount(
                    Strings.sCountOfIncompleteDRecords,
                    NumberOfIncompleteDRecords);
            addLoadSummaryCount(
                    Strings.sCountOfRecordIDsNotLoaded,
                    RecordIDsNotLoaded.size());
            HashSet<DW_PersonID> set;
            HashSet<DW_PersonID> allSet;
            allSet = new HashSet<DW_PersonID>();
            /**
             * Claimants
             */
            set = new HashSet<DW_PersonID>();
            set.addAll(ClaimIDToClaimantPersonIDLookup.values());
            allSet.addAll(set);
            addLoadSummaryCount(
                    Strings.sCountOfUniqueClaimants,
                    set.size());
            /**
             * Partners
             */
            addLoadSummaryCount(Strings.sCountOfClaimsWithPartners,
                    ClaimIDToPartnerPersonIDLookup.size());
            set = DW_SHBE_Handler.getUniquePersonIDs0(ClaimIDToPartnerPersonIDLookup);
            allSet.addAll(set);
            addLoadSummaryCount(
                    Strings.sCountOfUniquePartners,
                    set.size());
            /**
             * Dependents
             */
            int nDependents;
            nDependents = DW_Collections.getCountHashMap_DW_ID__HashSet_DW_PersonID(ClaimIDToDependentPersonIDsLookup);
            addLoadSummaryCount(
                    Strings.sCountOfDependentsInAllClaims,
                    nDependents);
            set = DW_SHBE_Handler.getUniquePersonIDs(ClaimIDToDependentPersonIDsLookup);
            allSet.addAll(set);
            int CountOfUniqueDependents = set.size();
            addLoadSummaryCount(
                    Strings.sCountOfUniqueDependents,
                    CountOfUniqueDependents);
            /**
             * NonDependents
             */
            int nNonDependents;
            nNonDependents = DW_Collections.getCountHashMap_DW_ID__HashSet_DW_PersonID(ClaimIDToNonDependentPersonIDsLookup);
            addLoadSummaryCount(
                    Strings.sCountOfNonDependentsInAllClaims,
                    nNonDependents);
            set = DW_SHBE_Handler.getUniquePersonIDs(ClaimIDToNonDependentPersonIDsLookup);
            allSet.addAll(set);
            int CountOfUniqueNonDependents = set.size();
            addLoadSummaryCount(
                    Strings.sCountOfUniqueNonDependents,
                    CountOfUniqueNonDependents);
            /**
             * All individuals
             */
            addLoadSummaryCount(
                    Strings.sCountOfIndividuals,
                    allSet.size());
            /**
             * Counts of: ClaimsWithClaimantsThatAreClaimantsInAnotherClaim
             * ClaimsWithClaimantsThatArePartnersInAnotherClaim
             * ClaimsWithPartnersThatAreClaimantsInAnotherClaim
             * ClaimsWithPartnersThatArePartnersInAnotherClaim
             * ClaimantsInMultipleClaimsInAMonth
             * PartnersInMultipleClaimsInAMonth
             * NonDependentsInMultipleClaimsInAMonth
             */
            addLoadSummaryCount(Strings.sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim,
                    ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.size());
            addLoadSummaryCount(Strings.sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim,
                    ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.size());
            addLoadSummaryCount(Strings.sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim,
                    ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.size());
            addLoadSummaryCount(Strings.sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim,
                    ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.size());
            addLoadSummaryCount(Strings.sCountOfClaimantsInMultipleClaimsInAMonth,
                    ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.size());
            addLoadSummaryCount(Strings.sCountOfPartnersInMultipleClaimsInAMonth,
                    PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.size());
            addLoadSummaryCount(Strings.sCountOfNonDependentsInMultipleClaimsInAMonth,
                    NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.size());
            addLoadSummaryCount(Strings.sLineCount,
                    lineCount);
            addLoadSummaryCount(Strings.sTotalIncome,
                    grandTotalIncome);
            addLoadSummaryCount(Strings.sTotalIncomeGreaterThanZeroCount,
                    totalIncomeGreaterThanZeroCount);
            addLoadSummaryCount(Strings.sAverage_NonZero_Income,
                    grandTotalIncome / (double) totalIncomeGreaterThanZeroCount);
            addLoadSummaryCount(Strings.sTotalWeeklyEligibleRentAmount,
                    grandTotalWeeklyEligibleRentAmount);
            addLoadSummaryCount(Strings.sTotalWeeklyEligibleRentAmountGreaterThanZeroCount,
                    totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            addLoadSummaryCount(Strings.sAverage_NonZero_WeeklyEligibleRentAmount,
                    grandTotalWeeklyEligibleRentAmount / (double) totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            env.log("<Summary Statistics>");

            /**
             * Write out data.
             */
            Generic_StaticIO.writeObject(Records, getRecordsFile());
            Generic_StaticIO.writeObject(ClaimIDsOfNewSHBEClaims,
                    getClaimIDsOfNewSHBEClaimsFile());
            Generic_StaticIO.writeObject(ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore,
                    getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile());
            Generic_StaticIO.writeObject(ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore,
                    getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile());
            Generic_StaticIO.writeObject(ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore,
                    getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile());
            Generic_StaticIO.writeObject(ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew,
                    getClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile());
            Generic_StaticIO.writeObject(ClaimantPersonIDs, getClaimantPersonIDsFile());
            Generic_StaticIO.writeObject(PartnerPersonIDs, getPartnerPersonIDsFile());
            Generic_StaticIO.writeObject(NonDependentPersonIDs, getNonDependentPersonIDsFile());
            Generic_StaticIO.writeObject(CottingleySpringsCaravanParkPairedClaimIDs,
                    getCottingleySpringsCaravanParkPairedClaimIDsFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfHBAtExtractDateInPayment,
                    getClaimIDsWithStatusOfHBAtExtractDateInPaymentFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfHBAtExtractDateSuspended,
                    getClaimIDsWithStatusOfHBAtExtractDateSuspendedFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfHBAtExtractDateOther,
                    getClaimIDsWithStatusOfHBAtExtractDateOtherFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfCTBAtExtractDateInPayment,
                    getClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfCTBAtExtractDateSuspended,
                    getClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile());
            Generic_StaticIO.writeObject(ClaimIDsWithStatusOfCTBAtExtractDateOther,
                    getClaimIDsWithStatusOfCTBAtExtractDateOtherFile());
            Generic_StaticIO.writeObject(SRecordsWithoutDRecords, getSRecordsWithoutDRecordsFile());
            Generic_StaticIO.writeObject(ClaimIDAndCountOfRecordsWithSRecords,
                    getClaimIDAndCountOfRecordsWithSRecordsFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithoutAMappableClaimantPostcode,
                    getClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile());
            Generic_StaticIO.writeObject(ClaimIDToClaimantPersonIDLookup,
                    getClaimIDToClaimantPersonIDLookupFile());
            Generic_StaticIO.writeObject(ClaimIDToPartnerPersonIDLookup,
                    getClaimIDToPartnerPersonIDLookupFile());
            Generic_StaticIO.writeObject(ClaimIDToNonDependentPersonIDsLookup,
                    getClaimIDToNonDependentPersonIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimIDToDependentPersonIDsLookup,
                    getClaimIDToDependentPersonIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim,
                    getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim,
                    getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim,
                    getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim,
                    getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim,
                    getClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile());
            Generic_StaticIO.writeObject(PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile());
            Generic_StaticIO.writeObject(NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimIDToPostcodeIDLookup, getClaimIDToPostcodeIDLookupFile());
            Generic_StaticIO.writeObject(ClaimIDToTenancyTypeLookup, getClaimIDToTenancyTypeLookupFile());
            Generic_StaticIO.writeObject(LoadSummary, getLoadSummaryFile());
            Generic_StaticIO.writeObject(RecordIDsNotLoaded, getRecordIDsNotLoadedFile());
            Generic_StaticIO.writeObject(ClaimIDsOfInvalidClaimantNINOClaims, getClaimIDsOfInvalidClaimantNINOClaimsFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesUnmappable, getClaimantPostcodesUnmappableFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesModified, getClaimantPostcodesModifiedFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes, getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile());
            Generic_StaticIO.writeObject(ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture, getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile());

            // Write out other outputs
            // Write out ClaimRefs of ClaimantsInMultipleClaimsInAMonth
            String YMN;
            YMN = DW_SHBE_Handler.getYearMonthNumber(inputFilename);
            writeOut(ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    logDir,
                    "ClaimantsInMultipleClaimsInAMonth",
                    YMN,
                    ClaimIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of PartnersInMultipleClaimsInAMonth
            writeOut(PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    logDir,
                    "PartnersInMultipleClaimsInAMonth",
                    YMN,
                    ClaimIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of PartnersInMultipleClaimsInAMonth
            writeOut(NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
                    logDir,
                    "NonDependentsInMultipleClaimsInAMonth",
                    YMN,
                    ClaimIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of ClaimIDOfInvalidClaimantNINOClaims
            String name = "ClaimRefsOfInvalidClaimantNINOClaims";
            File dir;
            dir = new File(
                    logDir,
                    name);
            dir.mkdirs();
            File f = new File(
                    dir,
                    name + YMN + ".csv");
            PrintWriter pw;
            pw = null;
            try {
                pw = new PrintWriter(f);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DW_SHBE_Records.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.println("ClaimRefs");
            Iterator<DW_ID> ite2;
            ite2 = ClaimIDsOfInvalidClaimantNINOClaims.iterator();
            while (ite2.hasNext()) {
                DW_ID = ite2.next();
                pw.println(ClaimIDToClaimRefLookup.get(DW_ID));
            }
            pw.close();
            env.log("----------------------");
            env.log("Loaded " + YM3);
            env.log("----------------------");
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeOut(
            HashMap<DW_PersonID, HashSet<DW_ID>> InMultipleClaimsInAMonthPersonIDToClaimIDsLookup,
            File logDir,
            String name,
            String YMN,
            HashMap<DW_ID, String> ClaimIDToClaimRefLookup,
            HashMap<DW_ID, String> NINOIDToNINOLookup,
            HashMap<DW_ID, String> DOBIDToDOBLookup) {
        File dir;
        PrintWriter pw = null;
        HashSet<DW_ID> ClaimRefs;
        Iterator<DW_PersonID> ite2;
        Iterator<DW_ID> ite3;
        File f;
        String s;
        DW_ID DW_ID;
        DW_PersonID PersonID;
        String NINO;
        String DOB;
        dir = new File(
                logDir,
                name);
        dir.mkdirs();
        f = new File(
                dir,
                name + YMN + ".csv");
        try {
            pw = new PrintWriter(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DW_SHBE_Records.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.println("NINO,DOB,ClaimRefs");
        ite2 = InMultipleClaimsInAMonthPersonIDToClaimIDsLookup.keySet().iterator();
        while (ite2.hasNext()) {
            PersonID = ite2.next();
            NINO = NINOIDToNINOLookup.get(PersonID.getNINO_ID());
            DOB = DOBIDToDOBLookup.get(PersonID.getDOB_ID());
            if (!NINO.trim().equalsIgnoreCase("")) {
                if (!NINO.trim().startsWith("XX999")) {
                    ClaimRefs = InMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(PersonID);
                    ite3 = ClaimRefs.iterator();
                    s = NINO + "," + DOB;
                    while (ite3.hasNext()) {
                        DW_ID = ite3.next();
                        s += "," + ClaimIDToClaimRefLookup.get(DW_ID);
                    }
                    pw.println(s);
                }
            }
        }
        pw.close();
    }

    /**
     * logs and adds s and n to LoadSummary.
     *
     * @param s
     * @param n
     */
    public final void addLoadSummaryCount(String s, Number n) {
        Env.logO(s + " " + n, true);
        LoadSummary.put(s, n);
    }

    /**
     *
     * @param DW_SHBE_Handler
     * @param DW_SHBE_Record
     * @param NINOToNINOIDLookup
     * @param NINOIDToNINOLookup
     * @param DOBToDOBIDLookup
     * @param DOBIDToDOBLookup
     * @param AllNonDependentPersonIDs
     * @param PersonIDToClaimRefsLookup
     * @param ClaimIDToClaimRefLookup
     */
    public final void initSRecords(
            DW_SHBE_Handler DW_SHBE_Handler,
            DW_SHBE_Record DW_SHBE_Record,
            HashMap<String, DW_ID> NINOToNINOIDLookup,
            HashMap<DW_ID, String> NINOIDToNINOLookup,
            HashMap<String, DW_ID> DOBToDOBIDLookup,
            HashMap<DW_ID, String> DOBIDToDOBLookup,
            HashSet<DW_PersonID> AllNonDependentPersonIDs,
            HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimRefsLookup,
            HashMap<DW_ID, String> ClaimIDToClaimRefLookup
    ) {
        ArrayList<DW_SHBE_S_Record> SRecordsForClaim;
        DW_ID ClaimID;
        ClaimID = DW_SHBE_Record.ClaimID;
        Iterator<DW_SHBE_S_Record> ite;
        DW_SHBE_S_Record SRecord;
        String ClaimantsNINO;
        SRecordsForClaim = getSRecordsWithoutDRecords().get(ClaimID);
        if (SRecordsForClaim != null) {
            // Declare variables
            DW_PersonID DW_PersonID;
            String NINO;
            String DOB;
            int SubRecordType;
            Object key;
            DW_ID otherClaimID;
            ite = SRecordsForClaim.iterator();
            while (ite.hasNext()) {
                SRecord = ite.next();
                NINO = SRecord.getSubRecordChildReferenceNumberOrNINO();
                DOB = SRecord.getSubRecordDateOfBirth();
                SubRecordType = SRecord.getSubRecordType();
                switch (SubRecordType) {
                    case 1:
                        ClaimantsNINO = SRecord.getClaimantsNationalInsuranceNumber();
                        if (ClaimantsNINO.trim().isEmpty()) {
                            ClaimantsNINO = Strings.sDefaultNINO;
                            Env.logE("ClaimantsNINO is empty for "
                                    + "ClaimID " + ClaimID + " ClaimRef "
                                    + Env.getSHBE_Data().getClaimIDToClaimRefLookup().get(ClaimID)
                                    + " Setting as default NINO " + ClaimantsNINO);
                        }
                        int i;
                        i = 0;
                        if (NINO.isEmpty()) {
                            boolean set;
                            set = false;
                            while (!set) {
                                NINO = "" + i;
                                NINO += "_" + ClaimantsNINO;
                                if (NINOToNINOIDLookup.containsKey(NINO)) {
                                    Env.logO(Env.DEBUG_Level_FINEST,
                                            "NINO " + NINO + " is not unique for " + ClaimantsNINO);
                                } else {
                                    set = true;
                                }
                                i++;
                            }
                        } else {
                            boolean set;
                            set = false;
                            NINO += "_" + ClaimantsNINO;
                            if (NINOToNINOIDLookup.containsKey(NINO)) {
                                /**
                                 * If the claimant has more than one claim, this
                                 * is fine. Otherwise we have to do something.
                                 */
                                if (ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.contains(ClaimID)) {
                                    set = true;
                                } else {
                                    Env.logO(Env.DEBUG_Level_FINEST,
                                            "NINO " + NINO + " is not unique for " + ClaimantsNINO
                                            + " and ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim does not contain "
                                            + "ClaimID " + ClaimID + " for ClaimRef "
                                            + Env.getSHBE_Data().getClaimIDToClaimRefLookup().get(ClaimID));
                                }
                            } else {
                                set = true;
                            }
                            while (!set) {
                                NINO = "" + i;
                                NINO += "_" + ClaimantsNINO;
                                if (NINOToNINOIDLookup.containsKey(NINO)) {
                                    Env.logO(Env.DEBUG_Level_FINEST,
                                            "NINO " + NINO + " is not unique for " + ClaimantsNINO);
                                } else {
                                    set = true;
                                }
                                i++;
                            }
                        }
                        DW_PersonID = DW_SHBE_Handler.getPersonID(
                                NINO,
                                DOB,
                                NINOToNINOIDLookup,
                                NINOIDToNINOLookup,
                                DOBToDOBIDLookup,
                                DOBIDToDOBLookup);
                        /**
                         * Add to ClaimIDToDependentPersonIDsLookup.
                         */
                        HashSet<DW_PersonID> s;
                        s = ClaimIDToDependentPersonIDsLookup.get(ClaimID);
                        if (s == null) {
                            s = new HashSet<DW_PersonID>();
                            ClaimIDToDependentPersonIDsLookup.put(ClaimID, s);
                        }
                        s.add(DW_PersonID);
                        addToPersonIDToClaimRefsLookup(
                                ClaimID,
                                DW_PersonID,
                                PersonIDToClaimRefsLookup);
                        break;
                    case 2:
                        DW_PersonID = DW_SHBE_Handler.getPersonID(
                                NINO,
                                DOB,
                                NINOToNINOIDLookup,
                                NINOIDToNINOLookup,
                                DOBToDOBIDLookup,
                                DOBIDToDOBLookup);
                        /**
                         * Ignore if this is a
                         * CottingleySpringsCaravanParkPairedClaimIDs. It may
                         * be that there are partners shared in these claims,
                         * but such a thing is ignored for now.
                         */
                        if (!CottingleySpringsCaravanParkPairedClaimIDs.contains(ClaimID)) {
                            /**
                             * If NonDependent is a NonDependent in another
                             * claim add to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.
                             */
                            key = DW_Collections.getKeyOfSetValue(ClaimIDToNonDependentPersonIDsLookup, DW_PersonID);
                            if (key != null) {
                                otherClaimID = (DW_ID) key;
                                HashSet<DW_ID> set;
                                set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(DW_PersonID);
                                if (set == null) {
                                    set = new HashSet<DW_ID>();
                                    NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(DW_PersonID, set);
                                }
                                set.add(ClaimID);
                                set.add(otherClaimID);
                                ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimID);
                                ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimID);
//                                if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    Env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                            + " is in ClaimRef " + ClaimIDToClaimRefLookup.get(ClaimID)
//                                            + " and " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                    Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                }
                            }
                            /**
                             * If NonDependent is a Claimant in another claim
                             * add to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.
                             */
                            if (ClaimIDToClaimantPersonIDLookup.containsValue(DW_PersonID)) {
                                if (key != null) {
                                    otherClaimID = (DW_ID) key;
                                    HashSet<DW_ID> set;
                                    set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(DW_PersonID);
                                    if (set == null) {
                                        set = new HashSet<DW_ID>();
                                        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(DW_PersonID, set);
                                    }
                                    set.add(ClaimID);
                                    set.add(otherClaimID);
                                    ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimID);
                                    ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimID);
//                                    if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                        Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                        Env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                                + " in ClaimRef " + ClaimIDToClaimRefLookup.get(ClaimID)
//                                                + " is a Claimant in " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                        Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    }
                                }
                            }
                            /**
                             * If NonDependent is a Partner in another claim add
                             * to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;
                             */
                            if (ClaimIDToPartnerPersonIDLookup.containsValue(DW_PersonID)) {
                                if (key != null) {
                                    otherClaimID = (DW_ID) key;
                                    HashSet<DW_ID> set;
                                    set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.get(DW_PersonID);
                                    if (set == null) {
                                        set = new HashSet<DW_ID>();
                                        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup.put(DW_PersonID, set);
                                    }
                                    set.add(ClaimID);
                                    set.add(otherClaimID);
                                    ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimID);
                                    ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimID);
//                                    if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                        Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                        Env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                                + " in ClaimRef " + ClaimIDToClaimRefLookup.get(ClaimID)
//                                                + " is a Partner in " + ClaimIDToClaimRefLookup.get(otherClaimID));
//                                        Env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    }
                                }
                            }
                        }
                        //HashSet<DW_PersonID> s;
                        s = ClaimIDToNonDependentPersonIDsLookup.get(ClaimID);
                        if (s == null) {
                            s = new HashSet<DW_PersonID>();
                            ClaimIDToNonDependentPersonIDsLookup.put(ClaimID, s);
                        }
                        s.add(DW_PersonID);
                        NonDependentPersonIDs.add(DW_PersonID);
                        AllNonDependentPersonIDs.add(DW_PersonID);
                        addToPersonIDToClaimRefsLookup(
                                ClaimID,
                                DW_PersonID,
                                PersonIDToClaimRefsLookup);
                        break;
                    default:
                        Env.logE("Unrecognised SubRecordType " + SubRecordType);
                        break;
                }
            }
            DW_SHBE_Record.SRecords = SRecordsForClaim;
            ClaimIDAndCountOfRecordsWithSRecords.put(ClaimID, SRecordsForClaim.size());
        }
        /**
         * Remove all assigned SRecords from SRecordsWithoutDRecords.
         */
        Iterator<DW_ID> iteID;
        iteID = ClaimIDAndCountOfRecordsWithSRecords.keySet().iterator();
        while (iteID.hasNext()) {
            SRecordsWithoutDRecords.remove(iteID.next());
        }
    }

    private void addToPersonIDToClaimRefsLookup(
            DW_ID ClaimID,
            DW_PersonID DW_PersonID,
            HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimRefsLookup) {
        HashSet<DW_ID> s;
        if (PersonIDToClaimRefsLookup.containsKey(DW_PersonID)) {
            s = PersonIDToClaimRefsLookup.get(DW_PersonID);
        } else {
            s = new HashSet<DW_ID>();
            PersonIDToClaimRefsLookup.put(DW_PersonID, s);
        }
        s.add(ClaimID);
    }

    /**
     * Month_10_2010_11_381112_D_records.csv
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * hb9803_SHBE_206728k\ April\ 2008.csv
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * 1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341
     * 307, 315
     *
     * @param directory
     * @param filename
     * @return
     */
    public final int readAndCheckFirstLine(File directory, String filename) {
        int type = 0;
        String type0Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,"
                + "23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,"
                + "43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,"
                + "63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,"
                + "83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,"
                + "102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,"
                + "117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,"
                + "135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,"
                + "150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,"
                + "165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,"
                + "180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,"
                + "195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,"
                + "210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,"
                + "226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,"
                + "241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,"
                + "256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,"
                + "271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,"
                + "293,294,295,296,297,298,299,308,309,310,311,316,317,318,319,"
                + "320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,"
                + "335,336,337,338,339,340,341";
        String type1Header = "1,2,3,4,8,9,11,12,13,14,15,16,17,18,19,20,21,22,"
                + "23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,"
                + "43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,"
                + "63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,"
                + "83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,"
                + "102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,"
                + "117,118,119,120,121,122,123,124,125,126,130,131,132,133,134,"
                + "135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,"
                + "150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,"
                + "165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,"
                + "180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,"
                + "195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,"
                + "210,211,213,214,215,216,217,218,219,220,221,222,223,224,225,"
                + "226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,"
                + "241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,"
                + "256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,"
                + "271,272,273,274,275,276,277,278,284,285,286,287,290,291,292,"
                + "293,294,295,296,297,298,299,307,308,309,310,311,315,316,317,"
                + "318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,"
                + "333,334,335,336,337,338,339,340,341";
        File inputFile = new File(directory, filename);
        try {
            BufferedReader br = Generic_StaticIO.getBufferedReader(inputFile);
            StreamTokenizer st = new StreamTokenizer(br);
            Generic_StaticIO.setStreamTokenizerSyntax5(st);
            st.wordChars('`', '`');
            int tokenType;
            tokenType = st.nextToken();
            String line = "";
            while (tokenType != StreamTokenizer.TT_EOL) {
                switch (tokenType) {
                    case StreamTokenizer.TT_WORD:
                        line += st.sval;
                        break;
                }
                tokenType = st.nextToken();
            }
            br.close();
            if (line.startsWith(type0Header)) {
                return 0;
            }
            if (line.startsWith(type1Header)) {
                return 1;
            } else {
                String[] lineSplit = line.split(",");
                Env.logE("Unrecognised header in DW_SHBE_Records.readAndCheckFirstLine(File,String)");
                Env.logE("Number of fields in header " + lineSplit.length);
                Env.logE("header:");
                Env.logE(line);

            }
        } catch (IOException ex) {
            Logger.getLogger(DW_SHBE_Handler.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return 2;
    }

    /**
     * @return the InputFile
     */
    public File getInputFile() {
        return InputFile;
    }

    /**
     * If not initialised, initialises ClaimIDToClaimantPersonIDLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_PersonID> getClaimIDToClaimantPersonIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToClaimantPersonIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToClaimantPersonIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDToClaimantPersonIDLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, DW_PersonID> getClaimIDToClaimantPersonIDLookup() {
        if (ClaimIDToClaimantPersonIDLookup == null) {
            File f;
            f = getClaimIDToClaimantPersonIDLookupFile();
            if (f.exists()) {
                ClaimIDToClaimantPersonIDLookup = (HashMap<DW_ID, DW_PersonID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToClaimantPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
            }
        }
        return ClaimIDToClaimantPersonIDLookup;
    }

    /**
     * If not initialised, initialises ClaimIDToPartnerPersonIDLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_PersonID> getClaimIDToPartnerPersonIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToPartnerPersonIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToPartnerPersonIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDToPartnerPersonIDLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, DW_PersonID> getClaimIDToPartnerPersonIDLookup() {
        if (ClaimIDToPartnerPersonIDLookup == null) {
            File f;
            f = getClaimIDToPartnerPersonIDLookupFile();
            if (f.exists()) {
                ClaimIDToPartnerPersonIDLookup = (HashMap<DW_ID, DW_PersonID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToPartnerPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
            }
        }
        return ClaimIDToPartnerPersonIDLookup;
    }

    /**
     * If not initialised, initialises ClaimIDToDependentPersonIDsLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, HashSet<DW_PersonID>> getClaimIDToDependentPersonIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToDependentPersonIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToDependentPersonIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDToDependentPersonIDsLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, HashSet<DW_PersonID>> getClaimIDToDependentPersonIDsLookup() {
        if (ClaimIDToDependentPersonIDsLookup == null) {
            File f;
            f = getClaimIDToDependentPersonIDsLookupFile();
            if (f.exists()) {
                ClaimIDToDependentPersonIDsLookup = (HashMap<DW_ID, HashSet<DW_PersonID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
            }
        }
        return ClaimIDToDependentPersonIDsLookup;
    }

    /**
     * If not initialised, initialises ClaimIDToNonDependentPersonIDsLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, HashSet<DW_PersonID>> getClaimIDToNonDependentPersonIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToNonDependentPersonIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToNonDependentPersonIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDToNonDependentPersonIDsLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, HashSet<DW_PersonID>> getClaimIDToNonDependentPersonIDsLookup() {
        if (ClaimIDToNonDependentPersonIDsLookup == null) {
            File f;
            f = getClaimIDToNonDependentPersonIDsLookupFile();
            if (f.exists()) {
                ClaimIDToNonDependentPersonIDsLookup = (HashMap<DW_ID, HashSet<DW_PersonID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToNonDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
            }
        }
        return ClaimIDToNonDependentPersonIDsLookup;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim() {
        if (ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim == null) {
            File f;
            f = getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim() {
        if (ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim == null) {
            File f;
            f = getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim() {
        if (ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim == null) {
            File f;
            f = getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim() {
        if (ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim == null) {
            File f;
            f = getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim() {
        if (ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim == null) {
            File f;
            f = getClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup then returns
     * it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup() {
        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup == null) {
            File f;
            f = getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile();
            if (f.exists()) {
                ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;
    }

    /**
     * If not initialised, initialises
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup then returns it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup() {
        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup == null) {
            File f;
            f = getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile();
            if (f.exists()) {
                PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;
    }

    /**
     * If not initialised, initialises
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup() {
        if (NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup == null) {
            File f;
            f = getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile();
            if (f.exists()) {
                NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup;
    }

    /**
     * If not initialised, initialises ClaimIDToPostcodeLookup then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_ID> getClaimIDToPostcodeIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToPostcodeIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToPostcodeIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimIDToPostcodeLookup
     */
    protected HashMap<DW_ID, DW_ID> getClaimIDToPostcodeIDLookup() {
        if (ClaimIDToPostcodeIDLookup == null) {
            File f;
            f = getClaimIDToPostcodeIDLookupFile();
            if (f.exists()) {
                ClaimIDToPostcodeIDLookup = (HashMap<DW_ID, DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToPostcodeIDLookup = new HashMap<DW_ID, DW_ID>();
            }
        }
        return ClaimIDToPostcodeIDLookup;
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture then returns it.
     *
     * @param handleOutOfMemoryError
     * @return ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture
     */
    public final HashSet<DW_ID> getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture then returns it.
     *
     * @return ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture
     */
    protected HashSet<DW_ID> getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture() {
        if (ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture == null) {
            File f;
            f = getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile();
            if (f.exists()) {
                ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture;
    }

    /**
     * If not initialised, initialises ClaimIDToTenancyTypeLookup then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return ClaimIDToTenancyTypeLookup
     */
    public final HashMap<DW_ID, Integer> getClaimIDToTenancyTypeLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDToTenancyTypeLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDToTenancyTypeLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimIDToTenancyTypeLookup then returns
     * it.
     *
     * @return ClaimIDToTenancyTypeLookup
     */
    protected HashMap<DW_ID, Integer> getClaimIDToTenancyTypeLookup() {
        if (ClaimIDToTenancyTypeLookup == null) {
            File f;
            f = getClaimIDToTenancyTypeLookupFile();
            if (f.exists()) {
                ClaimIDToTenancyTypeLookup = (HashMap<DW_ID, Integer>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
            }
        }
        return ClaimIDToTenancyTypeLookup;
    }

    /**
     * If not initialised, initialises LoadSummary then returns it.
     *
     * @param handleOutOfMemoryError
     * @return LoadSummary
     */
    public final HashMap<String, Number> getLoadSummary(boolean handleOutOfMemoryError) {
        try {
            return getLoadSummary();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getLoadSummary(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises LoadSummary then returns it.
     *
     * @return LoadSummary
     */
    protected HashMap<String, Number> getLoadSummary() {
        if (LoadSummary == null) {
            File f;
            f = getLoadSummaryFile();
            if (f.exists()) {
                LoadSummary = (HashMap<String, Number>) Generic_StaticIO.readObject(f);
            } else {
                LoadSummary = new HashMap<String, Number>();
            }
        }
        return LoadSummary;
    }

    /**
     * If not initialised, initialises RecordIDsNotLoaded then returns it.
     *
     * @param handleOutOfMemoryError
     * @return RecordIDsNotLoaded
     */
    public final ArrayList<Long> getRecordIDsNotLoaded(boolean handleOutOfMemoryError) {
        try {
            return getRecordIDsNotLoaded();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getRecordIDsNotLoaded(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises RecordIDsNotLoaded then returns it.
     *
     * @return RecordIDsNotLoaded
     */
    protected ArrayList<Long> getRecordIDsNotLoaded() {
        if (RecordIDsNotLoaded == null) {
            File f;
            f = getRecordIDsNotLoadedFile();
            if (f.exists()) {
                RecordIDsNotLoaded = (ArrayList<Long>) Generic_StaticIO.readObject(f);
            } else {
                RecordIDsNotLoaded = new ArrayList<Long>();
            }
        }
        return RecordIDsNotLoaded;
    }

    /**
     * If not initialised, initialises ClaimRefsOfInvalidClaimantNINOClaims then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return ClaimIDsOfInvalidClaimantNINOClaims
     */
    public final HashSet<DW_ID> getClaimIDsOfInvalidClaimantNINOClaims(boolean handleOutOfMemoryError) {
        try {
            return getClaimIDsOfInvalidClaimantNINOClaims();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimIDsOfInvalidClaimantNINOClaims(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimRefsOfInvalidClaimantNINOClaims then
     * returns it.
     *
     * @return ClaimIDsOfInvalidClaimantNINOClaims
     */
    protected HashSet<DW_ID> getClaimIDsOfInvalidClaimantNINOClaims() {
        if (ClaimIDsOfInvalidClaimantNINOClaims == null) {
            File f;
            f = getClaimIDsOfInvalidClaimantNINOClaimsFile();
            if (f.exists()) {
                ClaimIDsOfInvalidClaimantNINOClaims = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimIDsOfInvalidClaimantNINOClaims = new HashSet<DW_ID>();
            }
        }
        return ClaimIDsOfInvalidClaimantNINOClaims;
    }

    /**
     * If not initialised, initialises ClaimantPostcodesUnmappable then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return ClaimantPostcodesUnmappable
     */
    public final HashMap<DW_ID, String> getClaimantPostcodesUnmappable(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPostcodesUnmappable();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimantPostcodesUnmappable(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimantPostcodesUnmappable then returns
     * it.
     *
     * @return ClaimantPostcodesUnmappable
     */
    protected HashMap<DW_ID, String> getClaimantPostcodesUnmappable() {
        if (ClaimantPostcodesUnmappable == null) {
            File f;
            f = getClaimantPostcodesUnmappableFile();
            if (f.exists()) {
                ClaimantPostcodesUnmappable = (HashMap<DW_ID, String>) Generic_StaticIO.readObject(f);
            } else {
                ClaimantPostcodesUnmappable = new HashMap<DW_ID, String>();
            }
        }
        return ClaimantPostcodesUnmappable;
    }

    /**
     * If not initialised, initialises ClaimantPostcodesModified then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return ClaimantPostcodesModified
     */
    public final HashMap<DW_ID, String[]> getClaimantPostcodesModified(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPostcodesModified();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimantPostcodesModified(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimantPostcodesModified then returns
     * it.
     *
     * @return ClaimantPostcodesModified
     */
    protected HashMap<DW_ID, String[]> getClaimantPostcodesModified() {
        if (ClaimantPostcodesModified == null) {
            File f;
            f = getClaimantPostcodesModifiedFile();
            if (f.exists()) {
                ClaimantPostcodesModified = (HashMap<DW_ID, String[]>) Generic_StaticIO.readObject(f);
            } else {
                ClaimantPostcodesModified = new HashMap<DW_ID, String[]>();
            }
        }
        return ClaimantPostcodesModified;
    }

    /**
     * If not initialised, initialises
     * ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, String> getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes then returns it.
     *
     * @return ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes
     */
    protected HashMap<DW_ID, String> getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes() {
        if (ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes == null) {
            File f;
            f = getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile();
            if (f.exists()) {
                ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = (HashMap<DW_ID, String>) Generic_StaticIO.readObject(f);
            } else {
                ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = new HashMap<DW_ID, String>();
            }
        }
        return ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes;
    }

    /**
     * @return the DataFile
     */
    protected final File getFile() {
        if (File == null) {
            File = getFile(
                    "DW_SHBE_Records"
                    + Strings.sBinaryFileExtension);
        }
        return File;
    }

    /**
     * @return RecordsFile initialising if it is not already initialised.
     */
    protected final File getRecordsFile() {
        if (RecordsFile == null) {
            RecordsFile = getFile(
                    Strings.sRecords
                    + Strings.sUnderscore
                    + "HashMap_String__DW_SHBE_Record"
                    + Strings.sBinaryFileExtension);
        }
        return RecordsFile;
    }

    /**
     * @return ClaimIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimIDsOfNewSHBEClaimsFile() {
        if (ClaimIDsOfNewSHBEClaimsFile == null) {
            ClaimIDsOfNewSHBEClaimsFile = getFile(
                    "ClaimIDsOfNewSHBEClaims"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNewSHBEClaimsFile;
    }

    /**
     * @return ClaimIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile == null) {
            ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile = getFile(
                    "ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile;
    }

    /**
     * @return ClaimIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile == null) {
            ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile = getFile(
                    "ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile;
    }

    /**
     * @return ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile
     * initialising if it is not already initialised.
     */
    protected final File getClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile == null) {
            ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile = getFile(
                    "ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile;
    }

    /**
     * @return ClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile initialising if it
     * is not already initialised.
     */
    protected final File getClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile() {
        if (ClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile == null) {
            ClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile = getFile(
                    "ClaimIDsOfNewSHBEClaimsWhereClaimantIsNew"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNewSHBEClaimsWhereClaimantIsNewFile;
    }

    public final File getClaimantPersonIDsFile() {
        if (ClaimantPersonIDsFile == null) {
            ClaimantPersonIDsFile = getFile(
                    "Claimant"
                    + Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimantPersonIDsFile;
    }

    public final File getPartnerPersonIDsFile() {
        if (PartnerPersonIDsFile == null) {
            PartnerPersonIDsFile = getFile(
                    "Partner"
                    + Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return PartnerPersonIDsFile;
    }

    public final File getNonDependentPersonIDsFile() {
        if (NonDependentPersonIDsFile == null) {
            NonDependentPersonIDsFile = getFile(
                    "NonDependent"
                    + Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return NonDependentPersonIDsFile;
    }

    public final HashSet<DW_PersonID> getClaimantPersonIDs(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPersonIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getClaimantPersonIDs(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
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
    public final HashSet<DW_PersonID> getClaimantPersonIDs(
            File f) {
        if (ClaimantPersonIDs == null) {
            ClaimantPersonIDs = DW_Collections.getHashSet_DW_PersonID(f);
        }
        return ClaimantPersonIDs;
    }

    public final HashSet<DW_PersonID> getPartnerPersonIDs(boolean handleOutOfMemoryError) {
        try {
            return getPartnerPersonIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getPartnerPersonIDs(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
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
    public final HashSet<DW_PersonID> getPartnerPersonIDs(
            File f) {
        if (PartnerPersonIDs == null) {
            PartnerPersonIDs = DW_Collections.getHashSet_DW_PersonID(f);
        }
        return PartnerPersonIDs;
    }

    public final HashSet<DW_PersonID> getNonDependentPersonIDs(boolean handleOutOfMemoryError) {
        try {
            return getNonDependentPersonIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                Env.clearMemoryReserve();
                if (!Env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                Env.initMemoryReserve();
                return getNonDependentPersonIDs(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
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
     * @return CottingleySpringsCaravanParkPairedClaimIDsFile initialising if it
     * is not already initialised.
     */
    protected final File getCottingleySpringsCaravanParkPairedClaimIDsFile() {
        if (CottingleySpringsCaravanParkPairedClaimIDsFile == null) {
            CottingleySpringsCaravanParkPairedClaimIDsFile = getFile(
                    Strings.sCottingleySpringsCaravanPark + "PairedClaimIDs"
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return CottingleySpringsCaravanParkPairedClaimIDsFile;
    }

    /**
     * @return ClaimIDsWithStatusOfHBAtExtractDateInPaymentFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfHBAtExtractDateInPaymentFile() {
        if (ClaimIDsWithStatusOfHBAtExtractDateInPaymentFile == null) {
            ClaimIDsWithStatusOfHBAtExtractDateInPaymentFile = getFile(
                    Strings.sHB + Strings.sPaymentTypeIn
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfHBAtExtractDateInPaymentFile;
    }

    /**
     * @return ClaimIDsWithStatusOfHBAtExtractDateSuspendedFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfHBAtExtractDateSuspendedFile() {
        if (ClaimIDsWithStatusOfHBAtExtractDateSuspendedFile == null) {
            ClaimIDsWithStatusOfHBAtExtractDateSuspendedFile = getFile(
                    Strings.sHB + Strings.sPaymentTypeSuspended
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfHBAtExtractDateSuspendedFile;
    }

    /**
     * @return ClaimIDsWithStatusOfHBAtExtractDateOtherFile initialising if it
     * is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfHBAtExtractDateOtherFile() {
        if (ClaimIDsWithStatusOfHBAtExtractDateOtherFile == null) {
            ClaimIDsWithStatusOfHBAtExtractDateOtherFile = getFile(
                    Strings.sHB + Strings.sPaymentTypeOther
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfHBAtExtractDateOtherFile;
    }

    /**
     * @return ClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile == null) {
            ClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile = getFile(
                    Strings.sCTB + Strings.sPaymentTypeIn
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateInPaymentFile;
    }

    /**
     * @return ClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile == null) {
            ClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile = getFile(
                    Strings.sCTB + Strings.sPaymentTypeSuspended
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateSuspendedFile;
    }

    /**
     * @return ClaimIDsWithStatusOfCTBAtExtractDateOtherFile initialising if it
     * is not already initialised.
     */
    protected final File getClaimIDsWithStatusOfCTBAtExtractDateOtherFile() {
        if (ClaimIDsWithStatusOfCTBAtExtractDateOtherFile == null) {
            ClaimIDsWithStatusOfCTBAtExtractDateOtherFile = getFile(
                    Strings.sCTB + Strings.sPaymentTypeOther
                    + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsWithStatusOfCTBAtExtractDateOtherFile;
    }

    /**
     * @return SRecordsWithoutDRecordsFile initialising if it is not already
     * initialised.
     */
    protected final File getSRecordsWithoutDRecordsFile() {
        if (SRecordsWithoutDRecordsFile == null) {
            SRecordsWithoutDRecordsFile = getFile(
                    "SRecordsWithoutDRecordsFile" + Strings.sUnderscore
                    + "HashMap_DW_ID__ArrayList_DW_SHBE_S_Record"
                    + Strings.sBinaryFileExtension);
        }
        return SRecordsWithoutDRecordsFile;
    }

    /**
     * @return ClaimIDAndCountOfRecordsWithSRecordsFile initialising if it is
     * not already initialised.
     */
    protected final File getClaimIDAndCountOfRecordsWithSRecordsFile() {
        if (ClaimIDAndCountOfRecordsWithSRecordsFile == null) {
            ClaimIDAndCountOfRecordsWithSRecordsFile = getFile(
                    "ClaimIDAndCountOfRecordsWithSRecordsFile" + Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDAndCountOfRecordsWithSRecordsFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile initialising
     * if it is not already initialised.
     */
    protected final File getClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile() {
        if (ClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile == null) {
            ClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile = getFile(
                    "ClaimIDsOfClaimsWithoutAMappableClaimantPostcode" + Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithoutAMappableClaimantPostcodeFile;
    }

    /**
     * @return ClaimIDToClaimantPersonIDLookupFile initialising if it is not
     * already initialised.
     */
    public final File getClaimIDToClaimantPersonIDLookupFile() {
        if (ClaimIDToClaimantPersonIDLookupFile == null) {
            ClaimIDToClaimantPersonIDLookupFile = getFile(
                    "ClaimIDToClaimantPersonIDLookup" + Strings.sUnderscore
                    + "HashMap_DW_ID_DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToClaimantPersonIDLookupFile;
    }

    /**
     * @return ClaimIDToPartnerPersonIDLookupFile initialising if it is not
     * already initialised.
     */
    public final File getClaimIDToPartnerPersonIDLookupFile() {
        if (ClaimIDToPartnerPersonIDLookupFile == null) {
            ClaimIDToPartnerPersonIDLookupFile = getFile(
                    "ClaimIDToPartnerPersonIDLookup" + Strings.sUnderscore
                    + "HashMap_DW_ID__DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToPartnerPersonIDLookupFile;
    }

    /**
     * @return ClaimIDToDependentPersonIDsLookupFile initialising if it is not
     * already initialised.
     */
    public final File getClaimIDToDependentPersonIDsLookupFile() {
        if (ClaimIDToDependentPersonIDsLookupFile == null) {
            ClaimIDToDependentPersonIDsLookupFile = getFile(
                    "ClaimIDToDependentPersonIDsLookupFile" + Strings.sUnderscore
                    + "HashMap_DW_ID__HashSet<DW_PersonID>"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToDependentPersonIDsLookupFile;
    }

    /**
     * @return ClaimIDToNonDependentPersonIDsLookupFile initialising if it is
     * not already initialised.
     */
    public final File getClaimIDToNonDependentPersonIDsLookupFile() {
        if (ClaimIDToNonDependentPersonIDsLookupFile == null) {
            ClaimIDToNonDependentPersonIDsLookupFile = getFile(
                    "ClaimIDToNonDependentPersonIDsLookupFile" + Strings.sUnderscore
                    + "HashMap_DW_ID__HashSet_DW_PersonID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToNonDependentPersonIDsLookupFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile() {
        if (ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile == null) {
            ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile = getFile(
                    "ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile() {
        if (ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile == null) {
            ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile = getFile(
                    "ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile() {
        if (ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile == null) {
            ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile = getFile(
                    "ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile() {
        if (ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile == null) {
            ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile = getFile(
                    "ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile;
    }

    /**
     * @return
     * ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile() {
        if (ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile == null) {
            ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile = getFile(
                    "ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile;
    }

    /**
     * @return ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile
     * initialising if it is not already initialised.
     */
    public final File getClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile() {
        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile == null) {
            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile = getFile(
                    "ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookup" + Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimantsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;
    }

    /**
     * @return PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile
     * initialising if it is not already initialised.
     */
    public final File getPartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile() {
        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile == null) {
            PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile = getFile(
                    "PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile" + Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return PartnersInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;
    }

    /**
     * @return NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile
     * initialising if it is not already initialised.
     */
    public final File getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile() {
        if (NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile == null) {
            NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile = getFile(
                    "NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile" + Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return NonDependentsInMultipleClaimsInAMonthPersonIDToClaimIDsLookupFile;
    }

    /**
     * @return ClaimIDToPostcodeIDLookupFile initialising if it is not already
     * initialised.
     */
    public final File getClaimIDToPostcodeIDLookupFile() {
        if (ClaimIDToPostcodeIDLookupFile == null) {
            ClaimIDToPostcodeIDLookupFile = getFile(
                    "ClaimIDToPostcodeIDLookup" + Strings.sUnderscore
                    + "HashMap_DW_ID__DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToPostcodeIDLookupFile;
    }

    /**
     * @return ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile
     * initialising if it is not already initialised.
     */
    public final File getClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile() {
        if (ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile == null) {
            ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile = getFile(
                    "ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile;
    }

    /**
     * @return ClaimIDToTenancyTypeLookupFile initialising if it is not already
     * initialised.
     */
    public final File getClaimIDToTenancyTypeLookupFile() {
        if (ClaimIDToTenancyTypeLookupFile == null) {
            ClaimIDToTenancyTypeLookupFile = getFile(
                    "ClaimIDToTenancyTypeLookup" + Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDToTenancyTypeLookupFile;
    }

    /**
     * @return LoadSummaryFile initialising if it is not already initialised.
     */
    public final File getLoadSummaryFile() {
        if (LoadSummaryFile == null) {
            LoadSummaryFile = getFile(
                    "LoadSummary" + Strings.sUnderscore
                    + "HashMap_String__Integer"
                    + Strings.sBinaryFileExtension);
        }
        return LoadSummaryFile;
    }

    /**
     * @return RecordIDsNotLoadedFile initialising if it is not already
     * initialised.
     */
    public final File getRecordIDsNotLoadedFile() {
        if (RecordIDsNotLoadedFile == null) {
            RecordIDsNotLoadedFile = getFile(
                    "RecordIDsNotLoaded" + Strings.sUnderscore
                    + "ArrayList_Long"
                    + Strings.sBinaryFileExtension);
        }
        return RecordIDsNotLoadedFile;
    }

    /**
     * @return ClaimIDsOfInvalidClaimantNINOClaimsFile initialising if it is not
     * already initialised.
     */
    public final File getClaimIDsOfInvalidClaimantNINOClaimsFile() {
        if (ClaimIDsOfInvalidClaimantNINOClaimsFile == null) {
            ClaimIDsOfInvalidClaimantNINOClaimsFile = getFile(
                    "ClaimIDsOfInvalidClaimantNINOClaimsFile" + Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimIDsOfInvalidClaimantNINOClaimsFile;
    }

    /**
     * @return ClaimantPostcodesUnmappableFile initialising if it is not already
     * initialised.
     */
    public final File getClaimantPostcodesUnmappableFile() {
        if (ClaimantPostcodesUnmappableFile == null) {
            ClaimantPostcodesUnmappableFile = getFile(
                    "ClaimantPostcodesUnmappable" + Strings.sUnderscore
                    + "HashMap_DW_ID__String"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimantPostcodesUnmappableFile;
    }

    /**
     * @return ClaimantPostcodesModifiedFile initialising if it is not already
     * initialised.
     */
    public final File getClaimantPostcodesModifiedFile() {
        if (ClaimantPostcodesModifiedFile == null) {
            ClaimantPostcodesModifiedFile = getFile(
                    "ClaimantPostcodesModified" + Strings.sUnderscore
                    + "HashMap_DW_ID__String[]"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimantPostcodesModifiedFile;
    }

    /**
     * @return ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile
     * initialising if it is not already initialised.
     */
    public final File getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile() {
        if (ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile == null) {
            ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile = getFile(
                    "ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes" + Strings.sUnderscore
                    + "HashMap_DW_ID__String"
                    + Strings.sBinaryFileExtension);
        }
        return ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile;
    }

    /**
     * Clears the main Data. This is for memory handling reasons.
     */
    public void clearData() {
        this.Records = null;
        this.RecordIDsNotLoaded = null;
        this.SRecordsWithoutDRecords = null;
    }
}
