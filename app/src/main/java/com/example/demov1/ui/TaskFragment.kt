package com.example.demov1.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demov1.R
import com.example.demov1.adapter.TaskAdapter
import com.example.demov1.databinding.FragmentTaskBinding
import com.example.demov1.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class TaskFragment : Fragment(R.layout.fragment_task) {

    private lateinit var adapter: TaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)


        adapter = TaskAdapter(
            onToggle = { _, _ ->
            },
            onClick = { task ->
                Toast.makeText(requireContext(), task.title, Toast.LENGTH_SHORT).show()
            }
        )

        binding.taskList.layoutManager = LinearLayoutManager(requireContext())
        binding.taskList.adapter = adapter

        loadTasks(binding)
    }

    private fun loadTasks(binding: FragmentTaskBinding) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val tasks = RetrofitClient.api.getTasks()
                adapter.submitList(tasks)
                binding.tvTaskTitle.text = "List công việc (${tasks.size})"
            } catch (e: Exception) {
                android.util.Log.e("TaskAPI", "Lỗi tải công việc", e)
                Toast.makeText(
                    requireContext(),
                    "Không tải được công việc: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
