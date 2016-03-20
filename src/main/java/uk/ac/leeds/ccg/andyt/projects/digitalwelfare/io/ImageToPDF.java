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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.io;

import com.itextpdf.text.BadElementException;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageToPDF {

    public static void main(String[] args) {
        Document document;
        File dir = new File("/nfs/see-fs-02_users/geoagdt/DataTransfer/DigitalWelfare/Output/Alison");
        File dirIn = new File(dir,
                "PNG");
        File dirOut = new File(dir,
                "PDF");
        dirOut.mkdirs();
        File outFile;
        String name;
        File[] inFiles;
        inFiles = dirIn.listFiles();
        for (File inFile : inFiles) {
            document = new Document();
            float marginWidth = document.leftMargin() + document.rightMargin();
            float marginHeight = document.topMargin() + document.bottomMargin();
            name = inFile.getName();
            name = name.substring(0, name.length() - 3);
            name += "pdf";
            outFile = new File(dirOut, name);
            try {
                FileOutputStream fos = new FileOutputStream(outFile);
                PdfWriter writer = PdfWriter.getInstance(document, fos);
                writer.open();
                document.open();
                Image im;
                im = Image.getInstance(inFile.toString());
                float width = im.getWidth() + marginWidth;
                float height = im.getHeight() + marginHeight;
                Rectangle r;
                r = new Rectangle(width, height);
                document.setPageSize(r);
                document.newPage();
                document.add(im);
                //document.
                document.close();
                writer.close();
            } catch (BadElementException ex) {
                Logger.getLogger(ImageToPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(ImageToPDF.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ImageToPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void writeImageToPDF(
            File f,
            Image i) {
        Document document = new Document();
        try {
            FileOutputStream fos = new FileOutputStream(f);
            PdfWriter writer;
            try {
                writer = PdfWriter.getInstance(document, fos);
                writer.open();
                document.open();
                document.add(i);
                document.close();
                writer.close();
            } catch (DocumentException ex) {
                Logger.getLogger(ImageToPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageToPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
