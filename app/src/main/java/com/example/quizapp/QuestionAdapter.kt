package com.example.quizapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.RecyclerviewItemBinding

class QuestionAdapter(private var mList :ArrayList<QuestionModel>, val context: Context?): RecyclerView.Adapter<QuestionAdapter.QuestionHolder>() {
    class QuestionHolder(val itemBinding: RecyclerviewItemBinding,itemClickListener: ItemClickListener?) : RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.setOnClickListener{
                itemClickListener?.onItemClick(adapterPosition)
            }
        }
    }
    private var listener=context as ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionHolder {
        val inflater = LayoutInflater.from(parent.context)
        return QuestionHolder(RecyclerviewItemBinding.inflate(inflater,parent,false),listener)
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: QuestionHolder, position: Int) {
        holder.itemBinding.question=mList[position]
    }
}