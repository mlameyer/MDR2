/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connamara;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Administrator
 */
public class UnixtoStandardTime 
{
    public String convert(long unixTime)
    {
        Date date = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSS");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        String formattedDate = sdf.format(date);
        
        return formattedDate;
    }
}
