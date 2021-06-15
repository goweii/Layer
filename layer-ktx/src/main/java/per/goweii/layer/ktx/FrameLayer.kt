package per.goweii.layer.ktx

import per.goweii.layer.FrameLayer

fun <T : FrameLayer> T.level(level: Int) = this.apply {
    this.setLevel(level)
}

fun <T : FrameLayer> T.cancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.setCancelableOnClickKeyBack(enable)
}