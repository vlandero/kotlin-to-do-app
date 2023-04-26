package com.example.todolist

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val todos: MutableList<Todo>
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() { // adapter e un bridge between UI component and data source pentru a umple recyclerView
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) // wrapper around view that contains the layout for an individual item in the list
    //aici, View-ul este androidx.constraintlayout.widget.ConstraintLayout din item_todo.xml

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo, // item_todo.xml, pentru ca pe astea le folosim sa le punem direct pe recycle view de pe main activity
                parent, // parintele, in cazul asta recycle view din main activity
                false
            )
        )
    }

    private fun toggleStrikeThrough(tvToDoTitle: TextView, isChecked: Boolean){
        if(isChecked){
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        else{
            tvToDoTitle.paintFlags = tvToDoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    public fun addTodo(todo: Todo) {
        todos.add(todo) // nu se updateaza
        notifyItemInserted(todos.size - 1) // ii spunem pozitia la care am adaugat si sa updateze
    }

    @SuppressLint("NotifyDataSetChanged")
    public fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.checked
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) { // functie care se ruleaza pentru fiecare elem din viewHolder
        val curTodo = todos[position]
        holder.itemView.apply{
            val cbDone = findViewById<CheckBox>(R.id.cbDone)
            val tvTodoTitle = findViewById<TextView>(R.id.tvTodoTitle)
            val ivTrash = findViewById<ImageView>(R.id.ivTrash)
            tvTodoTitle.text = curTodo.title
            cbDone.isChecked = curTodo.checked
            toggleStrikeThrough(tvTodoTitle, curTodo.checked)
            cbDone.setOnCheckedChangeListener {_, isChecked ->
                toggleStrikeThrough(tvTodoTitle, isChecked)
                curTodo.checked = !curTodo.checked
            }
            ivTrash.setOnClickListener {
                todos.remove(curTodo)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size;
    }
}