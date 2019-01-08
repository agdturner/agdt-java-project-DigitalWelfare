package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;
import uk.ac.leeds.ccg.andyt.generic.lang.Generic_String;
import uk.ac.leeds.ccg.andyt.generic.util.Generic_Time;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io.DW_Files;

public class DW_Types extends DW_HTMLPage {

    protected String projectName;
    protected String mainDirectoryName;

    public DW_Types(DW_Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        new DW_Types(null).run();
    }

    public void run() {
        lineSeparator = System.getProperty("line.separator").getBytes();
        projectName = "DigitalWelfare";
        mainDirectoryName = "Report";
        String reportName = "Report1";
        // Links
        String OALink = getLink2(
                "OA",
                "./");
        String LSOALink = getLink2(
                "LSOA",
                "./");
        String MSOALink = getLink2(
                "MSOA",
                "./");
        String PostcodeUnitLink = getLink2(
                "PostcodeUnit",
                "./");
        String PostcodeSectorLink = getLink2(
                "PostcodeSector",
                "./");
        String PostcodeDistrictLink = getLink2(
                "PostcodeDistrict",
                "./");
        String HBClaimantsLink = getLink2(
                "HBClaimants",
                "./");
        String CTBClaimantsLink = getLink2(
                "CTBClaimants",
                "./");
        // Definitions
        HashMap<String, String> definitions;
        definitions = new HashMap<>();
        definitions.put(
                "AllTenure",
                "All Tenures");
        definitions.put(
                "RegulatedTenure",
                "Regulated Tenures");
        definitions.put(
                "UnregulatedTenure",
                "Unregulated Tenures");
        definitions.put(
                "OA",
                "Output Area: The highest resolution census output area. Some "
                + "of these have changed between the 2001 and 2011 "
                + "censuses. In 1991 only Scotland had Output Areas "
                + "whilst the rest of the UK had Enumeration Districts "
                + "as the highest reolution at which census area "
                + "statistics were available. OAs are of a compable "
                + "size to " + PostcodeUnitLink + "s, but tend to have "
                + "a more compact shape.");
        definitions.put(
                "LSOA",
                "Lower Layer Super Output Area: The second highest resolution "
                + "census output area. Each LSOA is compriosed of a set "
                + "of " + OALink + "s.");
        definitions.put(
                "MSOA",
                "Middle Layer Super Output Area: The third highest resolution "
                + "census output area. Each MSOA is compriosed of a set "
                + "of " + OALink + "'s. MSOA definitions are "
                + "consistent in both 2001 and 2011 censuses and they "
                + "are thus important for considering changes over "
                + "time. MSOAs are not necessarily aggregates of "
                + LSOALink + "s.");
        definitions.put(
                "PostcodeUnit",
                "PostcodeUnit: A set of delivery points generalised into a "
                + "discrete polygon given all other postcode units. "
                + "An example of a postcode unit it LS2 9JT. Postcode "
                + "units are more continually updated than census "
                + "spatial units. Updated ONS Postcode Directory "
                + "lookup files are provided on a quarterly basis "
                + "(i.e. 4 times per year). See also: "
                + "<a href=\"http://www.ons.gov.uk/ons/guide-method/geography/products/postcode-directories/-nspp-/index.html\">http://www.ons.gov.uk/ons/guide-method/geography/products/postcode-directories/-nspp-/index.html</a>; "
                + "<a href=\"https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom\">https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom</a>.");
        definitions.put(
                "PostcodeSector",
                "PostcodeSector: A set of delivery points generalised into a "
                + "discrete polygon given all other postcode sectors. "
                + "These are an aggregate of " + PostcodeUnitLink
                + "s. An example of a postcode sector code is LS2 9. "
                + "See also: "
                + "<a href=\"https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom\">https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom</a>.");
        definitions.put(
                "PostcodeDistricts",
                "PostcodeSector: A set of delivery points generalised into a "
                + "discrete polygon given all other postcode sectors. "
                + "These are an aggregate of " + PostcodeUnitLink
                + "s. An example of a postcode sector code is LS2 9. "
                + "See also: "
                + "<a href=\"https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom\">https://en.wikipedia.org/wiki/Postcodes_in_the_United_Kingdom</a>; "
                + "<a href=\"https://en.wikipedia.org/wiki/List_of_postcode_districts_in_the_United_Kingdom\">https://en.wikipedia.org/wiki/List_of_postcode_districts_in_the_United_Kingdom</a>.");
        definitions.put(
                "HBClaimants",
                "Housing Benefit claimant population. (Housing Benefit "
                        + "claimants are also Council Tax Benefit claimants.)");
        definitions.put(
                "CTBClaimants",
                "Council Tax Benefit claimant populations not including "
                + HBClaimantsLink + ".");
        definitions.put(
                "AllClaimants",
                "Count of all claimants (both " + HBClaimantsLink + " and "
                + CTBClaimantsLink + ")");
        definitions.put(
                "OnFlowClaimants",
                "Counts of claimants not claiming the previous month and that "
                + "have not appeared as claimants before in Leeds in "
                + "the time period.");
        definitions.put(
                "ReturnFlowClaimants",
                "Counts of claimants not recorded as claiming in the previous "
                + "month, but that have appeared as claimants before "
                + "in Leeds in the time period.");
        definitions.put(
                "StableClaimants",
                "Counts of claimants that have a postcode location recorded "
                + "differently in the previous month.");
        definitions.put(
                "AllInChurnClaimants",
                "Counts of claimants that have moved to an area. This includes "
                + "all people moving within the area. (Moving to an "
                + "area is inferred by the postcode being recorded "
                + "differently, so this may or may not represent a "
                + "move as it also included all those records where a "
                + "postcode was originally misrecorded for some "
                + "reason.");
        definitions.put(
                "AllOutChurnClaimants",
                "Counts of claimants that have moved out of an area. This "
                + "includes all people moving within the area. (Moving "
                + "to an area is inferred by the postcode being recorded "
                + "differently, so this may or may not represent a "
                + "move as it also included all those records where a "
                + "postcode was originally misrecorded for some "
                + "reason.");
        definitions.put(
                "InDistanceChurnClaimants",
                "Counts of claimants that have moved into this area from within "
                + "a specified distance.");
        definitions.put(
                "WithinDistanceChurnClaimants",
                "Counts of claimants that have moved within a specified "
                + "distance and that now reside in this area");
        definitions.put(
                "OutDistanceChurnClaimants",
                "Counts of claimants that have moved out of this area more than "
                + "a specified distance.");
        Iterator<String> ite;
        ite = definitions.keySet().iterator();
        while (ite.hasNext()) {
            String type = ite.next();
            write(reportName, type, definitions);
        }
    }

