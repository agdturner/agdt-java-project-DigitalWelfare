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
package uk.ac.leeds.ccg.projects.dw.reporting;

import java.io.BufferedOutputStream;
import java.io.IOException;
import uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.DW_Environment;
import uk.ac.leeds.ccg.projects.dw.core.DW_Object;

/**
 *
 * @author geoagdt
 */
public abstract class DW_HTMLPage extends DW_Object {

    protected byte[] lineSeparator;

    protected BufferedOutputStream componentFOS;
    protected BufferedOutputStream masterFOS;

    public DW_HTMLPage(DW_Environment env) {
        super(env);
    }

    public void writeStartOfBody(
            String baseURLString0,
            String pageName,
            BufferedOutputStream fos) {
        try {
            fos.write(lineSeparator);
            writeLine("<body>", fos);
            writeLine("<div><ul>", fos);
            writeLine("<li><h1><a href=\"" + baseURLString0 + "\" title=\""
                    + pageName + " Web Page @ School of Geography, University "
                    + "of Leeds\">" + pageName
                    + " Web Page</a></h1></li>\n",
                    fos);
            writeLine("</ul></div>", fos);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public void writeLine(
            String line,
            BufferedOutputStream fos) throws IOException {
        fos.write(line.getBytes());
        fos.write(lineSeparator);
    }

    public void writeHTMLHeader(
            String projectName,
            String pageName,
            BufferedOutputStream fos) throws IOException {
        writeLine("<!DOCTYPE html>", fos);
        fos.write(lineSeparator);
        writeLine("<html lang=en-GB>", fos);
        fos.write(lineSeparator);
        writeLine("<head>", fos);
        writeLine("<title>" + projectName + " Project " + pageName
                + " Web Page @ School of Geography, University of Leeds</title>",
                fos);
        writeLine("<meta charset=\"UTF-8\">", fos);
        writeLine("<meta name=\"Keywords\" content=\"" + projectName + "\" />", fos);
        writeLine("<meta name=\"description\" content=\"Web Page\" />", fos);
        writeLine("<meta name=\"author\" content=\"Andy Turner\" />", fos);
        writeLine(
                "<link rel=\"stylesheet\" "
                + "href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" "
                + "type=\"text/css\" />", fos);
        writeLine("</head>", fos);
    }

    public void writeStartOfSection(
            String sectionTitle,
            int headingLevel,
            BufferedOutputStream fos) throws IOException {
        String sectionTitleNoSpaces;
        sectionTitleNoSpaces = sectionTitle.replaceAll(" ", "_");
        writeLine("<div id=\"" + sectionTitleNoSpaces + "\">", fos);
        writeLine("<h" + headingLevel + ">" + sectionTitle + "</h" + headingLevel + ">", fos);
    }

    public void writeHTMLFooter(
            String date,
            BufferedOutputStream fos) throws IOException {
        writeLine("<div id=\"Validation_and_Metadata\">", fos);
        writeLine("<!-- Begin Footer -->", fos);
        writeLine("<ul>", fos);
        writeLine("<li><h2>Validation and Metadata</h2>", fos);
        writeLine("<ul>", fos);
        writeLine("<!-- Begin Validation -->", fos);
        writeLine("<!-- For validating this page -->", fos);
        writeLine("<li>", fos);
        writeLine("<a href=\"http://validator.w3.org/check/referer\">", fos);
        writeLine("Check Page Source is Valid", fos);
        writeLine("</a>", fos);
        writeLine("</li>", fos);
        writeLine("<!-- For validating the CSS linked from the header-->", fos);
        writeLine("<li>", fos);
        writeLine("<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">", fos);
        writeLine("Check Style Sheet is Valid", fos);
        writeLine("</a>", fos);
        writeLine("</li>", fos);
        writeLine("<!-- End Validation -->", fos);
        writeLine("<!-- Begin Footer -->", fos);
        writeLine("<li>Version 0.0.1 of this page published on " + date + ".</li>", fos);
        writeLine("<!-- For validating the CSS linked from the header-->", fos);
        writeLine("</ul></li>", fos);
        writeLine("</ul>", fos);
        writeLine("</div>", fos);
        writeLine("<!-- End Footer -->", fos);
        writeLine("</body>", fos);
        writeLine("</html>", fos);
    }

    public static String getLink2(
            String type,
            String dir) {
        String result;
        result = "<a href=\"" + dir + type + ".html\">" + type + "</a>";
        return result;
    }

    /**
     * @param id The id for the link.
     * @param name The name for the link.
     * @return {@code "<li><a href=\"#" + id + "\">" + name + "</a></li>"}
     */
    public static String getLink(
            String id,
            String name) {
        String result;
        result = "<a href=\"#" + id + "\">" + name + "</a>";
        return result;
    }

}
