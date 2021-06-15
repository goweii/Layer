Android稳定高效的浮层创建管理框架。

可实现Dialog/Popup/BottomSheet等弹窗，引导层，悬浮按钮，浮动通知，吐司，安全键盘等效果。



[GitHub主页](https://github.com/goweii/Layer)

[Demo下载](https://gitee.com/goweii/Layer/raw/master/app/demo/demo.apk)



# 简介

如果你的App用的是单Activity架构，那么这个就是系统Dialog/PopupWindow/Toast的完美替代。而且可以实现悬浮按钮和引导层等一些列功能。

不是单Activity也不影响，上面这些功能一样可以实现，只是Toast无法跨页面，会随Activity一起关闭。

- 同时兼容support和androidx
- 链式调用
- 支持自由扩展
- 实现几种常用效果
  - Dialog/BottomSheet效果
    - 占用区域不会超过当前Activity避免导航栏遮挡
    - 支持自定义大小和显示位置
    - 支持自定义数据绑定
    - 支持自定义进出场动画
    - 支持自定义背景颜色/图片/高斯模糊
    - 支持在Activity的onCreate生命周期弹出
    - 支持从ApplicationContext中弹出
    - 支持拖拽关闭
    - 支持不拦截外部事件
  - Popup效果
    - 拥有Dialog效果特性
    - 支持跟随目标View移动
  - Toast效果
    - 支持自定义图标和文字
    - 支持自定义显示时长
    - 支持自定义位置
    - 支持自定义背景资源和颜色
    - 支持自定义透明度
    - 支持自定义进出场动画
  - Guide效果
    - 详见demo
  - Float效果
    - 支持自定义吸附边
    - 支持自定义正常和低姿态2中模式
    - 支持自定义低姿态显示效果
  - Notification效果
    - 支持滑动关闭



# 截图

截图效果较差，建议[下载Demo](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.apk)体验最新功能

| ![20210610_190449.gif](https://i.loli.net/2021/06/10/6jgVucdrE73S2pG.gif) | ![20210610_190537.gif](https://i.loli.net/2021/06/10/N617Xf2Kl5Woqd8.gif) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![20210610_190654.gif](https://i.loli.net/2021/06/10/aVoWBmGqtE1HkUP.gif) | ![20210610_190715.gif](https://i.loli.net/2021/06/10/npHzPjwdqfKBDQt.gif) |



# 使用说明



## 集成

![](https://img.shields.io/badge/Downloads%20Week-1.4k-green) ![](https://img.shields.io/badge/Downloads%20Month-7.3K-blue)

- ### 添加仓库

```groovy
// build.gradle(Project:)
allprojects {
    repositories {
        maven { url "https://gitee.com/goweii/maven-repository/raw/master/releases/" }
    }
}
```

- ### 添加依赖

  [点击查看最新版本号](https://github.com/goweii/AnyLayer/releases)
```groovy
// build.gradle(Module:)
dependencies {
    // 完整引入
    implementation "per.goweii.layer:Layer:1.0.0"
    
    // 按需引入
    // 基础库
    // implementation "per.goweii.layer:layer:1.0.0"
    // 自动初始化（依赖基础库和Jetpack Startup）
    // implementation "per.goweii.layer:layer-startup:1.0.0"
    // 扩展库（依赖基础库）
    // implementation "per.goweii.layer:layer-ext:1.0.0"
    // Kotlin扩展库（依赖基础库和扩展库）
    // implementation "per.goweii.layer:layer-ktx:1.0.0"
}
```

- ### 一些问题/建议

  - 建议按需引入

  - layer-startup依赖于Jetpack Startup，需要自行引入。



## 更新说明

[点击查看](https://github.com/goweii/Layer/releases)



## 类间关系

- **[ViewManager]()**（管理View的动态添加移除和KeyEvent事件注册）

- **[Layer]()**（对ViewManager的包装，实现进出场动画逻辑和事件监听，规范接口形式，分离出ViewHolder/ListenerHolder/Config三大内部类）
  - **[FrameLayer]()**（限定父布局为FrameLayout，引入了Layer层级概念）
    - **[DecorLayer]()**（限定父布局为DecorView）
      - **[DialogLayer]()**（规范子布局层级，加入背景层，分离动画为背景动画和内容动画）
        - **[PopupLayer]()**（可依据锚点View定位）
      - **[ToastLayer]()**（吐司）
      - **[GuideLayer]()**（引导层）
      - **[FloatLayer]()**（悬浮按钮）
      - **[NotificationLayer]()**（通知）

- **[AnimatorHelper]()**（创建常用属性动画）



# 如果你觉得还不错，就请我喝杯咖啡吧~

| 微信 | 支付宝 | QQ |
| :---: | :---: | :---: |
| ![wx_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/wx_qrcode.png) | ![zfb_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/zfb_qrcode.png) | ![qq_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/qq_qrcode.png) |

