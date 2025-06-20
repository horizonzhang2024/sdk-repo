package com.zhanchen.main.utils

object Constants {

    //
    // Scale
    //
    const val FILTER_NONE = 0       // Point sample; Fastest.
    const val FILTER_LINEAR = 1     // Filter horizontally only.
    const val FILTER_BILINEAR = 2   // Faster than box, but lower quality scaling down.
    const val FILTER_BOX = 3        // Highest quality.

    //
    // Rotate
    //

    const val ROTATE_0 = 0          // No rotation.
    const val ROTATE_90 = 90        // Rotate 90 degrees clockwise.
    const val ROTATE_180 = 180      // Rotate 180 degrees.
    const val ROTATE_270 = 270      // Rotate 270 degrees clockwise.

}