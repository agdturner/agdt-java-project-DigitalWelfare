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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.visualisation.mapping;

import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Point;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.geotools.data.collection.TreeSetFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeatureType;
import uk.ac.leeds.ccg.andyt.agdtgeotools.AGDT_Shapefile;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ProcessorAdviceLeeds;

/**
 *
 * @author geoagdt
 */
public class DW_MapsAdviceLeeds extends DW_Maps {

    DW_ProcessorAdviceLeeds DW_ProcessorAdviceLeeds;
    
    public DW_MapsAdviceLeeds(DW_Environment env) {
        super(env);
    }

    public TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft) {
        TreeSetFeatureCollection result;
        TreeMap<String, AGDT_Point> tAdviceLeedsNamesAndPoints;
        tAdviceLeedsNamesAndPoints = DW_ProcessorAdviceLeeds.getAdviceLeedsNamesAndPoints();
        TreeMap<String, AGDT_Point> map = tAdviceLeedsNamesAndPoints;
        /*
         * GeometryFactory will be used to create the geometry attribute of each feature,
         * using a Point object for the location.
         */
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = new TreeSetFeatureCollection();
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String outlet = ite.next();
            String name = outlet;
            AGDT_Point p = map.get(outlet);
            addPointFeature(p, gf, sfb, name, result);
        }
        return result;
    }

    public TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft,
            String outletTarget) {
        TreeSetFeatureCollection result;
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(sft);
        result = getAdviceLeedsPointFeatureCollection(
                sft,
                outletTarget,
                gf,
                sfb);
        return result;
    }

    public TreeSetFeatureCollection getAdviceLeedsPointFeatureCollection(
            SimpleFeatureType sft,
            String outletTarget,
            GeometryFactory gf,
            SimpleFeatureBuilder sfb) {
        TreeSetFeatureCollection result;
        result = new TreeSetFeatureCollection();
        TreeMap<String, AGDT_Point> tAdviceLeedsNamesAndPoints;
        tAdviceLeedsNamesAndPoints = DW_ProcessorAdviceLeeds.getAdviceLeedsNamesAndPoints();
        TreeMap<String, AGDT_Point> map = tAdviceLeedsNamesAndPoints;
        Iterator<String> ite = map.keySet().iterator();
        while (ite.hasNext()) {
            String outlet = ite.next();
            if (outlet.equalsIgnoreCase(outletTarget)) {
                AGDT_Point p = map.get(outlet);
                String name = outlet;
                addPointFeature(p, gf, sfb, name, result);
            }
        }
        return result;
    }

    public File createAdviceLeedsPointShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getAdviceLeedsPointFeatureCollection(sft);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    /**
     * @param dir
     * @param shapefileFilename
     * @param outletTarget
     * @return
     */
    public File createAdviceLeedsPointShapefileIfItDoesNotExist(
            File dir,
            String shapefileFilename,
            String outletTarget) {
        File result;
        SimpleFeatureType sft;
        sft = getPointSimpleFeatureType(getDefaultSRID());
        TreeSetFeatureCollection fc;
        fc = getAdviceLeedsPointFeatureCollection(
                sft, outletTarget);
        result = createShapefileIfItDoesNotExist(
                dir,
                shapefileFilename,
                fc,
                sft);
        return result;
    }

    /**
     * @return
     */
    public DW_Shapefile getAdviceLeedsPointDW_Shapefile() {
        String tAdviceLeedsPointName = "AllAdviceLeedsPoints";
        File tAdviceLeedsPointShapefileDir;
        tAdviceLeedsPointShapefileDir = new File(
                env.getDW_Files().getGeneratedAdviceLeedsDir(),
                tAdviceLeedsPointName);
        String tAdviceLeedsPointShapefileFilename;
        tAdviceLeedsPointShapefileFilename = tAdviceLeedsPointName + ".shp";
        File tAdviceLeedsPointShapefile;
        tAdviceLeedsPointShapefile = createAdviceLeedsPointShapefileIfItDoesNotExist(
                tAdviceLeedsPointShapefileDir,
                tAdviceLeedsPointShapefileFilename);
        return new DW_Shapefile(tAdviceLeedsPointShapefile);
    }

    /**
     * @return
     */
    public ArrayList<AGDT_Shapefile> getAdviceLeedsPointDW_Shapefiles() {
        ArrayList<AGDT_Shapefile> result;
        result = new ArrayList<AGDT_Shapefile>();
        ArrayList<String> tAdviceLeedsServiceNames;
        tAdviceLeedsServiceNames = DW_ProcessorAdviceLeeds.getAdviceLeedsServiceNames();
        Iterator<String> ite;
        ite = tAdviceLeedsServiceNames.iterator();
        while (ite.hasNext()) {
            String tAdviceLeedsPointName = ite.next();
            File tAdviceLeedsPointShapefileDir;
            tAdviceLeedsPointShapefileDir = new File(
                    DW_Files.getGeneratedAdviceLeedsDir(),
                    tAdviceLeedsPointName);
            String tAdviceLeedsPointShapefileFilename;
            tAdviceLeedsPointShapefileFilename = tAdviceLeedsPointName + ".shp";
            File tAdviceLeedsPointShapefile;
            tAdviceLeedsPointShapefile = createAdviceLeedsPointShapefileIfItDoesNotExist(
                    tAdviceLeedsPointShapefileDir,
                    tAdviceLeedsPointShapefileFilename,
                    tAdviceLeedsPointName);
            result.add(new AGDT_Shapefile(tAdviceLeedsPointShapefile));
        }
        return result;
    }
}
