package com.example.final_todo.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.final_todo.R;
import com.example.final_todo.TodoListActivity;
import com.example.final_todo.UpdateTodo;
import com.example.final_todo.database.AppDatabase;
import com.example.final_todo.model.Todo;
import com.example.final_todo.viewmodel.TodoViewModel;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TodoAdaptor extends RecyclerView.Adapter<TodoAdaptor.TodoView> {
    private List<Todo> todoList;
    Context context;
    public void setTodoList(List<Todo> todoList, Context context) {
        this.context=context;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoitemlayout, parent, false);
        return new TodoView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoView holder, int position) {
            holder.tvTitle.setText(todoList.get(position).getTitle());
            holder.tvDescription.setText(todoList.get(position).getDescription());
            holder.tvComplete.setText(todoList.get(position).isCompleted() == true ? "Completed" : "Incomplete");
            int priority = todoList.get(position).getPriority();
            if(priority == 0)
                holder.tvPriority.setText(R.string.todo_high);
            else if(priority ==1)
                holder.tvPriority.setText(R.string.todo_medium);
            else
                holder.tvPriority.setText(R.string.todo_low);

            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            holder.tvDate.setText(dateFormat.format(todoList.get(position).getTodoDate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateTodo.class);
                    intent.putExtra("id",todoList.get(position).getTodoId());
                    intent.putExtra("todotitle",todoList.get(position).getTitle());
                    intent.putExtra("tvComplete",todoList.get(position).isCompleted());
                    intent.putExtra("priority",todoList.get(position).getPriority());
                    intent.putExtra("dateTime",todoList.get(position).getTodoDate());
                    intent.putExtra("description",todoList.get(position).getDescription());
                    intent.putExtra("createdOn",todoList.get(position).getCreatedOn());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

    }

    public Todo getNote(int position){
        return todoList.get(position);
    }

    public int getUpdateNote(int position){

        return todoList.get(position).getTodoId();
    }
    @Override
    public int getItemCount() {
        return (todoList == null ? 0 : todoList.size());
    }

    public class TodoView extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvComplete, tvDate, tvPriority;
        public TodoView(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.todo_item_tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.todo_item_tv_description);
            tvComplete = (TextView) itemView.findViewById(R.id.todo_item_tv_complete);
            tvDate = (TextView) itemView.findViewById(R.id.todo_item_tv_date);
            tvPriority = (TextView) itemView.findViewById(R.id.todo_item_tv_prority);
        }
    }
}