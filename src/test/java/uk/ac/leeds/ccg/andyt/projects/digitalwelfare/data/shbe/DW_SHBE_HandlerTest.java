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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

/**
 *
 * @author geoagdt
 */
public class DW_SHBE_HandlerTest {
    
    public DW_SHBE_HandlerTest() {
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

//    /**
//     * Test of getRecordTypes method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetRecordTypes() {
//        System.out.println("getRecordTypes");
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        HashSet<String> expResult = null;
//        HashSet<String> result = instance.getRecordTypes();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of initRecordTypes method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testInitRecordTypes() {
//        System.out.println("initRecordTypes");
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        instance.initRecordTypes();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of loadSHBEData method, of class DW_SHBE_Handler.
     */
    @Test
    public void testLoadSHBEData() {
//        System.out.println("loadSHBEData");
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        ArrayList expResult = null;
//        ArrayList result = instance.loadSHBEData();
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of loadInputData method, of class DW_SHBE_Handler.
     */
    @Test
    public void testLoadInputData_File_String() {
        System.out.println("loadInputData");
        DW_Files tDW_Files;
        tDW_Files = new DW_Files();
        String dir;
        dir = tDW_Files.getDigitalWelfareDir().toString();
        DW_Environment env;
        env = new DW_Environment(dir);
        DW_SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = env.getDW_SHBE_Handler();
        File directory = tDW_Files.getInputSHBEDir();
        String[] filenames = tDW_SHBE_Handler.getSHBEFilenamesAll();
//        filenames[0] = "hb9803_SHBE_206728k April 2008.csv";
//        filenames[1] = "hb9803_SHBE_234696k October 2008.csv";
//        filenames[2] = "hb9803_SHBE_265149k April 2009.csv";
//        filenames[3] = "hb9803_SHBE_295723k October 2009.csv";
//        filenames[4] = "hb9803_SHBE_329509k April 2010.csv";
//        filenames[5] = "hb9803_SHBE_363186k October 2010.csv";
//        filenames[6] = "hb9803_SHBE_391746k March 2011.csv";
//        filenames[7] = "hb9803_SHBE_397524k April 2011.csv";
//        filenames[8] = "hb9803_SHBE_415181k July 2011.csv";
//        filenames[9] = "hb9803_SHBE_433970k October 2011.csv";
//        filenames[10] = "hb9803_SHBE_451836k January 2012.csv";
//        filenames[11] = "hb9803_SHBE_470742k April 2012.csv";
//        filenames[12] = "hb9803_SHBE_490903k July 2012.csv";
//        filenames[13] = "hb9803_SHBE_511038k October 2012.csv";
//        filenames[14] = "hb9803_SHBE_530243k January 2013.csv";
//        filenames[15] = "hb9803_SHBE_536123k February 2013.csv";
//        filenames[16] = "hb9991_SHBE_543169k March 2013.csv";
//        filenames[17] = "hb9991_SHBE_549416k April 2013.csv";
//        filenames[18] = "hb9991_SHBE_555086k May 2013.csv";
//        filenames[19] = "hb9991_SHBE_562036k June 2013.csv";
//        filenames[20] = "hb9991_SHBE_568694k July 2013.csv";
//        filenames[21] = "hb9991_SHBE_576432k August 2013.csv";
//        filenames[22] = "hb9991_SHBE_582832k September 2013.csv";
//        filenames[23] = "hb9991_SHBE_589664k Oct 2013.csv";
//        filenames[24] = "hb9991_SHBE_596500k Nov 2013.csv";
//        filenames[25] = "hb9991_SHBE_603335k Dec 2013.csv";
//        filenames[26] = "hb9991_SHBE_609791k Jan 2014.csv";
//        filenames[27] = "hb9991_SHBE_615103k Feb 2014.csv";
//        filenames[28] = "hb9991_SHBE_621666k Mar 2014.csv";
//        filenames[29] = "hb9991_SHBE_629066k Apr 2014.csv";
//        filenames[30] = "hb9991_SHBE_635115k May 2014.csv";
//        filenames[31] = "hb9991_SHBE_641800k June 2014.csv";
//        filenames[32] = "hb9991_SHBE_648859k July 2014.csv";
//        filenames[33] = "hb9991_SHBE_656520k August 2014.csv";
//        filenames[34] = "hb9991_SHBE_663169k September 2014.csv";
//        filenames[35] = "hb9991_SHBE_670535k October 2014.csv";
//        filenames[36] = "hb9991_SHBE_677543k November 2014.csv";
//        filenames[37] = "hb9991_SHBE_684519k December 2014.csv";
//        filenames[38] = "hb9991_SHBE_691401k January 2015.csv";
//        filenames[39] = "hb9991_SHBE_697933k February 2015.csv";
//        filenames[40] = "hb9991_SHBE_705679k March 2015.csv";
//        filenames[41] = "hb9991_SHBE_712197k April 2015.csv";
        
//        String filename = filenames[0];
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        Object[] result = instance.loadInputData(directory, filename, true);
//        Object[] expResult = instance.loadInputData(directory, filename, false);
//        assertArrayEquals(expResult, result); // Why is this failing?
        
        
        
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

//    /**
//     * Test of loadInputData method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testLoadInputData_3args() {
//        System.out.println("loadInputData");
//        File directory = null;
//        String filename = "";
//        DW_SHBE_CollectionHandler handler = null;
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        Object[] expResult = null;
//        Object[] result = instance.loadInputData(directory, filename, handler);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of readAndCheckFirstLine method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testReadAndCheckFirstLine() {
//        System.out.println("readAndCheckFirstLine");
//        File directory = null;
//        String filename = "";
//        DW_SHBE_Handler instance = new DW_SHBE_Handler();
//        int expResult = 0;
//        int result = instance.readAndCheckFirstLine(directory, filename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of aggregate method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testAggregate_HashSet() {
//        System.out.println("aggregate");
//        HashSet<DW_SHBE_Record> records = null;
//        DW_SHBE_RecordAggregate expResult = null;
//        DW_SHBE_RecordAggregate result = DW_SHBE_Handler.aggregate(records);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of aggregate method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testAggregate_DW_SHBE_Record_DW_SHBE_RecordAggregate() {
//        System.out.println("aggregate");
//        DW_SHBE_Record aSHBE_DataRecord = null;
//        DW_SHBE_RecordAggregate a_Aggregate_SHBE_DataRecord = null;
//        DW_SHBE_Handler.aggregate(aSHBE_DataRecord, a_Aggregate_SHBE_DataRecord);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getClaimantsIncomeTotal method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetClaimantsIncomeTotal() {
//        System.out.println("getClaimantsIncomeTotal");
//        DW_SHBE_Record DRecord = null;
//        long expResult = 0L;
//        long result = DW_SHBE_Handler.getClaimantsIncomeTotal(DRecord);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getPartnersIncomeTotal method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetPartnersIncomeTotal() {
//        System.out.println("getPartnersIncomeTotal");
//        DW_SHBE_Record DRecord = null;
//        long expResult = 0L;
//        long result = DW_SHBE_Handler.getPartnersIncomeTotal(DRecord);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getIncomeTotal method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetIncomeTotal() {
//        System.out.println("getIncomeTotal");
//        DW_SHBE_Record DRecord = null;
//        long expResult = 0L;
//        long result = DW_SHBE_Handler.getIncomeTotal(DRecord);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUnderOccupancy method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetUnderOccupancy() {
//        System.out.println("getUnderOccupancy");
//        DW_SHBE_Record DRecord = null;
//        boolean expResult = false;
//        boolean result = DW_SHBE_Handler.getUnderOccupancy(DRecord);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getUnderOccupancyAmount method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetUnderOccupancyAmount() {
//        System.out.println("getUnderOccupancyAmount");
//        DW_SHBE_Record DRecord = null;
//        int expResult = 0;
//        int result = DW_SHBE_Handler.getUnderOccupancyAmount(DRecord);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSHBEFilenamesAll method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetSHBEFilenamesAll() {
//        System.out.println("getSHBEFilenamesAll");
//        String[] expResult = null;
//        String[] result = DW_SHBE_Handler.getSHBEFilenamesAll();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getMonth method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetMonth() {
//        System.out.println("getMonth");
//        String SHBEFilename = "";
//        String expResult = "";
//        String result = DW_SHBE_Handler.getMonth(SHBEFilename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getYear method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetYear() {
//        System.out.println("getYear");
//        String SHBEFilename = "";
//        String expResult = "";
//        String result = DW_SHBE_Handler.getYear(SHBEFilename);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getSHBEFilenamesSome method, of class DW_SHBE_Handler.
//     */
//    @Test
//    public void testGetSHBEFilenamesSome() {
//        System.out.println("getSHBEFilenamesSome");
//        String[] expResult = null;
//        String[] result = DW_SHBE_Handler.getSHBEFilenamesSome();
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
