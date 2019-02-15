package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseActivity
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.login.LoginEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.MainContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.MainPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.login.LoginActivity
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home.CollectFragment
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home.HomeFragment
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge.KnowledgeSystemFragment
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.navigation.NavigationFragment
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.project.ProjectFragment
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.wechat.WeChatFragment
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class MainActivity : BaseMvpActivity<MainContract.View,MainContract.Presenter>(), MainContract.View {

    private val BOTTOM_INDEX = "bottom_index"

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    private var fragmentList: MutableList<BaseFragment>? = null

    private var homeFragment: HomeFragment? = null
    private var knowledgeFragment: KnowledgeSystemFragment? = null
    private var weChatFragment: WeChatFragment? =null
    private var navigationFragment: NavigationFragment? = null
    private var projectFragment: ProjectFragment? = null
    private var collectFragment: CollectFragment? = null

    private var mLastIndex: Int = 0
    private var mIndex: Int = 0

    private val mDialog by lazy { DialogUtil.getWaitDialog(this, getString(R.string.loginOut_ing)) }

    private var tvUser: TextView? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        mPresenter?.registerEvent()
        fragmentList = mutableListOf()
        setSupportActionBar(common_toolbar)
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowTitleEnabled(false)
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        common_toolbar.setNavigationOnClickListener { onBackPressedSupport() }
    }

    override fun getData() {
        homeFragment = HomeFragment.getInstance("", "")
        fragmentList?.add(homeFragment!!)
        initFragment()
        common_toolbar_title_tv.text = getString(R.string.home_pager)
        navigation.run {
            tvUser = getHeaderView(0).findViewById(R.id.nav_header_login_tv)
        }
        switchFragment(mIndex)
        initBottomNavigation()
        initDrawerLayout()
        initNavigationView()
        intFab()
    }

    private fun initFragment() {
        knowledgeFragment = KnowledgeSystemFragment.getInstance("", "")
        weChatFragment = WeChatFragment.getInstance("","")
        navigationFragment = NavigationFragment.getInstance("", "")
        projectFragment = ProjectFragment.getInstance("", "")
        collectFragment = CollectFragment.getInstance("", "")

        fragmentList?.add(knowledgeFragment!!)
        fragmentList?.add(weChatFragment!!)
        fragmentList?.add(navigationFragment!!)
        fragmentList?.add(projectFragment!!)
        fragmentList?.add(collectFragment!!)

    }

    private fun initDrawerLayout() {
        val toggle = object : ActionBarDrawerToggle(
                this, drawer_layout, common_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            //侧栏滑动时
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                val mContent = drawer_layout.getChildAt(0)
                val scale = 1 - slideOffset
                val endScale = 0.8f + scale * 0.2f
                val startScale = 1 - 0.3f * scale

                //设置左边菜单滑动后的占据屏幕大小
                drawerView.scaleX = startScale
                drawerView.scaleY = startScale
                //设置菜单透明度
                drawerView.alpha = 0.6f + 0.4f * (1 - scale)

                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.translationX = drawerView.measuredWidth * (1 - scale)
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate()
                //设置右边菜单滑动后的占据屏幕大小
                mContent.scaleX = endScale
                mContent.scaleY = endScale
            }
        }
        toggle.syncState()
        drawer_layout.addDrawerListener(toggle)
    }

    private fun initBottomNavigation() {
        // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
        // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
        // BottomNavigationViewHelper.disableShiftMode(this)
        bottom_navigation_view.run {
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener(onNavigationItemReselectedListener)
        }

    }

    private val onNavigationItemReselectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when (item.itemId) {
                    R.id.tab_home_pager -> {
                        loadPage(getString(R.string.home_pager), 0)
                        true
                    }
                    R.id.tab_knowledge_hierarchy -> {
                        loadPage(getString(R.string.knowledge_hierarchy), 1)
                        true
                    }
                    R.id.tab_weChat ->{
                        loadPage(getString(R.string.weChat),2)
                        true
                    }
                    R.id.tab_navigation -> {
                        loadPage(getString(R.string.navigation), 3)
                        true
                    }
                    else -> {
                        loadPage(getString(R.string.project), 4)
                        true
                    }
                }

            }

    private fun initNavigationView() {
        tvUser?.run {
            text = if (!isLogin) {
                getString(R.string.login_in)
            } else {
                username
            }
            setOnClickListener {
                if (!isLogin) {
                    startActivity<LoginActivity>()
                }
            }
        }

        navigation.menu.findItem(R.id.nav_item_my_collect).setOnMenuItemClickListener {
            if (isLogin) {
                showCollectPage()
                collectFragment?.requestData()
            } else {
                DialogUtil.showSnackBar(this, getString(R.string.login_tint))
            }
            true
        }

        navigation.menu.findItem(R.id.nav_item_setting).setOnMenuItemClickListener {
            startActivity<SettingActivity>()
            true
        }

        navigation.menu.findItem(R.id.nav_item_home).setOnMenuItemClickListener {
            showHomePage()
            homeFragment?.changeData()
            true
        }

        navigation.menu.findItem(R.id.nav_item_about_us).setOnMenuItemClickListener {
            startActivity<AboutUsActivity>()
            true
        }

        navigation.menu.findItem(R.id.nav_item_todo).setOnMenuItemClickListener {
            if(isLogin) {
                startActivity<TodoActivity>()
            }else{
                DialogUtil.showSnackBar(this,getString(R.string.login_tint))
            }
            true
        }

        //退出登录
        navigation.menu.findItem(R.id.nav_item_logout).run {
            isVisible = isLogin
            setOnMenuItemClickListener {
                DialogUtil.getConfirmDialog(this@MainActivity, getString(R.string.logout_tint)
                        , DialogInterface.OnClickListener { _, _ ->
                    mDialog.show()
                    mPresenter?.loginOut()
                }).show()
                true
            }
        }
    }

    private fun intFab() {
        fab.setOnClickListener {
            when (mIndex) {
                0 -> homeFragment?.scrollTop()
                1 -> knowledgeFragment?.scrollTop()
                2 -> weChatFragment?.scrollTop()
                3 -> navigationFragment?.scrollTop()
                4 -> projectFragment?.scrollTop()
                5 -> collectFragment?.scrollTop()
            }

        }
    }

    private fun showCollectPage() {
        common_toolbar_title_tv.text = getString(R.string.collect)
        switchFragment(Constants.TYPE_COLLECT)
        navigation.menu.findItem(R.id.nav_item_my_collect).isCheckable = true
        drawer_layout.closeDrawers()
    }


    private fun showHomePage() {
        common_toolbar_title_tv.text = getString(R.string.home_pager)
        switchFragment(Constants.TYPE_HOME)
        bottom_navigation_view.selectedItemId = R.id.tab_home_pager
        navigation.menu.findItem(R.id.nav_item_home).isCheckable = true
        drawer_layout.closeDrawers()
    }

    private fun loadPage(title: String, position: Int) {
        common_toolbar_title_tv.text = title
        switchFragment(position)
    }

    @SuppressLint("RestrictedApi")
    private fun switchFragment(position: Int) {
        mIndex = position
        when(mIndex){
            0 -> common_toolbar_title_tv.text = getString(R.string.home_pager)
            1 -> common_toolbar_title_tv.text = getString(R.string.knowledge_hierarchy)
            2 -> common_toolbar_title_tv.text = getString(R.string.weChat)
            3 -> common_toolbar_title_tv.text = getString(R.string.navigation)
            else -> common_toolbar_title_tv.text = getString(R.string.project)
        }
        when {
            position < Constants.TYPE_COLLECT -> {
                fab.visibility = View.VISIBLE
                bottom_navigation_view.visibility = View.VISIBLE
            }
            else -> {
                bottom_navigation_view.visibility = View.INVISIBLE
            }
        }

        if (position >= fragmentList!!.size) {
            return
        }

        val transaction = supportFragmentManager.beginTransaction()
        val target = fragmentList!![position]
        val lastFrag = fragmentList!![mLastIndex]
        transaction.hide(lastFrag)
        mLastIndex = position
        if (!target.isAdded) {
            supportFragmentManager.beginTransaction().remove(target).commit()
            //这里不能使用transaction.replace方法，否则会出现切换回这个界面内容显示不出来，
            // 与LayoutManager android.support.v7.widget.LinearLayoutManager@229851b is already attached to a
            // RecyclerView: android.support.v7.widget 的异常
            transaction.add(R.id.fl_page, target)
        }
        transaction.show(target)
        transaction.commitAllowingStateLoss()
    }

    override fun showLoginView() {
        tvUser?.text = username
        navigation.menu.findItem(R.id.nav_item_logout).isVisible = true
        drawer_layout.closeDrawers()
        homeFragment?.changeData()
    }

    override fun showLoginOutView() {
        tvUser?.text = getString(R.string.login_in)
        navigation.menu.findItem(R.id.nav_item_logout).isVisible = false
        homeFragment?.changeData()
    }

    override fun acceptNightModel(isNightModel: Boolean) {
        if(isNightModel){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
        recreate()
    }

    override fun acceptLoadPhoto(isLoadPhoto: Boolean) {
        homeFragment?.changeData()
        projectFragment?.changeProjectData()
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this, msg!!)
    }

    override fun loadTopArticle() {
        homeFragment?.changeData()
    }

    override fun loginOutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                Preference.clearPreference()
                uiThread {
                    mDialog.dismiss()
                    isLogin = false
                    startActivity<LoginActivity>()
                    RxBus.default.post(LoginEvent(false))
                }
            }
        }
    }

    override fun updateColor() {
        super.updateColor()
        changeColor()
    }

    override fun changeColor() {
        fab.backgroundTintList = ColorStateList.valueOf(mThemeColor)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        if(savedInstanceState!=null){
            mIndex = savedInstanceState?.getInt(BOTTOM_INDEX)
        }
    }

    //保存当前Fragment位置，避免断网后，重新请求数据不对应当前底部导航栏
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BOTTOM_INDEX,mIndex)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_usage ->{
                startActivity<UseWebActivity>()
            }

            R.id.action_search -> {
                startActivity<SearchActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeFragment = null
        knowledgeFragment = null
        weChatFragment = null
        navigationFragment = null
        projectFragment = null
        collectFragment = null
    }

}
