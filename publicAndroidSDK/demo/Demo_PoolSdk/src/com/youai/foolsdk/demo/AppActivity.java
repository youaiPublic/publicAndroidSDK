package com.youai.foolsdk.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.gzyouai.fengniao.sdk.framework.PoolExitDialogListener;
import com.gzyouai.fengniao.sdk.framework.PoolExitListener;
import com.gzyouai.fengniao.sdk.framework.PoolExpansionListener;
import com.gzyouai.fengniao.sdk.framework.PoolLoginInfo;
import com.gzyouai.fengniao.sdk.framework.PoolLoginListener;
import com.gzyouai.fengniao.sdk.framework.PoolLogoutListener;
import com.gzyouai.fengniao.sdk.framework.PoolPayInfo;
import com.gzyouai.fengniao.sdk.framework.PoolPayListener;
import com.gzyouai.fengniao.sdk.framework.PoolReport;
import com.gzyouai.fengniao.sdk.framework.PoolRoleInfo;
import com.gzyouai.fengniao.sdk.framework.PoolRoleListener;
import com.gzyouai.fengniao.sdk.framework.PoolSDKCallBackListener;
import com.gzyouai.fengniao.sdk.framework.PoolSDKCode;
import com.gzyouai.fengniao.sdk.framework.PoolSdkHelper;
import com.gzyouai.fengniao.sdk.framework.PoolSdkLog;

public class AppActivity extends Activity {

	private Button yaLoginBt;
	private Button yaEnterGameBt;
	private Button yaPayBt;
	private Button yaSubmitRoleDataBt;
	private Button yaChannelCenter;
	private Button yaSwitchAccountBt;
	private Button yaLogoutBt;
	private Button yaForumBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layoutId = getResources().getIdentifier(
				"public_sdk_self_game_login", "layout", getPackageName());
		setContentView(layoutId);
		initLoginView();
		//PoolSdkHelper.isDebug = true;
		PoolSdkLog.setIsShowLog(true);
		PoolSdkHelper.init(this, new PoolSDKCallBackListener() {
			@Override
			public void poolSdkCallBack(int code, String msg) {
				// TODO Auto-generated method stub
				PoolSdkLog.logInfo("callback: code:" + code + "msg:" + msg);
				switch (code) {
				case PoolSDKCode.POOLSDK_INIT_SUCCESS:// 初始化成功
					PoolSdkLog.logInfo("游戏中POOLSDK_INIT_SUCCESS");
					login();
					break;
				case PoolSDKCode.POOLSDK_INIT_FAIL:
					break;
				default:
					break;
				}
			}
		});// init(this);// this为游戏的activity对象

