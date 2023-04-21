package com.example.quizapp

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit


class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val RESPONSE_ENTRY_KEY="questions"
        private const val URL = "https://raw.githubusercontent.com/tVishal96/sample-english-mcqs/master/db.json"
        private const val RESPONSE_QUESTION_KEY="question"
        private const val RESPONCE_OPTIONS_KEY="options"
        private const val RESPONCE_CURRECT_OPTION_KEY="correct_option"
        const val PREF_NAME="shared_preferances"
        const val PREF_KEY="data_from_sharedpreferance"
        const val IS_PREFDATA="is_data_is_in_preferance"
    }
    private var app: Application
    private var queue: RequestQueue
    var questionNo=-1
    var hideNext = MutableLiveData<Boolean>()
    var hidePrevious = MutableLiveData<Boolean>()
    val questionListLiveData = MutableLiveData<ArrayList<QuestionModel>>()
    private var responseList=ArrayList<QuestionModel>()
    val question = MutableLiveData<QuestionModel>()
    private var time = MutableLiveData<Long>()
    var stringTime = MutableLiveData<String>()
    var completeTime = MutableLiveData<String>()
    var autoSubmit = MutableLiveData<Boolean>()
    var isSplashScreenCompeleted = MutableLiveData<Boolean>()
    var score = MutableLiveData<Int>()
    init {
        app=application
        queue=Volley.newRequestQueue(application)
        val pref = app.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        if (pref.getBoolean(IS_PREFDATA,false))
        {
            Log.d(TAG, ": taking previous session data")
            val previousData=pref.getString(PREF_KEY,null)
            if (previousData != null) {
                responseList = parseResponseArray(previousData)
                questionListLiveData.postValue(responseList)
            }else{
                Log.e(TAG, "List is empty: ")
            }
        }else{
            object: CountDownTimer(1500, 1000){
                override fun onTick(p0: Long) {
                    isSplashScreenCompeleted.value=false
                }
                override fun onFinish() {
                    isSplashScreenCompeleted.value=true
                }
            }.start()
            fetchQuestions()
        }
    }

    private fun fetchQuestions() {
        val stringRequest = StringRequest(Request.Method.GET, URL,
            { response ->
                // Display the first 500 characters of the response string
                responseList = parseResponseObject(response)
                responseList.shuffle()
                Log.d(TAG, "Data successfully fetch: "+responseList.size)
                questionListLiveData.postValue(responseList)
            },
            { Log.e(TAG, "$it") })

        queue.add(stringRequest)
    }


    fun reFetch()
    {
        autoSubmit.value = false
        fetchQuestions()
    }
    @Throws(JSONException::class)
    private fun parseResponseObject(response: String): ArrayList<QuestionModel> {
        val models: ArrayList<QuestionModel> = ArrayList()
        val res = JSONObject(response)
        val entries = res.optJSONArray(RESPONSE_ENTRY_KEY) ?: return models
        for (i in 0 until entries.length()) {
            val obj = entries[i] as JSONObject
            val question = obj.optString(RESPONSE_QUESTION_KEY)
            val optionArray = obj.optJSONArray(RESPONCE_OPTIONS_KEY)
            val option = ArrayList<String>()
            for (j in 0..3)
            {
                val op = optionArray?.get(j).toString()
                option.add(op)
            }
            val correctOptinOfQuestion = option[obj.optInt(RESPONCE_CURRECT_OPTION_KEY)]
            option.shuffle()
            val model = QuestionModel(question,option,correctOptinOfQuestion,0,0)
            models.add(model)
        }
        return models
    }
    @Throws(JSONException::class)
    private fun parseResponseArray(response: String): ArrayList<QuestionModel> {
        val models: ArrayList<QuestionModel> = ArrayList()
        val res = JSONArray(response)
        for (i in 0 until res.length())
        {
            val jObject = res.getJSONObject(i)
            val optionArray = ArrayList<String>()
            val jOptionArray = jObject.getJSONArray("answerArrayList")
            for (j in 0 until jOptionArray.length())
            {
                val option = jOptionArray[j].toString()
                optionArray.add(option)
            }
            val boolmark = jObject.get("bookMarkStatus")
            val correctOption=jObject.get("correctOption").toString()
            val question = jObject.get("question").toString()
            val usersAnswer = jObject.get("userAnswer")
            val model = QuestionModel(question,optionArray,correctOption ,usersAnswer as Int,boolmark as Int)
            models.add(model)
        }
        return models
    }
    fun getQustion(position:Int)
    {
        questionNo=position
        hidePrevious.value = questionNo==0
        hideNext.value = questionNo==(responseList.size-1)
        question.postValue(responseList[questionNo])
    }
    fun bookmarkChange()
    {
        if(responseList[questionNo].bookMarkStatus==0)
        {
            responseList[questionNo].bookMarkStatus=1
        }else
        {
            responseList[questionNo].bookMarkStatus=0
        }
        question.value = responseList[questionNo]
        questionListLiveData.value = responseList
    }
    fun optionSelected(selected:Int)
    {
        responseList[questionNo].userAnswer = selected
        questionListLiveData.value = responseList
    }
    fun getScoreOfQuiz(){
        var scoreCount=0
        for(i in 0 until (questionListLiveData.value?.size ?: 10))
        {
            var posi=questionListLiveData.value?.get(i)?.userAnswer?: -1
            posi-=1
            if(posi>=0&& questionListLiveData.value?.get(i)?.answerArrayList!![posi] == questionListLiveData.value?.get(i)?.correctOption)
            {
                scoreCount++
            }
        }
        score.value=scoreCount
    }
    fun setTime(timer:Long)
    {
        time.value = timer
        val string = convertTime(timer)
        stringTime.value = string
    }
    fun setCompleteTime(){
        completeTime.value=convertTime(600000- time.value!!)
    }
    private fun convertTime(timer:Long)= String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(timer) -
                TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(timer)),
        TimeUnit.MILLISECONDS.toSeconds(timer) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer)))

    override fun onCleared() {
        super.onCleared()
        if(time.value!=-1L&& autoSubmit.value == false)
        {
            Log.d(TAG, "session data stored")
            val gson = Gson()
            val listString = gson.toJson(responseList)
            val pref = app.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putString(PREF_KEY,listString)
            editor.putBoolean(IS_PREFDATA,true)
            editor.apply()
        }
    }
}