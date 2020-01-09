package app.appworks.school.stylish

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import app.appworks.school.stylish.ad.AdFragment
import app.appworks.school.stylish.data.source.remote.StylishRemoteDataSource
import app.appworks.school.stylish.databinding.*
import app.appworks.school.stylish.dialog.MessageDialog
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.login.LoginDialog
import app.appworks.school.stylish.login.UserManager
import app.appworks.school.stylish.network.StylishApiFilter
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort
import app.appworks.school.stylish.userlogin.UserLoginDialog
import app.appworks.school.stylish.util.CurrentFragmentType
import app.appworks.school.stylish.util.DrawerToggleType
import app.appworks.school.stylish.util.Logger
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class MainActivity : BaseActivity() {

    /**
     * Lazily initialize our [MainViewModel].
     */
    val viewModel by viewModels<MainViewModel> { getVmFactory() }



//    private lateinit var binding1:FragmentCatalogBinding

//    private lateinit var binding2:DialogCommercialAdBinding


    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var targetID = 0

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    targetID = R.id.navigation_home
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToHomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_catalog -> {
                    targetID = R.id.navigation_catalog
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCatalogFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_cart -> {
                    targetID = R.id.navigation_cart
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCartFragment())
                    return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    targetID = R.id.navigation_profile
                    when (viewModel.isLoggedIn) {
                        true -> {
                            findNavController(R.id.myNavHostFragment).navigate(
                                NavigationDirections.navigateToProfileFragment(
                                    viewModel.user.value
                                )
                            )
                        }
                        false -> {
                            findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEmailLoginDialog())
                            return@OnNavigationItemSelectedListener false
                        }
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    // get the height of status bar from system
    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    private var userLogin:UserLoginDialog? = null

    private var userLoginAndShowAd: AdFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LogoActivity::class.java))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel







        // observe current fragment change, only for show info
        viewModel.currentFragmentType.observe(this, Observer {
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
            Logger.i("[${viewModel.currentFragmentType.value}]")
            Logger.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        })

        viewModel.navigateToLoginSuccess.observe(this, Observer {
            it?.let {
                findNavController(R.id.myNavHostFragment).navigate(
                    NavigationDirections.navigateToMessageDialog(MessageDialog.MessageType.LOGIN_SUCCESS)
                )
                
                viewModel.onLoginSuccessNavigated()


                // navigate to profile after login success
                when (viewModel.currentFragmentType.value) {
                    CurrentFragmentType.CART -> {
                        if (targetID == R.id.navigation_profile) {
                            viewModel.navigateToProfileByBottomNav(it)
                        }
                    }
                    else -> {
                        if(targetID == R.id.navigation_profile) {
                            viewModel.navigateToProfileByBottomNav(it)
                        }
                    }
                }
            }
        })

        viewModel.navigateToProfileByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_profile
                viewModel.onProfileNavigated()
            }
        })

        viewModel.navigateToHomeByBottomNav.observe(this, Observer {
            it?.let {
                binding.bottomNavView.selectedItemId = R.id.navigation_home
                viewModel.onHomeNavigated()
            }
        })





        setupToolbar()
        setupBottomNav()
        setupDrawer()
        setupNavController()

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "RECORD VIEW"
//            val result = StylishRemoteDataSource.userSignIn("abc@gmail.com", "name")
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.userSignIn}")
//
//
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }


        val testtoken = "9c9ddeea44206d5eb1ec1827a4e55efe3e221cbcca6abc089ef6ba9bb37e740e"
        val dummyToken = "2f7c7900c565ae05f9e8cae6b87828778d98b494b36b8db2f361e041c243a72a"

        /**
         * GROUPBUY
         */

        /**
         * FETCH GROUPBUY
         */

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "JOIN GROUP BUY"
//            val result = StylishRemoteDataSource.getGroupBuys("11022fcae1ee7cc9097c24eecd60950839fc467762ae78a433441acdb0c8ecbb")
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.data}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /**
         * JOIN GROUPBUY
         */

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "JOIN GROUP BUY"
//            val result = StylishRemoteDataSource.updateGroupBuy("11022fcae1ee7cc9097c24eecd60950839fc467762ae78a433441acdb0c8ecbb", 201902191245)
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.success}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }


        /**
         * CREATE GROUPBUY
         */
//                CoroutineScope(Dispatchers.Main).launch {
//                        val tag = "GROUP BUY"
//            val result = StylishRemoteDataSource.createGroupBuy(AddGroupBuyBody("11022fcae1ee7cc9097c24eecd60950839fc467762ae78a433441acdb0c8ecbb", 201807201824, "test12@test.com", "test13@test.com"))
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.groupID}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /***
         * CHATBOT
         */

