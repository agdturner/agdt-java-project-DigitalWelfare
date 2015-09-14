package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_StaticString;
import uk.ac.leeds.ccg.andyt.generic.utilities.Generic_StaticTime;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.data.generated.DW_Table;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ChoroplethMaps_SHBE;
import static uk.ac.leeds.ccg.andyt.projects.digitalwelfare.process.DW_ChoroplethMaps_SHBE.getFilenames;

public class DW_Report extends DW_HTMLPage {

    protected ArrayList<String> levels;
    protected String levelsString;
    protected ArrayList<String> claimantTypes;
    protected ArrayList<String> tenureTypeGroups;
    protected ArrayList<Double> distances;
    protected ArrayList<String> types;
    protected ArrayList<String> distanceTypes;
    protected ArrayList<Integer> omit;
    protected String reportName;
    protected double distanceThreshold;
    protected String projectName;
    protected String mainDirectoryName;
    protected String baseReportDir;
    protected String baseURLString0;
    protected String date;

    public DW_Report() {
    }

    public static void main(String[] args) {
        new DW_Report().run();
    }

    public void run() {
        levels = new ArrayList<String>();
        ArrayList<String> levelsCopy;
        levelsCopy = new ArrayList<String>();
        levelsCopy.add("OA");
        levelsCopy.add("LSOA");
        levelsCopy.add("MSOA");
        levelsCopy.add("PostcodeUnit");
        levelsCopy.add("PostcodeSector");
        levelsCopy.add("PostcodeDistrict");

        lineSeparator = System.getProperty("line.separator").getBytes();
        projectName = "DigitalWelfare";
        mainDirectoryName = "Report";
        reportName = "Report1";

        claimantTypes = new ArrayList<String>();
        claimantTypes.add("HB");
        claimantTypes.add("CTB");

        tenureTypeGroups = new ArrayList<String>();
        tenureTypeGroups.add("all");
        tenureTypeGroups.add("regulated");
        tenureTypeGroups.add("unregulated");

        // Specifiy distances
        distances = new ArrayList<Double>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
            //for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        types = new ArrayList<String>();
        types.add("All"); // Count of all claimants
////        types.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add("OnFlow"); // These are people not claiming the previous month and that have not claimed before.
        types.add("ReturnFlow"); // These are people not claiming the previous month but that have claimed before.
        types.add("Stable"); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add("AllInChurn"); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add("AllOutChurn"); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        distanceTypes = new ArrayList<String>();
        distanceTypes.add("InDistanceChurn"); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add("WithinDistanceChurn"); // A count of all claimants that have moved within this area.
        distanceTypes.add("OutDistanceChurn"); // A count of all claimants that have moved out from this area.

        // Run for consecutive monthly data
        omit = new ArrayList<Integer>();
        omit.add(0);
        omit.add(1);
        omit.add(2);
        omit.add(3);
        omit.add(4);
        omit.add(5);
        omit.add(6);
        omit.add(7);
        omit.add(8);
        omit.add(9);
        omit.add(10);
        omit.add(11);
        omit.add(12);
        omit.add(13);
        omit.add(14);

        baseReportDir = mainDirectoryName + "/" + reportName;
        baseURLString0 = "http://www.geog.leeds.ac.uk/people/a.turner/projects/"
                + projectName + "/" + baseReportDir + "/";
        date = Generic_StaticTime.getDate();

        Iterator<String> levelsIte;
        levelsIte = levelsCopy.iterator();
        while (levelsIte.hasNext()) {
            String level;
            level = levelsIte.next();
            System.out.println("Write out for level " + level);
            levels.add(level);
            levelsString = level;
            write();
            levels.remove(level);
        }

        boolean writeDefinitions = true;
        if (writeDefinitions) {
            new DW_Types().run();
        }
    }

