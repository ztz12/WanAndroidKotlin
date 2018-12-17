package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.login

import android.os.Bundle
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseActivity
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.login.LoginEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData
import com.example.zhangtianzhu.wanandroidkotlin.contract.login.RegisterContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.login.RegisterPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.MainActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity

class RegisterActivity : BaseMvpActivity<RegisterContract.View,RegisterContract.Presenter>(),RegisterContract.View {

    override fun createPresenter(): RegisterContract.Presenter = RegisterPresenter()
    private var user :String by Preference(Constants.USERNAME,"")

    private val mDialog by lazy { DialogUtil.getWaitDialog(this,getString(R.string.register_ing)) }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initData() {
    }

    override fun getData() {
        register_btn.setOnClickListener(onClickListener)
        register_toolbar.setOnClickListener(onClickListener)
        tv_login.setOnClickListener(onClickListener)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

    override fun getRegisterData(loginData: LoginData) {
        DialogUtil.showSnackBar(this,"注册成功!")
        isLogin = true
        user = loginData.username
        RxBus.default.post(LoginEvent(true))
        startActivity<MainActivity>()
        finish()
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if(mDialog!=null&&mDialog.isShowing){
            mDialog.dismiss()
        }
    }

    private val onClickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.register_btn->
                        register()
                R.id.register_toolbar->
                        finish()
                R.id.tv_login -> {
                    startActivity<LoginActivity>()
                    finish()
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }
            }
    }

    private fun register(){
        if(validate()){
            mPresenter?.registerWanAndroid(register_account_edit.text.toString(),
                                            register_password_edit.text.toString(),
                                            register_repassword_edit.text.toString())
        }
    }

    private fun validate(): Boolean {
        var valid = true
        val username: String = register_account_edit.text.toString()
        val password: String = register_password_edit.text.toString()
        val repassword: String = register_repassword_edit.text.toString()

        if (username.isEmpty()) {
            register_account_edit.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            register_password_edit.error = getString(R.string.password_not_empty)
            valid = false
        }
        if(password != repassword){
            register_repassword_edit.error = getString(R.string.password_not_equals_repassword)
            valid = false
        }
        if (repassword.isEmpty()) {
            register_repassword_edit.error = getString(R.string.repassword_not_empty)
            valid = false
        }
        return valid

    }

}
