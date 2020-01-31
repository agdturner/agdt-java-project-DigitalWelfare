/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.projects.dw.io;

import uk.ac.leeds.ccg.projects.dw.io.DW_IO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author geoagdt
 */
public class DW_IOTest {

    public DW_IOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of splitByQuotesThenCommas method, of class DW_IO.
     */
    @Test
    public void testSplitWithQuotesThenCommas() {
        System.out.println("splitByQuotesThenCommas");
        String line;
        String[] expResult;
        String[] result = null;
        int testNumber;
        // Test1
        testNumber = 1;
        line = "1,2,3,\"4,567\",8,9";
        expResult = new String[6];
        expResult[0] = "1";
        expResult[1] = "2";
        expResult[2] = "3";
        expResult[3] = "4,567";
        expResult[4] = "8";
        expResult[5] = "9";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test2
        testNumber = 2;
        line = "1,2,3,\"4,567\",\"8\",9";
        expResult = new String[6];
        expResult[0] = "1";
        expResult[1] = "2";
        expResult[2] = "3";
        expResult[3] = "4,567";
        expResult[4] = "8";
        expResult[5] = "9";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 3
        testNumber = 3;
        line = "1,2,3,,\"4,567\",\"8\",9";
        expResult = new String[7];
        expResult[0] = "1";
        expResult[1] = "2";
        expResult[2] = "3";
        expResult[3] = "";
        expResult[4] = "4,567";
        expResult[5] = "8";
        expResult[6] = "9";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 4
        testNumber = 4;
        line = "A,B,,*C*,\"English, Other\",Yes,Yes,Yes,\"�1,000 - �1,499 pcm\",D,,,F";
        expResult = new String[13];
        expResult[0] = "A";
        expResult[1] = "B";
        expResult[2] = "";
        expResult[3] = "*C*";
        expResult[4] = "English, Other";
        expResult[5] = "Yes";
        expResult[6] = "Yes";
        expResult[7] = "Yes";
        expResult[8] = "�1,000 - �1,499 pcm";
        expResult[9] = "D";
        expResult[10] = "";
        expResult[11] = "";
        expResult[12] = "F";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 5
        testNumber = 5;
        line = "2012/13,Q2,909384,50-64,58,Female,Long-term health condition,Yes,,Permanently sick/disabled,White - British,British,Not Recorded,Yes,Yes,Yes,�400 - �599 pcm,Couple,Council/ALMO tenant,Married/Cohabiting/Civil Partnership,0,0,LS26 8NP,Leeds,Rothwell,Elmet and Rothwell,Alec Shelbrooke MP,Leeds,LEEDS,,,,\"Yes, the client is agreeable to MAS evaluation (market research)\",Client agrees to MAS evaluation,2,22/02/1954";
        expResult = new String[36];
        expResult[0] = "2012/13";
        expResult[1] = "Q2";
        expResult[2] = "909384";
        expResult[3] = "50-64";
        expResult[4] = "58";
        expResult[5] = "Female";
        expResult[6] = "Long-term health condition";
        expResult[7] = "Yes";
        expResult[8] = "";
        expResult[9] = "Permanently sick/disabled";
        expResult[10] = "White - British";
        expResult[11] = "British";
        expResult[12] = "Not Recorded";
        expResult[13] = "Yes";
        expResult[14] = "Yes";
        expResult[15] = "Yes";
        expResult[16] = "�400 - �599 pcm";
        expResult[17] = "Couple";
        expResult[18] = "Council/ALMO tenant";
        expResult[19] = "Married/Cohabiting/Civil Partnership";
        expResult[20] = "0";
        expResult[21] = "0";
        expResult[22] = "LS26 8NP";
        expResult[23] = "Leeds";
        expResult[24] = "Rothwell";
        expResult[25] = "Elmet and Rothwell";
        expResult[26] = "Alec Shelbrooke MP";
        expResult[27] = "Leeds";
        expResult[28] = "LEEDS";
        expResult[29] = "";
        expResult[30] = "";
        expResult[31] = "";
        expResult[32] = "Yes, the client is agreeable to MAS evaluation (market research)";
        expResult[33] = "Client agrees to MAS evaluation";
        expResult[34] = "2";
        expResult[35] = "22/02/1954";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 6
        testNumber = 6;
        line = "2012/13,Q2,1606281,,-1,Female,Unknown/Withheld,Not Recorded,,,Other,,Not Recorded,Yes,Yes,Yes,,,,,0,0,LS6 1RW,Leeds,Headingley,Leeds North West,Greg Mulholland MP,Leeds,LEEDS,,,,,,,";
        expResult = new String[36];
        expResult[0] = "2012/13";
        expResult[1] = "Q2";
        expResult[2] = "1606281";
        expResult[3] = "";
        expResult[4] = "-1";
        expResult[5] = "Female";
        expResult[6] = "Unknown/Withheld";
        expResult[7] = "Not Recorded";
        expResult[8] = "";
        expResult[9] = "";
        expResult[10] = "Other";
        expResult[11] = "";
        expResult[12] = "Not Recorded";
        expResult[13] = "Yes";
        expResult[14] = "Yes";
        expResult[15] = "Yes";
        expResult[16] = "";
        expResult[17] = "";
        expResult[18] = "";
        expResult[19] = "";
        expResult[20] = "0";
        expResult[21] = "0";
        expResult[22] = "LS6 1RW";
        expResult[23] = "Leeds";
        expResult[24] = "Headingley";
        expResult[25] = "Leeds North West";
        expResult[26] = "Greg Mulholland MP";
        expResult[27] = "Leeds";
        expResult[28] = "LEEDS";
        expResult[29] = "";
        expResult[30] = "";
        expResult[31] = "";
        expResult[32] = "";
        expResult[33] = "";
        expResult[34] = "";
        expResult[35] = "";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 7
        testNumber = 7;
        line = "2012/13,Q2,1606281,,\"-1\",Female";
        expResult = new String[6];
        expResult[0] = "2012/13";
        expResult[1] = "Q2";
        expResult[2] = "1606281";
        expResult[3] = "";
        expResult[4] = "-1";
        expResult[5] = "Female";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
        // Test 8
        testNumber = 8;
        line = "\"2012\",\"Q1\",,\"5\",7,,";
        expResult = new String[7];
        expResult[0] = "2012";
        expResult[1] = "Q1";
        expResult[2] = "";
        expResult[3] = "5";
        expResult[4] = "7";
        expResult[5] = "";
        expResult[6] = "";
        result = DW_IO.splitWithQuotesThenCommas(line);
        printResultComparison(testNumber, line, expResult, result);
        assertArrayEquals(expResult, result);
    }

