package uk.ac.leeds.ccg.projects.dw.process.lcc;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.data.types.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.projects.dw.data.DW_Claim;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.data.DW_RentArrearsUO;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_ProcessorLCCRentArrears extends DW_ProcessorLCC {

    public DW_ProcessorLCCRentArrears(DW_Environment env) throws IOException, Exception {
        super(env);
    }

    public void run(boolean newData) throws Exception, Error {

        boolean doMonths;
        boolean doClaims;
        doMonths = true;
        doClaims = false;
        doClaims = true;

        Path f = Paths.get(files.getGeneratedLCCDir().toString(),
                "RentArrearsAll" + files.getDefaultBinaryFileExtension());
        DW_RentArrearsUO raUO;
        if (newData) {
            raUO = new DW_RentArrearsUO(env);
            Generic_IO.writeObject(raUO, f);
        } else if (Files.exists(f)) {
            raUO = (DW_RentArrearsUO) Generic_IO.readObject(f);
            raUO.SHBE_Handler = env.getShbeData();
            raUO.UO_Data = env.getUoData();
// The following code was an attempt to automatically detect if a reload was 
// necessary and simplify things a bit, but I didn't quite get it to work. The
// idea was to count the number of files and compare this with the size of the 
// respective collection.
//            int nUOSets = DW_RentArrearsUO.ClaimData.entrySet().iterator().next().getValue().InUO.size();
//            int nUOFiles = files.getInputUnderOccupiedDir().listFiles().length / 2;
//            if (nUOSets < nUOFiles) {
//                DW_RentArrearsUO = new DW_RentArrearsUO(env);
//                env.ge.io.writeObject(DW_RentArrearsUO, f);
//            }
        } else {
            raUO = new DW_RentArrearsUO(env);
            Generic_IO.writeObject(raUO, f);
        }

        Map<UKP_YM3, SHBE_Records> allSHBE = shbeData.getData();

        DecimalFormat df = new DecimalFormat("0.00");

        HashMap<SHBE_ClaimID, DW_Claim> ClaimData;
        ClaimData = raUO.ClaimData;

        int AllSHBECount;
        int AllSHBESuspendedCount;
        double AllBTTotal;
        int AllBTCount;
        double AllBT14Total;
        int AllBT14Count;
        double AllBT25Total;
        int AllBT25Count;
        double AllRATotal;
        int AllRACount;
        int AllRANonZeroCount;
        double AllDHPTotal;
        int AllDHPCount;
        AllSHBECount = 0;
        AllSHBESuspendedCount = 0;
        AllBTTotal = 0;
        AllBTCount = 0;
        AllBT14Total = 0;
        AllBT14Count = 0;
        AllBT25Total = 0;
        AllBT25Count = 0;
        AllDHPTotal = 0;
        AllDHPCount = 0;
        int counter;
        int SHBECount;
        int SHBECountPriorToApril2013;
        int SHBECountSinceApril2013;
        int SHBESuspendedCount;
        int SHBEPriorToFirstUOCount;
        int SHBESinceFirstUOCount;
        int SHBEPriorToFirstUOSuspendedCount;
        int SHBESinceFirstUOSuspendedCount;
        double BTTotal;
        double BT14Total;
        double BT25Total;
        int BTCount;
        int BT14Count;
        int BT25Count;
        double DHPTotal;
        int DHPCount;
        counter = 0;
        SHBECount = 0;
        SHBECountPriorToApril2013 = 0;
        SHBECountSinceApril2013 = 0;
        SHBESuspendedCount = 0;
        SHBEPriorToFirstUOCount = 0;
        SHBESinceFirstUOCount = 0;
        SHBEPriorToFirstUOSuspendedCount = 0;
        SHBESinceFirstUOSuspendedCount = 0;
        BTTotal = 0;
        BT14Total = 0;
        BT25Total = 0;
        BTCount = 0;
        BT14Count = 0;
        BT25Count = 0;
        DHPTotal = 0;
        DHPCount = 0;

        SHBE_ClaimID ClaimID;
        String ClaimRef;
        DW_Claim DW_Claim;
        Iterator<Integer> ite;
        Iterator<SHBE_ClaimID> ite2;
        Integer i;
        Double BT;
        Double DHP;
        Boolean Suspended;

        PrintWriter pw0 = null;
        PrintWriter pw2 = null;
            String d = this.files.getOutputLCCDir().toString();
        if (doClaims) {
            pw0 = Generic_IO.getPrintWriter(Paths.get(d, "RentArrearsClaimsAll.csv"), false);
            pw2 = Generic_IO.getPrintWriter(Paths.get(d, "RentArrearsSummary.csv"), false);
        }
        PrintWriter pw1 = null;
        if (doMonths) {
            pw1 = Generic_IO.getPrintWriter(Paths.get(d, "RentArrearsMonths.csv"), false);
        }

        // Rent Arrears Difference variables
        Double StartRentArrears;

        double MaxRentArrears = 0;
        Integer IndexOfMaxRentArrears;

        Double EndRentArrears;
        double AllMaxRentArrears = 0;
        Integer IndexOfFirstSHBE;
        Integer IndexOfFirstDHP;
        Integer IndexOfFirstBT;
        Integer IndexOfFirstBT14;
        Integer IndexOfFirstBT25;
        Integer IndexOfLastSHBE;
        Integer IndexOfLastDHP;
        Integer IndexOfLastBT;
        Integer IndexOfLastBT14;
        Integer IndexOfLastBT25;
        Integer IndexOfFirstNonZeroArrears;
        Integer IndexOfLastNonZeroArrears;
        Double ValueOfLastNonZeroArrears;
        Integer IndexOfMaximumRAD;
        Integer IndexOfMinimumRAD;

        double RAD;
        double RADTotal;
        double RADPositiveTotal;
        double RADNegativeTotal;
        int RADCount;
        int RADPositiveCount;
        int RADNegativeCount;
        Double RA0;
        Double RA1;
        double RATotal;
        int RACount;
        int RANonZeroCount;
        double RADDHP; // DHP Aggregate Total
        int RADDHPCount; // DHP Aggregate Count
        double RADNoDHP; // non-DHP Aggregate Total
        int RADNoDHPCount; // non-DHP Aggregate Count
        double RADPositiveDHP;
        int RADPositiveDHPCount;
        int RADNegativeDHPCount;
        double RADNegativeDHP;
        double RADPositiveNoDHP;
        int RADPositiveNoDHPCount;
        int RADNegativeNoDHPCount;
        double RADNegativeNoDHP;
        RADCount = 0;
        RADPositiveCount = 0;
        RADNegativeCount = 0;
        RATotal = 0;
        RACount = 0;
        RANonZeroCount = 0;
        RADDHP = 0;
        RADDHPCount = 0;
        RADPositiveDHP = 0;
        RADPositiveDHPCount = 0;
        RADNegativeDHPCount = 0;
        RADNegativeDHP = 0;
        RADNoDHP = 0;
        RADNoDHPCount = 0;
        RADPositiveNoDHP = 0;
        RADPositiveNoDHPCount = 0;
        RADNegativeNoDHPCount = 0;
        RADNegativeNoDHP = 0;

        double AllRADDHP; // All DHP Aggregate Total
        double AllRADPositiveDHP;
        double AllRADNegativeDHP;
        int AllRADDHPCount; // All DHP Aggregate Count
        int AllRADPositiveDHPCount;
        int AllRADNegativeDHPCount;
        double AllRADNoDHP; // All non-DHP Aggregate Total
        double AllRADPositiveNoDHP;
        double AllRADNegativeNoDHP;
        int AllRADNoDHPCount; // All non-DHP Aggregate Count
        int AllRADPositiveNoDHPCount;
        int AllRADNegativeNoDHPCount;
        double RADMax;
        double RADMin;

        AllRADDHP = 0;
        AllRADPositiveDHP = 0;
        AllRADNegativeDHP = 0;
        AllRADDHPCount = 0;
        AllRADPositiveDHPCount = 0;
        AllRADNegativeDHPCount = 0;
        AllRADNoDHP = 0;
        AllRADPositiveNoDHP = 0;
        AllRADNegativeNoDHP = 0;
        AllRADNoDHPCount = 0;
        AllRADPositiveNoDHPCount = 0;
        AllRADNegativeNoDHPCount = 0;
        UKP_YM3 YM3;
        SHBE_Records SHBE_Records;
        SHBE_Record Record;
        SHBE_D_Record DRecord;
        Boolean ChildUnder11;
        String PostcodeF0;
        String PostcodeF1;
        AllRATotal = 0;
        AllRACount = 0;
        AllRANonZeroCount = 0;

        ArrayList<Integer> include;
        //include = env.getShbeData().getIncludeMonthlyUO();
        include = env.getShbeData().getIncludeAll();
        int i0 = include.iterator().next();

        String line;

        if (doClaims) {

            env.ge.log("Iterate over each claim first then each time period.", true);
            line = "Number, ClaimID, ClaimRef, "
                    + "FirstSeenSHBEDate, LastSeenSHBEDate, "
                    + "SHBECount, "
                    + "SHBECountPriorToApril2013, SHBECountSinceApril2013, "
                    + "SHBESuspendedCount, "
                    + "SHBEPriorToFirstUOCount, SHBESinceFirstUOCount, "
                    + "SHBEPriorToFirstUOSuspendedCount, SHBESinceFirstUOSuspendedCount, "
                    + "PercentageSuspensionPriorToFirstUO, PercentageSuspensionSinceFirstUO, "
                    + "BTTotal, BTCount, BTAverage, BTFirstSeenDate, BTLastSeenDate, "
                    + "BT14Total, BT14Count, BT14Average, BT14FirstSeenDate, BT14LastSeenDate, "
                    + "BT25Total, BT25Count, BT25Average, BT25FirstSeenDate, BT25LastSeenDate, "
                    + "DHPTotal, DHPCount, DHPAverage, DHPFirstSeenDate, DHPLastSeenDate,"
                    + "RABTEntry, RABTEntryDate, RANonZeroFirstSeenDate, "
                    + "RATotal, RACount, RANonZeroCount, "
                    + "RAMaximum, RAMaximumDate, "
                    + "RALastSeen, RALastSeenDate, "
                    + "RANonZeroLastSeen, "
                    + "RADAverage, "
                    + "RADPositiveAverage, "
                    + "RADNegativeAverage, "
                    + "RADMaximum, RADMaximumDate, "
                    + "RADMinimum, RADMinimumDate, "
                    + "RADDHPAverage, "
                    + "RADPositiveDHPAverage, "
                    + "RADNegativeDHPAverage, "
                    + "RADNoDHPAverage, "
                    + "RADPositiveNoDHPAverage, "
                    + "RADNegativeNoDHPAverage, "
                    + "Disability, Passported/Standard Indicator, Gender of Claimant, "
                    + "Households with young children (under 11), "
                    + "Ethnicity of Claimant, "
                    + "PostcodeFirstSeenBTDate, PostcodeLastSeenSHBEDate, PostcodeChanged";
            env.ge.log(line, true);
            pw0.println(line);
            // Iterate over each claim
            ite2 = ClaimData.keySet().iterator();
            while (ite2.hasNext()) {

                AllMaxRentArrears = Math.max(AllMaxRentArrears, MaxRentArrears);
                StartRentArrears = null;
                MaxRentArrears = 0;
                IndexOfFirstSHBE = null;
                IndexOfFirstNonZeroArrears = null;
                IndexOfLastNonZeroArrears = null;
                ValueOfLastNonZeroArrears = null;
                IndexOfMaxRentArrears = null;
                IndexOfFirstDHP = null;
                IndexOfFirstBT = null;
                IndexOfFirstBT14 = null;
                IndexOfFirstBT25 = null;
                IndexOfLastSHBE = null;
                IndexOfLastDHP = null;
                IndexOfLastBT = null;
                IndexOfLastBT14 = null;
                IndexOfLastBT25 = null;
                EndRentArrears = null;
                IndexOfMaximumRAD = null;
                IndexOfMinimumRAD = null;
                AllSHBECount += SHBECount;
                AllSHBESuspendedCount += SHBESuspendedCount;
                AllBTTotal += BTTotal;
                AllBT14Total += BT14Total;
                AllBT25Total += BT25Total;
                AllBTCount += BTCount;
                AllBT14Count += BT14Count;
                AllBT25Count += BT25Count;
                AllDHPTotal += DHPTotal;
                AllDHPCount += DHPCount;
                AllRATotal += RATotal;
                AllRACount += RACount;
                AllRANonZeroCount += RANonZeroCount;
                SHBECount = 0;
                SHBECountPriorToApril2013 = 0;
                SHBECountSinceApril2013 = 0;
                SHBESuspendedCount = 0;
                SHBEPriorToFirstUOCount = 0;
                SHBESinceFirstUOCount = 0;
                SHBEPriorToFirstUOSuspendedCount = 0;
                SHBESinceFirstUOSuspendedCount = 0;
                BTTotal = 0;
                BT14Total = 0;
                BT25Total = 0;
                BTCount = 0;
                BT14Count = 0;
                BT25Count = 0;
                DHPTotal = 0;
                DHPCount = 0;
                AllRADDHP += RADDHP;
                AllRADPositiveDHP += RADPositiveDHP;
                AllRADNegativeDHP += RADNegativeDHP;
                AllRADDHPCount += RADDHPCount;
                AllRADPositiveDHPCount += RADPositiveDHPCount;
                AllRADNegativeDHPCount += RADNegativeDHPCount;
                AllRADNoDHP += RADNoDHP;
                AllRADPositiveNoDHP += RADPositiveNoDHP;
                AllRADNegativeNoDHP += RADNegativeNoDHP;
                AllRADNoDHPCount += RADNoDHPCount;
                AllRADPositiveNoDHPCount += RADPositiveNoDHPCount;
                AllRADNegativeNoDHPCount += RADNegativeNoDHPCount;

                RADTotal = 0;
                RADPositiveTotal = 0;
                RADNegativeTotal = 0;

                RADMax = Double.NEGATIVE_INFINITY;
                RADMin = Double.POSITIVE_INFINITY;

                RADCount = 0;
                RADPositiveCount = 0;
                RADNegativeCount = 0;
                RATotal = 0;
                RACount = 0;
                RANonZeroCount = 0;
                RADDHP = 0;
                RADDHPCount = 0;
                RADPositiveDHP = 0;
                RADPositiveDHPCount = 0;
                RADNegativeDHPCount = 0;
                RADNegativeDHP = 0;
                RADNoDHP = 0;
                RADNoDHPCount = 0;
                RADPositiveNoDHP = 0;
                RADPositiveNoDHPCount = 0;
                RADNegativeNoDHPCount = 0;
                RADNegativeNoDHP = 0;

                ClaimID = ite2.next();

//            for (int j = 0; j < 17; j++) {
//                YM3 = shbeData.getYM3(shbeFilenames[j]);
//                env.ge.log("YM3 " + YM3, true);
//                SHBE_Records = shbeData.getRecords(YM3);
//                HashMap<SHBE_ClaimID, SHBE_Record> Records;
//                Records = SHBE_Records.getRecords(env.HOOME);
//                if (Records.keySet().contains(ClaimID)) {
//                    SHBECountPriorToApril2013++;
//                    SHBECount++;
//                }
//            }
                //if (ClaimID.getID() == 137367) {
                //    int debug = 1;
                ClaimRef = cid2c.get(ClaimID);
                DW_Claim = ClaimData.get(ClaimID);

                ite = include.iterator();
                // Iterate over each time period
                while (ite.hasNext()) {
                    i = ite.next();
                    Suspended = DW_Claim.Suspended.get(i);
                    boolean loop = false;
                    try {
                        loop = DW_Claim.InSHBE.get(i);
                    } catch (NullPointerException e) {
                        System.err.println("" + e.getMessage() + "");
                        System.err.println("i " + i);
                    }
                    if (loop) {
                        SHBECount++;
                        if (i < 17) {
                            SHBECountPriorToApril2013++;
                        } else {
                            SHBECountSinceApril2013++;
                        }
                        if (IndexOfFirstBT != null) {
                            SHBESinceFirstUOCount++;
                            if (Suspended != null) {
                                if (Suspended) {
                                    SHBESinceFirstUOSuspendedCount++;
                                }
                            }
                        } else {
                            SHBEPriorToFirstUOCount++;
                            if (Suspended != null) {
                                if (Suspended) {
                                    SHBEPriorToFirstUOSuspendedCount++;
                                }
                            }
                        }
                        if (IndexOfFirstSHBE == null) {
                            IndexOfFirstSHBE = i;
                        }
                        IndexOfLastSHBE = i;
//                } else {
//                    System.out.println("" + ClaimID + " not in SHBE " + shbeData.getYM3(shbeFilenames[i]));
                    }
                    if (Suspended != null) {
                        if (Suspended) {
                            SHBESuspendedCount++;
                        }
                    }
                    if (DW_Claim.BT.containsKey(i)) {
                        //BT = DW_Claim.BT.get(i) / 100.0d; //Get into pounds from pence
                        BT = DW_Claim.BT.get(i) * 4 / 100.0d; //Get into pounds from pence and monthly totals
                        BTTotal += BT;
                        //BTCount++;
                        BTCount += 4;
                        if (IndexOfFirstBT == null) {
                            IndexOfFirstBT = i;
                        }
                        IndexOfLastBT = i;
                        boolean isType14;
                        isType14 = DW_Claim.Type14.get(i);
                        if (isType14) {
                            BT14Total += BT;
                            BT14Count += 4;
                            if (IndexOfFirstBT14 == null) {
                                IndexOfFirstBT14 = i;
                            }
                            IndexOfLastBT14 = i;
                        } else {
                            BT25Total += BT;
                            BT25Count += 4;
                            if (IndexOfFirstBT25 == null) {
                                IndexOfFirstBT25 = i;
                            }
                            IndexOfLastBT25 = i;
                        }
                    }
                    if (DW_Claim.DHP.containsKey(i)) {
                        //DHP = DW_Claim.DHP.get(i) / 100.0d; //Get into pounds from pence
                        DHP = DW_Claim.DHP.get(i) * 4 / 100.0d; //Get into pounds from pence and monthly totals
                        if (DHP > 0) {
                            DHPTotal += DHP;
                            //DHPCount++;
                            DHPCount += 4;
                            if (IndexOfFirstDHP == null) {
                                IndexOfFirstDHP = i;
                                IndexOfLastDHP = i;
                            }
                            IndexOfLastDHP = i;
                        }
                    }
                    if (DW_Claim.RentArrears.containsKey(i)) {
                        RA1 = DW_Claim.RentArrears.get(i);
                        if (RA1 != null) {
                            RACount += 1;
                            if (RA1 > 0) {
                                RATotal += RA1;
                                RANonZeroCount += 1;
                                if (IndexOfFirstNonZeroArrears == null) {
                                    IndexOfFirstNonZeroArrears = i;
                                }
                                IndexOfLastNonZeroArrears = i;
                                ValueOfLastNonZeroArrears = RA1;
                            }
                            if (StartRentArrears == null) {
                                StartRentArrears = RA1;
                                MaxRentArrears = RA1;
                                IndexOfMaxRentArrears = i;
                            } else {
                                if (RA1 > MaxRentArrears) {
                                    MaxRentArrears = RA1;
                                    IndexOfMaxRentArrears = i;
                                }
                                EndRentArrears = RA1;
                            }
                        }
                        if (i > i0) {
                            if (DW_Claim.RentArrears.containsKey(i - 1)) {
                                RA0 = DW_Claim.RentArrears.get(i - 1);
                                if (RA0 != null && RA1 != null) {
                                    RAD = RA1 - RA0;
                                    RADTotal += RAD;
                                    RADCount++;
                                    if (RAD > 0) {
                                        RADPositiveTotal += RAD;
                                        RADPositiveCount++;
                                    } else {
                                        RADNegativeTotal += RAD;
                                        RADNegativeCount++;
                                    }
                                    if (RAD > RADMax) {
                                        IndexOfMaximumRAD = i;
                                        RADMax = RAD;
                                    }
                                    if (RAD < RADMin) {
                                        IndexOfMinimumRAD = i;
                                        RADMin = RAD;
                                    }
                                    if (DW_Claim.DHP.containsKey(i)) {
                                        if (DW_Claim.DHP.get(i) > 0) {
                                            RADDHP += RAD;
                                            RADDHPCount++;
                                            if (RAD > 0) {
                                                RADPositiveDHP += RAD;
                                                RADPositiveDHPCount++;
                                            } else {
                                                RADNegativeDHPCount++;
                                                RADNegativeDHP += RAD;
                                            }
                                        } else {
                                            RADNoDHP += RAD;
                                            RADNoDHPCount++;
                                            if (RAD > 0) {
                                                RADPositiveNoDHP += RAD;
                                                RADPositiveNoDHPCount++;
                                            } else {
                                                RADNegativeNoDHPCount++;
                                                RADNegativeNoDHP += RAD;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                counter++;
                line = "" + counter
                        + ", " + ClaimID
                        + ", " + ClaimRef;
                // SHBE
                if (IndexOfFirstSHBE != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstSHBE])
                            + ", " + shbeData.getYM3(shbeFilenames[IndexOfLastSHBE]);
                } else {
                    /**
                     * This only happens if someone is in the UO data, but is
                     * never in a SHBE extract and that should only happen very
                     * rarely if at all.
                     */
                    line += ", null";
                    line += ", null";
                }
                line += ", " + SHBECount;
                line += ", " + SHBECountPriorToApril2013;
                line += ", " + SHBECountSinceApril2013;
                line += ", " + SHBESuspendedCount;

                line += ", " + SHBEPriorToFirstUOCount;
                line += ", " + SHBESinceFirstUOCount;

                line += ", " + SHBEPriorToFirstUOSuspendedCount;
                line += ", " + SHBESinceFirstUOSuspendedCount;
                if (SHBEPriorToFirstUOCount > 0) {
                    line += ", " + SHBEPriorToFirstUOSuspendedCount * 100 / (double) SHBEPriorToFirstUOCount;
                } else {
                    line += ", 0";
                }
                if (SHBESinceFirstUOCount > 0) {
                    line += ", " + SHBESinceFirstUOSuspendedCount * 100 / (double) SHBESinceFirstUOCount;
                } else {
                    line += ", 0";
                }

                // BT
                // All
                line += ", " + df.format(BTTotal);
                line += ", " + BTCount;
                if (BTCount > 0) {
                    line += ", " + df.format(BTTotal / BTCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (IndexOfFirstBT != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstBT]);
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfLastBT]);
                } else {
                    line += ", null";
                    line += ", null";
                }
                // 14%
                line += ", " + df.format(BT14Total);
                line += ", " + BT14Count;
                if (BT14Count > 0) {
                    line += ", " + df.format(BT14Total / BT14Count);
                } else {
                    line += ", " + df.format(0);
                }
                if (IndexOfFirstBT14 != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstBT14]);
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfLastBT14]);
                } else {
                    line += ", null";
                    line += ", null";
                }
                // 25%
                line += ", " + df.format(BT25Total);
                line += ", " + BT25Count;
                if (BT25Count > 0) {
                    line += ", " + df.format(BT25Total / BT25Count);
                } else {
                    line += ", " + df.format(0);
                }
                if (IndexOfFirstBT25 != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstBT25]);
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfLastBT25]);
                } else {
                    line += ", null";
                    line += ", null";
                }

                // DHP
                line += ", " + df.format(DHPTotal);
                line += ", " + DHPCount;
                if (DHPCount > 0) {
                    line += ", " + df.format(DHPTotal / DHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (IndexOfFirstDHP != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstDHP]);
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfLastDHP]);
                } else {
                    line += ", null";
                    line += ", null";
                }
                // Rent Arrears
                line += ", " + StartRentArrears;
                if (IndexOfFirstNonZeroArrears != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstBT]);
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfFirstNonZeroArrears]);
                } else {
                    line += ", null";
                    line += ", null";
                }
                line += ", " + RATotal;
                line += ", " + RACount;
                line += ", " + RANonZeroCount;
