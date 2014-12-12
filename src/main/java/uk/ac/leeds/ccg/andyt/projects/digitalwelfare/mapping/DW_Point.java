/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.mapping;

import java.io.Serializable;

/**
 *
 * @author geoagdt
 */
public class DW_Point implements Serializable {

    private int x;
    private int y;

    public DW_Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public String toString() {
        return "x " + x + ", y " + y;
    }
}
