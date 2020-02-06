package uk.ac.leeds.ccg.projects.dw.process.lcc;

import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.data.ukp.data.onspd.ONSPD_Point;
import uk.ac.leeds.ccg.data.ukp.util.UKP_YM3;
import uk.ac.leeds.ccg.data.shbe.core.SHBE_Strings;
import uk.ac.leeds.ccg.geotools.Geotools_Shapefile;
import uk.ac.leeds.ccg.geotools.Geotools_Style;
import uk.ac.leeds.ccg.geotools.core.Geotools_Strings;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_AreaCodesAndShapefiles;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_Shapefile;
import uk.ac.leeds.ccg.projects.dw.data.generated.DW_Table;
import uk.ac.leeds.ccg.data.shbe.data.SHBE_Data;
import uk.ac.leeds.ccg.data.ukp.data.UKP_Data;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.visualisation.mapping.DW_StyleParameters;

/**
 * @author Andy Turner
 * @version 1.0.0
 */
public class DW_LineMapsLCC extends DW_Maps {

    private final String targetPropertyNameLSOA = "LSOA11CD";
//    private DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
//    private DW_AreaCodesAndShapefiles tMSOACodesAndLeedsMSOAShapefile;
    protected ArrayList<Geotools_Shapefile> midgrounds;
    protected ArrayList<Geotools_Shapefile> foregrounds;

    protected DW_StyleParameters styleParameters;

    protected Geotools_Style style;

    public DW_LineMapsLCC(DW_Environment de) throws IOException {
        super(de);
        style = geotools.getStyle();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //new DW_LineMapsLCC().run2();
            new DW_LineMapsLCC(null).run();
        } catch (Exception | Error e) {
            System.err.println(e.getLocalizedMessage());
            e.printStackTrace(System.err);
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

    protected TreeMap<String, ArrayList<UKP_YM3>> getYM3s(
            ArrayList<Integer> includes) throws IOException, Exception {
        TreeMap<String, ArrayList<UKP_YM3>> r = new TreeMap<>();
        SHBE_Data shbeData = dwEnv.getShbeData();
        String[] tSHBEFilenamesAll = shbeData.getFilenames();
        ArrayList<UKP_YM3> yMs;
        // All
        Iterator<Integer> includesIte = includes.iterator();
        yMs = new ArrayList<>();
        UKP_YM3 yM3 = null;
        boolean first = true;
        String name = "";
        int i;
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yM3 = shbeData.getYM3(tSHBEFilenamesAll[i]);
            if (first) {
                name = yM3.toString();
                first = false;
            }
            yMs.add(yM3);
        }
        name += "_TO_" + yM3;
        //result.put("All", yearMonths1);
        r.put(name, yMs);
        // Individual
        includesIte = includes.iterator();
        i = includesIte.next();
        UKP_YM3 yM30 = shbeData.getYM3(tSHBEFilenamesAll[i]);
        while (includesIte.hasNext()) {
            i = includesIte.next();
            yMs = new ArrayList<>();
            UKP_YM3 yM31;
            yM31 = shbeData.getYM3(tSHBEFilenamesAll[i]);
            yMs.add(yM30);
            yMs.add(yM31);
//            result.put(yM, yearMonths1);
            r.put(yM30 + "_TO_" + yM31, yMs);
            yM30 = yM31;
        }
        return r;
    }

