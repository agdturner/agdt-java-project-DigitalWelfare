package uk.ac.leeds.ccg.projects.dw.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.data.id.SHBE_ClaimID;
import uk.ac.leeds.ccg.data.shbe.data.types.SHBE_D_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Record;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Records;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.math.Math_BigDecimal;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Record;
import uk.ac.leeds.ccg.projects.dw.data.uo.DW_UO_Set;
import uk.ac.leeds.ccg.projects.dw.io.DW_Files;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_IncomeAndRentSummary extends SHBE_Data {

    private static final long serialVersionUID = 1L;

    DW_Environment de;
    public DW_Files df;
    public DW_Strings ds;

    public DW_IncomeAndRentSummary(DW_Environment e, DW_Strings ds) 
            throws IOException, Exception {
        super(e.shbeEnv);
        this.de = e;
        this.df = e.files;
        this.ds = ds;
    }

    /**
     *
     * @param DW_SHBE_Records
     * @param HB_CTB
     * @param PTs
     * @param YM30
     * @param UOReportSetCouncil
     * @param UOReportSetRSL
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @param forceNew
     * @return HashMap<String, BigDecimal> result where the keys are ds for
     * Income and Rent Summary Statistics. An example key is:
     * SHBE_Strings.sTotal_Income + nameSuffix , where; nameSuffix =
     * AllOrHBOrCTB + PT. (AllOrHBOrCTB is given by HB_CTB, PT is given by PTs)
     */
    public HashMap<String, BigDecimal> getIncomeAndRentSummary(
            SHBE_Records DW_SHBE_Records, ArrayList<String> HB_CTB,
            ArrayList<String> PTs, UKP_YM3 YM30,
            DW_UO_Set UOReportSetCouncil, DW_UO_Set UOReportSetRSL,
            boolean doUnderOccupancy, boolean doCouncil, boolean doRSL,
            boolean forceNew) throws IOException, ClassNotFoundException {

        // Initialise result
        HashMap<String, BigDecimal> r = new HashMap<>();

        // Initialise IncomeAndRentSummaryFile
        Path p= getIncomeAndRentSummaryFile(YM30, doUnderOccupancy, doCouncil, doRSL);
        if (Files.exists(p)) {
            if (!forceNew) {
                return (HashMap<String, BigDecimal>) Generic_IO.readObject(
                        p);
            }
        }

        // Declare other variables
        int nTT;
        String HBOrCTB;
        String PT;
        String nameSuffix;
        Iterator<String> HB_CTBIte;
        Iterator<String> PTsIte;
        Map<SHBE_ClaimID, SHBE_Record> recs;
        BigDecimal tBD;
        BigDecimal zBD;
        BigDecimal TotalIncome;
        long TotalCount_IncomeNonZero;
        long TotalCount_IncomeZero;
        BigDecimal TotalWeeklyEligibleRentAmount;
        long TotalCount_WeeklyEligibleRentAmountNonZero;
        long TotalCount_WeeklyEligibleRentAmountZero;
        BigDecimal[] TotalIncomeTT;
        long[] TotalCount_IncomeTTNonZero;
        long[] TotalCount_IncomeTTZero;
        BigDecimal[] TotalTTWeeklyEligibleRentAmount;
        int[] TotalCount_TTWeeklyEligibleRentAmountNonZero;
        int[] TotalCount_TTWeeklyEligibleRentAmountZero;
        Iterator<SHBE_ClaimID> ite;
        HashMap<SHBE_ClaimID, DW_UO_Record> map;
        SHBE_ClaimID ClaimID;
        int TT;
        BigDecimal income;

        // Initialise some variables
        nTT = getNumberOfTenancyTypes();
        HB_CTBIte = HB_CTB.iterator();
        recs = DW_SHBE_Records.getRecords(de.HOOME);

        // Do loop for HB and CTB
        while (HB_CTBIte.hasNext()) {
            HBOrCTB = HB_CTBIte.next();
            Set<SHBE_ClaimID> claimIDs;

            // Initialise variables
            TotalCount_IncomeNonZero = 0;
            TotalIncome = BigDecimal.ZERO;
            TotalCount_IncomeZero = 0;
            TotalWeeklyEligibleRentAmount = BigDecimal.ZERO;
            TotalCount_WeeklyEligibleRentAmountNonZero = 0;
            TotalCount_WeeklyEligibleRentAmountZero = 0;
            TotalIncomeTT = new BigDecimal[nTT];
            TotalCount_IncomeTTNonZero = new long[nTT];
            TotalCount_IncomeTTZero = new long[nTT];
            TotalTTWeeklyEligibleRentAmount = new BigDecimal[nTT];
            TotalCount_TTWeeklyEligibleRentAmountNonZero = new int[nTT];
            TotalCount_TTWeeklyEligibleRentAmountZero = new int[nTT];
            for (int i = 0; i < nTT; i++) {
                TotalIncomeTT[i] = BigDecimal.ZERO;
                TotalCount_IncomeTTNonZero[i] = 0;
                TotalCount_IncomeTTZero[i] = 0;
                TotalTTWeeklyEligibleRentAmount[i] = BigDecimal.ZERO;
                TotalCount_TTWeeklyEligibleRentAmountNonZero[i] = 0;
                TotalCount_TTWeeklyEligibleRentAmountZero[i] = 0;
            }
            PTsIte = PTs.iterator();

            // Do loop for Payment Types
            while (PTsIte.hasNext()) {
                PT = PTsIte.next();
                if (HBOrCTB.equalsIgnoreCase(DW_Strings.sHB)) {
                    claimIDs = getClaimIDsWithStatusOfHBAtExtractDate(DW_SHBE_Records, PT);
                } else {
                    claimIDs = getClaimIDsWithStatusOfCTBAtExtractDate(DW_SHBE_Records, PT);
                }
//                Debugging code
//                if (ClaimIDs == null) {
//                    System.out.println(SHBE_Strings.sHB);
//                    System.out.println(PT);
//                }
//                
                if (doUnderOccupancy) {
                    if (UOReportSetCouncil != null) {
                        map = UOReportSetCouncil.getMap();
                        ite = map.keySet().iterator();
                        while (ite.hasNext()) {
                            ClaimID = ite.next();
                            if (claimIDs.contains(ClaimID)) {
                                SHBE_Record rec;
                                rec = recs.get(ClaimID);
                                if (rec != null) {
                                    SHBE_D_Record aDRecord;
                                    aDRecord = rec.getDRecord();
                                    TT = aDRecord.getTenancyType();
                                    income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                                    if (HBOrCTB.equalsIgnoreCase(DW_Strings.sHB)) {
                                        // HB
                                        if (isHBClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    } else // CTB
                                    if (isCTBOnlyClaim(TT)) {
                                        TotalIncome = TotalIncome.add(income);
                                        TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                        if (income.compareTo(BigDecimal.ZERO) == 1) {
                                            TotalCount_IncomeNonZero++;
                                            TotalCount_IncomeTTNonZero[TT]++;
                                        } else {
                                            TotalCount_IncomeZero++;
                                            TotalCount_IncomeTTZero[TT]++;
                                        }
                                        tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                        TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                        TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                        if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                            TotalCount_WeeklyEligibleRentAmountNonZero++;
                                            TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                        } else {
                                            TotalCount_WeeklyEligibleRentAmountZero++;
                                            TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (UOReportSetRSL != null) {
                        map = UOReportSetRSL.getMap();
                        ite = map.keySet().iterator();
                        while (ite.hasNext()) {
                            ClaimID = ite.next();
                            if (claimIDs.contains(ClaimID)) {
                                SHBE_Record rec;
                                rec = recs.get(ClaimID);
                                if (rec != null) {
                                    SHBE_D_Record aDRecord;
                                    aDRecord = rec.getDRecord();
                                    TT = aDRecord.getTenancyType();
                                    income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                                    if (HBOrCTB.equalsIgnoreCase(DW_Strings.sHB)) {
                                        // HB
                                        if (isHBClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    } else {
                                        // CTB
                                        if (isCTBOnlyClaim(TT)) {
                                            TotalIncome = TotalIncome.add(income);
                                            TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                            if (income.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_IncomeNonZero++;
                                                TotalCount_IncomeTTNonZero[TT]++;
                                            } else {
                                                TotalCount_IncomeZero++;
                                                TotalCount_IncomeTTZero[TT]++;
                                            }
                                            tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                            TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                            TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                            if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                                TotalCount_WeeklyEligibleRentAmountNonZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                            } else {
                                                TotalCount_WeeklyEligibleRentAmountZero++;
                                                TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Iterator<SHBE_ClaimID> ClaimIDsIte;
                    ClaimIDsIte = claimIDs.iterator();
                    while (ClaimIDsIte.hasNext()) {
                        ClaimID = ClaimIDsIte.next();
                        SHBE_Record rec;
                        rec = recs.get(ClaimID);
                        SHBE_D_Record aDRecord;
                        aDRecord = rec.getDRecord();
                        TT = aDRecord.getTenancyType();
                        income = BigDecimal.valueOf(getClaimantsAndPartnersIncomeTotal(aDRecord));
                        if (HBOrCTB.equalsIgnoreCase(DW_Strings.sHB)) {
                            // HB
                            if (isHBClaim(TT)) {
                                TotalIncome = TotalIncome.add(income);
                                TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                if (income.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_IncomeNonZero++;
                                    TotalCount_IncomeTTNonZero[TT]++;
                                } else {
                                    TotalCount_IncomeZero++;
                                    TotalCount_IncomeTTZero[TT]++;
                                }
                                tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_WeeklyEligibleRentAmountNonZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                } else {
                                    TotalCount_WeeklyEligibleRentAmountZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                }
                            }
                        } else // CTB
                        {
                            if (isCTBOnlyClaim(TT)) {
                                TotalIncome = TotalIncome.add(income);
                                TotalIncomeTT[TT] = TotalIncomeTT[TT].add(income);
                                if (income.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_IncomeNonZero++;
                                    TotalCount_IncomeTTNonZero[TT]++;
                                } else {
                                    TotalCount_IncomeZero++;
                                    TotalCount_IncomeTTZero[TT]++;
                                }
                                tBD = BigDecimal.valueOf(aDRecord.getWeeklyEligibleRentAmount());
                                TotalWeeklyEligibleRentAmount = TotalWeeklyEligibleRentAmount.add(tBD);
                                TotalTTWeeklyEligibleRentAmount[TT] = TotalTTWeeklyEligibleRentAmount[TT].add(tBD);
                                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                                    TotalCount_WeeklyEligibleRentAmountNonZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountNonZero[TT]++;
                                } else {
                                    TotalCount_WeeklyEligibleRentAmountZero++;
                                    TotalCount_TTWeeklyEligibleRentAmountZero[TT]++;
                                }
                            }
                        }
                    }
                }
                nameSuffix = HBOrCTB + PT;
                tBD = BigDecimal.valueOf(TotalCount_IncomeNonZero);
                zBD = BigDecimal.valueOf(TotalCount_IncomeZero);
                r.put(DW_Strings.sTotal_Income + nameSuffix, TotalIncome);
                r.put(DW_Strings.sTotalCount_IncomeNonZero + nameSuffix, tBD);
                r.put(DW_Strings.sTotalCount_IncomeZero + nameSuffix, zBD);
                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                    r.put(DW_Strings.sAverage_NonZero_Income + nameSuffix,
                            Math_BigDecimal.divideRoundIfNecessary(
                                    TotalIncome, tBD, 2, RoundingMode.HALF_UP));
                } else {
                    r.put(DW_Strings.sAverage_NonZero_Income + nameSuffix, BigDecimal.ZERO);
                }
                tBD = BigDecimal.valueOf(TotalCount_WeeklyEligibleRentAmountNonZero);
                zBD = BigDecimal.valueOf(TotalCount_WeeklyEligibleRentAmountZero);
                r.put(DW_Strings.sTotal_WeeklyEligibleRentAmount + nameSuffix,
                        TotalWeeklyEligibleRentAmount);
                r.put(DW_Strings.sTotalCount_WeeklyEligibleRentAmountNonZero + nameSuffix, tBD);
                r.put(DW_Strings.sTotalCount_WeeklyEligibleRentAmountZero + nameSuffix, zBD);
                if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                    r.put(DW_Strings.sAverage_NonZero_WeeklyEligibleRentAmount + nameSuffix,
                            Math_BigDecimal.divideRoundIfNecessary(
                                    TotalWeeklyEligibleRentAmount,
                                    tBD, 2, RoundingMode.HALF_UP));
                }
                for (int i = 0; i < nTT; i++) {
                    // Income
                    r.put(ds.sTotal_IncomeTT[i] + nameSuffix, TotalIncomeTT[i]);
                    tBD = BigDecimal.valueOf(TotalCount_IncomeTTNonZero[i]);
                    zBD = BigDecimal.valueOf(TotalCount_IncomeTTZero[i]);
                    r.put(ds.sTotalCount_IncomeNonZeroTT[i] + nameSuffix, tBD);
                    r.put(ds.sTotalCount_IncomeZeroTT[i] + nameSuffix, zBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        r.put(ds.sAverage_NonZero_IncomeTT[i] + nameSuffix,
                                Math_BigDecimal.divideRoundIfNecessary(
                                        TotalIncomeTT[i],
                                        tBD, 2, RoundingMode.HALF_UP));
                    } else {
                        r.put(ds.sAverage_NonZero_IncomeTT[i] + nameSuffix, BigDecimal.ZERO);
                    }
                    // Rent
                    r.put(ds.sTotal_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                            TotalTTWeeklyEligibleRentAmount[i]);
                    tBD = BigDecimal.valueOf(
                            TotalCount_TTWeeklyEligibleRentAmountNonZero[i]);
                    zBD = BigDecimal.valueOf(
                            TotalCount_TTWeeklyEligibleRentAmountZero[i]);
                    r.put(ds.sTotalCount_WeeklyEligibleRentAmountNonZeroTT[i] + nameSuffix, tBD);
                    r.put(ds.sTotalCount_WeeklyEligibleRentAmountZeroTT[i] + nameSuffix, zBD);
                    if (tBD.compareTo(BigDecimal.ZERO) == 1) {
                        r.put(ds.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                                Math_BigDecimal.divideRoundIfNecessary(
                                        TotalTTWeeklyEligibleRentAmount[i],
                                        tBD, 2, RoundingMode.HALF_UP));
                    } else {
                        r.put(ds.sAverage_NonZero_WeeklyEligibleRentAmountTT[i] + nameSuffix,
                                BigDecimal.ZERO);
                    }
                }
            }
        }
        env.env.io.writeObject(r, p);
        return r;
    }

    /**
     * @param YM3
     * @param doUnderOccupancy
     * @param doCouncil
     * @param doRSL
     * @return
     */
    public Path getIncomeAndRentSummaryFile(UKP_YM3 YM3, boolean doUnderOccupancy,
            boolean doCouncil, boolean doRSL) throws IOException {
        Path r;
        String partFilename;
        if (doUnderOccupancy) {
            if (doCouncil) {
                if (doRSL) {
                    //partFilename = "IncomeAndRentSummaryUA_HashMap_String__BigDecimal.dat";
                    partFilename = DW_Strings.sIncomeAndRentSummary
                            + DW_Strings.sU + DW_Strings.s_A + DW_Strings.symbol_underscore
                            + DW_Strings.s_HashMap + DW_Strings.symbol_underscore
                            + DW_Strings.s_String + DW_Strings.symbol_underscore + DW_Strings.symbol_underscore
                            + DW_Strings.s_BigDecimal + DW_Strings.sBinaryFileExtension;
                } else {
                    //partFilename = "IncomeAndRentSummaryUC_HashMap_String__BigDecimal.dat";
                    partFilename = DW_Strings.sIncomeAndRentSummary
                            + DW_Strings.sU + DW_Strings.sCouncil + DW_Strings.symbol_underscore
                            + DW_Strings.s_HashMap + DW_Strings.symbol_underscore
                            + DW_Strings.s_String + DW_Strings.symbol_underscore + DW_Strings.symbol_underscore
                            + DW_Strings.s_BigDecimal + DW_Strings.sBinaryFileExtension;
                }
            } else {
                //partFilename = "IncomeAndRentSummaryUR_HashMap_String__BigDecimal.dat";
                partFilename = DW_Strings.sIncomeAndRentSummary
                        + DW_Strings.sU + DW_Strings.sRSL + DW_Strings.symbol_underscore
                        + DW_Strings.s_HashMap + DW_Strings.symbol_underscore
                        + DW_Strings.s_String + DW_Strings.symbol_underscore + DW_Strings.symbol_underscore
                        + DW_Strings.s_BigDecimal + DW_Strings.sBinaryFileExtension;
            }
        } else {
            //partFilename = "IncomeAndRentSummary_HashMap_String__BigDecimal.dat";
            partFilename = DW_Strings.sIncomeAndRentSummary
                    + DW_Strings.s_HashMap + DW_Strings.symbol_underscore
                    + DW_Strings.s_String + DW_Strings.symbol_underscore + DW_Strings.symbol_underscore
                    + DW_Strings.s_BigDecimal + DW_Strings.sBinaryFileExtension;
        }
        Path dir = Paths.get(df.getGeneratedSHBEDir().toString(), YM3.toString());
        dir = df.getUODir(dir, doUnderOccupancy, doCouncil);
        Files.createDirectories(dir);
        r = Paths.get(dir.toString(), partFilename);
        return r;
    }
}
