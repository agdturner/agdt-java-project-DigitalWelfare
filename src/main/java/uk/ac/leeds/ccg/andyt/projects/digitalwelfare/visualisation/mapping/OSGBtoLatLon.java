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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import uk.ac.leeds.ccg.andyt.generic.math.Generic_BigDecimal;

/**
 * Adapted from http://www.movable-type.co.uk/scripts/latlong-gridref.html.
 * Ordnance Survey Grid Reference functions (c) Chris Veness 2005-2012
 * http://www.movable-type.co.uk/scripts/gridref.js
 *
 * @author geoagdt
 */
public class OSGBtoLatLon {

    private BigDecimal PI;

    public static BigDecimal getPI() {
        return new Generic_BigDecimal().get_PI();
    }
    
    public BigDecimal get_PI() {
        if (PI != null) {
            PI = getPI();
        }
        return PI;
    }
    
    public OSGBtoLatLon() {
    }
    
    public static void main(String[] args) {
        new OSGBtoLatLon().run();
    }

    public void run() {
        //double lat = 53.807882;
        //double lon = -1.557103;
        //run1(lat,lon);
        double easting = 429162;
        double northing = 434735;
        run2(easting,northing);
    }
        
    public void run1(
            double lat,
            double lon) {
        System.out.println("lat " + lat + ", lon " + lon);
        double[] eastingNorthing = latlon2osgb(lat, lon);
        System.out.println(
                "easting " + eastingNorthing[0] + ", "
                + "northing " + eastingNorthing[1]);
        double[] latLon = osgb2latlon(eastingNorthing[0], eastingNorthing[1]);
        System.out.println("lat " + latLon[0] + ", lon " + latLon[1]);

        RoundingMode aRoundingMode = RoundingMode.HALF_UP;
        //RoundingMode aRoundingMode = RoundingMode.HALF_EVEN;
        //RoundingMode aRoundingMode = RoundingMode.HALF_DOWN;
        //RoundingMode aRoundingMode = RoundingMode.DOWN;

        BigDecimal latBigDecimal = BigDecimal.valueOf(lat);
        BigDecimal lonBigDecimal = BigDecimal.valueOf(lon);
        System.out.println("latBigDecimal " + latBigDecimal + ", lonBigDecimal " + lonBigDecimal);
        //int decimalPlaces = 10;
        //for (int decimalPlaces = 10; decimalPlaces < 100; decimalPlaces += 10) {
        for (int decimalPlaces = 4; decimalPlaces < 32; decimalPlaces += 4) {
            System.out.println("decimalPlaces " + decimalPlaces);
            BigDecimal[] eastingNorthingBigDecimal = latlon2osgb(
                    latBigDecimal,
                    lonBigDecimal,
                    decimalPlaces,
                    aRoundingMode);
            System.out.println(
                    "easting " + eastingNorthingBigDecimal[0] + ", "
                    + "northing " + eastingNorthingBigDecimal[1]);
            BigDecimal[] latLonBigDecimal = osgb2latlon(
                    eastingNorthingBigDecimal[0],
                    eastingNorthingBigDecimal[1],
                    decimalPlaces,
                    aRoundingMode);
            System.out.println("lat " + latLonBigDecimal[0] + ", lon " + latLonBigDecimal[1]);

        }
    }

    public void run2(
            double easting,
            double northing) {
        System.out.println(
                    "easting " + easting + ", "
                    + "northing " + northing);
        double[] latLon = osgb2latlon(easting, northing);
        System.out.println(
                    "lat " + latLon[0] + ", "
                    + "lon " + latLon[1]);
            
        RoundingMode aRoundingMode = RoundingMode.HALF_UP;
        //RoundingMode aRoundingMode = RoundingMode.HALF_EVEN;
        //RoundingMode aRoundingMode = RoundingMode.HALF_DOWN;
        //RoundingMode aRoundingMode = RoundingMode.DOWN;

        BigDecimal[] eastingNorthingBigDecimal = new BigDecimal[2];
        eastingNorthingBigDecimal[0] = BigDecimal.valueOf(easting);
        eastingNorthingBigDecimal[1] = BigDecimal.valueOf(northing);
        
        /**
         * http://www.ordnancesurvey.co.uk/gps/transformation
         * 53.808111 lat
         * -1.558647 lon
         * 429162.016 easting
         * 434735.009 northing
         * 53.808111 lat
         * -1.558647 lon
        */
        for (int decimalPlaces = 4; decimalPlaces < 32; decimalPlaces += 4) {
            System.out.println("decimalPlaces " + decimalPlaces);
            BigDecimal[] latLonBigDecimal = osgb2latlon(
                    eastingNorthingBigDecimal[0],
                    eastingNorthingBigDecimal[1],
                    decimalPlaces,
                    aRoundingMode);
            System.out.println("lat " + latLonBigDecimal[0] + ", lon " + latLonBigDecimal[1]);
            BigDecimal[] eastingNorthingBigDecimal2 = latlon2osgb(
                    latLonBigDecimal[0],
                    latLonBigDecimal[1],
                    decimalPlaces,
                    aRoundingMode);
            System.out.println(
                    "easting " + eastingNorthingBigDecimal2[0] + ", "
                    + "northing " + eastingNorthingBigDecimal2[1]);

        }
    }
    
    /**
     * Converts WGS84 latitude/longitude coordinate to an Ordnance Survey
     * easting/northing coordinate
     *
     * @param lat latitude to be converted
     * @param lon longitude to be converted
     * @return result double[2] where result[0] is easting, result[1] is
     * northing
     */
    public static double[] latlon2osgb(double lat, double lon) {
        double[] result = new double[2];
        lat = toRadians(lat);
        lon = toRadians(lon);
        double a = 6377563.396;
        double b = 6356256.910;            // Airy 1830 major & minor semi-axes
        double F0 = 0.9996012717;          // NatGrid scale factor on central meridian
        //System.out.println("F0 " + F0);
        double lat0 = toRadians(49);
        double lon0 = toRadians(-2);       // NatGrid true origin is 49ºN,2ºW
        double N0 = -100000;
        double E0 = 400000;                // northing & easting of true origin, metres
        double e2 = 1 - (b * b) / (a * a); // eccentricity squared
        //System.out.println("Eccentricity squared " + e2);
        double n = (a - b) / (a + b);
        double n2 = n * n;
        double n3 = n * n * n;

        double cosLat = Math.cos(lat);
        double sinLat = Math.sin(lat);
        double nu = a * F0 / Math.sqrt(1 - e2 * sinLat * sinLat); // transverse radius of curvature
        //System.out.println("Transverse radius of curvature " + nu);
        double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sinLat * sinLat, 1.5); // meridional radius of curvature
        double eta2 = nu / rho - 1;

