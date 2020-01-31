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
package uk.ac.leeds.ccg.projects.dw.core;

import uk.ac.leeds.ccg.projects.dw.core.DW_ID;
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
public class DW_IDTest {
    
    public DW_IDTest() {
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
     * Test of getID method, of class DW_ID.
     */
    @Test
    public void testGetID() {
        System.out.println("getID");
        DW_ID instance = null;
        instance = new DW_ID(100);
        int expResult = 100;
        long result = instance.getID();
        assertEquals(expResult, result);
        DW_ID instance2 = null;
        instance2 = new DW_ID(instance);
        result = instance.getID();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class DW_ID.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DW_ID instance = null;
        instance = new DW_ID(100);
        String expResult = "100";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class DW_ID.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        obj = new DW_ID(100);
        DW_ID instance = null;
        instance = new DW_ID(100);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // Second test
        instance = new DW_ID(101);
        expResult = false;
        result = instance.equals(obj);
        assertEquals(expResult, result);
    }

}
