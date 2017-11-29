package com.yapp.mrrabbit.yapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.util.ArrayList;


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
    private Button bt_sincronizar, bt_todos_tipos_escanear;
    private Evento evento;
    private TextView tv_tiquets_vendidos, tv_tiquets_escaneados, tv_actualizado;
    private ProgressDialog progress;
    private IntentIntegrator qrScan;
    private ArrayList<Button> botonesEscanear;
    private ArrayList<Tiquete> tiquetesBDLocal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String escanerSeleccionado;

    private DBTiquetesDisponibles dbtd;
    private int tiquetesEscaneados;

    private View dialogView;
    private AlertDialog dialog;
    private Button btSalirDialogResultScann;
    private Button btContinuarDialogResultScann;
    private TextView tvMensajePrincipalDialogResultScann;
    private TextView tvMensajeSecundarioDialogResultScann;
    private TextView tvFechaHoraDialogResultScann;
    private TextView tvTipoEntradaDialogResultScann;
    private TextView tvNombreDialogResultScann;
    private TextView tvCedulaDialogResultScann;
    private View viewDialogResultScann;
    private ImageView ivIcDialogResultScann;

    private Context contextoEscaner;

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
                break;
        }
    }


    public void habilitarEscaneo(){
        bt_sincronizar.setBackgroundResource(R.drawable.raduis_button_green);
        bt_sincronizar.setText("Actualizar");
    }

    private void getTiquetesDisponiblesEvento() {
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                cargarCambiosAlServidor();
                DataAccess da = new DataAccess();
                evento.setTipoTiquetes(da.getTipoTiquetesEvento(evento.getIdEvento()));
                evento.setTiquetesEvento(da.getTiquetesEvento(evento.getIdEvento()));
                if(evento.getTiquetesEvento()!=null && !evento.getTiquetesEvento().isEmpty()) {
                    guardarTiquetesDisponiblesBD();
                    showToastFromOtherThread("Sincronización exitosa! Yapp puede escanear!");
                    habilitarEscaneoFromOtherThread();
                }else{
                    if(bt_sincronizar.getText().equals("Actualizar y Sincronizar")){
                        showToastFromOtherThread("Este evento aun no tiene tiquetes");
                    }else {
                        showToastFromOtherThread("Ups algo salio mal, intente de nuevo mas tarde");
                    }
                }
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
        contextoEscaner = getActivity().getApplicationContext();
        checkEvento();
        cargarElementosVisuales();
        cargarInfomacionEvento();
        cargarBotenesTiquetes();
        cargarTiquetesFromDB();
        cargarVendidosYEscaneados();
    }

    private void cargarElementosVisuales() {
        bt_sincronizar = (Button) getActivity().findViewById(R.id.fe_bt_sincronizar);
        bt_todos_tipos_escanear = (Button) getActivity().findViewById(R.id.bt_todos_tipos_escanear);
        tv_tiquets_vendidos = (TextView) getActivity().findViewById(R.id.tv_tiquetes_vendidos_escanear);
        tv_tiquets_escaneados = (TextView) getActivity().findViewById(R.id.tv_tiquetes_escaneados_escanear);
        tv_actualizado = (TextView) getActivity().findViewById(R.id.tv_actualizado_escanear);

        bt_sincronizar.setOnClickListener(this);
        bt_todos_tipos_escanear.setOnClickListener(abrirEscanner(false));
    }

    public void cargarInfomacionEvento(){
        if(evento!=null){
            tv_tiquets_escaneados.setText(String.valueOf(evento.getTotalTiquetesEscaneados()));
            tv_tiquets_vendidos.setText(String.valueOf(evento.getTotalTiquetesVendidos()));
            tv_actualizado.setText("Actualizado: "+DataAccess.getCurrentTime()+" - "+DataAccess.getCurrentDate("dd/MM/yyyy"));
        }
    }

    public void cargarCambiosAlServidor(){
        int cantidadTiquetesActualizar = 0;
        int cantidadTiquetesActualizados = 0;
        if(tiquetesBDLocal!=null) {
            for (Tiquete tiqueteTemp : tiquetesBDLocal) {
                if (tiqueteTemp.isCanjeada() && !tiqueteTemp.isSincronizada()) {
                    DataAccess da = new DataAccess();
                    cantidadTiquetesActualizar++;
                    if(da.subirTiuetesEscaneadosAlServidor(tiqueteTemp.getCodigoQR(), tiqueteTemp.getFechaEscaneo())){
                        cantidadTiquetesActualizados++;
                        dbtd.setSincronizado(tiqueteTemp.getIdTiquete());
                    }
                }
            }
            if(cantidadTiquetesActualizar==cantidadTiquetesActualizados && cantidadTiquetesActualizados>0){
                showToastFromOtherThread("Todos los tiquetes canjeados se han subido al servidir");
            }else if(cantidadTiquetesActualizar>cantidadTiquetesActualizados && cantidadTiquetesActualizados>0){
                showToastFromOtherThread("Upss! Algunos tiquetes canjeados no se han subido al servidir, intente de nuevo mas tarde");
            }else if(cantidadTiquetesActualizar>cantidadTiquetesActualizados && cantidadTiquetesActualizados==0){
                showToastFromOtherThread("Upss hubo un problema! Los tiquetes canjeados no se han subido al servidir, intente de nuevo mas tarde");
            }
        }
    }

    public void cargarBotenesTiquetes(){
        if(evento!=null) {
            ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.cl_fragment_escanear);
            ConstraintSet set = new ConstraintSet();
            int id_boton_anterior = R.id.bt_todos_tipos_escanear;
            botonesEscanear = new ArrayList<>();
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

                botonEscanear.setOnClickListener(abrirEscanner(false));

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
                botonesEscanear.add(botonEscanear);
            }
        }
    }

    public View.OnClickListener abrirEscanner(boolean nuevoDialogo){
        if(nuevoDialogo && dialog!=null){
                dialog.dismiss();
        }
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoBoton = ((Button) v).getText().toString();
                if(!textoBoton.equals("Continuar")) {
                    escanerSeleccionado = ((Button) v).getText().toString();
                }
                if(bt_sincronizar.getText().equals("Actualizar")) {
                    qrScan = IntentIntegrator.forSupportFragment(Escanear.this);
                    qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    qrScan.setPrompt("Escane el codigo QR");
                    qrScan.setCameraId(0);
                    qrScan.setBeepEnabled(false);
                    qrScan.setBarcodeImageEnabled(false);
                    qrScan.setOrientationLocked(false);
                    qrScan.initiateScan();
                }else{
                    Toast.makeText(contextoEscaner, "Debe actualizar antes de escanear", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String resultadoQR = result.getContents();
            if(resultadoQR!=null){
                resultadoQR = getCodeOfUrl(resultadoQR);
                Tiquete tiqueteEscaneado = null;
                tiqueteEscaneado = dbtd.obtenerTiqueteByQR(resultadoQR);
                if(tiqueteEscaneado!=null){
                    definirAccionTiqueteEscaneado(tiqueteEscaneado, dbtd);
                }else{
                    cargarDialogoResultadoEscanner(null, 0);
                }
            }
        }

    }

    public String getCodeOfUrl(String url){
        String code = url.substring(url.lastIndexOf("codigo=")+7);
        return code;
    }//http://yappdevelopers.com/boleteria/?codigo=rq2SpqKY

    public void definirAccionTiqueteEscaneado(Tiquete tiquete, DBTiquetesDisponibles dbtd){
        if(!tiquete.isCanjeada()) {
            if (escanerSeleccionado.equals("Todos los tipos") || tiquete.getTipoTiquete().equals(escanerSeleccionado)) {
                if (dbtd.setCanjeado(tiquete.getIdTiquete()) > 0) {
                    dbtd.setFechaEscaneado(tiquete.getIdTiquete());
                    tiquete.setFechaEscaneo(DBTiquetesDisponibles.fechaEscaneao);
                    cargarTiquetesFromDB();
                    cargarVendidosYEscaneados();
                    cargarDialogoResultadoEscanner(tiquete, 1);
                } else {
                    desplegarMensaje("Error al canjear el tiquete");
                }
            } else {
                cargarDialogoResultadoEscanner(tiquete, 0);
            }
        }else{
            cargarDialogoResultadoEscanner(tiquete, -1);
        }
    }

    public void cargarVendidosYEscaneados(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(evento!=null){
                    tv_tiquets_vendidos.setText(String.valueOf(evento.gettotalSales()));
                    tv_tiquets_escaneados.setText(String.valueOf(getCantidadTiquetesEscaneadosLocal()));
                }
            }
        });
    }

    public int getCantidadTiquetesEscaneadosLocal(){
        tiquetesEscaneados = 0;
        ArrayList<Tiquete> listaAContar;
        if(tiquetesBDLocal!=null){
            listaAContar = tiquetesBDLocal;
        }else{
            listaAContar = evento.getTiquetesEvento();
        }
        for (Tiquete tiquete: listaAContar){
            if(tiquete.isCanjeada()){
                tiquetesEscaneados++;
            }
        }
        return tiquetesEscaneados;
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

    public void cambiarFondoBotones(Button boton, boolean bandera){
        if(bandera){
            boton.setBackgroundResource(R.drawable.radius_button_blue);
        }else{
            boton.setBackgroundResource(R.drawable.radius_button_sincronizar);
        }
    }

    public boolean guardarTiquetesDisponiblesBD(){
        if(tiquetesBDLocal.isEmpty()) {
            for (Tiquete tiquete : evento.getTiquetesEvento()) {
                dbtd.agregar(tiquete);
            }
            cargarTiquetesFromDB();
            cargarVendidosYEscaneados();
        }else{
            tiquetesBDLocal.clear();
            dbtd.eliminarBaseDatos();
            guardarTiquetesDisponiblesBD();
        }
        return true;
    }

    public void cargarTiquetesFromDB(){
        if(dbtd==null && evento!=null) {
            dbtd = new DBTiquetesDisponibles(getActivity().getApplicationContext(), evento.getIdEvento());
        }
        tiquetesBDLocal = dbtd.obtenerTodos();
        if(!tiquetesBDLocal.isEmpty()) {
            habilitarEscaneoFromOtherThread();
        }
    }

    public void showToastFromOtherThread(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void habilitarEscaneoFromOtherThread(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                habilitarEscaneo();
            }
        });
    }

    public void desplegarMensaje(String mensaje){
        Toast.makeText(contextoEscaner, mensaje, Toast.LENGTH_LONG).show();
    }

    public void cargarDialogoResultadoEscanner(Tiquete tiquete, int resultado){ //0 no es un tiquete o no pertenece a escaneer, 1 tiquete escaneado exitosamente, -1 tiquete que ya ha sido escaneado posteriormente
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((MainActivity)getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_result_scann, null);

        tvMensajePrincipalDialogResultScann = (TextView) dialogView.findViewById(R.id.id_mensaje_principal);
        tvMensajeSecundarioDialogResultScann = (TextView) dialogView.findViewById(R.id.id_mensaje_secundario);
        tvFechaHoraDialogResultScann = (TextView) dialogView.findViewById(R.id.id_fechahora_escaneado);
        tvTipoEntradaDialogResultScann = (TextView) dialogView.findViewById(R.id.id_tipoEntrada);
        tvNombreDialogResultScann = (TextView) dialogView.findViewById(R.id.id_nombreComprador);
        tvCedulaDialogResultScann = (TextView) dialogView.findViewById(R.id.id_cedulaComprador);
        viewDialogResultScann = dialogView.findViewById(R.id.id_view_rd);
        ivIcDialogResultScann = (ImageView) dialogView.findViewById(R.id.id_ic_dialog_result);

        btContinuarDialogResultScann = (Button) dialogView.findViewById(R.id.id_bt_continuar);
        btSalirDialogResultScann = (Button) dialogView.findViewById(R.id.id_bt_salir);

        btSalirDialogResultScann.setOnClickListener(cerrarDialogo());
        btContinuarDialogResultScann.setOnClickListener(abrirEscanner(true));

        String mensajeNoTiqueteNoEscaner = "Este código no es válido para ingresar a esta experiencia";
        String mensajeSecundario = "";
        tvMensajeSecundarioDialogResultScann.setVisibility(View.INVISIBLE);

        if(tiquete!=null){
            try {
                if(!tiquete.getFechaEscaneo().equals("null")) {
                    tvFechaHoraDialogResultScann.setText(DataAccess.formatearFechaDialogoEscaner(tiquete.getFechaEscaneo()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvNombreDialogResultScann.setText(tiquete.getNombreComprador());
            tvTipoEntradaDialogResultScann.setText(tiquete.getTipoTiquete());
            if(!tiquete.getCedulaComprador().isEmpty()){
                tvCedulaDialogResultScann.setText(tiquete.getCedulaComprador());
            }
            mensajeNoTiqueteNoEscaner = "Bloque incorrecto";
            mensajeSecundario = "Estás escaneando entradas únicamente de "+escanerSeleccionado+" , si desea puede Salir y seleccionar otro bloque de tipo de entradas";
        }else{
            mensajeSecundario = "Dale Salir y luego al botón verde Actualizar para que verifiques";
        }
        switch (resultado){
            case -1: // -1 tiquete que ya ha sido escaneado posteriormente
                viewDialogResultScann.setBackgroundColor(Color.parseColor("#e50000"));
                tvMensajePrincipalDialogResultScann.setText("Yapp! ha sido escaneado");
                ivIcDialogResultScann.setBackgroundResource(R.drawable.cancel);
                break;

            case 0: //0 no es un tiquete o no pertenece a bloquee
                tvMensajeSecundarioDialogResultScann.setText(mensajeSecundario);
                viewDialogResultScann.setBackgroundColor(Color.parseColor("#e50000"));
                tvMensajePrincipalDialogResultScann.setText(mensajeNoTiqueteNoEscaner);
                ivIcDialogResultScann.setBackgroundResource(R.drawable.cancel);
                tvFechaHoraDialogResultScann.setVisibility(View.INVISIBLE);
                tvNombreDialogResultScann.setVisibility(View.INVISIBLE);
                tvTipoEntradaDialogResultScann.setVisibility(View.INVISIBLE);
                tvMensajeSecundarioDialogResultScann.setVisibility(View.VISIBLE);
                tvCedulaDialogResultScann.setVisibility(View.INVISIBLE);
                break;

            case 1: //1 tiquete escaneado exitosamente

                break;

        }

        mBuilder.setView(dialogView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public View.OnClickListener cerrarDialogo(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null) {
                    dialog.dismiss();
                }
            }
        };
    }

    public void checkEvento(){
        if (evento == null) {
            evento = MainActivity.evento;
        }
    }
}
