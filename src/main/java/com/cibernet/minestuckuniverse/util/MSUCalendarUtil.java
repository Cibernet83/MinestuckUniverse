package com.cibernet.minestuckuniverse.util;

import java.util.Calendar;

public class MSUCalendarUtil
{
    public static int getCalendarDate()
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getCalendarMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static boolean isChristmas()
    {
        return getCalendarMonth() == Calendar.DECEMBER && (getCalendarDate() == 24 || getCalendarDate() == 25);
    }
}
