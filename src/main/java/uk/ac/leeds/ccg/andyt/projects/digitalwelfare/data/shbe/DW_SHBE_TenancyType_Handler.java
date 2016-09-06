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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_TenancyType_Handler extends DW_Object {

    public final int iMinus999 = -999;
    public final int zero = 0;
    public final int i1 = 1;
    public final int i2 = 2;
    public final int i3 = 3;
    public final int i4 = 4;
    public final int i5 = 5;
    public final int i6 = 6;
    public final int i7 = 7;
    public final int i8 = 8;
    public final int i9 = 9;
    public final String sMinus999 = "-999";
    public final String s0 = "0";
    public final String s1 = "1";
    public final String s2 = "2";
    public final String s3 = "3";
    public final String s4 = "4";
    public final String s5 = "5";
    public final String s6 = "6";
    public final String s7 = "7";
    public final String s8 = "8";
    public final String s9 = "9";
    public final String sU = "U";
    public final String sUnderOccupied = "Under Occupied";
    public final String sall = "all";
    public final String space = " ";
    public final String sEmpty = "";
    public final String sCouncil = "Council";
    public final String sPrivateRegulated = "Private Regulated";
    public final String sPrivateDeregulated = "Private Deregulated";
    public final String sHousingAssociation = "Housing Association";
    public final String sCTBOnlyCasesWhereClaimantSetAsOwnerWithinCouncilTax = "CTB only cases, where Claimant or Liable Person is set as the owner within Council Tax";
    public final String sCTBOnlyCasesWhereClaimantNotSetAsOwnerWithinCouncilTax = "CTB only cases, where Claimant or Liable Person is not set as the owner within Council Tax";
    public final String sPrivateOther = "Private Other";
    public final String sCouncilTenantCasesHRAFlagNo = "Council Tenant cases where the HRA flag is set to 'No'";
    public final String sPrivateTenantHonHANonHomelessWithMealsDeduction = "Private Tenant, non-HA, non-Homeless cases where there is a Meals deduction";
    public final String sNoTenancy = "No Tenancy";
    public final String sUnknown = "Unknown";

    public DW_SHBE_TenancyType_Handler(DW_Environment env) {
        this.env = env;
    }
    
    public String getTenancyTypeName(String tenancyType) {
        String result;
        if (tenancyType.startsWith(s1)) {
            result = sCouncil;
        } else if (tenancyType.startsWith(s2)) {
            result = sPrivateRegulated;
        } else if (tenancyType.startsWith(s3)) {
            result = sPrivateDeregulated;
        } else if (tenancyType.startsWith(s4)) {
            result = sHousingAssociation;
        } else if (tenancyType.startsWith(s5)) {
            result = sCTBOnlyCasesWhereClaimantSetAsOwnerWithinCouncilTax;
        } else if (tenancyType.startsWith(s6)) {
            result = sPrivateOther;
        } else if (tenancyType.startsWith(s7)) {
            result = sCTBOnlyCasesWhereClaimantNotSetAsOwnerWithinCouncilTax;
        } else if (tenancyType.startsWith(s8)) {
            result = sCouncilTenantCasesHRAFlagNo;
        } else if (tenancyType.startsWith(s9)) {
            result = sPrivateTenantHonHANonHomelessWithMealsDeduction;
        } else if (tenancyType.startsWith(sMinus999)) {
            result = sNoTenancy;
        } else {
            result = tenancyType.replaceAll(sU, sEmpty);
        }
        if (tenancyType.endsWith(sU)) {
            result += space + sUnderOccupied;
        }
        return result;
    }

    public String getTenancyTypeName(int tenancyType) {
        switch (tenancyType) {
            case 1:
                //return "Council Tenant HRA cases";
                return sCouncil;
            case 2:
                //return "Private Tenant Regulated, non-Housing Association cases";
                return sPrivateRegulated;
            case 3:
                //return "Private Tenant Deregulated, non-Housing Association cases";
                return sPrivateDeregulated;
            case 4:
                //return "Private Tenant Housing Association cases";
                return sHousingAssociation;
            case 5:
                return sCTBOnlyCasesWhereClaimantSetAsOwnerWithinCouncilTax;
            case 6:
                //return "Other Private Tenant cases";
                return sPrivateOther;
            case 7:
                return sCTBOnlyCasesWhereClaimantNotSetAsOwnerWithinCouncilTax;
            case 8:
                return sCouncilTenantCasesHRAFlagNo;
            case 9:
                return sPrivateTenantHonHANonHomelessWithMealsDeduction;
            case -999:
                return sNoTenancy;
            default:
                return sUnknown;
        }
    }

    /**
     * For storing an ArrayList of TenancyTypeUnregulated for convenience.
     */
    protected ArrayList<String> TenancyTypeUnregulated;

    /**
     * For returning an ArrayList of TenancyTypeUnregulated for convenience.
     *
     * @return
     */
    public ArrayList<String> getTenancyTypeUnregulated() {
        if (TenancyTypeUnregulated == null) {
            TenancyTypeUnregulated = new ArrayList<String>();
            TenancyTypeUnregulated.add(s3);
            TenancyTypeUnregulated.add(s6);
        }
        return TenancyTypeUnregulated;
    }

    /**
     * For storing an ArrayList of TenancyTypeRegulated for convenience.
     */
    protected ArrayList<String> TenancyTypeRegulated;

    /**
     * For storing an ArrayList of TenancyTypeRegulatedUO for convenience.
     */
    protected ArrayList<String> TenancyTypeRegulatedUO;

    /**
     * For returning an ArrayList of TenancyTypeRegulated for convenience.
     *
     * @return
     */
    public ArrayList<String> getTenancyTypeRegulated(boolean doUnderOccupiedData) {
        if (doUnderOccupiedData) {
            if (TenancyTypeRegulatedUO == null) {
                TenancyTypeRegulatedUO = new ArrayList<String>();
                TenancyTypeRegulatedUO.add(s1);
                TenancyTypeRegulatedUO.add(s2);
                TenancyTypeRegulatedUO.add(s4);
                TenancyTypeRegulatedUO.add(s1 + sU);
                //TenancyTypeRegulatedUO.add(s2 + sU);
                TenancyTypeRegulatedUO.add(s4 + sU);
            }
            return TenancyTypeRegulatedUO;
        } else {
            if (TenancyTypeRegulated == null) {
                TenancyTypeRegulated = new ArrayList<String>();
                TenancyTypeRegulated.add(s1);
                TenancyTypeRegulated.add(s2);
                TenancyTypeRegulated.add(s4);
            }
            return TenancyTypeRegulated;
        }
    }

    public ArrayList<String> getTenancyTypeAll() {
        ArrayList<String> result;
        result = new ArrayList<String>();
        //result.add(s0);
        result.add(s1);
        result.add(s2);
        result.add(s3);
        result.add(s4);
        result.add(s5);
        result.add(s6);
        result.add(s7);
        result.add(s8);
        result.add(s9);
        return result;
    }

    public ArrayList<String> getTenancyTypeAll(boolean doUnderOccupiedData) {
        ArrayList<String> result;
        result = getTenancyTypeAll();
        if (doUnderOccupiedData) {
            //result.add(s0 + sU);
            result.add(s1 + sU);
            result.add(s2 + sU);
            result.add(s3 + sU);
            result.add(s4 + sU);
            result.add(s5 + sU);
            result.add(s6 + sU);
            result.add(s7 + sU);
            result.add(s8 + sU);
            result.add(s9 + sU);
        }
        return result;
    }

    public Object[] getTenancyTypeGroups() {
        Object[] result;
        result = new Object[4];
        Boolean underOccupied;
        HashMap<Boolean, TreeMap<String, ArrayList<String>>> tenancyTypeGroups;
        tenancyTypeGroups = new HashMap<Boolean, TreeMap<String, ArrayList<String>>>();
        HashMap<Boolean, ArrayList<String>> tenancyTypesGrouped;
        tenancyTypesGrouped = new HashMap<Boolean, ArrayList<String>>();
        TreeMap<String, ArrayList<String>> ttgs;
        ArrayList<String> ttg;
        ArrayList<String> all;
        ArrayList<String> regulated;
        ArrayList<String> unregulated;
        HashMap<Boolean, ArrayList<String>> regulatedGroups;
        regulatedGroups = new HashMap<Boolean, ArrayList<String>>();
        HashMap<Boolean, ArrayList<String>> unregulatedGroups;
        unregulatedGroups = new HashMap<Boolean, ArrayList<String>>();
        ArrayList<String> rg;
        ArrayList<String> ug;
        underOccupied = false;
        ttgs = new TreeMap<String, ArrayList<String>>();
        all = getTenancyTypeAll(underOccupied);
        ttgs.put(sall, all);
        regulated = getTenancyTypeRegulated(underOccupied);
        ttgs.put("regulated", regulated);
        unregulated = getTenancyTypeUnregulated();
        ttgs.put("unregulated", unregulated);
        tenancyTypeGroups.put(underOccupied, ttgs);
        ttg = new ArrayList<String>();
        ttg.add("Regulated");
        ttg.add("Unregulated");
        ttg.add("Ungrouped");
        ttg.add(sMinus999);
        tenancyTypesGrouped.put(underOccupied, ttg);
        rg = getTenancyTypeRegulated(underOccupied);
        regulatedGroups.put(underOccupied, rg);
        ug = getTenancyTypeUnregulated();
        unregulatedGroups.put(underOccupied, ug);
        underOccupied = true;
        ttgs = new TreeMap<String, ArrayList<String>>();
        all = getTenancyTypeAll(underOccupied);
        ttgs.put(sall, all);
        regulated = getTenancyTypeRegulated(underOccupied);
        ttgs.put("regulated", regulated);
        unregulated = getTenancyTypeUnregulated();
        ttgs.put("unregulated", unregulated);
        tenancyTypeGroups.put(underOccupied, ttgs);
        ttg = new ArrayList<String>();
        ttg.add("Regulated");
        ttg.add("RegulatedUO");
        ttg.add("Unregulated");
        ttg.add("UnregulatedUO");
        ttg.add("Ungrouped");
        ttg.add("UngroupedUO");
        ttg.add(sMinus999);
        tenancyTypesGrouped.put(underOccupied, ttg);
        rg = getTenancyTypeRegulated(underOccupied);
        regulatedGroups.put(underOccupied, rg);
        ug = getTenancyTypeUnregulated();
        unregulatedGroups.put(underOccupied, ug);
        result[0] = tenancyTypeGroups;
        result[1] = tenancyTypesGrouped;
        result[2] = regulatedGroups;
        result[3] = unregulatedGroups;
        return result;
    }

}
