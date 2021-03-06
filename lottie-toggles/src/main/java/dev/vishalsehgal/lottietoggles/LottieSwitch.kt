package dev.vishalsehgal.lottietoggles

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.RenderMode
import dev.vishalsehgal.lottietoggles.`interface`.OnCheckChangeListener
import dev.vishalsehgal.lottietoggles.view.LottiefiedSwitchView
import dev.vishalsehgal.lottietoggles.view.ToggleableLottieView
import kotlin.math.min

class LottieSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var animationFile: Int = -1
    private var toggleSpeed = 1f

    companion object {
        @BindingAdapter("app:switch_lottieFile")
        @JvmStatic
        fun setAnimationFile(lottieSwitch: LottieSwitch, switchFile: Int = -1) {
            if (lottieSwitch.lottieAnimationView.animation == null) {
                lottieSwitch.lottieAnimationView.setAnimation(switchFile)
            }
        }

    }

    var isChecked: Boolean
        get() = this.lottieAnimationView.isChecked
        set(value) {
            this.lottieAnimationView.isChecked = value
        }

    private val lottieAnimationView by lazy {
        LottiefiedSwitchView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            repeatMode = LottieDrawable.REVERSE
            setRenderMode(RenderMode.AUTOMATIC)
            speed = toggleSpeed
            if (animationFile != -1)
                setAnimation(animationFile)
        }
    }

    init {

        context.theme.obtainStyledAttributes(attrs, R.styleable.LottieSwitch, defStyleAttr, 0)
            .let { style ->

                animationFile = style.getResourceId(R.styleable.LottieSwitch_switch_lottieFile, -1)
                toggleSpeed = style.getFloat(R.styleable.LottieSwitch_switch_toggleSpeed, 3f)
                isChecked = style.getBoolean(R.styleable.LottieSwitch_switch_checked, false)
                post { addView(lottieAnimationView) }
                style.recycle()

            }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = resources.getDimensionPixelSize(R.dimen.lottie_switch_default_width)
        val desiredHeight = resources.getDimensionPixelSize(R.dimen.lottie_switch_default_height)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val measuredWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> {
                widthSize
            }
            MeasureSpec.AT_MOST -> {
                min(desiredWidth, widthSize)
            }
            else -> {
                desiredWidth
            }
        }

        val measuredHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> {
                heightSize
            }
            MeasureSpec.AT_MOST -> {
                min(desiredHeight, heightSize)
            }
            else -> {
                desiredHeight
            }
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
        measureChild(lottieAnimationView, measuredWidth, measuredHeight)
    }

    fun setOnCheckedChangedListener(onCheckChangeListener: (ToggleableLottieView, Boolean) -> Unit) {
        lottieAnimationView.onCheckedChangeListener = object : OnCheckChangeListener {
            override fun onCheckedChanged(
                toggleableLottieView: ToggleableLottieView,
                isChecked: Boolean
            ) = onCheckChangeListener(toggleableLottieView, isChecked)
        }
    }

    internal class SavedState : BaseSavedState {
        var checked = false
        var progress = 0f

        /**
         * Constructor called from [LottieSwitch.onSaveInstanceState]
         */
        constructor(superState: Parcelable?) : super(superState) {}

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(`in`: Parcel) : super(`in`) {
            checked = (`in`.readValue(null) as Boolean?)!!
            progress = (`in`.readValue(null) as Float?)!!
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeValue(checked)
            out.writeValue(progress)
        }

        override fun toString(): String {
            return ("LottieSwitch.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}"
                    + " progress=" + progress + "}"
                    )
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return SavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        lottieAnimationView.isEnabled = enabled
        super.setEnabled(enabled)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.checked = isChecked
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        isChecked = ss.checked
        requestLayout()
    }

}