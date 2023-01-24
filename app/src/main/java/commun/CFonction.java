package commun;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.danem.cnoir.qrpoche.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CFonction extends Fonctions {
	
	public static boolean IsFileExist(String path,String fichier)
	{
		try {
		
			String contain=Fonctions.GiveFld(fichier,0,".",true);
			String ext=Fonctions.GiveFld(fichier,1,".",false);
			contain=contain.replace("*","");
			ext=ext.replace("*","");
			
			FileChecker f=new FileChecker();
			return f.isFileExist(path,ext,contain);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public static int _wtoi(String val)
	{
		return Fonctions.convertToInt(val);
	}
	public static String DecrypteCodeBarre(String stCodeBarre)
	{
		
		String stIDColis="",stCP="";
		if (stCodeBarre.length()==18 || stCodeBarre.length()==17)
		{
			stIDColis=Fonctions.Left(stCodeBarre,12);
			stCP=Fonctions.Mid(stCodeBarre,12,5);
		}
		//DAMART
		if (stCodeBarre.length ()==24)
		{
			stIDColis= Fonctions.Right( stCodeBarre,12);
			stIDColis="08000"+ Fonctions.Left(stIDColis,7);
			stCP=Fonctions.Mid(stCodeBarre,3,5);
		}
		if (stCodeBarre.length()==12)
		{
			stIDColis=stCodeBarre;
			stCP="     ";
		}
		if (stCodeBarre.length()==13)
		{
			stIDColis=Fonctions.Left(stCodeBarre,12);
			stCP="     ";
		}
		return stIDColis+";"+stCP;
		

	}
	
	static public String GiveFld(String val,int pos,String sep)
	{
		return Fonctions.GiveFld(val, pos, sep, true);
	}
	

	
	static public boolean WriteProfileString(Context c, String key, 
			String val) 
	{
		String app=c.getString(R.string.app_name);
		SharedPreferences settings = c.getSharedPreferences(app, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString(key, val);

	      // Commit the edits!
	      return editor.commit();
	}
	static public String GetProfileString(Context c, String key, 
			String val) 
	{
		String app=c.getString(R.string.app_name);
		 SharedPreferences settings = c.getSharedPreferences(app, 0);
	     String result = settings.getString(key, val);
	     
	     return result;
	}
	public static float wcstod(String stVal , Integer iBase )
	{
		//iBase non utilis�
		return convertToFloat(stVal);
	}
	static public String Trim(String val)
	{
		return val.trim();
	}
	static public String GetCurDate()
	{
		String m_stCurDate;

		m_stCurDate=getYYYYMMDD();
		//^ Initialisation de la date courante
		//m_stCurDate.Format(_T("%.4i%.2i%.2i"), pST.wYear, pST.wMonth, pST.wDay);

		return(m_stCurDate);
	}
	static public String GetCurTime()
	{
		String m_stCurDate;

		m_stCurDate=gethhmmss();
		//^ Initialisation de la date courante
		//m_stCurDate.Format(_T("%.4i%.2i%.2i"), pST.wYear, pST.wMonth, pST.wDay);

		return(m_stCurDate);
	}
	
	public static String YYYYMMDD_TO_DD_MM_YYYY(String date)
	{
		return Fonctions.YYYYMMDD_to_dd_mm_yyyy(date);
	}
	public static long GetTickCountPerso () 
	{
		return System.currentTimeMillis() ;
	}
	public static boolean IsNumeriqueOnly(String stFld)
	{
		stFld=stFld.replace("0", "" );
		stFld=stFld.replace("1", "" );
		stFld=stFld.replace("2", "" );
		stFld=stFld.replace("3", "" );
		stFld=stFld.replace("4", "" );
		stFld=stFld.replace("5", "" );
		stFld=stFld.replace("6", "" );
		stFld=stFld.replace("7", "" );
		stFld=stFld.replace("8", "" );
		stFld=stFld.replace("9", "" );
		if( !stFld.equals("") )
			return false ;
		return true ;
	}

	
}
