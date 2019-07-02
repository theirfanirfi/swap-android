package swap.irfanullah.com.swap.Libraries;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDiff {

    public static String getTimeDifference(String date1)
    {
        if(date1 == null){
            return "00";
        }



        String returnStatement = "00";
        Date d1 = new Date();

        Date d2 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("y-M-d HH:mm");
        try {
            String currentTime = simpleDateFormat.format(new Date());
            d1 = simpleDateFormat.parse(date1);
            d2 = simpleDateFormat.parse(currentTime);
            long difference = d2.getTime() - d1.getTime();
            int days = days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

            if(days > 1)
            {
                returnStatement = Integer.toString(days)+"d";
            }
            else if(hours > 1)
            {
                returnStatement = Integer.toString(hours)+"h";
            }
            else if(hours < 1)
            {
                returnStatement = Integer.toString(min)+"m";

            }
        } catch (ParseException e) {
            Log.i("DATETIME",e.toString());
            return "00";

        }

        return  returnStatement;
    }
}