        double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (lat - lat0);
        double Mb = (3 * n + 3 * n * n + (21D / 8D) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0);
        double Mc = ((15D / 8D) * n2 + (15D / 8D) * n3) * Math.sin(2D * (lat - lat0)) * Math.cos(2D * (lat + lat0));
        double Md = (35D / 24D) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3D * (lat + lat0));
        double M = b * F0 * (Ma - Mb + Mc - Md); // meridional arc

        double cos3lat = cosLat * cosLat * cosLat;
        double cos5lat = cos3lat * cosLat * cosLat;
        double tan2lat = Math.tan(lat) * Math.tan(lat);
        double tan4lat = tan2lat * tan2lat;

        double I = M + N0;
        double II = (nu / 2D) * sinLat * cosLat;
        double III = (nu / 24D) * sinLat * cos3lat * (5 - tan2lat + 9 * eta2);
        double IIIA = (nu / 720D) * sinLat * cos5lat * (61 - 58 * tan2lat + tan4lat);
        double IV = nu * cosLat;
        double V = (nu / 6D) * cos3lat * (nu / rho - tan2lat);
        double VI = (nu / 120D) * cos5lat * (5 - 18 * tan2lat + tan4lat + 14 * eta2 - 58 * tan2lat * eta2);

        double dLon = lon - lon0;
        double dLon2 = dLon * dLon;
        double dLon3 = dLon2 * dLon;
        double dLon4 = dLon3 * dLon;
        double dLon5 = dLon4 * dLon;
        double dLon6 = dLon5 * dLon;

        double N = I + II * dLon2 + III * dLon4 + IIIA * dLon6;
        double E = E0 + IV * dLon + V * dLon3 + VI * dLon5;
        //System.out.println("E " + E + ", N " + N);
        result[0] = E;
        result[1] = N;
        return result;
        //return new OsGridRef(E, N);
    }

    /**
     * Converts WGS84 latitude/longitude coordinate to an Ordnance Survey
     * easting/northing coordinate
     * http://en.wikipedia.org/wiki/World_Geodetic_System
     * http://en.wikipedia.org/wiki/Ordnance_Survey_National_Grid
     *
     * @param lat latitude to be converted
     * @param lon longitude to be converted
     * @param decimalPlaces
     * @param aRoundingMode
     * @return result double[2] where result[0] is easting, result[1] is
     * northing
     */
    public static BigDecimal[] latlon2osgb(
            BigDecimal lat,
            BigDecimal lon,
            int decimalPlaces,
            RoundingMode aRoundingMode) {
        //int doubleDecimalPlaces = decimalPlaces * 2;
        //int tripleDecimalPlaces = decimalPlaces * 3;
        int hDecimalPlaces = Math.max(decimalPlaces * 10, 100);

        /*
         * http://en.wikipedia.org/wiki/World_Geodetic_System#Main_parameters
         * The coordinate origin of WGS 84 is meant to be located at the Earth's
         * center of mass; the error is believed to be less than 2 cm.
         * The WGS 84 meridian of zero longitude is the IERS Reference Meridian,
         * 5.31 arc seconds or 102.5 metres (336.3 ft) east of the Greenwich 
         * meridian at the latitude of the Royal Observatory.
         * 
         * The WGS 84 datum surface is an oblate spheroid (ellipsoid) with major
         * (equatorial) radius a = 6378137 m at the equator and flattening 
         * f = 1/298.257223563. The polar semi-minor axis b then equals a times 
         * (1−f), or 6356752.3142 m.
         * 
         * Presently WGS 84 uses the EGM96 (Earth Gravitational Model 1996) 
         * geoid, revised in 2004. This geoid defines the nominal sea level 
         * surface by means of a spherical harmonics series of degree 360 (which 
         * provides about 100 km horizontal resolution). The deviations of the 
         * EGM96 geoid from the WGS 84 reference ellipsoid range from about 
         * −105 m to about +85 m. EGM96 differs from the original WGS 84 geoid, 
         * referred to as EGM84.
         */
        /*
         * http://en.wikipedia.org/wiki/Ordnance_Survey_National_Grid#Datum_shift_between_OSGB_36_and_WGS_84
         * The difference between the coordinates on different datums varies 
         * from place to place. The longitude and latitude positions on OSGB 36 
         * are the same as for WGS 84 at a point in the Atlantic Ocean well to 
         * the west of Great Britain. In Cornwall, the WGS 84 longitude lines 
         * are about 70 metres east of their OSGB 36 equivalents, this value 
         * rising gradually to about 120 m east on the east coast of East 
         * Anglia. The WGS 84 latitude lines are about 70 m south of the OSGB 36
         * lines in South Cornwall, the difference diminishing to zero in the 
         * Scottish Borders, and then increasing to about 50 m north on the 
         * north coast of Scotland. (If the lines are further east, then the 
         * longitude value of any given point is further west. Similarly, if the
         * lines are further south, the values will give the point a more 
         * northerly latitude.) The smallest datum shift is on the west coast of
         * Scotland and the greatest in Kent.
         * 
         * But Great Britain has not shrunk by 100+ metres; a point near Land's 
         * End now computes to be 27.6 metres closer to a point near Duncansby 
         * Head than it did under OSGB36.
         * 
         * http://en.wikipedia.org/wiki/Ordnance_Survey_National_Grid#Summary_parameters_of_the_coordinate_system
         * Datum: OSGB36
         * Map projection: Transverse Mercator
         * True Origin: 49°N, 2°W
         * False Origin: 400 km west, 100 km north of True Origin
         * Scale Factor: 0.9996012717 (or exactly log10(0.9998268) - 1)?????
         * EPSG Code: EPSG:27700
         * Ellipsoid: Airy 1830
         * The defining Airy dimensions are a = 20923713 "feet", b = 20853810 "feet". In the Retriangulation the base-10 logarithm of the number of metres in a "foot" was set at (0.48401603 − 1) exactly and the Airy metric dimensions are calculated from that. The flattening is exactly 69903 divided by 20923713.
         * Semi-major axis a: 6377563.396 m
         * Semi-minor axis b: 6356256.909 m
         * Flattening (derived constant): 1/299.3249646
         * 
         * http://www.ordnancesurvey.co.uk/docs/support/guide-coordinate-systems-great-britain.pdf
         */
        BigDecimal[] result = new BigDecimal[2];
        lat = toRadians(
                lat,
                hDecimalPlaces,
                aRoundingMode);
        lon = toRadians(
                lon,
                hDecimalPlaces,
                aRoundingMode);
        /*
         * Airy 1830 major semi-axis
         */
        BigDecimal a = new BigDecimal("6377563.396");
        /*
         * Airy 1830 minor semi-axis
         */
        //BigDecimal b = new BigDecimal("6356256.910"); 
        BigDecimal b = new BigDecimal("6356256.909");
        /*
         * NatGrid scale factor on central meridian
         */
        BigDecimal F0 = new BigDecimal("0.9996012717");
//        BigDecimal F0 = Generic_BigDecimal.log(
//                10,
//                new BigDecimal("0.9998268"),
//                tripleDecimalPlaces,
//                aRoundingMode).subtract(BigDecimal.ONE);

//        BigDecimal tenPowow = Generic_BigDecimal.power(
//                BigDecimal.TEN, 
//                new BigDecimal("0.48401603"),
//                tripleDecimalPlaces, 
//                aRoundingMode);


        //double f02 = Math.pow(10, 0.48401603)/10.0d;
//        BigDecimal F02 = Generic_BigDecimal.divideRoundIfNecessary(
//                tenPowow, 
//                BigDecimal.TEN,
//                tripleDecimalPlaces, 
//                aRoundingMode);

        //System.out.println("F0 " + F0);
        // NatGrid true origin is 49ºN, 2ºW
        BigDecimal lat0 = toRadians(
                new BigDecimal("49"),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal lon0 = toRadians(
                new BigDecimal("-2"),
                hDecimalPlaces,
                aRoundingMode);
        // northing & easting of true origin, metres
        BigDecimal N0 = new BigDecimal("-100000");
        BigDecimal E0 = new BigDecimal("400000");
        //double e2 = 1 - (b * b) / (a * a); // eccentricity squared
        BigDecimal b2 = b.multiply(b);
        BigDecimal a2 = a.multiply(a);
        BigDecimal b2overa2 = Generic_BigDecimal.divideRoundIfNecessary(
                b2,
                a2,
                hDecimalPlaces,
                aRoundingMode);
        // eccentricity squared
        BigDecimal e2 = BigDecimal.ONE.subtract(b2overa2);
        //System.out.println("Eccentricity squared " + e2);
        BigDecimal asubtractb = a.subtract(b);
        BigDecimal aaddb = a.add(b);
        //double n = (a - b) / (a + b);
        BigDecimal n = Generic_BigDecimal.divideRoundIfNecessary(
                asubtractb,
                aaddb,
                hDecimalPlaces,
                aRoundingMode);
        //System.out.println("n " + n);
        //double n2 = n * n;
        BigDecimal n2 = n.multiply(n);
        //System.out.println("n2 " + n2);
        //double n3 = n * n * n;
        BigDecimal n3 = n2.multiply(n);
        //System.out.println("n3 " + n3);

        Generic_BigDecimal aGeneric_BigDecimal = new Generic_BigDecimal(1000);
        //double cosLat = Math.cos(lat);
        BigDecimal cosLat = Generic_BigDecimal.cos(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        //double sinLat = Math.sin(lat);
        BigDecimal sinLat = Generic_BigDecimal.sin(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        //double nu = a * F0 / Math.sqrt(1 - e2 * sinLat * sinLat); // transverse radius of curvature
        BigDecimal nunum = a.multiply(F0);
        BigDecimal nubit = BigDecimal.ONE.subtract(e2.multiply(sinLat.multiply(sinLat)));
        BigDecimal nuden = Generic_BigDecimal.sqrt(
                nubit,
                hDecimalPlaces,
                aRoundingMode);
        // transverse radius of curvature
        BigDecimal nu = Generic_BigDecimal.divideRoundIfNecessary(
                nunum,
                nuden,
                hDecimalPlaces,
                aRoundingMode);
        //System.out.println("Transverse radius of curvature " + nu);
        //double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sinLat * sinLat, 1.5); // meridional radius of curvature
        BigDecimal rhonum = nunum.multiply(BigDecimal.ONE.subtract(e2));
//        BigDecimal rhoden2 = Generic_BigDecimal.power(
//                nubit,
//                new BigDecimal("1.5"),
//                tripleDecimalPlaces,
//                aRoundingMode);
        BigDecimal rhoden = nuden.multiply(nubit);
        BigDecimal rho = Generic_BigDecimal.divideRoundIfNecessary(
                rhonum,
                rhoden,
                hDecimalPlaces,
                aRoundingMode); // meridional radius of curvature
        //double eta2 = nu / rho - 1;
        BigDecimal eta2 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                rho,
                hDecimalPlaces,
                aRoundingMode).subtract(BigDecimal.ONE);
        //double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (lat - lat0);
        BigDecimal three = BigDecimal.valueOf(3);
        BigDecimal four = BigDecimal.valueOf(4);
        BigDecimal five = BigDecimal.valueOf(5);
        BigDecimal fivebyfour = Generic_BigDecimal.divideRoundIfNecessary(
                five,
                four,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal latSubtractLat0 = lat.subtract(lat0);
        BigDecimal Ma = (BigDecimal.ONE.add(n.add(fivebyfour.multiply(n2)).add(fivebyfour.multiply(n3)))).multiply(latSubtractLat0);
        //double Mb = (3 * n + 3 * n * n + (21D / 8D) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0);
        BigDecimal twentyOneOverEight = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(21),
                BigDecimal.valueOf(8),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal sinLatSubtractLat0 = Generic_BigDecimal.sin(
                latSubtractLat0,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal latAddLat0 = lat.add(lat0);
        BigDecimal cosLatAddLat0 = Generic_BigDecimal.cos(
                latAddLat0,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal Mb = (((three.multiply(n)).add((three.multiply(n2)))).add(twentyOneOverEight.multiply(n3))).multiply(sinLatSubtractLat0.multiply(cosLatAddLat0));
        //double Mc = ((15D / 8D) * n2 + (15D / 8D) * n3) * Math.sin(2D * (lat - lat0)) * Math.cos(2D * (lat + lat0));
        BigDecimal fifteenOverEight = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(8),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal sin2LatSubtractLat0 = Generic_BigDecimal.sin(
                BigDecimal.valueOf(2).multiply(latSubtractLat0),
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal cos2LatAddLat0 = Generic_BigDecimal.cos(
                BigDecimal.valueOf(2).multiply(latAddLat0),
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal Mc = ((fifteenOverEight.multiply(n2)).add(fifteenOverEight.multiply(n3))).multiply(sin2LatSubtractLat0).multiply(cos2LatAddLat0);
        //double Md = (35D / 24D) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3D * (lat + lat0));
        BigDecimal thirtyFiveOverTwentyFour = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(35),
                BigDecimal.valueOf(24),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal sin3LatSubtractLat0 = Generic_BigDecimal.sin(
                BigDecimal.valueOf(3).multiply(latSubtractLat0),
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal cos3LatAddLat0 = Generic_BigDecimal.cos(
                BigDecimal.valueOf(3).multiply(latAddLat0),
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal Md = thirtyFiveOverTwentyFour.multiply(n3).multiply(sin3LatSubtractLat0).multiply(cos3LatAddLat0);
        //double M = b * F0 * (Ma - Mb + Mc - Md); // meridional arc
        BigDecimal M = b.multiply(F0).multiply(Ma.subtract(Mb).add(Mc).subtract(Md)); // meridional arc
        //double cos3lat = cosLat * cosLat * cosLat;
        BigDecimal cos3lat = cosLat.multiply(cosLat).multiply(cosLat);
        //double cos5lat = cos3lat * cosLat * cosLat;
        BigDecimal cos5lat = cos3lat.multiply(cosLat).multiply(cosLat);
        //double tan2lat = Math.tan(lat) * Math.tan(lat);
        BigDecimal tanLat = Generic_BigDecimal.tan(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal tan2lat = tanLat.multiply(tanLat);
        //double tan4lat = tan2lat * tan2lat;
        BigDecimal tan4lat = tan2lat.multiply(tan2lat);

        //double I = M + N0;
        BigDecimal I = M.add(N0);
        //double II = (nu / 2D) * sinLat * cosLat;
        BigDecimal nuBy2 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                BigDecimal.valueOf(2),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal II = nuBy2.multiply(sinLat).multiply(cosLat);
        //double III = (nu / 24D) * sinLat * cos3lat * (5 - tan2lat + 9 * eta2);
        BigDecimal nuBy24 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                BigDecimal.valueOf(24),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal III = nuBy24.multiply(sinLat).multiply(cos3lat).multiply(BigDecimal.valueOf(5).subtract(tan2lat).add(BigDecimal.valueOf(9).multiply(eta2)));
        //double IIIA = (nu / 720D) * sinLat * cos5lat * (61 - 58 * tan2lat + tan4lat);
        BigDecimal nuBy720 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                BigDecimal.valueOf(720),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal IIIA = nuBy720.multiply(sinLat).multiply(cos5lat).multiply(BigDecimal.valueOf(61).subtract((BigDecimal.valueOf(58).multiply(tan2lat)).add(tan4lat)));
        //double IV = nu * cosLat;
        BigDecimal IV = nu.multiply(cosLat);
        //double V = (nu / 6D) * cos3lat * (nu / rho - tan2lat);
        BigDecimal nuBy6 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                BigDecimal.valueOf(6),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal nuByRhoSubtractTan2Lat = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                rho.subtract(tan2lat),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal V = nuBy6.multiply(cos3lat).multiply(nuByRhoSubtractTan2Lat);
        //double VI = (nu / 120D) * cos5lat * (5 - 18 * tan2lat + tan4lat + 14 * eta2 - 58 * tan2lat * eta2);
        BigDecimal nuBy120 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                BigDecimal.valueOf(120),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal VI = nuBy120.multiply(cos5lat).multiply(BigDecimal.valueOf(5).subtract(BigDecimal.valueOf(18).multiply(tan2lat)).add(tan4lat).add(BigDecimal.valueOf(14).multiply(eta2)).subtract(BigDecimal.valueOf(58).multiply(tan2lat).multiply(eta2)));

        //double dLon = lon - lon0;
        BigDecimal dLon = lon.subtract(lon0);
        //double dLon2 = dLon * dLon;
        BigDecimal dLon2 = dLon.multiply(dLon);
        //double dLon3 = dLon2 * dLon;
        BigDecimal dLon3 = dLon2.multiply(dLon);
        //double dLon4 = dLon3 * dLon;
        BigDecimal dLon4 = dLon3.multiply(dLon);
        //double dLon5 = dLon4 * dLon;
        BigDecimal dLon5 = dLon4.multiply(dLon);
        //double dLon6 = dLon5 * dLon;
        BigDecimal dLon6 = dLon5.multiply(dLon);

        //double N = I + II * dLon2 + III * dLon4 + IIIA * dLon6;
        BigDecimal N = I.add(II.multiply(dLon2)).add(III.multiply(dLon4)).add(IIIA.multiply(dLon6));
        //double E = E0 + IV * dLon + V * dLon3 + VI * dLon5;
        BigDecimal E = E0.add(IV.multiply(dLon)).add(V.multiply(dLon3)).add(VI.multiply(dLon5));
        //System.out.println("E " + E + ", N " + N);
        result[0] = Generic_BigDecimal.roundIfNecessary(E, decimalPlaces, aRoundingMode);
        result[1] = Generic_BigDecimal.roundIfNecessary(N, decimalPlaces, aRoundingMode);
        return result;
        //return new OsGridRef(E, N);
    }

    /**
     * Convert Ordnance Survey easting-northing coordinate to a WGS84
     * latitude-longitude coordinate
     *
     * @param easting The easting to be converted
     * @param northing The northing to be converted
     * @return result double[2] where result[0] is latitude and result[1] is
     * longitude
     */
    public static double[] osgb2latlon(double easting, double northing) {
        double[] result = new double[2];

        double a = 6377563.396;
        double b = 6356256.910;            // Airy 1830 major & minor semi-axes
        double F0 = 0.9996012717;          // NatGrid scale factor on central meridian
        double lat0 = toRadians(49);
        double lon0 = toRadians(-2);       // NatGrid true origin is 49ºN,2ºW
        double N0 = -100000;
        double E0 = 400000;                // northing & easting of true origin, metres
        double e2 = 1 - (b * b) / (a * a); // eccentricity squared
        //System.out.println("e2 " + e2);
        double n = (a - b) / (a + b);
        //System.out.println("n " + n);
        double n2 = n * n;
        double n3 = n * n * n;

        double lat = lat0;
        double M = 0;
        do {
            lat = (northing - N0 - M) / (a * F0) + lat;
            //System.out.println("lat " + lat);
            double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (lat - lat0);
            //System.out.println("Ma " + Ma);
            double Mb = (3 * n + 3 * n * n + (21 / 8) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0);
            //System.out.println("Mb " + Mb);
            double Mc = ((15 / 8) * n2 + (15 / 8) * n3) * Math.sin(2 * (lat - lat0)) * Math.cos(2 * (lat + lat0));
            //System.out.println("Mc " + Mc);
            double Md = (35 / 24) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3 * (lat + lat0));
            //System.out.println("Md " + Md);
            M = b * F0 * (Ma - Mb + Mc - Md);    // meridional arc
            //System.out.println("M " + M);
            //break;
            //System.out.println("northing - N0 - M " + (northing - N0 - M));

        } while (northing - N0 - M >= 0.0001);  // ie until < 0.01mm
        System.out.println("lat " + lat);
        double cosLat = Math.cos(lat);
        System.out.println("cosLat " + cosLat);
        double sinLat = Math.sin(lat);
        System.out.println("sinLat " + sinLat);
        double sin2Lat = sinLat * sinLat;
        System.out.println("sin2Lat " + sin2Lat);
        double nu = a * F0 / Math.sqrt(1 - e2 * sin2Lat);                 // transverse radius of curvature
        System.out.println("nu " + nu);
        double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sin2Lat, 1.5); // meridional radius of curvature
        System.out.println("rho " + rho);
        double eta2 = nu / rho - 1;
        System.out.println("eta2 " + eta2);

        double tanLat = Math.tan(lat);
        System.out.println("tanLat " + tanLat);
        double tan2lat = tanLat * tanLat;
        double tan4lat = tan2lat * tan2lat;
        double tan6lat = tan4lat * tan2lat;
        System.out.println("tan6lat " + tan6lat);
        double secLat = 1 / cosLat;
        double nu3 = nu * nu * nu;
        double nu5 = nu3 * nu * nu;
        double nu7 = nu5 * nu * nu;
        double VII = tanLat / (2 * rho * nu);
        System.out.println("VII " + VII);
        double VIII = tanLat / (24 * rho * nu3) * (5 + 3 * tan2lat + eta2 - 9 * tan2lat * eta2);
        System.out.println("VIII " + VIII);
        double IX = tanLat / (720 * rho * nu5) * (61 + 90 * tan2lat + 45 * tan4lat);
        System.out.println("IX " + IX);
        double X = secLat / nu;
        System.out.println("X " + X);
        double XI = secLat / (6 * nu3) * (nu / rho + 2 * tan2lat);
        System.out.println("XI " + XI);
        double XII = secLat / (120 * nu5) * (5 + 28 * tan2lat + 24 * tan4lat);
        System.out.println("XII " + XII);
        double XIIA = secLat / (5040 * nu7) * (61 + 662 * tan2lat + 1320 * tan4lat + 720 * tan6lat);
        System.out.println("XIIA " + XIIA);
        double dE = (easting - E0);
        double dE2 = dE * dE;
        double dE3 = dE2 * dE;
        double dE4 = dE2 * dE2;
        double dE5 = dE3 * dE2;
        double dE6 = dE4 * dE2;
        double dE7 = dE5 * dE2;
        lat = lat - VII * dE2 + VIII * dE4 - IX * dE6;
        double lon = lon0 + X * dE - XI * dE3 + XII * dE5 - XIIA * dE7;
        result[0] = toDegrees(lat);
        result[1] = toDegrees(lon);
        //System.out.println("lat " + lat + ", lon " + lon);
        return result;
    }

    /**
     * Convert Ordnance Survey easting-northing coordinate to a WGS84
     * latitude-longitude coordinate
     *
     * @param easting The easting to be converted
     * @param northing The northing to be converted
     * @param decimalPlaces
     * @param aRoundingMode
     * @return result double[2] where result[0] is latitude and result[1] is
     * longitude
     */
    public static BigDecimal[] osgb2latlon(
            BigDecimal easting,
            BigDecimal northing,
            int decimalPlaces,
            RoundingMode aRoundingMode) {
        //int doubleDecimalPlaces = decimalPlaces * 2;
        //int tripleDecimalPlaces = decimalPlaces * 3;
        int hDecimalPlaces = Math.max(decimalPlaces * 10, 100);
        BigDecimal precision = new BigDecimal(BigInteger.ONE, decimalPlaces);
        BigDecimal[] result = new BigDecimal[2];
        /*
         * Airy 1830 major semi-axis
         */
        BigDecimal a = new BigDecimal("6377563.396");
        /*
         * Airy 1830 minor semi-axis
         */
        //BigDecimal b = new BigDecimal("6356256.910"); 
        BigDecimal b = new BigDecimal("6356256.909");
        /*
         * NatGrid scale factor on central meridian
         */
        BigDecimal F0 = new BigDecimal("0.9996012717");
//        BigDecimal tenPowow = Generic_BigDecimal.power(
//                BigDecimal.TEN, 
//                new BigDecimal("0.48401603"),
//                tripleDecimalPlaces, 
//                aRoundingMode);
//        BigDecimal F02 = Generic_BigDecimal.divideRoundIfNecessary(
//                tenPowow, 
//                BigDecimal.TEN,
//                tripleDecimalPlaces, 
//                aRoundingMode);
//        BigDecimal F0 = Generic_BigDecimal.log(
//                10,
//                new BigDecimal("0.9998268").subtract(BigDecimal.ONE),
//                decimalPlaces,
//                aRoundingMode);
        // NatGrid true origin is 49ºN,2ºW
        BigDecimal lat0 = toRadians(
                new BigDecimal("49"),
                hDecimalPlaces,
                aRoundingMode);
        //System.out.println("lat0 " + lat0);
        BigDecimal lon0 = toRadians(
                new BigDecimal("-2"),
                hDecimalPlaces,
                aRoundingMode);
        // northing & easting of true origin, metres
        BigDecimal N0 = new BigDecimal("-100000");
        BigDecimal E0 = new BigDecimal("400000");
        //double e2 = 1 - (b * b) / (a * a); // eccentricity squared
        BigDecimal b2 = b.multiply(b);
        BigDecimal a2 = a.multiply(a);
        BigDecimal b2overa2 = Generic_BigDecimal.divideRoundIfNecessary(
                b2,
                a2,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal e2 = BigDecimal.ONE.subtract(b2overa2); // eccentricity squared
        //System.out.println("e2 " + e2);
        //double n = (a - b) / (a + b);
        BigDecimal asubtractb = a.subtract(b);
        BigDecimal aaddb = a.add(b);
        BigDecimal n = Generic_BigDecimal.divideRoundIfNecessary(
                asubtractb,
                aaddb,
                hDecimalPlaces,
                aRoundingMode);
        //System.out.println("n " + n);
        //double n2 = n * n;
        BigDecimal n2 = n.multiply(n);
        //double n3 = n * n * n;
        BigDecimal n3 = n2.multiply(n);

        Generic_BigDecimal aGeneric_BigDecimal = new Generic_BigDecimal(1000);
        BigDecimal two = BigDecimal.valueOf(2);
        BigDecimal three = BigDecimal.valueOf(3);
        BigDecimal fiveBy4 = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(4),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal twenty1By8 = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(21),
                BigDecimal.valueOf(8),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal fifteenBy8 = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(8),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal thirty5Over24 = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.valueOf(35),
                BigDecimal.valueOf(24),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal lat = new BigDecimal(lat0.toString());
        BigDecimal previousComparitor = new BigDecimal("100");
        BigDecimal M = BigDecimal.ZERO;
        boolean loop = true;
        do {
            //lat = (northing - N0 - M) / (a * F0) + lat;
            lat = Generic_BigDecimal.divideRoundIfNecessary(
                    northing.subtract(N0).subtract(M),
                    (a.multiply(F0)),
                    hDecimalPlaces,
                    aRoundingMode).add(lat);
            //System.out.println("lat " + lat);
            //double Ma = (1 + n + (5 / 4) * n2 + (5 / 4) * n3) * (lat - lat0);
            BigDecimal latSubtractLat0 = lat.subtract(lat0);
            BigDecimal Ma = (BigDecimal.ONE.add(n).add(fiveBy4.multiply(n2)).add(fiveBy4.multiply(n3))).multiply(latSubtractLat0);
            //System.out.println("Ma " + Ma);
            //double Mb = (3 * n + 3 * n * n + (21 / 8) * n3) * Math.sin(lat - lat0) * Math.cos(lat + lat0);
            BigDecimal sinLatSubtractLat0 = Generic_BigDecimal.sin(
                    latSubtractLat0,
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal latAddLat0 = lat.add(lat0);
            BigDecimal cosLatAddLat0 = Generic_BigDecimal.cos(
                    latAddLat0,
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal Mb = (three.multiply(n).add(three.multiply(n2)).add(twenty1By8.multiply(n3))).multiply(sinLatSubtractLat0).multiply(cosLatAddLat0);
            //System.out.println("Mb " + Mb);
            //double Mc = ((15 / 8) * n2 + (15 / 8) * n3) * Math.sin(2 * (lat - lat0)) * Math.cos(2 * (lat + lat0));
            BigDecimal sin2LatSubtractLat0 = Generic_BigDecimal.sin(
                    two.multiply(latSubtractLat0),
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal cos2LatAddLat0 = Generic_BigDecimal.cos(
                    two.multiply(latAddLat0),
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal Mc = ((fifteenBy8.multiply(n2)).add(fifteenBy8.multiply(n3))).multiply(sin2LatSubtractLat0).multiply(cos2LatAddLat0);
            //System.out.println("Mc " + Mc);
            //double Md = (35 / 24) * n3 * Math.sin(3 * (lat - lat0)) * Math.cos(3 * (lat + lat0));
            BigDecimal sin3LatSubtractLat0 = Generic_BigDecimal.sin(
                    three.multiply(latSubtractLat0),
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal cos3LatAddLat0 = Generic_BigDecimal.cos(
                    three.multiply(latAddLat0),
                    aGeneric_BigDecimal,
                    hDecimalPlaces,
                    aRoundingMode);
            BigDecimal Md = thirty5Over24.multiply(n3).multiply(sin3LatSubtractLat0).multiply(cos3LatAddLat0);
            //System.out.println("Md " + Md);
            //M = b * F0 * (Ma - Mb + Mc - Md);    // meridional arc
            M = b.multiply(F0).multiply(Ma.subtract(Mb).add(Mc).subtract(Md));    // meridional arc
            //System.out.println("M " + M);
            BigDecimal comparitor = northing.subtract(N0).subtract(M);
//            if (comparitor.compareTo(BigDecimal.ZERO) == -1) {
//                comparitor = comparitor.negate();
//            }
            loop = comparitor.compareTo(precision) == 1;
            if (loop) {
                //System.out.println(comparitor + " > " + precision);
                if (previousComparitor.compareTo(comparitor) == 0) {
                    //System.out.println("(previousLat.compareTo(lat) == 0) breaking out of loop");
                    loop = false;
                }
                previousComparitor = new BigDecimal(comparitor.toString());
            } else {
                //System.out.println(comparitor + " <= " + precision);
            }
        } while (loop);  // ie until < 0.01mm
        System.out.println("lat " + lat);

        //double cosLat = Math.cos(lat);
        BigDecimal cosLat = Generic_BigDecimal.cos(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("cosLat " + cosLat);
        //double sinLat = Math.sin(lat);
        BigDecimal sinLat = Generic_BigDecimal.sin(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("sinLat " + sinLat);
        //double nu = a * F0 / Math.sqrt(1 - e2 * sinLat * sinLat);                 // transverse radius of curvature
        BigDecimal sin2Lat = sinLat.multiply(sinLat);
        BigDecimal splurge = Generic_BigDecimal.sqrt(
                BigDecimal.ONE.subtract(e2.multiply(sin2Lat)),
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal aMultiplyF0 = a.multiply(F0);
        // transverse radius of curvature
        BigDecimal nu = Generic_BigDecimal.divideRoundIfNecessary(
                aMultiplyF0,
                splurge,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("nu " + nu);
        //double rho = a * F0 * (1 - e2) / Math.pow(1 - e2 * sinLat * sinLat, 1.5); // meridional radius of curvature
        BigDecimal sqrtSplurge = Generic_BigDecimal.sqrt(
                splurge,
                hDecimalPlaces,
                aRoundingMode);
        //BigDecimal denom2 = splurge.multiply(sqrtSplurge);
        BigDecimal denom = Generic_BigDecimal.power(
                splurge, 
                new BigDecimal(1.5),
                hDecimalPlaces,
                aRoundingMode);
        // meridional radius of curvature
        BigDecimal rho = Generic_BigDecimal.divideRoundIfNecessary(
                aMultiplyF0.multiply(BigDecimal.ONE.subtract(e2)),
                denom,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("rho " + rho);
        //double eta2 = nu / rho - 1;
        BigDecimal eta2 = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                rho,
                hDecimalPlaces,
                aRoundingMode).subtract(BigDecimal.ONE);
        System.out.println("eta2 " + eta2);
        //double tanLat = Math.tan(lat);
        BigDecimal tanLat = Generic_BigDecimal.tan(
                lat,
                aGeneric_BigDecimal,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("tanLat " + tanLat);
        //double tan2lat = tanLat * tanLat;
        BigDecimal tan2Lat = tanLat.multiply(tanLat);
        //double tan4lat = tan2lat * tan2lat;
        BigDecimal tan4Lat = tan2Lat.multiply(tan2Lat);
        //double tan6lat = tan4lat * tan2lat;
        BigDecimal tan6Lat = tan4Lat.multiply(tan2Lat);
        System.out.println("tan6Lat " + tan6Lat);
        //double secLat = 1 / cosLat;
        BigDecimal secLat = Generic_BigDecimal.divideRoundIfNecessary(
                BigDecimal.ONE,
                cosLat,
                hDecimalPlaces,
                aRoundingMode);
        //double nu3 = nu * nu * nu;
        BigDecimal nu2 = nu.multiply(nu);
        BigDecimal nu3 = nu.multiply(nu2);
        //double nu5 = nu3 * nu * nu;
        BigDecimal nu5 = nu3.multiply(nu2);
        //double nu7 = nu5 * nu * nu;
        BigDecimal nu7 = nu5.multiply(nu2);
        //double VII = tanLat / (2 * rho * nu);
        BigDecimal VII = Generic_BigDecimal.divideRoundIfNecessary(
                tanLat,
                two.multiply(rho).multiply(nu),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("VII " + VII);
        //double VIII = tanLat / (24 * rho * nu3) * (5 + 3 * tan2lat + eta2 - 9 * tan2lat * eta2);
        BigDecimal VIII = Generic_BigDecimal.divideRoundIfNecessary(
                tanLat,
                (BigDecimal.valueOf(24).multiply(rho).multiply(nu3)).multiply(BigDecimal.valueOf(5).add(three.multiply(tan2Lat)).add(eta2).subtract(BigDecimal.valueOf(9).multiply(tan2Lat).multiply(eta2))),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("VIII " + VIII);
        //double IX = tanLat / (720 * rho * nu5) * (61 + 90 * tan2lat + 45 * tan4lat);
        BigDecimal IX = Generic_BigDecimal.divideRoundIfNecessary(
                tanLat,
                (BigDecimal.valueOf(720).multiply(rho).multiply(nu5)).multiply(BigDecimal.valueOf(61).add(BigDecimal.valueOf(90).multiply(tan2Lat)).add(BigDecimal.valueOf(45).multiply(tan4Lat))),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("IX " + IX);
        //double X = secLat / nu;
        BigDecimal X = Generic_BigDecimal.divideRoundIfNecessary(
                secLat,
                nu,
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("X " + X);
        //double XI = secLat / (6 * nu3) * (nu / rho + 2 * tan2lat);
        BigDecimal nuByRho = Generic_BigDecimal.divideRoundIfNecessary(
                nu,
                rho,
                hDecimalPlaces,
                aRoundingMode);
        BigDecimal XI = Generic_BigDecimal.divideRoundIfNecessary(
                secLat,
                BigDecimal.valueOf(6).multiply(nu3).multiply(nuByRho.add(two.multiply(tan2Lat))),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("XI " + XI);
        //double XII = secLat / (120 * nu5) * (5 + 28 * tan2lat + 24 * tan4lat);
        BigDecimal XII = Generic_BigDecimal.divideRoundIfNecessary(
                secLat,
                BigDecimal.valueOf(120).multiply(nu5).multiply(BigDecimal.valueOf(5).add(BigDecimal.valueOf(28).multiply(tan2Lat).add(BigDecimal.valueOf(24).multiply(tan4Lat)))),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("XII " + XII);
        //double XIIA = secLat / (5040 * nu7) * (61 + 662 * tan2lat + 1320 * tan4lat + 720 * tan6lat);
        BigDecimal XIIA = Generic_BigDecimal.divideRoundIfNecessary(
                secLat,
                BigDecimal.valueOf(5040).multiply(nu7).multiply(BigDecimal.valueOf(61).add(BigDecimal.valueOf(662).multiply(tan2Lat).add(BigDecimal.valueOf(1320).multiply(tan4Lat).add(BigDecimal.valueOf(720).multiply(tan6Lat))))),
                hDecimalPlaces,
                aRoundingMode);
        System.out.println("XIIA " + XIIA);

        //double dE = (easting - E0);
        BigDecimal dE = easting.subtract(E0);
        //double dE2 = dE * dE;
        BigDecimal dE2 = dE.multiply(dE);
        //double dE3 = dE2 * dE;
        BigDecimal dE3 = dE2.multiply(dE);
        //double dE4 = dE2 * dE2;
        BigDecimal dE4 = dE3.multiply(dE);
        //double dE5 = dE3 * dE2;
        BigDecimal dE5 = dE4.multiply(dE);
        //double dE6 = dE4 * dE2;
        BigDecimal dE6 = dE5.multiply(dE);
        //double dE7 = dE5 * dE2;
        BigDecimal dE7 = dE6.multiply(dE);
        //lat = lat - VII * dE2 + VIII * dE4 - IX * dE6;
        lat = lat.subtract(VII.multiply(dE2)).add(VIII.multiply(dE4)).subtract(IX.multiply(dE6));
        //double lon = lon0 + X * dE - XI * dE3 + XII * dE5 - XIIA * dE7;
        BigDecimal lon = lon0.add(X.multiply(dE)).subtract(XI.multiply(dE3)).add(XII.multiply(dE5)).subtract(XIIA.multiply(dE7));
        result[0] = toDegrees(lat, decimalPlaces, aRoundingMode);
        result[1] = toDegrees(lon, decimalPlaces, aRoundingMode);
        //System.out.println("lat " + lat + ", lon " + lon);
        return result;
    }

    public static double toRadians(double d) {
        return d * Math.PI / 180D;
    }

    public static BigDecimal toRadians(
            BigDecimal d,
            int decimalPlaces,
            RoundingMode aRoundingMode) {
        return Generic_BigDecimal.divideRoundIfNecessary(
                d.multiply(getPI()),
                BigDecimal.valueOf(180L),
                decimalPlaces,
                aRoundingMode);
    }

    public static double toDegrees(double d) {
        return d * 180D / Math.PI;
    }

    public static BigDecimal toDegrees(
            BigDecimal d,
            int decimalPlaces,
            RoundingMode aRoundingMode) {
        return Generic_BigDecimal.divideRoundIfNecessary(
                d.multiply(BigDecimal.valueOf(180L)),
                getPI(),
                decimalPlaces,
                aRoundingMode);
    }
///**
// * Converts standard grid reference ('SU387148') to fully numeric ref ([438700,114800]);
// *   returned co-ordinates are in metres, centred on supplied grid square;
// *
// * @param {String} gridref: Standard format OS grid reference
// * @returns {OsGridRef}     Numeric version of grid reference in metres from false origin
// */
//OsGridRef.parse = function(gridref) {
//  gridref = gridref.trim();
//  // get numeric values of letter references, mapping A->0, B->1, C->2, etc:
//  double l1 = gridref.toUpperCase().charCodeAt(0) - 'A'.charCodeAt(0);
//  double l2 = gridref.toUpperCase().charCodeAt(1) - 'A'.charCodeAt(0);
//  // shuffle down letters after 'I' since 'I' is not used in grid:
//  if (l1 > 7) l1--;
//  if (l2 > 7) l2--;
//
//  // convert grid letters into 100km-square indexes from false origin (grid square SV):
//  double e = ((l1-2)%5)*5 + (l2%5);
//  double n = (19-Math.floor(l1/5)*5) - Math.floor(l2/5);
//  if (e<0 || e>6 || n<0 || n>12) return new OsGridRef(NaN, NaN);
//
//  // skip grid letters to get numeric part of ref, stripping any spaces:
//  gridref = gridref.slice(2).replace(/ /g,'');
//
//  // append numeric part of references to grid index:
//  e += gridref.slice(0, gridref.length/2);
//  n += gridref.slice(gridref.length/2);
//
//  // normalise to 1m grid, rounding up to centre of grid square:
//  switch (gridref.length) {
//    case 0: e += '50000'; n += '50000'; break;
//    case 2: e += '5000'; n += '5000'; break;
//    case 4: e += '500'; n += '500'; break;
//    case 6: e += '50'; n += '50'; break;
//    case 8: e += '5'; n += '5'; break;
//    case 10: break; // 10-digit refs are already 1m
//    default: return new OsGridRef(NaN, NaN);
//  }
//
//  return new OsGridRef(e, n);
//}
///**
// * Converts this numeric grid reference to standard OS grid reference
// *
// * @param {Number} [digits=6] Precision of returned grid reference (6 digits = metres)
// * @return {String)           This grid reference in standard format
// */
//OsGridRef.prototype.toString = function(digits) {
//  digits = (typeof digits == 'undefined') ? 10 : digits;
//  e = this.easting, n = this.northing;
//  if (e==NaN || n==NaN) return '??';
//  
//  // get the 100km-grid indices
//  var e100k = Math.floor(e/100000), n100k = Math.floor(n/100000);
//  
//  if (e100k<0 || e100k>6 || n100k<0 || n100k>12) return '';
//
//  // translate those into numeric equivalents of the grid letters
//  var l1 = (19-n100k) - (19-n100k)%5 + Math.floor((e100k+10)/5);
//  var l2 = (19-n100k)*5%25 + e100k%5;
//
//  // compensate for skipped 'I' and build grid letter-pairs
//  if (l1 > 7) l1++;
//  if (l2 > 7) l2++;
//  var letPair = String.fromCharCode(l1+'A'.charCodeAt(0), l2+'A'.charCodeAt(0));
//
//  // strip 100km-grid indices from easting & northing, and reduce precision
//  e = Math.floor((e%100000)/Math.pow(10,5-digits/2));
//  n = Math.floor((n%100000)/Math.pow(10,5-digits/2));
//
//  var gridRef = letPair + ' ' + e.padLz(digits/2) + ' ' + n.padLz(digits/2);
//
//  return gridRef;
//}
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  */
///** Trims whitespace from string (q.v. blog.stevenlevithan.com/archives/faster-trim-javascript) */
//if (typeof String.prototype.trim == 'undefined') {
//  String.prototype.trim = function() {
//    return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
//  }
//}
///** Pads a number with sufficient leading zeros to make it w chars wide */
//if (typeof String.prototype.padLz == 'undefined') {
//  Number.prototype.padLz = function(w) {
//    var n = this.toString();
//    var l = n.length;
//    for (var i=0; i<w-l; i++) n = '0' + n;
//    return n;
//  }
//}
}
