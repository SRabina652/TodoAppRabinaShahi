package com.example.final_todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.final_todo.adaptor.CategoryAdaptor;
import com.example.final_todo.viewmodel.CategoryViewModel;

public class MainActivity extends AppCompatActivity {

    CategoryViewModel categoryViewModel;
    RecyclerView categoryRecyclerView;
    CategoryAdaptor categoryAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toast.makeText(this, "Main Activity", Toast.LENGTH_SHORT).show();
        categoryViewModel  = new ViewModelProvider(this).get(CategoryViewModel.class);

    }
    public void replaceFragmentCategoryList(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryListFragment.class, null)
                .addToBackStack("CategoryList")
                .setReorderingAllowed(true)
                .commit();
    }

    public void replaceFragmentCategory(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_fragment_container, CategoryFragment.class, null)
                .addToBackStack("Category")
                .setReorderingAllowed(true)
                .commit();
    }
    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_newcategory:
                replaceFragmentCategory();
                return true;
            case R.id.main_menu_clear_category:
                categoryViewModel.deleteAllCategory();
                replaceFragmentCategory();
                return true;
            case R.id.main_menu_logout:
                SharedPreferences sharedPreferences =  getSharedPreferences("login", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return  true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        finish();

    }

}