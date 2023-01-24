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
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import commun.Fonctions;
import commun.Global;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.HashMap;
import java.util.Map;


//Tof, copie de barcodeActivityEA600 pour gestion cb unitech et ajout gestion CB honeywell
public class barcodeActivityMultiple extends  baseActivity  implements BarcodeReader.BarcodeListener,
		BarcodeReader.TriggerListener
{

	  private final static String SCAN_ACTION = ScanManager.ACTION_DECODE;//default action


	public boolean bIntegratedScannerPresent = false ;
	    private Vibrator mVibrator;
	    private ScanManager mScanManager = null ;		//unitech
		private static BarcodeReader barcodeReader = null;		//honeywell
		private static AidcManager manager = null;					//honeywell

		private static SoundPool soundpool = null;
	    private static int soundid;
	    private String barcodeStr;
	    private boolean isScaning = false;
		public boolean bWedge =false ;
		public boolean bBlockScan = true ;		//pour correction rapide des plantages, pas de fermeture du scan mais blocage avec bip d'erreur (en pas d'envoi de message)
		EditText etCB = null ; //pour comptatibilitéé avec version wedge
		String m_keyWedge="";//on memorise dedans les saisies wedge barcode

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
			Log.e("exception","openScanner=>"+e.getLocalizedMessage());
			HashMap<String, String> v_Params = new HashMap<>();
			v_Params.put("p_Fonction","barcodeActivityEA600.open");
			mScanManager = null ;		//pour detection non utilisation du scan integré
		}

		try
		{
			//code honeywell

			if (barcodeReader != null) {

				// register bar code event listener
				barcodeReader.addBarcodeListener(this);

				// set the trigger mode to client control
				try {
					barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
							BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
				} catch (UnsupportedPropertyException e) {
					Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
				}
				// register trigger state change listener
				barcodeReader.addTriggerListener(this);

				Map<String, Object> properties = new HashMap<String, Object>();
				// Set Symbologies On/Off
				properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
				properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
				properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, true);
				properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
				// Set Max Code 39 barcode length
				properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
				// Turn on center decoding
				properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
				// Enable bad read response
				properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
				// Apply the settings
				barcodeReader.setProperties(properties);
			}

		}
		catch(Exception e )
		{
		}

		bBlockScan = false ;
	}

	void close()
	{
		try {


			if (mScanManager != null) {
				mScanManager.stopDecode();
				isScaning = false;
			}
		}
		catch(Exception ex)
		{

		}
		//code honeywell
		try {
			if (barcodeReader != null) {
				// close BarcodeReader to clean up resources.
				barcodeReader.close();
				barcodeReader = null;
			}

			if (manager != null) {
				// close AidcManager to disconnect from the scanner service.
				// once closed, the object can no longer be used.
				manager.close();
			}
		}
		catch(Exception ex)
		{

		}


	}

	//Fonction a appelé dans plash ou app pour que l'initialisation soit fait avant l'appel à une activity utilisant le scan
	public  void initCB(Context c)
	{
		if ( manager == null )
		{
			AidcManager.create(c, new AidcManager.CreatedCallback()
			{

				@Override
				public void onCreated(AidcManager aidcManager)
				{
					manager = aidcManager;
					try
					{
						if ( barcodeReader == null )
							barcodeReader = manager.createBarcodeReader();
					} catch (InvalidScannerNameException e)
					{
						//Toast.makeText(c, "Invalid Scanner Name Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
					} catch (Exception e)
					{
						//Toast.makeText(c, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// result
		super.onCreate(savedInstanceState);

        try {


            open();
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (mScanManager != null) {
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
        catch(Exception ex)
        {

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
						Fonctions.playSound(barcodeActivityMultiple.this , R.raw.beep_nok );
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
					barcodeStr = CleanCodebarre(barcodeStr );		//Pour amrmée de terre, clean du code barre
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
		if ( mScanManager != null )		//Si on n'a pas de codebarre integré, on ne block pas sur changement d'activity
			resumeCB();
		open();
		super.onResume();
	}

	@Override
	public void onPause() {
		isScaning = false ;
		if ( mScanManager != null )		//Si on n'a pas de codebarre integré, on ne block pas sur changement d'activity
			pauseCB();
		close();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		//pauseCB();
		super.onDestroy();
        try {


            unregisterReceiver(mScanReceiver);
        }
        catch (Exception ex)
        {

        }
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

	//on va récupérer les saisies du codeabar WEDGE
	/*@Override
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		if ( e.getAction() == KeyEvent.ACTION_DOWN )
		{
			if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER)
			{
				Toast.makeText(getApplicationContext(),
					m_keyWedge,
					Toast.LENGTH_SHORT).show();

				//Update(true, m_keyWedge);//pas d'ajout de qte depuis la liste
				m_keyWedge="";
				return true;
			}
			else
			{
				/*Toast.makeText(getApplicationContext(),
						m_keyWedge,
				Toast.LENGTH_SHORT).show();
				char unicodeChar = (char)e.getUnicodeChar();
				Log.e("unicodeChar","scancode=>"+e.getScanCode());
				Log.e("unicodeChar","unicodeChar=>"+unicodeChar);
				Log.e("unicodeChar","charachtermap=>"+e.getKeyCharacterMap().toString());
				Log.e("unicodeChar","characther=>"+e.getCharacters());
				Log.e("unicodeChar","getnumber=>"+e.getNumber());
				Log.e("unicodeChar","getdisplaylabel=>"+e.getDisplayLabel());
				if ( unicodeChar  >= '0' && unicodeChar <= 'z' ) //pour gestion caractère spéciaux de la douchette granit
					m_keyWedge+=unicodeChar;

				Toast.makeText(getApplicationContext(),
						m_keyWedge,
						Toast.LENGTH_SHORT).show();
			}
		}
		return super.dispatchKeyEvent(e);
	};*/



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
		b.putString("cb", tag);
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
        try {


            if (mScanManager != null) {
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
        catch(Exception ex)
        {

        }
    }
	public void StopScan()
	{
        try {


            if (mScanManager != null)
                mScanManager.stopDecode();
        }
        catch (Exception ex)
        {

        }
    }

	public void InitListenerCB (EditText etCB )
	{
		//Ne fait rien, seulement pour mode wedge
	}

	//Ci dessous event HONEYWELL
	@Override
	public void onBarcodeEvent(final BarcodeReadEvent event) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// update UI to reflect the data
				/*List<String> list = new ArrayList<String>();
				list.add("Barcode data: " + event.getBarcodeData());
				list.add("Character Set: " + event.getCharset());
				list.add("Code ID: " + event.getCodeId());
				list.add("AIM ID: " + event.getAimId());
				list.add("Timestamp: " + event.getTimestamp());

				final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						AutomaticBarcodeActivity.this, android.R.layout.simple_list_item_1, list);

				barcodeList.setAdapter(dataAdapter);*/

				try {
					if ( isScaning == false || isActivityVisible() == false ) {
						//VideBuffer(intent);
						return;
					}

					if ( bBlockScan == true )
					{
						//blocage avec son d'erreur
						Fonctions.playSound(barcodeActivityMultiple.this , R.raw.beep_nok );
						//VideBuffer(intent);
						return;

					}
					// TODO Auto-generated method stub
					//isScaning = false;
					if ( GL_lSecuEntreScans < SystemClock.uptimeMillis())
						GL_lSecuEntreScans = SystemClock.uptimeMillis()+ 700  ;
					else
					{
						//VideBuffer(intent) ;
						return ;
					}
					if ( soundpool  != null )
						soundpool.play(soundid, 1, 1, 0, 0, 1);

					//byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
					//int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
					//byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
					//Log.i("debug", "----codetype--" + temp);
					barcodeStr = event.getBarcodeData() ;
					barcodeStr = CleanCodebarre(barcodeStr );		//Pour amrmée de terre, clean du code barre
					if(barcodeStr!=null)
						setResultText(new String(barcodeStr));
				} catch (Exception e) {
					Log.e("TAG",e.getLocalizedMessage());
				}

			}
		});
	}
	@Override
	public void onTriggerEvent(TriggerStateChangeEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFailureEvent(BarcodeFailureEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		Log.e("TAG","onKeymultiple"+ event.getCharacters());
		try {
			if ( isScaning == false || isActivityVisible() == false ) {
				//VideBuffer(intent);
				return false;
			}

			if ( bBlockScan == true )
			{
				//blocage avec son d'erreur
				Fonctions.playSound(barcodeActivityMultiple.this , R.raw.beep_nok );
				//VideBuffer(intent);
				return false;
			}
			// TODO Auto-generated method stub
			//isScaning = false;
			if ( GL_lSecuEntreScans < SystemClock.uptimeMillis())
				GL_lSecuEntreScans = SystemClock.uptimeMillis()+ 700  ;
			else
			{
				//VideBuffer(intent) ;
				return false;
			}
			if ( soundpool  != null )
				soundpool.play(soundid, 1, 1, 0, 0, 1);

			//byte[] barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG);
			//int barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0);
			//byte temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, (byte) 0);
			//Log.i("debug", "----codetype--" + temp);
			barcodeStr = event.getCharacters() ;
			barcodeStr = CleanCodebarre(barcodeStr );		//Pour amrmée de terre, clean du code barre
			if(barcodeStr!=null)
				setResultText(new String(barcodeStr));
		} catch (Exception e) {
			Log.e("TAG",e.getLocalizedMessage());
		}
		return true;
	}
}
