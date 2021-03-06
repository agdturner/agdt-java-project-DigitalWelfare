package uk.ac.leeds.ccg.projects.dw.reporting;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.generic.lang.Generic_String;
import uk.ac.leeds.ccg.generic.util.Generic_Time;
import uk.ac.leeds.ccg.projects.dw.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Strings;
import uk.ac.leeds.ccg.projects.dw.data.generated.DW_Table;
import uk.ac.leeds.ccg.projects.dw.process.lcc.DW_ChoroplethMapsLCC;
import static uk.ac.leeds.ccg.projects.dw.process.lcc.DW_ChoroplethMapsLCC.getFilenames;

public class DW_Report extends DW_HTMLPage {

    protected ArrayList<String> levels;
    protected String levelsString;
    protected ArrayList<String> claimantTypes;
    protected ArrayList<String> tenureTypeGroups;
    protected ArrayList<Double> distances;
    protected ArrayList<String> types;
    protected ArrayList<String> distanceTypes;
    protected TreeMap<String, ArrayList<Integer>> includes;
    protected String reportName;
    protected double distanceThreshold;
    protected String projectName;
    protected String mainDirectoryName;
    protected String baseReportDir;
    protected String baseURLString0;
    protected String date;

    public DW_Report(DW_Environment env) throws IOException {
        super(env);
    }

