package journal.test.pkg1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter
{
	public static String Format(Date date)
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy 'at' h:mm a z");
		return dateFormatter.format(date);
	}
}
