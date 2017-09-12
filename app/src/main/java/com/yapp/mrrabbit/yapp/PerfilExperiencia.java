package com.yapp.mrrabbit.yapp;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
    private TextView tv_cerrarDialog;
    private Button bt_accion_dialog;
    private AlertDialog dialog;
    private View dialogView;

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
                abrirDialogo(R.layout.dialog_influencers, R.id.tv_cerrar_dialog_influencers, R.id.bt_enviar_excel_influencers);
                bt_accion_dialog.setOnClickListener(enviar_excel_influencers());
                break;
            case R.id.ib_reporte_financiero:
                abrirDialogo(R.layout.dialog_finanzas, R.id.tv_cerrar_dialog_finanzas, 0);
                break;
            case R.id.ib_reporte:
                abrirDialogo(R.layout.dialog_reporte, R.id.tv_cerrar_dialog_reporte, 0);
                break;
            case R.id.ib_pausar_venta:
                abrirDialogo(R.layout.dialog_pausar_venta, R.id.tv_cerrar_dialog_pausar_venta, 0);
                break;
        }

    }

    public void abrirDialogo(int resource, int tv_cerrar_dialog, int bt_dialog){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((MainActivity)getActivity());
        dialogView = getActivity().getLayoutInflater().inflate(resource, null);

        tv_cerrarDialog = (TextView) dialogView.findViewById(R.id.tv_cerrar_dialog_influencers);
        tv_cerrarDialog.setOnClickListener(cerrarDialogo());

        bt_accion_dialog = (Button) dialogView.findViewById(bt_dialog);

        mBuilder.setView(dialogView);
        dialog = mBuilder.create();
        dialog.show();
        añadirInfluencers(dialogView);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarElemntosVisuales();
    }

    private void cargarElemntosVisuales() {
        bt_escanear = (Button) getActivity().findViewById(R.id.fpe_bt_escanear);
        ibt_reporteFinanciero = (ImageButton) getActivity().findViewById(R.id.ib_reporte_financiero);
        ibt_influencers = (ImageButton) getActivity().findViewById(R.id.ib_referidos_influencers);
        ibt_reporte = (ImageButton) getActivity().findViewById(R.id.ib_reporte);
        ibt_pausar_venta = (ImageButton) getActivity().findViewById(R.id.ib_pausar_venta);

        bt_escanear.setOnClickListener(this);
        ibt_reporteFinanciero.setOnClickListener(this);
        ibt_influencers.setOnClickListener(this);
        ibt_reporte.setOnClickListener(this);
        ibt_pausar_venta.setOnClickListener(this);
    }


    public void añadirInfluencers(View dialog){
        //View dialog = getActivity().getLayoutInflater().inflate(R.layout.dialog_influencers, null);
        ConstraintLayout layout = (ConstraintLayout) dialog.findViewById(R.id.dialog_influencers);
        ConstraintSet set = new ConstraintSet();

        int id_nombre_anterior = R.id.textView21;
        int id_separaodor_1_anterior = 0;
        int id_correo_anterior= 0;
        int id_separaodor_2_anterior = 0;
        int id_cantidad_anterior = 0;

        for (int i=1; i<40; i++){
            TextView tv_nombre = new TextView(getActivity());
            tv_nombre.setText("Michael "+i);
            tv_nombre.setWidth(140);
            tv_nombre.setId(i);

            TextView tv_separador_1 = new TextView(getActivity());
            tv_separador_1.setText("|");
            tv_separador_1.setId(40+i);

            TextView tv_correo = new TextView(getActivity());
            tv_correo.setText("michael.pemu@gmail.com");
            tv_nombre.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_correo.setWidth(340);
            tv_correo.setId(80+i);

            TextView tv_separador_2 = new TextView(getActivity());
            tv_separador_2.setText("|");
            tv_separador_2.setId(120+i);

            TextView tv_cantidad = new TextView(getActivity());
            tv_cantidad.setText("22");
            tv_cantidad.setWidth(50);
            tv_cantidad.setId(160+i);

            View v_inea = new View(getActivity());
            v_inea.setId(200+i);
            v_inea.setBackgroundColor(Color.parseColor("#D3D3D3"));
            v_inea.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

            layout.addView(tv_nombre, 0);
            layout.addView(tv_separador_1, 0);
            layout.addView(tv_correo, 0);
            layout.addView(tv_separador_2, 0);
            layout.addView(tv_cantidad, 0);
            layout.addView(v_inea, 0);

            set.clone(layout);

            //contrain del nombre
            set.connect(tv_nombre.getId(), ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 10);
            set.connect(tv_nombre.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);
            set.connect(tv_nombre.getId(), ConstraintSet.RIGHT, tv_separador_1.getId(), ConstraintSet.LEFT);

            //constrain separador 1
            set.connect(tv_separador_1.getId(), ConstraintSet.TOP, tv_nombre.getId(), ConstraintSet.TOP);
            set.connect(tv_separador_1.getId(), ConstraintSet.LEFT, tv_nombre.getId(), ConstraintSet.RIGHT);
            set.connect(tv_separador_1.getId(), ConstraintSet.RIGHT, tv_correo.getId(), ConstraintSet.LEFT);

            //constrain correo
            set.connect(tv_correo.getId(), ConstraintSet.TOP, tv_separador_1.getId(), ConstraintSet.TOP);
            set.connect(tv_correo.getId(), ConstraintSet.LEFT, tv_separador_1.getId(), ConstraintSet.RIGHT);
            set.connect(tv_correo.getId(), ConstraintSet.RIGHT, tv_separador_2.getId(), ConstraintSet.LEFT);

            //constrain separador 2
            set.connect(tv_separador_2.getId(), ConstraintSet.TOP, tv_correo.getId(), ConstraintSet.TOP);
            set.connect(tv_separador_2.getId(), ConstraintSet.LEFT, tv_correo.getId(), ConstraintSet.RIGHT);
            set.connect(tv_separador_2.getId(), ConstraintSet.RIGHT, tv_cantidad.getId(), ConstraintSet.LEFT);

            //constrain cantidad
            set.connect(tv_cantidad.getId(), ConstraintSet.TOP, tv_separador_2.getId(), ConstraintSet.TOP);
            set.connect(tv_cantidad.getId(), ConstraintSet.LEFT, tv_separador_2.getId(), ConstraintSet.RIGHT);
            set.connect(tv_cantidad.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);

            //contrain linea view
            set.connect(v_inea.getId(), ConstraintSet.TOP, tv_nombre.getId(), ConstraintSet.BOTTOM);

            set.applyTo(layout);

            id_nombre_anterior = i;
        }

        set.connect(R.id.bt_enviar_excel_influencers, ConstraintSet.TOP, id_nombre_anterior, ConstraintSet.BOTTOM, 20);
        set.applyTo(layout);
    }

    private View.OnClickListener enviar_excel_influencers() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Enviado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

}
