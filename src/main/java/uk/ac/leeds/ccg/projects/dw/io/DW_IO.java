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
package uk.ac.leeds.ccg.projects.dw.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author geoagdt
 */
public class DW_IO {

    public static final String[] split(String s, String s0) {
        String[] result;
        if (isAllString(s, s0)) {
            result = new String[s.length()];
            for (int i = 0; i < s.length(); i++) {
                result[i] = "";
            }
            return result;
        }
        //int startStringCount = getStartStringCount(s, s0);
        int endStringCount = getEndStringCount(s, s0);
        String[] split = s.split(s0);
        result = new String[split.length + endStringCount];
        System.arraycopy(split, 0, result, 0, split.length);
        int index = split.length;
        for (int i = index; i < result.length; i++) {
            result[i] = "";
            index++;
        }
        return result;
    }

    public static final String[] splitWithQuotesThenCommas(String s) {
        // Special Case
        if (!s.contains("\"")) {
            return split(s, ",");
        }
        String[] result;
        String[] split1 = split(s, "\"");
        // a,d,g,"ew,2",x,y
        // "dsas",abd,w
        List<String> resultsArrayList = new ArrayList<>();
        // Deal with first string
        if (s.startsWith("\"")) {
            for (int i = 1; i < split1.length; i++) {
                if ((i + 1) % 2 == 0) {
                    resultsArrayList.add(split1[i]);
                } else {
                    if (!split1[i].equalsIgnoreCase(",")) {
                        if (split1[i].startsWith(",")) {
                            String dummy = split1[i].substring(1);
                            resultsArrayList.addAll(Arrays.asList(split(dummy, ",")));
                        } else {
                            resultsArrayList.addAll(Arrays.asList(split(split1[i], ",")));
                        }
                    }
                }
            }
        } else {
            if (s.startsWith(",")) {
                resultsArrayList.add("");
            }
            for (int i = 0; i < split1.length; i++) {
                if (i % 2 == 0) {
                    if (!split1[i].equalsIgnoreCase(",")) {
                        if (split1[i].endsWith(",")) {
                            String dummy = split1[i].substring(0, split1[i].length() - 1);
                            if (dummy.startsWith(",")) {
                                String dummy2 = dummy.substring(1);
                                resultsArrayList.addAll(Arrays.asList(split(dummy2, ",")));
                            } else {
                                resultsArrayList.addAll(Arrays.asList(split(dummy, ",")));
                            }
                        } else {
                            if (split1[i].startsWith(",")) {
                                String dummy = split1[i].substring(1);
                                resultsArrayList.addAll(Arrays.asList(split(dummy, ",")));
                            } else {
                                resultsArrayList.addAll(Arrays.asList(split(split1[i], ",")));
                            }
                        }
                    }
                } else {
                    resultsArrayList.add(split1[i]);
                }
            }
            if (s.endsWith(",")) {
                resultsArrayList.add("");
            }
        }
        //result = (String[]) resultsArrayList.toArray();
        result = new String[resultsArrayList.size()];
        Iterator<String> ite = resultsArrayList.iterator();
        int index = 0;
        while (ite.hasNext()) {
            result[index] = ite.next();
            index++;
        }
        return result;
    }

    public static boolean isAllCommas(String s) {
        return isAllString(s, ",");
    }

    public static boolean isAllString(String s, String s0) {
        // Special cases
        if (s.equalsIgnoreCase(s0)) {
            return true;
        }
        if (!s.contains(s0)) {
            return false;
        }
        int slength = s.length();
        int s0length = s0.length();
        if (slength < s0length) {
            return false;
        }
        if (slength % s0length != 0) {
            return false;
        }
        String dummy = s;
        while (dummy.startsWith(s0)) {
            if (dummy.length() > s0length) {
                dummy = dummy.substring(s0length);
            } else {
                if (dummy.equalsIgnoreCase(s0)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * An integer count of the number of times the char c is in the String s.
     *
     * @param s
     * @param c
     * @return
     */
    public static int count(String s, char c) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                result++;
            }
        }
        return result;
    }

    public static int getStartCommaCount(String s) {
        int result = 0;
        String s0 = ",";
        result = getStartStringCount(s, s0);
        return result;
    }

    /**
     * Return the number of s0 at the start of s
     *
     * @param s
     * @param s0
     * @return
     */
    public static int getStartStringCount(String s, String s0) {
        int result = 0;
        // Special cases
        if (s.equalsIgnoreCase(s0)) {
            return 1;
        }
        if (isAllString(s, s0)) {
            return s.length();
        }
        int s0length = s0.length();
        // Calculate
        String dummy = s;
        while (dummy.startsWith(s0)) {
            result++;
            if (dummy.length() > 1) {
                dummy = dummy.substring(s0length);
            }
        }
        return result;
    }

    public static int getEndCommaCount(String s) {
        return getEndStringCount(s, ",");
    }

    /**
     * This calculates and returns a count of the number of s0 Strings found at
     * the end of String s. If s0 has a length of greater than 1 this currently
     * breaks.
     *
     * @param s
     * @param s0
     * @return
     */
    public static int getEndStringCount(String s,
            String s0) {
        int result = 0;
        // Special cases
        if (s.equalsIgnoreCase(s0)) {
            return 1;
        }
        if (isAllString(s, s0)) {
            return s.length();
        }
        int s0length = s0.length();
        // Calculate
        String dummy = s;
        while (dummy.endsWith(s0)) {
            result++;
            dummy = dummy.substring(0, dummy.length() - s0length);
        }
        return result;
    }
}
