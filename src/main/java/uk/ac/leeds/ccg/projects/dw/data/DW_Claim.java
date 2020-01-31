package uk.ac.leeds.ccg.projects.dw.data;

import java.util.HashMap;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_Claim extends DW_Object {

    /**
     * The ClaimID of the claim.
     */
    SHBE_ClaimID ClaimID;

    /**
     * Keys are SHBE indexes, values are (at the index time): true if claim is
     * in SHBE; false otherwise.
     */
    public HashMap<Integer, Boolean> InSHBE;

    /**
     * Keys are SHBE indexes, values are (at the index time): false if SHBE
     * DRecord.getStatusOfHBClaimAtExtractDate() == 1; true otherwise.
     */
    public HashMap<Integer, Boolean> Suspended;

    /**
     * Keys are SHBE indexes, values are (at the index time): true if claim is
     * in UO; false otherwise.
     */
    public HashMap<Integer, Boolean> InUO;

    /**
     * Keys are SHBE indexes, values are (at the index time): null if there is
     * no data on Rent Arrears; the value of the RentArrears.
     */
    public HashMap<Integer, Double> RentArrears;

    /**
     * Keys are SHBE indexes, values are (at the index time): null if there is
     * no data; the value of the Weekly Additional Discretionary Payment.
     */
    public HashMap<Integer, Integer> DHP;

    /**
     * Keys are SHBE indexes, values are (at the index time): null if there is
     * no data; the value of the Weekly Eligible Rent Amount.
     */
    //public HashMap<Integer, Double> WERA;
    /**
     * Keys are SHBE indexes, values are (at the index time): null if there is
     * no data; the value of the Bedroom Tax amount as calculated as either 14%
     * or 25% of WERA.
     */
    public HashMap<Integer, Double> BT;

    /**
     * Keys are SHBE indexes, values are (at the index time): null if there is
     * no BT; true if BT is 14% of WERA. false if BT is 25% of WERA.
     */
    public HashMap<Integer, Boolean> Type14;

    /**
     * Keys are SHBE indexes, values are (at the index time): true if claim has
     * a child under 11 years old; false otherwise.
     */
    public HashMap<Integer, Boolean> ChildUnder11;

    /**
     * Keys are SHBE indexes, values are (at the index time): true if claim has
     * a child under 11 years old; false otherwise.
     */
    public HashMap<Integer, String> PostcodeFs;

    public DW_Claim(DW_Environment e, int logID, SHBE_ClaimID claimID) {
        super(e, logID);
        init(claimID);
    }
    
    public DW_Claim(DW_Environment e, SHBE_ClaimID claimID) {
        super(e);
        init(claimID);
    }
        
    private void init(SHBE_ClaimID claimID){
        this.ClaimID = claimID;
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
