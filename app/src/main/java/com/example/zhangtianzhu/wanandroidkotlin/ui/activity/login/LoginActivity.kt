package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.login.LoginEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData
import com.example.zhangtianzhu.wanandroidkotlin.contract.login.LoginContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.login.LoginPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.MainActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(),LoginContract.View {

    private val mPresenter :LoginPresenter by lazy { LoginPresenter() }

    private var user by Preference(Constants.USERNAME,"")

    private val mDialog  by lazy { DialogUtil.getWaitDialog(this,getString(R.string.login_ing)) }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData() {
        mPresenter.attachView(this)
        login_account_edit.setText(user)
    }

    override fun getData() {
        login_btn.setOnClickListener(onClickListener)
        login_toolbar.setOnClickListener(onClickListener)
        tv_create.setOnClickListener(onClickListener)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showLoginData(loginData: LoginData) {
        user = loginData.username
        isLogin = true
        isFirstIn = false
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        RxBus.default.post(LoginEvent(true))
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        when(view.id){
            R.id.login_btn->{
                login()
            }
            R.id.tv_create-> {
                val options = ActivityOptions.makeScaleUpAnimation(tv_create,
                        tv_create.length()/2,
                        tv_create.length()/2,
                        0,
                        0)
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java),options.toBundle())
            }
            R.id.login_toolbar->{
                finish()
            }
        }
    }

    private fun login(){
        if(validate()){
            mPresenter.loginWanAndroid(login_account_edit.text.toString(),login_password_edit.text.toString())
        }
    }

    private fun validate(): Boolean {
        var valid = true
        val username: String = login_account_edit.text.toString()
        val password: String = login_password_edit.text.toString()

        if (username.isEmpty()) {
            login_account_edit.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            login_password_edit.error = getString(R.string.password_not_empty)
            valid = false
        }
        return valid

    }

}
