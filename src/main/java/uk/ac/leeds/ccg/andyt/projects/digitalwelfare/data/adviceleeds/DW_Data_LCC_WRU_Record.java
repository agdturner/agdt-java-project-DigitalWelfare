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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.adviceleeds;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_IO;

/**
 *
 * @author geoagdt
 */
public class DW_Data_LCC_WRU_Record extends DW_Data_Postcode_Record {

    // Header
    //Unique Customer Ref,Date of Birth,Ethnicity Type,Ethnicity Sub Type,Postcode,Enquiry Reference Number,Activity Ref,Type,Interview Location,Interview Date,Benefit Type,Disability,AgeAtInterview,Age Now
    // Example record
    //1-102J,20-Sep-1936,Not Stated,Refused,LS6 1LS,2-464768375,2-7SFJ2M,Welfare Rights,Home Visit (MacMillan),05-Dec-2011,Blue Badge,-,75,77.11
    /**
     * RecordID
     */
    private String UniqueCustomerRef;
    private String DateOfBirth;
    private String EthnicityType;
    private String EthnicitySubType;
//    private String Postcode;
    private String EnquiryReferenceNumber;
    private String ActivityRef;
    private String Type;
    private String InterviewLocation;
    private String InterviewDate;
    private String BenefitType;
    private String Disability;
    private String AgeAtInterview;
    private String AgeNow;

    public DW_Data_LCC_WRU_Record(DW_Environment env) {
       super(env);
    }

    /**
     * @param RecordID
     * @param line
     * @param handler
     * @throws java.lang.Exception
     */
    public DW_Data_LCC_WRU_Record(
            DW_Environment env,
            long RecordID,
            String line,
            DW_Data_LCC_WRU_Handler handler) throws Exception {
        super(env);
        setRecordID(RecordID);
        String[] fields;
        fields = DW_IO.splitWithQuotesThenCommas(line);
        int fieldCount = fields.length;
        if (fieldCount != 14) {
            System.out.println("RecordID " + RecordID + ", fieldCount" + fieldCount);
        }
        UniqueCustomerRef = fields[0];
        DateOfBirth = fields[1];
        EthnicityType = fields[2];
        EthnicitySubType = fields[3];
        setPostcode(fields[4]);
        EnquiryReferenceNumber = fields[5];
        ActivityRef = fields[6];
        Type = fields[7];
        InterviewLocation = fields[8];
        InterviewDate = fields[9];
        BenefitType = fields[10];
        Disability = fields[11];
        AgeAtInterview = fields[12];
        AgeNow = fields[13];
    }

    /**
     * @return the UniqueCustomerRef
     */
    public String getUniqueCustomerRef() {
        return UniqueCustomerRef;
    }

    /**
     * @param UniqueCustomerRef the UniqueCustomerRef to set
     */
    public void setUniqueCustomerRef(String UniqueCustomerRef) {
        this.UniqueCustomerRef = UniqueCustomerRef;
    }

    /**
     * @return the DateOfBirth
     */
    public String getDateOfBirth() {
        return DateOfBirth;
    }

    /**
     * @param DateOfBirth the DateOfBirth to set
     */
    public void setDateOfBirth(String DateOfBirth) {
        this.DateOfBirth = DateOfBirth;
    }

    /**
     * @return the EthnicityType
     */
    public String getEthnicityType() {
        return EthnicityType;
    }

    /**
     * @param EthnicityType the EthnicityType to set
     */
    public void setEthnicityType(String EthnicityType) {
        this.EthnicityType = EthnicityType;
    }

    /**
     * @return the EthnicitySubType
     */
    public String getEthnicitySubType() {
        return EthnicitySubType;
    }

    /**
     * @param EthnicitySubType the EthnicitySubType to set
     */
    public void setEthnicitySubType(String EthnicitySubType) {
        this.EthnicitySubType = EthnicitySubType;
    }

    /**
     * @return the EnquiryReferenceNumber
     */
    public String getEnquiryReferenceNumber() {
        return EnquiryReferenceNumber;
    }

    /**
     * @param EnquiryReferenceNumber the EnquiryReferenceNumber to set
     */
    public void setEnquiryReferenceNumber(String EnquiryReferenceNumber) {
        this.EnquiryReferenceNumber = EnquiryReferenceNumber;
    }

    /**
     * @return the ActivityRef
     */
    public String getActivityRef() {
        return ActivityRef;
    }

    /**
     * @param ActivityRef the ActivityRef to set
     */
    public void setActivityRef(String ActivityRef) {
        this.ActivityRef = ActivityRef;
    }

    /**
     * @return the Type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param Type the Type to set
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * @return the InterviewLocation
     */
    public String getInterviewLocation() {
        return InterviewLocation;
    }

    /**
     * @param InterviewLocation the InterviewLocation to set
     */
    public void setInterviewLocation(String InterviewLocation) {
        this.InterviewLocation = InterviewLocation;
    }

    /**
     * @return the InterviewDate
     */
    public String getInterviewDate() {
        return InterviewDate;
    }

    /**
     * @param InterviewDate the InterviewDate to set
     */
    public void setInterviewDate(String InterviewDate) {
        this.InterviewDate = InterviewDate;
    }

    /**
     * @return the BenefitType
     */
    public String getBenefitType() {
        return BenefitType;
    }

    /**
     * @param BenefitType the BenefitType to set
     */
    public void setBenefitType(String BenefitType) {
        this.BenefitType = BenefitType;
    }

    /**
     * @return the Disability
     */
    public String getDisability() {
        return Disability;
    }

    /**
     * @param Disability the Disability to set
     */
    public void setDisability(String Disability) {
        this.Disability = Disability;
    }

    /**
     * @return the AgeAtInterview
     */
    public String getAgeAtInterview() {
        return AgeAtInterview;
    }

    /**
     * @param AgeAtInterview the AgeAtInterview to set
     */
    public void setAgeAtInterview(String AgeAtInterview) {
        this.AgeAtInterview = AgeAtInterview;
    }

    /**
     * @return the AgeNow
     */
    public String getAgeNow() {
        return AgeNow;
    }

    /**
     * @param AgeNow the AgeNow to set
     */
    public void setAgeNow(String AgeNow) {
        this.AgeNow = AgeNow;
    }
    
    
}
