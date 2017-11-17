package com.yapp.mrrabbit.yapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PerfilExperiencia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PerfilExperiencia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilExperiencia extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button bt_escanear;
    private ImageButton ibt_reporteFinanciero;
    private ImageButton ibt_influencers;
    private ImageButton ibt_reporte;
    private ImageButton ibt_pausar_venta;
    private ImageButton ibt_estadisticas;
    private TextView tv_cerrarDialog;
    private TextView tv_pausarVenta;
    private TextView tv_ticketes_hoy, tv_ticketes_semana, tv_ticketes_mes, tv_ticketes_total;
    private TextView tv_dinero_hoy, tv_dinero_semana, tv_dinero_mes, tv_dinero_total;
    private Button bt_accion_dialog;
    private AlertDialog dialog;
    private View dialogView;
    private boolean excel_enviado;

    private ArrayList<EditText> lista_ed_entradasDisponibles;
    private ArrayList<EditText> lista_ed_mensajeEntrada;
    private int sumEntradasDisponibles;
    private String parametrosURLPausarVenta;

    private EditText etEntrada1;
    private EditText etEntrada2;

    private boolean ventaPausada;

    private ProgressDialog progress;

    private Evento eventoPerfil;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PerfilExperiencia() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilExperiencia.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilExperiencia newInstance(String param1, String param2) {
        PerfilExperiencia fragment = new PerfilExperiencia();
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
        return inflater.inflate(R.layout.fragment_perfil_experiencia, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;

        int id = v.getId();
        switch (id){
            case R.id.fpe_bt_escanear:
                fragment = new Escanear();
                ((Escanear)fragment).setEvento(eventoPerfil);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.main_content, fragment).commit();
                ((MainActivity)getActivity()).esconderItemsActionMenu();
                //((MainActivity)getActivity()).getAmiBuscar().setVisible(true);
                break;
            case R.id.ib_referidos_influencers:
                cargarDialogo(R.layout.dialog_influencers, R.id.tv_cerrar_dialog_influencers, R.id.bt_enviar_excel_influencers);
                getInfluencersPorEvento();
                excel_enviado = false;
                bt_accion_dialog.setOnClickListener(enviar_excel("referral_report"));
                break;
            case R.id.ib_reporte_financiero:
                cargarDialogo(R.layout.dialog_finanzas, R.id.tv_cerrar_dialog_finanzas, R.id.bt_solcitar_adelanto);
                desplegarPopUpFinanzas();
                bt_accion_dialog.setOnClickListener(solicitarAdelanto());
                break;
            case R.id.ib_reporte:
                cargarDialogo(R.layout.dialog_reporte, R.id.tv_cerrar_dialog_reporte, R.id.bt_enviar_excel_reporte);
                desplegarPopUpReporte();
                excel_enviado = false;
                bt_accion_dialog.setOnClickListener(enviar_excel("send_report"));
                break;
            case R.id.ib_pausar_venta:
                cargarDialogo(R.layout.dialog_pausar_venta, R.id.tv_cerrar_dialog_pausar_venta, R.id.bt_guardar_pausar_venta);
                desplegarPopUpPausarVentas();
                bt_accion_dialog.setOnClickListener(guardar_pausar_venta("Guardado"));
                break;

        }
    }

    public void getInfluencersPorEvento(){
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DataAccess da = new DataAccess();
                eventoPerfil.setInfluencers(da.getInfluencersEvento(eventoPerfil.getIdEvento()));
                if(!eventoPerfil.getInfluencers().isEmpty()) {
                    añadirInfluencers();
                    showDialogFromOtherThread();
                }else{
                    showToastFromOtherThread("Tu experiencia no tiene influencers aún");
                }
                dismissLoading();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }

    public void desplegarPopUpFinanzas(){
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!eventoPerfil.getTipoTiquetes().isEmpty()) {
                    cargarFinanzas();
                    showDialogFromOtherThread();
                }else{
                    showToastFromOtherThread("Este evento no cuenta datos de finanzas");
                }
                dismissLoading();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }

    public void desplegarPopUpReporte(){
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!eventoPerfil.getTipoTiquetes().isEmpty()) {
                    cargarReporte();
                    showDialogFromOtherThread();
                }else{
                    showToastFromOtherThread("Este evento no cuenta datos de reporte");
                }
                dismissLoading();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }

    public void desplegarPopUpPausarVentas(){
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!eventoPerfil.getTipoTiquetes().isEmpty()) {
                    cargarPausarVenta();
                    showDialogFromOtherThread();
                }else{
                    showToastFromOtherThread("Este evento no cuenta con entradas");
                }
                dismissLoading();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }

    public View.OnClickListener enviar_excel(final String url){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogLoading();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataAccess da = new DataAccess();
                        if(da.envarExcel(eventoPerfil.getIdEvento(), url)){
                            showToastFromOtherThread("Excel enviado");
                        }else{
                            showToastFromOtherThread("Hubo un problema al enviar el excel, intente de nuevo mas tarde");
                        }
                        dismissLoading();
                        Thread.currentThread().interrupt();
                    }
                });
                t.start();
            }
        };
    }

    public void cargarDialogo(int resource, int tv_cerrar_dialog, int bt_dialog){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((MainActivity)getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(resource, null);

        tv_cerrarDialog = (TextView) dialogView.findViewById(tv_cerrar_dialog);
        tv_cerrarDialog.setOnClickListener(cerrarDialogo());

        bt_accion_dialog = (Button) dialogView.findViewById(bt_dialog);

        mBuilder.setView(dialogView);
        dialog = mBuilder.create();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventoPerfil = MainActivity.evento;
        cargarElemntosVisuales();
        hayTiquetesDisponibles();
        if(eventoPerfil!=null){
            cargarEventoPerfil();
        }
    }

    public void cargarEventoPerfil(){
        String modena = "$";
        tv_ticketes_hoy.setText(String.valueOf(eventoPerfil.gettodaySales()));
        tv_ticketes_semana.setText(String.valueOf(eventoPerfil.getweekSales()));
        tv_ticketes_mes.setText(String.valueOf(eventoPerfil.getmonthSales()));
        tv_ticketes_total.setText(String.valueOf(eventoPerfil.gettotalSales()));
        tv_dinero_hoy.setText(modena+String.valueOf(eventoPerfil.gettodayMoney()));
        tv_dinero_semana.setText(modena+String.valueOf(eventoPerfil.getweekMoney()));
        tv_dinero_mes.setText(modena+String.valueOf(eventoPerfil.getmonthMoney()));
        tv_dinero_total.setText(modena+String.valueOf(eventoPerfil.gettotalMoney()));
    }

    private void cargarElemntosVisuales() {
        bt_escanear = (Button) getActivity().findViewById(R.id.fpe_bt_escanear);

        ibt_reporteFinanciero = (ImageButton) getActivity().findViewById(R.id.ib_reporte_financiero);
        ibt_influencers = (ImageButton) getActivity().findViewById(R.id.ib_referidos_influencers);
        ibt_reporte = (ImageButton) getActivity().findViewById(R.id.ib_reporte);
        ibt_pausar_venta = (ImageButton) getActivity().findViewById(R.id.ib_pausar_venta);

        tv_pausarVenta = (TextView) getActivity().findViewById(R.id.tv_pausarVenta);

        tv_ticketes_hoy = (TextView) getActivity().findViewById(R.id.tv_tickets_hoy);
        tv_ticketes_semana = (TextView) getActivity().findViewById(R.id.tv_tickets_semana);
        tv_ticketes_mes = (TextView) getActivity().findViewById(R.id.tv_tickets_mes);
        tv_ticketes_total = (TextView) getActivity().findViewById(R.id.tv_tickets_total);
        tv_dinero_hoy = (TextView) getActivity().findViewById(R.id.tv_dollar_hoy);
        tv_dinero_semana = (TextView) getActivity().findViewById(R.id.tv_dollar_semana);
        tv_dinero_mes = (TextView) getActivity().findViewById(R.id.tv_dollar_mes);
        tv_dinero_total = (TextView) getActivity().findViewById(R.id.tv_dollar_total);

        bt_escanear.setOnClickListener(this);
        ibt_reporteFinanciero.setOnClickListener(this);
        ibt_influencers.setOnClickListener(this);
        ibt_reporte.setOnClickListener(this);
        ibt_pausar_venta.setOnClickListener(this);
    }



    public void añadirInfluencers(){

        ConstraintLayout layout = (ConstraintLayout) dialogView.findViewById(R.id.dialog_influencers);
        ConstraintSet set = new ConstraintSet();

        int id_nombre_anterior = R.id.textView21;
        /*int id_separaodor_1_anterior = 0;
        int id_correo_anterior= 0;
        int id_separaodor_2_anterior = 0;
        int id_cantidad_anterior = 0;*/

        for (int i=1; i<=eventoPerfil.getInfluencers().size(); i++){
            TextView tv_nombre_tiquete = new TextView(getActivity());
            tv_nombre_tiquete.setText(eventoPerfil.getInfluencers().get(i-1).getNombre());
            tv_nombre_tiquete.setWidth(160);
            tv_nombre_tiquete.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_nombre_tiquete.setId(i);

            TextView tv_separador_1 = new TextView(getActivity());
            tv_separador_1.setText("|");
            tv_separador_1.setWidth(5);
            tv_separador_1.setId(40+i);

            TextView tv_correo = new TextView(getActivity());
            tv_correo.setText(eventoPerfil.getInfluencers().get(i-1).getCorreo());
            tv_correo.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_correo.setWidth(340);
            tv_correo.setId(80+i);

            TextView tv_separador_2 = new TextView(getActivity());
            tv_separador_2.setText("|");
            tv_separador_2.setWidth(5);
            tv_separador_2.setId(120+i);

            TextView tv_dinero = new TextView(getActivity());
            tv_dinero.setText(String.valueOf(eventoPerfil.getInfluencers().get(i-1).getCantidad()+200));
            tv_dinero.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_dinero.setWidth(50);
            tv_dinero.setId(160+i);

            float textSize = 12;

            tv_nombre_tiquete.setTextSize(textSize);
            tv_correo.setTextSize(textSize);
            tv_dinero.setTextSize(textSize);
            tv_separador_1.setTextSize(textSize);
            tv_separador_2.setTextSize(textSize);

            View v_inea = new View(getActivity());
            v_inea.setId(200+i);
            v_inea.setBackgroundColor(Color.parseColor("#D3D3D3"));
            v_inea.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

            layout.addView(tv_nombre_tiquete, 0);
            layout.addView(tv_separador_1, 0);
            layout.addView(tv_correo, 0);
            layout.addView(tv_separador_2, 0);
            layout.addView(tv_dinero, 0);
            layout.addView(v_inea, 0);

            set.clone(layout);

            //contrain del nombre
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 10);
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.RIGHT, tv_separador_1.getId(), ConstraintSet.LEFT);

            //constrain separador 1
            set.connect(tv_separador_1.getId(), ConstraintSet.TOP, tv_nombre_tiquete.getId(), ConstraintSet.TOP);
            set.connect(tv_separador_1.getId(), ConstraintSet.LEFT, tv_nombre_tiquete.getId(), ConstraintSet.RIGHT);
            set.connect(tv_separador_1.getId(), ConstraintSet.RIGHT, tv_correo.getId(), ConstraintSet.LEFT);

            //constrain correo
            set.connect(tv_correo.getId(), ConstraintSet.TOP, tv_separador_1.getId(), ConstraintSet.TOP);
            set.connect(tv_correo.getId(), ConstraintSet.LEFT, tv_separador_1.getId(), ConstraintSet.RIGHT);
            set.connect(tv_correo.getId(), ConstraintSet.RIGHT, tv_separador_2.getId(), ConstraintSet.LEFT);

            //constrain separador 2
            set.connect(tv_separador_2.getId(), ConstraintSet.TOP, tv_correo.getId(), ConstraintSet.TOP);
            set.connect(tv_separador_2.getId(), ConstraintSet.LEFT, tv_correo.getId(), ConstraintSet.RIGHT);
            set.connect(tv_separador_2.getId(), ConstraintSet.RIGHT, tv_dinero.getId(), ConstraintSet.LEFT);

            //constrain cantidad
            set.connect(tv_dinero.getId(), ConstraintSet.TOP, tv_separador_2.getId(), ConstraintSet.TOP);
            set.connect(tv_dinero.getId(), ConstraintSet.LEFT, tv_separador_2.getId(), ConstraintSet.RIGHT);
            set.connect(tv_dinero.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

            //contrain linea view
            set.connect(v_inea.getId(), ConstraintSet.TOP, tv_nombre_tiquete.getId(), ConstraintSet.BOTTOM);

            set.applyTo(layout);

            id_nombre_anterior = i;
        }

        set.connect(R.id.bt_enviar_excel_influencers, ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 20);
        set.applyTo(layout);
    }

    public void cargarFinanzas(){

        ConstraintLayout layout = (ConstraintLayout) dialogView.findViewById(R.id.dialog_finanzas);
        ConstraintSet set = new ConstraintSet();

        TipoTiquete ttTemp = eventoPerfil.getTipoTiquetes().get(0);

        ((TextView)dialogView.findViewById(R.id.tv_tipo_entrada_1)).setText(ttTemp.getNombre());
        ((TextView)dialogView.findViewById(R.id.tv_dinero_tipoentrada_1)).setText("$"+String.valueOf(ttTemp.getDineroSubtotal()));
        ((TextView)dialogView.findViewById(R.id.tv_numero_entradas_1)).setText(String.valueOf(ttTemp.getTiquetesVendidos()));

        int id_nombre_anterior = R.id.tv_tipo_entrada_1;
        int id_cantidad_anterior = R.id.tv_numero_entradas_1;
        int id_dinero_anterior = R.id.tv_dinero_tipoentrada_1;
        int size = eventoPerfil.getTipoTiquetes().size();

        int subtotalEntradas = 0;
        double subtotalDinero = 0;
        double comision = 0;
        double recibiras = 0;


        for (int i=1; i<size; i++){
            ttTemp = eventoPerfil.getTipoTiquetes().get(i);

            TextView tv_nombre_tiquete = new TextView(getActivity());
            tv_nombre_tiquete.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_nombre_tiquete.setText(ttTemp.getNombre());
            tv_nombre_tiquete.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_nombre_tiquete.setId(i);

            subtotalEntradas += ttTemp.getTiquetesVendidos();
            TextView tv_cantidad = new TextView(getActivity());
            tv_cantidad.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_cantidad.setText(String.valueOf(ttTemp.getTiquetesVendidos()));
            tv_cantidad.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_cantidad.setId((size*1)+i);

            subtotalDinero += ttTemp.getDineroSubtotal();
            TextView tv_dinero = new TextView(getActivity());
            tv_dinero.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_dinero.setText("$"+String.valueOf(ttTemp.getDineroSubtotal()));
            tv_dinero.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_dinero.setId((size*2)+i);

            View v_inea = new View(getActivity());
            v_inea.setId((size*3)+i);
            v_inea.setBackgroundColor(Color.parseColor("#D3D3D3"));
            v_inea.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

            layout.addView(tv_nombre_tiquete, 0);
            layout.addView(tv_cantidad, 0);
            layout.addView(tv_dinero, 0);
            layout.addView(v_inea, 0);

            set.clone(layout);

            //contrain del nombre
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 20);
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.LEFT, id_nombre_anterior, ConstraintSet.LEFT, 10);

            //constrain cantidad entradas
            set.connect(tv_cantidad.getId(), ConstraintSet.TOP, tv_nombre_tiquete.getId(), ConstraintSet.TOP);
            set.connect(tv_cantidad.getId(), ConstraintSet.BOTTOM, tv_nombre_tiquete.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_cantidad.getId(), ConstraintSet.RIGHT, id_cantidad_anterior, ConstraintSet.RIGHT);

            //constrain dinero entradas
            set.connect(tv_dinero.getId(), ConstraintSet.TOP, tv_cantidad.getId(), ConstraintSet.TOP);
            set.connect(tv_dinero.getId(), ConstraintSet.BOTTOM, tv_cantidad.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_dinero.getId(), ConstraintSet.RIGHT, id_dinero_anterior, ConstraintSet.RIGHT, 10);

            //contrain linea view
            set.connect(v_inea.getId(), ConstraintSet.TOP, tv_nombre_tiquete.getId(), ConstraintSet.BOTTOM);

            set.applyTo(layout);

            id_nombre_anterior = tv_nombre_tiquete.getId();
            id_cantidad_anterior = tv_cantidad.getId();
            id_dinero_anterior = tv_dinero.getId();
        }

        comision = subtotalDinero*0.10;
        recibiras = subtotalDinero-comision;

        ((TextView)dialogView.findViewById(R.id.tv_subtotal_numero_entradas)).setText(String.valueOf(subtotalEntradas));
        ((TextView)dialogView.findViewById(R.id.tv_subtotal_dinero_tipoentrada)).setText(String.valueOf(subtotalDinero));
        ((TextView)dialogView.findViewById(R.id.tv_dinero_comision)).setText(String.valueOf(comision));
        ((TextView)dialogView.findViewById(R.id.tv_dinero_recibiras)).setText(String.valueOf(recibiras));

        set.connect(R.id.tv_subtotal, ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 20);
        set.applyTo(layout);

        if(recibiras<1000){
            ((Button)dialogView.findViewById(R.id.bt_solcitar_adelanto)).setVisibility(View.INVISIBLE);
        }
    }

    public void cargarReporte(){
        ConstraintLayout layout = (ConstraintLayout) dialogView.findViewById(R.id.dialog_reporte);
        ConstraintSet set = new ConstraintSet();

        int id_tipo_entrada_anterior_vendida = R.id.tv_ventas;
        int id_tipo_entrada_anterior_escaneada = R.id.tv_escaneo;
        int size = eventoPerfil.getTipoTiquetes().size();

        TipoTiquete tipoTemp = null;
        int id = 0;
        for (int i = 0; i < size; i++) {
            tipoTemp = eventoPerfil.getTipoTiquetes().get(i);
            //Tiquetes Vendidos
            id = i+1;
            TextView tv_tiquete = new TextView(getActivity());
            tv_tiquete.setId(id);
            tv_tiquete.setLayoutParams(new ConstraintLayout.LayoutParams(120, 120));
            tv_tiquete.setBackgroundResource(R.drawable.radius_border);
            tv_tiquete.setGravity(Gravity.CENTER);
            tv_tiquete.setTextSize(20);
            tv_tiquete.setTextColor(Color.BLACK);
            tv_tiquete.setTypeface(null, Typeface.BOLD);
            tv_tiquete.setText(String.valueOf(tipoTemp.getTiquetesVendidos()));

            TextView tv_dinero_tiquete = new TextView(getActivity());
            tv_dinero_tiquete.setId(size+id);
            tv_dinero_tiquete.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_dinero_tiquete.setGravity(Gravity.CENTER);
            tv_dinero_tiquete.setTextSize(15);
            tv_dinero_tiquete.setTextColor(Color.parseColor("#FF4081"));
            tv_dinero_tiquete.setTypeface(null, Typeface.BOLD);
            tv_dinero_tiquete.setText("$"+String.valueOf(tipoTemp.getDineroSubtotal()));

            TextView tv_nombre_tiquete = new TextView(getActivity());
            tv_nombre_tiquete.setId((2*size)+id);
            tv_nombre_tiquete.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_nombre_tiquete.setMaxWidth(130);
            tv_nombre_tiquete.setGravity(Gravity.CENTER);
            tv_nombre_tiquete.setTextSize(11);
            tv_nombre_tiquete.setText(String.valueOf(tipoTemp.getNombre()));


            //Tiquetes Escaneados
            TextView tv_tiquete_escaneado = new TextView(getActivity());
            tv_tiquete_escaneado.setId((3*size)+id);
            tv_tiquete_escaneado.setLayoutParams(new ConstraintLayout.LayoutParams(120, 120));
            tv_tiquete_escaneado.setBackgroundResource(R.drawable.radius_border);
            tv_tiquete_escaneado.setGravity(Gravity.CENTER);
            tv_tiquete_escaneado.setTextSize(20);
            tv_tiquete_escaneado.setTextColor(Color.BLACK);
            tv_tiquete_escaneado.setTypeface(null, Typeface.BOLD);
            tv_tiquete_escaneado.setText(String.valueOf(i));

            TextView tv_nombre_tiquete_escaneado = new TextView(getActivity());
            tv_nombre_tiquete_escaneado.setId((4*size)+id);
            tv_nombre_tiquete_escaneado.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_nombre_tiquete_escaneado.setMaxWidth(130);
            tv_nombre_tiquete_escaneado.setGravity(Gravity.CENTER);
            tv_nombre_tiquete_escaneado.setTextSize(11);
            tv_nombre_tiquete_escaneado.setText(String.valueOf(tipoTemp.getNombre()));

            layout.addView(tv_tiquete);
            layout.addView(tv_dinero_tiquete);
            layout.addView(tv_nombre_tiquete);
            layout.addView(tv_tiquete_escaneado);
            layout.addView(tv_nombre_tiquete_escaneado);

            set.clone(layout);

            if(i==0 || i%4==0){
                set.connect(tv_tiquete.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_vendida, ConstraintSet.BOTTOM, marginCorrectoTvTiquete(i));
                set.connect(tv_tiquete.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 20);
                //set.connect(tv_tiquete.getId(), ConstraintSet.RIGHT, tv_tiquete.getId()+1, ConstraintSet.RIGHT);

                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_escaneada, ConstraintSet.BOTTOM, marginCorrectoTvTiquete(i));
                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 20);
                //set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.RIGHT, tv_tiquete_escaneado.getId()+1, ConstraintSet.RIGHT);
            }else if((i+1)%4==0){
                set.connect(tv_tiquete.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_vendida, ConstraintSet.TOP);
                set.connect(tv_tiquete.getId(), ConstraintSet.LEFT, id_tipo_entrada_anterior_vendida, ConstraintSet.RIGHT);
                set.connect(tv_tiquete.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_escaneada, ConstraintSet.TOP);
                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.LEFT, id_tipo_entrada_anterior_escaneada, ConstraintSet.RIGHT);
                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

                set.connect(id_tipo_entrada_anterior_vendida, ConstraintSet.RIGHT, tv_tiquete.getId(), ConstraintSet.LEFT);
                set.connect(id_tipo_entrada_anterior_escaneada, ConstraintSet.RIGHT, tv_tiquete_escaneado.getId(), ConstraintSet.LEFT);
            }else{
                set.connect(tv_tiquete.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_vendida, ConstraintSet.TOP);
                set.connect(tv_tiquete.getId(), ConstraintSet.LEFT, id_tipo_entrada_anterior_vendida, ConstraintSet.RIGHT);
                //set.connect(tv_tiquete.getId(), ConstraintSet.RIGHT, tv_tiquete.getId()+1, ConstraintSet.RIGHT);

                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.TOP, id_tipo_entrada_anterior_escaneada, ConstraintSet.TOP);
                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.LEFT, id_tipo_entrada_anterior_escaneada, ConstraintSet.RIGHT);
                //set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.RIGHT, tv_tiquete_escaneado.getId()+1, ConstraintSet.RIGHT);

                set.connect(id_tipo_entrada_anterior_vendida, ConstraintSet.RIGHT, tv_tiquete.getId(), ConstraintSet.LEFT);
                set.connect(id_tipo_entrada_anterior_escaneada, ConstraintSet.RIGHT, tv_tiquete_escaneado.getId(), ConstraintSet.LEFT);
            }

            if(i == size-1){
                set.connect(tv_tiquete.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
                set.connect(tv_tiquete_escaneado.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
            }

            //constrain del tv_dinero_tiquete
            set.connect(tv_dinero_tiquete.getId(), ConstraintSet.BOTTOM, tv_tiquete.getId(), ConstraintSet.BOTTOM, 0);
            set.connect(tv_dinero_tiquete.getId(), ConstraintSet.LEFT, tv_tiquete.getId(), ConstraintSet.LEFT, 0);
            set.connect(tv_dinero_tiquete.getId(), ConstraintSet.RIGHT, tv_tiquete.getId(), ConstraintSet.RIGHT, 0);

            //constrain del tv_nombre_tiquete
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.TOP, tv_tiquete.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.LEFT, tv_tiquete.getId(), ConstraintSet.LEFT);
            set.connect(tv_nombre_tiquete.getId(), ConstraintSet.RIGHT, tv_tiquete.getId(), ConstraintSet.RIGHT);

            //constrain del tv_nomobre_tiquete_escaneado
            set.connect(tv_nombre_tiquete_escaneado.getId(), ConstraintSet.TOP, tv_tiquete_escaneado.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_nombre_tiquete_escaneado.getId(), ConstraintSet.LEFT, tv_tiquete_escaneado.getId(), ConstraintSet.LEFT);
            set.connect(tv_nombre_tiquete_escaneado.getId(), ConstraintSet.RIGHT, tv_tiquete_escaneado.getId(), ConstraintSet.RIGHT);

            set.applyTo(layout);

            id_tipo_entrada_anterior_vendida = tv_tiquete.getId();
            id_tipo_entrada_anterior_escaneada = tv_tiquete_escaneado.getId();
        }

        set.connect(R.id.tv_escaneo, ConstraintSet.TOP, id_tipo_entrada_anterior_vendida, ConstraintSet.BOTTOM, 100);
        //set.connect(R.id.tv_escaneo_1, ConstraintSet.TOP, id_tipo_entrada_anterior_escaneada, ConstraintSet.BOTTOM, 70);
        set.connect(R.id.bt_enviar_excel_reporte, ConstraintSet.TOP, id_tipo_entrada_anterior_escaneada, ConstraintSet.BOTTOM, 100);
        set.applyTo(layout);
    }

    public void cargarPausarVenta(){
        ConstraintLayout layout = (ConstraintLayout) dialogView.findViewById(R.id.dialog_pausar_venta);
        ConstraintSet set = new ConstraintSet();

        int id_view_anterior = R.id.view_pausar_venta;
        int size = eventoPerfil.getTipoTiquetes().size();

        TipoTiquete tipoTemp = null;
        lista_ed_entradasDisponibles = new ArrayList<>();
        lista_ed_mensajeEntrada = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            tipoTemp = eventoPerfil.getTipoTiquetes().get(i-1);

            TextView tv_nombre_entrada = new TextView(getActivity());
            tv_nombre_entrada.setId(i);
            tv_nombre_entrada.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_nombre_entrada.setTextColor(Color.DKGRAY);
            tv_nombre_entrada.setTextSize(20);
            tv_nombre_entrada.setGravity(Gravity.CENTER);
            tv_nombre_entrada.setText(tipoTemp.getNombre());

            EditText et_disponibles = new EditText(getActivity());
            et_disponibles.setId(size+i);
            et_disponibles.setLayoutParams(new ConstraintLayout.LayoutParams(120, 120));
            et_disponibles.setBackgroundResource(R.drawable.radius_border);
            et_disponibles.setText(String.valueOf(tipoTemp.getTiquetesDisponibles()));
            et_disponibles.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            //et_disponibles.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            et_disponibles.setTextSize(20);
            et_disponibles.setTextColor(Color.BLACK);
            et_disponibles.setTypeface(null, Typeface.BOLD);
            et_disponibles.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et_disponibles.setSelection(et_disponibles.getText().length());
            et_disponibles.setFilters(new InputFilter[] {new InputFilter.LengthFilter(5)});
            lista_ed_entradasDisponibles.add(et_disponibles);

            TextView tv_texto_disponibles = new TextView(getActivity());
            tv_texto_disponibles.setId((size*2)+i);
            tv_texto_disponibles.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_texto_disponibles.setTextColor(Color.parseColor("#cfcfcf"));
            tv_texto_disponibles.setTextSize(12);
            tv_texto_disponibles.setText("Disponibles");

            EditText et_mensaje = new EditText(getActivity());
            et_mensaje.setId((size*3)+i);
            et_mensaje.setLayoutParams(new ConstraintLayout.LayoutParams(440, 60));
            et_mensaje.setTextSize(16);
            et_mensaje.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            et_mensaje.setTextColor(Color.BLACK);
            et_mensaje.setBackgroundResource(R.drawable.black_border);
            et_mensaje.setText(tipoTemp.getMensaje());
            et_mensaje.setPadding(0,5,0,0);
            lista_ed_mensajeEntrada.add(et_mensaje);

            TextView tv_texto_mensaje_clientes = new TextView(getActivity());
            tv_texto_mensaje_clientes.setId((size*4)+i);
            tv_texto_mensaje_clientes.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_texto_mensaje_clientes.setTextColor(Color.parseColor("#cfcfcf"));
            tv_texto_mensaje_clientes.setTextSize(12);
            tv_texto_mensaje_clientes.setText("Mensaje para los clientes");

            View v_inea = new View(getActivity());
            v_inea.setId((size*5)+i);
            v_inea.setBackgroundColor(Color.parseColor("#D3D3D3"));
            v_inea.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

            layout.addView(tv_nombre_entrada);
            layout.addView(et_disponibles);
            layout.addView(tv_texto_disponibles);
            layout.addView(et_mensaje);
            layout.addView(tv_texto_mensaje_clientes);
            layout.addView(v_inea);

            set.clone(layout);

            //contrain tv_nombre entrada
            set.connect(tv_nombre_entrada.getId(), ConstraintSet.TOP, id_view_anterior, ConstraintSet.BOTTOM, 16);
            set.connect(tv_nombre_entrada.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
            set.connect(tv_nombre_entrada.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

            //contrain et_disponibles
            set.connect(et_disponibles.getId(), ConstraintSet.TOP, tv_nombre_entrada.getId(), ConstraintSet.BOTTOM, 20);
            set.connect(et_disponibles.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
            set.connect(et_disponibles.getId(), ConstraintSet.RIGHT, et_mensaje.getId(), ConstraintSet.LEFT);

            //contrain tv_texto_disponibles
            set.connect(tv_texto_disponibles.getId(), ConstraintSet.TOP, et_disponibles.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_texto_disponibles.getId(), ConstraintSet.LEFT, et_disponibles.getId(), ConstraintSet.LEFT);
            set.connect(tv_texto_disponibles.getId(), ConstraintSet.RIGHT, et_disponibles.getId(), ConstraintSet.RIGHT);

            //contrain et_mensaje
            set.connect(et_mensaje.getId(), ConstraintSet.TOP, et_disponibles.getId(), ConstraintSet.TOP);
            set.connect(et_mensaje.getId(), ConstraintSet.BOTTOM, et_disponibles.getId(), ConstraintSet.BOTTOM);
            set.connect(et_mensaje.getId(), ConstraintSet.LEFT, et_disponibles.getId(), ConstraintSet.RIGHT);
            set.connect(et_mensaje.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

            //contrain tv_texto_mensaje_clientes
            set.connect(tv_texto_mensaje_clientes.getId(), ConstraintSet.TOP, et_mensaje.getId(), ConstraintSet.BOTTOM);
            set.connect(tv_texto_mensaje_clientes.getId(), ConstraintSet.LEFT, et_mensaje.getId(), ConstraintSet.LEFT);
            set.connect(tv_texto_mensaje_clientes.getId(), ConstraintSet.RIGHT, et_mensaje.getId(), ConstraintSet.RIGHT);

            //contrain view
            set.connect(v_inea.getId(), ConstraintSet.TOP, tv_texto_disponibles.getId(), ConstraintSet.BOTTOM, 40);
            set.connect(v_inea.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
            set.connect(v_inea.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

            set.applyTo(layout);
            id_view_anterior = v_inea.getId();
        }

        set.connect(R.id.bt_guardar_pausar_venta, ConstraintSet.TOP, id_view_anterior, ConstraintSet.BOTTOM, 20);
        set.applyTo(layout);
    }

    public int marginCorrectoTvTiquete(int index){
        if(index>=4){
            return 80;
        }else{
            return 20;
        }
    }

    public void hayTiquetesDisponibles(){
        if(sumEntradasDisponibles>0){
            ibt_pausar_venta.setImageResource(R.mipmap.ic_pause);
            tv_pausarVenta.setText("Pausar Venta");
        }else{
            ibt_pausar_venta.setImageResource(R.mipmap.ic_play);
            tv_pausarVenta.setText("Activar Venta");
        }
    }

    private View.OnClickListener guardar_pausar_venta(final String mensaje) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarFomratoCorrectoEntradasDisponibles()) {
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                    hayTiquetesDisponibles();
                    dialog.dismiss();
                }else{
                    Toast.makeText(getActivity(), "Solo se permiten numeros", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public boolean validarFomratoCorrectoEntradasDisponibles(){
        sumEntradasDisponibles = 0;
        for (EditText et_temp : lista_ed_entradasDisponibles){
            if(esNumero(et_temp.getText().toString())){
                sumEntradasDisponibles+=Integer.parseInt(et_temp.getText().toString());
            }else{
                return false;
            }
        }
        return true;
    }

    private View.OnClickListener solicitarAdelanto() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirWebView("http://yappexperience.com/adelantos");
            }
        };
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

    public void genrarParametrosURLPausarVenta(int[] cantidades, String[] mensajes){
        parametrosURLPausarVenta = "";
        for (int i=0; i<eventoPerfil.getTipoTiquetes().size(); i++) {
            TipoTiquete temp_tipoTiquete = eventoPerfil.getTipoTiquetes().get(i);
            parametrosURLPausarVenta += eventoPerfil.getTipoTiquetes().get(i).getIdTiquete()+":"+cantidades[i]+":"+mensajes[i]+"|";
        }
    }

    public void abrirWebView(String url){
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url",url);
        intent.putExtras(bundle);
        startActivity(intent);
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

    public boolean esNumero(String numero){
        boolean esNumero = true;
        try{
            Integer.parseInt(numero);
        }catch (NumberFormatException e){
            esNumero = false;
        }
        return  esNumero;
    }

    public void showDialogFromOtherThread(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                dialog.show();
            }
        });
    }

    public void showToastFromOtherThread(final String mensaje){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Evento getEventoPerfil() {
        return eventoPerfil;
    }

    public void setEventoPerfil(Evento eventoPerfil) {
        this.eventoPerfil = eventoPerfil;
    }
}
