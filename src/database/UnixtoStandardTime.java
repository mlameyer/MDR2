/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class UnixtoStandardTime 
{
    public BigDecimal convert(String unixTime)
    {
        String c = "", completedTime;
        BigDecimal decimal;
        if(unixTime.length() > 12)
        {
            Date date = new Date(Long.parseLong(unixTime.substring(0, 13)));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
            completedTime = c.concat(sdf.format(date)).concat(".").concat(unixTime.substring(13, unixTime.length() - 1));
            decimal = new BigDecimal(completedTime);
        }
        else
        {
            long l = Long.parseLong(unixTime);
            Date date = new Date(l);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
            decimal = new BigDecimal(sdf.format(date));
        }
        
        return decimal;
    }
}