    public void run2(
            boolean doUnderOccupancy,
            boolean doCouncil) throws Exception, Error {

        DW_Table table = new DW_Table(dwEnv);
        String filename;
        //filename = "PostcodeChanges_Start_2008April_End_2008October.csv";
        Path dirIn = Paths.get(files.getOutputSHBETablesDir().toString(),
                DW_Strings.sTenancy, DW_Strings.s_A,
                DW_Strings.sTenancyTypeTransition,
                DW_Strings.sCheckedPreviousTenancyType,
                DW_Strings.sTenancyAndPostcodeChanges);
        if (doUnderOccupancy) {
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sU);
            if (doCouncil) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCouncil);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sRSL);
            }
        } else {
            dirIn = Paths.get(dirIn.toString(), DW_Strings.s_A);
        }
        dirIn = Paths.get(dirIn.toString(), DW_Strings.sPostcodeChanged,
                DW_Strings.s_A, DW_Strings.sGroupedNo,
                DW_Strings.sPostcodeChanges,
                DW_Strings.sCheckedPreviousPostcode);

        ArrayList<Integer> includes;
        //includes = SHBE_Handler.getSHBEFilenameIndexesExcept34();
        includes = dwEnv.getShbeData().getSHBEFilenameIndexes();

        TreeMap<String, ArrayList<UKP_YM3>> yM3s;
        yM3s = getYM3s(includes);

        ArrayList<ArrayList<String>> allTenancyTypeChanges;
        allTenancyTypeChanges = getAllTenancyTypeChanges(doUnderOccupancy);

        Iterator<String> yM3sIte;
        yM3sIte = yM3s.keySet().iterator();
        while (yM3sIte.hasNext()) {
            String name;
            name = yM3sIte.next();
            System.out.println(name);
            ArrayList<UKP_YM3> yMs;
            yMs = yM3s.get(name);
            ArrayList<String> lines;
            lines = new ArrayList<>();
            Iterator<UKP_YM3> yMsIte;
            yMsIte = yMs.iterator();
            UKP_YM3 yM0 = yMsIte.next();
            while (yMsIte.hasNext()) {
                UKP_YM3 yM1;
                yM1 = yMsIte.next();
                filename = "PostcodeChanges_Start_" + yM0 + "_End_" + yM1 + ".csv";
                Path f = Paths.get(dirIn.toString(), filename);
                //System.out.println("Load " + f);
                if (Files.exists(f)) {
                    ArrayList<String> linesPart;
                    linesPart = table.readCSV(f);
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

        SHBE_Data shbeData = dwEnv.getShbeData();

        ArrayList<String> paymentTypes;
        paymentTypes = SHBE_Strings.getPaymentTypes();
//        paymentTypes.remove(SHBE_Handler.sAllPT);
//        paymentTypes.remove(SHBE_Handler.sInPayment);
//        paymentTypes.remove(SHBE_Handler.sSuspended);
//        paymentTypes.remove(SHBE_Handler.sOtherPT);

        Iterator<String> paymentTypesIte;

        TreeMap<String, ArrayList<Integer>> includes;
        includes = shbeData.getIncludes();
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

                    TreeMap<String, ArrayList<UKP_YM3>> yM3s;
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

    private void init(boolean doCommunityAreasOverlay) throws IOException {
        initStyleParameters();
        foregrounds = new ArrayList<>();
        mapDirectory = files.getOutputSHBELineMapsDir();
        if (doCommunityAreasOverlay) {
            mapDirectory = Paths.get(mapDirectory.toString(),
                    "CommunityAreasOverlaid");
            foregrounds.add(getCommunityAreasDW_Shapefile());
        } else {
            DW_AreaCodesAndShapefiles tLSOACodesAndLeedsLSOAShapefile;
            tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
                    dwEnv,
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

    private void initStyleParameters() throws IOException {
        styleParameters = new DW_StyleParameters();
        styleParameters.setForegroundStyle(
                dwEnv.getGeotools().getStyle().createDefaultPolygonStyle(
                        Color.BLACK, //Color.GREEN,
                        Color.WHITE), 0);
        styleParameters.setMidgroundStyle(
                geotools.getStyle().createDefaultLineStyle(Color.LIGHT_GRAY), 0);
    }

    private void setStyleParameters(
            int type,
            Color origin,
            Color destination) {
        if (type == 0) {
            styleParameters.setMidgroundStyle(
                    geotools.getStyle().createDefaultLineStyle(origin), 1);
            styleParameters.setMidgroundStyle(style.createDefaultLineStyle(destination), 2);
        } else {
            styleParameters.setMidgroundStyle(style.createDefaultLineStyle(origin), 2);
            styleParameters.setMidgroundStyle(style.createDefaultLineStyle(destination), 1);

        }
    }

//    private void initLSOACodesAndLeedsLSOAShapefile(
//            String targetPropertyNameLSOA) {
//        tLSOACodesAndLeedsLSOAShapefile = new DW_AreaCodesAndShapefiles(
//                "LSOA", targetPropertyNameLSOA, getShapefileDataStoreFactory());
//    }
    public void postcodeMaps(TreeMap<String, ArrayList<UKP_YM3>> yearMonths,
            String includeName, ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeChanges,
            boolean doTenancyTypeAndPostcodeChange, boolean doUnderOccupancyData,
            boolean doCouncil, boolean doCheckedPreviousTenure,
            boolean doCheckedPreviousPostcode, boolean grouped) throws Exception {
        DW_Table table = new DW_Table(dwEnv);
        Path dirIn;
        Path dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = Paths.get(files.getOutputSHBETablesDir().toString(), "Tenancy");
            dirOut = Paths.get(mapDirectory.toString(), DW_Strings.sTenancyAndPostcodeChanges);
            dirIn = files.getUODir(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = files.getUODir(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sTenancyTypeTransition);
            if (doCheckedPreviousTenure) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyType);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sTenancyAndPostcodeChanges);
        } else {
            dirIn = Paths.get(mapDirectory.toString(), DW_Strings.sPostcodeChanges);
            dirOut = Paths.get(mapDirectory.toString(), DW_Strings.sPostcodeChanges);
            dirIn = files.getUODir(dirIn, doUnderOccupancyData, doCouncil);
            dirOut = files.getUODir(dirOut, doUnderOccupancyData, doCouncil);
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sPostcodeChanged);
            dirIn = Paths.get(dirIn.toString(), includeName);
            if (grouped) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sGrouped);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sGrouped);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sGroupedNo);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sGroupedNo);
            }
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sPostcodeChanges);
            if (doCheckedPreviousPostcode) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousPostcode);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcode);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
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
                ArrayList<UKP_YM3> yMs;
                yMs = yearMonths.get(name);
                ArrayList<String> lines;
                lines = new ArrayList<>();
                Iterator<UKP_YM3> yMsIte;
                yMsIte = yMs.iterator();
                UKP_YM3 yM30 = yMsIte.next();
                UKP_YM3 yM30Start = yM30;
                UKP_YM3 yM30End;
                while (yMsIte.hasNext()) {
                    UKP_YM3 yM31 = yMsIte.next();
                    String filename;
                    filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                    Path f = Paths.get(dirIn.toString(), filename);
                    if (Files.exists(f)) {
                        //System.out.println("Load " + f);
                        ArrayList<String> linesPart;
                        linesPart = table.readCSV(f);
                        lines.addAll(linesPart);
                    }
                    yM30 = yM31;
                }
                yM30End = yM30;
                //System.out.println(filename + " lines " + lines.size());
                //int count = 0;
                if (!lines.isEmpty()) {
                    //name += "_TO_" + split1[0] + "_" + split1[1];
                    Path dirOut2 = Paths.get(dirOut.toString(), name);
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
                            yM30 = new UKP_YM3(lineSplit[1].trim());
                            UKP_YM3 yM31;
                            yM31 = new UKP_YM3(lineSplit[2].trim());
                            String tenancyTypeChange;
                            UKP_YM3 yM30v;
                            yM30v = ukpData.getNearestYM3ForONSPDLookup(yM30);
                            UKP_YM3 yM31v;
                            yM31v = ukpData.getNearestYM3ForONSPDLookup(yM31);
                            tenancyTypeChange = lineSplit[3].trim();
                            if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                                if (yMs.contains(yM31)) {
                                    String postcode0 = lineSplit[4].trim();
                                    String postcode1 = lineSplit[5].trim();
                                    ONSPD_Point origin;
                                    origin = ukpData.getPointFromPostcode(
                                            yM30v,
                                            UKP_Data.TYPE_UNIT,
                                            postcode0);
                                    ONSPD_Point destination;
                                    destination = ukpData.getPointFromPostcode(
                                            yM31v,
                                            UKP_Data.TYPE_UNIT,
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
                        Path dirOut3;
                        dirOut3 = Paths.get(dirOut2.toString(), name);
                        String nameM;
                        nameM = name + "M";
                        tsfc = tsfcMs.get(tenancyTypeChanges);
                        if (tsfc != null) {
                            Path f0;
                            f0 = geotools.getOutputShapefile(dirOut3, nameM);
                            DW_Shapefile.transact(f0, aLineSFT, tsfc,
                                    getShapefileDataStoreFactory());
                            String nameS;
                            nameS = name + "S";
                            tsfc = tsfcSs.get(tenancyTypeChanges);
                            Path f1;
                            f1 = geotools.getOutputShapefile(dirOut3, nameS);
                            DW_Shapefile.transact(f1, aLineSFT, tsfc,
                                    getShapefileDataStoreFactory());
                            String nameE;
                            nameE = name + "E";
                            tsfc = tsfcEs.get(tenancyTypeChanges);
                            Path f2;
                            f2 = geotools.getOutputShapefile(dirOut3, nameE);
                            DW_Shapefile.transact(f2, aLineSFT, tsfc,
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
                                Path dirOut4 = Paths.get(dirOut3.toString(), name2);
                                Files.createDirectories(dirOut4);
                                setStyleParameters(i, Color.RED, Color.BLUE);
                                geotools.outputToImage(legendMessage, name,
                                        midgrounds, foregrounds, dirOut4,
                                        Geotools_Strings.png_String,
                                        imageWidth, styleParameters, 0,
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
            TreeMap<String, ArrayList<UKP_YM3>> yearMonths,
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
        DW_Table table = new DW_Table(dwEnv);
        Path dirIn;
        Path dirInCouncil;
        Path dirInRSL;
        Path dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = Paths.get(files.getOutputSHBETablesDir().toString(),
                    DW_Strings.sTenancy, paymentType, DW_Strings.s_A,
                    DW_Strings.sTenancyTypeTransition);
            dirOut = Paths.get(mapDirectory.toString(), paymentType,
                    DW_Strings.sTenancyAndPostcodeChanges);
            dirOut = files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            if (doCheckedPreviousTenure) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyType);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sTenancyAndPostcodeChanges);
        } else {
            dirIn = Paths.get(files.getOutputSHBETablesDir().toString(), DW_Strings.sPostcodeChanges);
            dirOut = Paths.get(mapDirectory.toString(), paymentType, DW_Strings.sPostcodeChanges);
            dirOut = files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
        }
        dirInCouncil = files.getUODir(dirIn, doUnderOccupancyData, true);
        dirInCouncil = Paths.get(dirInCouncil.toString(),
                DW_Strings.sPostcodeChanged, includeName);
        dirInRSL = files.getUODir(dirIn, doUnderOccupancyData, false);
        dirInRSL = Paths.get(dirInRSL.toString(),
                DW_Strings.sPostcodeChanged, includeName);

//        // <Hack>
//        dirInCouncil = Paths.get(
//                dirInCouncil,
//                DW_Strings.sAll);
//        dirInRSL = Paths.get(
//                dirInRSL,
//                DW_Strings.sAll);
//        // </Hack>
        if (grouped) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGrouped);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGrouped);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGrouped);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGroupedNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGroupedNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGroupedNo);
        }
        dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sPostcodeChanges);
        dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte;
        yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<UKP_YM3> yM3s = yearMonths.get(name);
            ArrayList<String> lines = new ArrayList<>();
            Iterator<UKP_YM3> yMsIte = yM3s.iterator();
            UKP_YM3 yM30 = yMsIte.next();
            UKP_YM3 yM30Start = yM30;
            UKP_YM3 yM30End;
            while (yMsIte.hasNext()) {
                UKP_YM3 yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                Path f;
                f = Paths.get(dirInCouncil.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                } else {
                    int debug = 1;
                }
                f = Paths.get(dirInRSL.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                Path dirOut2;
                dirOut2 = Paths.get(dirOut.toString(), name);
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
                        yM30 = new UKP_YM3(lineSplit[1].trim());
                        UKP_YM3 yM31;
                        yM31 = new UKP_YM3(lineSplit[2].trim());
                        UKP_YM3 yM30v;
                        yM30v = ukpData.getNearestYM3ForONSPDLookup(yM30);
                        UKP_YM3 yM31v;
                        yM31v = ukpData.getNearestYM3ForONSPDLookup(yM31);
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
                                origin = ukpData.getPointFromPostcode(
                                        yM30v,
                                        UKP_Data.TYPE_UNIT,
                                        postcode0);
                                ONSPD_Point destination;
                                destination = ukpData.getPointFromPostcode(
                                        yM31v,
                                        UKP_Data.TYPE_UNIT,
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
                    Path dirOut3;
                    dirOut3 = Paths.get(dirOut2.toString(), name);
                    String nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        Path f0 = geotools.getOutputShapefile(dirOut3, nameM);
                        DW_Shapefile.transact(f0, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        Path f1 = geotools.getOutputShapefile(dirOut3, nameS);
                        DW_Shapefile.transact(f1, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        Path f2 = geotools.getOutputShapefile(dirOut3, nameE);
                        DW_Shapefile.transact(f2, aLineSFT, tsfc,
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
                            Path dirOut4 = Paths.get(dirOut3.toString(), name2);
                            Files.createDirectories(dirOut4);
                            setStyleParameters(i, Color.RED, Color.BLUE);
                            geotools.outputToImage(legendMessage, name,
                                    midgrounds, foregrounds, dirOut4,
                                    Geotools_Strings.png_String,
                                    imageWidth, styleParameters, 0,
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
            TreeMap<String, ArrayList<UKP_YM3>> yearMonths,
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
        DW_Table table = new DW_Table(dwEnv);
        Path dirIn;
        Path dirInCouncil;
        Path dirInRSL;
        Path dirOut;
        if (doTenancyTypeAndPostcodeChange) {
            dirIn = Paths.get(files.getOutputSHBETablesDir().toString(),
                    DW_Strings.sTenancy, paymentType, DW_Strings.s_A,
                    DW_Strings.sTenancyTypeTransition);
            dirOut = Paths.get(mapDirectory.toString(), paymentType,
                    DW_Strings.s_A + DW_Strings.sPostcodeChanges);
            dirOut = files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
            if (doCheckedPreviousTenure) {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyType);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyType);
            } else {
                dirIn = Paths.get(dirIn.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
                dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousTenancyTypeNo);
            }
            dirIn = Paths.get(dirIn.toString(), DW_Strings.sTenancyAndPostcodeChanges);
        } else {
            dirIn = Paths.get(files.getOutputSHBETablesDir().toString(), DW_Strings.sPostcodeChanges);
            dirOut = Paths.get(mapDirectory.toString(), paymentType, DW_Strings.sPostcodeChanges);
            dirOut = files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
        }
        dirInCouncil = files.getUODir(dirIn, doUnderOccupancyData, true);
        dirInCouncil = Paths.get(dirInCouncil.toString(),
                DW_Strings.sPostcodeChanged, includeName);
        dirInRSL = files.getUODir(dirIn, doUnderOccupancyData, false);
        dirInRSL = Paths.get(dirInRSL.toString(),
                DW_Strings.sPostcodeChanged, includeName);
        if (grouped) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGrouped);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGrouped);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGrouped);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGroupedNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGroupedNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGroupedNo);
        }
        dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sPostcodeChanges);
        dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
        }
        Path dirIn2;
        Path dirInCouncil2;
        Path dirInRSL2;
        dirIn2 = Paths.get(files.getOutputSHBETablesDir().toString(),
                DW_Strings.sPostcodeChanges);
        dirIn2 = Paths.get(dirIn2.toString(), paymentType);
        dirInCouncil2 = files.getUODir(dirIn2, doUnderOccupancyData, true);
        dirInRSL2 = files.getUODir(dirIn2, doUnderOccupancyData, false);
        dirInCouncil2 = Paths.get(dirInCouncil2.toString(),
                DW_Strings.sPostcodeChanged);
        dirInRSL2 = Paths.get(dirInRSL2.toString(), DW_Strings.sPostcodeChanged);
        dirInCouncil2 = Paths.get(dirInCouncil2.toString(), includeName);
        dirInRSL2 = Paths.get(dirInRSL2.toString(), includeName);
        if (grouped) {
            dirInCouncil2 = Paths.get(dirInCouncil2.toString(),
                    DW_Strings.sGrouped);
            dirInRSL2 = Paths.get(dirInRSL2.toString(), DW_Strings.sGrouped);
        } else {
            dirInCouncil2 = Paths.get(dirInCouncil2.toString(),
                    DW_Strings.sGroupedNo);
            dirInRSL2 = Paths.get(dirInRSL2.toString(), DW_Strings.sGroupedNo);
        }
        dirInCouncil2 = Paths.get(dirInCouncil2.toString(), DW_Strings.sPostcodeChanges);
        dirInRSL2 = Paths.get(dirInRSL2.toString(), DW_Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil2 = Paths.get(dirInCouncil2.toString(),
                    DW_Strings.sCheckedPreviousPostcode);
            dirInRSL2 = Paths.get(dirInRSL2.toString(),
                    DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil2 = Paths.get(dirInCouncil2.toString(),
                    DW_Strings.sCheckedPreviousPostcodeNo);
            dirInRSL2 = Paths.get(dirInRSL2.toString(),
                    DW_Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte = yearMonths.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<UKP_YM3> yM3s = yearMonths.get(name);
            ArrayList<String> lines = new ArrayList<>();
            Iterator<UKP_YM3> yMsIte = yM3s.iterator();
            UKP_YM3 yM30 = yMsIte.next();
            UKP_YM3 yM30Start = yM30;
            UKP_YM3 yM30End;
            while (yMsIte.hasNext()) {
                UKP_YM3 yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                Path f = Paths.get(dirInCouncil.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                f = Paths.get(dirInRSL.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                f = Paths.get(dirInCouncil2.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                f = Paths.get(dirInRSL2.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                Path dirOut2 = Paths.get(dirOut.toString(), name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> attcIte = allTenancyTypeChanges.iterator();
                while (attcIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges = attcIte.next();

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
                        yM30 = new UKP_YM3(lineSplit[1].trim());
                        UKP_YM3 yM31;
                        yM31 = new UKP_YM3(lineSplit[2].trim());
                        UKP_YM3 yM30v;
                        yM30v = ukpData.getNearestYM3ForONSPDLookup(yM30);
                        UKP_YM3 yM31v;
                        yM31v = ukpData.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange;
                        tenancyTypeChange = lineSplit[3].trim();
                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                ONSPD_Point origin;
                                origin = ukpData.getPointFromPostcode(
                                        yM30v, UKP_Data.TYPE_UNIT, postcode0);
                                ONSPD_Point destination;
                                destination = ukpData.getPointFromPostcode(
                                        yM31v, UKP_Data.TYPE_UNIT, postcode1);
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
                    Path dirOut3;
                    dirOut3 = Paths.get(dirOut2.toString(), name);
                    String nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        Path f0 = geotools.getOutputShapefile(dirOut3, nameM);
                        DW_Shapefile.transact(f0, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        Path f1 = geotools.getOutputShapefile(dirOut3, nameS);
                        DW_Shapefile.transact(f1, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        Path f2 = geotools.getOutputShapefile(dirOut3, nameE);
                        DW_Shapefile.transact(f2, aLineSFT, tsfc,
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
                            Path dirOut4 = Paths.get(dirOut3.toString(), name2);
                            Files.createDirectories(dirOut4);
                            setStyleParameters(i, Color.RED, Color.BLUE);
                            geotools.outputToImage(legendMessage, name,
                                    midgrounds, foregrounds, dirOut4,
                                    Geotools_Strings.png_String, imageWidth,
                                    styleParameters, 0, Double.POSITIVE_INFINITY,
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
     * @param pt payment type
     * @param yms yearMonths
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
    public void postcodeMaps(String pt, TreeMap<String, ArrayList<UKP_YM3>> yms,
            String includeName, ArrayList<Integer> include,
            ArrayList<ArrayList<String>> allTenancyTypeGroups,
            boolean doUnderOccupancyData, boolean doCouncil, boolean doRSL,
            boolean doCheckedPreviousPostcode, boolean grouped) throws Exception {
        DW_Table table = new DW_Table(dwEnv);
        Path dirIn;
        Path dirInCouncil;
        Path dirInRSL;
        Path dirOut;
        dirIn = Paths.get(files.getOutputSHBETablesDir().toString(),
                DW_Strings.sPostcodeChanges, pt);
        dirOut = Paths.get(mapDirectory.toString(), pt, DW_Strings.sPostcodeChanges);
        dirOut = files.getUOFile(dirOut, doUnderOccupancyData, doCouncil, doRSL);
        //dirIn = files.getUODir(dirIn, doUnderOccupancyData, doCouncil, doRSL);
        dirInCouncil = files.getUODir(dirIn, doUnderOccupancyData, true);
        dirInCouncil = Paths.get(dirInCouncil.toString(),
                DW_Strings.sPostcodeChanged, includeName);
        dirInRSL = files.getUODir(dirIn, doUnderOccupancyData, false);
        dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sPostcodeChanged,
                includeName);
        if (grouped) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGrouped);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGrouped);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGrouped);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sGroupedNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sGroupedNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sGroupedNo);
        }
        dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sPostcodeChanges);
        dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sPostcodeChanges);
        if (doCheckedPreviousPostcode) {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcode);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcode);
        } else {
            dirInCouncil = Paths.get(dirInCouncil.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirInRSL = Paths.get(dirInRSL.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
            dirOut = Paths.get(dirOut.toString(), DW_Strings.sCheckedPreviousPostcodeNo);
        }
        Iterator<String> yearMonthsIte = yms.keySet().iterator();
        while (yearMonthsIte.hasNext()) {
            String name = yearMonthsIte.next();
            System.out.println(name);
            ArrayList<UKP_YM3> yM3s = yms.get(name);
            ArrayList<String> lines = new ArrayList<>();
            Iterator<UKP_YM3> yMsIte = yM3s.iterator();
            UKP_YM3 yM30 = yMsIte.next();
            UKP_YM3 yM30Start = yM30;
            UKP_YM3 yM30End;
            while (yMsIte.hasNext()) {
                UKP_YM3 yM31 = yMsIte.next();
                String filename = "PostcodeChanges_Start_" + yM30 + "_End_" + yM31 + ".csv";
                Path f = Paths.get(dirInCouncil.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                f = Paths.get(dirInRSL.toString(), filename);
                if (Files.exists(f)) {
                    lines.addAll(table.readCSV(f));
                }
                yM30 = yM31;
            }
            yM30End = yM30;
            if (!lines.isEmpty()) {
                //name += "_TO_" + split1[0] + "_" + split1[1];
                Path dirOut2 = Paths.get(dirOut.toString(), name);
                // Step 3.
                SimpleFeatureType aLineSFT = DataUtilities.createType(
                        "LINE",
                        "the_geom:LineString," + "name:String," + "number:Integer");

                // Totals for each tenancy type
                Iterator<ArrayList<String>> attgIte = allTenancyTypeGroups.iterator();
                while (attgIte.hasNext()) {
                    ArrayList<String> tenancyTypeChanges = attgIte.next();

                    // Initialise a FeatureCollections and SimpleFeatureBuilders
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcSs
                            = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcMs
                            = new HashMap<>();
                    HashMap<ArrayList<String>, TreeSetFeatureCollection> tsfcEs
                            = new HashMap<>();

                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbSs
                            = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbMs
                            = new HashMap<>();
                    HashMap<ArrayList<String>, SimpleFeatureBuilder> sfbEs
                            = new HashMap<>();

                    // Create SimpleFeatureBuilder
                    //FeatureFactory ff = FactoryFinder.getGeometryFactories();
                    GeometryFactory geometryFactory = new GeometryFactory();

                    long count = 0;
                    long notPCCount = 0;

                    Iterator<String> linesIte = lines.iterator();
                    while (linesIte.hasNext()) {
                        String line = linesIte.next();
                        String[] lineSplit = line.split(",");
                        yM30 = new UKP_YM3(lineSplit[1].trim());
                        UKP_YM3 yM31 = new UKP_YM3(lineSplit[2].trim());
                        UKP_YM3 yM30v = ukpData.getNearestYM3ForONSPDLookup(yM30);
                        UKP_YM3 yM31v = ukpData.getNearestYM3ForONSPDLookup(yM31);
                        String tenancyTypeChange = lineSplit[3].trim();
                        if (tenancyTypeChanges.contains(tenancyTypeChange)) {
                            if (yM3s.contains(yM31)) {
                                String postcode0 = lineSplit[4].trim();
                                String postcode1 = lineSplit[5].trim();
                                ONSPD_Point origin
                                        = ukpData.getPointFromPostcode(
                                                yM30v, UKP_Data.TYPE_UNIT, postcode0);
                                ONSPD_Point destination
                                        = ukpData.getPointFromPostcode(
                                                yM31v, UKP_Data.TYPE_UNIT, postcode1);
                                TreeSetFeatureCollection tsfcS
                                        = tsfcSs.get(tenancyTypeChanges);
                                if (tsfcS == null) {
                                    tsfcS = new TreeSetFeatureCollection();
                                    tsfcSs.put(tenancyTypeChanges, tsfcS);
                                }
                                SimpleFeatureBuilder sfbS
                                        = sfbSs.get(tenancyTypeChanges);
                                if (sfbS == null) {
                                    sfbS = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcM
                                        = tsfcMs.get(tenancyTypeChanges);
                                if (tsfcM == null) {
                                    tsfcM = new TreeSetFeatureCollection();
                                    tsfcMs.put(tenancyTypeChanges, tsfcM);
                                }
                                SimpleFeatureBuilder sfbM
                                        = sfbMs.get(tenancyTypeChanges);
                                if (sfbM == null) {
                                    sfbM = new SimpleFeatureBuilder(aLineSFT);
                                }
                                TreeSetFeatureCollection tsfcE
                                        = tsfcEs.get(tenancyTypeChanges);
                                if (tsfcE == null) {
                                    tsfcE = new TreeSetFeatureCollection();
                                    tsfcEs.put(tenancyTypeChanges, tsfcE);
                                }
                                SimpleFeatureBuilder sfbE
                                        = sfbEs.get(tenancyTypeChanges);
                                if (sfbE == null) {
                                    sfbE = new SimpleFeatureBuilder(aLineSFT);
                                }
                                if (origin != null && destination != null) {
                                    double originx = origin.getX();
                                    double originy = origin.getY();
                                    double destinationx = destination.getX();
                                    double destinationy = destination.getY();
                                    double break1x = (originx + (originx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    double break1y = (originy + (originy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
//                        break1x = (originx + destinationx) / 2.0d;
//                        break1y = (originy + destinationy) / 2.0d;
                                    double break2x = (destinationx + (destinationx + ((originx + destinationx) / 2.0d)) / 2.0d) / 2.0d;
                                    double break2y = (destinationy + (destinationy + ((originy + destinationy) / 2.0d)) / 2.0d) / 2.0d;
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
                    String dates = getDatesString(yM30Start.toString(), yM30End.toString());
                    String legendMessage = dates + " Count " + count;
                    System.out.println("line count = " + count);
                    System.out.println("Postcode not valid count = " + notPCCount);
                    TreeSetFeatureCollection tsfc;
                    name = getName(tenancyTypeChanges);
                    Path dirOut3 = Paths.get(dirOut2.toString(), name);
                    String nameM = name + "M";
                    tsfc = tsfcMs.get(tenancyTypeChanges);
                    if (tsfc != null) {
                        Path f0 = geotools.getOutputShapefile(dirOut3, nameM);
                        DW_Shapefile.transact(f0, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameS = name + "S";
                        tsfc = tsfcSs.get(tenancyTypeChanges);
                        Path f1 = geotools.getOutputShapefile(dirOut3, nameS);
                        DW_Shapefile.transact(f1, aLineSFT, tsfc,
                                getShapefileDataStoreFactory());
                        String nameE = name + "E";
                        tsfc = tsfcEs.get(tenancyTypeChanges);
                        Path f2 = geotools.getOutputShapefile(dirOut3, nameE);
                        DW_Shapefile.transact(f2, aLineSFT, tsfc,
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
                            Path dirOut4 = Paths.get(dirOut3.toString(), name2);
                            Files.createDirectories(dirOut4);
                            setStyleParameters(i, Color.RED, Color.BLUE);
                            geotools.outputToImage(legendMessage, name,
                                    midgrounds, foregrounds, dirOut4,
                                    Geotools_Strings.png_String, imageWidth,
                                    styleParameters, 0,
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
        String r = "";
        Iterator<String> ite = tenancyTypeChanges.iterator();
        boolean doneFirst = false;
        while (ite.hasNext()) {
            String s = ite.next();
            s = s.replaceAll(" ", "");
            s = s.replaceAll("UO", "U");
            if (!doneFirst) {
                r += s;
                doneFirst = true;
            } else {
                r += "_" + s;
            }
        }
        return r;
    }
}
