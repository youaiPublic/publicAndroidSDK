#SDK 接入文档

###1、开发环境搭建
    以下是以 Eclipse 为例,在 Eclipse 环境下开发的 SDK 配置<br>
    1.1、将 SDK 压缩包中 Jar 目录下的所有 jar 包复制到游戏工程的 libs 目录下<br>
    ![Alt text](../image/image1.png)
    1.2、将1.1中复制的jar包引用到游戏工程<br>
    1.3、复制SDK压缩包中assets目录下的所有内容到游戏工程的assets目录，将游戏中的闪屏图片放到assets中poolsdk_splash目录<br>		            下，将assets中的poolsdk.xml中的payCallbackUrl参数配置为游戏测试的充值回调地址（注：此回调地址为测试使用，正式<br>		           环境以SDK后台配置的地址为准）
    1.4、修改游戏工程的AndroidManifest.xml（可以参照复制Demo中AndroidManifest.xml文件）
        ①．添加声明权限：
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
 	    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
        <uses-permission android:name="android.permission.READ_LOGS" />
        <uses-permission android:name="android.permission.GET_TASKS" />
        
        ②．添加对应的Activity，service，receiver等：
        <!-- start YouaiSDK -->
        <receiver
            android:name="com.gzyouai.fengniao.sdk.framework.AppInstallReceiver"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.PACKAGE_ADDED" />
                <action android:name="android.intent.action.PACKAGE_REPLACED" />
                <action android:name="android.intent.action.PACKAGE_REMOVED" />
                <data android:scheme="package" />
            </intent-filter>
        </receiver>
        <!-- start i9133 -->
        <activity
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop" />
        <activity
            android:name="com.youai.sdk.YouaiSDK"
            android:label="youai_sdk"
            android:theme="@android:style/Theme.Dialog" >
        </activity>
        <service
            android:name="com.youai.sdk.YouaiService"
            android:enabled="true" >
            <intent-filter>
                <action android:name="com.youai.sdk.YouaiService" />
            </intent-filter>
        </service>
        <service
            android:name="com.youai.sdk.FloatViewService"
            android:enabled="true"
            android:exported="true" >
            <intent-filter>
                <action android:name="com.youai.sdk.FloatViewService" />
            </intent-filter>
        </service>
        <receiver android:name="com.youai.sdk.Receiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" >
                </action>
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </receiver>
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind" >
        </activity>
        <activity
            android:name="com.alipay.sdk.auth.AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind" >
        </activity>
        <!-- end i9133 -->
        
