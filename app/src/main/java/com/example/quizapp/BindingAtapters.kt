package com.example.quizapp

import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setImage")
fun ImageView.setImage(status: Int){
    if(status==0)
    {
        this.setImageResource(R.drawable.baseline_bookmark_add)
        this.contentDescription="add bookmark"
    }else{
        this.setImageResource(R.drawable.baseline_bookmark_added)
        this.contentDescription="bookmark added"
    }
}

@BindingAdapter("setImage")
fun ImageButton.setImage(status: Int){
    if(status==0)
    {
        this.setImageResource(R.drawable.baseline_bookmark_add)
        this.contentDescription="add bookmark"
    }else{
        this.setImageResource(R.drawable.baseline_bookmark_added)
        this.contentDescription="bookmark added"
    }
}