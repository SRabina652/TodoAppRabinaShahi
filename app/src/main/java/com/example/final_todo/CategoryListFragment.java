package com.example.final_todo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.final_todo.adaptor.CategoryAdaptor;
import com.example.final_todo.model.Category;
import com.example.final_todo.viewmodel.CategoryViewModel;

import java.util.List;

public class CategoryListFragment extends Fragment implements CategoryAdaptor.OnTaskClickListner {

    CategoryViewModel categoryViewModel;
    RecyclerView categoryRecyclerView;
    Category category;
    Context context;
    CategoryAdaptor categoryAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getActivity().getApplicationContext(), "Category list Fragment Class", Toast.LENGTH_SHORT).show();

        category = new Category();
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        categoryAdaptor = new CategoryAdaptor(this::onItemClick);
        categoryViewModel.getCategoryList().observe(getActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdaptor.setCategoryList(categories);
                categoryAdaptor.notifyDataSetChanged();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
                categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                categoryRecyclerView.setAdapter(categoryAdaptor);

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (direction == ItemTouchHelper.LEFT) {
                            CategoryListFragment list = new CategoryListFragment();
                            categoryViewModel.deleteCategory(categoryAdaptor.getCategoryNote(viewHolder.getAdapterPosition()));
                            categoryRecyclerView.getRecycledViewPool().clear();
                            categoryAdaptor.notifyItemRemoved(categoryAdaptor.getUpdateCategory(viewHolder.getAdapterPosition()));
                            Toast.makeText(getActivity().getApplicationContext(), "CATEGORY DELETED", Toast.LENGTH_SHORT).show();
                        } else if (direction == ItemTouchHelper.RIGHT) {
                            int position = viewHolder.getAdapterPosition();
                            UpdateCategoryFragment updateCategoryFragment = new UpdateCategoryFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", categoryAdaptor.getCategoryPosition(position).getCategoryId());
                            bundle.putString("name", categoryAdaptor.getCategoryPosition(position).getCategory());
                            updateCategoryFragment.setArguments(bundle);
                            getParentFragmentManager().beginTransaction().replace(R.id.activity_main_fragment_container, updateCategoryFragment).addToBackStack(null).commit();
                        }

                    }
                }).attachToRecyclerView(categoryRecyclerView);
            }
        }, 100);
        return view;
    }


    @Override
    public void onItemClick(int position) {

    }

    private void DeleteAllCategory() {
        categoryViewModel.deleteCategory(category);
    }
}
