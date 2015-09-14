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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.reporting;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author geoagdt
 */
public abstract class DW_HTMLPage {

    protected byte[] lineSeparator;

    protected FileOutputStream fos;

    public DW_HTMLPage() {
    }

    public void writeStartOfBody(
            String baseURLString0,
            String pageName) {
        try {
            fos.write(lineSeparator);
            writeLine("<body>");
            writeLine("<div><ul>");
            writeLine("<li><h1><a href=\"" + baseURLString0 + "\" title=\""
                    + pageName + " Web Page @ School of Geography, University "
                    + "of Leeds\">" + pageName
                    + " Web Page</a></h1></li>\n");
            writeLine("</ul></div>");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void writeLine(
            String line) throws IOException {
        fos.write(line.getBytes());
        fos.write(lineSeparator);
    }

    public void writeHTMLHeader(
            String projectName,
            String pageName) throws IOException {
        writeLine("<!DOCTYPE html>");
        fos.write(lineSeparator);
        writeLine("<html lang=en-GB>");
        fos.write(lineSeparator);
        writeLine("<head>");
        writeLine("<title>" + projectName + " Project " + pageName
                + " Web Page @ School of Geography, University of Leeds</title>");
        writeLine("<meta charset=\"UTF-8\">");
        writeLine("<meta name=\"Keywords\" content=\"" + projectName + "\" />");
        writeLine("<meta name=\"description\" content=\"Web Page\" />");
        writeLine("<meta name=\"author\" content=\"Andy Turner\" />");
        writeLine(
                "<link rel=\"stylesheet\" "
                + "href=\"http://www.geog.leeds.ac.uk/people/a.turner/style/SOGStyle1CSS2.1.css\" "
                + "type=\"text/css\" />");
        writeLine("</head>");
    }

    public void writeStartOfSection(
            String sectionTitle,
            int headingLevel) throws IOException {
        String sectionTitleNoSpaces;
        sectionTitleNoSpaces = sectionTitle.replaceAll(" ", "_");
        writeLine("<div id=\"" + sectionTitleNoSpaces + "\">");
        writeLine("<h" + headingLevel + ">" + sectionTitle + "</h" + headingLevel + ">");
    }

    public void writeHTMLFooter(String date) throws IOException {
        writeLine("<div id=\"Validation_and_Metadata\">");
        writeLine("<!-- Begin Footer -->");
        writeLine("<ul>");
        writeLine("<li><h2>Validation and Metadata</h2>");
        writeLine("<ul>");
        writeLine("<!-- Begin Validation -->");
        writeLine("<!-- For validating this page -->");
        writeLine("<li>");
        writeLine("<a href=\"http://validator.w3.org/check/referer\">");
        writeLine("Check Page Source is Valid");
        writeLine("</a>");
        writeLine("</li>");
        writeLine("<!-- For validating the CSS linked from the header-->");
        writeLine("<li>");
        writeLine("<a href=\"http://jigsaw.w3.org/css-validator/check/referer\">");
        writeLine("Check Style Sheet is Valid");
        writeLine("</a>");
        writeLine("</li>");
        writeLine("<!-- End Validation -->");
        writeLine("<!-- Begin Footer -->");
        writeLine("<li>Version 0.0.1 of this page published on " + date + ".</li>");
        writeLine("<!-- For validating the CSS linked from the header-->");
        writeLine("</ul></li>");
        writeLine("</ul>");
        writeLine("</div>");
        writeLine("<!-- End Footer -->");
        writeLine("</body>");
        writeLine("</html>");
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