    public void printResultComparison(
            int testNumber,
            String line,
            String[] expResult,
            String[] result) {
        System.out.println("Test " + testNumber
                + " line " + line);
        for (int i = 0; i < expResult.length; i++) {
            System.out.println("expResult[" + i + "] " + expResult[i]
                    + ", result[" + i + "] " + result[i]);
            assertEquals(expResult[i], result[i]);
        }
    }

    /**
     * Test of isAllCommas method, of class DW_IO.
     */
    @Test
    public void testIsAllCommas() {
        System.out.println("isAllCommas");
        String s;
        boolean expResult;
        boolean result = false;
        // Test 1
        s = ", ";
        expResult = false;
        result = DW_IO.isAllCommas(s);
        assertEquals(expResult, result);
        // Test 2
        s = ",";
        expResult = true;
        result = DW_IO.isAllCommas(s);
        assertEquals(expResult, result);
        // Test 3
        s = ",,,,,,,,";
        expResult = true;
        result = DW_IO.isAllCommas(s);
        assertEquals(expResult, result);
        // Test 4
        s = ",,,,,, ,,,,";
        expResult = false;
        result = DW_IO.isAllCommas(s);
        assertEquals(expResult, result);
        // Test 5
        s = " ,";
        expResult = false;
        result = DW_IO.isAllCommas(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of count method, of class DW_IO.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        String s;
        char c;
        int expResult;
        int result;
        // Test 1
        s = "ddsadsa";
        c = 'd';
        expResult = 3;
        result = DW_IO.count(s, c);
        assertEquals(expResult, result);
        // Test 2
        s = "\\zxz,.\\z/c\\zcz\\p;\\\'saaqs";
        c = '\'';
        expResult = 1;
        result = DW_IO.count(s, c);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStartStringCount method, of class DW_IO.
     */
    @Test
    public void testGetStartStringCount() {
        System.out.println("getStartStringCount");
        String s;
        String s0;
        int expResult;
        int result = 0;
        // Test 1
        s = ",,,12345,13232,4231";
        s0 = ",";
        expResult = 3;
        try {
            result = DW_IO.getStartStringCount(s, s0);
        } catch (Exception ex) {
            Logger.getLogger(DW_IOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(expResult, result);
        // Test 2
        s = ",\",\",12345,13232,4231";
        s0 = ",\"";
        expResult = 2;
        result = DW_IO.getStartStringCount(s, s0);
        assertEquals(expResult, result);
    }

    /**
     * Test of getEndStringCount method, of class DW_IO.
     */
    @Test
    public void testGetEndStringCount() {
        System.out.println("getEndStringCount");
        String s;
        String s0;
        int expResult;
        int result = 0;
        // Test 1
        s = "2,4231,,,,,";
        s0 = ",";
        expResult = 5;
        result = DW_IO.getEndStringCount(s, s0);
        assertEquals(expResult, result);
        // Test 2
        s = "2,4231,\",\"";
        s0 = ",\"";
        expResult = 2;
        result = DW_IO.getEndStringCount(s, s0);
        assertEquals(expResult, result);
    }

    /**
     * Test of split method, of class DW_IO.
     */
    @Test
    public void testSplit() {
        System.out.println("split0");
        String s;
        String s0;
        String[] expResult;
        String[] result = null;
        // Test 1
        s = ",,2,4231,,,,,";
        s0 = ",";
        expResult = new String[9];
        expResult[0] = "";
        expResult[1] = "";
        expResult[2] = "2";
        expResult[3] = "4231";
        expResult[4] = "";
        expResult[5] = "";
        expResult[6] = "";
        expResult[7] = "";
        expResult[8] = "";
        result = DW_IO.split(s, s0);
        assertArrayEquals(expResult, result);
    }

}