//                if (RACount > 0) {
//                    line += ", " + files.format(RATotal/(double)RACount);
//                } else {
//                    line += ", " + files.format(0);
//                }
                line += ", " + MaxRentArrears;
                if (IndexOfMaxRentArrears != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfMaxRentArrears]);
                } else {
                    line += ", null";
                }
                line += ", " + EndRentArrears;
                if (IndexOfLastNonZeroArrears != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfLastNonZeroArrears]);
                } else {
                    line += ", null";
                }
                if (ValueOfLastNonZeroArrears != null) {
                    line += ", " + ValueOfLastNonZeroArrears;
                } else {
                    line += ", null";
                }
                // Rent Arrears Differences
                if (RADCount > 0) {
                    line += ", " + df.format(RADTotal / (double) RADCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADPositiveCount > 0) {
                    line += ", " + df.format(RADPositiveTotal / (double) RADPositiveCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADNegativeCount > 0) {
                    line += ", " + df.format(RADNegativeTotal / (double) RADNegativeCount);
                } else {
                    line += ", " + df.format(0);
                }
                line += ", " + RADMax;
                if (IndexOfMaximumRAD != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfMaximumRAD]);
                } else {
                    line += ", null";
                }
                line += ", " + RADMin;
                if (IndexOfMinimumRAD != null) {
                    line += ", " + shbeData.getYM3(shbeFilenames[IndexOfMinimumRAD]);
                } else {
                    line += ", null";
                }
                if (RADDHPCount > 0) {
                    line += ", " + df.format(RADDHP / (double) RADDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADPositiveDHPCount > 0) {
                    line += ", " + df.format(RADPositiveDHP / (double) RADPositiveDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADNegativeDHPCount > 0) {
                    line += ", " + df.format(RADNegativeDHP / (double) RADNegativeDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADNoDHPCount > 0) {
                    line += ", " + df.format(RADNoDHP / (double) RADNoDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADPositiveNoDHPCount > 0) {
                    line += ", " + df.format(RADPositiveNoDHP / (double) RADPositiveNoDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADNegativeNoDHPCount > 0) {
                    line += ", " + df.format(RADNegativeNoDHP / (double) RADNegativeNoDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (IndexOfFirstBT != null) {
                    YM3 = shbeData.getYM3(shbeFilenames[IndexOfFirstBT]);
                    //System.out.println("IndexOfFirstBT " + IndexOfFirstBT);
                    //System.out.println("YM3 " + YM3);
                    //SHBE_Records = AllSHBE.get(YM3);
                    SHBE_Records = shbeData.getRecords(YM3, env.HOOME);
                    if (SHBE_Records == null) {
                        env.ge.log("AllSHBE.get(YM3) is null!");
                    }
                    Map<SHBE_ClaimID, SHBE_Record> recs 
                            = SHBE_Records.getRecords(env.HOOME);
                    if (recs == null) {
                        env.ge.log("AllSHBE.get(YM3).getRecords(true) is null!");
                    }
                    Record = recs.get(ClaimID);
                    if (Record != null) {
                        DRecord = Record.getDRecord();
                        if (shbeData.getDisability(DRecord)) {
                            line += ", 1";
                        } else {
                            line += ", 0";
                        }
                        line += ", " + DRecord.getPassportedStandardIndicator();
                        line += ", " + DRecord.getClaimantsGender();
                        ChildUnder11 = DW_Claim.ChildUnder11.get(IndexOfFirstBT);
                        if (ChildUnder11 != null) {
                            if (ChildUnder11) {
                                line += ", 1";
                            } else {
                                line += ", 0";
                            }
                        } else {
                            line += ", 0";
                        }
                        line += ", " + DRecord.getClaimantsEthnicGroup();
                    }
                    PostcodeF0 = DW_Claim.PostcodeFs.get(IndexOfFirstBT);
                    line += ", " + PostcodeF0;
                    PostcodeF1 = DW_Claim.PostcodeFs.get(IndexOfLastSHBE);
                    line += ", " + PostcodeF1;
                    if (PostcodeF0.equalsIgnoreCase(PostcodeF1)) {
                        line += ", 0";
                    } else {
                        line += ", 1";
                    }
                }
                if (counter % 500 == 0 || counter < 10) {
                    env.ge.log(line, true);
                }
                pw0.println(line);
            }
            pw0.close();

            env.ge.log("After " + counter + " out of " + ClaimData.size(), true);
            line = "BTTotal, BTCount, BTAverage, "
                    + "BT14Total, BT14Count, BT14Average, "
                    + "BT25Total, BT25Count, BT25Average, "
                    + "DHPTotal, DHPCount, DHPAverage, "
                    + "RADDHPAverage, RADPositiveDHPAverage, RADNegativeDHPAverage,"
                    + "RADNoDHPAverage, RADPositiveNoDHPAverage, RADNegativeNoDHPAverage, "
                    + "RentArrearsMaximum, "
                    + "RentArrearsTotal, RentArrearsCount, RentArrearsNonZeroCount";
            env.ge.log(line, true);
            pw2.println(line);
            // BT
            // All
            line = "" + df.format(AllBTTotal);
            line += ", " + AllBTCount;
            if (AllBTCount > 0) {
                line += ", " + df.format(AllBTTotal / AllBTCount);
            } else {
                line += ", ";
            }
            // 14%
            line += ", " + df.format(AllBT14Total);
            line += ", " + AllBT14Count;
            if (AllBT14Count > 0) {
                line += ", " + df.format(AllBT14Total / AllBT14Count);
            } else {
                line += ", ";
            }
            // 25%
            line += "," + df.format(AllBT25Total);
            line += ", " + AllBT25Count;
            if (AllBT25Count > 0) {
                line += ", " + df.format(AllBT25Total / AllBT25Count);
            } else {
                line += ", ";
            }
            line += ", " + df.format(AllDHPTotal);
            line += ", " + AllDHPCount;
            line += ", " + df.format(AllDHPTotal / AllDHPCount);
            line += ", " + df.format(AllRADDHP / (double) AllRADDHPCount);
            if (AllRADPositiveDHPCount > 0) {
                line += ", " + df.format(AllRADPositiveDHP / (double) AllRADPositiveDHPCount);
            } else {
                line += ", 0";
            }
            if (AllRADNegativeDHPCount > 0) {
                line += ", " + df.format(AllRADNegativeDHP / (double) AllRADNegativeDHPCount);
            } else {
                line += ", 0";
            }
            line += ", " + df.format(AllRADNoDHP / (double) AllRADNoDHPCount);
            if (AllRADPositiveNoDHPCount > 0) {
                line += ", " + df.format(AllRADPositiveNoDHP / (double) AllRADPositiveNoDHPCount);
            } else {
                line += ", 0";
            }
            if (AllRADNegativeDHPCount > 0) {
                line += ", " + df.format(AllRADNegativeNoDHP / (double) AllRADNegativeNoDHPCount);
            } else {
                line += ", 0";
            }
            line += ", " + AllMaxRentArrears;
            line += ", " + AllRATotal;
            line += ", " + AllRACount;
            line += ", " + AllRANonZeroCount;
            env.ge.log(line, true);
            pw2.println(line);
            counter = 0;
            pw2.close();
        }

        if (doMonths) {
            env.ge.log("Iterate over each time period first then each claim.", true);
            line = "YM3, "
                    + "BTTotal, BTCount, BTAverage, "
                    + "BT14Total, BT14Count, BT14Average, "
                    + "BT25Total, BT25Count, BT25Average, "
                    + "DHPTotal, DHPCount, DHPAverage, "
                    + "RADDHPAverage, RADPositiveDHPAverage, RADNegativeDHPAverage,"
                    + "RADNoDHPAverage, RADPositiveNoDHPAverage, RADNegativeNoDHPAverage, "
                    + "RentArrearsMaximum, RentArrearsTotal, RentArrearsCount, RentArrearsNonZeroCount";
            env.ge.log(line, true);
            pw1.println(line);
            // Iterate over each time period
            ite = include.iterator();
            while (ite.hasNext()) {
                i = ite.next();

                MaxRentArrears = 0;
                BTTotal = 0;
                BT14Total = 0;
                BT25Total = 0;
                BTCount = 0;
                BT14Count = 0;
                BT25Count = 0;
                DHPTotal = 0;
                DHPCount = 0;
                RADDHP = 0;
                RADDHPCount = 0;
                RADPositiveDHP = 0;
                RADPositiveDHPCount = 0;
                RADNegativeDHP = 0;
                RADNegativeDHPCount = 0;
                RADNoDHP = 0;
                RADNoDHPCount = 0;
                RADPositiveNoDHP = 0;
                RADPositiveNoDHPCount = 0;
                RADNegativeNoDHP = 0;
                RADNegativeNoDHPCount = 0;
                RATotal = 0;
                RACount = 0;
                RANonZeroCount = 0;

                // Iterate over each claim
                ite2 = ClaimData.keySet().iterator();
                while (ite2.hasNext()) {
                    ClaimID = ite2.next();
                    DW_Claim = ClaimData.get(ClaimID);
                    if (DW_Claim.BT.containsKey(i)) {
                        BT = DW_Claim.BT.get(i) * 4 / 100.0d; //Get into pounds from pence
                        BTTotal += BT;
                        BTCount += 4;
                        boolean isType14;
                        isType14 = DW_Claim.Type14.get(i);
                        if (isType14) {
                            BT14Total += BT;
                            BT14Count += 4;
                        } else {
                            BT25Total += BT;
                            BT25Count += 4;
                        }
                    }
                    if (DW_Claim.DHP.containsKey(i)) {
                        DHP = DW_Claim.DHP.get(i) / 100.0d; //Get into pounds from pence
                        if (DHP > 0) {
                            DHPTotal += DHP;
                            DHPCount++;
                        }
                    }
                    if (DW_Claim.RentArrears.containsKey(i)) {
                        RA1 = DW_Claim.RentArrears.get(i);
                        if (RA1 != null) {
                            MaxRentArrears = Math.max(MaxRentArrears, RA1);
                            RATotal += RA1;
                            RACount += 1;
                            if (RA1 > 0) {
                                RANonZeroCount += 1;
                            }
                        }
                        if (i > i0) {
                            if (DW_Claim.RentArrears.containsKey(i - 1)) {
                                RA0 = DW_Claim.RentArrears.get(i - 1);
                                if (RA0 != null && RA1 != null) {
                                    RAD = RA1 - RA0;
                                    if (DW_Claim.DHP.containsKey(i)) {
                                        if (DW_Claim.DHP.get(i) > 0) {
                                            RADDHP += RAD;
                                            RADDHPCount++;
                                            if (RAD > 0) {
                                                RADPositiveDHP += RAD;
                                                RADPositiveDHPCount++;
                                            } else {
                                                RADNegativeDHPCount++;
                                                RADNegativeDHP += RAD;
                                            }
                                        } else {
                                            RADNoDHP += RAD;
                                            RADNoDHPCount++;
                                            if (RAD > 0) {
                                                RADPositiveNoDHP += RAD;
                                                RADPositiveNoDHPCount++;
                                            } else {
                                                RADNegativeNoDHP += RAD;
                                                RADNegativeNoDHPCount++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                counter++;
                line = shbeData.getYM3(shbeFilenames[i]).toString();
                line += ", " + df.format(BTTotal);
                line += ", " + BTCount;
                if (BTCount > 0) {
                    line += ", " + df.format(BTTotal / BTCount);
                } else {
                    line += ", " + df.format(0);
                }
                line += ", " + df.format(BT14Total);
                line += ", " + BT14Count;
                if (BT14Count > 0) {
                    line += ", " + df.format(BT14Total / BT14Count);
                } else {
                    line += ", " + df.format(0);
                }
                line += ", " + df.format(BT25Total);
                line += ", " + BT25Count;
                if (BT25Count > 0) {
                    line += ", " + df.format(BT25Total / BT25Count);
                } else {
                    line += ", " + df.format(0);
                }
                line += ", " + df.format(DHPTotal);
                line += ", " + DHPCount;
                if (DHPCount > 0) {
                    line += ", " + df.format(DHPTotal / DHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADDHPCount > 0) {
                    line += ", " + df.format(RADDHP / (double) RADDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADPositiveDHPCount > 0) {
                    line += ", " + df.format(RADPositiveDHP / (double) RADPositiveDHPCount);
                } else {
                    line += ", 0";
                }
                if (RADNegativeDHPCount > 0) {
                    line += ", " + df.format(RADNegativeDHP / (double) RADNegativeDHPCount);
                } else {
                    line += ", 0";
                }
                if (RADNoDHPCount > 0) {
                    line += ", " + df.format(RADNoDHP / (double) RADNoDHPCount);
                } else {
                    line += ", " + df.format(0);
                }
                if (RADPositiveNoDHPCount > 0) {
                    line += ", " + df.format(RADPositiveNoDHP / (double) RADPositiveNoDHPCount);
                } else {
                    line += ", 0";
                }
                if (RADNegativeDHPCount > 0) {
                    line += ", " + df.format(RADNegativeNoDHP / (double) RADNegativeNoDHPCount);
                } else {
                    line += ", 0";
                }
                line += ", " + df.format(MaxRentArrears);
                line += ", " + df.format(RATotal);
                line += ", " + df.format(RACount);
                line += ", " + df.format(RANonZeroCount);
                env.ge.log(line, true);
                pw1.println(line);
            }
            pw1.close();
        }

//        System.out.println("totalBT " + totalBT);
//            env.ge.log("</" + PT + ">");
//        }
    }
}
