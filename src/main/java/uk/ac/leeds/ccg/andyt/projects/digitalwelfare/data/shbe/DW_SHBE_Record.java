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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Strings;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_Record extends DW_Object implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * DW_Strings for convenience. Set from env.getDW_Strings()
     */
    protected transient DW_Strings DW_Strings;

    /**
     * StatusOfHBClaimAtExtractDate 0 is OtherPaymentType 1 is InPayment 2 is
     * Suspended
     */
    private int StatusOfHBClaimAtExtractDate;

    /**
     * The Year_Month of the records. This is for retrieving the Year_Month
     * DW_SHBE_Records which this is part of.
     */
    protected String YM3;

    /**
     * The ClaimRef DW_ID.
     */
    protected DW_ID ClaimRefID;

    /**
     * A convenient lookup for knowing if ClaimPostcodeF is a valid format for a
     * UK postcode.
     */
    protected boolean ClaimPostcodeFValidPostcodeFormat;

    /**
     * A convenient lookup for knowing if ClaimPostcodeF is mappable.
     */
    protected boolean ClaimPostcodeFMappable;

    /**
     * For storing a ONSPD format version of ClaimPostcodeF1.
     */
    protected String ClaimPostcodeF;

    /**
     * For storing if the Claimant Postcode has been modified by removing non
     * A-Z, a-z, 0-9 characters and replacing "O" with "0" or removing "0"
     * altogether.
     */
    protected boolean ClaimPostcodeFAutoModified;

    /**
     * For storing if the ClaimPostcodeF was subsequently (since the extract)
     * checked and modified.
     */
    protected boolean ClaimPostcodeFManModified;

    /**
     * For storing if the ClaimPostcodeF has been updated from the future. For 
     * the time being, this is only allowed for Claimant Postcodes that were 
     * originally blank or that had invalid formats.
     */
    protected boolean ClaimPostcodeFUpdatedFromTheFuture;

    /**
     * The Postcode DW_ID.
     */
    protected DW_ID PostcodeID;

    /**
     * DRecord
     */
    protected DW_SHBE_D_Record DRecord;

    /**
     * SRecords associated with a DRecord
     */
    protected ArrayList<DW_SHBE_S_Record> SRecords;

    /**
     *
     * @param env
     * @param YM3 The Year_Month of this.
     * @param ClaimRefID The ClaimRef DW_ID for this.
     */
    public DW_SHBE_Record(DW_Environment env, String YM3, DW_ID ClaimRefID) {
        super(env);
        DW_Strings = env.getDW_Strings();
        this.YM3 = YM3;
        this.ClaimRefID = ClaimRefID;
    }

    /**
     * Creates a DW_SHBE_Record.
     *
     * @param env
     * @param YM3 The Year_Month of this.
     * @param ClaimRefID The ClaimRef DW_ID for this.
     * @param DRecord
     */
    public DW_SHBE_Record(
            DW_Environment env,
            String YM3,
            DW_ID ClaimRefID,
            DW_SHBE_D_Record DRecord) {
        super(env);
        this.DW_Strings = env.getDW_Strings();
        this.YM3 = YM3;
        this.ClaimRefID = ClaimRefID;
        this.DRecord = DRecord;
    }

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
     * Returns a Brief String description of this.
     *
     * @return
     */
    public String toStringBrief() {
        String result;
        result = "YM3 " + YM3;
        result += DW_Strings.sNewLine;
        if (DRecord != null) {
            result += "DRecord: " + DRecord.toStringBrief();
            result += DW_Strings.sNewLine;
        }
        SRecords = getSRecords();
        if (SRecords != null) {
            long NumberOfS_Records;
            NumberOfS_Records = SRecords.size();
            result += " Number of SRecords = " + NumberOfS_Records;
            result += DW_Strings.sNewLine;
            if (NumberOfS_Records > 0) {
                result += ": ";
            }
            Iterator<DW_SHBE_S_Record> ite;
            ite = SRecords.iterator();
            while (ite.hasNext()) {
                DW_SHBE_S_Record rec;
                rec = ite.next();
                result += " SRecord: " + rec.toString();
                result += DW_Strings.sNewLine;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result;
        result = "ClaimRefDW_ID " + ClaimRefID
                + DW_Strings.sNewLine
                + "StatusOfHBClaimAtExtractDate " + StatusOfHBClaimAtExtractDate
                + DW_Strings.sNewLine
                + "YM3 " + YM3
                + DW_Strings.sNewLine;
        if (DRecord != null) {
            result += "DRecord: " + DRecord.toString()
                    + DW_Strings.sNewLine;
        }
        SRecords = getSRecords();
        if (SRecords != null) {
            long NumberOfS_Records;
            NumberOfS_Records = SRecords.size();
            result += " Number of SRecords = " + NumberOfS_Records
                    + DW_Strings.sNewLine;
            if (NumberOfS_Records > 0) {
                result += ": ";
            }
            Iterator<DW_SHBE_S_Record> ite;
            ite = SRecords.iterator();
            while (ite.hasNext()) {
                DW_SHBE_S_Record rec;
                rec = ite.next();
                result += " SRecord: " + rec.toString()
                        + DW_Strings.sNewLine;
            }
        }
        return result;
    }

    /**
     * @return ClaimRefDW_ID
     */
    public DW_ID getClaimRefID() {
        return ClaimRefID;
    }

    /**
     * @return a copy of StatusOfHBClaimAtExtractDate.
     */
    public int getStatusOfHBClaimAtExtractDate() {
        return StatusOfHBClaimAtExtractDate;
    }

    /**
     * @param StatusOfHBClaimAtExtractDate the StatusOfHBClaimAtExtractDate to
     * set
     */
    protected final void getStatusOfHBClaimAtExtractDate(int StatusOfHBClaimAtExtractDate) {
        this.StatusOfHBClaimAtExtractDate = StatusOfHBClaimAtExtractDate;
    }

    /**
     * @return PaymentType
     */
    public String getPaymentType() {
        return env.getDW_Strings().getPaymentTypes().get(StatusOfHBClaimAtExtractDate + 1);
    }

    /**
     *
     * @return
     */
    public DW_SHBE_D_Record getDRecord() {
        return DRecord;
    }

    /**
     * @return the SRecords initialising if needs be.
     */
    public final ArrayList<DW_SHBE_S_Record> getSRecords() {
        return SRecords;
    }

    /**
     * @param SRecords the SRecords to set
     */
    public void setSRecords(ArrayList<DW_SHBE_S_Record> SRecords) {
        this.SRecords = SRecords;
    }

    /**
     * @return the ClaimPostcodeF
     */
    public String getClaimPostcodeF() {
        return ClaimPostcodeF;
    }

    /**
     * @return ClaimPostcodeFValidPostcodeFormat
     */
    public boolean isClaimPostcodeFValidFormat() {
        return ClaimPostcodeFValidPostcodeFormat;
    }

    /**
     * @return ClaimPostcodeFMappable
     */
    public boolean isClaimPostcodeFMappable() {
        return ClaimPostcodeFMappable;
    }

    /**
     * @return the PostcodeID
     */
    public DW_ID getPostcodeID() {
        return PostcodeID;
    }
}
