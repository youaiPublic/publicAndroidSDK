## 自主切换用户中间件接入说明 ##
注：本SDK应用可以正常使用 `基于返回第三方SDK openid` 游戏可以找回对应的角色，需要游戏服务器做相应的处理。

开启切换功能后游戏需要将用户中心，切换帐号等功能做相应处理

## 解压后文件清单
![](http://i.imgur.com/IkpMILJ.png)

## SDK资源文件接入步骤
1、在AndroidManifest.xml 中添加以下权限
```code
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
```

2、在AndroidManifest.xml中添加以下activity
```code
<!-- alipay sdk begin -->
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
<!-- alipay sdk end -->

<!-- changeuser SDK start -->
<activity
android:name="com.owner.sdk.OwnerSDK"
android:label="owner_sdk"
android:theme="@android:style/Theme.Dialog" >
</activity>

<activity android:name="com.owner.middleware.utils.SelectRolesActivity"
android:theme="@style/fanyou_DialogParent" >
</activity>

<activity
android:name="com.owner.middleware.utils.BindActivity"
android:theme="@style/fanyou_DialogParent" >
</activity>

<activity
android:name="com.owner.middleware.utils.ResultActivity"
android:theme="@style/fanyou_DialogParent" >
</activity>
<!-- changeuser SDK end -->
```
3、添加libs目录下文件到你的工程中
![](http://i.imgur.com/xEfVSmH.png)
4、添加资源文件夹res直接拷贝覆盖到你工程中的res文件夹
![](http://i.imgur.com/WNS4qg5.png)
## SDK代码接入部分
**1、初始化接口**
在程序初始化（onCreate）时调用
**OwnerSdkHelper.init(activity, appid, appkey, channel, baseUrl, finishListener, initListener);**
### 示例
```code
OwnerSdkHelper.init(
		this, 
		"570c681c62c0d03e78ba9668",
		"d302a729895087ca4277b77f93bd8cc0",
		"channel",
		"http://10.20.201.122:9006",
		new OwnerSDKEventsListener() {
					@Override
					public void onEventDispatch(int i, Intent data) {
						//Log.i("youaisdk","code:" + i + "data:" + data.toString());
						if (i == OwnerSDK.LOGIN_ACTION_CODE) {
							//登录成功后返回的信息
							System.err.println(data.getStringExtra("open_id"));
							System.err.println(data.getStringExtra("token"));
							System.err.println(data.getStringExtra("timestamp"));
						} else if (i == OwnerSDK.PAY_ACTION_CODE) {

						}
					}

				},new OwnerSDKInitListener() {
					@Override
					public void onSuccess(String content) {
						//初始化成功回调
					}

					@Override
					public void onFail(String content) {
						//初始化失败回调
					}
				});
```


**2、void record接口**
需在进入游戏时调用，确保可以保存和获取到相应的数据
（如同一账号下有多个角色，请将角色ID传入username）
**OwnerSdkHelper.record(openId, userType, serverId, userName, userId, level, loginType, Custom);**
```code
OwnerSdkHelper.record(
String openId, //渠道SDK登录成功后返回的openid
String userType, //原渠道SDK用户类型,与后台的对应
String serverId, //用户服务器id
String userName, //用户名称
String userId, //用户id
String level,  //用户等级
String loginType, //登录类型（可传""）
String Custom); //自定义参数（可传""）
```

----------

**3、boolean isShowChangeUser接口**
提供给游戏判断是否开启切换用户功能
**OwnerSdkHelper.isShowChangeUser();**


----------

**4、void showChangeUser接口**
当判断双账号为开启时调用showChangeUser,否则调用游戏正常调用对应SDK逻辑
**OwnerSdkHelper.showChangeUser();**

----------

**5、void pay接口**
支付接口
**OwnerSdkHelper.pay(serverId, roleName, amount, callBackInfo);**
示例
```code
OwnerSdkHelper.pay(
"S10086", //服务器ID
"role996", //playerID
100,//游戏币，支付金额兑换比例到后台的运营资料设置汇率
"");//自定义参数
```
