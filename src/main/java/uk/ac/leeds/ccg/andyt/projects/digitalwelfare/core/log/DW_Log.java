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
package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.log;

import java.util.logging.Level;
import uk.ac.leeds.ccg.andyt.generic.logging.Generic_Log;

/**
 * A class for logging. This is not currently used.
 * @author geoagdt
 */
public final class DW_Log extends Generic_Log {

    private static final String sourcePackage = DW_Log.class.getPackage().getName();
    private static final String sourceClass = DW_Log.class.getName();
    public static final Level DW_DefaultLogLevel = Level.ALL;
    public static final String DW_DefaultLoggerName = sourcePackage + ".DW_Log";

    public DW_Log() {
    }
}
