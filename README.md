![](img/party.png)

叔想做个直播demo很久了，最近终于得空，做了一个视频群聊Demo，以飨观众。 直播云有很多大厂在做，经老铁介绍，Agora不错，遂入坑。

# Agora SDK集成 #
叔专注SDK集成几十年，Agora SDK集成也并没有搞什么事情，大家按照下面步骤上车就行。

## 1. 注册 ##
登录[官网](https://www.agora.io/cn/)，注册个人账号，这个叔就不介绍了。

## 2. 创建应用 ##
注册账号登录后，进入后台，找到“添加新项目”按钮，点击创建新项目，创建好后就会获取到一个App ID, 做过SDK集成的老铁们都知道这是干啥用的。

![](img/create_app.png)

## 3. 下载SDK ##
进入官方[下载](https://www.agora.io/cn/news/download/)界面, 这里我们选择```视频通话 + 直播 SDK```中的Android版本下载。下载后解压之后又两个文件夹，分别是libs和samples, libs文件夹存放的是库文件，samples是官方Demo源码，大叔曾说过欲练此SDK，必先跑Sample, 有兴趣的同学可以跑跑。

## 4. 集成SDK ##
### 1. 导入库文件 ###
将libs文件夹的下的文件导入Android Studio, 最终效果如下：
![](img/libs.png)

### 2. 添加必要权限 ###
在AndroidManifest.xml中添加如下权限

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />

### 3. 配置APP ID ###
在values文件夹下创建strings-config.xml, 配置在官网创建应用的App ID。

	<resources>
	    <string name="private_app_id">6ffa586315ed49e6a8cdff064ad8a0b0</string>
	</resources>

# 小结 #
至此，Agoria SDK集成已经完毕，似不似如丝般顺滑？如果官方能够提供Gradle依赖，还可以省掉下载SDK和导入库文件的步骤，那就更滑了。接下来我们就可以愉快地和SDK玩耍了，各位老铁请关注下篇

老铁，一起来开Party(二) —— Agoria SDK实践


