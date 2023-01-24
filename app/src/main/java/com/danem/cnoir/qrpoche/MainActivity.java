package com.danem.cnoir.qrpoche;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import commun.Fonctions;
import commun.Global;
import swipebutton.SwipeButton;
import swipebutton.SwipeButtonCustomItems;

import static com.google.zxing.BarcodeFormat.QR_CODE;

public class MainActivity extends AbstractBarcodeActivity  {

    public final int MODE_CDE = 1 ;     //mode commande, on demande une qte uniquement
    public final int MODE_REL = 2 ;     //mode releve, saisie prix et facing
    static public String[] stUniqueIdByMode= new String[5] ;        //secu, il n'y a que 2 mode pour l'instant

    public final int ASK_CDE = 1 ;
    public final int ASK_FACING = 2 ;
    public final int ASK_PRICE = 3 ;

    public final String SEP_FIELD =";" ;
    public final String SEP_LINES ="#" ;
    public final String SEP_SAVEFIELD =";" ;
    public final String SEP_SAVELINES ="!" ;


    public StringBuilder stringListForQrCode = null ;
    public int iNbrCB = 0 ;
    public int iMaxLenght=1700 ;        //nbr carac max possible dans un QRCODE
    //a noter: valeur max du qr code est d'environ 3000, mais a plus de 2000 on commence à avoir des problème (soit d'affichage ou soit depuis la camera du lecteur)
    public ImageView ivQR = null ;
    public TextView tvInfo = null ;
    public SwipeButton mSwipeButtonRaz = null ;
    public SwipeButton mSwipeButtonChangeMode = null ;
    public int iCurrentMode =  MODE_CDE ;
    public int iDimQrCode = 0 ;

    //Pour garder en mémoire les saisie, et eventuellement les modifier
    static public ArrayList<String> GLOBAL_star_CB;
    static public ArrayList<String> GLOBAL_star_VALUES;
    static public ArrayList<Integer> GLOBAL_star_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGui() ;
        initVars() ;
        setListeners(false) ;
        //Test de génération d'un QRCODE trés long
        /*for ( int iCpt = 0 ; iCpt < 136 ; iCpt ++ )
        {
            String cb = "1542358"+4876+iCpt +"51" ;
            int random = new Random().nextInt((100 - 1) + 1) + 1;

            RecordValuesAndUpdateQRString(cb, random+"", -1) ;

        }
        displayQR();*/

        if ( LoadData() == true )
            displayQR();

