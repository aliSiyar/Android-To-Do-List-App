package com.alisiyararslan.todolist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.alisiyararslan.todolist.databinding.RecyclerRowTaskBinding

import com.alisiyararslan.todolist.model.Task
import com.alisiyararslan.todolist.roomdb.TaskDao
import com.alisiyararslan.todolist.roomdb.TaskDatabase
import com.alisiyararslan.todolist.view.TaskDetailFragmentDirections
import com.alisiyararslan.todolist.view.TaskListFragmentDirections
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskAdapter(val taskList: List<Task>,var db: TaskDatabase,var taskDao: TaskDao): RecyclerView.Adapter<TaskAdapter.TaskHolder>() {


    class TaskHolder(val binding: RecyclerRowTaskBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val binding = RecyclerRowTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        if(taskList.get(position).taskDescripion.length > 60){

            holder.binding.todoText.setText(taskList.get(position).taskDescripion.subSequence(0,60).toString() + "...")
        }else{
            holder.binding.todoText.setText(taskList.get(position).taskDescripion)
        }

        holder.binding.todoChechBox.setChecked(taskList.get(position).isCompleted)




        holder.binding.todoChechBox.setOnClickListener{
            var compositeDisposible= CompositeDisposable()
            taskList.get(position).isCompleted = !taskList.get(position).isCompleted
            compositeDisposible.add(
                taskDao.update(taskList.get(position))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }

        holder.binding.todoText.setOnClickListener{
            val action=TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(taskList.get(position))
            Navigation.findNavController(it).navigate(action)
        }




    }
}