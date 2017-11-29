package com.yapp.mrrabbit.yapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.concurrent.Callable;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MenuItem amiMegaphone;
    private MenuItem amiAnunciate;
    private MenuItem amiBuscar;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Menu actionMenu;
    public static Evento evento;
    private View dialogView;

    public int id_evento; //426806 evento michael Gratis //425960 evento cuco de pago -> para pruebas
    public static int id_usuario; //michael id es 700009 y cuco id 100298 -> para pruebas

    private IntentIntegrator qrScan;

    private EditText et_idUsuario;
    private EditText et_idEvento;
    private Button bt_logIn;

    private  Button bt_abrirLogIn;

    private AlertDialog dialog;



    //private MaterialSearchView searchView;
    private SearchView searchView;
    private static ProgressDialog progress;

    public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("");
        id_evento = 426806;//evento michael
        id_usuario = 700009;

        //id_evento = 431163;//evento michael 2
        //id_usuario = 700009;

        //id_evento = 425960;//evento cuco
        //id_usuario = 100298;

        //id_evento = 429391;//evento music fest
        //id_usuario = 700358;


        appContext = this;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        actionMenu = menu;
        amiMegaphone = menu.findItem(R.id.aim_megaphone);
        amiAnunciate = menu.findItem(R.id.ami_anunciate);
        amiBuscar = menu.findItem(R.id.ami_search);
        esconderItemsActionMenu();

        bt_logIn = (Button) findViewById(R.id.bt_abrirLogIn);
        bt_logIn.setOnClickListener(clickAbrirLogIn());

        abrirLogIn();

        //searchView.setMenuItem(amiBuscar);
        //searchView.setCursorDrawable(R.drawable.custom_cursor);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.ami_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("tiquete, # de compra");

        searchViewEvents();
        return true;
    }

    public void abrirLogIn(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        dialogView = getLayoutInflater().inflate(R.layout.log_in, null);

        et_idEvento = (EditText) dialogView.findViewById(R.id.et_idEvento);
        et_idUsuario = (EditText) dialogView.findViewById(R.id.et_idUsuario);
        bt_logIn = (Button) dialogView.findViewById(R.id.bt_logIn);
        bt_logIn.setOnClickListener(clickLogIn());

        mBuilder.setView(dialogView);
        dialog = mBuilder.create();
        dialog.show();
    }

    public View.OnClickListener clickLogIn(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idUsuario = convertirNumero(et_idUsuario.getText().toString().trim());
                int idEvento = convertirNumero(et_idEvento.getText().toString().trim());
                if((idEvento+idUsuario)>0) {
                    dialog.dismiss();
                    id_usuario = idUsuario;
                    id_evento = idEvento;
                    abrirPerfilEvento();
                }else{
                    Toast.makeText(getApplicationContext(), "Debe ingresar solo numeros", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public View.OnClickListener clickAbrirLogIn(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirLogIn();
            }
        };
    }

    public int convertirNumero(String hilera){
        int numero = -1;
        try{
            numero = Integer.parseInt(hilera);
        }catch (NumberFormatException e){

        }
        return  numero;
    }

    public void abrirPerfilEvento(){
        fragment = new PerfilExperiencia();
        Callable<Void> setEventoPerfilExperiencia = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ((PerfilExperiencia)fragment).setEventoPerfil(evento);
                return null;
            }
        };
        cargarEventoById(id_evento, setEventoPerfilExperiencia);
        //amiMegaphone.setVisible(true);
        amiAnunciate.setVisible(true);
    }

    public void searchViewEvents(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    //setSearchviewTextSize(searchView,12);
                }else{
                    //setSearchviewTextSize(searchView,17);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                //textView.setText(query);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.ami_search:
                break;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;
        esconderItemsActionMenu();
        if (id == R.id.nav_perfil) {
            abrirPerfilEvento();
        } else if (id == R.id.nav_escaner_qr) {
            fragment = new Escanear();
            Callable<Void> setEventoEscanear = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ((Escanear)fragment).setEvento(evento);
                    return null;
                }
            };
            cargarEventoById(id_evento, setEventoEscanear);
            //amiBuscar.setVisible(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargarEventoById(final int idEvento, final Callable method){
        displayDialogLoading();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                getEventoById(idEvento);
                getTipoTiquetesEvento(idEvento);
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissLoading();
                abrirFragment();
                Thread.currentThread().interrupt();
            }
        });
        t.start();
    }

    public void esconderItemsActionMenu(){
        amiMegaphone.setVisible(false);
        amiAnunciate.setVisible(false);
        amiBuscar.setVisible(false);
    }

    public void displayDialogLoading(){
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    public void getEventoById(final int id){
        DataAccess da = new DataAccess();
        evento = da.getInfoEventoSales(id);
    }

    public void getTipoTiquetesEvento(final int id){
        if(evento!=null){
            DataAccess da = new DataAccess();
            evento.setTipoTiquetes(da.getTipoTiquetesEvento(id));
            int[] lvi = da.getLikesClicksImpresionesEvento(id);
            evento.setImpresiones(lvi[0]);
            evento.setClicks(lvi[1]);
            evento.setLikes(lvi[2]);
        }
    }

    public void dismissLoading(){
        if(progress!=null) {
            progress.dismiss();
        }
    }

    public void abrirFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
    }

    public MenuItem getAmiMegaphone() {
        return amiMegaphone;
    }

    public void setAmiMegaphone(MenuItem amiMegaphone) {
        this.amiMegaphone = amiMegaphone;
    }

    public MenuItem getAmiAnunciate() {
        return amiAnunciate;
    }

    public void setAmiAnunciate(MenuItem amiAnunciate) {
        this.amiAnunciate = amiAnunciate;
    }

    public MenuItem getAmiBuscar() {
        return amiBuscar;
    }

    public void setAmiBuscar(MenuItem amiBuscar) {
        this.amiBuscar = amiBuscar;
    }

}