        UpdateInfos() ;
    }
    protected void initGui() {
        setContentView(R.layout.activity_main);
        stringListForQrCode = new StringBuilder("") ;

        //Recuperation de la petite dimension d'écran (le qrcode generer est carré, on fait en sorte que ça prenne toute la largeur/hauteur dispo)
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if ( size.x > size.y)
            iDimQrCode = size.y ;
        else
            iDimQrCode = size.x ;
        ivQR = ((ImageView) findViewById(R.id.ivQR)) ;
        //Poissitionnement de la hauteur du QRcode en fonction de la largeur de l'écran
        ivQR.getLayoutParams().height = (iDimQrCode);
        tvInfo = ((TextView) findViewById(R.id.tvInfo)) ;

        mSwipeButtonRaz = (SwipeButton) findViewById(R.id.my_swipe_button_raz);
        mSwipeButtonChangeMode = (SwipeButton) findViewById(R.id.my_swipe_button_change_mode);

    }
    protected void initVars()
    {
        GLOBAL_star_CB   = new ArrayList<String>();
        GLOBAL_star_VALUES   = new ArrayList<String>();
        GLOBAL_star_MODE   = new ArrayList<Integer>();

        stUniqueIdByMode[MODE_CDE] = UUID.randomUUID().toString() ;
        stUniqueIdByMode[MODE_REL] = UUID.randomUUID().toString() ;


    }

    public void UpdateInfos()
    {
        if (iNbrCB== 0  )
        {
            mSwipeButtonRaz.setVisibility(View.INVISIBLE);
            if ( bIntegratedScannerPresent == true )
                ivQR.setVisibility(View.INVISIBLE);
            else
            {
                ivQR.setVisibility(View.VISIBLE);
                ivQR.setImageResource(R.drawable.scan);
            }

        }
        else
        {
            ivQR.setVisibility(View.VISIBLE);
            mSwipeButtonRaz.setVisibility(View.VISIBLE);

        }
        tvInfo.setText("Nombre de code barre enregistrés: "+iNbrCB);

    }

    public void setListeners( boolean bUpdate) {
        if ( bUpdate == false  )
        {
            Global.hRead = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {

                    String cb = msg.getData().getString("cb");
                    CbTraitement(cb);
                }
            };

            //
            SwipeButtonCustomItems swipeButtonSettingsRaz = new SwipeButtonCustomItems()
            {
                @Override
                public void onSwipeConfirm()
                {
                    //Reset pour le mode courant, parcours inverse des tableaux
                    for ( int i=  GLOBAL_star_CB.size()-1 ; i >=0 ; i--)
                    {
                        if (GLOBAL_star_MODE.get(i) == iCurrentMode)
                        {
                            GLOBAL_star_MODE.remove(i);
                            GLOBAL_star_CB.remove(i);
                            GLOBAL_star_VALUES.remove(i);
                        }
                    }
                    displayQR() ;
                }
            };

            //rouge et degradé de rouge
            swipeButtonSettingsRaz
                    .setGradientColor1(0xFFff424a)
                    .setGradientColor2(0xFFff7c82)
                    .setGradientColor2Width(60)
                    .setGradientColor3(0xFFfcb5b8)
                    .setPostConfirmationColor(0xFFfcb5b8)
                    .setActionConfirmDistanceFraction(0.7)
                    .setButtonPressText("Balayez sur la droite pour repartir à zéro")
                    .setActionConfirmText("RAZ");

            if (mSwipeButtonRaz != null)
            {
                mSwipeButtonRaz.setSwipeButtonCustomItems(swipeButtonSettingsRaz);
            }
            ivQR.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    if (bIntegratedScannerPresent == true)
                        return;
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    } else
                    {
                        //mode zxing
                        launchActivityForResult(MainActivity.this, BarcodeReaderActivity.class, null, LAUNCH_SCAN);
                        ;
                    }


                }
            });
        }

        SwipeButtonCustomItems swipeButtonSettingsChangeMode= new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
                if ( iCurrentMode == MODE_CDE )
                {
                    iCurrentMode = MODE_REL ;
                    displayQR() ;
                    setListeners(true);
                    mSwipeButtonChangeMode.setBackgroundColor(0xFF42F453);
                }
                else if  ( iCurrentMode == MODE_REL )
                {
                    iCurrentMode = MODE_CDE ;
                    displayQR() ;
                    setListeners(true);
                    mSwipeButtonChangeMode.setBackgroundColor(0xFFabc5fc);

                }

            }
        };

        //bleu et degradé de bleu
        swipeButtonSettingsChangeMode
                .setGradientColor1(0xFF4e85fc)
                .setGradientColor2(0xFF84abff)
                .setGradientColor3(0xFFabc5fc )
                .setPostConfirmationColor(0xFFabc5fc)
                .setActionConfirmDistanceFraction(0.7)
                .setGradientColor2Width(60)
                .setButtonPressText("Mode COMMANDE, balayez sur la droite pour changer")
                .setActionConfirmText("Mode COMMANDE");

        if ( iCurrentMode ==  MODE_REL)
        {
            //vert et degradé de vert
            swipeButtonSettingsChangeMode
                    .setGradientColor1(0xFF12ff02)
                    .setGradientColor2(0xFF6dff63)
                    .setGradientColor3(0xFFa4ff9e)
                    .setPostConfirmationColor(0xFFa4ff9e)
                    .setButtonPressText("Mode RELEVE, balayez sur la droite pour changer")
                    .setActionConfirmText("Mode RELEVE");


        }
        if (mSwipeButtonChangeMode != null) {
            mSwipeButtonChangeMode.setSwipeButtonCustomItems(swipeButtonSettingsChangeMode);
        }
        //Pour l'instant, desactivation du mode relevé
        mSwipeButtonChangeMode.setVisibility(View.GONE);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK)
        {
            if (requestCode==LAUNCH_SCAN)
            {
                String cb = data.getStringExtra("cb");
                CbTraitement(cb) ;

            }

        }

    }

    void CbTraitement(String cb)
    {
        if ( cb == null )
            return ;
        if ( cb.length() <= 0)
            return ;


        //Check si CB deja saisie
        int iIdxExistingValue= -1 ;
        for ( int i= 0 ; i < GLOBAL_star_CB.size() ; i++)
        {
            if (GLOBAL_star_MODE.get(i) == iCurrentMode)
            {
                if ( GLOBAL_star_CB.get(i).equals(cb) )
                {
                    iIdxExistingValue = i ;
                }
            }
        }

        //control len max
        if ( cb.length()+10+stringListForQrCode.length() > iMaxLenght)      //+10 pour les values
        {
            Fonctions.playSound(MainActivity.this, R.raw.beep_nok);
            Fonctions._showAlert("Nombre de code barre max atteint, merci de transferer vos données et repartir à zéro pour continuer.\n\nA noter: vous pouvez sauvegarder la saisie actuelle en faisant une copie d'écran (bouton volume - et power en même temps) avant de faire un RAZ.", this);
            return ;
        }
        askValues(cb,iIdxExistingValue) ;
    }


    static Dialog mDialog = null ;
    static String valueToRecord ;
    static int iCurrentValueAsked ;
    public void askValues(final String cb, final int iIdxExistingValue){

        //Secu, pour ne pas lancer 2 fois la dialog
        if ( mDialog != null )
            return ;
        //if ( isActivityVisible() == false )		//Pour eviter qu'un activity masquée declenche le blocage
        //    return ;
        pauseCB() ;
        iCurrentValueAsked = -1 ;
        valueToRecord = "" ;

        mDialog = new Dialog(MainActivity.this);
        mDialog.setContentView(R.layout.askqte);
        mDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = (int)(iDimQrCode*0.9);
        final TextView tv_input =  mDialog.findViewById(R.id.tv_input);
        final TextView tv_inputinfo =  mDialog.findViewById(R.id.tv_inputinfo);
        final TextView tv_avertissement =  mDialog.findViewById(R.id.tv_avertissement);
        final TextView tv_title =  mDialog.findViewById(R.id.tv_title);


        if ( iCurrentMode == MODE_CDE)
        {
            tv_title.setText("Quantité pour " + cb);
            iCurrentValueAsked = ASK_CDE ;
            tv_inputinfo.setText("Quantité :");
        }
        else
        {
            //mode relevé, demande de facing en premier, puis demande de prix
            tv_title.setText("Facing pour "+cb);
            iCurrentValueAsked = ASK_FACING ;
            tv_inputinfo.setText("Facing :");
        }

        if ( iIdxExistingValue == -1 )
        {
            tv_avertissement.setVisibility(View.GONE);
        }
        else
        {
            Fonctions.playSound(MainActivity.this, R.raw.beep_nok);
        }
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);  //pour empecher l'utilisation du bouton back


        final String[] stPin = {""};
        final Button mB[] = new Button[10];

        mB[0] = (Button) mDialog.findViewById(R.id.x0);
        mB[1] = (Button) mDialog.findViewById(R.id.x1);
        mB[2] = (Button) mDialog.findViewById(R.id.x2);
        mB[3] = (Button) mDialog.findViewById(R.id.x3);
        mB[4] = (Button) mDialog.findViewById(R.id.x4);
        mB[5] = (Button) mDialog.findViewById(R.id.x5);
        mB[6] = (Button) mDialog.findViewById(R.id.x6);
        mB[7] = (Button) mDialog.findViewById(R.id.x7);
        mB[8] = (Button) mDialog.findViewById(R.id.x8);
        mB[9] = (Button) mDialog.findViewById(R.id.x9);

        ImageButton mBack = (ImageButton) mDialog.findViewById(R.id.xBackNum);
        ImageButton m_Valide = (ImageButton) mDialog.findViewById(R.id.xValide);


        for (int iCpt = 0 ; iCpt < 10 ; iCpt ++) {
            final int finalICpt = iCpt;
            mB[iCpt].setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            stPin[0] += mB[finalICpt].getText() ;
                            String dispayvalue = stPin[0] ;
                            if ( iCurrentValueAsked == ASK_PRICE )
                            {
                                //formatage, pour ajout automatique de decimal
                                dispayvalue = FormatPrice(dispayvalue ) ;
                            }

                            tv_input.setText(dispayvalue);

                        }

                    }
            );
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_input.setText("");
                stPin[0] = "";
            }
        });

        m_Valide.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //if (tv_password.getText().equals(stCode))
                if ( !tv_input.getText().equals("") )
                {
                    //debug, pour tester rapidement la limite de caracs
                    //int iDebugMax = 1;
                    //if (iNbrCB == 0  ) iDebugMax = 180;
                    //for ( int i =0 ; i< iDebugMax ; i++ )
                    /*{
                        stringListForQrCode.append(cb + ";");
                        stringListForQrCode.append(tv_qte.getText() + ";");
                        iNbrCB++;
                    }*/
                    if (iCurrentValueAsked == ASK_FACING )
                    {
                        //On enregistre la veleur et on passe à la demande de prix, en laissant la dlg affichée
                        valueToRecord +=tv_input.getText().toString()+SEP_FIELD ;
                        tv_input.setText("");
                        stPin[0] = "";
                        iCurrentValueAsked = ASK_PRICE ;
                        tv_title.setText("Prix pour " + cb);
                        tv_inputinfo.setText("Prix :");
                        return ;
                    }
                    valueToRecord += tv_input.getText().toString() ;
                    RecordValuesAndUpdateQRString(cb,valueToRecord, iIdxExistingValue) ;
                    displayQR() ;
                    isRunning = false;

                    resumeCB();

                    mDialog.dismiss();
                    mDialog = null ;

                } else {

                    Toast.makeText(MainActivity.this,"Vous devez saisir une quantité pour pouvoir valider.",Toast.LENGTH_SHORT).show();
                }
            }
        }));



        mDialog.show();
    }

    void RecordValuesAndUpdateQRString(String cb, String values, int iIdxExistingValue)
    {
        if ( iIdxExistingValue != -1 )
        {
            //suppression de l'ancienne valeur
            if ( iIdxExistingValue < GLOBAL_star_CB.size() )
            {
                GLOBAL_star_CB.remove(iIdxExistingValue) ;
                GLOBAL_star_VALUES.remove(iIdxExistingValue) ;
                GLOBAL_star_MODE.remove(iIdxExistingValue) ;
            }

        }
        //ajout de la valeur
        GLOBAL_star_CB.add(cb);
        GLOBAL_star_VALUES.add(values);
        GLOBAL_star_MODE.add(iCurrentMode);

    }

    void displayQR()
    {
        //Mise à jour de chaine pour le qrcode à afficher
        stringListForQrCode.setLength(0);
        iNbrCB = 0 ;

        for ( int i = 0 ; i < GLOBAL_star_CB.size() ; i++ )
        {
            if ( GLOBAL_star_MODE.get(i) == iCurrentMode )
            {
                stringListForQrCode.append(GLOBAL_star_CB.get(i) + SEP_FIELD);
                stringListForQrCode.append(GLOBAL_star_VALUES.get(i) + SEP_LINES);
                iNbrCB++;

                if (iNbrCB >=  (25-10)  )
                {
                    AfxMessageBoxSpe(("Mo"+"De de"+"Mo"+", "+(26-11)+" c"+"B "+"mA"+"x").toLowerCase(Locale.ROOT));
                    break;
                }
            }
        }
        if ( iNbrCB != 0 )
        {   //Ajout en premier de l'id unique de rel et du type de saisie
            stringListForQrCode.insert(0, stUniqueIdByMode[iCurrentMode] + SEP_FIELD + iCurrentMode + SEP_LINES);
        }
        SaveData() ;
        //FurtiveMessageBox(stringListForQrCode.toString());
        try {
            // Generate QR code
            final BitMatrix encoded = new QRCodeWriter().encode(
                    stringListForQrCode.toString(), QR_CODE, iDimQrCode, iDimQrCode);
            // Convert QR code to Bitmap
            int width = encoded.getWidth();
            int height = encoded.getHeight();
            int[] pixels = new int[width * height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    pixels[y * width + x] = encoded.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, width, 0, 0, width, height);
            UpdateInfos();
            ivQR.setImageBitmap(bmp);
        } catch (Exception e) {
            //FurtiveMessageBox("Erreur, vos dernières saisies n'ont pas été enregistrées");
            e.printStackTrace();
            UpdateInfos();
        }
    }
    String FormatPrice(String price)
    {
        if ( price.length() == 0  )
            return  "0.00" ;
        if ( price.length() == 1 )
            return "0.0"+price ;
        if ( price.length() == 2 )
            return "0."+price ;
        int iPosdecimal = price.length()-2 ;
        String priceret = price.substring(0, iPosdecimal) +"."+price.substring( iPosdecimal) ;

        return priceret ;
    }

    //fonctions de sauvegarde/recuperation de données
    final String savefile = "QRPocheCurrent.dat" ;

    public void SaveData()
    {
        StringBuilder datas = new StringBuilder() ;
        for ( int i = 0 ; i< GLOBAL_star_CB.size() ; i++ )
        {
            String line = GLOBAL_star_CB.get(i)+SEP_SAVEFIELD ;
            line += GLOBAL_star_VALUES.get(i)+SEP_SAVEFIELD ;
            line += GLOBAL_star_MODE.get(i)+SEP_SAVEFIELD ;
            line += stUniqueIdByMode[GLOBAL_star_MODE.get(i)]+SEP_SAVELINES ;
            datas.append(line);
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(savefile, Context.MODE_PRIVATE));
            outputStreamWriter.write(datas.toString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            FurtiveMessageBox("Erreur sauvegarde: " + e.toString());
        }
    }
    public boolean LoadData()
    {
        String datas = "";

        try {
            InputStream inputStream = this.openFileInput(savefile);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                datas = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            return false ;        //fichier inexistant, aucun probléme
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            FurtiveMessageBox("Erreur sauvegarde: " + e.toString());
        }
        boolean bDataImported = false ;
        if ( !datas.equals("") )
        {
            String[] lines = datas.split(SEP_SAVELINES);
            if (lines.length > 0 )
            {
                for (int i=0 ; i< lines.length ; i++)
                {
                    String line = lines[i] ;
                    String[] fld = line.split(SEP_SAVEFIELD);
                    if ( fld.length>=4 )
                    {
                        if ( !fld[0].equals("") )
                        {
                            bDataImported = true;
                            GLOBAL_star_CB.add(fld[0]);
                            GLOBAL_star_VALUES.add(fld[1]);
                            GLOBAL_star_MODE.add(Fonctions.convertToInt(fld[2]));
                            stUniqueIdByMode[Fonctions.convertToInt(fld[2])] = fld[3];
                        }
                    }

                }
            }

        }
        if (  bDataImported == true)
        {
            Fonctions._showAlert("Attention, il y a eu une erreur lors de votre précedente utilisation de l'application.\n\nVos dernières saisies ont été rechargées.",this);
            return true ;
        }
        return false ;
    }
    public void AfxMessageBoxSpe( String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }
}