package commun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.danem.cnoir.qrpoche.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Fonctions 
{
	public static String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
		}
	static public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
	static public String getMacAdr(Context c)
	{
		WifiManager wifiMan = (WifiManager) c.getSystemService(
        Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		String macAddr = wifiInf.getMacAddress();
		
		return macAddr;
	}
	static public String getYYYYMMDD(int ndelay)
    {
    	 final Calendar c = Calendar.getInstance( ); 
    	 
         int mYear = c.get(Calendar.YEAR); 
         int mMonth = c.get(Calendar.MONTH); 
         int mDay = c.get(Calendar.DAY_OF_MONTH);
        
        
      // entrée de la nouvelle date (dans 30 jours)
         c.set(mYear, mMonth, mDay +ndelay);
 
         // obtention des éléments de cette nlle date
         
          mYear = c.get(Calendar.YEAR); 
          mMonth = c.get(Calendar.MONTH)+1; 
          mDay = c.get(Calendar.DAY_OF_MONTH);
      
      
         String Date=String.format("%04d%02d%02d", mYear,mMonth,mDay);
         
         return Date;
         
         
    
    }
 
	
	
	static public String TimePicker_to_HHMM(TimePicker dp)
	{
		try
		{


			int hour=dp.getCurrentHour();




			int minute =dp.getCurrentMinute();


			String sthour=String.valueOf(hour);
			sthour="0"+sthour;
			int len=sthour.length();
			sthour=sthour.substring(len-2, len);


			String stMinute=String.valueOf(minute);
			stMinute="0"+stMinute;
			len=stMinute.length();
			stMinute=stMinute.substring(len-2, len);


			return sthour+stMinute;
		}
		catch(Exception ex)
		{

		}
		return "";
	}
	/*
	 * convertie une chaine hexa color #FFOOOO en int
	 */
	public static int converColorStringToInt(String dieseString)
	{
		try
		{
			int c=Color.parseColor(dieseString);
			return c;
		}
		catch(Exception ex)
		{
			return 0;
		}
	}
	
	/**
	 * donne une longueur max � un edittext
	 * @param et
	 * @param maxlenth
	 * @return
	 */
	public static boolean setMaxLenth(EditText et, int maxlenth)
	{
		try
		{
			 InputFilter[] FilterArray = new InputFilter[1];
			 FilterArray[0] = new InputFilter.LengthFilter(maxlenth);
			 et.setFilters(FilterArray);
			 
			 return true;
		}
		catch(Exception ex)
		{
			
		}
		return false;
	}
	 public static boolean createDirectory(String dir)
	 {
		 
         try {
			String PATH = Environment.getExternalStorageDirectory() +"/"+dir;
   //     	 String PATH = ExternalStorage.root +"/"+dir;
			PATH=PATH.replace("//", "/");
			 File file = new File(PATH);
			 return file.mkdirs();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
         return false;
	 }
	public static String readTextFromResource(Context c, int resourceID)
	{
		InputStream raw = c.getResources().openRawResource(resourceID);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		int i;
		try
		{
			i = raw.read();
			while (i != -1)
			{
				stream.write(i);
				i = raw.read();
			}
			raw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return stream.toString();
	}

	/**
	 * @author marcvouaux
	 * charge une image dans une imageview
	 * @param imageView
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean fileToImageView( ImageView imageView, String path,String fileName){
		File jpgFile = new File(path+"/"+fileName);
		Bitmap thumbnail ;

		if(jpgFile.exists()){
			Log.d("TAG", "file exist "+fileName);
			try {
				thumbnail = BitmapFactory.decodeStream(new FileInputStream(jpgFile), null, null);
			
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setImageBitmap(thumbnail);
				
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TAG", "error decoding"+e.toString());
			}
		}
		return false;
	}
		
	public static boolean fileToImageView( ImageView imageView, String path){
		File jpgFile = new File(path);
		Bitmap thumbnail ;

		if(jpgFile.exists()){
			
			try {
				thumbnail = BitmapFactory.decodeStream(new FileInputStream(jpgFile), null, null);
			
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setImageBitmap(thumbnail);
				
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("TAG", "error decoding"+e.toString());
			}
		}
	return false;
	}
	public static int getscrOrientation(Activity c) {
		Display getOrient = c.getWindowManager().getDefaultDisplay();

		int orientation = getOrient.getOrientation();

		// Sometimes you may get undefined orientation Value is 0
		// simple logic solves the problem compare the screen
		// X,Y Co-ordinates and determine the Orientation in such cases
		if (orientation == Configuration.ORIENTATION_UNDEFINED) {

			Configuration config = c.getResources().getConfiguration();
			orientation = config.orientation;

			if (orientation == Configuration.ORIENTATION_UNDEFINED) {
				// if height and widht of screen are equal then
				// it is square orientation
				if (getOrient.getWidth() == getOrient.getHeight()) {
					orientation = Configuration.ORIENTATION_SQUARE;
				} else { // if widht is less than height than it is portrait
					if (getOrient.getWidth() < getOrient.getHeight()) {
						orientation = Configuration.ORIENTATION_PORTRAIT;
					} else { // if it is not any of the above it will defineitly
						// be landscape
						orientation = Configuration.ORIENTATION_LANDSCAPE;
					}
				}
			}
		}
		return orientation; // return value 1 is portrait and 2 is Landscape
		// Mode
	}
	/**
	 * Transforme un dateicker en monthpicker
	 * @author Marc VOUAUX
	 * @param datePicker
	 */
	static public void setMonthPicker(DatePicker datePicker)
	{
		try {
			Field f[] = datePicker.getClass().getDeclaredFields();
			for (Field field : f) {
				if (field.getName().equals("mDayPicker")) {
					field.setAccessible(true);
					Object dayPicker = new Object();
					dayPicker = field.get(datePicker);
					((View) dayPicker).setVisibility(View.GONE);
				}
			}
		} catch (SecurityException e) {

		} 
		catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}
	/**
	 * Transforme un dateicker en yearpicker
	 * @author Marc VOUAUX
	 * @param datePicker
	 */
	static public void setYearPicker(DatePicker datePicker)
	{
		try {
			Field f[] = datePicker.getClass().getDeclaredFields();
			for (Field field : f) {
				String fldname=field.getName();
				if (field.getName().equals("mDayPicker") || field.getName().equals("mMonthPicker")
						|| field.getName().equals("mMonthSpinner")
						|| field.getName().equals("mDaySpinner")) {
					field.setAccessible(true);
					Object dayPicker = new Object();
					dayPicker = field.get(datePicker);
					((View) dayPicker).setVisibility(View.GONE);
				}
			}
		} catch (SecurityException e) {

		} 
		catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}
	static private String[] splitted1={"0"};
	static public String GiveFld(String chainesep, int fld,String sep,boolean reset)
	{

		try
		{
			if (reset)
				splitted1=chainesep.split("\\"+sep);
			
			
			String test;
			test=splitted1[fld].toString();
			

			return splitted1[fld].toString();
		}
		catch(Exception ex)
		{
			String err=ex.getMessage();
			return "";
		}



	}
	static public String GiveFld2(String chainesep, String sep,boolean reset)
	{

		try
		{
			if (reset)
				splitted1=chainesep.split("\\"+sep);

			return splitted1.toString();
		}
		catch(Exception ex)
		{
			String err=ex.getMessage();
			return "";
		}



	}
	static public float convertToFloat(String val)
	{
		try
		{
			if(val==null)
			{
				val="0";
			}
			if(val.equals(""))
			{
				val="0";
			}
			val=val.replace(",", ".");
			DecimalFormat df = new DecimalFormat("0.00");
			df.setMaximumFractionDigits(2);
			df.setMinimumFractionDigits(2);
			float fl=Float.parseFloat(val);
			String f=df.format(fl);
			f=f.replace(",", ".");
			
			return Float.parseFloat(f);
			
		}
		catch(Exception ex)
		{

		}
		return 0;
	}
	static public int convertToInt(String val)
	{
		try
		{
			if(val==null)
			{
				val="0";
			}
			if(val.equals(""))
			{
				val="0";
			}
			val=val.replace(",", ".");

			return Integer.parseInt(val);
		}
		catch(Exception ex)
		{

		}
		return 0;
	}	
	static public int convertBoolToInt(boolean val)
	{
		try
		{
			if(val==true)
			{
				return 1;
			}
			
			return 0;
		}
		catch(Exception ex)
		{

		}
		return 0;
	}	
	static public long convertToLong(String val)
	{
		try
		{
			if(val==null)
			{
				val="0";
			}
			if(val.equals(""))
			{
				val="0";
			}
			val=val.replace(",", ".");

			return Long.valueOf(val);
		}
		catch(Exception ex)
		{

		}
		return 0;
	}	
	/**
	 * @author Marc VOUAUX
	 * @param tab
	 * @param idx
	 * permet de retourner sans erreur un item d'un tableau
	 * @return
	 */
	public static String getStringArrayValue(String []tab,int idx)
	{
		try
		{
			return tab[idx];	
		}
		catch(Exception ex)
		{
			return "";
		}

	}
	static public void FurtiveMessageBox(Context c,String message){
		Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show message by alert
	 * @param message
	 * @param c current context
	 */
	static public void _showAlert(final String message, final Context c) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		AlertDialog dialog = builder.create();
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

		dialog.show();
		
		//Set the dialog to immersive
		dialog.getWindow().getDecorView().setSystemUiVisibility(
		((Activity)c).getWindow().getDecorView().getSystemUiVisibility());

		//Clear the not focusable flag from the window
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

	}

	
	/**
	 * Show context on UiThread
	 * @param message
	 * @param current context
	 */
	static public void FurtiveMessageBox(String message, final Context c){
		Toast.makeText(c,message,Toast.LENGTH_LONG).show();
	}

	/**
	 * retourne le type d'ecran
	 * http://developer.android.com/guide/practices/screens_support.html
	 */
	static public int MEDIUMDPI_LARGE=0;
	static public int HIGHDPI_NORMAL=1;
	static public int MEDIUMDPI_XLARGE=2;

	public static int getResol(Activity c)
	{
		DisplayMetrics dm=new DisplayMetrics();
		Display display = c.getWindowManager().getDefaultDisplay(); 
		c.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int so=Fonctions.getscrOrientation(c);
		//if (so!=Configuration.ORIENTATION_LANDSCAPE)
		//if (dm.)

		//Configuration.SCREENLAYOUT_SIZE_XLARGE;
		//xlarge screens are at least 960dp x 720dp
		if (dm.widthPixels>=960 && dm.heightPixels>=720)
			return Configuration.SCREENLAYOUT_SIZE_XLARGE;

		return 0;
		/*
	    if (dm.density==1 && 
	    		( (dm.heightPixels==752 || dm.heightPixels==768) && (dm.widthPixels==1280 || dm.widthPixels==1024 )) 
	    		)
	    	return MEDIUMDPI_XLARGE;

	   if (dm.density==1 && 
			   ((dm.heightPixels==800 || dm.heightPixels==854) && (dm.widthPixels==480)) ||
			   (dm.heightPixels==1024 || dm.widthPixels==600)
	   		)
	    	return MEDIUMDPI_LARGE;

	    return HIGHDPI_NORMAL;
		 */
	}
	/**
	 * aujourd'hui en hhmmss
	 * @author Marc VOUAUX
	 * @return
	 */
	static public String gethhmmss()
	{
		final Calendar c = Calendar.getInstance( ); 

			int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMin= c.get(Calendar.MINUTE);
		int mSec= c.get(Calendar.SECOND);

		String heure=String.format("%02d%02d%02d",mHour,mMin,mSec);

		return heure;

	}
	static public String gethhmm()
	{
		
		return Left( gethhmmss(),4);

	}
	/**
	 * aujourd'hui en hhmmss + un increment
	 * @author Marc VOUAUX
	 * @return
	 */
	static public String gethhmmssEx(int increment)
	{
		int val=convertToInt(gethhmmss())+increment;

		return getInToStringDanem(val);

	}
	/**
	 * aujourd'hui en yyyymmddhhmmss
	 * @author Marc VOUAUX
	 * @return
	 */
	static public String getYYYYMMDDhhmmss()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mYear = c.get(Calendar.YEAR); 
		int mMonth = c.get(Calendar.MONTH)+1; 
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int mHour = c.get(Calendar.HOUR_OF_DAY);
		int mMin= c.get(Calendar.MINUTE);
		int mSec= c.get(Calendar.SECOND);

		String heure=String.format("%04d%02d%02d%02d%02d%02d", mYear,mMonth,mDay,mHour,mMin,mSec);

		return heure;

	}
	static public String getDD_MM_YYYY()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mYear = c.get(Calendar.YEAR); 
		int mMonth = c.get(Calendar.MONTH)+1; 
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		String day=String.format("%02d",mDay );
		String month=String.format("%02d",mMonth);
		String year=String.format("%04d",mYear);


		String heure=day+"/"+month+"/"+year ;
		
		return heure;



	}
	
	static public String getYYYYMMDD()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mYear = c.get(Calendar.YEAR); 
		int mMonth = c.get(Calendar.MONTH)+1; 
		int mDay = c.get(Calendar.DAY_OF_MONTH);



		String heure=String.format("%04d%02d%02d", mYear,mMonth,mDay);

		return heure;



	}
	static public String getYYYY()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mYear = c.get(Calendar.YEAR); 



		String Annee=String.format("%04d", mYear);

		return Annee;



	}
	static public String getMM()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mMonth = c.get(Calendar.MONTH); 



		String Mois=String.format("%02d", mMonth);

		return Mois;



	}
	/**
	 * aujourd'hui en yyyymmdd
	 * @return
	 */
	static public String getDD()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mDay = c.get(Calendar.DAY_OF_MONTH);



		String Day=String.format("%02d", mDay);

		return Day;



	}
	static public String getJourSemaine()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mDayOfweek = c.get(Calendar.DAY_OF_WEEK);



		String stDayOfweek=String.format("%02d", mDayOfweek);

		return stDayOfweek;



	}
	static public String getJourdeAnne()
	{
		final Calendar c = Calendar.getInstance( ); 

		int mDayOfYear = c.get(Calendar.DAY_OF_YEAR);



		String stDayOfYear=String.format("%03d", mDayOfYear);

		return stDayOfYear;



	}

	static public String getDay_Of_Year()
	{
		String stDayofYear="111";
		try
		{
			Calendar calendar1 = Calendar.getInstance();
			int year = calendar1.get(Calendar.YEAR);
			int month = calendar1.get(Calendar.MONTH);
			int day = calendar1.get(Calendar.DAY_OF_MONTH);

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.DAY_OF_YEAR);
			
		

			stDayofYear=Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stDayofYear;

	}
	static public String getDay_Of_Week()
	{
		String stDayofWeek="111";
		try
		{
			Calendar calendar1 = Calendar.getInstance();
			int year = calendar1.get(Calendar.YEAR);
			int month = calendar1.get(Calendar.MONTH);
			int day = calendar1.get(Calendar.DAY_OF_MONTH);

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.DAY_OF_WEEK);



			stDayofWeek=Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stDayofWeek;

	}
	static public String getWeek_Of_Year()
	{
		String stWeekofYear="111";
		try
		{
			Calendar calendar1 = Calendar.getInstance();
			int year = calendar1.get(Calendar.YEAR);
			int month = calendar1.get(Calendar.MONTH);
			int day = calendar1.get(Calendar.DAY_OF_MONTH);

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.WEEK_OF_YEAR);

			stWeekofYear=Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stWeekofYear;

	}
	//renvoi une date avec un delta
	  static public String getYYYYMMDD_Datecde(int ndelay)
	    {
	    	 final Calendar c = Calendar.getInstance( ); 
	    	 
	         int mYear = c.get(Calendar.YEAR); 
	         int mMonth = c.get(Calendar.MONTH); 
	         int mDay = c.get(Calendar.DAY_OF_MONTH);
	        
	        
	      // entrée de la nouvelle date (dans 30 jours)
	         c.set(mYear, mMonth, mDay +ndelay);
	 
	         // obtention des éléments de cette nlle date
	         
	          mYear = c.get(Calendar.YEAR); 
	          mMonth = c.get(Calendar.MONTH)+1; 
	          mDay = c.get(Calendar.DAY_OF_MONTH);
	      
	      
	         String Date=String.format("%04d%02d%02d", mYear,mMonth,mDay);
	         
	         return Date;
	         
	         
	    
	    }
	  
	//renvoi une date avec un delta
	  static public String getYYYY_MM_DD_YYYYMMDD(int YYYY,int MM, int DD)
	    {
	    	 final Calendar c = Calendar.getInstance( ); 
	    	 
	         int mYear = YYYY; 
	         int mMonth = MM; 
	         int mDay = DD;
	        
	        
	      // entrée de la nouvelle date (dans 30 jours)
	         c.set(mYear, mMonth, mDay );
	 
	         // obtention des éléments de cette nlle date
	         
	          mYear = c.get(Calendar.YEAR); 
	          mMonth = c.get(Calendar.MONTH)+1; 
	          mDay = c.get(Calendar.DAY_OF_MONTH);
	      
	      
	         String Date=String.format("%04d%02d%02d", mYear,mMonth,mDay);
	         
	         return Date;
	         
	         
	    
	    }
	  
 
	  static public String getYYYYMMDD_YYYY_MM_DD(int YYYY,int MM, int DD)
	    {
	    	 final Calendar c = Calendar.getInstance( ); 
	    	 
	         int mYear = YYYY; 
	         int mMonth = MM; 
	         int mDay = DD;
	        
	        
	      // entrée de la nouvelle date (dans 30 jours)
	         c.set(mYear, mMonth, mDay );
	 
	         // obtention des éléments de cette nlle date
	         
	          mYear = c.get(Calendar.YEAR); 
	          mMonth = c.get(Calendar.MONTH)+1; 
	          mDay = c.get(Calendar.DAY_OF_MONTH);
	      
	      
	         String Date=String.format("%04d%02d%02d",mYear ,mMonth,mDay);
	         
	         return Date;
	         
	         
	    
	    }

	/**
	 * transformation de date
	 * @author Marc VOUAUX
	 * @param YYYYMMDDhhmmss
	 * @return
	 */
	static public String YYYYMMDDhhmmss_to_dd_mm_yyyy_hh_mm_ss(String YYYYMMDDhhmmss)
	{
		try
		{
			if (YYYYMMDDhhmmss.length()!=14)
				return "";

			String year=YYYYMMDDhhmmss.substring(0, 4);
			String month=YYYYMMDDhhmmss.substring(4, 6);
			String day=YYYYMMDDhhmmss.substring(6, 8);
			String hour=YYYYMMDDhhmmss.substring(8, 10);
			String min=YYYYMMDDhhmmss.substring(10, 12);
			String sec=YYYYMMDDhhmmss.substring(12, 14);

			return day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec;
		}
		catch(Exception ex)
		{

		}
		return "";

	}
	/**
	 * @author Marc VOUAUX
	 * @param YYYYMMDD
	 * @return
	 */
	static public String YYYYMMDD_to_dd_mm_yyyy(String YYYYMMDD)
	{
		try
		{
			if (YYYYMMDD.length()!=8)
				return "";

			String year=YYYYMMDD.substring(0, 4);
			String month=YYYYMMDD.substring(4, 6);
			String day=YYYYMMDD.substring(6, 8);

			return day+"/"+month+"/"+year;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	static public String dd_mm_yyyy_to_YYYYMMDD(String dd_mm_yyyy)
	{
		try
		{
			if (dd_mm_yyyy.length()!=10)
				return "";

			String year=dd_mm_yyyy.substring(6, 10);
			String month=dd_mm_yyyy.substring(3, 5);
			String day=dd_mm_yyyy.substring(0, 2);

			return year+month+day;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	static public String YYYY_MM_DD_to_dd_mm_yyyy(String YYYYMMDD)
	{
		try
		{
			if (YYYYMMDD.length()!=10)
				return "";

			String year=YYYYMMDD.substring(0, 4);
			String month=YYYYMMDD.substring(5, 7);
			String day=YYYYMMDD.substring(8, 10);

			return day+"/"+month+"/"+year;
		}
		catch(Exception ex)
		{
			return "";
		}


	}

	static public String hhmm_to_hh_point_mm(String hhmm)
	{
		try
		{
			if (hhmm.length()!=4)
				return "";

			String heure=hhmm.substring(0, 2);
			String minute=hhmm.substring(2, 4);
		
			return heure+":"+minute;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	static public String DatePicker_to_YYYYMMDD(DatePicker dp)
	{
		try
		{

			int day=dp.getDayOfMonth();
			int month =dp.getMonth()+1;
			int year=dp.getYear();

			String stDay=String.valueOf(day);
			stDay="0"+stDay;
			int len=stDay.length();
			stDay=stDay.substring(len-2, len);

			String stMonth=String.valueOf(month);
			stMonth="0"+stMonth;
			len=stMonth.length();
			stMonth=stMonth.substring(len-2, len);

			String stYear=String.valueOf(year);

			return stYear+stMonth+stDay;
		}
		catch(Exception ex)
		{

		}
		return "";
	}
	
	/*
	 * on transforme 1231 (MMDD) en 31/12 (DD/MM)
	 */
	static public String mmddTOdd_mm(String hhmm)
	{
		try
		{
			if (hhmm.length()!=4)
				return "";

			String mois=hhmm.substring(0, 2);
			String jour=hhmm.substring(2, 4);
		
			return jour+"/"+mois;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	static public void sendEmailMsg(Context c, String to, String text, String Subject)
	{
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
		sendIntent.setType("message/rfc822");
		c.startActivity(Intent.createChooser(sendIntent, "Envoyer un message"));
	}
	static public void sendEmailMsg2(Activity c, String to, String cc, String cci, String text, String Subject)
	{
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
		sendIntent.putExtra(Intent.EXTRA_CC, new String[]{cc});
		sendIntent.putExtra(Intent.EXTRA_BCC, new String[]{cci});
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
		sendIntent.setType("message/rfc822");
		c.startActivityForResult(Intent.createChooser(sendIntent, "Envoyer un message"),9944);
	}
	
	/*
	 * Transform� un String en  Double
	 */
	static public double GetStringToDoubleDanem(String stvaleur)
	{
		try
		{
			double dValeur=0;
			if(stvaleur==null)
			{
				stvaleur="0";
			}
			if(stvaleur.equals(""))
			{
				stvaleur="0";
			}
			stvaleur=stvaleur.replace(",", ".");
			dValeur=Double.parseDouble(stvaleur);

			return dValeur;
		}
		catch(Exception ex)
		{
			return 0;
		}




	}
	/*
	 * Transform� un String en  float
	 */
	static public float GetStringToFloatDanem(String stvaleur)
	{
		try
		{
			float dValeur=0;
			if(stvaleur==null)
			{
				stvaleur="0";
			}
			if(stvaleur.equals(""))
			{
				stvaleur="0";
			}
			stvaleur=stvaleur.replace(",", ".");
			//dValeur=Float.parseDouble(stvaleur);
			dValeur=Float.parseFloat(stvaleur);

			return dValeur;
		}
		catch(Exception ex)
		{
			return 0;
		}




	}
	/*
	 * Transform� un String en  float
	 */
	static public long GetStringToLongDanem(String stvaleur)
	{
		try
		{
			long dValeur=0;
			if(stvaleur==null)
			{
				stvaleur="0";
			}
			if(stvaleur.equals(""))
			{
				stvaleur="0";
			}
			stvaleur=stvaleur.replace(",", ".");
			//dValeur=Float.parseDouble(stvaleur);
			dValeur=Long.parseLong(stvaleur);
					
					
			return dValeur;
		}
		catch(Exception ex)
		{
			return 0;
		}




	}
	/*
	 * Transform� un String en  Int
	 */
	static public int GetStringToInt2Danem(String stvaleur)
	{
		try
		{
			double dValeur=0;
			dValeur=Fonctions.GetStringToDoubleDanem(stvaleur);
			
			DecimalFormat nf = new DecimalFormat("#");
			nf.setMaximumFractionDigits(Integer.MAX_VALUE);
			nf.setParseIntegerOnly(true);
			stvaleur=nf.format(dValeur);
			stvaleur=stvaleur.replace(",", ".");

			
			
			

			
			int iValeur=0;
			if(stvaleur==null)
			{
				stvaleur="0";
			}
			if(stvaleur.equals(""))
			{
				stvaleur="0";
			}
			stvaleur=stvaleur.replace(",", ".");
			iValeur=Integer.parseInt(stvaleur);
			

			return iValeur;

		}
		catch(Exception ex)
		{
			return 0;
		}

	}
	/*
	 * Transform� un String en  Int
	 */
	static public int GetStringToIntDanem(String stvaleur)
	{
		try
		{
			int iValeur=0;
			if(stvaleur==null)
			{
				stvaleur="0";
			}
			if(stvaleur.equals(""))
			{
				stvaleur="0";
			}
			stvaleur=stvaleur.replace(",", ".");
			iValeur=Integer.parseInt(stvaleur);
			

			return iValeur;

		}
		catch(Exception ex)
		{
			return 0;
		}

	}
	/*
	 * Enlev� le '.' dans un String 
	 */
	static public String GetStringDanem(String stvaleur)
	{
		String SValeurInt="";
		try
		{
			if(stvaleur==null)
			{
				stvaleur="";

			}
			SValeurInt=stvaleur.replace(",", ".");


			return SValeurInt;
		}
		catch(Exception ex)
		{
			return "";
		}
		





	}
	static public String GetStringDanem2(String stvaleur)
	{
		String SValeurInt="";
		try
		{
			if(stvaleur==null)
			{
				stvaleur="";

			}
			SValeurInt=stvaleur.replace(".", ",");
			


			return SValeurInt;
		}
		catch(Exception ex)
		{
			return "";
		}





	}
	/*
	 * Format double en String avec param�tre du Decimal 
	 * ex: stlong="0.0000" alors valeur avec 4 d�cimal apr�s la virgule
	 */
	static public String GetDoubleToStringFormatDanem(double dValeur, String stlong)
	{
		String stValeur="";


		try
		{
			DecimalFormat dcFormat = new DecimalFormat(stlong);
			stValeur=dcFormat.format(dValeur);
			stValeur=stValeur.replace(",", ".");
	


			return stValeur;
		}
		catch(Exception ex)
		{
			return "";
		}





	}
	public static double decimal (double d) {
        // on cr�e un DecimalFormat pour formater le double en chaine :
        DecimalFormat df = new DecimalFormat();
        df.setGroupingUsed(false);            // Pas de regroupement dans la partie enti�re
        df.setMinimumFractionDigits(1);        // Au minimum 1 d�cimale
        df.setMaximumFractionDigits(340);    // Au maximum 340 d�cimales (valeur max. pour les doubles / voir la doc)
 
        // On formate le double en chaine
        String str = df.format(d);
        // On r�cup�re le caract�re s�parateur entre la partie enti�re et d�cimale :
        char separator = df.getDecimalFormatSymbols().getDecimalSeparator();
        // On ne r�cup�re que la partie d�cimale :
        str = str.substring( str.indexOf(separator) + 1 );
        // Que l'on transforme en double :
        return Double.parseDouble(str);
    }
	/*
	 * Format float en String avec param�tre du Decimal 
	 * ex: stlong="0.0000" alors valeur avec 4 d�cimal ap�s la virgule
	 */
	static public String GetFloatToStringFormatDanem(float dValeur, String stlong)
	{
		String stValeur="";
		try
		{
			DecimalFormat dcFormat = new DecimalFormat(stlong);
			stValeur=dcFormat.format(dValeur);
			stValeur=stValeur.replace(",", ".");



			return stValeur;
		}
		catch(Exception ex)
		{
			return "";
		}





	}
	/*
	 * Transform� un Int en String 
	 */
	static public String getInToStringDanem(int iValeur)
	{
		String stValeur="";

		try
		{
			stValeur=Integer.toString(iValeur);



			return stValeur;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	//BigDecimal.ROUND_HALF_UP
	public static double round(double unrounded, int precision, int roundingMode)
	{
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}
	public static double round(double unrounded, int precision)
	{
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
		return rounded.doubleValue();
	}
	/*
	 * Transform� un long en String 
	 */
	static public String getLongToStringDanem(long lValeur)
	{
		String stValeur="";

		try
		{
			stValeur=Long.toString(lValeur);


			return stValeur;
		}
		catch(Exception ex)
		{
			return "";
		}


	}
	/**
	 * @author Marc VOUAUX
	 * retourne le numver en entier
	 * 
	 * @param c
	 * @return
	 */
	static public String getVersion(Context c,StringBuffer sbVer) {
		int v = 0;
		try {
			v = c.getPackageManager().getPackageInfo(c.getApplicationInfo().packageName, 0).versionCode;
			String version=c.getPackageManager().getPackageInfo(c.getApplicationInfo().packageName, 0).versionName;

			version = version.replace(".","");

			return  v+version;

		} catch (NameNotFoundException e) {
			// Huh? Really?
		}
		return "err";
	}

	static public String getPrixVenteTTC(String PrixVenteHT)
	{
		double prixventeHt=0;
		double tva=1.196; //tva 19.6
		double prixventeTTC=0;
		String GetPrixVenteTTC="";
		prixventeTTC=Fonctions.GetStringToDoubleDanem(PrixVenteHT)*tva;
		GetPrixVenteTTC =Fonctions.GetDoubleToStringFormatDanem(prixventeTTC, "0.00");

		int v = 0;
		try {
			prixventeTTC=prixventeHt*tva;

		} catch (Exception ex) {
			// Huh? Really?
		}

		return GetPrixVenteTTC;
	}
	static public String Left(String val,int len)
    {
    	try
    	{
    		if (len>val.length()) len=val.length()-0;
    		String chaine=val.substring(0,len);
    		
    		return chaine;
    	}
    	catch(Exception ex)
    	{
    		return "";	
    	}
    }
    static public String Mid(String val,int start,int len)
    {
    	try
    	{
    		if (start+len>val.length()) len=val.length()-start;
    		String chaine=val.substring(start,start+len);
    		
    		return chaine;
    	}
    	catch(Exception ex)
    	{
    		return "";	
    	}
    }
    
    /*
     * mv: right=right2 car je ne comprend pas le code >3 ???? 7/9/2012
     */
    static public String Right(String val,int len)
    {
    	try
    	{
    		return Right2(val,len);
    		/*
    		int lendebut=0;
    		int lenfin=0;
    		if (val.length()>3) lendebut=val.length()-len;
    		
    		lenfin=lendebut+len;
    		
    		
    		String chaine=val.substring(lendebut,lenfin);
    		
    		return chaine;
    		*/
    	}
    	catch(Exception ex)
    	{
    		return "";	
    	}
    }
    static public String Right2(String val,int len)
    {
    	try
    	{
    		int lendebut=0;
    		int lenfin=0;
    		//Si la longeur est > � la longeur de la chaine alors longeur =longeur chaine
    		if(len>val.length())
    		{
    			len=val.length();
    		}
    		
    		lendebut=val.length()-len;
    		
    		lenfin=lendebut+len;
    		
    		
    		String chaine=val.substring(lendebut,lenfin);
    		
    		return chaine;
    	}
    	catch(Exception ex)
    	{
    		return "";	
    	}
    }
    static public String ReplaceGuillemet(String val)
    {
    	String toto="";
    	
    	try
    	{
    		//on double les quotes si il y a une quote    		
    		toto=val.replace("�",".");
    		toto=val.replace("\r\n"," ");
    		toto=val.replace("\r"," ");
    		toto=val.replace("\n"," ");
    		
    		return toto;
    		
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	return "";
    }
    static public String ReplacePI(String val)
    {
    	String toto="";
    	
    	try
    	{
    		//on double les quotes si il y a une quote    		
    		toto=val.replace("|","");
    		toto=toto.trim();
    		return toto;
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	return "";
    }
    static public String ReturnRetourChariot(String val)
    {
    	String toto="";
    	
    	try
    	{
    		//on double les quotes si il y a une quote  
    		toto=val.replace("|","\r\n");
    		
    		return toto;
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	return "";
    }
    static public String FindInduitive(String val)
    {
    	String toto="";
    	
    	try
    	{
    		//on double les quotes si il y a une quote    		
    		//toto=val.replace("�",".");
    		//toto=val.replace("\r\n"," ");
    		//toto=val.replace("\r"," ");
    		//toto=val.replace("\n"," ");
    		toto=val.replace(" ","%");
    		
    		return toto;
    	}
    	catch(Exception ex)
    	{
    		
    	}
    	return "";
    }
    public static boolean Longeur(String s,int Max){
    	if(Max==0)
    	   Max=255;
    	int tailleString=0;
    	
		boolean result = true;
		if(s != null && s.equals("") == true)
		{
			
		}
		else
		{
			tailleString=s.length();
			if(tailleString>Max)
				result = false;
				
		}

		return result;
	}
    public static int LastDayOfMonth(int nMonth)
    {
    	switch(nMonth)
    	{
    	case 1 :
    	case 3 :
    	case 5 :
    	case 7 :
    	case 8 :
    	case 10 :
    	case 12 :
    		return 31;
    	case 2 :
    		return 28;
    	case 4 :
    	case 6 :
    	case 9 :
    	case 11 :
    		return 30;
    	default :
    		return -1;

    	}
    }
    static public String AddSpace(String val,int leng, int Calibre)// 0 : a gauhe - 1: a droite
    {
    	/*String toto="";
    	
    	
    	try
    	{
    		if(Calibre==sqlite_h.LV_RIGHT)//calibre � droite
    		{
    			toto = String.format("%"+leng+"s", val);
            	
    		}
    		else
    		{
    			toto = String.format("%-"+leng+"s", val);
            	
    		}
    	
    		return toto;
    	}
    	catch(Exception ex)
    	{
    		
    	}*/
    	return "";
    }
    
    public static ArrayList antiDoublons(ArrayList al) {
    	 
        ArrayList al2 = new ArrayList();
        for (int i=0; i<al.size(); i++) {
            Object o = al.get(i);
            if (!al2.contains(o))
                al2.add(o);
        }
        al = null;
        return al2;
 
    }
   
    static public boolean convertToBool(String val)
    {
    	try {
			String valmini=val.toLowerCase();
			if (valmini.equals("false") || valmini.equals("0") || valmini.equals("") || valmini.equals("n"))
				return false;
			if (valmini.equals("true") || valmini.equals("1") || valmini.equals("o"))
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
    	return false;
    }
    static public  String SearchLike(String stcritere)
	{
		String Valeur=Fonctions.GetStringDanem(stcritere);
		String stLike="";
		if(!stcritere.equals(""))
		{
			Valeur=Fonctions.GetStringDanem(stcritere).replace("*", "%");
				
		}
		return Valeur;
		
		
	}
    static public  boolean WriteLin(FileWriter fos, String stLine ) 
    {
    	try {
	    	fos.write(stLine   );
	    	String stFinLigne = "\r\n" ;
	    	fos.write(stFinLigne  );
   		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		}
    	return true ;
   }
    
    static public  boolean WriteLin(OutputStream fos, String stLine ) 
    {
    	try {
	    	fos.write(stLine.getBytes(),0,stLine.length()   );
	    	String stFinLigne = "\r\n" ;
	    	fos.write(stFinLigne.getBytes(),0,stFinLigne.length()   );
   		
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		}
    	return true ;
   }
    public static String PutFld(String stSrc,String stFld,int nx,int nLng,int nLeft)
    {
    	return "" ;
    	/*String stLeft,stMid,stRight;
    	
    	//si cadre a gauche
    	if (nLeft==sqlite_h.LV_LEFT)
    	{
    		stLeft=stSrc;
    		stLeft=Fonctions.Left(stLeft, nx);// stLeft.Left(nx);
    		stRight=stSrc;
    		stRight=Fonctions.Right(stRight,stRight.length()-(nx+nLng));
    		stMid = Fonctions.AddSpace(stFld, nLng,sqlite_h.LV_LEFT);
    	//	stMid=stFld;
    	//	stMid+="                                                                                                                                                                                                                                                   ";
    	//	stMid=stMid.Left(nLng);
    		return stSrc=stLeft+stMid+stRight;
    	}
    	else
    	{
    		stLeft=stSrc;
    		stLeft=Fonctions.Left(stLeft,nx);
    		stRight=stSrc;
    		stRight=Fonctions.Right(stRight, stRight.length()-(nx+nLng));
//    		stMid="                                                                                                                                                                                                                                                   ";
//    		stMid+=stFld;
//    		stMid=stMid.Right(nLng);
    		stMid = AddSpace(stFld, nLng, sqlite_h.LV_RIGHT);

    		return stSrc=stLeft+stMid+stRight;
    	}*/
    }


    static public void turnGPSOn(Activity act)
    {
         Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
         intent.putExtra("enabled", true);
         act.sendBroadcast(intent);

        String provider = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            act.sendBroadcast(poke);


        }
        
      
    }
    // automatic turn off the gps
    static public void turnGPSOff(Activity act)
    {/*
        String provider = Settings.Secure.getString(act.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            act.sendBroadcast(poke);
        }
        */
      //Disable GPS
        Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        intent.putExtra("enabled", false);
        act.sendBroadcast(intent);
    }
    static public String getYYYYMMDD_MOINS(int ndelay)
    {
    	 final Calendar c = Calendar.getInstance( ); 
    	 
         int mYear = c.get(Calendar.YEAR); 
         int mMonth = c.get(Calendar.MONTH); 
         int mDay = c.get(Calendar.DAY_OF_MONTH);
        
        
      // entrée de la nouvelle date (dans 30 jours)
         c.set(mYear, mMonth, mDay -ndelay);
 
         // obtention des éléments de cette nlle date
         
          mYear = c.get(Calendar.YEAR); 
          mMonth = c.get(Calendar.MONTH)+1; 
          mDay = c.get(Calendar.DAY_OF_MONTH);
      
      
         String Date=String.format("%04d%02d%02d", mYear,mMonth,mDay);
         
         return Date;
         
         
    
    }
    static public String setYYYYMMDD_PLUS(String yyyymmdd,int ndelay)
    {
    	 
    
         DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
         Date d=new Date();;
		try {
			d = format.parse(yyyymmdd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
         Calendar c = Calendar.getInstance();
         c.setTime(d); // Now use today date.
         c.add(Calendar.DATE, ndelay); // Adding 5 days
         String output = sdf.format(c.getTime());
         
         return output;
         
         
    
    }
    static public boolean isleap (int yr)
    {
    	return ( yr % 400 == 0 || (yr % 4 == 0 && yr % 100 != 0));
    	 
    }
    
    static public String scalar_to_ymd (long scalar )
    {
       int n,yr,mo,day;                /* compute inverse of years_to_days() */

       for ( n = (int)((scalar * 400L) / 146097); years_to_days(n) < scalar;)
          n++;                          /* 146097 == years_to_days(400) */
       yr = n;
       n = (int)(scalar - years_to_days(n-1));
       if ( n > 59 ) {                       /* adjust if past February */
          n += 2;
          if ( isleap(yr) )
             n -= n > 62 ? 1 : 2;
       }
       mo = (n * 100 + 3007) / 3057;    /* inverse of months_to_days() */
       day = n - months_to_days(mo);
       
       String date=String.format("%04d%02d%02d",yr,mo,day);
       return date;
    }
    
    static public long ymd_to_scalar (int annee, int mois, int jour)
    {
      long scalaire;
      scalaire = jour + months_to_days(mois);
      if ( mois > 2 )                         /* Verification si c'est uen annee bissextile ou non*/
        scalaire -= isBissextile(annee) ? 1 : 2;
      annee--;
      scalaire += years_to_days(annee);
      return scalaire;
    }
  //Fonction calculant la probailite que ce soint uen annee bissextile ou non
    static public boolean isBissextile (int annee)
    {
    	boolean i=false;
    	if(annee % 400 == 0 )
    	{
    		i=true;
    	}
    	if(annee % 4 == 0 && annee % 100 != 0 )
    	{
    		i=true;
    	}
    	
      return i; //c'est vrai que les 2 sont super util enssemble mais au moins ca evite certains de problemes
    }

    //Bon et bien pour la suite je ne pense pas qu'il y est besoin d'explication ?

    static int months_to_days (int mois)
    {
      return (mois * 3057 - 3007) / 100;
    }

    static long years_to_days (int annee)
    {
      return annee * 365L + annee / 4 - annee / 100 + annee / 400;
    }
    
    public static void playSound(Context c,int res)
    {
    	if (res==R.raw.beep_nok)
    	{
    		int i=0;
    		i++;
    		
    	}
    	MediaPlayer mp = MediaPlayer.create(c, res);
         mp.setOnCompletionListener(new OnCompletionListener() {

             @Override
             public void onCompletion(MediaPlayer mp) {
                 // TODO Auto-generated method stub
                 mp.release();
             }

         });   
         mp.start();
    }
    

}
