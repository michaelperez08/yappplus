package com.yapp.mrrabbit.yapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Escanear.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Escanear#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Escanear extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button sincronizar, bt_todos_tipos_escanear;
    private Evento evento;
    private TextView tv_tiquets_vendidos, tv_tiquets_escaneados, tv_actualizado;
    private ProgressDialog progress;
    private IntentIntegrator qrScan;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Escanear() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Escanear.
     */
    // TODO: Rename and change types and number of parameters
    public static Escanear newInstance(String param1, String param2) {
        Escanear fragment = new Escanear();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_escanear, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.fe_bt_sincronizar:
                getTiquetesDisponiblesEvento();
                sincronizar.setBackgroundResource(R.drawable.raduis_button_green);
                sincronizar.setText("Actualizado");
                break;
        }

    }

    private void getTiquetesDisponiblesEvento() {
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DataAccess da = new DataAccess();
                evento.setTiquetesEvento(da.getTiquetesEvento(evento.getIdEvento()));
                dismissLoading();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        evento = MainActivity.evento;
        cargarElementosVisuales();
        cargarInfomacionEvento();
        cargarBotenesTiquetes();
    }

    private void cargarElementosVisuales() {
        sincronizar = (Button) getActivity().findViewById(R.id.fe_bt_sincronizar);
        bt_todos_tipos_escanear = (Button) getActivity().findViewById(R.id.bt_todos_tipos_escanear);
        tv_tiquets_vendidos = (TextView) getActivity().findViewById(R.id.tv_tiquetes_vendidos_escanear);
        tv_tiquets_escaneados = (TextView) getActivity().findViewById(R.id.tv_tiquetes_escaneados_escanear);
        tv_actualizado = (TextView) getActivity().findViewById(R.id.tv_actualizado_escanear);

        sincronizar.setOnClickListener(this);
        bt_todos_tipos_escanear.setOnClickListener(abrirEscanner());
    }

    public void cargarInfomacionEvento(){
        if(evento!=null){
            tv_tiquets_escaneados.setText(String.valueOf(evento.getTotalTiquetesEscaneados()));
            tv_tiquets_vendidos.setText(String.valueOf(evento.getTotalTiquetesVendidos()));
            tv_actualizado.setText("Actualizado: "+DataAccess.getCurrentTime()+" - "+DataAccess.getCurrentDate("dd/MM/yyyy"));
        }
    }

    public void cargarBotenesTiquetes(){
        if(evento!=null) {
            ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.cl_fragment_escanear);
            ConstraintSet set = new ConstraintSet();
            int id_boton_anterior = R.id.bt_todos_tipos_escanear;

            for (int i = 0; i < evento.getTipoTiquetes().size(); i++) {
                Button botonEscanear = new Button(getActivity());
                botonEscanear.setId(i + 1);
                botonEscanear.setLayoutParams(new ConstraintLayout.LayoutParams(400, 70));
                botonEscanear.setBackgroundResource(R.drawable.radius_button);
                botonEscanear.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_qr_code, 0, 0, 0);
                botonEscanear.setCompoundDrawablePadding(-30);
                botonEscanear.setGravity(Gravity.CENTER);
                botonEscanear.setText(evento.getTipoTiquetes().get(i).getNombre());
                botonEscanear.setTextColor(Color.WHITE);
                botonEscanear.setTextSize(12);

                botonEscanear.setOnClickListener(abrirEscanner());

                layout.addView(botonEscanear, 0);
                set.clone(layout);
                set.connect(botonEscanear.getId(), ConstraintSet.TOP, id_boton_anterior, ConstraintSet.BOTTOM, 14);
                set.connect(botonEscanear.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 0);
                set.connect(botonEscanear.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 0);

                if((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) && (i == evento.getTipoTiquetes().size()-1)){
                    //set.connect(botonEscanear.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM, 40);
                    set.connect(layout.getId(), ConstraintSet.BOTTOM, botonEscanear.getId(), ConstraintSet.BOTTOM, 80);
                }

                set.applyTo(layout);
                id_boton_anterior = botonEscanear.getId();
            }
        }
    }

    public View.OnClickListener abrirEscanner(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan = IntentIntegrator.forSupportFragment(Escanear.this);
                qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                qrScan.setPrompt("Escane el codigo QR");
                qrScan.setCameraId(0);
                qrScan.setBeepEnabled(false);
                qrScan.setBarcodeImageEnabled(false);
                qrScan.initiateScan();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            Toast.makeText(getActivity(), "Escaneado: "+result.getContents(), Toast.LENGTH_LONG).show();
        }

    }

    public void displayDialogLoading(){
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void dismissLoading(){
        if(progress!=null) {
            progress.dismiss();
        }
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
