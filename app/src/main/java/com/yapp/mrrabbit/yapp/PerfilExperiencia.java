package com.yapp.mrrabbit.yapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

    private EditText etEntrada1;
    private EditText etEntrada2;

    private boolean ventaPausada;

    private ProgressDialog progress;

    private int disponibles_entrada1, disponibles_entrada2;

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
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.main_content, fragment).commit();
                ((MainActivity)getActivity()).esconderItemsActionMenu();
                ((MainActivity)getActivity()).getAmiBuscar().setVisible(true);
                break;
            case R.id.ib_referidos_influencers:
                cargarDialogo(R.layout.dialog_influencers, R.id.tv_cerrar_dialog_influencers, R.id.bt_enviar_excel_influencers);
                getInfluencersPorEvento();
                excel_enviado = false;
                bt_accion_dialog.setOnClickListener(enviar_excel("send_report"));
                break;
            case R.id.ib_reporte_financiero:
                cargarDialogo(R.layout.dialog_finanzas, R.id.tv_cerrar_dialog_finanzas, R.id.bt_solcitar_adelanto);
                desplegarPopUpFinanzas();
                bt_accion_dialog.setOnClickListener(solicitarAdelanto());
                break;
            case R.id.ib_reporte:
                cargarDialogo(R.layout.dialog_reporte, R.id.tv_cerrar_dialog_reporte, R.id.bt_enviar_excel_reporte);
                excel_enviado = false;
                bt_accion_dialog.setOnClickListener(enviar_excel("send_report"));
                dialog.show();
                break;
            case R.id.ib_pausar_venta:
                cargarDialogo(R.layout.dialog_pausar_venta, R.id.tv_cerrar_dialog_pausar_venta, R.id.bt_guardar_pausar_venta);
                cargarElementosPausarVenta();
                bt_accion_dialog.setOnClickListener(guardar_pausar_venta("Guardado"));
                dialog.show();
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
                    showToastFromOtherThread("Este evento no cuenta con influencers");
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
        disponibles_entrada1 = 0;
        disponibles_entrada2 = 0;
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
    }

    private void cargarElementosPausarVenta(){
        etEntrada1 = (EditText) dialogView.findViewById(R.id.et_disponibles_te_1);
        etEntrada2 = (EditText) dialogView.findViewById(R.id.et_disponibles_te_2);

        etEntrada1.setText(String.valueOf(disponibles_entrada1));
        etEntrada2.setText(String.valueOf(disponibles_entrada2));

        etEntrada1.setSelection(etEntrada1.getText().length());
        etEntrada2.setSelection(etEntrada2.getText().length());

        etEntrada1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(5)});
        etEntrada2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(5)});

        etEntrada1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etEntrada2.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    public void hayTiquetesDisponibles(){
        if((disponibles_entrada1+disponibles_entrada2)>0){
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
                if(esNumero(etEntrada1.getText().toString()) && esNumero(etEntrada2.getText().toString())) {
                    disponibles_entrada1 = Integer.valueOf(etEntrada1.getText().toString());
                    disponibles_entrada2 = Integer.valueOf(etEntrada2.getText().toString());
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
                    hayTiquetesDisponibles();
                    dialog.dismiss();
                }else{
                    Toast.makeText(getActivity(), "Solo se permiten numeros", Toast.LENGTH_SHORT).show();
                }
            }
        };
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