    public void write() {
        File dir0 = new File(
                DW_Files.getOutputDir(),
                baseReportDir);
        File dir1 = new File(
                dir0,
                levelsString);
        String[] tFilenames;
        tFilenames = getFilenames();

        Iterator<String> claimantTypesIte;
        claimantTypesIte = claimantTypes.iterator();
        while (claimantTypesIte.hasNext()) {
            String claimantType;
            claimantType = claimantTypesIte.next();
            String claimantType2 = Generic_StaticString.getCapitalFirstLetter(claimantType) + "Claimants";
            File dir2 = new File(
                    dir1,
                    claimantType2);
            dir2.mkdirs();
            Iterator<String> tenureIte;
            tenureIte = tenureTypeGroups.iterator();
            while (tenureIte.hasNext()) {
                String tenure = tenureIte.next();
                String tenure2 = Generic_StaticString.getCapitalFirstLetter(tenure) + "Tenure";
                String reportFilename = tenure2 + ".html";
                File f = new File(
                        dir2,
                        reportFilename);
                int filePathDepth;
                filePathDepth = Generic_StaticIO.getFileDepth(dir2)
                        - Generic_StaticIO.getFileDepth(dir0)
                        + Generic_StaticIO.getFileDepth(baseReportDir);
                String relativeFilePath;
                relativeFilePath = Generic_StaticIO.getRelativeFilePath(
                        filePathDepth);
//                String distanceRelativeFilePath;
//                distanceRelativeFilePath = Generic_StaticIO.getRelativeFilePath(
//                        filePathDepth + 1);
                String definitionsPath;
                definitionsPath = relativeFilePath + baseReportDir + "/Definitions/";
                fos = Generic_StaticIO.getFileOutputStream(f);
                String pageTitle;
                pageTitle = reportName + " " + claimantType2 + " " + tenure2;
                write(definitionsPath,
                        relativeFilePath,
                        //distanceRelativeFilePath,
                        pageTitle,
                        claimantType,
                        claimantType2,
                        tenure,
                        tenure2,
                        reportFilename,
                        tFilenames);
                try {
                    writeHTMLFooter(date);
                    fos.close();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void write(
            String definitionsPath,
            String relativeFilePath,
            //String distanceRelativeFilePath,
            String pageTitle,
            String claimantType,
            String claimantType2,
            String tenure,
            String tenure2,
            String reportFilename,
            String[] tFilenames) {
        try {
            writeHTMLHeader(
                    projectName,
                    pageTitle);
            writeStartOfBody(
                    baseURLString0,
                    pageTitle);
            Iterator<String> typesIte;
            Iterator<String> distanceTypesIte;
            Iterator<String> levelsIte;
            Iterator<Double> distancesIte;

            String claimantTypeLink;
            claimantTypeLink = getLink2(
                    claimantType2,
                    definitionsPath);
            String link;
            link = getLink2(
                    tenure2,
                    definitionsPath);

            writeLine("<div>");
            writeLine("<ul>");
            writeLine("<li><h2>Definitions</h2>");
            writeLine("<ul>");
            writeLine("<li>" + claimantTypeLink + "</li>");
            link = getLink2(
                    tenure2,
                    definitionsPath);
            writeLine("<li>" + link + "</li>");
            writeLine("<li>Types");
            writeLine("<ul>");
            typesIte = types.iterator();
            while (typesIte.hasNext()) {
                link = getLink2(
                        typesIte.next() + "Claimants",
                        definitionsPath);
                writeLine("<li>" + link + "</li>");
            }
            writeLine("</ul></li>");
            writeLine("<li>Distance Types");
            writeLine("<ul>");
            distanceTypesIte = distanceTypes.iterator();
            while (distanceTypesIte.hasNext()) {
                link = getLink2(
                        distanceTypesIte.next() + "Claimants",
                        definitionsPath);
                writeLine("<li>" + link + "</li>");
            }
            writeLine("</ul></li>");
            writeLine("<li>Levels");
            writeLine("<ul>");
            levelsIte = levels.iterator();
            while (levelsIte.hasNext()) {
                link = getLink2(
                        levelsIte.next(),
                        definitionsPath);
                writeLine("<li>" + link + "</li>");
            }
            writeLine("</ul></li>");
            writeLine("</ul>");
            writeLine("</div>");
            writeContents(tFilenames, claimantType, claimantTypeLink);

            for (int i = 0; i < tFilenames.length; i++) {
                if (!omit.contains(i)) {
                    String year;
                    year = DW_ChoroplethMaps_SHBE.getFilenameYear(tFilenames[i]);
                    String month;
                    month = DW_ChoroplethMaps_SHBE.getFilenameMonth(tFilenames[i]);
                    String yearMonth;
                    yearMonth = year + " " + month;
                    String idl;
                    idl = yearMonth.replace(" ", "_");
                    String idn;
                    idn = yearMonth;
                    writeLine("<div id=\"" + idl + "\">");
                    writeLine("<ul>");
                    writeLine("<li><h2>" + idn + "</h2>");
                    typesIte = types.iterator();
                    while (typesIte.hasNext()) {
                        String type;
                        type = typesIte.next();
                        String type2;
                        type2 = Generic_StaticString.getCapitalFirstLetter(type);
                        type2 += "Claimants";
                        idn = type2;
                        idl = yearMonth.replace(" ", "_") + "_" + idn.replace(" ", "_");
                        writeLine("<h3 id=\"" + idl + "\">" + idn + "</h3>");
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level;
                            level = levelsIte.next();
                            idn = yearMonth + " " + type2;
                            idn += " " + level;
                            idl = idn.replace(" ", "_");
                            writeStartOfSection(idn, 4);
                            writeSection(
                                    relativeFilePath,
                                    claimantType,
                                    tenure,
                                    idn,
                                    type,
                                    level,
                                    year,
                                    month);
                            writeLine("</div>");
                        }
                    }
                    distanceTypesIte = distanceTypes.iterator();
                    while (distanceTypesIte.hasNext()) {
                        String distanceType;
                        distanceType = distanceTypesIte.next();
                        String type2;
                        type2 = Generic_StaticString.getCapitalFirstLetter(distanceType);
                        type2 += "Claimants";
                        idn = yearMonth + " " + type2;
                        idl = idn.replace(" ", "_");
                        writeLine("<h3 id=\"" + idl + "\">" + idn + "</h3>");
                        distancesIte = distances.iterator();
                        while (distancesIte.hasNext()) {
                            distanceThreshold = distancesIte.next();
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                String level;
                                level = levelsIte.next();
                                idn = yearMonth + " " + type2;
                                idn += " " + distanceThreshold + " Metres";
                                idn += " " + level;
                                idl = idn.replace(" ", "_");
                                writeStartOfSection(idn, 4);
                                writeSection(
                                        relativeFilePath,
                                        claimantType,
                                        tenure,
                                        idn,
                                        distanceType,
                                        level,
                                        year,
                                        month);
                                writeLine("</div>");
                            }
                        }
                    }
                    writeLine("</li>");
                    writeLine("</ul>");
                    writeLine("</div>");
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeSection(
            String relativeFilePath,
            String claimantType,
            String tenure,
            String sectionTitle,
            String type,
            String level,
            String year,
            String month) throws IOException {
        writeLine("");
        writeLine("<h5>Choropleth Map</h5>");
        writeLine("");
        String imageSource;
        String name = year + month + "CountClippedToLeedsLAD";
        String filepath;
        filepath = claimantType + "/" + tenure + "/" + type
                + "/WithoutBoundaries/CommonlyStyled/EqualInterval/";
        if (type.contains("Distance")) {
            filepath += distanceThreshold + "/";
        }
        filepath += "all/" //all is a relic from when dealing with LSOAs within different deprivation classes
                + name + "/";
        imageSource = relativeFilePath + "LCC/SHBE/Maps/Choropleth/"
                + level + "/" + filepath + name + ".png";;
        writeLine("<img src=\"" + imageSource + "\" alt=\"[" + imageSource + "]\" />");
        writeLine("");
        writeLine("");
        File dir;
        File f;
        ArrayList<String> table;
        // Total, In and Out Count Table
        writeLine("");
        writeLine("<h5>Total, In and Out Count Table</h5>");
        writeLine("");
        dir = new File(
                DW_Files.getOutputSHBEChoroplethDir(level),
                filepath);
        f = new File(
                dir,
                name + ".txt");
        table = DW_Table.readCSV(f);
        writeTable(table);
        // Counts By Tenure Table
        writeLine("");
        writeLine("<h5>Counts By Tenure Table</h5>");
        writeLine("");
        dir = new File(
                DW_Files.getGeneratedSHBEDir(level),
                type + "/" + claimantType + "/" + tenure);
        if (type.contains("Distance")) {
            dir = new File(
                    dir,
                    "" + distanceThreshold);
        }
        f = new File(
                dir,
                "CountsByTenure" + year + month + ".csv");
        table = DW_Table.readCSV(f);
        writeTable(table);
        // Top 10s
        // Top 10
        writeExtremeAreaTable(
             dir,
             year,
             month);

        String type2;
        // Top 10 increases
        type2 = "Increases";
        //  Absolute
        name = "Absolute";
        writeExtremeAreaChangesTable(dir, name, type2, year, month);
        //  Relative
        name = "Relative";
        writeExtremeAreaChangesTable(dir, name, type2, year, month);

        // Top 10 decreases
        type2 = "Decreases";
        //  Absolute
        name = "Absolute";
        writeExtremeAreaChangesTable(dir, name, type2, year, month);
        //  Relative
        name = "Relative";
        writeExtremeAreaChangesTable(dir, name, type2, year, month);

    }

    protected void writeExtremeAreaTable(
            File dir,
            String year,
            String month) throws IOException {
        writeLine("");
        writeLine("<h5>Areas With The Highest Numbers of Claimants Table</h5>");
        writeLine("");
        File f;
        ArrayList<String> table;
        f = new File(
                dir,
                "HighestClaimants" + year + month + ".csv");
        table = DW_Table.readCSV(f);
            writeTable(table);
    }
    
    protected void writeExtremeAreaChangesTable(
            File dir,
            String name,
            String type,
            String year,
            String month) throws IOException {
        File f;
        ArrayList<String> table;
        f = new File(
                dir,
                "ExtremeAreaChanges" + name + type + "LastYear" + year + month + ".csv");
        if (f.exists()) {
            writeLine("");
            writeLine("<h5>Areas With The Largest " + name + " " + type + " In Numbers Of Claimants Table From Last Year</h5>");
            writeLine("");
            table = DW_Table.readCSV(f);
            writeTable(table);
        }
        f = new File(
                dir,
                "ExtremeAreaChanges" + name + type + "LastMonth" + year + month + ".csv");
        if (f.exists()) {
            writeLine("");
            writeLine("<h5>Areas With The Largest " + name + " " + type + " In Numbers Of Claimants Table From Last Month</h5>");
            writeLine("");
            table = DW_Table.readCSV(f);
            writeTable(table);
        }
    }

    protected void writeTable(ArrayList<String> table) throws IOException {
        //writeLine("<table style=\"width:100%\">");
        //writeLine("<table border=\"1\">");
        writeLine("<table style=\"border:1px solid black\">");
        Iterator<String> ite;
        ite = table.iterator();
        while (ite.hasNext()) {
            String row;
            row = ite.next();
            String[] split;
            split = row.split(",");
            writeLine("<tr>");
            for (String split1 : split) {
                //writeLine("<td>" + split1 + "</td>");
                writeLine("<td style=\"border:1px solid grey\">" + split1 + "</td>");
            }
            writeLine("</tr>");
        }
        writeLine("</table>");
        writeLine("");
    }

    public void writeContents(
            String[] tFilenames,
            String claimantType,
            String claimantTypeLink) throws IOException {
        writeLine("<div><ul>");
        writeLine("<li><h2>Contents</h2>");
        writeLine("<ul>");
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;
        Iterator<String> levelsIte;
        for (int i = 0; i < tFilenames.length; i++) {
            if (!omit.contains(i)) {
                String year;
                year = DW_ChoroplethMaps_SHBE.getFilenameYear(tFilenames[i]);
                String month;
                month = DW_ChoroplethMaps_SHBE.getFilenameMonth(tFilenames[i]);
                typesIte = types.iterator();
                String yearMonth;
                yearMonth = year + " " + month;
                String idn;
                String idn0;
                idn = yearMonth;
                String idl;
                idl = idn.replace(" ", "_");
                writeLine("<li>" + getLink(idl, idn));
                writeLine("<ul>");
                while (typesIte.hasNext()) {
                    String type;
                    type = typesIte.next();
                    String type2;
                    type2 = Generic_StaticString.getCapitalFirstLetter(type);
                    type2 += "Claimants";
                    idn = yearMonth + " " + type2;
                    idn0 = type2;
                    idl = idn.replace(" ", "_");
                    //writeLine("<li>" + getLink(idl, idn));
                    writeLine("<li>" + getLink(idl, idn0));
                    writeLine("<ul>");
                    levelsIte = levels.iterator();
                    while (levelsIte.hasNext()) {
                        String level;
                        level = levelsIte.next();
                        idn = yearMonth;
                        idn += " " + type2;
                        idn += " " + level;
                        idl = idn.replace(" ", "_");
                        writeLine("<li>" + getLink(idl, level) + "</li>");
                    }
                    writeLine("</ul>");
                    writeLine("</li>");
                }
                distanceTypesIte = distanceTypes.iterator();
                while (distanceTypesIte.hasNext()) {
                    String distanceType;
                    distanceType = distanceTypesIte.next();
                    String type2;
                    type2 = Generic_StaticString.getCapitalFirstLetter(distanceType);
                    type2 += "Claimants";
                    idn = yearMonth + " " + type2;
                    idl = idn.replace(" ", "_");
                    //writeLine("<li>" + getLink(idl, idn));
                    writeLine("<li>" + getLink(idl, type2));
                    writeLine("<ul>");
                    distancesIte = distances.iterator();
                    while (distancesIte.hasNext()) {
                        distanceThreshold = distancesIte.next();
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level;
                            level = levelsIte.next();
                            idn = yearMonth;
                            idn += " " + type2;
                            idn += " " + distanceThreshold + " Metres";
                            idn += " " + level;
                            idl = idn.replace(" ", "_");
                            writeLine("<li>" + getLink(idl, distanceThreshold + " Metres " + level) + "</li>");
                        }
                    }
                    writeLine("</ul>");
                    writeLine("</li>");
                }
                writeLine("</ul>");
                writeLine("</li>");
            }
        }
        writeLine("</ul>");
        writeLine("</li>");
        writeLine("</ul></div>");
    }
}
