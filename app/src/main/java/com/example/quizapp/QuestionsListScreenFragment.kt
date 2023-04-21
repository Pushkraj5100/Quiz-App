package com.example.quizapp

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.FragmentQuestionsListScreenBinding

class QuestionsListScreenFragment : Fragment() {
    private lateinit var queViewModel: QuestionsViewModel
    private lateinit var progressDialog: ProgressDialog
    lateinit var binding: FragmentQuestionsListScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queViewModel = ViewModelProvider(requireActivity())[QuestionsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_questions_list_screen,container,false)
        binding.viewModel = queViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        progressDialog=ProgressDialog(activity)
        progressDialog.setMessage("Please Wait!!!")
        progressDialog.show()
        setupLiveData()
        return binding.root
    }

    private fun setupLiveData(){
        queViewModel.questionListLiveData.observe(viewLifecycleOwner, Observer {
            handlequestionList(it)
        })
    }
    private fun handlequestionList(questionModels: ArrayList<QuestionModel>) {
        val adapter = QuestionAdapter(questionModels, context)
        binding.recyclerviewQuestion.layoutManager=LinearLayoutManager(activity)
        binding.recyclerviewQuestion.adapter=adapter
        progressDialog.dismiss()
    }

}