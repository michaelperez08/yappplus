package com.yapp.mrrabbit.yapp;

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
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MenuItem amiMegaphone;
    private MenuItem amiAnunciate;
    private MenuItem amiBuscar;

    private Menu actionMenu;

    //private MaterialSearchView searchView;
    private SearchView searchView;

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

        //searchView = (MaterialSearchView) findViewById(R.id.search_view);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        Fragment fragment = null;
        esconderItemsActionMenu();

        if (id == R.id.nav_perfil) {
            fragment = new PerfilExperiencia();
            amiMegaphone.setVisible(true);
            amiAnunciate.setVisible(true);
        } else if (id == R.id.nav_escaner_qr) {
            fragment = new Escanear();
            amiBuscar.setVisible(true);
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void esconderItemsActionMenu(){
        amiMegaphone.setVisible(false);
        amiAnunciate.setVisible(false);
        amiBuscar.setVisible(false);
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
