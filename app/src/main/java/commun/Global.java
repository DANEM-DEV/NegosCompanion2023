package commun;


import android.graphics.Bitmap;
import android.os.Handler;


import java.util.ArrayList;

public class Global {
	static public  Bitmap b_signature;
	static public  String receptionnaire;
	static public  String lastCodecLientPere;
	 
	

	static public Handler hRead;
	static public boolean iswedge=false;
	
	static public String GLOBAL_stIdConn;
	static public boolean GLOBAL_bConnexionRunning;;
	static public String lastErrorMessage; 
	static public int GLOBAL_dNbrColis=0;
	static public int GLOBAL_dNbrKiala=0;
	static public String GLOBAL_stLastCP="";
 
	static public int ELAPSE_GPS_SYNC=(int)(5*1000)*60;
	static public long  GLOBAL_ConnTickCount=0;
	
	static public boolean GLOBAL_bPrintLblSig;
 
	static public ArrayList<String> GLOBAL_starAlerteHeure;
	static public ArrayList<String> GLOBAL_starAlerte ;
	
	//Etat KeyBoard
	static public int GLOBAL_EtatKeyboard_ALPHA=0;
	static public int GLOBAL_EtatKeyboard_ALPHANUMERIC=1;
	static public int GLOBAL_EtatKeyboard_NUMERIC=2;
	
	
	static public ArrayList<String> GLOBAL_starLivrAutresMotif ;
	static public ArrayList<String> GLOBAL_starNLCnpai ;
	static public ArrayList<String> GLOBAL_starNLCnrec ;
	static public ArrayList<String> GLOBAL_starNLCpcod ;
	static public ArrayList<String> GLOBAL_starNLrefus ;
	static public ArrayList<String> GLOBAL_starNLautre ;	
	
	static public boolean GLOBAL_bGpsRun ;
	static public boolean GLOBAL_bGpsIsRunning ;
	static public double GLOBAL_dLatitude ;
	static public double GLOBAL_dLongitude ;
	static public String GLOBAL_stSatellite ;
	static public int GLOBAL_dwDOP ;
	static public String GLOBAL_stLastErrorGps ;
	static public boolean GLOBAL_bReconnectNeeded;
	
	static public int GL_iNbrInserCol;
	static public int GL_iNbrUpdateCol;
	static public boolean GL_bLastLigneInsert;
	static public int GL_iNbrColCardsOffAdded;
 
}