    public void write(
            String reportName,
            String type,
            HashMap<String, String> definitions) {
        String baseReportDir = mainDirectoryName + "/" + reportName + "/" + "Definitions/";
        String baseURLString0 = "http://www.geog.leeds.ac.uk/people/a.turner/"
                + projectName + "/" + baseReportDir;
        String date = Generic_Time.getDate();

        File dir = new File(
                Env.getFiles().getOutputDir(),
                baseReportDir);
        dir.mkdirs();
        String reportFilename = type + ".html";
        String baseURLString1 = baseURLString0 + reportFilename;
        File f = new File(
                dir,
                reportFilename);
        componentFOS = Generic_IO.getFileOutputStream(f);
        write(type,
                definitions,
                projectName,
                reportName,
                reportFilename,
                baseReportDir,
                baseURLString0,
                baseURLString1);
        try {
            writeHTMLFooter(date, componentFOS);
            componentFOS.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void write(
            String type,
            HashMap<String, String> definitions,
            String projectName,
            String reportName,
            String reportFilename,
            String directories,
            String baseURLString0,
            String baseURLString1) {
        try {
            String definition;
            definition = definitions.get(type);
            reportName += " " + type + " Definition";
            writeHTMLHeader(projectName, reportName, componentFOS);
            componentFOS.write(lineSeparator);
            writeStartOfBody(baseURLString0, reportName, componentFOS);
            //writeContents();
            writeLine("<ul>", componentFOS);
            writeLine("<li>", componentFOS);
            writeStartOfSection(" " + type + " Definition", 2, componentFOS);
            writeLine("<p>" + definition + "</p>", componentFOS);
            writeLine("</li>", componentFOS);
            writeLine("</ul>", componentFOS);
            writeLine("</div>", componentFOS);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
