package com.example.quizapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.databinding.FragmentQuestionDetailScreenBinding


class QuestionDetailScreenFragment : Fragment() {
    private lateinit var queViewModel: QuestionsViewModel
    private lateinit var binding: FragmentQuestionDetailScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queViewModel = ViewModelProvider(requireActivity())[QuestionsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_question_detail_screen,container,false)
        binding.viewModel = queViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            Log.d(TAG, "next question: ")
            binding.optionGroup.clearCheck()
            queViewModel.getQustion(queViewModel.questionNo + 1)
        }
        binding.buttonPrevious.setOnClickListener {
            Log.d(TAG, "previous question: ")
            binding.optionGroup.clearCheck()
            queViewModel.getQustion(queViewModel.questionNo - 1)
        }
        binding.optionGroup.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.first_option ->{
                    queViewModel.optionSelected(1)
                }
                R.id.second_option ->{
                    queViewModel.optionSelected(2)
                }
                R.id.third_option ->{
                    queViewModel.optionSelected(3)
                }
                R.id.fourth_option ->{
                    queViewModel.optionSelected(4)
                }
            }
            Log.d(TAG, "Answer Marked: ")
        }
    }
}