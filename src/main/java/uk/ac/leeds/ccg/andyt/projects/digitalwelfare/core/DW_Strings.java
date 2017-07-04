/*
 * Copyright (C) 2016 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core;

import java.util.ArrayList;

/**
 * A class for holding all Strings used in the DigitalWelfare project. It is
 * intended that there is only one instance of this class which is accessed via
 * the common instance of DW_Environment.
 *
 * @author geoagdt
 */
public class DW_Strings {

    /**
     * Short code for Underscore.
     */
    public final String sUnderscore = "_";
    
    /**
     * Short code for Comma.
     */
    public String sComma = ",";
    
    /**
     * Short code for sCommaSpace.
     */
    public String sCommaSpace = ", ";
    
    /**
     * Short code for All.
     */
    public final String sA = "A";

//    /**
//     * Short code for All.
//     */
//    public final String sAll = "All";

    /**
     * Short code for Both.
     */
    public final String sB = "B";

    /**
     * Short code for Council.
     */
    public final String sCouncil = "C";
    //public final String sCouncil = "Council";

    /**
     * Short code for NotCheckedPreviousPostcode.
     */
    public final String sCheckedPreviousPostcodeNo = "CPPN";
    //public final String sCheckedPreviousPostcodeNo = "NotCheckedPreviousPostcode";

    /**
     * Short code for CheckedPreviousPostcode.
     */
    public final String sCheckedPreviousPostcode = "CPPY";
    //public final String sCheckedPreviousPostcode = "CheckedPreviousPostcode";

    /**
     * Short code for CheckedPreviousTenancyType.
     */
    public final String sCheckedPreviousTenancyType = "CPTTY";
    //public final String sCheckedPreviousTenancyType = "CheckedPreviousTenancyType";

    /**
     * Short code for NotCheckedPreviousTenancyType.
     */
    public final String sCheckedPreviousTenancyTypeNo = "CPTTN";
    //public final String sCheckedPreviousTenancyTypeNo = "NotCheckedPreviousTenancyType";

    /**
     * Short code for Council Tax Relief Benefit.
     */
    public final String sCTB = "CR";
    
    /**
     * Short code for GroupedYes.
     */
    public final String sGrouped = "GY";
    //public final String sGrouped = "Grouped";
    
    /**
     * Short code for GroupedNo.
     */
    public final String sGroupedNo = "GN";
    //public final String sGroupedNo = "Ungrouped";

    /**
     * Short code for Housing Benefit.
     */
    public final String sHB = "HB";
    
    /**
     * Short code for GroupedNo.
     */
    public final String sIncludeMonthlySinceApril2013 = "IMU";
    
    /**
     * Short code for Include6Monthly.
     */
    public final String sInclude6Monthly = "I6M";
    
    /**
     * Short code for IncludeMonthly.
     */
    public final String sIncludeMonthly = "IM";
    
    /**
     * Short code for IncludeYearly.
     */
    public final String sIncludeYearly = "IY";
    
    /**
     * Short code for IncludeAll.
     */
    public final String sIncludeAll = "IA";
    
    /**
     * Short code for Include3Monthly.
     */
    public final String sInclude3Monthly = "I3M";
    
    /**
     * Short code for Output Area.
     */
    public final String sOA = "OA";

    /**
     * Short code for Lower-layer Super Output Area.
     */
    public final String sLSOA = "LSOA";
    
    /**
     * Short code for PaymentTypeIn.
     */
    public final String sPaymentTypeIn = "PTI";
    
    /**
     * Short code for PaymentTypeOther.
     */
    public final String sPaymentTypeOther = "PTO";
    
    /**
     * Short code for PaymentTypeAll.
     */
    public final String sPaymentTypeAll = "PTA";
    
    /**
     * Short code for PaymentTypeSuspended.
     */
    public final String sPaymentTypeSuspended = "PTS";

    /**
     * Short code for PostcodeChanged.
     */
    public final String sPostcodeChanged = "PCY";
    //public final String sPostcodeChanged = "PostcodeChanged";

    /**
     * Short code for PostcodeChanges.
     */
    public final String sPostcodeChanges = "PCs";
    //public final String sPostcodeChanges = "PostcodeChanges";

    /**
     * Short code for PostcodeUnchanged.
     */
    public final String sPostcodeChangedNo = "PCN";
    //public final String sPostcodeChangedNo = "PostcodeUnchanged";

    /**
     * Short code for Registered Social Landlord.
     */
    public final String sRSL = "R";
    //public final String sRSL = "RSL";

    /**
     * Short code for StyleCommon.
     */
    public final String sStyleCommon = "SC";

    /**
     * Short code for StyleIndividual.
     */
    public final String sStyleIndividual = "SI";

    /**
     * Short code for Tenancy.
     */
    public final String sTenancy = "T";
    //public final String sTenancy = "Tenancy";

    /**
     * Short code for TenancyAndPostcodeChanges.
     */
    public final String sTenancyAndPostcodeChanges = "TAPC";
    //public final String sTenancyAndPostcodeChanges = "TenancyAndPostcodeChanges";

    /**
     * Short code for TenancyTypeTransition.
     */
    public final String sTenancyTypeTransition = "TTT";
    //public final String sTenancyTypeTransition = "TenancyTypeTransition";

    /**
     * Short code for TenancyTypeTransitionLineGraphs.
     */
    public final String sTenancyTypeTransitionLineGraphs = "TTTLG";
    //public final String sTenancyTypeTransitionLineGraphs = "TenancyTypeTransitionLineGraphs";

    /**
     * Short code for UnderOccupied.
     */
    public final String sU = "U";
    
    /**
     * Short code for WithOrWithoutPostcodeChange.
     */
    public final String sWithOrWithoutPostcodeChange = "MPC"; // Maybe Postcode Changed 
    
//    /**
//     * Short code for UnderOccupied.
//     */
//    public final String sUO = "UO";

    public DW_Strings() {
    }

    /**
     * For getting an {@code ArrayList<String>} of PaymentTypes.
     * @return 
     */
    public ArrayList<String> getPaymentTypes() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(sPaymentTypeAll);
        result.add(sPaymentTypeIn);
        result.add(sPaymentTypeSuspended);
        result.add(sPaymentTypeOther);
        //result.add("NotInPaymentNotSuspended");
        return result;
    }

}
