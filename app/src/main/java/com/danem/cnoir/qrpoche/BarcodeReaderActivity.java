package com.danem.cnoir.qrpoche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Window;
import android.view.WindowManager;

import com.danem.cnoir.qrpoche.R;
import com.google.android.gms.vision.barcode.Barcode;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class BarcodeReaderActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);
        setTitle("Scannez un article");
    }

    @Override
    public void onScanned(Barcode barcode) {
        // single barcode scanned
        Intent intent = new Intent();
        intent.putExtra("cb", barcode.rawValue.toString());
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {
        // multiple barcodes scanned
        //Pas de gestion de multi scan
        Intent intent = new Intent();
        intent.putExtra("cb", list.get(0).rawValue.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // barcode scanned from bitmap image
    }

    @Override
    public void onScanError(String s) {
        // scan error
    }

    @Override
    public void onCameraPermissionDenied() {
        // camera permission denied
    }
}