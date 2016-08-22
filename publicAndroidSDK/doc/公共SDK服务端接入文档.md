#公共SDK服务端接入文档

###1.登录验证	客户端发送请求参数给服务端,服务端对参数进行 MD5 加密,如果加密结果和serverSign 一致,则验证通过,否则不通过
参数 			| 类型 				| 描述
------------ 	| ------------- 	| ------------
userType 		| int  				| 用户类型 ID(渠道类型,兼容以 前帐号体系)
openId 			| string  			| 平台渠道方返回用户ID
timestamp 		| string  			| 时间戳,公共SDK发送到客户端的参数timestamp, 服务端可以判断 timestamp 是否 在有效的登录时间内
serverSign 		| string  			| 验证密钥,公共 SDK 发送到 客户端的参数 serverSign,加密串:gameAppkey=%s&userType=% s&openId=%s&timestamp=%s注意:(游戏测试验证时 gameAppkey 默认值为 fytx_onlie#@12&34 进行验证, 出正式母包时改为游戏本身的gameAppkey值)

验证方式:  
serverSign=MD5(gameAppkey=%s&userType=%s&openId=%s&timestamp=%s) 如:
md5("gameAppkey=xxxx&userType=1&openId=123&timestamp=111111111111")  
gameAppkey : 在公共SDK后台的 Md5加密的KEY

###2. 充值验证(通知游戏方发货)
	公共SDK服务器接收到渠道服务器充值回调,对回调进行 MD5 验证,如果验证通过, 则采用Post方式马上回调给游戏充值服务器,否则不会回调充值服务器,然后再返回 信息给渠道服务器。回调参数如下:
参数 			| 类型 			| 描述
------------ 	| -------------| ------------
serverId 		| int  			| 服务器 ID
playerId 		| int  			| 角色 ID
orderId 		| string  		| 渠道方订单 ID
payAmount 		| double  		| 充值金额(如有商品 ID,此值为商 品的金额)
currency 		| string  		| 充值货币(CNY:人民币)
goodsId 		| string  		| 商品ID,参数对一些有商品列表 的充值有效
goodsName 		| string  		| 客户端充值 IP
custom	 		| string  		| 透传参数 ,长度 512
serverSign 		| string  		| 发给充值服务器验证的注意:(游戏测试验证时 gameAppkey 默认值为fytx_onlie#@12&34 进行验证, 出正式母包时改为游戏本身的 gameAppkey 值)
uId 			| string  		| 平台渠道帐号 ID
gameSimpleName 	| string  		| 公告SDK后台游戏名
sdkSimpleName 	| string  		| 平台渠道代号
queryId 		| string  		| 公共SDK订单号
postTime 		| string  		| 订单创建时间戳

验证方式:  
serverSign=MD5(serverId=%s&playerId=%s&orderId=%s&gameAppKe y=%s)%s:为相应的值  
gameAppkey : 在公共SDK后台的Md5加密的KEY  
验证通过后,返回公共 SDK 服务器数据,数据格式:{"code":0,"message":"无订 单"}。code:0 表示失败,1 表示成功,message:描述原因。