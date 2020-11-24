package dev.vishalsehgal.lottietoggles.`interface`

import dev.vishalsehgal.lottietoggles.ToggleableLottieView

interface OnCheckChangeListener {

    /**
     * Called when a LottieToggleableView changes it's state.
     *
     * @param toggleableLottieView The view which is either on/off.
     * @param isChecked The on/off state of toggle, true when toggle turns on.
     */
    fun onCheckedChanged(toggleableLottieView: ToggleableLottieView, isChecked: Boolean)
}

interface Checkable{

    /**
     * Change the checked state of the LottieToggleableView to the inverse of its current state.
     */
    fun toggle()

    /**
     * @return The current checked state of the LottieToggleableView
     */
    var isChecked: Boolean
}