    public static void main(String[] args) {
        try {
            new DW_Report(null).run();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run() throws IOException, Exception {
        levels = new ArrayList<>();
        ArrayList<String> levelsCopy;
        levelsCopy = new ArrayList<>();
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

        claimantTypes = DW_Strings.getHB_CTB();

        tenureTypeGroups = new ArrayList<>();
        tenureTypeGroups.add("all");
        tenureTypeGroups.add("regulated");
        tenureTypeGroups.add("unregulated");

        // Specifiy distances
        distances = new ArrayList<>();
        for (double distance = 1000.0d; distance < 5000.0d; distance += 1000.0d) {
            //for (double distance = 1000.0d; distance < 2000.0d; distance += 1000.0d) {
            distances.add(distance);
        }

        types = new ArrayList<>();
        types.add("All"); // Count of all claimants
////        types.add("NewEntrant"); // New entrants will include people already from Leeds. Will this also include people new to Leeds? - Probably...
        types.add("OnFlow"); // These are people not claiming the previous month and that have not claimed before.
        types.add("ReturnFlow"); // These are people not claiming the previous month but that have claimed before.
        types.add("Stable"); // The popoulation of claimants who's postcode location is the same as in the previous month.
        types.add("AllInChurn"); // A count of all claimants that have moved to this area (including all people moving within the area).
        types.add("AllOutChurn"); // A count of all claimants that have moved that were living in this area (including all people moving within the area).

        distanceTypes = new ArrayList<>();
        distanceTypes.add("InDistanceChurn"); // A count of all claimants that have moved within this area.
        // A useful indication of places where displaced people from Leeds are placed?
        distanceTypes.add("WithinDistanceChurn"); // A count of all claimants that have moved within this area.
        distanceTypes.add("OutDistanceChurn"); // A count of all claimants that have moved out from this area.

        // Run for consecutive monthly data
        includes = env.getShbeData().getIncludes();
        includes.remove("All");
//        includes.remove("Yearly");
//        includes.remove("6Monthly");
//        includes.remove("3Monthly");
//        includes.remove("MonthlyUO");
//        includes.remove("Monthly");

        baseReportDir = mainDirectoryName + "/" + reportName;
        baseURLString0 = "http://www.geog.leeds.ac.uk/people/a.turner/projects/"
                + projectName + "/" + baseReportDir + "/";
        date = Generic_Time.getDate();

        ArrayList<Boolean> b;
        b = new ArrayList<>();
        b.add(true);
        b.add(false);

        Iterator<Boolean> iteb;
        iteb = b.iterator();
        while (iteb.hasNext()) {
            boolean doUnderOccupied;
            doUnderOccupied = iteb.next();
            if (doUnderOccupied) {
                Iterator<Boolean> iteb2;
                iteb2 = b.iterator();
                while (iteb2.hasNext()) {
                    boolean doCouncil;
                    doCouncil = iteb2.next();
                    writeStartOfMaster(
                            doUnderOccupied,
                            doCouncil);
                    Iterator<String> levelsIte;
                    levelsIte = levelsCopy.iterator();
                    while (levelsIte.hasNext()) {
                        String level;
                        level = levelsIte.next();
                        System.out.println("Write out for level " + level);
                        levels.add(level);
                        levelsString = level;
                        write(
                                doUnderOccupied,
                                doCouncil);
                        writeToMaster();
                        levels.remove(level);
                    }
                    writeEndOfMaster();
                    boolean writeDefinitions = true;
                    if (writeDefinitions) {
                        new DW_Types(env).run();
                    }
                }
            } else {
                writeStartOfMaster(
                        false,
                        false);
                Iterator<String> levelsIte;
                levelsIte = levelsCopy.iterator();
                while (levelsIte.hasNext()) {
                    String level;
                    level = levelsIte.next();
                    System.out.println("Write out for level " + level);
                    levels.add(level);
                    levelsString = level;
                    write(
                            false,
                            false);
                    writeToMaster();
                    levels.remove(level);
                }
                writeEndOfMaster();
                boolean writeDefinitions = true;
                if (writeDefinitions) {
                    new DW_Types(env).run();
                }
            }
        }
    }

    public void writeStartOfMaster(
            boolean doUnderOccupied,
            boolean doCouncil) throws IOException {
        try {
            Path dirOut = Paths.get(env.files.getOutputDir().toString(), baseReportDir);
            dirOut = env.files.getUODir(dirOut, doUnderOccupied, doCouncil);
            Files.createDirectories(dirOut);
            String pageTitle = "Results";
            Path f = Paths.get(dirOut.toString(), pageTitle + ".html");
            masterFOS = Generic_IO.getBufferedOutputStream(f);
            writeHTMLHeader(projectName, pageTitle, masterFOS);
            writeStartOfBody(baseURLString0, pageTitle, masterFOS);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void writeToMaster() {
        try {
            Path dir0 = Paths.get(env.files.getOutputDir().toString(), baseReportDir);
            Path dir1 = Paths.get(dir0.toString(), levelsString);
            writeLine("<div>", masterFOS);
            writeLine("<ul>", masterFOS);
            writeLine("<li><h2>" + levelsString + "</h2>", masterFOS);
            writeLine("<ul>", masterFOS);
            //String[] tFilenames;
            //tFilenames = getFilenames();
            Iterator<String> claimantTypesIte;
            claimantTypesIte = claimantTypes.iterator();
            while (claimantTypesIte.hasNext()) {
                String claimantType;
                claimantType = claimantTypesIte.next();
                String claimantType2 = Generic_String.getCapitalFirstLetter(claimantType) + "Claimants";
                Path dir2 = Paths.get(dir1.toString(), claimantType2);
                writeLine("<li>" + claimantType2, masterFOS);
                writeLine("<ul>", masterFOS);
                //dir2.mkdirs();
                Iterator<String> tenureIte;
                tenureIte = tenureTypeGroups.iterator();
                while (tenureIte.hasNext()) {
                    String tenure = tenureIte.next();
                    String tenure2 = Generic_String.getCapitalFirstLetter(tenure) + "Tenure";
                    String reportFilename = tenure2 + ".html";
                    writeLine("<li><a href=\"./" + levelsString + "/" + claimantType2 + "/" + tenure2 + ".html\">"
                            + tenure2 + "</a></li>", masterFOS);
                }
                writeLine("</ul></li>", masterFOS);
            }
            writeLine("</ul></li>", masterFOS);
            writeLine("</ul></div>", masterFOS);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void writeEndOfMaster() {
        try {
            writeHTMLFooter(date, masterFOS);
            masterFOS.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void write(
            boolean doUnderOccupied,
            boolean doCouncil) throws FileNotFoundException, IOException {
        Path dirOut = Paths.get(env.files.getOutputDir().toString(), 
                baseReportDir, levelsString);
        dirOut = env.files.getUODir(dirOut, doUnderOccupied, doCouncil);
        String[] tFilenames;
        tFilenames = getFilenames();
        Iterator<String> claimantTypesIte;
        claimantTypesIte = claimantTypes.iterator();
        while (claimantTypesIte.hasNext()) {
            String claimantType;
            claimantType = claimantTypesIte.next();
            String claimantType2 = Generic_String.getCapitalFirstLetter(claimantType) + "Claimants";
            Path dirOut2 = Paths.get(dirOut.toString(), claimantType2);
            Files.createDirectories(dirOut2);
            Iterator<String> tenureIte;
            tenureIte = tenureTypeGroups.iterator();
            while (tenureIte.hasNext()) {
                String tenure = tenureIte.next();
                String tenure2 = Generic_String.getCapitalFirstLetter(tenure) + "Tenure";
                String reportFilename = tenure2 + ".html";
                Path f = Paths.get(dirOut2.toString(), reportFilename);
//                int filePathDepth = env.ge.io.getFileDepth(dirOut2)
//                        - env.ge.io.getFileDepth(dirOut)
//                        + env.ge.io.getFileDepth(baseReportDir);
//                String relativeFilePath;
//                relativeFilePath = env.ge.io.getRelativeFilePath(
//                        filePathDepth);
//                String distanceRelativeFilePath;
//                distanceRelativeFilePath = env.ge.io.getRelativeFilePath(
//                        filePathDepth + 1);
//                String definitionsPath;
//                definitionsPath = relativeFilePath + baseReportDir + "/Definitions/";
//                componentFOS = env.ge.io.getBufferedOutputStream(f);
//                String pageTitle;
//                pageTitle = reportName + " " + claimantType2 + " " + tenure2;
//                write(doUnderOccupied, doCouncil, definitionsPath,
//                        relativeFilePath,
//                        //distanceRelativeFilePath,
//                        pageTitle, claimantType, claimantType2, tenure, tenure2,
//                        reportFilename, tFilenames);
//                try {
//                    writeHTMLFooter(date, componentFOS);
//                    componentFOS.close();
//                } catch (IOException e) {
//                    System.err.println(e.getMessage());
//                    e.printStackTrace(System.err);
//                }
            }
        }
    }

    public void write(
            boolean doUnderOccupied,
            boolean doCouncil,
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
                    pageTitle,
                    componentFOS);
            writeStartOfBody(
                    baseURLString0,
                    pageTitle,
                    componentFOS);
            Iterator<String> typesIte;
            Iterator<String> distanceTypesIte;
            Iterator<String> levelsIte;
            Iterator<Double> distancesIte;

            String claimantTypeLink;
            claimantTypeLink = getLink2(claimantType2, definitionsPath);
            String link;
            writeLine("<div>", componentFOS);
            writeLine("<ul>", componentFOS);
            writeLine("<li><h2>Definitions</h2>", componentFOS);
            writeLine("<ul>", componentFOS);
            writeLine("<li>" + claimantTypeLink + "</li>", componentFOS);
            link = getLink2(tenure2, definitionsPath);
            writeLine("<li>" + link + "</li>", componentFOS);
            writeLine("<li>Types", componentFOS);
            writeLine("<ul>", componentFOS);
            typesIte = types.iterator();
            while (typesIte.hasNext()) {
                link = getLink2(typesIte.next() + "Claimants", definitionsPath);
                writeLine("<li>" + link + "</li>", componentFOS);
            }
            writeLine("</ul></li>", componentFOS);
            writeLine("<li>Distance Types", componentFOS);
            writeLine("<ul>", componentFOS);
            distanceTypesIte = distanceTypes.iterator();
            while (distanceTypesIte.hasNext()) {
                link = getLink2(distanceTypesIte.next() + "Claimants",
                        definitionsPath);
                writeLine("<li>" + link + "</li>", componentFOS);
            }
            writeLine("</ul></li>", componentFOS);
            writeLine("<li>Levels", componentFOS);
            writeLine("<ul>", componentFOS);
            levelsIte = levels.iterator();
            while (levelsIte.hasNext()) {
                link = getLink2(levelsIte.next(), definitionsPath);
                writeLine("<li>" + link + "</li>", componentFOS);
            }
            writeLine("</ul></li>", componentFOS);
            writeLine("</ul>", componentFOS);
            writeLine("</div>", componentFOS);
            writeContents(tFilenames, claimantType, claimantTypeLink,
                    componentFOS);

            Iterator<String> includesIte;
            includesIte = includes.keySet().iterator();
            while (includesIte.hasNext()) {
                String includeName;
                includeName = includesIte.next();
                ArrayList<Integer> include;
                include = includes.get(includeName);
                writeLine("<div id=\"" + includeName + "\">", componentFOS);
                writeLine("<ul>", componentFOS);
                writeLine("<li><h2>" + includeName + "</h2>", componentFOS);

                for (int i = 0; i < tFilenames.length; i++) {
                    if (include.contains(i)) {
                        String year;
                        year = DW_ChoroplethMapsLCC.getFilenameYear(tFilenames[i]);
                        String month;
                        month = DW_ChoroplethMapsLCC.getFilenameMonth(tFilenames[i]);
                        String yearMonth;
                        yearMonth = year + " " + month;
                        String idn;
                        idn = includeName + " " + yearMonth;
                        String idl;
                        idl = idn.replace(" ", "_");
                        writeLine("<div id=\"" + idl + "\">", componentFOS);
                        writeLine("<ul>", componentFOS);
                        writeLine("<li><h3>" + idn + "</h3>", componentFOS);
                        typesIte = types.iterator();
                        while (typesIte.hasNext()) {
                            String type;
                            type = typesIte.next();
                            String type2;
                            type2 = Generic_String.getCapitalFirstLetter(type);
                            type2 += "Claimants";
                            idn = type2;
                            idl = yearMonth.replace(" ", "_") + "_" + idn.replace(" ", "_");
                            writeLine("<h4 id=\"" + idl + "\">" + idn + "</h4>", componentFOS);
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                String level;
                                level = levelsIte.next();
                                idn = yearMonth + " " + type2;
                                idn += " " + level;
                                //idl = idn.replace(" ", "_");
                                writeStartOfSection(idn, 5, componentFOS);
                                writeSection(
                                        doUnderOccupied,
                                        doCouncil,
                                        relativeFilePath,
                                        claimantType,
                                        tenure,
                                        idn,
                                        type,
                                        level,
                                        year,
                                        month);
                                writeLine("</div>", componentFOS);
                            }
                        }
                        distanceTypesIte = distanceTypes.iterator();
                        while (distanceTypesIte.hasNext()) {
                            String distanceType;
                            distanceType = distanceTypesIte.next();
                            String type2;
                            type2 = Generic_String.getCapitalFirstLetter(distanceType);
                            type2 += "Claimants";
                            idn = yearMonth + " " + type2;
                            idl = idn.replace(" ", "_");
                            writeLine("<h4 id=\"" + idl + "\">" + idn + "</h4>", componentFOS);
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
                                    //idl = idn.replace(" ", "_");
                                    writeStartOfSection(idn, 5, componentFOS);
                                    writeSection(
                                            doUnderOccupied,
                                            doCouncil,
                                            relativeFilePath,
                                            claimantType,
                                            tenure,
                                            idn,
                                            distanceType,
                                            level,
                                            year,
                                            month);
                                    writeLine("</div>", componentFOS);
                                }
                            }
                        }
                        writeLine("</li>", componentFOS);
                        writeLine("</ul>", componentFOS);
                        writeLine("</div>", componentFOS);
                    }
                }
                writeLine("</li>", componentFOS);
                writeLine("</ul>", componentFOS);
                writeLine("</div>", componentFOS);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void writeSection(
            boolean doUnderOccupied,
            boolean doCouncil,
            String relativeFilePath,
            String claimantType,
            String tenure,
            String sectionTitle,
            String type,
            String level,
            String year,
            String month) throws IOException {
        DW_Table tab = new DW_Table(env);
        writeLine("", componentFOS);
        writeLine("<h5>Choropleth Map</h5>", componentFOS);
        writeLine("", componentFOS);
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
                + level + "/" + filepath + name + ".png";
        writeLine("<img src=\"" + imageSource + "\" alt=\"[" + imageSource + "]\" />", componentFOS);
        writeLine("", componentFOS);
        writeLine("", componentFOS);
        Path dir;
        Path f;
        ArrayList<String> table;
        // Total, In and Out Count Table
        dir = Paths.get(env.files.getOutputSHBEChoroplethDir(level).toString(), filepath);
        f = Paths.get(dir.toString(), name + ".txt");
        if (Files.exists(f)) {
            writeLine("", componentFOS);
            writeLine("<h5>Total, In and Out Count Table</h5>", componentFOS);
            writeLine("", componentFOS);
            table = tab.readCSV(f);
            writeTable(table, componentFOS);
        }
        // Counts By Tenure Table
        dir = Paths.get(env.files.getGeneratedSHBEDir(level, doUnderOccupied, doCouncil).toString(),
                type + "/" + claimantType + "/" + tenure);
        if (type.contains("Distance")) {
            dir = Paths.get(dir.toString(), "" + distanceThreshold);
        }
        f = Paths.get(dir.toString(), "CountsByTenure" + year + month + ".csv");
        if (Files.exists(f)) {
            writeLine("", componentFOS);
            writeLine("<h5>Counts By Tenure Table</h5>", componentFOS);
            writeLine("", componentFOS);
            table = tab.readCSV(f);
            writeTable(table, componentFOS);
        }
        // Top 10s
        // Top 10
        writeExtremeAreaTable(dir, year, month);

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

    protected void writeExtremeAreaTable(Path dir, String year, String month)
            throws IOException {
        DW_Table tab = new DW_Table(env);
        Path f = Paths.get(dir.toString(), "HighestClaimants" + year + month + ".csv");
        if (Files.exists(f)) {
            writeLine("", componentFOS);
            writeLine("<h5>Areas With The Highest Numbers of Claimants Table</h5>", componentFOS);
            writeLine("", componentFOS);
            ArrayList<String> table;
            table = tab.readCSV(f);
            writeTable(table, componentFOS);
        }
    }

    protected void writeExtremeAreaChangesTable(Path dir, String name,
            String type, String year, String month) throws IOException {
        DW_Table tab = new DW_Table(env);
        Path f;
        f = Paths.get(dir.toString(), "ExtremeAreaChanges" + name + type + "LastYear" + year
                + month + ".csv");
        if (Files.exists(f)) {
            writeLine("", componentFOS);
            writeLine("<h5>Areas With The Largest " + name + " " + type
                    + " In Numbers Of Claimants Table From Last Year</h5>",
                    componentFOS);
            writeLine("", componentFOS);
            ArrayList<String> table;
            table = tab.readCSV(f);
            writeTable(table, componentFOS);
        }
        f = Paths.get(dir.toString(),
                "ExtremeAreaChanges" + name + type + "LastMonth" + year + month
                + ".csv");
        if (Files.exists(f)) {
            writeLine("", componentFOS);
            writeLine("<h5>Areas With The Largest " + name + " " + type
                    + " In Numbers Of Claimants Table From Last Month</h5>",
                    componentFOS);
            writeLine("", componentFOS);
            ArrayList<String> table;
            table = tab.readCSV(f);
            writeTable(table, componentFOS);
        }
    }

    protected void writeTable(
            ArrayList<String> table,
            BufferedOutputStream fos) throws IOException {
        //writeLine("<table style=\"width:100%\">");
        //writeLine("<table border=\"1\">");
        writeLine("<table style=\"border:1px solid black\">", fos);
        Iterator<String> ite;
        ite = table.iterator();
        while (ite.hasNext()) {
            String row;
            row = ite.next();
            String[] split;
            split = row.split(",");
            writeLine("<tr>", fos);
            for (String split1 : split) {
                //writeLine("<td>" + split1 + "</td>");
                writeLine("<td style=\"border:1px solid grey\">" + split1 + "</td>", fos);
            }
            writeLine("</tr>", fos);
        }
        writeLine("</table>", fos);
        writeLine("", fos);
    }

    public void writeContents(
            String[] tFilenames,
            String claimantType,
            String claimantTypeLink,
            BufferedOutputStream fos) throws IOException {
        writeLine("<div><ul>", fos);
        writeLine("<li><h2>Contents</h2>", fos);
        writeLine("<ul>", fos);
        Iterator<String> typesIte;
        Iterator<String> distanceTypesIte;
        Iterator<Double> distancesIte;
        Iterator<String> levelsIte;
        Iterator<String> includesIte;
        includesIte = includes.keySet().iterator();
        while (includesIte.hasNext()) {
            String includeName;
            includeName = includesIte.next();
            ArrayList<Integer> include;
            include = includes.get(includeName);
            writeLine("<li>" + getLink(includeName, includeName), fos);
            writeLine("<ul>", fos);
            for (int i = 0; i < tFilenames.length; i++) {
                if (include.contains(i)) {
                    String year;
                    year = DW_ChoroplethMapsLCC.getFilenameYear(tFilenames[i]);
                    String month;
                    month = DW_ChoroplethMapsLCC.getFilenameMonth(tFilenames[i]);
                    typesIte = types.iterator();
                    String yearMonth;
                    yearMonth = year + " " + month;
                    String idn;
                    String idn0;
                    idn = includeName + " " + yearMonth;
                    String idl;
                    idl = idn.replace(" ", "_");
                    writeLine("<li>" + getLink(idl, idn), fos);
                    writeLine("<ul>", fos);
                    while (typesIte.hasNext()) {
                        String type;
                        type = typesIte.next();
                        String type2;
                        type2 = Generic_String.getCapitalFirstLetter(type);
                        type2 += "Claimants";
                        idn = includeName + " " + yearMonth + " " + type2;
                        idn0 = type2;
                        idl = idn.replace(" ", "_");
                        //writeLine("<li>" + getLink(idl, idn));
                        writeLine("<li>" + getLink(idl, idn0), fos);
                        writeLine("<ul>", fos);
                        levelsIte = levels.iterator();
                        while (levelsIte.hasNext()) {
                            String level;
                            level = levelsIte.next();
                            idn = includeName + " " + yearMonth;
                            idn += " " + type2;
                            idn += " " + level;
                            idl = idn.replace(" ", "_");
                            writeLine("<li>" + getLink(idl, level) + "</li>", fos);
                        }
                        writeLine("</ul>", fos);
                        writeLine("</li>", fos);
                    }
                    distanceTypesIte = distanceTypes.iterator();
                    while (distanceTypesIte.hasNext()) {
                        String distanceType;
                        distanceType = distanceTypesIte.next();
                        String type2;
                        type2 = Generic_String.getCapitalFirstLetter(distanceType);
                        type2 += "Claimants";
                        idn = includeName + " " + yearMonth + " " + type2;
                        idl = idn.replace(" ", "_");
                        //writeLine("<li>" + getLink(idl, idn));
                        writeLine("<li>" + getLink(idl, type2), fos);
                        writeLine("<ul>", fos);
                        distancesIte = distances.iterator();
                        while (distancesIte.hasNext()) {
                            distanceThreshold = distancesIte.next();
                            levelsIte = levels.iterator();
                            while (levelsIte.hasNext()) {
                                String level;
                                level = levelsIte.next();
                                idn = includeName + " " + yearMonth;
                                idn += " " + type2;
                                idn += " " + distanceThreshold + " Metres";
                                idn += " " + level;
                                idl = idn.replace(" ", "_");
                                writeLine("<li>" + getLink(idl, distanceThreshold + " Metres " + level) + "</li>", fos);
                            }
                        }
                        writeLine("</ul>", fos);
                        writeLine("</li>", fos);
                    }
                    writeLine("</ul>", fos);
                    writeLine("</li>", fos);
                }
            }
            writeLine("</ul>", fos);
            writeLine("</li>", fos);
        }
        writeLine("</ul>", fos);
        writeLine("</li>", fos);
        writeLine("</ul></div>", fos);
    }
}