//        CoroutineScope(Dispatchers.Main).launch {
//                        val tag = "CHAT BOT"
//            val result = StylishRemoteDataSource.getReplyFromChatbot(ChatbotBody(2, 201807242222, 178.0, 70.0))
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.reply}, ACTIVITY : ${result.data.activities}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /***
         * AD
         */
//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "GET AD"
//            val result = StylishRemoteDataSource.getAd()
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.ad}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /** Get Product Detail*/
//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "GET PRODUCT DETAIL"
//            val result = StylishRemoteDataSource.getProductDetail("f272145222f587ee40e63f9c1c6161f8d990073efb6146250566a677e6fe8bb5", getString(Currency.JPY.abbRes), "201807201824")
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.product}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /**Fetch Product List*/

//        binding1.fragmentCatalogFilterView.spinner_filter.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                when(view?.id){
//                    0 -> StylishRemoteDataSource.getProductList("men", null, Sort.POPULARITY, Order.ASCEND)
//                    1 -> StylishRemoteDataSource.getProductList("men", null, Sort.POPULARITY, Order.DESCEND)
//                    2 -> StylishRemoteDataSource.getProductList("men", null, Sort.PRICE, Order.ASCEND)
//                    3 -> StylishRemoteDataSource.getProductList("men", null, Sort.PRICE, Order.DESCEND)
//                }
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//
//            }
//
//        }



//        //price des
//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "FETCH PRODUCT LIST"
//            val result = StylishRemoteDataSource.getProductList("men", null, Sort.PRICE, Order.DESCEND)
//
//            when(result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.products}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//        }

        /** User View Record
         * "f272145222f587ee40e63f9c1c6161f8d990073efb6146250566a677e6fe8bb5"
         * */
//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "RECORD VIEW"
//            val result = StylishRemoteDataSource.getUserViewingRecord(testtoken)
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.records}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "RECORD VIEW"
//            val result = StylishRemoteDataSource.userSignUp("name", "abc@gmail.com", "name")
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.userSignUp}")
//
//
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /** name@gmail.com, name, 12345
         * "9c9ddeea44206d5eb1ec1827a4e55efe3e221cbcca6abc089ef6ba9bb37e740e"
         */

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "RECORD VIEW"
//            val result = StylishRemoteDataSource.userSignIn( "name@gmail.com", "12345")
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.userSignIn}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//
//        }

        /**
         * Coupon API - 查詢、新增、修改
         * Still need some fix
         */

//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "COUPON GET"
//            val result = StylishRemoteDataSource.getAvaliableCoupons(testtoken)
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.coupons}")
//                        Log.i(tag,"MESSAGE : ${result.data.message}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//        }

        /***
         * Coupon API 新增
         * Still need to test, due to HTTP 500
         */
//        CoroutineScope(Dispatchers.Main).launch {
//            val tag = "COUPON PUT"
//            val result = StylishRemoteDataSource.addNewCoupons(dummyToken, 8)
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.coupons}")
//                        Log.i(tag,"MESSAGE : ${result.data.message}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//        }

        /**
         * GET USER PROFILE
         * */
