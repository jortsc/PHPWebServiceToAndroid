package com.innovacom.webservicestatus;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import services.SoapService;
import services.CustomerStatus;


public class CustomerStatusActivity extends Activity implements OnClickListener {

    private TextView customer;
    private TextView customerDoc;
    private TextView customerComment;
    private TextView customerStatus;
    private TextView customerPhone;
    private EditText input;

    private SoapService srv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_main);
        srv = new SoapService();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_status, menu);
        return true;
        //Toast.makeText(getBaseContext(),getResources().getString(R.string.label_resp), Toast.LENGTH_LONG).show();
        //getResources().getColor(R.color.ok)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.callWeb:
                input = (EditText) findViewById(R.id.strStastus);
                setContentView(R.layout.response);
                customerDoc = (TextView) findViewById(R.id.customerDoc);
                customer = (TextView) findViewById(R.id.customeName);
                customerComment = (TextView) findViewById(R.id.comentario);
                customerStatus = (TextView) findViewById(R.id.colorEstado);
                customerPhone = (TextView) findViewById(R.id.telefono);

                System.out.println(input.getText().toString()+" ***************************");

                CustomerStatus cs = srv.getStatus(input.getText().toString(), getResources().getString(R.string.secret));
                if(cs.getId() > 0){
                    customerDoc.setText(cs.getDocumento());
                    customer.setText(cs.getNombre());
                    customerComment.setText(cs.getComentario());
                    customerPhone.setText(""+cs.getTelefono());

                    int color = 98;
                    String st = "";
                    if (cs.getEstado().equals("ok")) {
                        color = R.color.ok;
                        st = "Todo Ok";
                    }else if(cs.getEstado().equals("en proceso")){
                        color = R.color.en_proceso;
                        st = "En proceso";
                    }else if(cs.getEstado().equals("pendiente orange")){
                        color = R.color.pendiente_orange;
                        st = "Pendiente Orange";
                    }else if(cs.getEstado().equals("pendiente distribuidor")){
                        color = R.color.pendiente_distri;
                        st = "Pendiente distribuidor";
                    }else if(cs.getEstado().equals("incidencia grave")){
                        color = R.color.incidencia_grave;
                        st = "Incidencia grave";
                    }else if(cs.getEstado().equals("baja")){
                        color = R.color.baja;
                        st = "Baja";
                    }else if(cs.getEstado().equals("sin visitar")){
                        color = R.color.sin_visitar;
                        st = "Sin visitar";
                    }
                    customerStatus.setBackgroundColor(getResources().getColor(color));
                    customerStatus.setText(st);
                }else{
                    customer.setText("No existe el cliente. . .");
                }
                break;
            default:
                Toast.makeText(getBaseContext(), "Defautl. . .", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