		PoolSdkHelper.setLogoutCallback(new PoolLogoutListener() {
			@Override
			public void onLogoutSuccess() {
				// TODO: 此处处理SDK登出的逻辑
				login();
				PoolSdkLog.logInfo("游戏中logoutSuccess");
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		PoolSdkHelper.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		PoolSdkHelper.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		PoolSdkHelper.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		PoolSdkHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		PoolSdkHelper.onPause();

	}

	@Override
	public void onRestart() {
		super.onRestart();
		PoolSdkHelper.onRestart();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		PoolSdkHelper.onNewIntent(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		PoolSdkHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		PoolSdkHelper.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		PoolSdkHelper.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		PoolSdkHelper.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		PoolSdkHelper.onWindowFocusChanged(hasFocus);
	}
	
	@Override
	public void onWindowAttributesChanged(LayoutParams params) {
		// TODO Auto-generated method stub
		super.onWindowAttributesChanged(params);
		PoolSdkHelper.onWindowAttributesChanged(params);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent pKeyEvent) {
		if (pKeyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& pKeyEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (PoolSdkHelper.hasExitDialog()) {
				PoolSdkHelper.showExitDialog(new PoolExitDialogListener() {
					@Override
					public void onDialogResult(int code, String msg) {
						// TODO Auto-generated method stub
						switch (code) {
						case PoolSDKCode.EXIT_SUCCESS:// 退出成功游戏处理自己退出逻辑
							finish();
							break;
						case PoolSDKCode.EXIT_CANCEL:// 取消退出
							break;
						default:
							break;
						}
					}
				});
			} else {
				// TODO: 调用游戏的退出界面
				showGameExitTips();
			}
			return false;
		}
		return super.dispatchKeyEvent(pKeyEvent);
	}

	private void showGameExitTips() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		int ic_dialog_alert_id = getRedIdByName("ic_dialog_alert", "drawable");
		dialog.setIcon(ic_dialog_alert_id);
		dialog.setTitle("提示");
		dialog.setMessage("是否退出游戏?");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PoolSdkHelper.exitGame(new PoolExitListener() {
					@Override
					public void onExitGame() {
						finish();
					}
				});
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private int getRedIdByName(String resName, String resType) {
		return getResources().getIdentifier(resName, resType, getPackageName());
	}

	private void initLoginView() {
		int ya_login_bt = getRedIdByName("ya_login_bt", "id");// getResources().getIdentifier("ya_login_bt",
																// "id",
																// getPackageName());
		yaLoginBt = (Button) findViewById(ya_login_bt);
		yaLoginBt.setText("登录");
		yaEnterGameBt = (Button) findViewById(getRedIdByName(
				"ya_enter_game_bt", "id"));
		yaEnterGameBt.setText("进入游戏");
	}

	private void initGameView() {
		yaPayBt = (Button) findViewById(getRedIdByName("ya_pay_bt", "id"));
		yaPayBt.setText("支付");
		yaSubmitRoleDataBt = (Button) findViewById(getRedIdByName(
				"ya_submit_role_data_bt", "id"));
		yaSubmitRoleDataBt.setText("提交角色数据");
		yaChannelCenter = (Button) findViewById(getRedIdByName(
				"ya_channel_center", "id"));
		yaChannelCenter.setText("渠道中心");
		yaSwitchAccountBt = (Button) findViewById(getRedIdByName(
				"ya_switch_account_bt", "id"));
		yaSwitchAccountBt.setText("切换账号");
		PoolSdkLog.logError("" + yaPayBt + "yachannel:" + yaChannelCenter);
		
		yaLogoutBt = (Button) findViewById(getRedIdByName("logout_bt", "id"));
		
		yaForumBt = (Button) findViewById(getRedIdByName("forum_bt", "id"));
	}

	public void yaOnClick(View view) {
		if (view == yaLoginBt) {
			login();
		} else if (view == yaEnterGameBt) {
			setContentView(getRedIdByName("public_sdk_self_game", "layout"));// (R.layout.game);
			initGameView();
			PoolReport.reportEnterGame("chufan","角色名", 12, "201","服务器名");
		} else if (view == yaPayBt) {
			pay();
		} else if (view == yaSubmitRoleDataBt) {
			submitRoleData();
		} else if (view == yaChannelCenter) {
			channelCenter();
		} else if (view == yaSwitchAccountBt) {
			switchAccount();
		} else if (view == yaLogoutBt){
			logout();
		} else if (view == yaForumBt){
			openForum();
		}
	}

	private void openForum(){
		PoolSdkHelper.openForum();
	}
	
	private void logout(){
		PoolSdkHelper.logout(this);//结果回调通知到PoolLogoutListener
		if(PoolSdkHelper.hasLogout()){
		}
	}
	
	private void login() {
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
	}

	private void pay() {
		final PoolPayInfo poolPayInfo = new PoolPayInfo();

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

	}

	/**
	 * 提交角色数据 该接口需要在3个地方调用
	 * 
	 * 1、登录游戏主场景 2、创建角色 3、角色升级
	 */
	private void submitRoleData() {
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
		poolRoleInfo.setRoleCTime(System.currentTimeMillis()/1000);//角色创建时间（秒）
		poolRoleInfo.setPartyName("公会名称");
		poolRoleInfo.setRoleType("狂战");//角色类型
		poolRoleInfo.setRoleChangeTime(System.currentTimeMillis()/1000);//角色更新时间
		poolRoleInfo.setVipLevel("10");//vip等级
		poolRoleInfo.setDiamond("1000");//余额
		poolRoleInfo.setMoneyType("金币");//商品单位
		
		poolRoleInfo.setCallType(PoolRoleInfo.Type_EnterGame);
		// poolRoleInfo.setCallType(PoolRoleInfo.Type_CreateRole);
		// poolRoleInfo.setCallType(PoolRoleInfo.Type_RoleUpgrade);

		PoolSdkHelper.submitRoleData(poolRoleInfo, new PoolRoleListener() {
			@Override
			public void onRoleDataSuccess(String paramCustom) {
				System.out.println("提交角色数据成功  = " + paramCustom);
			}
		});
	}

	/**
	 * 用户中心
	 * 
	 * 游戏方先调用YASDKHelper.hasChannelCenter()获取是否有用户中心，
	 * 如果有的话，游戏中需要添加按钮，点击按钮调用YASDKHelper.openChannelCenter();
	 * 如果没有，则不需要显示按钮，也不用调用下面的接口
	 */
	private void channelCenter() {
		PoolSdkHelper.openChannelCenter();
	}

	/**
	 * 切换帐号
	 */
	private void switchAccount() {
		if(PoolSdkHelper.hasSwitchAccount()){
			PoolSdkHelper.switchAccount(this);
		}
	}

	/**
	 * 扩展接口
	 */
	private void expansionInterface() {
		PoolSdkHelper.expansionInterface("自定义参数", new PoolExpansionListener() {

			@Override
			public void onSuccess(String paramCustom) {

			}
		});
	}
}