//        CoroutineScope(Dispatchers.Main).launch {
//                        val tag = "GET USER PROFILE"
//            val result = StylishRemoteDataSource.getUserProfile(dummyToken)
//
//            when (result) {
//                is Result.Success -> {
//                    if (result.data.error != null) {
//                        Log.i(tag, "ERROR : ${result.data.error}")
//                    } else {
//                        Log.i(tag, "RESULT : ${result.data.user?.coupons}")
//                        Log.i(tag,"MESSAGE : ${result.data.user?.viewedProduct}")
//                    }
//                }
//
//                is Result.Error -> {
//                    Log.i(tag, "ERROR : ${result.exception.message}")
//                }
//
//                is Result.Fail -> {
//                    Log.i(tag, "FAIL : ${result.error}")
//                }
//            }
//        }

    } //closing curly brace of fun onCreate

    var isFirstTime = true
    override fun onResume() {
        super.onResume()

        if (isFirstTime) {
            if(UserManager.isLoggedIn) {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalAdFragment())
            } else {
                userLogin = UserLoginDialog{shouldSignInOrSignUp ->
                    if (shouldSignInOrSignUp) {
                        // show dialog for signin/signup
                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEmailLoginDialog())
//                        if(UserManager.isLoggedIn){

//                            userLoginAndShowAd =  AdFragment{shouldShowAd:Boolean ->
//                                if(shouldShowAd==true) {
//                                    findNavController(R.id.myNavHostFragment).navigate(
//                                        NavigationDirections.actionGlobalAdFragment()
//                                    )
//                                }
//
//
//                            }
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalAdFragment())
//                        }
                    } else {
                        // fetch ad and displays it
                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalAdFragment())
                    }
                }

                // FragmentManager is a Interface for interacting with Fragment objects inside of an Activity
                userLogin?.show(supportFragmentManager, "tag")

            }
        }

        isFirstTime = false

    }


    /**
     * Set up [BottomNavigationView], add badge view through [BottomNavigationMenuView] and [BottomNavigationItemView]
     * to display the count of Cart
     */
    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val menuView = binding.bottomNavView.getChildAt(0) as BottomNavigationMenuView
        val itemView = menuView.getChildAt(2) as BottomNavigationItemView
        val bindingBadge = BadgeBottomBinding.inflate(LayoutInflater.from(this), itemView, true)
        bindingBadge.lifecycleOwner = this
        bindingBadge.viewModel = viewModel
    }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment, it better than another design
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView]
     */
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.catalogFragment -> CurrentFragmentType.CATALOG
                R.id.cartFragment -> CurrentFragmentType.CART
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.detailFragment -> CurrentFragmentType.DETAIL
                R.id.paymentFragment -> CurrentFragmentType.PAYMENT
                R.id.checkoutSuccessFragment -> CurrentFragmentType.CHECKOUT_SUCCESS
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    /**
     * Set up the layout of [Toolbar], according to whether it has cutout
     */
    private fun setupToolbar() {

        binding.toolbar.setPadding(0, statusBarHeight, 0, 0)

        launch {

            val dpi = resources.displayMetrics.densityDpi.toFloat()
            val dpiMultiple = dpi / DisplayMetrics.DENSITY_DEFAULT

            val cutoutHeight = getCutoutHeight()

            Logger.i("====== ${Build.MODEL} ======")
            Logger.i("$dpi dpi (${dpiMultiple}x)")
            Logger.i("statusBarHeight: ${statusBarHeight}px/${statusBarHeight / dpiMultiple}dp")

            when {
                cutoutHeight > 0 -> {
                    Logger.i("cutoutHeight: ${cutoutHeight}px/${cutoutHeight / dpiMultiple}dp")

                    val oriStatusBarHeight =
                        resources.getDimensionPixelSize(R.dimen.height_status_bar_origin)

                    binding.toolbar.setPadding(0, oriStatusBarHeight, 0, 0)
                    val layoutParams = Toolbar.LayoutParams(
                        Toolbar.LayoutParams.WRAP_CONTENT,
                        Toolbar.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.gravity = Gravity.CENTER
                    layoutParams.topMargin = statusBarHeight - oriStatusBarHeight
                    binding.imageToolbarLogo.layoutParams = layoutParams
                    binding.textToolbarTitle.layoutParams = layoutParams
                }
            }
            Logger.i("====== ${Build.MODEL} ======")
        }
    }

    /**
     * Set up [androidx.drawerlayout.widget.DrawerLayout] with [androidx.appcompat.widget.Toolbar]
     */
    private fun setupDrawer() {

        //FilterNavView swipe lock
//        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, binding.CatalogFilterNavView)

//        binding.drawerLayout.onInterceptTouchEvent({
//            MotionEvent()
//        })

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                when (UserManager.isLoggedIn) { // check user login status when open drawer
                    true -> {
                        viewModel.checkUser()
                    }
                    else -> {
                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEmailLoginDialog())//.navigateToLoginDialog())
                        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            binding.drawerLayout.closeDrawer(GravityCompat.START)
                        }
                    }
                }

            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }

        // Set up header of drawer ui using data binding
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false
        )

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

        // Observe current drawer toggle to set the navigation icon and behavior
        viewModel.currentDrawerToggleType.observe(this, Observer { type ->

            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
            binding.toolbar.setNavigationIcon(
                when (type) {
                    DrawerToggleType.BACK -> R.drawable.toolbar_back
                    else -> R.drawable.toolbar_menu
                }
            )
            actionBarDrawerToggle?.setToolbarNavigationClickListener {
                when (type) {
                    DrawerToggleType.BACK -> onBackPressed()
                    else -> {
                    }
                }
            }
        })
    }

    /**
     * override back key for the drawer design
     */
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    // setup what happens when filter selected

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when(item?.itemId){
//            R.id.popularity_ascending -> StylishApiFilter.SHOW_POPULARITY_ASCENDING
//            R.id.popularity_descending -> StylishApiFilter.SHOW_POPULARITY_DESCENDING
//            R.id.price_ascending -> StylishApiFilter.SHOW_PRICE_ASCENDING
//            R.id.price_descending -> StylishApiFilter.SHOW_PRICE_DESCENDING
//            R.id.price_range -> StylishApiFilter.SHOW_PRICE_RANGE
//            else -> StylishApiFilter.SHOW_ALL
//        }
//        return true
//    }



}
