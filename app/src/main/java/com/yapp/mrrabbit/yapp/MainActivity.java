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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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


    private IntentIntegrator qrScan;

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
        int id_entrada = 426806; //426806 Gratis //425960 de pago -> para pruebas
        fragment = null;
        esconderItemsActionMenu();
        if (id == R.id.nav_perfil) {
            fragment = new PerfilExperiencia();
            Callable<Void> setEventoPerfilExperiencia = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ((PerfilExperiencia)fragment).setEventoPerfil(evento);
                    return null;
                }
            };
            cargarEventoById(id_entrada, setEventoPerfilExperiencia);
            //amiMegaphone.setVisible(true);
            amiAnunciate.setVisible(true);
        } else if (id == R.id.nav_escaner_qr) {
            fragment = new Escanear();
            Callable<Void> setEventoEscanear = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ((Escanear)fragment).setEvento(evento);
                    return null;
                }
            };
            cargarEventoById(id_entrada, setEventoEscanear);
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

