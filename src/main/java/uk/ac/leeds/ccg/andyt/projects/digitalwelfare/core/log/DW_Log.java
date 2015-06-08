package uk.ac.leeds.ccg.andyt.projects.digitalwelfare.core.log;

import java.util.logging.Level;
import uk.ac.leeds.ccg.andyt.generic.logging.Generic_Log;

public final class DW_Log extends Generic_Log {

    private static final String sourcePackage = DW_Log.class.getPackage().getName();
    private static final String sourceClass = DW_Log.class.getName();
    public static final Level DW_DefaultLogLevel = Level.ALL;
    public static final String DW_DefaultLoggerName = sourcePackage + ".DW_Log";
    public DW_Log(){}
}
