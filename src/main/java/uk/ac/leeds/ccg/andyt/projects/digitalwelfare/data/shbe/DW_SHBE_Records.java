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
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_Collections;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.DW_CorrectedPostcodes;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.postcode.DW_Postcode_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.util.DW_Collections;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Records extends DW_Object implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * For convenience.
     */
    private transient DW_Strings DW_Strings;

    /**
     * Call this method to initialise fields declared transient after having
     * read this back as a Serialized Object.
     *
     * @param env
     */
    public void init(DW_Environment env) {
        this.env = env;
        this.DW_Strings = env.getDW_Strings();
    }

    /**
     * Keys are ClaimRefIDs, values are DW_SHBE_Record.
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
     * A store for ClaimRefIDs for Cottingley Springs Caravan Park where there
     * are two claims for a claimant, one for a pitch and the other for the rent
     * of a caravan.
     */
    private HashSet<DW_ID> CottingleySpringsCaravanParkPairedClaimRefIDs;

    /**
     * A store for ClaimRefIDs where: StatusOfHBClaimAtExtractDate = 1 (In
     * Payment).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfHBAtExtractDateInPayment;

    /**
     * A store for ClaimRefIDs where: StatusOfHBClaimAtExtractDate = 2
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfHBAtExtractDateSuspended;

    /**
     * A store for ClaimRefIDs where: StatusOfHBClaimAtExtractDate = 0
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfHBAtExtractDateOther;

    /**
     * A store for ClaimRefIDs where: StatusOfCTBClaimAtExtractDate = 1 (In
     * Payment).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment;

    /**
     * A store for ClaimRefIDs where: StatusOfCTBClaimAtExtractDate = 2
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended;

    /**
     * A store for ClaimRefIDs where: StatusOfCTBClaimAtExtractDate = 0
     * (Suspended).
     */
    private HashSet<DW_ID> ClaimRefIDsWithStatusOfCTBAtExtractDateOther;

    /**
     * SRecordsWithoutDRecords indexed by ClaimRef DW_ID. Once the SHBE data is
     * loaded from source, this only contains those SRecordsWithoutDRecords that
     * are not linked to a DRecord.
     */
    private HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>> SRecordsWithoutDRecords;

    /**
     * For storing the ClaimRef DW_ID of Records that have SRecords along with
     * the count of those SRecordsWithoutDRecords.
     */
    private HashMap<DW_ID, Integer> ClaimRefIDAndCountOfRecordsWithSRecords;

    /**
     * For storing the Year_Month of this. This is an identifier for these data.
     */
    private String YM3;

    /**
     * For storing the NearestYM3ForONSPDLookup of this. This is derived from
     * YM3.
     */
    private String NearestYM3ForONSPDLookup;

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
     * File for storing ClaimRefIDsOfNewSHBEClaims.
     */
    private File ClaimRefIDsOfNewSHBEClaimsFile;

    /**
     * File for storing
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore.
     */
    private File ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile;

    /**
     * File for storing ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.
     */
    private File ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile;

    /**
     * File for storing
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore.
     */
    private File ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile;

    /**
     * File for storing ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew.
     */
    private File ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile;

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
     * File for storing CottingleySpringsCaravanParkPairedClaimRefIDs.
     */
    private File CottingleySpringsCaravanParkPairedClaimRefIDsFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfHBAtExtractDateInPayment.
     */
    private File ClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfHBAtExtractDateSuspended.
     */
    private File ClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfHBAtExtractDateOther.
     */
    private File ClaimRefIDsWithStatusOfHBAtExtractDateOtherFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment.
     */
    private File ClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended.
     */
    private File ClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile;

    /**
     * File for storing ClaimRefIDsWithStatusOfCTBAtExtractDateOther.
     */
    private File ClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile;

    /**
     * File for storing SRecordsWithoutDRecords.
     */
    private File SRecordsWithoutDRecordsFile;

    /**
     * File for storing ClaimRefIDAndCountOfRecordsWithSRecordsFile.
     */
    private File ClaimRefIDAndCountOfRecordsWithSRecordsFile;

    /**
     * For storing the ClaimRefID of Records without a mappable Claimant
     * Postcode.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode;

    /**
     * File for storing ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode.
     */
    private File ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile;

    /**
     * ClaimRef DW_IDs mapped to DW_PersonID of Claimants.
     */
    private HashMap<DW_ID, DW_PersonID> ClaimRefIDToClaimantPersonIDLookup;

    /**
     * ClaimRef DW_IDs mapped to DW_PersonID of Partners. If there is no main
     * Partner for the claim then there is no mapping.
     */
    private HashMap<DW_ID, DW_PersonID> ClaimRefIDToPartnerPersonIDLookup;

    /**
     * ClaimRef DW_IDs mapped to {@code HashSet<DW_PersonID>} of Dependents. If
     * there are no main Dependents for the claim then there is no mapping.
     */
    private HashMap<DW_ID, HashSet<DW_PersonID>> ClaimRefIDToDependentPersonIDsLookup;

    /**
     * ClaimRef DW_IDs mapped to {@code HashSet<DW_PersonID>} of Dependents. If
     * there are no main Dependents for the claim then there is no mapping.
     */
    private HashMap<DW_ID, HashSet<DW_PersonID>> ClaimRefIDToNonDependentPersonIDsLookup;

    /**
     * Claim DW_IDs of Claims with Claimants that are Claimants in another
     * claim.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim;

    /**
     * Claim DW_IDs of Claims with Claimants that are Partners in another claim.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim;

    /**
     * Claim DW_IDs of Claims with Partners that are Claimants in another claim.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim;

    /**
     * Claim DW_IDs of Claims with Partners that are Partners in multiple
     * claims.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim;

    /**
     * ClaimRef DW_IDs of Claims with NonDependents that are Claimants or
     * Partners in another claim.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim;

    /**
     * DW_PersonIDs of Claimants that are in multiple claims in a month mapped
     * to a set of ClaimRef DW_IDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;

    /**
     * DW_PersonIDs of Partners that are in multiple claims in a month mapped to
     * a set of ClaimRef DW_IDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;

    /**
     * DW_PersonIDs of NonDependents that are in multiple claims in a month
     * mapped to a set of ClaimRef DW_IDs of those claims.
     */
    private HashMap<DW_PersonID, HashSet<DW_ID>> NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;

    /**
     * ClaimRef DW_IDs mapped to Postcode DW_IDs.
     */
    private HashMap<DW_ID, DW_ID> ClaimRefIDToPostcodeIDLookup;

    /**
     * ClaimRef DW_IDs of the claims that have had PostcodeF updated from the
     * future. This is only done if the postcode was previously of an invalid
     * format.
     */
    private HashSet<DW_ID> ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture;

    /**
     * ClaimRef DW_IDs. This is only used when reading the data to check that
     * ClaimRefIDs are unique.
     */
    private HashSet<DW_ID> ClaimRefIDs;

    /**
     * ClaimRef DW_IDs Of New SHBE Claims.
     */
    private HashSet<DW_ID> ClaimRefIDsOfNewSHBEClaims;

    /**
     * ClaimRefIDs Of New SHBE Claims Where Claimant Was Claimant Before.
     */
    private HashSet<DW_ID> ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore;

    /**
     * ClaimRefIDs Of New SHBE Claims Where Claimant Was Partner Before
     */
    private HashSet<DW_ID> ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore;

    /**
     * ClaimRefIDs Of New SHBE Claims Where Claimant Was NonDependent Before
     */
    private HashSet<DW_ID> ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore;

    /**
     * ClaimRefIDs Of New SHBE Claims Where Claimant Is New
     */
    private HashSet<DW_ID> ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew;

    /**
     * ClaimRef DW_IDs mapped to TenancyType.
     */
    private HashMap<DW_ID, Integer> ClaimRefIDToTenancyTypeLookup;

    /**
     * LoadSummary
     */
    private HashMap<String, Number> LoadSummary;

    /**
     * The line numbers of records that for some reason could not be loaded.
     */
    private ArrayList<Long> RecordIDsNotLoaded;

    /**
     * Store of ClaimRefIDs of all Claims where Claimant National Insurance
     * Number is invalid.
     */
    private HashSet<DW_ID> ClaimRefIDsOfInvalidClaimantNINOClaims;

    /**
     * Claimant Postcodes that are not (currently) mappable (indexed by
     * ClaimRefID).
     */
    private HashMap<DW_ID, String> ClaimantPostcodesUnmappable;

    /**
     * Claimant Postcodes that have been automatically modified to make them
     * mappable (indexed by ClaimRefID).
     */
    private HashMap<DW_ID, String[]> ClaimantPostcodesModified;

    /**
     * Claimant Postcodes Checked by local authority to be mappable, but not
     * found in the subsequent ONSPD or the latest (indexed by ClaimRefID).
     */
    private HashMap<DW_ID, String> ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes;

    /**
     * PartnerDW_IDs File.
     */
    private File ClaimRefIDToClaimantPersonIDLookupFile;

    /**
     * ClaimRefIDToPartnerPersonIDLookup File.
     */
    private File ClaimRefIDToPartnerPersonIDLookupFile;

    /**
     * DependentClaimRefIDs File.
     */
    private File ClaimRefIDToDependentPersonIDsLookupFile;

    /**
     * NonDependentClaimRefIDs File.
     */
    private File ClaimRefIDToNonDependentPersonIDsLookupFile;

    /**
     * ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim File.
     */
    private File ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile;

    /**
     * ClaimRefIDOfClaimsWithClaimantsThatArePartnersInAnotherClaim File.
     */
    private File ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile;

    /**
     * ClaimRefIDOfClaimsWithPartnersThatAreClaimantsInAnotherClaim File.
     */
    private File ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile;

    /**
     * ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim File.
     */
    private File ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile;

    /**
     * ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * File.
     */
    private File ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile;

    /**
     * ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup File.
     */
    private File ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;

    /**
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup File.
     */
    private File PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;

    /**
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup File.
     */
    private File NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;

    /**
     * ClaimRefIDToPostcodeLookup File.
     */
    private File ClaimRefIDToPostcodeIDLookupFile;

    /**
     * ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture File.
     */
    private File ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile;

    /**
     * ClaimRefIDToTenancyTypeLookup File.
     */
    private File ClaimRefIDToTenancyTypeLookupFile;

    /**
     * LoadSummary File.
     */
    private File LoadSummaryFile;

    /**
     * RecordIDsNotLoaded File.
     */
    private File RecordIDsNotLoadedFile;

    /**
     * ClaimRefsOfInvalidClaimantNINOClaims File.
     */
    private File ClaimRefIDsOfInvalidClaimantNINOClaimsFile;

    /**
     * UnmappableClaimantPostcodes File.
     */
    private File ClaimantPostcodesUnmappableFile;

    /**
     * ModifiedPostcodes File
     */
    private File ClaimantPostcodesModifiedFile;

    /**
     * ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes File
     */
    private File ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile;

    /**
     * If not initialised, initialises Records then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_SHBE_Record> getRecords(boolean handleOutOfMemoryError) {
        try {
            return getRecords();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getRecords(handleOutOfMemoryError);
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
     * If not initialised, initialises ClaimRefIDsOfNewSHBEClaims then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaims(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfNewSHBEClaims();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNewSHBEClaims(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimRefIDsOfNewSHBEClaims then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaims() {
        if (ClaimRefIDsOfNewSHBEClaims == null) {
            File f;
            f = getClaimRefIDsOfNewSHBEClaimsFile();
            if (f.exists()) {
                ClaimRefIDsOfNewSHBEClaims = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfNewSHBEClaims = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfNewSHBEClaims;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore == null) {
            File f;
            f = getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile();
            if (f.exists()) {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore == null) {
            File f;
            f = getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile();
            if (f.exists()) {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore == null) {
            File f;
            f = getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile();
            if (f.exists()) {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew == null) {
            File f;
            f = getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile();
            if (f.exists()) {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew;
    }

    /**
     * If not initialised, initialises
     * CottingleySpringsCaravanParkPairedClaimRefIDs then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getCottingleySpringsCaravanParkPairedClaimRefIDs(boolean handleOutOfMemoryError) {
        try {
            return getCottingleySpringsCaravanParkPairedClaimRefIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getCottingleySpringsCaravanParkPairedClaimRefIDs(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * CottingleySpringsCaravanParkPairedClaimRefIDs then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getCottingleySpringsCaravanParkPairedClaimRefIDs() {
        if (CottingleySpringsCaravanParkPairedClaimRefIDs == null) {
            File f;
            f = getCottingleySpringsCaravanParkPairedClaimRefIDsFile();
            if (f.exists()) {
                CottingleySpringsCaravanParkPairedClaimRefIDs = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                CottingleySpringsCaravanParkPairedClaimRefIDs = new HashSet<DW_ID>();
            }
        }
        return CottingleySpringsCaravanParkPairedClaimRefIDs;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateInPayment then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateInPayment(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfHBAtExtractDateInPayment();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfHBAtExtractDateInPayment(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateInPayment then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfHBAtExtractDateInPayment() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateInPayment == null) {
            File f;
            f = getClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfHBAtExtractDateInPayment = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfHBAtExtractDateInPayment = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateInPayment;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateSuspended then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateSuspended(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfHBAtExtractDateSuspended();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfHBAtExtractDateSuspended(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateSuspended then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfHBAtExtractDateSuspended() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateSuspended == null) {
            File f;
            f = getClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfHBAtExtractDateSuspended = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfHBAtExtractDateSuspended = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateSuspended;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateOther then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfHBAtExtractDateOther(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfHBAtExtractDateOther();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfHBAtExtractDateOther(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfHBAtExtractDateOther then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfHBAtExtractDateOther() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateOther == null) {
            File f;
            f = getClaimRefIDsWithStatusOfHBAtExtractDateOtherFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfHBAtExtractDateOther = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfHBAtExtractDateOther = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateOther;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateInPayment(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfCTBAtExtractDateInPayment();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfCTBAtExtractDateInPayment(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfCTBAtExtractDateInPayment() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment == null) {
            File f;
            f = getClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateSuspended(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfCTBAtExtractDateSuspended();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfCTBAtExtractDateSuspended(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfCTBAtExtractDateSuspended() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended == null) {
            File f;
            f = getClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateOther then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimIDsWithStatusOfCTBAtExtractDateOther(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsWithStatusOfCTBAtExtractDateOther();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimIDsWithStatusOfCTBAtExtractDateOther(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsWithStatusOfCTBAtExtractDateOther then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsWithStatusOfCTBAtExtractDateOther() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateOther == null) {
            File f;
            f = getClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile();
            if (f.exists()) {
                ClaimRefIDsWithStatusOfCTBAtExtractDateOther = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsWithStatusOfCTBAtExtractDateOther = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateOther;
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
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
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
     * If not initialised, initialises ClaimRefIDAndCountOfRecordsWithSRecords
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, Integer> getClaimRefIDAndCountOfRecordsWithSRecords(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDAndCountOfRecordsWithSRecords();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDAndCountOfRecordsWithSRecords(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithoutAValidClaimantPostcode(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithoutAValidClaimantPostcode(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimRefIDAndCountOfRecordsWithSRecords
     */
    protected HashMap<DW_ID, Integer> getClaimRefIDAndCountOfRecordsWithSRecords() {
        if (ClaimRefIDAndCountOfRecordsWithSRecords == null) {
            File f;
            f = getClaimRefIDAndCountOfRecordsWithSRecordsFile();
            if (f.exists()) {
                ClaimRefIDAndCountOfRecordsWithSRecords = (HashMap<DW_ID, Integer>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDAndCountOfRecordsWithSRecords = new HashMap<DW_ID, Integer>();
            }
        }
        return ClaimRefIDAndCountOfRecordsWithSRecords;
    }

    /**
     * @return the ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode() {
        if (ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode;
    }

    /**
     * @return YM3
     */
    public String getYM3() {
        return YM3;
    }

    /**
     * @return NearestYM3ForONSPDLookup
     */
    public String getNearestYM3ForONSPDLookup() {
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
                    env.getDW_Files().getGeneratedSHBEDir(), getYM3());
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
            String YM3
    ) {
        super(env);
        this.YM3 = YM3;
        NearestYM3ForONSPDLookup = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        env.logO("YM3 " + YM3, true);
        env.logO("NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup, true);
        DW_Strings = env.getDW_Strings();
        Records = getRecords(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfNewSHBEClaims = getClaimRefIDsOfNewSHBEClaims(env._HandleOutOfMemoryError_boolean);
        ClaimantPersonIDs = getClaimantPersonIDs(env._HandleOutOfMemoryError_boolean);
        PartnerPersonIDs = getPartnerPersonIDs(env._HandleOutOfMemoryError_boolean);
        NonDependentPersonIDs = getNonDependentPersonIDs(env._HandleOutOfMemoryError_boolean);
        CottingleySpringsCaravanParkPairedClaimRefIDs = getCottingleySpringsCaravanParkPairedClaimRefIDs(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfHBAtExtractDateInPayment = getClaimIDsWithStatusOfHBAtExtractDateInPayment(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfHBAtExtractDateSuspended = getClaimIDsWithStatusOfHBAtExtractDateSuspended(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfHBAtExtractDateOther = getClaimIDsWithStatusOfHBAtExtractDateOther(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment = getClaimIDsWithStatusOfCTBAtExtractDateInPayment(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended = getClaimIDsWithStatusOfCTBAtExtractDateSuspended(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsWithStatusOfCTBAtExtractDateOther = getClaimIDsWithStatusOfCTBAtExtractDateOther(env._HandleOutOfMemoryError_boolean);
        SRecordsWithoutDRecords = getSRecordsWithoutDRecords(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDAndCountOfRecordsWithSRecords = getClaimRefIDAndCountOfRecordsWithSRecords(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode = getClaimRefIDsOfClaimsWithoutAValidClaimantPostcode(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToClaimantPersonIDLookup = getClaimRefIDToClaimantPersonIDLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToPartnerPersonIDLookup = getClaimRefIDToPartnerPersonIDLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToDependentPersonIDsLookup = getClaimRefIDToDependentPersonIDsLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToNonDependentPersonIDsLookup = getClaimRefIDToNonDependentPersonIDsLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = getClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(env._HandleOutOfMemoryError_boolean);
        ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(env._HandleOutOfMemoryError_boolean);
        PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(env._HandleOutOfMemoryError_boolean);
        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToPostcodeIDLookup = getClaimRefIDToPostcodeIDLookup(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDToTenancyTypeLookup = getClaimRefIDToTenancyTypeLookup(env._HandleOutOfMemoryError_boolean);
        LoadSummary = getLoadSummary(env._HandleOutOfMemoryError_boolean);
        RecordIDsNotLoaded = getRecordIDsNotLoaded(env._HandleOutOfMemoryError_boolean);
        ClaimRefIDsOfInvalidClaimantNINOClaims = getClaimRefIDsOfInvalidClaimantNINOClaims(env._HandleOutOfMemoryError_boolean);
        ClaimantPostcodesUnmappable = getClaimantPostcodesUnmappable(env._HandleOutOfMemoryError_boolean);
        ClaimantPostcodesModified = getClaimantPostcodesModified(env._HandleOutOfMemoryError_boolean);
        ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes(env._HandleOutOfMemoryError_boolean);
    ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(env._HandleOutOfMemoryError_boolean);
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
            String LatestYM3ForONSPDFormat,
           File logDir
    ) {
        super(env);
        DW_SHBE_Handler DW_SHBE_Handler;
        DW_SHBE_Handler = env.getDW_SHBE_Handler();
        DW_Postcode_Handler DW_Postcode_Handler;
        DW_Postcode_Handler = env.getDW_Postcode_Handler();
        DW_SHBE_Data DW_SHBE_Data;
        DW_SHBE_Data = env.getDW_SHBE_Data();
        InputFile = new File(inputDirectory, inputFilename);
        YM3 = DW_SHBE_Handler.getYM3(inputFilename);
        NearestYM3ForONSPDLookup = DW_Postcode_Handler.getNearestYM3ForONSPDLookup(YM3);
        DW_Strings = env.getDW_Strings();
        Records = new HashMap<DW_ID, DW_SHBE_Record>();
        ClaimRefIDs = new HashSet<DW_ID>();
        ClaimRefIDsOfNewSHBEClaims = new HashSet<DW_ID>();
        ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore = new HashSet<DW_ID>();
        ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore = new HashSet<DW_ID>();
        ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore = new HashSet<DW_ID>();
        ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew = new HashSet<DW_ID>();
        ClaimantPersonIDs = new HashSet<DW_PersonID>();
        PartnerPersonIDs = new HashSet<DW_PersonID>();
        NonDependentPersonIDs = new HashSet<DW_PersonID>();
        CottingleySpringsCaravanParkPairedClaimRefIDs = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfHBAtExtractDateInPayment = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfHBAtExtractDateSuspended = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfHBAtExtractDateOther = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended = new HashSet<DW_ID>();
        ClaimRefIDsWithStatusOfCTBAtExtractDateOther = new HashSet<DW_ID>();
        SRecordsWithoutDRecords = new HashMap<DW_ID, ArrayList<DW_SHBE_S_Record>>();
        ClaimRefIDAndCountOfRecordsWithSRecords = new HashMap<DW_ID, Integer>();
        ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode = new HashSet<DW_ID>();
        ClaimRefIDToClaimantPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
        ClaimRefIDToPartnerPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
        ClaimRefIDToDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
        ClaimRefIDToNonDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
        ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
        ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
        ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = new HashSet<DW_ID>();
        ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
        ClaimRefIDToPostcodeIDLookup = new HashMap<DW_ID, DW_ID>();
        ClaimRefIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
        LoadSummary = new HashMap<String, Number>();
        RecordIDsNotLoaded = new ArrayList<Long>();
        ClaimRefIDsOfInvalidClaimantNINOClaims = new HashSet<DW_ID>();
        ClaimantPostcodesUnmappable = new HashMap<DW_ID, String>();
        ClaimantPostcodesModified = new HashMap<DW_ID, String[]>();
        ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes = new HashMap<DW_ID, String>();
        ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = new HashSet<DW_ID>();
        env.log("----------------------");
        env.log("Load " + YM3);
        env.log("----------------------");
        env.log("NearestYM3ForONSPDLookup " + NearestYM3ForONSPDLookup);
        env.log("LatestYM3ForONSPDLookup " + LatestYM3ForONSPDFormat);
        if (!LatestYM3ForONSPDFormat.equalsIgnoreCase(NearestYM3ForONSPDLookup)) {
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
        HashMap<DW_ID, AGDT_Point> PostcodeIDToPointLookup;
        /**
         * Mapping of ClaimRef String to ClaimRef DW_ID.
         */
        HashMap<String, DW_ID> ClaimRefToClaimRefIDLookup;
        /**
         * ClaimRef DW_ID mapped to ClaimRef String.
         */
        HashMap<DW_ID, String> ClaimRefIDToClaimRefLookup;

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
         * All DW_PersonID to ClaimRefIDs Lookup
         */
        HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimRefIDsLookup;

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
        PersonIDToClaimRefIDsLookup = DW_SHBE_Data.getPersonIDToClaimRefIDsLookup();
        PostcodeToPostcodeIDLookup = DW_SHBE_Data.getPostcodeToPostcodeIDLookup();
        PostcodeIDToPostcodeLookup = DW_SHBE_Data.getPostcodeIDToPostcodeLookup();
        PostcodeIDToPointLookup = DW_SHBE_Data.getPostcodeIDToPointLookup(YM3);
        ClaimRefToClaimRefIDLookup = DW_SHBE_Data.getClaimRefToClaimRefIDLookup();
        ClaimRefIDToClaimRefLookup = DW_SHBE_Data.getClaimRefIDToClaimRefLookup();
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
            DW_ID ClaimRefID;
            DW_SHBE_Record record;
            int StatusOfHBClaimAtExtractDate;
            int StatusOfCTBClaimAtExtractDate;
            String Postcode;
            String ClaimantNINO;
            String ClaimantDOB;
            DW_PersonID ClaimantPersonID;
            boolean addToNew;
            Object key;
            DW_ID otherClaimRefID = null;
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
                                    ClaimRefID = DW_SHBE_Handler.getIDAddIfNeeded(
                                            ClaimRef,
                                            ClaimRefToClaimRefIDLookup,
                                            ClaimRefIDToClaimRefLookup);
                                    ArrayList<DW_SHBE_S_Record> recs;
                                    recs = SRecordsWithoutDRecords.get(ClaimRefID);
                                    if (recs == null) {
                                        recs = new ArrayList<DW_SHBE_S_Record>();
                                        SRecordsWithoutDRecords.put(ClaimRefID, recs);
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
                                        ClaimRefID = DW_SHBE_Handler.getIDAddIfNeeded(
                                                ClaimRef,
                                                ClaimRefToClaimRefIDLookup,
                                                ClaimRefIDToClaimRefLookup,
                                                ClaimRefIDs,
                                                ClaimRefIDsOfNewSHBEClaims);
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
                                        record = Records.get(ClaimRefID);
                                        if (record == null) {
                                            record = new DW_SHBE_Record(
                                                    env, YM3, ClaimRefID, DRecord);
                                            Records.put(ClaimRefID, record);
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
                                                ClaimRefIDsWithStatusOfHBAtExtractDateOther.add(ClaimRefID);
                                                break;
                                            }
                                            case 1: {
                                                ClaimRefIDsWithStatusOfHBAtExtractDateInPayment.add(ClaimRefID);
                                                break;
                                            }
                                            case 2: {
                                                ClaimRefIDsWithStatusOfHBAtExtractDateSuspended.add(ClaimRefID);
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
                                                ClaimRefIDsWithStatusOfCTBAtExtractDateOther.add(ClaimRefID);
                                                break;
                                            }
                                            case 1: {
                                                ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment.add(ClaimRefID);
                                                break;
                                            }
                                            case 2: {
                                                ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended.add(ClaimRefID);
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
                                                        ClaimantPostcodesModified.put(ClaimRefID, p);
                                                    }
                                                }
                                            }
                                            record.ClaimPostcodeFValidPostcodeFormat = DW_Postcode_Handler.isValidPostcodeForm(record.ClaimPostcodeF);
                                            if (PostcodeToPostcodeIDLookup.containsKey(record.ClaimPostcodeF)) {
                                                CountOfMappableClaimantPostcodes++;
                                                record.ClaimPostcodeFMappable = true;
                                                record.PostcodeID = PostcodeToPostcodeIDLookup.get(record.ClaimPostcodeF);
                                                // Add the point to the lookup
                                                AGDT_Point AGDT_Point;
                                                AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                            NearestYM3ForONSPDLookup,
                                                            DW_Strings.sUnit,
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
                                                AGDT_Point AGDT_Point;
                                                if (isMappablePostcodeLastestYM3) {
                                                    AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                            LatestYM3ForONSPDFormat,
                                                            DW_Strings.sUnit,
                                                            record.ClaimPostcodeF);
                                                } else {
                                                    AGDT_Point = DW_Postcode_Handler.getPointFromPostcodeNew(
                                                            NearestYM3ForONSPDLookup,
                                                            DW_Strings.sUnit,
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
                                                ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode.add(ClaimRefID);
                                                boolean PostcodeCheckedAsMappable = false;
                                                PostcodeCheckedAsMappable = PostcodesCheckedAsMappable.contains(record.ClaimPostcodeF);
                                                if (PostcodeCheckedAsMappable) {
                                                    ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes.put(ClaimRefID, Postcode);
                                                } else {
                                                    // Store unmappable claimant postcode.
                                                    ClaimantPostcodesUnmappable.put(ClaimRefID, Postcode);
                                                }
                                            }
                                            ClaimRefIDToPostcodeIDLookup.put(ClaimRefID, record.PostcodeID);
                                            ClaimRefIDToTenancyTypeLookup.put(ClaimRefID, TenancyType);
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
                                            ClaimRefIDsOfInvalidClaimantNINOClaims.add(ClaimRefID);
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
                                        if (ClaimRefIDsOfNewSHBEClaims.contains(ClaimRefID)) {
                                            addToNew = true;
                                            if (AllClaimantPersonIDs.contains(ClaimantPersonID)) {
                                                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore.add(ClaimRefID);
                                                addToNew = false;
                                            }
                                            if (AllPartnerPersonIDs.contains(ClaimantPersonID)) {
                                                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.add(ClaimRefID);
                                                addToNew = false;
                                            }
                                            if (AllNonDependentIDs.contains(ClaimantPersonID)) {
                                                ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore.add(ClaimRefID);
                                                addToNew = false;
                                            }
                                            if (addToNew) {
                                                ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew.add(ClaimRefID);
                                            }
                                        }
                                        /**
                                         * If ClaimantDW_PersonID is already in
                                         * ClaimRefIDToClaimantPersonIDLookup.
                                         * then ClaimantDW_PersonID has multiple
                                         * claims in a month.
                                         */
                                        if (ClaimRefIDToClaimantPersonIDLookup.containsValue(ClaimantPersonID)) {
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
                                            key = Generic_Collections.getKeyFromValue(
                                                    ClaimRefIDToClaimantPersonIDLookup,
                                                    ClaimantPersonID);
                                            Postcode = DRecord.getClaimantsPostcode();
                                            if (key != null) {
                                                otherClaimRefID = (DW_ID) key;
                                                otherClaimRef = ClaimRefIDToClaimRefLookup.get(otherClaimRefID);
                                                // Treat those paired records for Cottingley Springs Caravan Park differently
                                                if (Postcode.equalsIgnoreCase(DW_Strings.CottingleySpringsCaravanParkPostcode)) {
//                                                    env.logO("Cottingley Springs Caravan Park "
//                                                            + DW_Strings.CottingleySpringsCaravanParkPostcode
//                                                            + " ClaimRef " + ClaimRef + " paired with " + otherClaimRef
//                                                            + " one claim is for the pitch, the other is for rent of "
//                                                            + "a mobile home. ");
                                                    CottingleySpringsCaravanParkPairedClaimRefIDs.add(ClaimRefID);
                                                    CottingleySpringsCaravanParkPairedClaimRefIDs.add(otherClaimRefID);
                                                } else {
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    env.logO(
//                                                            "Claimant with NINO " + ClaimantNINO
//                                                            + " DoB " + ClaimantDOB
//                                                            + " has mulitple claims. "
//                                                            + "The Claimant has had a second claim set up and the "
//                                                            + "previous claim is still on the system for some reason.");
//                                                    env.logO("Current ClaimRef " + ClaimRef);
//                                                    env.logO("Other ClaimRef " + otherClaimRef);
                                                    otherRecord = Records.get(otherClaimRefID);
                                                    if (otherRecord == null) {
                                                        env.logE("Unexpected error xx: This should not happen. "
                                                                + this.getClass().getName()
                                                                + ".DW_SHBE_Records(DW_Environment, File, String)");
                                                    } else {
//                                                        env.logO("This D Record");
//                                                        env.logO(DRecord.toStringBrief());
//                                                        env.logO("Other D Record");
//                                                        env.logO(otherRecord.DRecord.toStringBrief());
                                                        /**
                                                         * Add to
                                                         * ClaimantsWithMultipleClaimsInAMonth.
                                                         */
                                                        ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.add(ClaimRefID);
                                                        ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.add(otherRecord.getClaimRefID());
                                                        HashSet<DW_ID> set;
                                                        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.containsKey(ClaimantPersonID)) {
                                                            set = ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(ClaimantPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(ClaimantPersonID, set);
                                                        }
                                                        set.add(ClaimRefID);
                                                        set.add(otherClaimRefID);
                                                    }
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                            }
                                        }
                                        /**
                                         * If ClaimantPersonID is in
                                         * ClaimRefIDToPartnerPersonIDLookup,
                                         * then claimant is a partner in another
                                         * claim. Add to
                                         * ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim
                                         * and
                                         * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.
                                         */
                                        if (ClaimRefIDToPartnerPersonIDLookup.containsValue(ClaimantPersonID)) {
                                            /**
                                             * Ignore if this is a
                                             * CottingleySpringsCaravanParkPairedClaimRefIDs.
                                             * It may be that there are partners
                                             * shared in these claims, but such
                                             * a thing is ignored for now.
                                             */
                                            if (!CottingleySpringsCaravanParkPairedClaimRefIDs.contains(ClaimRefID)) {
                                                /**
                                                 * If Claimant is a Partner in
                                                 * another claim add to
                                                 * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim
                                                 * and
                                                 * ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.
                                                 */
                                                key = Generic_Collections.getKeyFromValue(
                                                        ClaimRefIDToPartnerPersonIDLookup,
                                                        ClaimantPersonID);
                                                if (key != null) {
                                                    otherClaimRefID = (DW_ID) key;
                                                    HashSet<DW_ID> set;
                                                    ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.add(otherClaimRefID);
                                                }
                                                ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.add(ClaimRefID);
//                                                env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                env.logO("Claimant with NINO " + ClaimantNINO
//                                                        + " DOB " + ClaimantDOB
//                                                        + " in ClaimRef " + ClaimRef
//                                                        + " is a Partner in " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                                env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
                                             * ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim
                                             * and
                                             * PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.
                                             */
                                            if (ClaimRefIDToPartnerPersonIDLookup.containsValue(PartnerPersonID)) {
                                                /*
                                             * Ignore if this is a CottingleySpringsCaravanParkPairedClaimRefIDs.
                                             * It may be that there are partners shared in these claims, but such
                                             * a thing is ignored for now.
                                                 */
                                                if (!CottingleySpringsCaravanParkPairedClaimRefIDs.contains(ClaimRefID)) {
                                                    key = Generic_Collections.getKeyFromValue(
                                                            ClaimRefIDToPartnerPersonIDLookup,
                                                            PartnerPersonID);
                                                    if (key != null) {
                                                        otherClaimRefID = (DW_ID) key;
                                                        HashSet<DW_ID> set;
                                                        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimRefID);
                                                        set.add(otherClaimRefID);
                                                        ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.add(otherClaimRefID);
                                                    }
                                                    ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.add(ClaimRefID);
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    env.logO("Partner with NINO " + NINOIDToNINOLookup.get(PartnerPersonID.getNINO_ID())
//                                                            + " DOB " + DOBIDToDOBLookup.get(PartnerPersonID.getDOB_ID())
//                                                            + " in ClaimRef " + ClaimRef
//                                                            + " is a Partner in " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                            }
                                            /**
                                             * If Partner is a Claimant in
                                             * another claim add to
                                             * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim
                                             * and
                                             * ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.
                                             */
                                            if (ClaimRefIDToClaimantPersonIDLookup.containsValue(PartnerPersonID)) {
                                                /**
                                                 * Ignore if this is a
                                                 * CottingleySpringsCaravanParkPairedClaimRefIDs.
                                                 * It may be that there are
                                                 * partners shared in these
                                                 * claims, but such a thing is
                                                 * ignored for now.
                                                 */
                                                if (!CottingleySpringsCaravanParkPairedClaimRefIDs.contains(ClaimRefID)) {
                                                    key = Generic_Collections.getKeyFromValue(
                                                            ClaimRefIDToClaimantPersonIDLookup,
                                                            PartnerPersonID);
                                                    if (key != null) {
                                                        otherClaimRefID = (DW_ID) key;

                                                        HashSet<DW_ID> set;
                                                        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimRefID);
                                                        set.add(otherClaimRefID);
                                                        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.containsKey(PartnerPersonID)) {
                                                            set = ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(PartnerPersonID);
                                                        } else {
                                                            set = new HashSet<DW_ID>();
                                                            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(PartnerPersonID, set);
                                                        }
                                                        set.add(ClaimRefID);
                                                        set.add(otherClaimRefID);
                                                        ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.add(otherClaimRefID);
                                                    }
                                                    ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.add(ClaimRefID);
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                                    env.logO("Partner with NINO " + NINOIDToNINOLookup.get(PartnerPersonID.getNINO_ID())
//                                                            + " DOB " + DOBIDToDOBLookup.get(PartnerPersonID.getDOB_ID())
//                                                            + " in ClaimRef " + ClaimRef
//                                                            + " is a Claimant in " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                                }
                                                ClaimRefIDToPartnerPersonIDLookup.put(ClaimRefID, PartnerPersonID);
                                            }
                                        }
                                        /**
                                         * Add to
                                         * ClaimRefIDToClaimantPersonIDLookup.
                                         */
                                        ClaimRefIDToClaimantPersonIDLookup.put(ClaimRefID, ClaimantPersonID);

                                        /**
                                         * Add to AllClaimantPersonIDs and
                                         * AllPartnerPersonIDs.
                                         */
                                        AllClaimantPersonIDs.add(ClaimantPersonID);
                                        ClaimantPersonIDs.add(ClaimantPersonID);
                                        addToPersonIDToClaimRefsLookup(
                                                ClaimRefID,
                                                ClaimantPersonID,
                                                PersonIDToClaimRefIDsLookup);
                                        if (PartnerPersonID != null) {
                                            AllPartnerPersonIDs.add(PartnerPersonID);
                                            PartnerPersonIDs.add(PartnerPersonID);
                                            ClaimRefIDToPartnerPersonIDLookup.put(ClaimRefID, PartnerPersonID);
                                            addToPersonIDToClaimRefsLookup(
                                                    ClaimRefID,
                                                    PartnerPersonID,
                                                    PersonIDToClaimRefIDsLookup);
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
                        PersonIDToClaimRefIDsLookup,
                        ClaimRefIDToClaimRefLookup);
            }
            env.log("</Add SRecords>");

            env.log("<Summary Statistics>");
            /**
             * Add statistics to LoadSummary.
             */
            /**
             * Statistics on New SHBE Claims
             */
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewSHBEClaims,
                    ClaimRefIDsOfNewSHBEClaims.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewSHBEClaimsWhereClaimantWasClaimantBefore,
                    ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewSHBEClaimsWhereClaimantWasPartnerBefore,
                    ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewSHBEClaimsWhereClaimantWasNonDependentBefore,
                    ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewSHBEClaimsWhereClaimantIsNew,
                    ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew.size());
            /**
             * Statistics on Postcodes
             */
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewClaimantPostcodes,
                    CountOfNewClaimantPostcodes);
            addLoadSummaryCount(
                    DW_Strings.sCountOfNewValidMappableClaimantPostcodes,
                    CountOfNewMappableClaimantPostcodes);
            addLoadSummaryCount(DW_Strings.sCountOfMappableClaimantPostcodes,
                    CountOfMappableClaimantPostcodes);
            addLoadSummaryCount(
                    DW_Strings.sCountOfNonMappableClaimantPostcodes,
                    CountOfNonMappableClaimantPostcodes);
            addLoadSummaryCount(
                    DW_Strings.sCountOfInvalidFormatClaimantPostcodes,
                    CountOfValidFormatClaimantPostcodes);
            /**
             * General count statistics
             */
            addLoadSummaryCount(
                    DW_Strings.sCountOfClaims,
                    Records.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfCTBClaims,
                    totalCouncilTaxBenefitClaims);
            addLoadSummaryCount(
                    DW_Strings.sCountOfCTBAndHBClaims,
                    totalCouncilTaxAndHousingBenefitClaims);
            addLoadSummaryCount(
                    DW_Strings.sCountOfHBClaims,
                    totalHousingBenefitClaims);
            addLoadSummaryCount(
                    DW_Strings.sCountOfRecords,
                    Records.size());
            addLoadSummaryCount(
                    DW_Strings.sCountOfSRecords,
                    countSRecords);
            addLoadSummaryCount(
                    DW_Strings.sCountOfSRecordsNotLoaded,
                    SRecordNotLoadedCount);
            addLoadSummaryCount(
                    DW_Strings.sCountOfIncompleteDRecords,
                    NumberOfIncompleteDRecords);
            addLoadSummaryCount(
                    DW_Strings.sCountOfRecordIDsNotLoaded,
                    RecordIDsNotLoaded.size());
            HashSet<DW_PersonID> set;
            HashSet<DW_PersonID> allSet;
            allSet = new HashSet<DW_PersonID>();
            /**
             * Claimants
             */
            set = new HashSet<DW_PersonID>();
            set.addAll(ClaimRefIDToClaimantPersonIDLookup.values());
            allSet.addAll(set);
            addLoadSummaryCount(
                    DW_Strings.sCountOfUniqueClaimants,
                    set.size());
            /**
             * Partners
             */
            addLoadSummaryCount(
                    DW_Strings.sCountOfClaimsWithPartners,
                    ClaimRefIDToPartnerPersonIDLookup.size());
            set = DW_SHBE_Handler.getUniquePersonIDs0(ClaimRefIDToPartnerPersonIDLookup);
            allSet.addAll(set);
            addLoadSummaryCount(
                    DW_Strings.sCountOfUniquePartners,
                    set.size());
            /**
             * Dependents
             */
            int nDependents;
            nDependents = DW_Collections.getCountHashMap_DW_ID__HashSet_DW_PersonID(
                    ClaimRefIDToDependentPersonIDsLookup);
            addLoadSummaryCount(
                    DW_Strings.sCountOfDependentsInAllClaims,
                    nDependents);
            set = DW_SHBE_Handler.getUniquePersonIDs(ClaimRefIDToDependentPersonIDsLookup);
            allSet.addAll(set);
            int CountOfUniqueDependents = set.size();
            addLoadSummaryCount(
                    DW_Strings.sCountOfUniqueDependents,
                    CountOfUniqueDependents);
            /**
             * NonDependents
             */
            int nNonDependents;
            nNonDependents = DW_Collections.getCountHashMap_DW_ID__HashSet_DW_PersonID(
                    ClaimRefIDToNonDependentPersonIDsLookup);
            addLoadSummaryCount(
                    DW_Strings.sCountOfNonDependentsInAllClaims,
                    nNonDependents);
            set = DW_SHBE_Handler.getUniquePersonIDs(ClaimRefIDToNonDependentPersonIDsLookup);
            allSet.addAll(set);
            int CountOfUniqueNonDependents = set.size();
            addLoadSummaryCount(
                    DW_Strings.sCountOfUniqueNonDependents,
                    CountOfUniqueNonDependents);
            /**
             * All individuals
             */
            addLoadSummaryCount(
                    DW_Strings.sCountOfIndividuals,
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
            addLoadSummaryCount(DW_Strings.sCountOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim,
                    ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.size());
            addLoadSummaryCount(DW_Strings.sCountOfClaimsWithClaimantsThatArePartnersInAnotherClaim,
                    ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim.size());
            addLoadSummaryCount(DW_Strings.sCountOfClaimsWithPartnersThatAreClaimantsInAnotherClaim,
                    ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim.size());
            addLoadSummaryCount(DW_Strings.sCountOfClaimsWithPartnersThatArePartnersInAnotherClaim,
                    ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim.size());
            addLoadSummaryCount(DW_Strings.sCountOfClaimantsInMultipleClaimsInAMonth,
                    ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.size());
            addLoadSummaryCount(DW_Strings.sCountOfPartnersInMultipleClaimsInAMonth,
                    PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.size());
            addLoadSummaryCount(DW_Strings.sCountOfNonDependentsInMultipleClaimsInAMonth,
                    NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.size());
            addLoadSummaryCount(DW_Strings.sLineCount,
                    lineCount);
            addLoadSummaryCount(DW_Strings.sTotalIncome,
                    grandTotalIncome);
            addLoadSummaryCount(DW_Strings.sTotalIncomeGreaterThanZeroCount,
                    totalIncomeGreaterThanZeroCount);
            addLoadSummaryCount(DW_Strings.sAverage_NonZero_Income,
                    grandTotalIncome / (double) totalIncomeGreaterThanZeroCount);
            addLoadSummaryCount(DW_Strings.sTotalWeeklyEligibleRentAmount,
                    grandTotalWeeklyEligibleRentAmount);
            addLoadSummaryCount(DW_Strings.sTotalWeeklyEligibleRentAmountGreaterThanZeroCount,
                    totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            addLoadSummaryCount(DW_Strings.sAverage_NonZero_WeeklyEligibleRentAmount,
                    grandTotalWeeklyEligibleRentAmount / (double) totalWeeklyEligibleRentAmountGreaterThanZeroCount);
            env.log("<Summary Statistics>");

            /**
             * Write out data.
             */
            Generic_StaticIO.writeObject(Records, getRecordsFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfNewSHBEClaims,
                    getClaimRefIDsOfNewSHBEClaimsFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore,
                    getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore,
                    getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore,
                    getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew,
                    getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile());
            Generic_StaticIO.writeObject(ClaimantPersonIDs, getClaimantPersonIDsFile());
            Generic_StaticIO.writeObject(PartnerPersonIDs, getPartnerPersonIDsFile());
            Generic_StaticIO.writeObject(NonDependentPersonIDs, getNonDependentPersonIDsFile());
            Generic_StaticIO.writeObject(CottingleySpringsCaravanParkPairedClaimRefIDs,
                    getCottingleySpringsCaravanParkPairedClaimRefIDsFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfHBAtExtractDateInPayment,
                    getClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfHBAtExtractDateSuspended,
                    getClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfHBAtExtractDateOther,
                    getClaimRefIDsWithStatusOfHBAtExtractDateOtherFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfCTBAtExtractDateInPayment,
                    getClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfCTBAtExtractDateSuspended,
                    getClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile());
            Generic_StaticIO.writeObject(ClaimRefIDsWithStatusOfCTBAtExtractDateOther,
                    getClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile());
            Generic_StaticIO.writeObject(SRecordsWithoutDRecords, getSRecordsWithoutDRecordsFile());
            Generic_StaticIO.writeObject(ClaimRefIDAndCountOfRecordsWithSRecords,
                    getClaimRefIDAndCountOfRecordsWithSRecordsFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode,
                    getClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile());
            Generic_StaticIO.writeObject(ClaimRefIDToClaimantPersonIDLookup,
                    getClaimRefIDToClaimantPersonIDLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDToPartnerPersonIDLookup,
                    getClaimRefIDToPartnerPersonIDLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDToNonDependentPersonIDsLookup,
                    getClaimRefIDToNonDependentPersonIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDToDependentPersonIDsLookup,
                    getClaimRefIDToDependentPersonIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim,
                    getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim,
                    getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim,
                    getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim,
                    getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim,
                    getClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile());
            Generic_StaticIO.writeObject(ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile());
            Generic_StaticIO.writeObject(PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile());
            Generic_StaticIO.writeObject(NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDToPostcodeIDLookup, getClaimRefIDToPostcodeIDLookupFile());
            Generic_StaticIO.writeObject(ClaimRefIDToTenancyTypeLookup, getClaimRefIDToTenancyTypeLookupFile());
            Generic_StaticIO.writeObject(LoadSummary, getLoadSummaryFile());
            Generic_StaticIO.writeObject(RecordIDsNotLoaded, getRecordIDsNotLoadedFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfInvalidClaimantNINOClaims, getClaimRefIDsOfInvalidClaimantNINOClaimsFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesUnmappable, getClaimantPostcodesUnmappableFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesModified, getClaimantPostcodesModifiedFile());
            Generic_StaticIO.writeObject(ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes, getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile());
            Generic_StaticIO.writeObject(ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture, getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile());
            
            // Write out other outputs
            // Write out ClaimRefs of ClaimantsInMultipleClaimsInAMonth

            String YMN;
            YMN = DW_SHBE_Handler.getYearMonthNumber(inputFilename);
            writeOut(
                    ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    logDir,
                    "ClaimantsInMultipleClaimsInAMonth",
                    YMN,
                    ClaimRefIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of PartnersInMultipleClaimsInAMonth
            writeOut(
                    PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    logDir,
                    "PartnersInMultipleClaimsInAMonth",
                    YMN,
                    ClaimRefIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of PartnersInMultipleClaimsInAMonth
            writeOut(
                    NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
                    logDir,
                    "NonDependentsInMultipleClaimsInAMonth",
                    YMN,
                    ClaimRefIDToClaimRefLookup,
                    NINOIDToNINOLookup,
                    DOBIDToDOBLookup);
            // Write out ClaimRefs of ClaimRefIDOfInvalidClaimantNINOClaims
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
            ite2 = ClaimRefIDsOfInvalidClaimantNINOClaims.iterator();
            while (ite2.hasNext()) {
                DW_ID = ite2.next();
                pw.println(ClaimRefIDToClaimRefLookup.get(DW_ID));
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
            HashMap<DW_PersonID, HashSet<DW_ID>> InMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup,
            File logDir,
            String name,
            String YMN,
            HashMap<DW_ID, String> ClaimRefIDToClaimRefLookup,
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
        ite2 = InMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.keySet().iterator();
        while (ite2.hasNext()) {
            PersonID = ite2.next();
            NINO = NINOIDToNINOLookup.get(PersonID.getNINO_ID());
            DOB = DOBIDToDOBLookup.get(PersonID.getDOB_ID());
            if (!NINO.trim().equalsIgnoreCase("")) {
                if (!NINO.trim().startsWith("XX999")) {
                    ClaimRefs = InMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(PersonID);
                    ite3 = ClaimRefs.iterator();
                    s = NINO + "," + DOB;
                    while (ite3.hasNext()) {
                        DW_ID = ite3.next();
                        s += "," + ClaimRefIDToClaimRefLookup.get(DW_ID);
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
        env.logO(s + " " + n, true);
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
     * @param ClaimRefIDToClaimRefLookup
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
            HashMap<DW_ID, String> ClaimRefIDToClaimRefLookup
    ) {
        ArrayList<DW_SHBE_S_Record> SRecordsForClaim;
        DW_ID ClaimRefID;
        ClaimRefID = DW_SHBE_Record.ClaimRefID;
        Iterator<DW_SHBE_S_Record> ite;
        DW_SHBE_S_Record SRecord;
        String ClaimantsNINO;
        SRecordsForClaim = getSRecordsWithoutDRecords().get(ClaimRefID);
        if (SRecordsForClaim != null) {
            // Declare variables
            DW_PersonID DW_PersonID;
            String NINO;
            String DOB;
            int SubRecordType;
            Object key;
            DW_ID otherClaimRefID;
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
                            ClaimantsNINO = DW_Strings.sDefaultNINO;
                            env.logE("ClaimantsNINO is empty for "
                                    + "ClaimRefID " + ClaimRefID + " ClaimRef "
                                    + env.getDW_SHBE_Data().getClaimRefIDToClaimRefLookup().get(ClaimRefID)
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
                                    env.logO(env.DEBUG_Level_FINEST,
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
                                if (ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim.contains(ClaimRefID)) {
                                    set = true;
                                } else {
                                    env.logO(env.DEBUG_Level_FINEST,
                                            "NINO " + NINO + " is not unique for " + ClaimantsNINO
                                            + " and ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim does not contain "
                                            + "ClaimRefID " + ClaimRefID + " for ClaimRef "
                                            + env.getDW_SHBE_Data().getClaimRefIDToClaimRefLookup().get(ClaimRefID));
                                }
                            } else {
                                set = true;
                            }
                            while (!set) {
                                NINO = "" + i;
                                NINO += "_" + ClaimantsNINO;
                                if (NINOToNINOIDLookup.containsKey(NINO)) {
                                    env.logO(env.DEBUG_Level_FINEST,
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
                         * Add to ClaimRefIDToDependentPersonIDsLookup.
                         */
                        HashSet<DW_PersonID> s;
                        s = ClaimRefIDToDependentPersonIDsLookup.get(ClaimRefID);
                        if (s == null) {
                            s = new HashSet<DW_PersonID>();
                            ClaimRefIDToDependentPersonIDsLookup.put(ClaimRefID, s);
                        }
                        s.add(DW_PersonID);
                        addToPersonIDToClaimRefsLookup(
                                ClaimRefID,
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
                         * CottingleySpringsCaravanParkPairedClaimRefIDs. It may
                         * be that there are partners shared in these claims,
                         * but such a thing is ignored for now.
                         */
                        if (!CottingleySpringsCaravanParkPairedClaimRefIDs.contains(ClaimRefID)) {
                            /**
                             * If NonDependent is a NonDependent in another
                             * claim add to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.
                             */
                            key = DW_Collections.getKeyOfSetValue(ClaimRefIDToNonDependentPersonIDsLookup, DW_PersonID);
                            if (key != null) {
                                otherClaimRefID = (DW_ID) key;
                                HashSet<DW_ID> set;
                                set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(DW_PersonID);
                                if (set == null) {
                                    set = new HashSet<DW_ID>();
                                    NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(DW_PersonID, set);
                                }
                                set.add(ClaimRefID);
                                set.add(otherClaimRefID);
                                ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimRefID);
                                ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimRefID);
//                                if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                            + " is in ClaimRef " + ClaimRefIDToClaimRefLookup.get(ClaimRefID)
//                                            + " and " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                    env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                }
                            }
                            /**
                             * If NonDependent is a Claimant in another claim
                             * add to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.
                             */
                            if (ClaimRefIDToClaimantPersonIDLookup.containsValue(DW_PersonID)) {
                                if (key != null) {
                                    otherClaimRefID = (DW_ID) key;
                                    HashSet<DW_ID> set;
                                    set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(DW_PersonID);
                                    if (set == null) {
                                        set = new HashSet<DW_ID>();
                                        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(DW_PersonID, set);
                                    }
                                    set.add(ClaimRefID);
                                    set.add(otherClaimRefID);
                                    ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimRefID);
                                    ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimRefID);
//                                    if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                        env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                        env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                                + " in ClaimRef " + ClaimRefIDToClaimRefLookup.get(ClaimRefID)
//                                                + " is a Claimant in " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                        env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    }
                                }
                            }
                            /**
                             * If NonDependent is a Partner in another claim add
                             * to
                             * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;
                             */
                            if (ClaimRefIDToPartnerPersonIDLookup.containsValue(DW_PersonID)) {
                                if (key != null) {
                                    otherClaimRefID = (DW_ID) key;
                                    HashSet<DW_ID> set;
                                    set = NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.get(DW_PersonID);
                                    if (set == null) {
                                        set = new HashSet<DW_ID>();
                                        NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup.put(DW_PersonID, set);
                                    }
                                    set.add(ClaimRefID);
                                    set.add(otherClaimRefID);
                                    ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(ClaimRefID);
                                    ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim.add(otherClaimRefID);
//                                    if (!(NINO.trim().equalsIgnoreCase("") || NINO.startsWith("XX999"))) {
//                                        env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                        env.logO("NonDependent with NINO " + NINO + " DOB " + DOB
//                                                + " in ClaimRef " + ClaimRefIDToClaimRefLookup.get(ClaimRefID)
//                                                + " is a Partner in " + ClaimRefIDToClaimRefLookup.get(otherClaimRefID));
//                                        env.logO("!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                                    }
                                }
                            }
                        }
                        //HashSet<DW_PersonID> s;
                        s = ClaimRefIDToNonDependentPersonIDsLookup.get(ClaimRefID);
                        if (s == null) {
                            s = new HashSet<DW_PersonID>();
                            ClaimRefIDToNonDependentPersonIDsLookup.put(ClaimRefID, s);
                        }
                        s.add(DW_PersonID);
                        NonDependentPersonIDs.add(DW_PersonID);
                        AllNonDependentPersonIDs.add(DW_PersonID);
                        addToPersonIDToClaimRefsLookup(
                                ClaimRefID,
                                DW_PersonID,
                                PersonIDToClaimRefsLookup);
                        break;
                    default:
                        env.logE("Unrecognised SubRecordType " + SubRecordType);
                        break;
                }
            }
            DW_SHBE_Record.SRecords = SRecordsForClaim;
            ClaimRefIDAndCountOfRecordsWithSRecords.put(ClaimRefID, SRecordsForClaim.size());
        }
        /**
         * Remove all assigned SRecords from SRecordsWithoutDRecords.
         */
        Iterator<DW_ID> iteID;
        iteID = ClaimRefIDAndCountOfRecordsWithSRecords.keySet().iterator();
        while (iteID.hasNext()) {
            SRecordsWithoutDRecords.remove(iteID.next());
        }
    }

    private void addToPersonIDToClaimRefsLookup(
            DW_ID ClaimRefID,
            DW_PersonID DW_PersonID,
            HashMap<DW_PersonID, HashSet<DW_ID>> PersonIDToClaimRefsLookup) {
        HashSet<DW_ID> s;
        if (PersonIDToClaimRefsLookup.containsKey(DW_PersonID)) {
            s = PersonIDToClaimRefsLookup.get(DW_PersonID);
        } else {
            s = new HashSet<DW_ID>();
            PersonIDToClaimRefsLookup.put(DW_PersonID, s);
        }
        s.add(ClaimRefID);
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
                env.logE("Unrecognised header in DW_SHBE_Records.readAndCheckFirstLine(File,String)");
                env.logE("Number of fields in header " + lineSplit.length);
                env.logE("header:");
                env.logE(line);

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
     * If not initialised, initialises ClaimRefIDToClaimantPersonIDLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_PersonID> getClaimRefIDToClaimantPersonIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToClaimantPersonIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToClaimantPersonIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimantClaimRefIDs then returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, DW_PersonID> getClaimRefIDToClaimantPersonIDLookup() {
        if (ClaimRefIDToClaimantPersonIDLookup == null) {
            File f;
            f = getClaimRefIDToClaimantPersonIDLookupFile();
            if (f.exists()) {
                ClaimRefIDToClaimantPersonIDLookup = (HashMap<DW_ID, DW_PersonID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToClaimantPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
            }
        }
        return ClaimRefIDToClaimantPersonIDLookup;
    }

    /**
     * If not initialised, initialises ClaimRefIDToPartnerPersonIDLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_PersonID> getClaimRefIDToPartnerPersonIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToPartnerPersonIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToPartnerPersonIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises PartnerClaimRefIDs then returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, DW_PersonID> getClaimRefIDToPartnerPersonIDLookup() {
        if (ClaimRefIDToPartnerPersonIDLookup == null) {
            File f;
            f = getClaimRefIDToPartnerPersonIDLookupFile();
            if (f.exists()) {
                ClaimRefIDToPartnerPersonIDLookup = (HashMap<DW_ID, DW_PersonID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToPartnerPersonIDLookup = new HashMap<DW_ID, DW_PersonID>();
            }
        }
        return ClaimRefIDToPartnerPersonIDLookup;
    }

    /**
     * If not initialised, initialises ClaimRefIDToDependentPersonIDsLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, HashSet<DW_PersonID>> getClaimRefIDToDependentPersonIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToDependentPersonIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToDependentPersonIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimRefIDToDependentPersonIDsLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, HashSet<DW_PersonID>> getClaimRefIDToDependentPersonIDsLookup() {
        if (ClaimRefIDToDependentPersonIDsLookup == null) {
            File f;
            f = getClaimRefIDToDependentPersonIDsLookupFile();
            if (f.exists()) {
                ClaimRefIDToDependentPersonIDsLookup = (HashMap<DW_ID, HashSet<DW_PersonID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
            }
        }
        return ClaimRefIDToDependentPersonIDsLookup;
    }

    /**
     * If not initialised, initialises ClaimRefIDToNonDependentPersonIDsLookup
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, HashSet<DW_PersonID>> getClaimRefIDToNonDependentPersonIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToNonDependentPersonIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToNonDependentPersonIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimRefIDToNonDependentPersonIDsLookup
     * then returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, HashSet<DW_PersonID>> getClaimRefIDToNonDependentPersonIDsLookup() {
        if (ClaimRefIDToNonDependentPersonIDsLookup == null) {
            File f;
            f = getClaimRefIDToNonDependentPersonIDsLookupFile();
            if (f.exists()) {
                ClaimRefIDToNonDependentPersonIDsLookup = (HashMap<DW_ID, HashSet<DW_PersonID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToNonDependentPersonIDsLookup = new HashMap<DW_ID, HashSet<DW_PersonID>>();
            }
        }
        return ClaimRefIDToNonDependentPersonIDsLookup;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim then
     * returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim() {
        if (ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then
     * returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim() {
        if (ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim then
     * returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim() {
        if (ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim then returns
     * it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim() {
        if (ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @return
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim() {
        if (ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaim
     * then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup then returns
     * it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup() {
        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup == null) {
            File f;
            f = getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile();
            if (f.exists()) {
                ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;
    }

    /**
     * If not initialised, initialises
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup then returns
     * it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup() {
        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup == null) {
            File f;
            f = getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile();
            if (f.exists()) {
                PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;
    }

    /**
     * If not initialised, initialises
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_PersonID, HashSet<DW_ID>> getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(boolean handleOutOfMemoryError) {
        try {
            return getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_PersonID, HashSet<DW_ID>> getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup() {
        if (NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup == null) {
            File f;
            f = getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile();
            if (f.exists()) {
                NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = (HashMap<DW_PersonID, HashSet<DW_ID>>) Generic_StaticIO.readObject(f);
            } else {
                NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup = new HashMap<DW_PersonID, HashSet<DW_ID>>();
            }
        }
        return NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup;
    }

    /**
     * If not initialised, initialises ClaimRefIDToPostcodeLookup then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, DW_ID> getClaimRefIDToPostcodeIDLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToPostcodeIDLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToPostcodeIDLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimRefIDToPostcodeLookup
     */
    protected HashMap<DW_ID, DW_ID> getClaimRefIDToPostcodeIDLookup() {
        if (ClaimRefIDToPostcodeIDLookup == null) {
            File f;
            f = getClaimRefIDToPostcodeIDLookupFile();
            if (f.exists()) {
                ClaimRefIDToPostcodeIDLookup = (HashMap<DW_ID, DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToPostcodeIDLookup = new HashMap<DW_ID, DW_ID>();
            }
        }
        return ClaimRefIDToPostcodeIDLookup;
    }

    /**
     * If not initialised, initialises
     * ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture
     */
    protected HashSet<DW_ID> getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture() {
        if (ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture == null) {
            File f;
            f = getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile();
            if (f.exists()) {
                ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture;
    }

    /**
     * If not initialised, initialises ClaimRefIDToTenancyTypeLookup then
     * returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, Integer> getClaimRefIDToTenancyTypeLookup(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDToTenancyTypeLookup();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDToTenancyTypeLookup(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises ClaimRefIDToTenancyTypeLookup then
     * returns it.
     *
     * @return
     */
    protected HashMap<DW_ID, Integer> getClaimRefIDToTenancyTypeLookup() {
        if (ClaimRefIDToTenancyTypeLookup == null) {
            File f;
            f = getClaimRefIDToTenancyTypeLookupFile();
            if (f.exists()) {
                ClaimRefIDToTenancyTypeLookup = (HashMap<DW_ID, Integer>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDToTenancyTypeLookup = new HashMap<DW_ID, Integer>();
            }
        }
        return ClaimRefIDToTenancyTypeLookup;
    }

    /**
     * If not initialised, initialises LoadSummary then returns it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<String, Number> getLoadSummary(boolean handleOutOfMemoryError) {
        try {
            return getLoadSummary();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getLoadSummary(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * If not initialised, initialises LoadSummary then returns it.
     *
     * @return
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
     * @return
     */
    public final ArrayList<Long> getRecordIDsNotLoaded(boolean handleOutOfMemoryError) {
        try {
            return getRecordIDsNotLoaded();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getRecordIDsNotLoaded(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the RecordIDsNotLoaded
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
     * @return
     */
    public final HashSet<DW_ID> getClaimRefIDsOfInvalidClaimantNINOClaims(boolean handleOutOfMemoryError) {
        try {
            return getClaimRefIDsOfInvalidClaimantNINOClaims();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimRefIDsOfInvalidClaimantNINOClaims(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimRefsOfInvalidClaimantNINOClaims
     */
    protected HashSet<DW_ID> getClaimRefIDsOfInvalidClaimantNINOClaims() {
        if (ClaimRefIDsOfInvalidClaimantNINOClaims == null) {
            File f;
            f = getClaimRefIDsOfInvalidClaimantNINOClaimsFile();
            if (f.exists()) {
                ClaimRefIDsOfInvalidClaimantNINOClaims = (HashSet<DW_ID>) Generic_StaticIO.readObject(f);
            } else {
                ClaimRefIDsOfInvalidClaimantNINOClaims = new HashSet<DW_ID>();
            }
        }
        return ClaimRefIDsOfInvalidClaimantNINOClaims;
    }

    /**
     * If not initialised, initialises ClaimantPostcodesUnmappable then returns
     * it.
     *
     * @param handleOutOfMemoryError
     * @return
     */
    public final HashMap<DW_ID, String> getClaimantPostcodesUnmappable(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPostcodesUnmappable();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimantPostcodesUnmappable(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimantPostcodesUnmappable
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
     * @return
     */
    public final HashMap<DW_ID, String[]> getClaimantPostcodesModified(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPostcodesModified();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimantPostcodesModified(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimantPostcodesModified
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
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
                return getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes(handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }

    /**
     * @return the ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes
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
                    + DW_Strings.sBinaryFileExtension);
        }
        return File;
    }

    /**
     * @return RecordsFile initialising if it is not already initialised.
     */
    protected final File getRecordsFile() {
        if (RecordsFile == null) {
            RecordsFile = getFile(
                    DW_Strings.sRecords
                    + DW_Strings.sUnderscore
                    + "HashMap_String__DW_SHBE_Record"
                    + DW_Strings.sBinaryFileExtension);
        }
        return RecordsFile;
    }

    /**
     * @return ClaimRefIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimRefIDsOfNewSHBEClaimsFile() {
        if (ClaimRefIDsOfNewSHBEClaimsFile == null) {
            ClaimRefIDsOfNewSHBEClaimsFile = getFile(
                    "ClaimRefIDsOfNewSHBEClaims"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNewSHBEClaimsFile;
    }

    /**
     * @return ClaimRefIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile == null) {
            ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile = getFile(
                    "ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBefore"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasClaimantBeforeFile;
    }

    /**
     * @return ClaimRefIDsOfNewSHBEClaimsFile initialising if it is not already
     * initialised.
     */
    protected final File getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile == null) {
            ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile = getFile(
                    "ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBefore"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasPartnerBeforeFile;
    }

    /**
     * @return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile
     * initialising if it is not already initialised.
     */
    protected final File getClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile == null) {
            ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile = getFile(
                    "ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBefore"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantWasNonDependentBeforeFile;
    }

    /**
     * @return ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile() {
        if (ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile == null) {
            ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile = getFile(
                    "ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNew"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNewSHBEClaimsWhereClaimantIsNewFile;
    }

    public final File getClaimantPersonIDsFile() {
        if (ClaimantPersonIDsFile == null) {
            ClaimantPersonIDsFile = getFile(
                    "Claimant"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimantPersonIDsFile;
    }

    public final File getPartnerPersonIDsFile() {
        if (PartnerPersonIDsFile == null) {
            PartnerPersonIDsFile = getFile(
                    "Partner"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return PartnerPersonIDsFile;
    }

    public final File getNonDependentPersonIDsFile() {
        if (NonDependentPersonIDsFile == null) {
            NonDependentPersonIDsFile = getFile(
                    "NonDependent"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return NonDependentPersonIDsFile;
    }

    public final HashSet<DW_PersonID> getClaimantPersonIDs(boolean handleOutOfMemoryError) {
        try {
            return getClaimantPersonIDs();
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
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
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
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
                env.clear_MemoryReserve();
                if (!env.clearSomeSHBECacheExcept(YM3)) {
                    throw e;
                }
                env.init_MemoryReserve(handleOutOfMemoryError);
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
     * @return CottingleySpringsCaravanParkPairedClaimRefIDsFile initialising if
     * it is not already initialised.
     */
    protected final File getCottingleySpringsCaravanParkPairedClaimRefIDsFile() {
        if (CottingleySpringsCaravanParkPairedClaimRefIDsFile == null) {
            CottingleySpringsCaravanParkPairedClaimRefIDsFile = getFile(
                    DW_Strings.sCottingleySpringsCaravanPark + "PairedClaimRefIDs"
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return CottingleySpringsCaravanParkPairedClaimRefIDsFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile initialising
     * if it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile == null) {
            ClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile = getFile(
                    DW_Strings.sHB + DW_Strings.sPaymentTypeIn
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateInPaymentFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile initialising
     * if it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile == null) {
            ClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile = getFile(
                    DW_Strings.sHB + DW_Strings.sPaymentTypeSuspended
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateSuspendedFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfHBAtExtractDateOtherFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfHBAtExtractDateOtherFile() {
        if (ClaimRefIDsWithStatusOfHBAtExtractDateOtherFile == null) {
            ClaimRefIDsWithStatusOfHBAtExtractDateOtherFile = getFile(
                    DW_Strings.sHB + DW_Strings.sPaymentTypeOther
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfHBAtExtractDateOtherFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile initialising
     * if it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile == null) {
            ClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile = getFile(
                    DW_Strings.sCTB + DW_Strings.sPaymentTypeIn
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateInPaymentFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile initialising
     * if it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile == null) {
            ClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile = getFile(
                    DW_Strings.sCTB + DW_Strings.sPaymentTypeSuspended
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateSuspendedFile;
    }

    /**
     * @return ClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile initialising if
     * it is not already initialised.
     */
    protected final File getClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile() {
        if (ClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile == null) {
            ClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile = getFile(
                    DW_Strings.sCTB + DW_Strings.sPaymentTypeOther
                    + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsWithStatusOfCTBAtExtractDateOtherFile;
    }

    /**
     * @return SRecordsWithoutDRecordsFile initialising if it is not already
     * initialised.
     */
    protected final File getSRecordsWithoutDRecordsFile() {
        if (SRecordsWithoutDRecordsFile == null) {
            SRecordsWithoutDRecordsFile = getFile(
                    "SRecordsWithoutDRecordsFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__ArrayList_DW_SHBE_S_Record"
                    + DW_Strings.sBinaryFileExtension);
        }
        return SRecordsWithoutDRecordsFile;
    }

    /**
     * @return ClaimRefIDAndCountOfRecordsWithSRecordsFile initialising if it is
     * not already initialised.
     */
    protected final File getClaimRefIDAndCountOfRecordsWithSRecordsFile() {
        if (ClaimRefIDAndCountOfRecordsWithSRecordsFile == null) {
            ClaimRefIDAndCountOfRecordsWithSRecordsFile = getFile(
                    "ClaimRefIDAndCountOfRecordsWithSRecordsFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDAndCountOfRecordsWithSRecordsFile;
    }

    /**
     * @return ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile
     * initialising if it is not already initialised.
     */
    protected final File getClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile() {
        if (ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile == null) {
            ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile = getFile(
                    "ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcode" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithoutAMappableClaimantPostcodeFile;
    }

    /**
     * @return the ClaimantClaimRefIDsFile
     */
    public final File getClaimRefIDToClaimantPersonIDLookupFile() {
        if (ClaimRefIDToClaimantPersonIDLookupFile == null) {
            ClaimRefIDToClaimantPersonIDLookupFile = getFile(
                    "ClaimRefIDToClaimantPersonIDLookup" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToClaimantPersonIDLookupFile;
    }

    /**
     * @return the ClaimRefIDToPartnerPersonIDLookupFile
     */
    public final File getClaimRefIDToPartnerPersonIDLookupFile() {
        if (ClaimRefIDToPartnerPersonIDLookupFile == null) {
            ClaimRefIDToPartnerPersonIDLookupFile = getFile(
                    "ClaimRefIDToPartnerPersonIDLookup" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToPartnerPersonIDLookupFile;
    }

    /**
     * @return the ClaimRefIDToDependentPersonIDsLookupFile
     */
    public final File getClaimRefIDToDependentPersonIDsLookupFile() {
        if (ClaimRefIDToDependentPersonIDsLookupFile == null) {
            ClaimRefIDToDependentPersonIDsLookupFile = getFile(
                    "ClaimRefIDToDependentPersonIDsLookupFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__HashSet<DW_PersonID>"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToDependentPersonIDsLookupFile;
    }

    /**
     * @return the NonDependentClaimRefIDsFile
     */
    public final File getClaimRefIDToNonDependentPersonIDsLookupFile() {
        if (ClaimRefIDToNonDependentPersonIDsLookupFile == null) {
            ClaimRefIDToNonDependentPersonIDsLookupFile = getFile(
                    "ClaimRefIDToNonDependentPersonIDsLookupFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__HashSet_DW_PersonID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToNonDependentPersonIDsLookupFile;
    }

    /**
     * @return the
     * ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile
     */
    public final File getClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile() {
        if (ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile == null) {
            ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile = getFile(
                    "ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaim" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithClaimantsThatAreClaimantsInAnotherClaimFile;
    }

    /**
     * @return the
     * ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile
     */
    public final File getClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile() {
        if (ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile == null) {
            ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile = getFile(
                    "ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaim" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithClaimantsThatArePartnersInAnotherClaimFile;
    }

    /**
     * @return the
     * ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile
     */
    public final File getClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile() {
        if (ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile == null) {
            ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile = getFile(
                    "ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithPartnersThatAreClaimantsInAnotherClaimFile;
    }

    /**
     * @return the
     * ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile
     */
    public final File getClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile() {
        if (ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile == null) {
            ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile = getFile(
                    "ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaim" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithPartnersThatArePartnersInAnotherClaimFile;
    }

    /**
     * @return the
     * ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile
     */
    public final File getClaimRefIDsOfClaimsWithNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile() {
        if (ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile == null) {
            ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile = getFile(
                    "ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaim" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfNonDependentsThatAreClaimantsOrPartnersInAnotherClaimFile;
    }

    /**
     * @return the
     * ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile
     */
    public final File getClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile() {
        if (ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile == null) {
            ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile = getFile(
                    "ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookup" + DW_Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimantsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;
    }

    /**
     * @return the
     * PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile
     */
    public final File getPartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile() {
        if (PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile == null) {
            PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile = getFile(
                    "PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return PartnersInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;
    }

    /**
     * @return the
     * NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile
     */
    public final File getNonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile() {
        if (NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile == null) {
            NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile = getFile(
                    "NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile" + DW_Strings.sUnderscore
                    + "HashMap_DW_PersonID__HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return NonDependentsInMultipleClaimsInAMonthPersonIDToClaimRefIDsLookupFile;
    }

    /**
     * @return the ClaimRefIDToPostcodeLookupFile
     */
    public final File getClaimRefIDToPostcodeIDLookupFile() {
        if (ClaimRefIDToPostcodeIDLookupFile == null) {
            ClaimRefIDToPostcodeIDLookupFile = getFile(
                    "ClaimRefIDToPostcodeIDLookup" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToPostcodeIDLookupFile;
    }

    /**
     * @return the ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile
     */
    public final File getClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile() {
        if (ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile == null) {
            ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile = getFile(
                    "ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFuture" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfClaimsWithClaimPostcodeFUpdatedFromTheFutureFile;
    }

    /**
     * @return the ClaimRefIDToTenancyTypeLookupFile
     */
    public final File getClaimRefIDToTenancyTypeLookupFile() {
        if (ClaimRefIDToTenancyTypeLookupFile == null) {
            ClaimRefIDToTenancyTypeLookupFile = getFile(
                    "ClaimRefIDToTenancyTypeLookup" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__Integer"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDToTenancyTypeLookupFile;
    }

    /**
     * @return the LoadSummaryFile
     */
    public final File getLoadSummaryFile() {
        if (LoadSummaryFile == null) {
            LoadSummaryFile = getFile(
                    "LoadSummary" + DW_Strings.sUnderscore
                    + "HashMap_String__Integer"
                    + DW_Strings.sBinaryFileExtension);
        }
        return LoadSummaryFile;
    }

    /**
     * @return the RecordIDsNotLoadedFile
     */
    public final File getRecordIDsNotLoadedFile() {
        if (RecordIDsNotLoadedFile == null) {
            RecordIDsNotLoadedFile = getFile(
                    "RecordIDsNotLoaded" + DW_Strings.sUnderscore
                    + "ArrayList_Long"
                    + DW_Strings.sBinaryFileExtension);
        }
        return RecordIDsNotLoadedFile;
    }

    /**
     * @return the ClaimRefsOfInvalidClaimantNINOClaimsFile
     */
    public final File getClaimRefIDsOfInvalidClaimantNINOClaimsFile() {
        if (ClaimRefIDsOfInvalidClaimantNINOClaimsFile == null) {
            ClaimRefIDsOfInvalidClaimantNINOClaimsFile = getFile(
                    "ClaimRefIDsOfInvalidClaimantNINOClaimsFile" + DW_Strings.sUnderscore
                    + "HashSet_DW_ID"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimRefIDsOfInvalidClaimantNINOClaimsFile;
    }

    /**
     * @return the ClaimantPostcodesUnmappableFile
     */
    public final File getClaimantPostcodesUnmappableFile() {
        if (ClaimantPostcodesUnmappableFile == null) {
            ClaimantPostcodesUnmappableFile = getFile(
                    "ClaimantPostcodesUnmappable" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimantPostcodesUnmappableFile;
    }

    /**
     * @return the ClaimantPostcodesModifiedFile
     */
    public final File getClaimantPostcodesModifiedFile() {
        if (ClaimantPostcodesModifiedFile == null) {
            ClaimantPostcodesModifiedFile = getFile(
                    "ClaimantPostcodesModified" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__String[]"
                    + DW_Strings.sBinaryFileExtension);
        }
        return ClaimantPostcodesModifiedFile;
    }

    /**
     * @return the ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile
     */
    public final File getClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile() {
        if (ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile == null) {
            ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodesFile = getFile(
                    "ClaimantPostcodesCheckedAsMappableButNotInONSPDPostcodes" + DW_Strings.sUnderscore
                    + "HashMap_DW_ID__String"
                    + DW_Strings.sBinaryFileExtension);
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
