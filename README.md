Android稳定高效的浮层创建管理框架。

可实现Dialog，PopupWindow，引导层，悬浮按钮，通知，吐司，安全键盘等效果。

[GitHub主页](https://github.com/goweii/Layer)

# 简介

如果你的App用的是单Activity架构，那么这个就是系统Dialog/PopupWindow/Toast的完美替代。而且可以实现悬浮按钮和引导层等一些列功能。

不是单Activity也不影响，上面这些功能一样可以实现，只是Toast无法跨页面，会随Activity一起关闭。

- 支持AndroidX
- 链式调用
- 支持自由扩展
- 实现几种常用效果
    - DialogLayer：AlertDialog/BottomSheetDialog
        - 占用区域不会超过当前Activity避免导航栏遮挡
        - 支持自定义大小和显示位置
        - 支持自定义数据绑定
        - 支持自定义进出场动画
        - 支持自定义背景颜色/图片/高斯模糊
        - 支持在Activity的onCreate生命周期弹出
        - 支持从ApplicationContext中弹出
        - 支持拖拽关闭
        - 支持不拦截外部事件
    - PopupLayer：PopupWindow
        - 拥有Dialog效果特性
        - 支持跟随目标View移动
    - ToastLayer：吐司
        - 支持自定义图标和文字
        - 支持自定义显示时长
        - 支持自定义位置
        - 支持自定义背景资源和颜色
        - 支持自定义透明度
        - 支持自定义进出场动画
    - GuideLayer：引导层
        - 支持自定义遮罩颜色和圆角半径
    - OverlayLayer：悬浮按钮
        - 支持自定义吸附边
        - 支持自定义正常和低姿态2中模式
        - 支持自定义低姿态显示效果
    - NotificationLayer：通知
        - 支持滑动关闭
    - KeyboardLayer：软键盘
        - 支持大小写字母/数字/常用符号

# 截图

截图效果较差，建议[下载Demo](https://raw.githubusercontent.com/goweii/Layer/master/simple/release/per.goweii.layer.simple.apk)体验最新功能

| ![qQF8OK.gif](https://s1.ax1x.com/2022/03/22/qQF8OK.gif) | ![qQF1Qx.gif](https://s1.ax1x.com/2022/03/22/qQF1Qx.gif) | ![qQF3y6.gif](https://s1.ax1x.com/2022/03/22/qQF3y6.gif) |
| --- | --- | --- |

# 如何集成

- 添加仓库

```groovy
// build.gradle(Project:)
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

- 添加依赖

![](https://img.shields.io/github/v/release/goweii/Layer)

```groovy
// build.gradle(Module:)
dependencies {
    def version = "1.0.0"
    
    // 完整引入
    implementation "com.github.goweii:Layer:$version"
    
    // 按需引入
    
    // 核心功能
    // implementation "com.github.goweii.Layer:layer-core:$version"
    // 核心功能Kotlin扩展库
    // implementation "com.github.goweii.Layer:layer-ktx:$version"
    // 自动初始化
    // implementation "com.github.goweii.Layer:layer-startup:$version"
    
    // 各个浮层效果（xxx为module名）
    // implementation "com.github.goweii.Layer:layer-xxx:$version"
    // implementation "com.github.goweii.Layer:layer-xxx-ktx:$version"
}
```

# 如何使用

具体使用可参考simple

- [LayersSimpleActivity](https://raw.githubusercontent.com/goweii/Layer/master/simple/src/main/java/per/goweii/layer/simple/LayersSimpleActivity.kt)
- [MaterialSimpleActivity](https://raw.githubusercontent.com/goweii/Layer/master/simple/src/main/java/per/goweii/layer/simple/MaterialSimpleActivity.kt)
- [CupertinoSimpleActivity](https://raw.githubusercontent.com/goweii/Layer/master/simple/src/main/java/per/goweii/layer/simple/CupertinoSimpleActivity.kt)

# 更新说明

[点击查看](https://github.com/goweii/Layer/releases)

# 如果你觉得还不错，就请我喝杯咖啡吧~

| 微信 | 支付宝 | QQ |
| :---: | :---: | :---: |
| ![wx_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/wx_qrcode.png) | ![zfb_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/zfb_qrcode.png) | ![qq_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/qq_qrcode.png) |