###2、功能接口
    (注意:以下所有接口都必须在 SDK 初始化完成之后才能调用)<br>
    2.1、继承 PoolSDKApplication(必接) <br>
        说明:<br>
        游戏工程如果没有Application,请在 AndroidManifest.xml 中添加SDK的Application,如下所示:<br>
        <application android:name="com.gzyouai.publicsdk.application.PoolSDKApplication"><br>
        游戏工程如果有 Application,请继承 SDK 中 PoolSDKApplication:
        public class XXXXApplication extends PoolSDKApplication { 
            @Override
            public void onCreate() {
                // TODO Auto-generated method stub
                super.onCreate();
            } 
        }
    2.2、初始化接口(必接)
        接口说明:
        首先在程序开始的地方调用 SDK 的初始化 init 方法,并设 置 Activity 对像和初始化完成回调监听(在初始化失败情况下不 再调用其它 SDK 接口方法)
        注意:要确保在 SDK 初始化成功后才可调用其它接口
        2.2.1、方法定义
            public static void init(final Activity activity,final PoolSDKCallBackListener callBackListener) 
        2.2.2、参数说明
        2.2.3、代码示例:
        PoolSdkHelper.init(this, new PoolSDKCallBackListener() {
			@Override
			public void poolSdkCallBack(int code, String msg) {
				// TODO Auto-generated method stub
				switch (code) {
				case PoolSDKCode.POOLSDK_INIT_SUCCESS://初始化成功
					PoolSdkLog.logInfo("游戏中POOLSDK_INIT_SUCCESS");
					login();
					break;
				case PoolSDKCode.POOLSDK_INIT_FAIL:
					break;
				default:
					break;
				}
			}
		});
	
	2.3、登录接口(必接)
	    接口说明:游戏登录时调用
	    2.3.1、方法定义
	    public static void login(final String paramCustom,final PoolLoginListener poolLoginListener)
	    2.3.2、参数说明
	    2.3.3、代码示例
    		PoolSdkHelper.login("登录自定义字段", new PoolLoginListener() {
    			@Override
    			public void onLoginSuccess(PoolLoginInfo poolLoginInfo) {
    				String userType = poolLoginInfo.getUserType();
    				String timestamp = poolLoginInfo.getTimestamp();
    				String serverSign = poolLoginInfo.getServerSign();
    				String openId = poolLoginInfo.getOpenID();
    				// TODO: 把以上信息发送给游戏服务端做登录校验，需要其他信息请从poolLoginInfo对象中获取
    				System.out.println("登录成功  userType = " + userType
    						+ "; timestamp = " + timestamp + "; serverSign = "
    						+ serverSign + "; openId = " + openId);
    			}
    			@Override
    			public void onLoginFailed(String errorMsg) {
    				System.out.println("登录失败  = " + errorMsg);
    			}
    		});
    		
    2.4、提交角色数据接口(必接)
        接口说明:游戏提交角色数据接口,该接口需要在以下 3 中情况下
        调用:进入游戏主场景、创建角色、角色升级
        2.4.1、方法定义
        public static void submitRoleData(final PoolRoleInfo poolRoleInfo,
			final PoolRoleListener poolRoleListener)
		2.4.2、参数说明
		2.4.3、代码示例
		/********************************************
		 * 以下所有字段都是必填项
		 */
		PoolRoleInfo poolRoleInfo = new PoolRoleInfo();
		poolRoleInfo.setRoleID("123456");
		poolRoleInfo.setRoleLevel("10");
		poolRoleInfo.setRoleSex("0");
		poolRoleInfo.setRoleName("我是角色名");
		poolRoleInfo.setServerID("1");
		poolRoleInfo.setServerName("我是服务器名");
		poolRoleInfo.setCustom("角色创建时间");
		poolRoleInfo.setCallType(PoolRoleInfo.Type_EnterGame);
		// poolRoleInfo.setCallType(PoolRoleInfo.Type_CreateRole);
		// poolRoleInfo.setCallType(PoolRoleInfo.Type_RoleUpgrade);

		PoolSdkHelper.submitRoleData(poolRoleInfo, new PoolRoleListener() {
			@Override
			public void onRoleDataSuccess(String paramCustom) {
				System.out.println("提交角色数据成功  = " + paramCustom);
			}
		});
		
	2.5、支付接口(必接)
	    2.5.1、方法定义
	        public static void pay(final PoolPayInfo poolPayInfo,final PoolPayListener poolPayListener)
	   2.5.2、参数说明
	   2.5.3、代码示例
	        /********************************************
		    * 以下所有字段都是必填项
		    */
		    // 设置充值金额，单位“元”
		    poolPayInfo.setAmount("1");
		    // 服务器id
		    poolPayInfo.setServerID("8");
    		// 服务器名
    		poolPayInfo.setServerName("我是服务器名");
    		// 角色id
    		poolPayInfo.setRoleID("123456");
    		// 角色名
    		poolPayInfo.setRoleName("我是角色名");
    		// 角色等级
    		poolPayInfo.setRoleLevel("10");
    		// 商品ID
    		poolPayInfo.setProductID("1");
    		// 商品名称
    		poolPayInfo.setProductName("金币");
    		// 商品描述
    		poolPayInfo.setProductDesc("购买金币");
    		// 兑换比例
    		poolPayInfo.setExchange("10");
    		// 自定义参数
    		poolPayInfo.setCustom("我是自定义参数");
    		// TODO Auto-generated method stub
    		PoolSdkHelper.pay(poolPayInfo, new PoolPayListener() {
    			@Override
    			public void onPaySuccess(String paramCustom) {
    				System.out.println("支付成功  = " + paramCustom);
    			}
    			@Override
    			public void onPayFailed(String paramCustom, String errorMsg) {
    				System.out.println("支付失败  = " + paramCustom + "; errorMsg = "
    						+ errorMsg);
    			}
    		});
    		
    2.6、检测 SDK 是否含有用户中心接口(必接)
        说明:如果接口返回为 true,表示需要游戏方在合适的界面中添 加一个用户中心的按钮,点击按钮时调用文档中 2.7 的用户中心 接口;如果返回 false,则不做处理
        2.6.1、方法定义
            public static boolean hasChannelCenter() 

