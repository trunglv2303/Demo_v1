package com.example.demov1.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demov1.databinding.ItemTaskBinding
import com.example.demov1.data.model.Task

class TaskAdapter(
    private val tasks: MutableList<Task> = mutableListOf(),
    private val onToggle: (Task, Boolean) -> Unit,
    private val onClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun submitList(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.tvTodo.text = task.title

            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = task.completed
            updateStrike(task.completed)

            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                updateStrike(isChecked)
                onToggle(task, isChecked)
            }
            binding.root.setOnClickListener { onClick(task) }
        }

        private fun updateStrike(completed: Boolean) {
            binding.tvTodo.paintFlags = if (completed) {
                binding.tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.tvTodo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}
