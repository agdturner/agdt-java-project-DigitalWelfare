/*
 * Copyright (C) 2017 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data;

import java.util.HashMap;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_ID;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public class DW_Claim extends DW_Object {
    
    /**
     * The ClaimID of the claim.
     */
    DW_ID ClaimID;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * true if claim is in SHBE;
     * false otherwise.
     */
    public HashMap<Integer, Boolean> InSHBE;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * false if SHBE DRecord.getStatusOfHBClaimAtExtractDate() == 1;
     * true otherwise.
     */
    public HashMap<Integer, Boolean> Suspended;

    /**
     * Keys are SHBE indexes, values are (at the index time):
     * true if claim is in UO;
     * false otherwise.
     */
    public HashMap<Integer, Boolean> InUO;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * null if there is no data on Rent Arrears;
     * the value of the RentArrears.
     */
    public HashMap<Integer, Double> RentArrears;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * null if there is no data;
     * the value of the Weekly Additional Discretionary Payment.
     */
    public HashMap<Integer, Integer> DHP;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * null if there is no data;
     * the value of the Weekly Eligible Rent Amount.
     */
    //public HashMap<Integer, Double> WERA;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * null if there is no data;
     * the value of the Bedroom Tax amount as calculated as either 14% or 25% of
     * WERA.
     */
    public HashMap<Integer, Double> BT;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * null if there is no BT;
     * true if BT is 14% of WERA.
     * false if BT is 25% of WERA.
     */
    public HashMap<Integer, Boolean> Type14;

    /**
     * Keys are SHBE indexes, values are (at the index time):
     * true if claim has a child under 11 years old;
     * false otherwise.
     */    
    public HashMap<Integer, Boolean> ChildUnder11;
    
    /**
     * Keys are SHBE indexes, values are (at the index time):
     * true if claim has a child under 11 years old;
     * false otherwise.
     */    
    public HashMap<Integer, String> PostcodeFs;
    
    public DW_Claim(
            DW_Environment env,
            DW_ID ClaimID){
        super(env);
        this.ClaimID = ClaimID;
        InSHBE = new HashMap<>();
        Suspended = new HashMap<>();
        InUO = new HashMap<>();
        RentArrears = new HashMap<>();
        DHP = new HashMap<>();
        BT = new HashMap<>();
        Type14 = new HashMap<>();
        ChildUnder11 = new HashMap<>();
        PostcodeFs = new HashMap<>();
    }
    
}
