package com.hk.payment_gateway;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCProductInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCShipmentInfoInitializer;
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener;

import java.util.UUID;

public class TransactionActivity extends AppCompatActivity implements SSLCTransactionResponseListener {
    private ConstraintLayout successL;
    private LinearLayout progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        successL = findViewById(R.id.successView);
        progressView = findViewById(R.id.progressView);

        //init gateway
        final SSLCommerzInitialization sslCommerzInitialization =
                new SSLCommerzInitialization(
                        "orbit6181713615cfd",
                        "orbit6181713615cfd@ssl",
                        700,
                        SSLCCurrencyType.BDT,
                        generateString(),
                        "yourProductType",
                        SSLCSdkType.TESTBOX
                );

        final SSLCCustomerInfoInitializer customerInfoInitializer = new
                SSLCCustomerInfoInitializer(
                "customer name",
                "customer email",
                "address",
                "dhaka",
                "1219",
                "Bangladesh",
                "phoneNumber"
        );
        SSLCProductInitializer.ProductProfile.TravelVertical travelInfo =
                new SSLCProductInitializer.ProductProfile.TravelVertical(
                        "Travel",
                        "10",
                        "A",
                        "12",
                        "Dhk-Syl"
                );
        final SSLCProductInitializer productInitializer =
                new SSLCProductInitializer(
                        "food",
                        "food",
                        travelInfo
                );

        SSLCShipmentInfoInitializer.ShipmentDetails shipmentDetails =
                new SSLCShipmentInfoInitializer.ShipmentDetails(
                        "AA",
                        "Address 1",
                        "Dhaka",
                        "1000",
                        "BD"
                );
        final SSLCShipmentInfoInitializer shipmentInfoInitializer =
                new SSLCShipmentInfoInitializer(
                        "Courier",
                        2,
                        shipmentDetails);

//payment action method
        IntegrateSSLCommerz
                .getInstance(TransactionActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .addCustomerInfoInitializer(customerInfoInitializer)
                .addProductInitializer(productInitializer)
                .buildApiCall(this);
    }

    @Override
    public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {
        progressView.setVisibility(View.GONE);
        successL.setVisibility(View.VISIBLE);
    }

    @Override
    public void transactionFail(String s) {
        Toast.makeText(TransactionActivity.this, "" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void merchantValidationError(String s) {
        Toast.makeText(TransactionActivity.this, "" + s, Toast.LENGTH_SHORT).show();
    }

    //generate random string
    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        Log.d("Transaction", "generateString: " + uuid);
        return uuid.replaceAll("-", "");
    }
}