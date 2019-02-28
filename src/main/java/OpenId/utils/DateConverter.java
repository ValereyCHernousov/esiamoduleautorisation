package OpenId.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateConverter {
    public static Timestamp converterToTimestamp(Date date){
        Timestamp ts=new Timestamp(date.getTime());
        return ts;
    }
}
