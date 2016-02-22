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

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_TenancyType_Handler {

    public static final int iMinus999 = -999;
    public static final int zero = 0;
    public static final int i1 = 1;
    public static final int i2 = 2;
    public static final int i3 = 3;
    public static final int i4 = 4;
    public static final int i5 = 5;
    public static final int i6 = 6;
    public static final int i7 = 7;
    public static final int i8 = 8;
    public static final int i9 = 9;
    public static final String sMinus999 = "-999";
    public static final String s0 = "0";
    public static final String s1 = "1";
    public static final String s2 = "2";
    public static final String s3 = "3";
    public static final String s4 = "4";
    public static final String s5 = "5";
    public static final String s6 = "6";
    public static final String s7 = "7";
    public static final String s8 = "8";
    public static final String s9 = "9";
    public static final String sUO = "UO";
    public static final String sUnderOccupied = "Under Occupied";
    public static final String sall = "all";
    public static final String space = " ";
    public static final String sEmpty = "";
    public static final String sCouncil = "Council";
    public static final String sPrivateRegulated = "Private Regulated";
    public static final String sPrivateDeregulated = "Private Deregulated";
    public static final String sHousingAssociation = "Housing Association";
    public static final String sCTBOnlyCasesWhereClaimantSetAsOwnerWithinCouncilTax = "CTB only cases, where Claimant or Liable Person is set as the owner within Council Tax";
    public static final String sCTBOnlyCasesWhereClaimantNotSetAsOwnerWithinCouncilTax = "CTB only cases, where Claimant or Liable Person is not set as the owner within Council Tax";
    public static final String sPrivateOther = "Private Other";
    public static final String sCouncilTenantCasesHRAFlagNo = "Council Tenant cases where the HRA flag is set to 'No'";
    public static final String sPrivateTenantHonHANonHomelessWithMealsDeduction = "Private Tenant, non-HA, non-Homeless cases where there is a Meals deduction";
    public static final String sNoTenancy = "No Tenancy";
    public static final String sUnknown = "Unknown";
    
    
    public static String getTenancyTypeName(String tenancyType) {
        String result;
        if (tenancyType.startsWith(s1)) {
            result = sCouncil;
        } else {
            if (tenancyType.startsWith(s2)) {
                result = sPrivateRegulated;
            } else {
                if (tenancyType.startsWith(s3)) {
                    result = sPrivateDeregulated;
                } else {
                    if (tenancyType.startsWith(s4)) {
                        result = sHousingAssociation;
                    } else {
                        if (tenancyType.startsWith(s5)) {
                            result = sCTBOnlyCasesWhereClaimantSetAsOwnerWithinCouncilTax;
                        } else {
                            if (tenancyType.startsWith(s6)) {
                                result = sPrivateOther;
                            } else {
                                if (tenancyType.startsWith(s7)) {
                                    result = sCTBOnlyCasesWhereClaimantNotSetAsOwnerWithinCouncilTax;
                                } else {
                                    if (tenancyType.startsWith(s8)) {
                                        result = sCouncilTenantCasesHRAFlagNo;
                                    } else {
                                        if (tenancyType.startsWith(s9)) {
                                            result = sPrivateTenantHonHANonHomelessWithMealsDeduction;
                                        } else {
                                            if (tenancyType.startsWith(sMinus999)) {
                                                result = sNoTenancy;
                                            } else {
                                                result = tenancyType.replaceAll(sUO, sEmpty);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (tenancyType.endsWith(sUO)) {
            result += space + sUnderOccupied;
        }
        return result;
    }

    public static String getTenancyTypeName(int tenancyType) {
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

    public static ArrayList<String> getTenancyTypeUnregulated(boolean doUnderOccupiedData) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(s3);
        result.add(s6);
        return result;
    }

    public static ArrayList<String> getTenancyTypeRegulated(boolean doUnderOccupiedData) {
        ArrayList<String> result;
        result = new ArrayList<String>();
        result.add(s1);
        result.add(s2);
        result.add(s4);
        if (doUnderOccupiedData) {
            result.add(s1 + sUO);
            //result.add(s2 + sUO);
            result.add(s4 + sUO);
        }
        return result;
    }

    public static ArrayList<String> getTenancyTypeAll() {
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

    public static ArrayList<String> getTenancyTypeAll(boolean doUnderOccupiedData) {
        ArrayList<String> result;
        result = getTenancyTypeAll();
        if (doUnderOccupiedData) {
            //result.add(s0 + sUO);
            result.add(s1 + sUO);
            //result.add(s2 + sUO);
            //result.add(s3 + sUO);
            result.add(s4 + sUO);
            //result.add(s5 + sUO);
            //result.add(s6 + sUO);
            //result.add(s7 + sUO);
            //result.add(s8 + sUO);
            //result.add(s9 + sUO);
        }
        return result;
    }

    public static Object[] getTenancyTypeGroups() {
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
        unregulated = getTenancyTypeUnregulated(underOccupied);
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
        ug = getTenancyTypeUnregulated(underOccupied);
        unregulatedGroups.put(underOccupied, ug);
        underOccupied = true;
        ttgs = new TreeMap<String, ArrayList<String>>();
        all = getTenancyTypeAll(underOccupied);
        ttgs.put(sall, all);
        regulated = getTenancyTypeRegulated(underOccupied);
        ttgs.put("regulated", regulated);
        unregulated = getTenancyTypeUnregulated(underOccupied);
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
        ug = getTenancyTypeUnregulated(underOccupied);
        unregulatedGroups.put(underOccupied, ug);
        result[0] = tenancyTypeGroups;
        result[1] = tenancyTypesGrouped;
        result[2] = regulatedGroups;
        result[3] = unregulatedGroups;
        return result;
    }
    
    
    
    
}
