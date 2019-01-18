/*
 * Copyright (C) 2014 geoagdt.
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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.lcc;

import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.data.ONSPD_Point;
import uk.ac.leeds.ccg.andyt.generic.data.onspd.util.ONSPD_YM3;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.andyt.geotools.Geotools_Style;
import uk.ac.leeds.ccg.andyt.geotools.core.Geotools_Strings;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.generic.data.shbe.data.SHBE_Handler;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping.DW_StyleParameters;

/**
 *
 * @author geoagdt
 */
public class DW_LineMapsLCC extends DW_Maps {

    private final String targetPropertyNameLSOA = "LSOA11CD";
//    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<Geotools_Shapefile> midgrounds;
    protected ArrayList<Geotools_Shapefile> foregrounds;

    protected DW_StyleParameters styleParameters;
    
    protected Geotools_Style Style;
    protected Geotools_Strings _Geotools_Strings;
    
    public DW_LineMapsLCC(DW_Environment de) {
        super(de);
        Style = Geotools.getStyle();
        _Geotools_Strings = Geotools.getGeotools_Strings();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //new DW_LineMapsLCC().run2();
            new DW_LineMapsLCC(null).run();
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        } catch (Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace();
//            StackTraceElement[] stes = e.getStackTrace();
//            for (StackTraceElement ste : stes) {
//                System.err.println(ste.toString());
//            }
        }
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChangesSocialGrouped() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChangesSocialGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChangesSocialGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChangesSocial() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChangesSocial(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChangesSocial(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChanges() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChanges(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChanges(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeGroups() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeGroups(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeGroups(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeChanges(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1 - 1UO");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4 - 4UO");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4UO - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1 - 4UO");
            tenancyTypeChanges.add("1UO - 4");
            tenancyTypeChanges.add("1UO - 4UO");
        } else {
            tenancyTypeChanges.add("1 - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4 - 1UO");
            tenancyTypeChanges.add("4UO - 1");
            tenancyTypeChanges.add("4UO - 1UO");
        } else {
            tenancyTypeChanges.add("4 - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
        } else {
            tenancyTypeChanges.add("1 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
        } else {
            tenancyTypeChanges.add("4 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            // Regulated to unregulated
            // End in 3
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("2UO - 3");
            tenancyTypeChanges.add("2 - 3UO");
            tenancyTypeChanges.add("2UO - 3UO");
            tenancyTypeChanges.add("2UO - 3");
            tenancyTypeChanges.add("2 - 3UO");
            tenancyTypeChanges.add("2UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
            // End in 6
            tenancyTypeChanges.add("1UO - 6");
            tenancyTypeChanges.add("1 - 6UO");
            tenancyTypeChanges.add("1UO - 6UO");
            tenancyTypeChanges.add("1UO - 6");
            tenancyTypeChanges.add("1 - 6UO");
            tenancyTypeChanges.add("1UO - 6UO");
            tenancyTypeChanges.add("2UO - 6");
            tenancyTypeChanges.add("2 - 6UO");
            tenancyTypeChanges.add("2UO - 6UO");
            tenancyTypeChanges.add("2UO - 6");
            tenancyTypeChanges.add("2 - 6UO");
            tenancyTypeChanges.add("2UO - 6UO");
            tenancyTypeChanges.add("4UO - 6");
            tenancyTypeChanges.add("4 - 6UO");
            tenancyTypeChanges.add("4UO - 6UO");
            tenancyTypeChanges.add("4UO - 6");
            tenancyTypeChanges.add("4 - 6UO");
            tenancyTypeChanges.add("4UO - 6UO");
        } else {
            tenancyTypeChanges.add("1 - 3");
            tenancyTypeChanges.add("2 - 3");
            tenancyTypeChanges.add("4 - 3");
            tenancyTypeChanges.add("1 - 6");
            tenancyTypeChanges.add("2 - 6");
            tenancyTypeChanges.add("4 - 6");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            // Social to private deregulated
            // End in 3
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("1UO - 3");
            tenancyTypeChanges.add("1 - 3UO");
            tenancyTypeChanges.add("1UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
            tenancyTypeChanges.add("4UO - 3");
            tenancyTypeChanges.add("4 - 3UO");
            tenancyTypeChanges.add("4UO - 3UO");
            // End in 6
            tenancyTypeChanges.add("1UO - 6");
            tenancyTypeChanges.add("1 - 6UO");
            tenancyTypeChanges.add("1UO - 6UO");
            tenancyTypeChanges.add("1UO - 6");
            tenancyTypeChanges.add("1 - 6UO");
            tenancyTypeChanges.add("1UO - 6UO");
            tenancyTypeChanges.add("4UO - 6");
            tenancyTypeChanges.add("4 - 6UO");
            tenancyTypeChanges.add("4UO - 6UO");
            tenancyTypeChanges.add("4UO - 6");
            tenancyTypeChanges.add("4 - 6UO");
            tenancyTypeChanges.add("4UO - 6UO");
        } else {
            tenancyTypeChanges.add("1 - 3");
            tenancyTypeChanges.add("4 - 3");
            tenancyTypeChanges.add("1 - 6");
            tenancyTypeChanges.add("4 - 6");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 1");
            tenancyTypeChanges.add("3 - 1UO");
            tenancyTypeChanges.add("3UO - 1UO");
        } else {
            tenancyTypeChanges.add("3 - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 4");
            tenancyTypeChanges.add("3 - 4UO");
            tenancyTypeChanges.add("3UO - 4UO");
        } else {
            tenancyTypeChanges.add("3 - 4");
        }
        result.add(tenancyTypeChanges);
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 1");
            tenancyTypeChanges.add("3 - 1UO");
            tenancyTypeChanges.add("3UO - 1UO");
            tenancyTypeChanges.add("3UO - 2");
            tenancyTypeChanges.add("3 - 2UO");
            tenancyTypeChanges.add("3UO - 2UO");
            tenancyTypeChanges.add("3UO - 4");
            tenancyTypeChanges.add("3 - 4UO");
            tenancyTypeChanges.add("3UO - 4UO");
        } else {
            tenancyTypeChanges.add("3 - 1");
            tenancyTypeChanges.add("3 - 2");
            tenancyTypeChanges.add("3 - 4");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeChangesSocialGrouped(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("Regulated - RegulatedUO");
            tenancyTypeChanges.add("RegulatedUO - Regulated");
            tenancyTypeChanges.add("RegulatedUO - RegulatedUO");
        } else {
            tenancyTypeChanges.add("Regulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeChangesSocial(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            // Social
            tenancyTypeChanges.add("1UO - 1");
            tenancyTypeChanges.add("1 - 1UO");
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("4UO - 4");
            tenancyTypeChanges.add("4 - 4UO");
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("1 - 4UO");
            tenancyTypeChanges.add("1UO - 4");
            tenancyTypeChanges.add("1UO - 4UO");
            tenancyTypeChanges.add("4 - 1UO");
            tenancyTypeChanges.add("4UO - 1");
            tenancyTypeChanges.add("4UO - 1UO");
            result.add(tenancyTypeChanges);
            tenancyTypeChanges = new ArrayList<>();
            // Social To UO
            tenancyTypeChanges.add("1 - 1UO");
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("4 - 4UO");
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("1 - 4UO");
            tenancyTypeChanges.add("1UO - 4UO");
            tenancyTypeChanges.add("4 - 1UO");
            tenancyTypeChanges.add("4UO - 1UO");
            result.add(tenancyTypeChanges);
            tenancyTypeChanges = new ArrayList<>();
            // Social From UO
            tenancyTypeChanges.add("1UO - 1");
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("4UO - 4");
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("1UO - 4");
            tenancyTypeChanges.add("1UO - 4UO");
            tenancyTypeChanges.add("4UO - 1");
            tenancyTypeChanges.add("4UO - 1UO");
        } else {
            tenancyTypeChanges.add("1 - 1");
            tenancyTypeChanges.add("4 - 4");
            tenancyTypeChanges.add("1 - 4");
            tenancyTypeChanges.add("4 - 1");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeGroups(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("1UO - 1");
            tenancyTypeChanges.add("1 - 1UO");
        } else {
            tenancyTypeChanges.add("1 - 1");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("2UO - 2UO");
            tenancyTypeChanges.add("2UO - 2");
            tenancyTypeChanges.add("2 - 2UO");
        } else {
            tenancyTypeChanges.add("2 - 2");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 3UO");
            tenancyTypeChanges.add("3UO - 3");
            tenancyTypeChanges.add("3 - 3UO");
        } else {
            tenancyTypeChanges.add("3 - 3");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("4UO - 4");
            tenancyTypeChanges.add("4 - 4UO");
        } else {
            tenancyTypeChanges.add("4 - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("6UO - 6UO");
            tenancyTypeChanges.add("6UO - 6");
            tenancyTypeChanges.add("6 - 6UO");
        } else {
            tenancyTypeChanges.add("6 - 6");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("1UO - 1");
            tenancyTypeChanges.add("1 - 1UO");
            tenancyTypeChanges.add("2UO - 2UO");
            tenancyTypeChanges.add("2UO - 2");
            tenancyTypeChanges.add("2 - 2UO");
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("4UO - 4");
            tenancyTypeChanges.add("4 - 4UO");
        } else {
            tenancyTypeChanges.add("1 - 1");
            tenancyTypeChanges.add("2 - 2");
            tenancyTypeChanges.add("4 - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("1UO - 1UO");
            tenancyTypeChanges.add("1UO - 1");
            tenancyTypeChanges.add("1 - 1UO");
            tenancyTypeChanges.add("4UO - 4UO");
            tenancyTypeChanges.add("4UO - 4");
            tenancyTypeChanges.add("4 - 4UO");
        } else {
            tenancyTypeChanges.add("1 - 1");
            tenancyTypeChanges.add("4 - 4");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("3UO - 3UO");
            tenancyTypeChanges.add("3UO - 3");
            tenancyTypeChanges.add("3 - 3UO");
            tenancyTypeChanges.add("6UO - 6UO");
            tenancyTypeChanges.add("6UO - 6");
            tenancyTypeChanges.add("6 - 6UO");
        } else {
            tenancyTypeChanges.add("3 - 3");
            tenancyTypeChanges.add("6 - 6");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeChangesGrouped() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeChangesGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeChangesGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected HashMap<Boolean, ArrayList<ArrayList<String>>> getAllTenancyTypeGroupsGrouped() {
        HashMap<Boolean, ArrayList<ArrayList<String>>> result;
        result = new HashMap<>();
        boolean doUnderOccupancy;
        ArrayList<ArrayList<String>> attc;
        doUnderOccupancy = true;
        attc = getAllTenancyTypeGroupsGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        doUnderOccupancy = false;
        attc = getAllTenancyTypeGroupsGrouped(doUnderOccupancy);
        result.put(doUnderOccupancy, attc);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeGroupsGrouped(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("Regulated - RegulatedUO");
            tenancyTypeChanges.add("RegulatedUO - Regulated");
            tenancyTypeChanges.add("RegulatedUO - RegulatedUO");
        } else {
            tenancyTypeChanges.add("Regulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("UnregulatedUO - Unregulated");
            tenancyTypeChanges.add("Unregulated - UnregulatedUO");
            tenancyTypeChanges.add("UnregulatedUO - UnregulatedUO");
        } else {
            tenancyTypeChanges.add("Unregulated - Unregulated");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected ArrayList<ArrayList<String>> getAllTenancyTypeChangesGrouped(
            boolean doUnderOccupancy) {
        ArrayList<ArrayList<String>> result;
        result = new ArrayList<>();
        ArrayList<String> tenancyTypeChanges;
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("Regulated - RegulatedUO");
            tenancyTypeChanges.add("RegulatedUO - Regulated");
        } else {
            tenancyTypeChanges.add("Regulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("UnregulatedUO - Unregulated");
            tenancyTypeChanges.add("Unregulated - UnregulatedUO");
        } else {
            tenancyTypeChanges.add("Unregulated - Unregulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("RegulatedUO - Unregulated");
            tenancyTypeChanges.add("Regulated - UnregulatedUO");
        } else {
            tenancyTypeChanges.add("Regulated - Unregulated");
        }
        result.add(tenancyTypeChanges);
        tenancyTypeChanges = new ArrayList<>();
        if (doUnderOccupancy) {
            tenancyTypeChanges.add("UnregulatedUO - Regulated");
            tenancyTypeChanges.add("Unregulated - RegulatedUO");
        } else {
            tenancyTypeChanges.add("Unregulated - Regulated");
        }
        result.add(tenancyTypeChanges);
        return result;
    }

    protected TreeMap<String, ArrayList<ONSPD_YM3>> getYM3s(
            ArrayList<Integer> includes) {
        TreeMap<String, ArrayList<ONSPD_YM3>> result;
        result = new TreeMap<>();
        SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = Env.getSHBE_Handler();
        String[] tSHBEFilenamesAll;
        tSHBEFilenamesAll = tDW_SHBE_Handler.getSHBEFilenamesAll();
        ArrayList<ONSPD_YM3> yMs;
        // All
        Iterator<Integer> includesIte;
        includesIte = includes.iterator();
        yMs = new ArrayList<>();
        ONSPD_YM3 yM3 = null;
        boolean first = true;
        String name = "";
        int i;
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yM3 = tDW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
            if (first) {
                name = yM3.toString();
                first = false;
            }
            yMs.add(yM3);
        }
        name += "_TO_" + yM3;
        //result.put("All", yearMonths1);
        result.put(name, yMs);
        // Individual
        includesIte = includes.iterator();
        i = includesIte.next();
        ONSPD_YM3 yM30 = tDW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yMs = new ArrayList<>();
            ONSPD_YM3 yM31;
            yM31 = tDW_SHBE_Handler.getYM3(tSHBEFilenamesAll[i]);
            yMs.add(yM30);
            yMs.add(yM31);
//            result.put(yM, yearMonths1);
            result.put(yM30 + "_TO_" + yM31, yMs);
            yM30 = yM31;
        }
        return result;
    }

    public void run2 (
            boolean doUnderOccupancy,
            boolean doCouncil) throws Exception, Error {
        String filename;
        //filename = "PostcodeChanges_Start_2008April_End_2008October.csv";
        File dirIn = new File(
                Files.getOutputSHBETablesDir(),
                Strings.sTenancy);
        dirIn = new File(
                dirIn,
                Strings.sA);
        dirIn = new File(
                dirIn,
                Strings.sTenancyTypeTransition);
        dirIn = new File(
                dirIn,
                Strings.sCheckedPreviousTenancyType);
        dirIn = new File(
                dirIn,
                Strings.sTenancyAndPostcodeChanges);
        if (doUnderOccupancy) {
            dirIn = new File(
                    dirIn,
                    Strings.sU);
            if (doCouncil) {
                dirIn = new File(
                        dirIn,
                        Strings.sCouncil);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sRSL);
            }
        } else {
            dirIn = new File(
                    dirIn,
                    Strings.sA);
        }
        dirIn = new File(
                dirIn,
                Strings.sPostcodeChanged);
        dirIn = new File(
                dirIn,
                Strings.sA);
        dirIn = new File(
                dirIn,
                Strings.sGroupedNo);
        dirIn = new File(
                dirIn,
                Strings.sPostcodeChanges);
        dirIn = new File(
                dirIn,
                Strings.sCheckedPreviousPostcode);

        ArrayList<Integer> includes;
        //includes = SHBE_Handler.getSHBEFilenameIndexesExcept34();
        includes = Env.getSHBE_Handler().getSHBEFilenameIndexes();

        TreeMap<String, ArrayList<ONSPD_YM3>> yM3s;
        yM3s = getYM3s(includes);

        ArrayList<ArrayList<String>> allTenancyTypeChanges;
        allTenancyTypeChanges = getAllTenancyTypeChanges(doUnderOccupancy);

        Iterator<String> yM3sIte;
        yM3sIte = yM3s.keySet().iterator();
        while (yM3sIte.hasNext()) {
            String name;
            name = yM3sIte.next();
            System.out.println(name);
            ArrayList<ONSPD_YM3> yMs;
            yMs = yM3s.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<>();
            Iterator<ONSPD_YM3> yMsIte;
            yMsIte = yMs.iterator();
            ONSPD_YM3 yM0 = yMsIte.next();
            while (yMsIte.hasNext()) {
                ONSPD_YM3 yM1;
                yM1 = yMsIte.next();
                filename = "PostcodeChanges_Start_" + yM0 + "_End_" + yM1 + ".csv";
                File f;
                f = new File(
                        dirIn,
                        filename);
                //System.out.println("Load " + f);
                if (f.exists()) {
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                    yM0 = yM1;
                } else {
                    System.out.println("File " + f + " does not exist.");
                }
            }
            int size = lines.size();
            System.out.println("Total moves " + Integer.toString(size));

            // Totals for each tenancy type
            Iterator<ArrayList<String>> allTenancyTypeChangesIte;
            allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
            while (allTenancyTypeChangesIte.hasNext()) {
                ArrayList<String> tenancyTypeChanges;
                tenancyTypeChanges = allTenancyTypeChangesIte.next();
                int moves = 0;
                String tenancyTypeChangesName;
                tenancyTypeChangesName = getName(tenancyTypeChanges);
                Iterator<String> linesIte;
                linesIte = lines.iterator();
                while (linesIte.hasNext()) {
                    String line = linesIte.next();
                    boolean contains;
                    contains = false;
                    Iterator<String> tenancyTypeChangesIte;
                    tenancyTypeChangesIte = tenancyTypeChanges.iterator();
                    while (tenancyTypeChangesIte.hasNext()) {
                        String tenancyTypeChange;
                        tenancyTypeChange = tenancyTypeChangesIte.next();
                        if (line.contains(tenancyTypeChange)) {
                            contains = true;
                        }
                    }
                    if (contains) {
                        moves++;
                    }
                }
                System.out.println("Total " + tenancyTypeChangesName + " moves " + moves);
            }
        }
    }

    public void run() throws Exception, Error {
        // If showMapsInJMapPane == true then resulting maps are displayed on 
        // screen in a JMapPane otherwise maps are only written to file.
//        showMapsInJMapPane = true;
        showMapsInJMapPane = false;
        imageWidth = 2000;

        SHBE_Handler tDW_SHBE_Handler;
        tDW_SHBE_Handler = Env.getSHBE_Handler();

        ArrayList<String> paymentTypes;
        paymentTypes = Env.SHBE_Env.Strings.getPaymentTypes();
//        paymentTypes.remove(SHBE_Handler.sAllPT);
//        paymentTypes.remove(SHBE_Handler.sInPayment);
//        paymentTypes.remove(SHBE_Handler.sSuspended);
//        paymentTypes.remove(SHBE_Handler.sOtherPT);

        Iterator<String> paymentTypesIte;

        TreeMap<String, ArrayList<Integer>> includes;
        includes = tDW_SHBE_Handler.getIncludes();
//        includes.remove("All");
//        includes.remove("Yearly");
//        includes.remove("6Monthly");
//        includes.remove("3Monthly");
//        includes.remove("MonthlyUO");
//        includes.remove("Monthly");

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChanges;
        allTenancyTypeChanges = getAllTenancyTypeChanges();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeGroups;
        allTenancyTypeGroups = getAllTenancyTypeGroups();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesGrouped;
        allTenancyTypeChangesGrouped = getAllTenancyTypeChangesGrouped();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeGroupsGrouped;
        allTenancyTypeGroupsGrouped = getAllTenancyTypeGroupsGrouped();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesSocial;
        allTenancyTypeChangesSocial = getAllTenancyTypeChangesSocial();

        HashMap<Boolean, ArrayList<ArrayList<String>>> allTenancyTypeChangesSocialGrouped;
        allTenancyTypeChangesSocialGrouped = getAllTenancyTypeChangesSocialGrouped();

        ArrayList<Boolean> b;
        b = new ArrayList<>();
        b.add(true);
        b.add(false);

        boolean doCommunityAreasOverlay;
//        doCommunityAreasOverlay = true;
//        doCommunityAreasOverlay = false;
        Iterator<Boolean> iteCAO;
        iteCAO = b.iterator();
        while (iteCAO.hasNext()) {
            doCommunityAreasOverlay = iteCAO.next();

            init(doCommunityAreasOverlay);

            paymentTypesIte = paymentTypes.iterator();
            while (paymentTypesIte.hasNext()) {
                String paymentType;
                paymentType = paymentTypesIte.next();
                Iterator<String> includesIte;
                includesIte = includes.keySet().iterator();
                while (includesIte.hasNext()) {
                    String includeName;
                    includeName = includesIte.next();
                    ArrayList<Integer> include;
                    include = includes.get(includeName);

                    TreeMap<String, ArrayList<ONSPD_YM3>> yM3s;
                    yM3s = getYM3s(include);
                    // Postcode And Tenancy Type
                    if (true) {
//                    if (false) {
                        boolean doTenancyTypeAndPostcodeChange;
                        Iterator<Boolean> iteb0;
                        iteb0 = b.iterator();
                        while (iteb0.hasNext()) {
                            doTenancyTypeAndPostcodeChange = iteb0.next();
                            boolean doUnderOccupancyData;
//                        doUnderOccupancyData = false;
                            Iterator<Boolean> iteb;
                            iteb = b.iterator();
                            while (iteb.hasNext()) {
                                doUnderOccupancyData = iteb.next();
                                if (doUnderOccupancyData) {
                                    boolean doCouncil;
                                    doCouncil = true;
                                    boolean doRSL;
                                    doRSL = true;
                                    Iterator<Boolean> iteb1;
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        boolean doCheckedPreviousTenure;
                                        doCheckedPreviousTenure = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousPostcode;
                                            doCheckedPreviousPostcode = iteb2.next();
                                            boolean grouped;
                                            grouped = false;
//                                        Iterator<Boolean> iteb3;
//                                        iteb3 = b.iterator();
//                                        while (iteb3.hasNext()) {
//                                            grouped = iteb3.next();
                                            if (grouped) {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        doRSL,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        grouped);
                                            } else {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChanges.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        doRSL,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        grouped);
                                            }
                                            //}
                                        }
                                    }
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        doCouncil = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousTenure;
                                            doCheckedPreviousTenure = iteb2.next();
                                            Iterator<Boolean> iteb3;
                                            iteb3 = b.iterator();
                                            while (iteb3.hasNext()) {
                                                boolean doCheckedPreviousPostcode;
                                                doCheckedPreviousPostcode = iteb3.next();
                                                boolean doGrouped;
                                                doGrouped = false;
//                                                Iterator<Boolean> iteb4;
//                                            iteb4 = b.iterator();
//                                            while (iteb4.hasNext()) {
//                                                doGrouped = iteb4.next();
                                                if (doGrouped) {
                                                    postcodeMaps(
                                                            paymentType,
                                                            yM3s,
                                                            includeName,
                                                            include,
                                                            allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                            doTenancyTypeAndPostcodeChange,
                                                            doUnderOccupancyData,
                                                            doCouncil,
                                                            false,
                                                            doCheckedPreviousTenure,
                                                            doCheckedPreviousPostcode,
                                                            doGrouped);
                                                } else {
                                                    postcodeMaps(
                                                            paymentType,
                                                            yM3s,
                                                            includeName,
                                                            include,
                                                            allTenancyTypeChanges.get(doUnderOccupancyData),
                                                            doTenancyTypeAndPostcodeChange,
                                                            doUnderOccupancyData,
                                                            doCouncil,
                                                            false,
                                                            doCheckedPreviousTenure,
                                                            doCheckedPreviousPostcode,
                                                            doGrouped);
                                                }
                                                //}
                                            }
                                        }
                                    }
                                } else {
                                    Iterator<Boolean> iteb1;
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        boolean doCheckedPreviousTenure;
                                        doCheckedPreviousTenure = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousPostcode;
                                            doCheckedPreviousPostcode = iteb2.next();
                                            boolean doGrouped;
                                            doGrouped = false;
//                                            Iterator<Boolean> iteb3;
//                                        iteb3 = b.iterator();
//                                        while (iteb3.hasNext()) {
//                                            doGrouped = iteb3.next();
                                            if (doGrouped) {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesGrouped.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        false,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            } else {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChanges.get(doUnderOccupancyData),
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        false,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            }
                                            //}
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // Postcode Only Changes (does not includes postcode changes between different TTs)
                    if (true) {
//                    if (false) {
                        boolean doUnderOccupancyData;
//                    doUnderOccupancyData = false;
                        Iterator<Boolean> iteb;
                        iteb = b.iterator();
                        while (iteb.hasNext()) {
                            doUnderOccupancyData = iteb.next();
                            if (doUnderOccupancyData) {
                                boolean doCouncil;
                                doCouncil = true;
                                boolean doRSL;
                                doRSL = true;
                                Iterator<Boolean> iteb1;
                                iteb1 = b.iterator();
                                while (iteb1.hasNext()) {
                                    boolean doCheckedPreviousPostcode;
                                    doCheckedPreviousPostcode = iteb1.next();
                                    boolean grouped;
                                    grouped = false;
//                                    Iterator<Boolean> iteb2;
//                                iteb2 = b.iterator();
//                                while (iteb2.hasNext()) {
//                                    grouped = iteb2.next();
                                    if (grouped) {
                                        postcodeMaps(
                                                paymentType,
                                                yM3s,
                                                includeName,
                                                include,
                                                allTenancyTypeGroupsGrouped.get(doUnderOccupancyData),
                                                doUnderOccupancyData,
                                                doCouncil,
                                                doRSL,
                                                doCheckedPreviousPostcode,
                                                grouped);
                                    } else {
                                        postcodeMaps(
                                                paymentType,
                                                yM3s,
                                                includeName,
                                                include,
                                                allTenancyTypeGroups.get(doUnderOccupancyData),
                                                doUnderOccupancyData,
                                                doCouncil,
                                                doRSL,
                                                doCheckedPreviousPostcode,
                                                grouped);
                                    }
                                    //}
                                }
                                iteb1 = b.iterator();
                                while (iteb1.hasNext()) {
                                    doCouncil = iteb1.next();
                                    Iterator<Boolean> iteb2;
                                    iteb2 = b.iterator();
                                    while (iteb2.hasNext()) {
                                        boolean doCheckedPreviousPostcode;
                                        doCheckedPreviousPostcode = iteb2.next();
                                        boolean doGrouped;
                                        doGrouped = false;
//                                        Iterator<Boolean> iteb3;
//                                    iteb3 = b.iterator();
//                                    while (iteb3.hasNext()) {
//                                        doGrouped = iteb3.next();
                                        if (doGrouped) {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeGroupsGrouped.get(doUnderOccupancyData),
                                                    doUnderOccupancyData,
                                                    doCouncil,
                                                    false,
                                                    doCheckedPreviousPostcode,
                                                    doGrouped);
                                        } else {
                                            postcodeMaps(
                                                    paymentType,
                                                    yM3s,
                                                    includeName,
                                                    include,
                                                    allTenancyTypeGroups.get(doUnderOccupancyData),
                                                    doUnderOccupancyData,
                                                    doCouncil,
                                                    false,
                                                    doCheckedPreviousPostcode,
                                                    doGrouped);
                                        }
                                        //}
                                    }
                                }
                            } else {
                                Iterator<Boolean> iteb1;
                                iteb1 = b.iterator();
                                while (iteb1.hasNext()) {
                                    boolean doCheckedPreviousPostcode;
                                    doCheckedPreviousPostcode = iteb1.next();
                                    boolean doGrouped;
                                    doGrouped = false;
//                                    Iterator<Boolean> iteb2;
//                                iteb2 = b.iterator();
//                                while (iteb2.hasNext()) {
//                                    doGrouped = iteb2.next();
                                    if (doGrouped) {
                                        postcodeMaps(
                                                paymentType,
                                                yM3s,
                                                includeName,
                                                include,
                                                allTenancyTypeGroupsGrouped.get(doUnderOccupancyData),
                                                doUnderOccupancyData,
                                                false,
                                                false,
                                                doCheckedPreviousPostcode,
                                                doGrouped);
                                    } else {
                                        postcodeMaps(
                                                paymentType,
                                                yM3s,
                                                includeName,
                                                include,
                                                allTenancyTypeGroups.get(doUnderOccupancyData),
                                                doUnderOccupancyData,
                                                false,
                                                false,
                                                doCheckedPreviousPostcode,
                                                doGrouped);
                                    }
                                    //}
                                }
                            }
                        }
                    }
                    // Postcode changes all (includes changes within the same TT and between TTs)
                    if (true) {
//                if (false) {
                        boolean doTenancyTypeAndPostcodeChange;
                        Iterator<Boolean> iteb0;
                        iteb0 = b.iterator();
                        while (iteb0.hasNext()) {
                            doTenancyTypeAndPostcodeChange = iteb0.next();
                            boolean doUnderOccupancyData;
                            //doUnderOccupancyData = false;
                            Iterator<Boolean> iteb;
                            iteb = b.iterator();
                            while (iteb.hasNext()) {
                                doUnderOccupancyData = iteb.next();
                                if (doUnderOccupancyData) {
                                    boolean doCouncil;
                                    doCouncil = true;
                                    boolean doRSL;
                                    doRSL = true;
                                    Iterator<Boolean> iteb1;
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        boolean doCheckedPreviousTenure;
                                        doCheckedPreviousTenure = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousPostcode;
                                            doCheckedPreviousPostcode = iteb2.next();
                                            boolean grouped;
                                            grouped = false;
//                                            Iterator<Boolean> iteb3;
//                                        iteb3 = b.iterator();
//                                        while (iteb3.hasNext()) {
//                                            grouped = iteb3.next();
                                            if (grouped) {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesSocialGrouped.get(doUnderOccupancyData),
                                                        true,
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        doRSL,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        grouped);
                                            } else {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesSocial.get(doUnderOccupancyData),
                                                        true,
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        doCouncil,
                                                        doRSL,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        grouped);
                                            }
                                            //}
                                        }
                                    }
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        doCouncil = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousTenure;
                                            doCheckedPreviousTenure = iteb2.next();
                                            Iterator<Boolean> iteb3;
                                            iteb3 = b.iterator();
                                            while (iteb3.hasNext()) {
                                                boolean doCheckedPreviousPostcode;
                                                doCheckedPreviousPostcode = iteb3.next();
                                                boolean doGrouped;
                                                doGrouped = false;
//                                                Iterator<Boolean> iteb4;
//                                            iteb4 = b.iterator();
//                                            while (iteb4.hasNext()) {
//                                                doGrouped = iteb4.next();
                                                if (doGrouped) {
                                                    postcodeMaps(
                                                            paymentType,
                                                            yM3s,
                                                            includeName,
                                                            include,
                                                            allTenancyTypeChangesSocialGrouped.get(doUnderOccupancyData),
                                                            true,
                                                            doTenancyTypeAndPostcodeChange,
                                                            doUnderOccupancyData,
                                                            doCouncil,
                                                            false,
                                                            doCheckedPreviousTenure,
                                                            doCheckedPreviousPostcode,
                                                            doGrouped);
                                                } else {
                                                    postcodeMaps(
                                                            paymentType,
                                                            yM3s,
                                                            includeName,
                                                            include,
                                                            allTenancyTypeChangesSocial.get(doUnderOccupancyData),
                                                            true,
                                                            doTenancyTypeAndPostcodeChange,
                                                            doUnderOccupancyData,
                                                            doCouncil,
                                                            false,
                                                            doCheckedPreviousTenure,
                                                            doCheckedPreviousPostcode,
                                                            doGrouped);
                                                }
                                                //}
                                            }
                                        }
                                    }
                                } else {
                                    Iterator<Boolean> iteb1;
                                    iteb1 = b.iterator();
                                    while (iteb1.hasNext()) {
                                        boolean doCheckedPreviousTenure;
                                        doCheckedPreviousTenure = iteb1.next();
                                        Iterator<Boolean> iteb2;
                                        iteb2 = b.iterator();
                                        while (iteb2.hasNext()) {
                                            boolean doCheckedPreviousPostcode;
                                            doCheckedPreviousPostcode = iteb2.next();
                                            boolean doGrouped;
                                            doGrouped = false;
//                                            Iterator<Boolean> iteb3;
//                                        iteb3 = b.iterator();
//                                        while (iteb3.hasNext()) {
//                                            doGrouped = iteb3.next();
                                            if (doGrouped) {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesSocialGrouped.get(doUnderOccupancyData),
                                                        true,
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        false,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            } else {
                                                postcodeMaps(
                                                        paymentType,
                                                        yM3s,
                                                        includeName,
                                                        include,
                                                        allTenancyTypeChangesSocial.get(doUnderOccupancyData),
                                                        true,
                                                        doTenancyTypeAndPostcodeChange,
                                                        doUnderOccupancyData,
                                                        false,
                                                        false,
                                                        doCheckedPreviousTenure,
                                                        doCheckedPreviousPostcode,
                                                        doGrouped);
                                            }
                                            //}
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //}
        // Tidy up
        if (!showMapsInJMapPane) {
//            Iterator<AGDT_Shapefile> ite1;
//            ite1 = midgrounds.iterator();
//            while (ite1.hasNext()) {
//                Geotools_Shapefile s = ite1.next();
//                s.dispose();
//            }
            Iterator<Geotools_Shapefile> ite;
            ite = foregrounds.iterator();
            while (ite.hasNext()) {
                ite.next().dispose();
//                tLSOACodesAndLeedsLSOAShapefile.dispose();
//            tMSOACodesAndLeedsMSOAShapefile.dispose();
            }
        }
    }

    private void init(boolean doCommunityAreasOverlay) {
        initStyleParameters();
        foregrounds = new ArrayList<>();
        mapDirectory = Files.getOutputSHBELineMapsDir();
        if (doCommunityAreasOverlay) {
            mapDirectory = new File(
                    mapDirectory,
                    "CommunityAreasOverlaid");
            foregrounds.add(getCommunityAreasDW_Shapefile());
        } else {
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
            tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                    Env,
                    "LSOA", 
                    targetPropertyNameLSOA, 
                    getShapefileDataStoreFactory());
            foregrounds.add(tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile());
        }
        //midgrounds = new ArrayList<AGDT_Shapefile>();
        //backgrounds = new ArrayList<AGDT_Shapefile>();
        //initLSOACodesAndLeedsLSOAShapefile(targetPropertyNameLSOA);
//        ForegroundDW_Shapefile1 = tLSOACodesAndLeedsLSOAShapefile.getLeedsLADDW_Shapefile();
    }

    private void initStyleParameters() {
        styleParameters = new DW_StyleParameters();
        styleParameters.setForegroundStyle(
                Env.getGeotools().getStyle().createDefaultPolygonStyle(
                        Color.BLACK, //Color.GREEN,
                        Color.WHITE), 0);
        styleParameters.setMidgroundStyle(
                Geotools.getStyle().createDefaultLineStyle(Color.LIGHT_GRAY), 0);
    }

    private void setStyleParameters(
            int type,
            Color origin,
            Color destination) {
        if (type == 0) {
            styleParameters.setMidgroundStyle(
                    Geotools.getStyle().createDefaultLineStyle(origin), 1);
            styleParameters.setMidgroundStyle(
                    Style.createDefaultLineStyle(destination), 2);
        } else {
            styleParameters.setMidgroundStyle(
                    Style.createDefaultLineStyle(origin), 2);
            styleParameters.setMidgroundStyle(
                    Style.createDefaultLineStyle(destination), 1);

        }
    }

//    private void initLSOACodesAndLeedsLSOAShapefile(
//            String targetPropertyNameLSOA) {
//        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
//                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
//    }
    public void postcodeMaps(
            TreeMap<String, ArrayList<ONSPD_YM3>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doTenancyTypeAndPostcodeChange,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    "Tenancy");
            dirOut = new File(
                    mapDirectory,
                    Strings.sTenancyAndPostcodeChanges);
            dirIn = Files.getUODir(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = Files.getUODir(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyType);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyAndPostcodeChanges);
        } else {
            dirIn = new File(
                    mapDirectory,
                    Strings.sPostcodeChanges);
            dirOut = new File(
                    mapDirectory,
                    Strings.sPostcodeChanges);
            dirIn = Files.getUODir(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = Files.getUODir(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = new File(
                    dirIn,
                    Strings.sPostcodeChanged);
            dirIn = new File(
                    dirIn,
                    includeName);
            if (grouped) {
                dirIn = new File(
                        dirIn,
                        Strings.sGrouped);
                dirOut = new File(
                        dirOut,
                        Strings.sGrouped);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sGroupedNo);
                dirOut = new File(
                        dirOut,
                        Strings.sGroupedNo);
            }
            dirIn = new File(
                    dirIn,
                    Strings.sPostcodeChanges);
            if (doCheckedPreviousPostcode) {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousPostcode);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousPostcode);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousPostcodeNo);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousPostcodeNo);
            }
        }
        //dirOut.mkdirs(); // done later
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();

            if (name.equalsIgnoreCase("2013_April_TO_2015_September")) {

                System.out.println(name);
                ArrayList<ONSPD_YM3> yMs;
                yMs = yearMonths.get(name);
                ArrayList<String> lines;
                lines = new ArrayList<>();
                Iterator<ONSPD_YM3> yMsIte;
                yMsIte = yMs.iterator();
                ONSPD_YM3 yM30 = yMsIte.next();
                ONSPD_YM3 yM30Start = yM30;
                ONSPD_YM3 yM30End;
                while (yMsIte.hasNext()) {
                    ONSPD_YM3 yM31;
                    yM31 = yMsIte.next();
                    String filename;
                    filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                    File f;
                    f = new File(
                            dirIn,
                            filename);
                    if (f.exists()) {
                        //System.out.println("Load " + f);
                        ArrayList<String> linesPart;
                        linesPart = DW_Table.readCSV(f);
                        lines.addAll(linesPart);
                    }
                    yM30 = yM31;
                }
                yM30End = yM30;
                //System.out.println(filename + " lines " + lines.size());
                //int count = 0;
                if (!lines.isEmpty()) {
                    //name += "_TO_" + split1[0] + "_" + split1[1];
                    File dirOut2;
                    dirOut2 = new File(
                            dirOut,
                            name);
                    // Step 3.
                    SimpleFeatureType aLineSFT = DataUtilities.createType(
                            "LINE",
                            "the_geom:LineString," + "name:String," + "number:Integer");

                    // Totals for each tenancy type
                    Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                    allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
                    while (allTenancyTypeChangesIte.hasNext()) {
                        ArrayList<String> tenancyTypeChanges;
                        tenancyTypeChanges = allTenancyTypeChangesIte.next();

                        // Initialise a FeatureCollections and SimpleFeatureBuilders
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                        tsfcSs = new HashMap<>();
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                        tsfcMs = new HashMap<>();
                        HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                        tsfcEs = new HashMap<>();

                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                        sfbSs = new HashMap<>();
                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                        sfbMs = new HashMap<>();
                        HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                        sfbEs = new HashMap<>();

                        // Create SimpleFeatureBuilder
                        //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                        GeometryFactory geometryFactory = new GeometryFactory();

                        long count = 0;
                        long notPCCount = 0;

                        Iterator<String> linesIte;
                        linesIte = lines.iterator();
                        while (linesIte.hasNext()) {
                            String line;
                            line = linesIte.next();
                            String[] lineSplit;
                            lineSplit = line.split(",");
                            //String yM30;
                            yM30 = new ONSPD_YM3(lineSplit[1].trim());
                            ONSPD_YM3 yM31;
                            yM31 = new ONSPD_YM3(lineSplit[2].trim());
                            String tenancyTypeChange;
                            ONSPD_YM3 yM30v;
                            yM30v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                            ONSPD_YM3 yM31v;
                            yM31v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                            tenancyTypeChange = lineSplit[3].trim();
                            if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                                if (yMs.contains(yM31)) {
                                    String postcode0 = lineSplit[4].trim();
                                    String postcode1 = lineSplit[5].trim();
                                    ONSPD_Point origin;
                                    origin = Postcode_Handler.getPointFromPostcode(
                                            yM30v,
                                            Postcode_Handler.TYPE_UNIT,
                                            postcode0);
                                    ONSPD_Point destination;
                                    destination = Postcode_Handler.getPointFromPostcode(
                                            yM31v,
                                            Postcode_Handler.TYPE_UNIT,
                                            postcode1);
                                    TreeSetFeatureCollection tsfcS;
                                    tsfcS = tsfcSs.get(tenancyTypeChanges);
                                    if (tsfcS == null) {
                                        tsfcS = new TreeSetFeatureCollection();
                                        tsfcSs.put(tenancyTypeChanges, tsfcS);
                                    }
                                    SimpleFeatureBuilder sfbS;
                                    sfbS = sfbSs.get(tenancyTypeChanges);
                                    if (sfbS == null) {
                                        sfbS = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    TreeSetFeatureCollection tsfcM;
                                    tsfcM = tsfcMs.get(tenancyTypeChanges);
                                    if (tsfcM == null) {
                                        tsfcM = new TreeSetFeatureCollection();
                                        tsfcMs.put(tenancyTypeChanges, tsfcM);
                                    }
                                    SimpleFeatureBuilder sfbM;
                                    sfbM = sfbMs.get(tenancyTypeChanges);
                                    if (sfbM == null) {
                                        sfbM = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    TreeSetFeatureCollection tsfcE;
                                    tsfcE = tsfcEs.get(tenancyTypeChanges);
                                    if (tsfcE == null) {
                                        tsfcE = new TreeSetFeatureCollection();
                                        tsfcEs.put(tenancyTypeChanges, tsfcE);
                                    }
                                    SimpleFeatureBuilder sfbE;
                                    sfbE = sfbEs.get(tenancyTypeChanges);
                                    if (sfbE == null) {
                                        sfbE = new SimpleFeatureBuilder(aLineSFT);
                                    }
                                    if (origin != null && destination != null) {

                                        //count ++;
                                        double originx;
                                        double originy;
                                        originx = origin.getX();
                                        originy = origin.getY();
                                        double destinationx;
                                        double destinationy;
                                        destinationx = destination.getX();
                                        destinationy = destination.getY();

                                        double break1x;
                                        double break1y;
                                        break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                        break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                        double break2x;
                                        double break2y;
                                        break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                        break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                        Coordinate[] coords;
                                        LineString lineString;
                                        SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(originx, originy);
                                        coords[1] = new Coordinate(break1x, break1y);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbS.add(lineString);
                                        feature = sfbS.buildFeature(null);
                                        tsfcS.add(feature);

                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(break1x, break1y);
                                        coords[1] = new Coordinate(break2x, break2y);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbM.add(lineString);
                                        // Add attributes to line
//          sfb.add(outlet);
                                        feature = sfbM.buildFeature(null);
                                        tsfcM.add(feature);

                                        coords = new Coordinate[2];
                                        coords[0] = new Coordinate(break2x, break2y);
                                        coords[1] = new Coordinate(destinationx, destinationy);
                                        lineString = geometryFactory.createLineString(coords);
                                        sfbE.add(lineString);
                                        // Add attributes to line
//          sfb.add(outlet);
                                        feature = sfbE.buildFeature(null);
                                        tsfcE.add(feature);
                                        count++;

                                    } else {
                                        notPCCount++;
                                    }
                                }
                            }
                        }
                        String dates;
                        dates = getDatesString(yM30Start.toString(), yM30End.toString());
                        String legendMessage;
                        legendMessage = dates + " Count " + count;
                        System.out.println("line count = " + count);
                        System.out.println("Postcode not valid count = " + notPCCount);
                        TreeSetFeatureCollection tsfc;
                        name = getName(tenancyTypeChanges);
                        File dirOut3;
                        dirOut3 = new File(
                                dirOut2,
                                name);
                        String nameM;
                        nameM = name + "M";
                        tsfc = tsfcMs.get(tenancyTypeChanges);
                        if (tsfc != null) {
                            File f0 = Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameM);
                            DW_Shapefile.transact(
                                    f0,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());
                            String nameS;
                            nameS = name + "S";
                            tsfc = tsfcSs.get(tenancyTypeChanges);
                            File f1 = Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameS);
                            DW_Shapefile.transact(
                                    f1,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());
                            String nameE;
                            nameE = name + "E";
                            tsfc = tsfcEs.get(tenancyTypeChanges);
                            File f2 = Geotools.getOutputShapefile(
                                    dirOut3,
                                    nameE);
                            DW_Shapefile.transact(
                                    f2,
                                    aLineSFT,
                                    tsfc,
                                    getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                            for (int i = 0; i < 2; i++) {
                                String name2;
                                if (i == 0) {
                                    name2 = "DestinationOnTop";
                                    midgrounds = new ArrayList<>();
                                    midgrounds.add(new DW_Shapefile(f0));
                                    midgrounds.add(new DW_Shapefile(f1));
                                    midgrounds.add(new DW_Shapefile(f2));
                                } else {
                                    name2 = "OriginOnTop";
                                    midgrounds = new ArrayList<>();
                                    midgrounds.add(new DW_Shapefile(f0));
                                    midgrounds.add(new DW_Shapefile(f2));
                                    midgrounds.add(new DW_Shapefile(f1));
                                }
                                System.out.println(name2);
                                File dirOut4;
                                dirOut4 = new File(
                                        dirOut3,
                                        name2);
                                dirOut4.mkdirs();
                                setStyleParameters(
                                        i,
                                        Color.RED,
                                        Color.BLUE);
                                Geotools.outputToImage(
                                        legendMessage,
                                        name,
                                        midgrounds,
                                        foregrounds,
                                        dirOut4,
                                        _Geotools_Strings.png_String,
                                        imageWidth,
                                        styleParameters,
                                        0,
                                        Double.POSITIVE_INFINITY,
                                        showMapsInJMapPane);
                            }
                        } else {
                            System.out.println("No lines to visualise1");
                        }
                    }
                }
                //System.out.println("count " + count);

            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    private String getDatesString(
            String yM30Start,
            String yM30End) {
        String result;
        String[] split;
        split = yM30Start.split("_");
        result = split[1] + " " + split[0] + " to ";
        split = yM30End.split("_");
        result += split[1] + " " + split[0];
        return result;
    }

    public void postcodeMaps(
            String paymentType,
            TreeMap<String, ArrayList<ONSPD_YM3>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doTenancyTypeAndPostcodeChange,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doRSL,
            boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirInCouncil;
        File dirInRSL;
        File dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    Strings.sTenancy);
            dirIn = new File(
                    dirIn,
                    paymentType);
            dirIn = new File(
                    dirIn,
                    Strings.sA);
            dirOut = new File(
                    mapDirectory,
                    paymentType);
            dirOut = new File(
                    dirOut,
                    Strings.sTenancyAndPostcodeChanges);
            dirOut = Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyType);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyAndPostcodeChanges);
            dirInCouncil = Files.getUODir(dirIn, doUnderOccupancyData, true);
            dirInRSL = Files.getUODir(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        } else {
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    Strings.sPostcodeChanges);
            dirOut = new File(
                    mapDirectory,
                    paymentType);
            dirOut = new File(
                    dirOut,
                    Strings.sPostcodeChanges);
            dirOut = Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirInCouncil = Files.getUODir(dirIn, doUnderOccupancyData, true);
            dirInRSL = Files.getUODir(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        }

//        // <Hack>
//        dirInCouncil = new File(
//                dirInCouncil,
//                Strings.sAll);
//        dirInRSL = new File(
//                dirInRSL,
//                Strings.sAll);
//        // </Hack>

        if (grouped) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGrouped);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGrouped);
            dirOut = new File(
                    dirOut,
                    Strings.sGrouped);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGroupedNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGroupedNo);
            dirOut = new File(
                    dirOut,
                    Strings.sGroupedNo);
        }
        dirInCouncil = new File(
                dirInCouncil,
                Strings.sPostcodeChanges);
        dirInRSL = new File(
                dirInRSL,
                Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcode);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcode);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcodeNo);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<ONSPD_YM3> yM3s;
            yM3s = yearMonths.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<>();
            Iterator<ONSPD_YM3> yMsIte;
            yMsIte = yM3s.iterator();
            ONSPD_YM3 yM30 = yMsIte.next();
            ONSPD_YM3 yM30Start = yM30;
            ONSPD_YM3 yM30End;
            while (yMsIte.hasNext()) {
                ONSPD_YM3 yM31;
                yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                File f;
                f = new File(
                        dirInCouncil,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                } else {
                    int debug = 1;
                }
                f = new File(
                        dirInRSL,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                File dirOut2;
                dirOut2 = new File(
                        dirOut,
                        name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
                while (allTenancyTypeChangesIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges;
                    tenancyTypeChanges = allTenancyTypeChangesIte.next();

                    // Initialise a FeatureCollections and SimpleFeatureBuilders
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                    tsfcSs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                    tsfcMs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                    tsfcEs = new HashMap<>();

                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                    sfbSs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                    sfbMs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                    sfbEs = new HashMap<>();

                    // Create SimpleFeatureBuilder
                    //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                    GeometryFactory geometryFactory = new GeometryFactory();

                    long count = 0;
                    long notPCCount = 0;

                    Iterator<String> linesIte;
                    linesIte = lines.iterator();
                    while (linesIte.hasNext()) {
                        String line;
                        line = linesIte.next();
                        String[] lineSplit;
                        lineSplit = line.split(",");
                        yM30 = new ONSPD_YM3(lineSplit[1].trim());
                        ONSPD_YM3 yM31;
                        yM31 = new ONSPD_YM3(lineSplit[2].trim());
                        ONSPD_YM3 yM30v;
                        yM30v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                        ONSPD_YM3 yM31v;
                        yM31v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange;
                        tenancyTypeChange = lineSplit[3].trim();

//                        String ttc;
//                        if (tenancyTypeChange.contains("P")) {
//                            int debug = 1;
//                        }
                        if (tenancyTypeChange.endsWith("4")) { // This is a horrible hack!
//                        if (tenancyTypeChange.endsWith("1") || tenancyTypeChange.endsWith("4")) { // This is a horrible hack!
//                        if (tenancyTypeChange.endsWith("3") || tenancyTypeChange.endsWith("6")) {
//                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                ONSPD_Point origin;
                                origin = Postcode_Handler.getPointFromPostcode(
                                        yM30v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode0);
                                ONSPD_Point destination;
                                destination = Postcode_Handler.getPointFromPostcode(
                                        yM31v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode1);
                                TreeSetFeatureCollection tsfcS;
                                tsfcS = tsfcSs.get(tenancyTypeChanges);
                                if (tsfcS == null) {
                                    tsfcS = new TreeSetFeatureCollection();
                                    tsfcSs.put(tenancyTypeChanges, tsfcS);
                                }
                                SimpleFeatureBuilder sfbS;
                                sfbS = sfbSs.get(tenancyTypeChanges);
                                if (sfbS == null) {
                                    sfbS = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcM;
                                tsfcM = tsfcMs.get(tenancyTypeChanges);
                                if (tsfcM == null) {
                                    tsfcM = new TreeSetFeatureCollection();
                                    tsfcMs.put(tenancyTypeChanges, tsfcM);
                                }
                                SimpleFeatureBuilder sfbM;
                                sfbM = sfbMs.get(tenancyTypeChanges);
                                if (sfbM == null) {
                                    sfbM = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcE;
                                tsfcE = tsfcEs.get(tenancyTypeChanges);
                                if (tsfcE == null) {
                                    tsfcE = new TreeSetFeatureCollection();
                                    tsfcEs.put(tenancyTypeChanges, tsfcE);
                                }
                                SimpleFeatureBuilder sfbE;
                                sfbE = sfbEs.get(tenancyTypeChanges);
                                if (sfbE == null) {
                                    sfbE = new SimpleFeatureBuilder(aLineSFT);
                                }
                                if (origin != null && destination != null) {

                                    double originx;
                                    double originy;
                                    originx = origin.getX();
                                    originy = origin.getY();
                                    double destinationx;
                                    double destinationy;
                                    destinationx = destination.getX();
                                    destinationy = destination.getY();

                                    double break1x;
                                    double break1y;
                                    break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                    double break2x;
                                    double break2y;
                                    break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                    Coordinate[] coords;
                                    LineString lineString;
                                    SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(originx, originy);
                                    coords[1] = new Coordinate(break1x, break1y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbS.add(lineString);
                                    feature = sfbS.buildFeature(null);
                                    tsfcS.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break1x, break1y);
                                    coords[1] = new Coordinate(break2x, break2y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbM.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbM.buildFeature(null);
                                    tsfcM.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break2x, break2y);
                                    coords[1] = new Coordinate(destinationx, destinationy);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbE.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbE.buildFeature(null);
                                    tsfcE.add(feature);

                                    count++;

                                } else {
                                    notPCCount++;
                                }
                            }
                        }
                    }
                    String dates;
                    dates = getDatesString(yM30Start.toString(), yM30End.toString());
                    String legendMessage;
                    legendMessage = dates + " Count " + count;
                    System.out.println("line count = " + count);
                    System.out.println("Postcode not valid count = " + notPCCount);
                    TreeSetFeatureCollection tsfc;
                    name = getName(tenancyTypeChanges);
                    File dirOut3;
                    dirOut3 = new File(
                            dirOut2,
                            name);
                    String nameM;
                    nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        File f0 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameM);
                        DW_Shapefile.transact(
                                f0,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameS;
                        nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        File f1 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameS);
                        DW_Shapefile.transact(
                                f1,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameE;
                        nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        File f2 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameE);
                        DW_Shapefile.transact(
                                f2,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                        for (int i = 0; i < 2; i++) {
                            String name2;
                            if (i == 0) {
                                name2 = "DestinationOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f1));
                                midgrounds.add(new DW_Shapefile(f2));
                            } else {
                                name2 = "OriginOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f2));
                                midgrounds.add(new DW_Shapefile(f1));
                            }
                            System.out.println(name2);
                            File dirOut4;
                            dirOut4 = new File(
                                    dirOut3,
                                    name2);
                            dirOut4.mkdirs();
                            setStyleParameters(
                                    i,
                                    Color.RED,
                                    Color.BLUE);
                            Geotools.outputToImage(
                                    legendMessage,
                                    name,
                                    midgrounds,
                                    foregrounds,
                                    dirOut4,
                                    _Geotools_Strings.png_String,
                                    imageWidth,
                                    styleParameters,
                                    0,
                                    Double.POSITIVE_INFINITY,
                                    showMapsInJMapPane);
                        }
                    } else {
                        System.out.println("No lines to visualise1");
                    }
                }
            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    public void postcodeMaps(
            String paymentType,
            TreeMap<String, ArrayList<ONSPD_YM3>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean ignore_JustForDistinguishingThisMethodFlag,
            boolean doTenancyTypeAndPostcodeChange,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doRSL,
            boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirInCouncil;
        File dirInRSL;
        File dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    Strings.sTenancy);
            dirIn = new File(
                    dirIn,
                    paymentType);
            dirIn = new File(
                    dirIn,
                    Strings.sA);
            dirOut = new File(
                    mapDirectory,
                    paymentType);
            dirOut = new File(
                    dirOut,
                    Strings.sA + Strings.sPostcodeChanges);
            dirOut = Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyType);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = new File(
                        dirIn,
                        Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = new File(
                        dirOut,
                        Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = new File(
                    dirIn,
                    Strings.sTenancyAndPostcodeChanges);
            dirInCouncil = Files.getUODir(dirIn, doUnderOccupancyData, true);
            dirInRSL = Files.getUODir(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        } else {
            dirIn = new File(
                    Files.getOutputSHBETablesDir(),
                    Strings.sPostcodeChanges);
            dirOut = new File(
                    mapDirectory,
                    paymentType);
            dirOut = new File(
                    dirOut,
                    Strings.sPostcodeChanges);
            dirOut = Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            dirInCouncil = Files.getUODir(dirIn, doUnderOccupancyData, true);
            dirInRSL = Files.getUODir(dirIn, doUnderOccupancyData, false);
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sPostcodeChanged);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sPostcodeChanged);
            dirInCouncil = new File(
                    dirInCouncil,
                    includeName);
            dirInRSL = new File(
                    dirInRSL,
                    includeName);
        }
        if (grouped) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGrouped);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGrouped);
            dirOut = new File(
                    dirOut,
                    Strings.sGrouped);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGroupedNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGroupedNo);
            dirOut = new File(
                    dirOut,
                    Strings.sGroupedNo);
        }
        dirInCouncil = new File(
                dirInCouncil,
                Strings.sPostcodeChanges);
        dirInRSL = new File(
                dirInRSL,
                Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcode);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcode);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcodeNo);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcodeNo);
        }
        File dirIn2;
        File dirInCouncil2;
        File dirInRSL2;
        dirIn2 = new File(
                Files.getOutputSHBETablesDir(),
                Strings.sPostcodeChanges);
        dirIn2 = new File(
                dirIn2,
                paymentType);
        dirInCouncil2 = Files.getUODir(dirIn2, doUnderOccupancyData, true);
        dirInRSL2 = Files.getUODir(dirIn2, doUnderOccupancyData, false);
        dirInCouncil2 = new File(
                dirInCouncil2,
                Strings.sPostcodeChanged);
        dirInRSL2 = new File(
                dirInRSL2,
                Strings.sPostcodeChanged);
        dirInCouncil2 = new File(
                dirInCouncil2,
                includeName);
        dirInRSL2 = new File(
                dirInRSL2,
                includeName);
        if (grouped) {
            dirInCouncil2 = new File(
                    dirInCouncil2,
                    Strings.sGrouped);
            dirInRSL2 = new File(
                    dirInRSL2,
                    Strings.sGrouped);
        } else {
            dirInCouncil2 = new File(
                    dirInCouncil2,
                    Strings.sGroupedNo);
            dirInRSL2 = new File(
                    dirInRSL2,
                    Strings.sGroupedNo);
        }
        dirInCouncil2 = new File(
                dirInCouncil2,
                Strings.sPostcodeChanges);
        dirInRSL2 = new File(
                dirInRSL2,
                Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil2 = new File(
                    dirInCouncil2,
                    Strings.sCheckedPreviousPostcode);
            dirInRSL2 = new File(
                    dirInRSL2,
                    Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil2 = new File(
                    dirInCouncil2,
                    Strings.sCheckedPreviousPostcodeNo);
            dirInRSL2 = new File(
                    dirInRSL2,
                    Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<ONSPD_YM3> yM3s;
            yM3s = yearMonths.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<>();
            Iterator<ONSPD_YM3> yMsIte;
            yMsIte = yM3s.iterator();
            ONSPD_YM3 yM30 = yMsIte.next();
            ONSPD_YM3 yM30Start = yM30;
            ONSPD_YM3 yM30End;
            while (yMsIte.hasNext()) {
                ONSPD_YM3 yM31;
                yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                File f;
                f = new File(
                        dirInCouncil,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                f = new File(
                        dirInRSL,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                f = new File(
                        dirInCouncil2,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                f = new File(
                        dirInRSL2,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                File dirOut2;
                dirOut2 = new File(
                        dirOut,
                        name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                allTenancyTypeChangesIte = allTenancyTypeChanges.iterator();
                while (allTenancyTypeChangesIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges;
                    tenancyTypeChanges = allTenancyTypeChangesIte.next();

                    // Initialise a FeatureCollections and SimpleFeatureBuilders
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                    tsfcSs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                    tsfcMs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                    tsfcEs = new HashMap<>();

                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                    sfbSs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                    sfbMs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                    sfbEs = new HashMap<>();

                    // Create SimpleFeatureBuilder
                    //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                    GeometryFactory geometryFactory = new GeometryFactory();

                    long count = 0;
                    long notPCCount = 0;

                    Iterator<String> linesIte;
                    linesIte = lines.iterator();
                    while (linesIte.hasNext()) {
                        String line;
                        line = linesIte.next();
                        String[] lineSplit;
                        lineSplit = line.split(",");
                        yM30 = new ONSPD_YM3(lineSplit[1].trim());
                        ONSPD_YM3 yM31;
                        yM31 = new ONSPD_YM3(lineSplit[2].trim());
                        ONSPD_YM3 yM30v;
                        yM30v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                        ONSPD_YM3 yM31v;
                        yM31v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange;
                        tenancyTypeChange = lineSplit[3].trim();
                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                ONSPD_Point origin;
                                origin = Postcode_Handler.getPointFromPostcode(
                                        yM30v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode0);
                                ONSPD_Point destination;
                                destination = Postcode_Handler.getPointFromPostcode(
                                        yM31v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode1);
                                TreeSetFeatureCollection tsfcS;
                                tsfcS = tsfcSs.get(tenancyTypeChanges);
                                if (tsfcS == null) {
                                    tsfcS = new TreeSetFeatureCollection();
                                    tsfcSs.put(tenancyTypeChanges, tsfcS);
                                }
                                SimpleFeatureBuilder sfbS;
                                sfbS = sfbSs.get(tenancyTypeChanges);
                                if (sfbS == null) {
                                    sfbS = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcM;
                                tsfcM = tsfcMs.get(tenancyTypeChanges);
                                if (tsfcM == null) {
                                    tsfcM = new TreeSetFeatureCollection();
                                    tsfcMs.put(tenancyTypeChanges, tsfcM);
                                }
                                SimpleFeatureBuilder sfbM;
                                sfbM = sfbMs.get(tenancyTypeChanges);
                                if (sfbM == null) {
                                    sfbM = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcE;
                                tsfcE = tsfcEs.get(tenancyTypeChanges);
                                if (tsfcE == null) {
                                    tsfcE = new TreeSetFeatureCollection();
                                    tsfcEs.put(tenancyTypeChanges, tsfcE);
                                }
                                SimpleFeatureBuilder sfbE;
                                sfbE = sfbEs.get(tenancyTypeChanges);
                                if (sfbE == null) {
                                    sfbE = new SimpleFeatureBuilder(aLineSFT);
                                }
                                if (origin != null && destination != null) {

                                    double originx;
                                    double originy;
                                    originx = origin.getX();
                                    originy = origin.getY();
                                    double destinationx;
                                    double destinationy;
                                    destinationx = destination.getX();
                                    destinationy = destination.getY();

                                    double break1x;
                                    double break1y;
                                    break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                    double break2x;
                                    double break2y;
                                    break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                    Coordinate[] coords;
                                    LineString lineString;
                                    SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(originx, originy);
                                    coords[1] = new Coordinate(break1x, break1y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbS.add(lineString);
                                    feature = sfbS.buildFeature(null);
                                    tsfcS.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break1x, break1y);
                                    coords[1] = new Coordinate(break2x, break2y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbM.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbM.buildFeature(null);
                                    tsfcM.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break2x, break2y);
                                    coords[1] = new Coordinate(destinationx, destinationy);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbE.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbE.buildFeature(null);
                                    tsfcE.add(feature);

                                    count++;

                                } else {
                                    notPCCount++;
                                }
                            }
                        }
                    }
                    String dates;
                    dates = getDatesString(yM30Start.toString(), yM30End.toString());
                    String legendMessage;
                    legendMessage = dates + " Count " + count;
                    System.out.println("line count = " + count);
                    System.out.println("Postcode not valid count = " + notPCCount);
                    TreeSetFeatureCollection tsfc;
                    name = getName(tenancyTypeChanges);
                    File dirOut3;
                    dirOut3 = new File(
                            dirOut2,
                            name);
                    String nameM;
                    nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        File f0 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameM);
                        DW_Shapefile.transact(
                                f0,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameS;
                        nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        File f1 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameS);
                        DW_Shapefile.transact(
                                f1,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameE;
                        nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        File f2 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameE);
                        DW_Shapefile.transact(
                                f2,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                        for (int i = 0; i < 2; i++) {
                            String name2;
                            if (i == 0) {
                                name2 = "DestinationOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f1));
                                midgrounds.add(new DW_Shapefile(f2));
                            } else {
                                name2 = "OriginOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f2));
                                midgrounds.add(new DW_Shapefile(f1));
                            }
                            System.out.println(name2);
                            File dirOut4;
                            dirOut4 = new File(
                                    dirOut3,
                                    name2);
                            dirOut4.mkdirs();
                            setStyleParameters(
                                    i,
                                    Color.RED,
                                    Color.BLUE);
                            Geotools.outputToImage(
                                    legendMessage,
                                    name,
                                    midgrounds,
                                    foregrounds,
                                    dirOut4,
                                    Geotools.getGeotools_Strings().png_String,
                                    imageWidth,
                                    styleParameters,
                                    0,
                                    Double.POSITIVE_INFINITY,
                                    showMapsInJMapPane);
                        }
                    } else {
                        System.out.println("No lines to visualise1");
                    }
                }
            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    /**
     * For changes within the same TT.
     *
     * @param paymentType
     * @param yearMonths
     * @param includeName
     * @param include
     * @param allTenancyTypeGroups (This can be grouped i.e. 1 - 1 and 4 - 4 in
     * the same. Not really changes, but classes)
     * @param doUnderOccupancyData
     * @param doCouncil
     * @param doRSL
     * @param doCheckedPreviousPostcode
     * @param grouped
     * @throws Exception
     */
    public void postcodeMaps(
            String paymentType,
            TreeMap<String, ArrayList<ONSPD_YM3>> yearMonths,
            String includeName,
            ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeGroups,
            boolean doUnderOccupancyData,
            boolean doCouncil,
            boolean doRSL,
            boolean doCheckedPreviousPostcode,
            boolean grouped) throws Exception {
        File dirIn;
        File dirInCouncil;
        File dirInRSL;
        File dirOut;
        dirIn = new File(
                Files.getOutputSHBETablesDir(),
                Strings.sPostcodeChanges);
        dirIn = new File(
                dirIn,
                paymentType);
        dirOut = new File(
                mapDirectory,
                paymentType);
        dirOut = new File(
                dirOut,
                Strings.sPostcodeChanges);
        dirOut = Files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
        //dirIn = Files.getUODir(dirIn, doUnderOccupancyData, doCouncil, doRSL);
        dirInCouncil = Files.getUODir(dirIn, doUnderOccupancyData, true);
        dirInRSL = Files.getUODir(dirIn, doUnderOccupancyData, false);
        dirInCouncil = new File(
                dirInCouncil,
                Strings.sPostcodeChanged);
        dirInRSL = new File(
                dirInRSL,
                Strings.sPostcodeChanged);
        dirInCouncil = new File(
                dirInCouncil,
                includeName);
        dirInRSL = new File(
                dirInRSL,
                includeName);
        if (grouped) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGrouped);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGrouped);
            dirOut = new File(
                    dirOut,
                    Strings.sGrouped);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sGroupedNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sGroupedNo);
            dirOut = new File(
                    dirOut,
                    Strings.sGroupedNo);
        }
        dirInCouncil = new File(
                dirInCouncil,
                Strings.sPostcodeChanges);
        dirInRSL = new File(
                dirInRSL,
                Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcode);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcode);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = new File(
                    dirInCouncil,
                    Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = new File(
                    dirInRSL,
                    Strings.sCheckedPreviousPostcodeNo);
            dirOut = new File(
                    dirOut,
                    Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name;
            name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<ONSPD_YM3> yM3s;
            yM3s = yearMonths.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<>();
            Iterator<ONSPD_YM3> yMsIte;
            yMsIte = yM3s.iterator();
            ONSPD_YM3 yM30 = yMsIte.next();
            ONSPD_YM3 yM30Start = yM30;
            ONSPD_YM3 yM30End;
            while (yMsIte.hasNext()) {
                ONSPD_YM3 yM31;
                yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                File f;
                f = new File(
                        dirInCouncil,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                f = new File(
                        dirInRSL,
                        filename);
                if (f.exists()) {
                    //System.out.println("Load " + f);
                    ArrayList<String> linesPart;
                    linesPart = DW_Table.readCSV(f);
                    lines.addAll(linesPart);
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                File dirOut2;
                dirOut2 = new File(
                        dirOut,
                        name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> allTenancyTypeChangesIte;
                allTenancyTypeChangesIte = allTenancyTypeGroups.iterator();
                while (allTenancyTypeChangesIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges;
                    tenancyTypeChanges = allTenancyTypeChangesIte.next();

                    // Initialise a FeatureCollections and SimpleFeatureBuilders
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs;
                    tsfcSs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs;
                    tsfcMs = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs;
                    tsfcEs = new HashMap<>();

                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs;
                    sfbSs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs;
                    sfbMs = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs;
                    sfbEs = new HashMap<>();

                    // Create SimpleFeatureBuilder
                    //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                    GeometryFactory geometryFactory = new GeometryFactory();

                    long count = 0;
                    long notPCCount = 0;

                    Iterator<String> linesIte;
                    linesIte = lines.iterator();
                    while (linesIte.hasNext()) {
                        String line;
                        line = linesIte.next();
                        String[] lineSplit;
                        lineSplit = line.split(",");
                        yM30 = new ONSPD_YM3(lineSplit[1].trim());
                        ONSPD_YM3 yM31;
                        yM31 = new ONSPD_YM3(lineSplit[2].trim());
                        ONSPD_YM3 yM30v;
                        yM30v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM30);
                        ONSPD_YM3 yM31v;
                        yM31v = Postcode_Handler.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange;
                        tenancyTypeChange = lineSplit[3].trim();
                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                ONSPD_Point origin;
                                origin = Postcode_Handler.getPointFromPostcode(
                                        yM30v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode0);
                                ONSPD_Point destination;
                                destination = Postcode_Handler.getPointFromPostcode(
                                        yM31v,
                                        Postcode_Handler.TYPE_UNIT,
                                        postcode1);
                                TreeSetFeatureCollection tsfcS;
                                tsfcS = tsfcSs.get(tenancyTypeChanges);
                                if (tsfcS == null) {
                                    tsfcS = new TreeSetFeatureCollection();
                                    tsfcSs.put(tenancyTypeChanges, tsfcS);
                                }
                                SimpleFeatureBuilder sfbS;
                                sfbS = sfbSs.get(tenancyTypeChanges);
                                if (sfbS == null) {
                                    sfbS = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcM;
                                tsfcM = tsfcMs.get(tenancyTypeChanges);
                                if (tsfcM == null) {
                                    tsfcM = new TreeSetFeatureCollection();
                                    tsfcMs.put(tenancyTypeChanges, tsfcM);
                                }
                                SimpleFeatureBuilder sfbM;
                                sfbM = sfbMs.get(tenancyTypeChanges);
                                if (sfbM == null) {
                                    sfbM = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcE;
                                tsfcE = tsfcEs.get(tenancyTypeChanges);
                                if (tsfcE == null) {
                                    tsfcE = new TreeSetFeatureCollection();
                                    tsfcEs.put(tenancyTypeChanges, tsfcE);
                                }
                                SimpleFeatureBuilder sfbE;
                                sfbE = sfbEs.get(tenancyTypeChanges);
                                if (sfbE == null) {
                                    sfbE = new SimpleFeatureBuilder(aLineSFT);
                                }
                                if (origin != null && destination != null) {

                                    double originx;
                                    double originy;
                                    originx = origin.getX();
                                    originy = origin.getY();
                                    double destinationx;
                                    double destinationy;
                                    destinationx = destination.getX();
                                    destinationy = destination.getY();

                                    double break1x;
                                    double break1y;
                                    break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;

                                    double break2x;
                                    double break2y;
                                    break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break2x = (originx + destinationx) / 2.0d;
//                        break2y = (originy + destinationy) / 2.0d;

//                        double midpointx;
//                        double midpointy;
//                        midpointx = (originx + destinationx) / 2.0d;
//                        midpointy = (originy + destinationy) / 2.0d;
                                    Coordinate[] coords;
                                    LineString lineString;
                                    SimpleFeature feature;

//                        coords = new Coordinate[2];
//                        coords[0] = new Coordinate(originx, originy);
//                        coords[1] = new Coordinate(destinationx, destinationy);
//                        lineString = geometryFactory.createLineString(coords);
//                        sfbOM.add(lineString);
////                        // Add attributes to line
////          sfb.add(outlet);
//                        feature = sfbOM.buildFeature(null);
//                        tsfcOM.add(feature);
                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(originx, originy);
                                    coords[1] = new Coordinate(break1x, break1y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbS.add(lineString);
                                    feature = sfbS.buildFeature(null);
                                    tsfcS.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break1x, break1y);
                                    coords[1] = new Coordinate(break2x, break2y);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbM.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbM.buildFeature(null);
                                    tsfcM.add(feature);

                                    coords = new Coordinate[2];
                                    coords[0] = new Coordinate(break2x, break2y);
                                    coords[1] = new Coordinate(destinationx, destinationy);
                                    lineString = geometryFactory.createLineString(coords);
                                    sfbE.add(lineString);
                                    // Add attributes to line
//          sfb.add(outlet);
                                    feature = sfbE.buildFeature(null);
                                    tsfcE.add(feature);

                                    count++;

                                } else {
                                    notPCCount++;
                                }
                            }
                        }
                    }
                    String dates;
                    dates = getDatesString(yM30Start.toString(), yM30End.toString());
                    String legendMessage;
                    legendMessage = dates + " Count " + count;
                    System.out.println("line count = " + count);
                    System.out.println("Postcode not valid count = " + notPCCount);
                    TreeSetFeatureCollection tsfc;
                    name = getName(tenancyTypeChanges);
                    File dirOut3;
                    dirOut3 = new File(
                            dirOut2,
                            name);
                    String nameM;
                    nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        File f0 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameM);
                        DW_Shapefile.transact(
                                f0,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameS;
                        nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        File f1 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameS);
                        DW_Shapefile.transact(
                                f1,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());
                        String nameE;
                        nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        File f2 = Geotools.getOutputShapefile(
                                dirOut3,
                                nameE);
                        DW_Shapefile.transact(
                                f2,
                                aLineSFT,
                                tsfc,
                                getShapefileDataStoreFactory());

//            // Had Hoped to getting away with doing this once
//            FeatureLayer backgroundFeatureLayer = DW_Shapefile.getFeatureLayer(
//                backgroundShapefile,
//                backgroundStyle);
                        for (int i = 0; i < 2; i++) {
                            String name2;
                            if (i == 0) {
                                name2 = "DestinationOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f1));
                                midgrounds.add(new DW_Shapefile(f2));
                            } else {
                                name2 = "OriginOnTop";
                                midgrounds = new ArrayList<>();
                                midgrounds.add(new DW_Shapefile(f0));
                                midgrounds.add(new DW_Shapefile(f2));
                                midgrounds.add(new DW_Shapefile(f1));
                            }
                            System.out.println(name2);
                            File dirOut4;
                            dirOut4 = new File(
                                    dirOut3,
                                    name2);
                            dirOut4.mkdirs();
                            setStyleParameters(
                                    i,
                                    Color.RED,
                                    Color.BLUE);
                            Geotools.outputToImage(
                                    legendMessage,
                                    name,
                                    midgrounds,
                                    foregrounds,
                                    dirOut4,
                                    _Geotools_Strings.png_String,
                                    imageWidth,
                                    styleParameters,
                                    0,
                                    Double.POSITIVE_INFINITY,
                                    showMapsInJMapPane);
                        }
                    } else {
                        System.out.println("No lines to visualise1");
                    }
                }
            } else {
                System.out.println("No lines to visualise2");
            }
        }
    }

    public String getName(ArrayList<String> tenancyTypeChanges) {
        String result = "";
        Iterator<String> ite;
        ite = tenancyTypeChanges.iterator();
        boolean doneFirst = false;
        while (ite.hasNext()) {
            String s;
            s = ite.next();
            s = s.replaceAll(" ", "");
            s = s.replaceAll("UO", "U");
            if (!doneFirst) {
                result += s;
                doneFirst = true;
            } else {
                result += "_" + s;
            }
        }
        return result;
    }
}
