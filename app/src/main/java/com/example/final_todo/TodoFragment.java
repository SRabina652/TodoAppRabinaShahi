package com.example.final_todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.final_todo.model.Category;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.CategoryViewModel;
import com.example.final_todo.viewmodel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {

    Spinner categoryDropDownList;
    EditText txtTodoDate, txtTitle, txtDescription;
    RadioGroup rgPriority;
    CheckBox chkComplete;
    Button btnSave;
    CategoryViewModel categoryViewModel;
    TodoViewModel todoViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity().getApplicationContext(), "Todo Fragment Class", Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        categoryViewModel = new ViewModelProvider(getActivity()).get(CategoryViewModel.class);
        todoViewModel =  new ViewModelProvider(getActivity()).get(TodoViewModel.class);
        categoryDropDownList = view.findViewById(R.id.fragment_todo_cbo_category);
        txtTodoDate = view.findViewById(R.id.fragment_todo_txt_date);
        txtTitle = view.findViewById(R.id.fragment_todo_txt_title);
        txtDescription = view.findViewById(R.id.fragment_todo_txtDescription);
        rgPriority = view.findViewById(R.id.fragment_todo_rg_priority);
        chkComplete = view.findViewById(R.id.fragment_todo_chk_complete);
        btnSave = view.findViewById(R.id.fragment_todo_btn_Save);

        categoryViewModel.getCategoryList().observe(getViewLifecycleOwner(), categories -> {
            setCategorySpinner(categories);
        });

        txtTodoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return view;
    }

    private void setCategorySpinner(List<Category> categories) {
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, categories) ;
        categoryDropDownList.setAdapter(adapter);
    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                txtTodoDate.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void saveData() {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String todoDate = txtTodoDate.getText().toString();
        Category category = (Category) categoryDropDownList.getSelectedItem();
        int checkedRadio = rgPriority.getCheckedRadioButtonId();
        int priority = 0;
        switch (checkedRadio) {
            case R.id.fragment_todo_rb_high:
                priority = 0;
                break;
            case R.id.fragment_todo_rb_mid:
                priority = 1;
                break;
            case R.id.fragment_todo_rb_low:
                priority = 2;
                break;
        }

        if (!title.isEmpty() && !description.isEmpty()) {
            if (!todoDate.equals("Pick Date")) {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date todoDateOn = new Date();
                try {
                    todoDateOn = formatter.parse(todoDate);
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Issue with date", Toast.LENGTH_SHORT).show();
                }
                Date createdOn = new Date();
                boolean isComplete = chkComplete.isChecked();

                Todo todo = new Todo(
                        title,
                        description,
                        todoDateOn,
                        isComplete,
                        priority,
                        category.getCategoryId(),
                        createdOn
                );

                todoViewModel.saveTodo(todo);
                Toast.makeText(getActivity(), "Todo Saved", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Please select a date", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Please insert all values", Toast.LENGTH_SHORT).show();
        }
    }
}
