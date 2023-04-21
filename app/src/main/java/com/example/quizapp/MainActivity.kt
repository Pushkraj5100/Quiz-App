package com.example.quizapp

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

private const val SAVE_INSTANCE = "save_instance"
const val TAG = "#####"

class MainActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var questionViewModel: QuestionsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questionViewModel = ViewModelProvider(this)[QuestionsViewModel::class.java]
        val pref = getSharedPreferences(QuestionsViewModel.PREF_NAME, Context.MODE_PRIVATE)
        if (savedInstanceState != null) {
            Log.d(TAG, "Rotate the device: ")
            when (savedInstanceState.getInt(SAVE_INSTANCE, 0)) {
                1 -> {
                    addFragmnet(
                        fragment = SplashScreenFragment(),
                        addToBackStack = false,
                        replaceFragment = false
                    )
                }
                2 -> {
                    afterSplashScreen()
                }
                3 -> {
                    afterStartButton()
                }
                4 -> {
                    addFragmnet(
                        QuestionDetailScreenFragment(),
                        addToBackStack = true,
                        replaceFragment = true
                    )
                }
                5 -> {
                    addFragmnet(
                        SummaryScreenFragment(),
                        addToBackStack = false,
                        replaceFragment = true
                    )
                }
            }
        } else if (pref.getBoolean(QuestionsViewModel.IS_PREFDATA, false)) {
            Log.d(TAG, "session is already running: ")
            afterStartButton()
        } else {
            addFragmnet(
                fragment = SplashScreenFragment(),
                addToBackStack = false,
                replaceFragment = false
            )

            questionViewModel.isSplashScreenCompeleted.observe(this, Observer {
                if(it)
                {
                    afterSplashScreen()
                }
            })
        }
        val counterReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                questionViewModel.setTime(p1?.getLongExtra(TimeService.KEY, -1) ?: -1)
                questionViewModel.autoSubmit.value =
                    p1?.getBooleanExtra(TimeService.FINISH_KEY, false)
            }
        }
        val filter = IntentFilter(TimeService.PACKAGE)
        registerReceiver(counterReceiver, filter)

        questionViewModel.autoSubmit.observe(this, Observer {
            if (it) {
                Log.d(TAG, "Test is autosubmitted: ")
                afterSubmitButton()
            }
        })

    }

    private fun addFragmnet(fragment: Fragment, addToBackStack: Boolean, replaceFragment: Boolean) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        if (replaceFragment) {
            transaction.replace(R.id.fragment_container, fragment)
        } else {
            if (manager.findFragmentById(R.id.fragment_container) !is SplashScreenFragment) {
                transaction.add(R.id.fragment_container, fragment)
            }
        }
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun afterSplashScreen() {
        addFragmnet(SetupScreenFragment(), addToBackStack = false, replaceFragment = true)
    }

    private fun afterStartButton() {
        addFragmnet(QuestionsListScreenFragment(), addToBackStack = false, replaceFragment = true)
        if (!applicationContext.isMyServiceRunning(TimeService::class.java)) {
            startService()
        }
    }
    fun afterStartButtonWithView(view: View)
    {
        afterStartButton()
    }

    private fun afterSubmitButton() {
        stopService()
        val pref = getSharedPreferences(QuestionsViewModel.PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(QuestionsViewModel.PREF_KEY, null)
        editor.putBoolean(QuestionsViewModel.IS_PREFDATA, false)
        editor.apply()
        questionViewModel.getScoreOfQuiz()
        questionViewModel.setCompleteTime()
        addFragmnet(SummaryScreenFragment(), addToBackStack = false, replaceFragment = true)
    }

    fun afterResetButton(view: View) {
        stopService()
        questionViewModel.reFetch()
        questionViewModel.setTime(-1)
        afterSplashScreen()
    }

    override fun onItemClick(position: Int) {
        questionViewModel.getQustion(position)
        addFragmnet(QuestionDetailScreenFragment(), addToBackStack = true, replaceFragment = true)
    }

    private fun startService() {
        val serviceIntent = Intent(this, TimeService::class.java)
        startService(serviceIntent)
    }

    private fun stopService() {
        val serviceIntent = Intent(this, TimeService::class.java)
        stopService(serviceIntent)
    }

    private fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) is SummaryScreenFragment) {
            finish()
        } else if (supportFragmentManager.findFragmentById(R.id.fragment_container) is QuestionDetailScreenFragment) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        val manager = supportFragmentManager
        when (manager.findFragmentById(R.id.fragment_container)) {
            is SplashScreenFragment -> {
                outState.putInt(SAVE_INSTANCE, 1)
            }
            is SetupScreenFragment -> {
                outState.putInt(SAVE_INSTANCE, 2)
            }
            is QuestionsListScreenFragment -> {
                outState.putInt(SAVE_INSTANCE, 3)
            }
            is QuestionDetailScreenFragment -> {
                outState.putInt(SAVE_INSTANCE, 4)
            }
            is SummaryScreenFragment -> {
                outState.putInt(SAVE_INSTANCE, 5)
            }
        }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    fun submitPressed(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this).setTitle("Submit Quiz")
        builder.setMessage("Are you sure you want to Submit the test ?")
        builder.setPositiveButton("Yes",
            DialogInterface.OnClickListener { _, _ ->
                questionViewModel.autoSubmit.value=true
                afterSubmitButton()
            })

        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { _, _ -> })

        builder.show()
    }
}