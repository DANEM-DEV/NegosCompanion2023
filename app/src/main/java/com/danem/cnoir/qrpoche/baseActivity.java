package com.danem.cnoir.qrpoche;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import commun.Fonctions;

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.LocalBroadcastManager;

public class baseActivity extends Activity {

    protected boolean disableBackButton=false;
	Handler hTimer;
	protected Handler hFromChildActivity;//initialis�e par l'activity si on veut recevoir des msg du background
 
	boolean isRunning;
	protected final int LAUNCH_SCAN=440;
	public static final int LAUNCH_INFOCOMPL=441;




	//protected ProgressDialog m_ProgressDialog = null; 
	public baseActivity()
	{
	 
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//UiSettings();
		isRunning = false;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		


	}
	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
	}
	public int getCurVer()
	{
		return Fonctions.convertToInt(curver);
	}

	protected void setListeners()
	{
	}
 
	@Override
	protected void onStart() {
		super.onStart();
	}
	static public void ERRORSOUND()
	{
		
	}
	static public void ERRORSOUND_2()
	{
		
	}
	static public void  ERRORSOUND_3()
	{
		
	}

	protected MenuItem addMenu(Menu menu, int stringID, int iconID){
		MenuItem item  = menu.add(Menu.NONE, stringID, Menu.NONE, stringID);
		int size = menu.size() - 1;
		if(iconID != -1)
			menu.getItem(size).setIcon(iconID);
		menu.getItem(size).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return item;
	}
 
	static public void launchActivityForResult(Activity a,Class<?> act,Bundle b,int id){
		
		Intent i = new Intent(a, act);
	// 	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//	i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
		if (b!=null)
			i.putExtras(b);
		a.startActivityForResult(i, id);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/*if (requestCode==LAUNCH_SETTINGS)
		{
			getGeneralInfo();
		}*/
	}
	
	protected void returnOK(Intent ri)
	{
		setResult(RESULT_OK,ri);
		finish();
	}
	protected void returnOK()
	{
		setResult(RESULT_OK);
		finish();
	}
	protected void returnCancel()
	{
		setResult(RESULT_CANCELED);
		finish();
	}
	

	public void hideKeyb2( )
	{
		try {
//			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}            	 

	}

    public void hideKeyb3(Activity activity )
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

    public void showKeyb2(View v)
    {
        try {
//			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);

		} catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	private  static File createDirectory(String dir){
		 String PATH = Environment.getExternalStorageDirectory() +"/"+dir;
		 File f = new File(PATH);
		 f.mkdirs(); 

		 return f;

	 }

	 public File[] getAllFiles(final String pattern,String dir){
		 return createDirectory(dir).listFiles(new FilenameFilter() {
			    @Override
			    public boolean accept(File dir, String name) {
			        return name.contains(pattern);
			    }
			});
	 }

	public void FurtiveMessageBox(String message){
		Toast toast= Toast.makeText(this,message,Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER,30,200);
		toast.show();
	}


	  
	/**
	 * @author Marc VOUAUX
	 * @param row
	 * @param list_id
	 * @param tv_id
	 * @param text
	 * exemple : ChangeRowTextViewValue(2,R.id.listView1,R.id.bottomtext,"tutu");
	 */
	void ChangeRowTextViewValue(int row,int list_id, int tv_id,String text)
	{
		TextView tv;
		View linha; 
		ListView myListView=(ListView)findViewById(list_id);
		linha=myListView.getChildAt(row);
		tv= (TextView) linha.findViewById(tv_id);
		tv.setText(text);
	}    

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @param val
	 */
	protected void setEditViewText(Activity act,int rID, String val)
	{
		try
		{
			EditText et=(EditText)act.findViewById(rID);
			setEditViewText(et,val);
		}
		catch(Exception ex)
		{

		}
	}

	protected void setButtonText(Button et,String val)
	{
		try
		{
			et.setText(val);
		}
		catch(Exception ex)
		{

		}
	}
	
	protected void setTextViewText(TextView et, String val)
	{
		try
		{
			et.setText(val);
		}
		catch(Exception ex)
		{

		}
	}
	
	protected void setEditViewText(EditText et,String val)
	{
		try
		{
			et.setText(val);
		}
		catch(Exception ex)
		{

		}
	}
	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @param val
	 */
	protected void setTextViewText(Activity act,int rID, String val)
	{
		try
		{
			TextView et=(TextView)act.findViewById(rID);
			et.setText(val);
		}
		catch(Exception ex)
		{

		}
	}
	public  void launchWeb(String url )
	{
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	protected String getEditViewText(Activity act,int rID )
	{
		try
		{
			EditText et=(EditText)act.findViewById(rID);
			return et.getText().toString();
		}
		catch(Exception ex)
		{

		}
		return "";
	}

	EditText getEditView(Activity act,int rID )
	{
		try
		{
			EditText et=(EditText)act.findViewById(rID);
			return et;
		}
		catch(Exception ex)
		{

		}
		return null;
	}

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	protected String getTextViewText(Activity act,int rID )
	{
		try
		{
			TextView et=(TextView)act.findViewById(rID);
			return et.getText().toString();
		}
		catch(Exception ex)
		{

		}
		return "";
	}

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	float getEditViewFloatValue(Activity act,int rID )
	{
		try
		{
			String val=getEditViewText(act,rID );
			return Float.parseFloat(val);
		}
		catch(Exception ex)
		{

		}
		return 0;
	}

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	float getTextViewFloatValue(Activity act,int rID )
	{
		try
		{
			String val=getTextViewText(act,rID );
			return Float.parseFloat(val);
		}
		catch(Exception ex)
		{

		}
		return 0;
	}  

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	String getSpinnerValue(Activity act,int rID )
	{
		try
		{
			Spinner et=(Spinner)act.findViewById(rID);
			String val=et.getSelectedItem().toString();
			return val;
		}
		catch(Exception ex)
		{

		}
		return "";
	}      

	public String getSpinnerValue(Spinner et )
	{
		try
		{
			String val=et.getSelectedItem().toString();
			return val;
		}
		catch(Exception ex)
		{

		}
		return "";
	}      
	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	public int getSpinnerSelectedIdx(Activity act,int rID )
	{
		try
		{
			Spinner et=(Spinner)act.findViewById(rID);
			int pos=et.getSelectedItemPosition();
			return pos;
		}
		catch(Exception ex)
		{

		}
		return -1;
	}       
	public int getSpinnerSelectedIdx(Spinner et )
	{
		try
		{
			int pos=et.getSelectedItemPosition();
			return pos;
		}
		catch(Exception ex)
		{

		}
		return -1;
	}       

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	public int getCheckBoxValue(Activity act,int rID )
	{
		try
		{
			CheckBox et=(CheckBox)act.findViewById(rID);
			boolean b=et.isChecked();
			if (b) 
				return 1;
			else
				return 0;
		}
		catch(Exception ex)
		{

		}
		return 0;
	}

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @param val
	 */
	public void SetCheckBoxValue(Activity act,int rID,int val )
	{
		try
		{
			CheckBox et=(CheckBox)act.findViewById(rID);
			setCheckBoxValue(et,val);
		}
		catch(Exception ex)
		{

		}
	}
	
	public void setCheckBoxValue(CheckBox et,int val)
	{
		try
		{
			boolean b=false;
			if (val==1) 
				b=true;
			else
				b=false;
	
			et.setChecked(b);
		}
		catch(Exception ex)
		{

		}
	}
	public void setRatingBarValue(RatingBar et,int val)
	{
		try
		{
			boolean b=false;
			if (val==1) 
				b=true;
			else
				b=false;
	
			et.setRating(val);
		}
		catch(Exception ex)
		{

		}
	}
	public void setCheckBoxValue(CheckBox et,boolean b)
	{
		try
		{
			et.setChecked(b);
		}
		catch(Exception ex)
		{

		}
	}
	
	static public void HHMM_to_TimePicker(Activity act,int rID,String hour)
	{
		try
		{
			if (hour.length()!=4) return;

			TimePicker dp=(TimePicker)act.findViewById(rID);

			int Hour=Integer.parseInt(   hour.substring(0,2));
			int Minut=Integer.parseInt(   hour.substring(2,4));

			dp.setCurrentHour(Hour);
			dp.setCurrentMinute(Minut);

		}
		catch(Exception ex)
		{

		}
		return ;
	}
	static public void HHMM_to_TimePicker(TimePicker dp,String hour)
	{
		try
		{
			if (hour.length()!=4) return;

			int Hour=Integer.parseInt(   hour.substring(0,2));
			int Minut=Integer.parseInt(   hour.substring(2,4));

			dp.setCurrentHour(Hour);
			dp.setCurrentMinute(Minut);
		}
		catch(Exception ex)
		{

		}
		return ;
	}
	static public String TimePicker_to_HHMM(Activity act,int rID)
	{
		try
		{
			TimePicker dp=(TimePicker)act.findViewById(rID);

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

	/**
	 * @author Marc VOUAUX
	 * @param act
	 * @param rID
	 * @return
	 */
	static public String DatePicker_to_YYYYMMDD(Activity act,int rID)
	{
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);
			return DatePicker_to_YYYYMMDD(dp);
		}
		catch(Exception ex)
		{

		}
		return "";
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
	
	static public String DatePicker_to_YYYY(Activity act,int rID)
	{
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);

			int year=dp.getYear();

			String stYear=String.valueOf(year);

			return stYear;
		}
		catch(Exception ex)
		{

		}
		return "";
	}
	static public String DatePicker_to_MM(Activity act,int rID)
	{
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);

			int month =dp.getMonth()+1;

			String stMonth=String.valueOf(month);
			stMonth="0"+stMonth;
			int len=stMonth.length();
			stMonth=stMonth.substring(len-2, len);

			return stMonth;
		}
		catch(Exception ex)
		{

		}
		return "";
	}
	static public String DatePicker_to_DD(Activity act,int rID)
	{
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);

			int day=dp.getDayOfMonth();

			String stDay=String.valueOf(day);
			stDay="0"+stDay;
			int len=stDay.length();
			stDay=stDay.substring(len-2, len);

			return stDay;
		}
		catch(Exception ex)
		{

		}
		return "";
	}

	static public String DatePicker_to_Day_Of_Year(Activity act,int rID)
	{
		String stDayofYear="111";
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);

			int day=dp.getDayOfMonth();
			int month =dp.getMonth();
			int year=dp.getYear();

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.DAY_OF_YEAR);

			stDayofYear=Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stDayofYear;

	}

	static public String DatePicker_to_Day_Of_Week(Activity act,int rID)
	{
		String stDayofWeek="111";
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);
			int day=dp.getDayOfMonth();
			int month =dp.getMonth();
			int year=dp.getYear();

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.DAY_OF_WEEK);

			stDayofWeek=Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stDayofWeek;

	}

    /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		addMenu(menu, R.string.capture, R.drawable.bunddl_settings);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch(item.getItemId()) {

			case R.string.action_settings:

				showDialogConnexion();
				break;
		}

		return true;
	}*/

	static public String DatePicker_to_Week_Of_Year(Activity act, int rID)
	{
		String stWeekofYear="111";
		try
		{
			DatePicker dp=(DatePicker)act.findViewById(rID);
			int day=dp.getDayOfMonth();
			int month =dp.getMonth();
			int year=dp.getYear();

			Calendar calendar2 = new GregorianCalendar(year, month, day);
			int doy2 = calendar2.get(Calendar.WEEK_OF_YEAR);

			stWeekofYear= Fonctions.getInToStringDanem(doy2);
		}
		catch(Exception ex)
		{

		}
		return stWeekofYear;

	}

	static public void YYYYMMDD_to_DatePicker(Activity act,int rID,String date)
	{
		try
		{
			if (date.length()!=8) return;

			DatePicker dp=(DatePicker)act.findViewById(rID);

			YYYYMMDD_to_DatePicker(dp, date);

		}
		catch(Exception ex)
		{

		}
		return ;
	}
	
	static public void YYYYMMDD_to_DatePicker(DatePicker dp,String date)
	{
		try
		{
			if (date.length()!=8) return;

			int day=Integer.parseInt(   date.substring(6,8));
			int month=Integer.parseInt(   date.substring(4,6))-1;
			int year=Integer.parseInt(   date.substring(0,4));

			dp.updateDate(year, month, day);

		}
		catch(Exception ex)
		{

		}
		return ;
	}

	/**
	 * recuperation securis�e d'un bundle
	 * @author Marc VOUAUX
	 * @param bu
	 * @param key
	 * @return
	 */
	public static String getBundleValue(Bundle bu, String key)
	{
		String val="";
		try
		{
			val = bu.getString(key);
			if (val==null) val="";
			return val;
		}
		catch(Exception ex)
		{
			val="";	
		}
		return val;
	}
	public static int getBundleValue(Bundle bu, String key,boolean i)
	{
		int val=0;
		try
		{
			val = bu.getInt(key);
			 
			return val;
		}
		catch(Exception ex)
		{
			val=0;	
		}
		return val;
	}
	protected boolean IsDebutScanOk()
	{
		return true;
	}
	protected void FinScan()
	{
		
	}
	public static boolean getBundleValueBool(Bundle bu, String key)
	{
		boolean val=false;
		try
		{
			val = bu.getBoolean(key);
		 
			return val;
		}
		catch(Exception ex)
		{
			val=false;	
		}
		return val;
	}
	public static int getBundleValueInt(Bundle bu, String key)
	{
		int val=-1;
		try
		{
			val = bu.getInt(key);
		 
			return val;
		}
		catch(Exception ex)
		{
			val=-1;	
		}
		return val;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (( keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME ) && disableBackButton) {
			
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		
		// Unregister since the activity is not visible
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(
		//		mMessageReceiver);
		super.onDestroy();
	}
	@Override
	public void onPause() {
		// Unregister since the activity is not visible
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(
		//		mMessageReceiver);
		super.onPause();
		activityVisible = false ;
	}

	private boolean activityVisible = false ;
	public boolean isActivityVisible() {
		return activityVisible;
	}

	@Override
	public void onResume() {
		super.onResume();
		activityVisible = true ;

	}
	
	/**
	 * R�cuperation de la version projet
	 * @return
	 */
	public String  VersionEntry ()
	{
		String Version="";
		try
		{
			PackageManager pm = getPackageManager();
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo( getPackageName(), 0);
			Version=pi.versionName ;
		}
		catch( Exception e ) 
		{
			e.printStackTrace();
		}
		return Version;

	}

	public void loadImageViewfromFilePath(Activity act, int rID, String filepath)
	{
		try {
			File imgFile = new  File(filepath);
			if(imgFile.exists())
			{
				ImageView iv=(ImageView)act.findViewById(rID);
				iv.setImageURI(Uri.fromFile(imgFile));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 protected static String gethhmm( TimePicker timepicker_hour){
		 String s = pad(timepicker_hour.getCurrentHour())+pad(timepicker_hour.getCurrentMinute());
		 
		 return s;
	 }
	 protected static String getAAAAMMJJ_hhmm(DatePicker datePicker, TimePicker timepicker_hou){
		 String s = datePicker.getYear()+pad(datePicker.getMonth()+1)+pad(datePicker.getDayOfMonth())+"_"
				 +pad(timepicker_hou.getCurrentHour())+pad(timepicker_hou.getCurrentMinute());
		 return s;
	 }
	 protected static int getYear_AAAAMMJJ_hhmm(String s){
		 if(s != null && s.length()>1){
			 String year = s.substring(0, 4);
			 return Integer.parseInt(year);
		 }
		 return 0;
	 }
	 protected static int getMonth_AAAAMMJJ_hhmm(String s){
		 if(s != null && s.length()>1){
			 
			 String month = s.substring(4, 6);
//			 Log.d("TAG",s +" month : "+month);
			 return Integer.parseInt(month) -1;
		 }
		 return 0;
	 }
	 
	 protected static int getDay_AAAAMMJJ_hhmm(String s){
		 if(s != null && s.length()>1){
			 String day = s.substring(6, 8);
			 return Integer.parseInt(day);
		 }
		 return 0;
	 }
	 protected static int[] getHours_AAAAMMJJ_hhmm(String s){
		 int[] hours = new int[8];
		 if(s != null && s.length()>1){
			 
				 String h = s.substring(9, 11);
				 String m = s.substring(11, 13);
				 hours[0] = Integer.parseInt(h);
				 hours[1] = Integer.parseInt(m); 
			 
			 return hours;
		 }
		 return null;
	 }
	 protected static String getAAAAMMJJ(DatePicker datePicker){
		 String s = datePicker.getYear()+pad(datePicker.getMonth()+1)+pad(datePicker.getDayOfMonth());
		 return s;
	 }
	
		public static String pad(int c) {
			 if (c >= 10)
				 return String.valueOf(c);
			 else
				 return "0" + String.valueOf(c);
		 }
		
		static public void YYYYMMDD_Liv_to_DatePicker(Activity act,int rID,String date,int ndelay)
		{
			try
			{
				
				DatePicker dp=(DatePicker)act.findViewById(rID);

				int day=Integer.parseInt(   date.substring(6,8));
				int month=Integer.parseInt(   date.substring(4,6))-1;
				int year=Integer.parseInt(   date.substring(0,4));
				
				
				Calendar calendar2 = new GregorianCalendar(year, month, day);
				 int yea2 = calendar2.get(Calendar.YEAR); 
		         int month2 = calendar2.get(Calendar.MONTH); 
		         int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		         
		         
		    
		         // entr�e de la nouvelle date (dans 30 jours)
				calendar2.set(yea2, month2, day2 +ndelay);
		 
		         // obtention des �l�ments de cette nlle date
		         
				yea2 = calendar2.get(Calendar.YEAR); 
				month2 = calendar2.get(Calendar.MONTH); 
				day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		          
				dp.updateDate(yea2, month2, day2);

			}
			catch(Exception ex)
			{

			}
			return ;
		}
		static public String YYYYMMDDhhmm_TimeInMillis(String date)
		{
			String milliseconde ="";
			try
			{
				if(Fonctions.GetStringDanem(date).equals(""))
				{
					date=String.valueOf(System.currentTimeMillis()); 
				}
				int year=Integer.parseInt(   date.substring(0,4));
				int month=Integer.parseInt(   date.substring(4,6))-1;
				int day=Integer.parseInt(   date.substring(6,8));
				int hour=Integer.parseInt(   date.substring(8,10));
				int minute=Integer.parseInt(   date.substring(10,12));
				int seconde=Integer.parseInt(   date.substring(12,14));
				
				Calendar calendar2 = new GregorianCalendar(year, month, day,hour,minute,seconde);
			
				milliseconde= Fonctions.getLongToStringDanem(calendar2.getTimeInMillis());
				  
			}
			catch(Exception ex)
			{
				milliseconde="";
			}
			return milliseconde ;
		}
		
		String curver;
		int minver = 0;
		StringBuffer sbVer = new StringBuffer();
		

		
		public void launchWaze(String adress)
		{
			try
			{
			   String url = "waze://?q="+adress;
			    Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
			   startActivity( intent );
			}
			catch ( ActivityNotFoundException ex  )
			{
			  Intent intent =
			    new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
			  startActivity(intent);
			}
		}
	//Fonction génèreique pour 'corriger' tous les codes barres scannés
	public static  String  CleanCodebarre(String cb)
	{
		cb = cb.replace("%", "" ) ;
		cb = cb.replace("'", "" ) ;
		cb = cb.replace(" ", "" ) ;

		if ( cb.length() > 254 )
			cb = "" ;
		/*dbKD402Colis kd402=new dbKD402Colis(GetDB());

		//Tentative de conversion du CB (renvoi du code org si pas de correspondance)
		String cb2=kd402.getNumInterneByNumColis(cb);
		if ( !cb2.equals("") )
			return cb2 ;*/
		return cb ;
	}

}
