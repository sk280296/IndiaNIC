package com.app.indianic.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.indianic.R
import com.app.indianic.databinding.LayoutCustomEdittextBinding
import com.app.indianic.utils.logError
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class CustomMaterialEditText : RelativeLayout {

    private var mContext: Context? = null
    private var view: View? = null
    private var textInputLayout: TextInputLayout? = null
    private var textInputEditText: TextInputEditText? = null

    private var animatedHintColor = Color.BLACK
    private var hintText: String? = ""
    private var inputType: Int = InputType.TYPE_CLASS_TEXT
    private var textTypeface: Int? = R.font.general_sans_regular
    private var textSize = 16
    private var editFieldMinHeight = 0
    private var textColor = Color.BLACK
    private var endIconDrawable: Drawable? = null
    private var lines: Int = 1
    private var textGravity: Int = Gravity.NO_GRAVITY
    private var textMaxLength: Int = Int.MAX_VALUE
    private var customUnfocusedBackground = R.drawable.bg_shape_8

    private var errorMessage: String? = null

    constructor(mContext: Context) : this(mContext, null)

    constructor(mContext: Context, attrs: AttributeSet?) : super(mContext, attrs) {
        this.mContext = mContext
        val binding = LayoutCustomEdittextBinding.inflate(LayoutInflater.from(mContext), this, true)
        textInputLayout = binding.textInputLayout
        textInputEditText = binding.textInputEditText
        if (attrs != null) {
            getAttributes(attrs)
        }
        setListener()
    }

    private fun getAttributes(attrs: AttributeSet) {
        val typedArray = mContext!!.obtainStyledAttributes(attrs, R.styleable.CustomViewAttributes)
        val text: String? = typedArray.getString(R.styleable.CustomViewAttributes_text)
        animatedHintColor =
            typedArray.getColor(
                R.styleable.CustomViewAttributes_animatedHintColor,
                ContextCompat.getColor(mContext!!, R.color.color_252527)
            )
        hintText = typedArray.getString(R.styleable.CustomViewAttributes_hint)
        textSize = typedArray.getDimensionPixelSize(R.styleable.CustomViewAttributes_textSize, 16)
        editFieldMinHeight = typedArray.getDimensionPixelSize(
            R.styleable.CustomViewAttributes_editFieldMinHeight,
            0
        )
        textColor = typedArray.getColor(
            R.styleable.CustomViewAttributes_textColor,
            ContextCompat.getColor(mContext!!, R.color.color_3C3B3E)
        )
        endIconDrawable = typedArray.getDrawable(R.styleable.CustomViewAttributes_endIconDrawable)
        inputType = typedArray.getInt(
            R.styleable.CustomViewAttributes_textInputType,
            InputType.TYPE_CLASS_TEXT
        )

        textTypeface = typedArray.getResourceId(
            R.styleable.CustomViewAttributes_textTypeface,
            R.font.general_sans_regular
        )
        lines = typedArray.getInt(R.styleable.CustomViewAttributes_lines, 1)
        textGravity =
            typedArray.getInt(R.styleable.CustomViewAttributes_textGravity, Gravity.NO_GRAVITY)
        textMaxLength =
            typedArray.getInt(R.styleable.CustomViewAttributes_textMaxLength, Int.MAX_VALUE)
        val focusable =
            typedArray.getBoolean(R.styleable.CustomViewAttributes_focusable, true)
        val focusableInTouchMode =
            typedArray.getBoolean(R.styleable.CustomViewAttributes_focusableInTouchMode, true)
        val spaceNotAllowed =
            typedArray.getBoolean(R.styleable.CustomViewAttributes_spaceNotAllowed, false)
        customUnfocusedBackground =
            typedArray.getResourceId(
                R.styleable.CustomViewAttributes_customUnfocusedBackground,
                R.drawable.bg_shape_8
            )


        //----------------setup values-------------------//

        textInputLayout?.setBackgroundResource(customUnfocusedBackground)
        textInputEditText?.setText(text ?: "")
        textInputLayout?.hint = hintText
        textInputEditText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        if (editFieldMinHeight > 0) {
            textInputLayout?.minimumHeight = editFieldMinHeight
            textInputEditText?.minimumHeight = editFieldMinHeight
        }
        textInputEditText?.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(textMaxLength))

        textTypeface?.let {
            val typeFace = ResourcesCompat.getFont(
                mContext!!, R.font.general_sans_regular
            )

            textInputEditText?.typeface = typeFace
        }

        textInputEditText?.setTextColor(textColor)

        if (endIconDrawable != null) {
            textInputLayout?.endIconMode = TextInputLayout.END_ICON_CUSTOM
            textInputLayout?.endIconDrawable = endIconDrawable
        }

        textInputEditText?.setLines(lines)

        if(lines==1){
            textInputEditText?.setSingleLine()
        }

        textInputEditText?.gravity = textGravity
        textInputEditText?.isFocusable = focusable
        textInputEditText?.isFocusableInTouchMode = focusableInTouchMode
        textInputEditText?.gravity = textGravity

        setKeyConstraint(spaceNotAllowed)

        setHintAppearance()

        typedArray.recycle()
    }


    private fun setKeyConstraint(spaceNotAllowed: Boolean = false) {
        if (spaceNotAllowed) {
            textInputEditText?.keyListener =
                DigitsKeyListener.getInstance("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890@._")
            textInputEditText?.setRawInputType(inputType)
        }
    }

    private fun setListener() {
        textInputEditText!!.onFocusChangeListener = null
        textInputEditText!!.onFocusChangeListener =
            OnFocusChangeListener { _: View?, hasFocus: Boolean ->
                logError("hasFocus : $hasFocus")
                val background: Drawable? =
                    mContext?.let { context ->
                        ContextCompat.getDrawable(
                            context,
                            if (hasFocus || textInputEditText?.text.toString()
                                    .isNotEmpty()
                            ) R.drawable.bg_stroke_shape_8 else customUnfocusedBackground
                        )
                    }
                textInputEditText!!.background = background

                setHintAppearance()
            }



    }



    private fun setHintAppearance() {
        if (checkEditTextIsBlank()) {
            setTextAppearanceOnBlank()
        }

    }

    private fun checkEditTextIsBlank(): Boolean {
        return TextUtils.isEmpty(textInputEditText?.text) && textInputEditText?.text.isNullOrBlank()
    }

    private fun setTextAppearanceOnBlank() {
        textInputLayout?.hintTextColor =
            setColorStateList(ContextCompat.getColor(context, R.color.color_84848C))
        textInputEditText?.setHintTextColor(
            setColorStateList(
                ContextCompat.getColor(
                    context,
                    R.color.color_84848C
                )
            )
        )

        textInputLayout?.defaultHintTextColor = setColorStateList(
            if (textInputEditText?.hasFocus() == true) animatedHintColor else ContextCompat.getColor(
                context,
                R.color.color_84848C
            )
        )

        textInputLayout?.typeface = ResourcesCompat.getFont(
            context,
            if (textInputEditText?.hasFocus() == true)
                R.font.general_sans_medium
            else
                R.font.general_sans_regular
        )
    }

    fun getText(): String {
        return textInputEditText?.text.toString().trim()
    }

    fun setText(text: String) {
        textInputEditText?.setText(text)
    }

    fun changeAppearance(isFocused: Boolean) {
        val background: Drawable? =
            mContext?.let { context ->
                ContextCompat.getDrawable(
                    context,
                    if (isFocused || textInputEditText?.text.toString().isNotEmpty())
                        R.drawable.bg_stroke_shape_8
                    else
                        customUnfocusedBackground
                )
            }
        textInputLayout!!.background = background

        textInputLayout?.defaultHintTextColor = setColorStateList(
            if (textInputEditText?.text?.isNotEmpty() == true)
                ContextCompat.getColor(context, R.color.color_252527)
            else
                ContextCompat.getColor(context, R.color.color_84848C)
        )

        val typeFace = ResourcesCompat.getFont(mContext!!, R.font.general_sans_medium)

        textInputLayout?.typeface = typeFace
    }



    fun setAfterTextChangedListener(listener: (String) -> Unit) {
        val editText = textInputEditText

        if (editText != null) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    listener.invoke(s.toString())
                    if (checkEditTextIsBlank()) {
                        setTextAppearanceOnBlank()
                        textInputLayout?.hint = hintText
                    }
                }
            })
        }
    }


    private fun setColorStateList(btnTxtColor: Int): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_active),
            intArrayOf(-android.R.attr.state_active)
        )
        val colors = intArrayOf(
            btnTxtColor,
            btnTxtColor,
            btnTxtColor,
            btnTxtColor,
            btnTxtColor,
            btnTxtColor,
            btnTxtColor
        )
        val colorStateList = ColorStateList(states, colors)



        return colorStateList
    }


}