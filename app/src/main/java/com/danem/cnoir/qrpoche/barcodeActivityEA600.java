package com.danem.cnoir.qrpoche;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Triggering;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import commun.Fonctions;
import commun.Global;


//Recup de CP2017, pour eviter les plantages aprés 300/400 ouvertures de scan
public class barcodeActivityEA600 extends  baseActivity   {
 
	  private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action
	    
	 	public boolean bIntegratedScannerPresent = false ;
	    private Vibrator mVibrator;
	    private ScanManager mScanManager = null ;
	    private static SoundPool soundpool = null;
	    private static int soundid;
	    private String barcodeStr;
	    private boolean isScaning = false;
		public boolean bWedge =false ;
		public boolean bBlockScan = true ;		//pour correction rapide des plantages, pas de fermeture du scan mais blocage avec bip d'erreur (en pas d'envoi de message)
		EditText etCB = null ; //pour comptatibilitéé avec version wedge

	void open()
	{
		
		 // TODO Auto-generated method stub
		try {
			if ( mScanManager == null ) {
				mScanManager = new ScanManager();
				if( mScanManager != null ) {
					mScanManager.openScanner();

					mScanManager.switchOutputMode(0);
					mScanManager.setTriggerMode(Triggering.HOST);
					bIntegratedScannerPresent = true ;
				}
			}
			if ( soundpool == null ) {
				soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
				if (soundpool != null)
					soundid = soundpool.load("/etc/Scan_new.ogg", 1);
			}
		}
		catch(Exception e )
		{
			mScanManager = null ;
			/*HashMap<String, String> v_Params = new HashMap<>();
			v_Params.put("p_Fonction","barcodeActivityEA600.open");
			FManageException.Create().ManageExceptionWithoutThrow( e , v_Params );*/

		}
		bBlockScan = false ;
		resumeCB() ;

	}
	
	void close()
	{
		if(mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
	}
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// result
		super.onCreate(savedInstanceState);
		 
		open();
	    mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		if ( mScanManager != null ) {
			IntentFilter filter = new IntentFilter();
			int[] idbuf = new int[]{PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG};
			String[] value_buf = mScanManager.getParameterString(idbuf);
			if (value_buf != null && value_buf[0] != null && !value_buf[0].equals("")) {
				filter.addAction(value_buf[0]);
			} else {
				filter.addAction(SCAN_ACTION);
			}

			registerReceiver(mScanReceiver, filter);
		}

 
	}

	private void VideBuffer(Intent intent)
	{
		byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
		int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
		byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);

	}

	static long GL_lSecuEntreScans = 0 ; //secu, pour avoir minimum 300ms entre chaque scan
	 private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

	        @Override
	        public void onReceive(Context context, Intent intent) {
	            try {
	            	if ( isScaning == false || isActivityVisible() == false ) {
						VideBuffer(intent);
						return;
					}

					if ( bBlockScan == true )
					{
						//blocage avec son d'erreur
						Fonctions.playSound(barcodeActivityEA600.this , R.raw.beep_nok );
						VideBuffer(intent);
						return;

					}
					// TODO Auto-generated method stub
					//isScaning = false;
					if ( GL_lSecuEntreScans < SystemClock.uptimeMillis())
						GL_lSecuEntreScans = SystemClock.uptimeMillis()+ 700  ;
					else
					{
						VideBuffer(intent) ;
						return ;
					}
					if ( soundpool  != null )
						soundpool.play(soundid, 1, 1, 0, 0, 1);
					//if ( mVibrator != null )
					//	mVibrator.vibrate(100);

					byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
					int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
					byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
					Log.i("debug", "----codetype--" + temp);
					barcodeStr = new String(barcode, 0, barcodelen);
					if(barcodeStr!=null) 	
						setResultText(new String(barcodeStr));
				} catch (Exception e) {
					Log.e("TAG",e.getLocalizedMessage());
				}
 
	        }

	    };
	@Override
	public void onResume() {
	 
		isScaning = true ;
		resumeCB();
		open();
		super.onResume();
	}

	@Override
	public void onPause() {
		isScaning = false ;
		pauseCB();
		close();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		//pauseCB();
		super.onDestroy();
		unregisterReceiver(mScanReceiver) ;
	}

	public boolean pauseCB()
	{
		//responsable des reboots
		boolean bres = false ;
		//if ( mScanManager != null )
		//	mScanManager.closeScanner();
			//bres = mScanManager.lockTrigger();
		bBlockScan = true  ;

    	Log.d("TAG","pauseCB");
		return bres ;
	}
	public boolean resumeCB()
	{
		boolean bres = false ;

		//if ( mScanManager != null )
		//	mScanManager.openScanner();
			//bres = mScanManager.unlockTrigger();
		bBlockScan = false ;
    	Log.d("TAG","resumeCB");
		return bres ;
	}
	
	 
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/* Discard key repeating */
		if (event.getRepeatCount() != 0)
			return super.onKeyDown(keyCode, event);

 
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		/* Discard key repeating */
		if (event.getRepeatCount() != 0)
			return super.onKeyDown(keyCode, event);

	
		return super.onKeyUp(keyCode, event);
	}
  
 
	void setResultText(String tag)
	{
		Message msg=new Message();
		Bundle b=new Bundle();

		//conversion de codebarre
		//dbKD402Colis colisconvert=new dbKD402Colis(GetDB());
		//String tag_conv = colisconvert.ConvertCBifNeeded(tag);
		String tag_conv = tag ;

		b.putString("cb", tag_conv);
		b.putString("cborg", tag);		//Pour l'acran dispatch, on veut afficher le CB scanné
		msg.setData(b);
		Global.hRead.dispatchMessage(msg);
		
		Log.i("TAG", "Tag "+tag+" added!");
//		mBeepManager.playBeepSoundAndVibrate();
	}
	void setSuccessFailText(String text)
	{
		Log.d("TAG",text);
	}
	//pour robot
	public void Scan()
	{
		if ( mScanManager != null ) {
			mScanManager.stopDecode();
			isScaning = true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mScanManager.startDecode();
		}
    }
	public void StopScan()
	{
		if ( mScanManager != null )
			mScanManager.stopDecode();
    }

	public void InitListenerCB (EditText etCB )
	{
		//Ne fait rien, seulement pour mode wedge
	}

	/*public void RemapScanKey() {

		KeyEvent currentKeyEvent = new KeyEvent(0,0,KeyEvent.ACTION_UP,KeyEvent.KEYCODE_F8,0,0,0,193,8);

		KeyMapManager mKeyMap = new KeyMapManager(getApplicationContext());
		mKeyMap.disableInterception(true);
		if (!mKeyMap.hasKeyEntry(193) ) {
			mKeyMap.mapKeyEntry(currentKeyEvent, KeyMapManager.KEY_TYPE_KEYCODE,
					Integer.toString(KeyEvent.KEYCODE_F9));
		}


	}*/
}
