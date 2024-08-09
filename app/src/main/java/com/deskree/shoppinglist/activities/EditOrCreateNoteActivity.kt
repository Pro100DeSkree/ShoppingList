package com.deskree.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.text.toSpanned
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.databinding.ActivityNewNoteBinding
import com.deskree.shoppinglist.dialogs.ExitDialog
import com.deskree.shoppinglist.entities.NoteItem
import com.deskree.shoppinglist.fragments.NoteFragment
import com.deskree.shoppinglist.utils.HtmlManager
import com.deskree.shoppinglist.utils.MyTouchListener
import com.deskree.shoppinglist.utils.ShareHelper
import com.deskree.shoppinglist.utils.TimeManager

class EditOrCreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var actionBarTitle = "Створити"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getNote()
        actionBarSettings()
        init()
        onClockColorPicker()
        binding.edtTitle.requestFocus()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
    }

    private fun onClockColorPicker() = with(binding) {
        imbPickerBlack.setOnClickListener {
            setStyleText(R.color.picker_black)
        }
        imbPickerBlue.setOnClickListener {
            setStyleText(R.color.picker_blue)
        }
        imbPickerGray.setOnClickListener {
            setStyleText(R.color.picker_gray)
        }
        imbPickerPurple.setOnClickListener {
            setStyleText(R.color.picker_purple)
        }
        imbPickerRed.setOnClickListener {
            setStyleText(R.color.picker_red)
        }
        imbPickerYellow.setOnClickListener {
            setStyleText(R.color.picker_yellow)
        }
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            fillNote()
            actionBarTitle = sNote.title
        }
    }

    private fun fillNote() = with(binding) {
        edtTitle.setText(note?.title)
        edtDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bmSave -> {
                setMainResult()
                finish()
            }

            android.R.id.home -> with(binding) {

                if ((edtTitle.text.toString().trim().isNotEmpty()
                            && edtDescription.text.isNotEmpty())
                    || edtDescription.text.isNotEmpty()
                ) {

                    if (note?.title == edtTitle.text.toString().trim()
                        && HtmlManager.getFromHtml(note?.content!!).toString().trim()
                        == edtDescription.text.toString().trim()
                    ) {
                        finish()
                    } else {
                        ExitDialog.showDialog(
                            this@EditOrCreateNoteActivity,
                            object : ExitDialog.Listener {
                                override fun save() {
                                    setMainResult()
                                }

                                override fun exit() {
                                    finish()
                                }
                            })
                    }
                } else {
                    finish()
                }
            }

            R.id.bmBold -> {
                setStyleText()
            }

            R.id.bmFormatColor -> {
                if (binding.colorPicker.isShown)
                    closeColorPicker()
                else
                    openColorPicker()
            }

            R.id.share_note -> {
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareNote(note!!),
                        getString(R.string.shareBy)
                    )
                )

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStyleText(colorID: Int? = null) = with(binding) {
        val startPos = edtDescription.selectionStart
        val endPos = edtDescription.selectionEnd

        if (colorID != null) {
            // Установка / прибитрання жирного шрифту
            val styles =
                edtDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
            if (styles.isNotEmpty()) edtDescription.text.removeSpan(styles[0])

            edtDescription.text.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@EditOrCreateNoteActivity,
                        colorID
                    )
                ),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            // Установка / прибирання кольору шрифту

            // МАЮ БАГ З НАКЛАДАННЯМ КОЛЬОРІВ
            val styles = edtDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
            var boldStyle: StyleSpan? = null
            if (styles.isNotEmpty()) {
                edtDescription.text.removeSpan(styles[0])
            } else {
                boldStyle = StyleSpan(Typeface.BOLD)
            }
            edtDescription.text.setSpan(
                boldStyle,
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        edtDescription.text.trim()
//        edtDescription.setSelection(endPos)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edtTitle.text.toString().trim(),
            content = HtmlManager.toHtml(edtDescription.text.trim().toSpanned()),
            changeDateTime = TimeManager.getCurrentTime()
        )
    }

    private fun createNewNote(): NoteItem = with(binding) {
        return NoteItem(
            null,
            edtTitle.text.toString().trim(),
            HtmlManager.toHtml(edtDescription.text.trim().toSpanned()),
            TimeManager.getCurrentTime(),
            category = ""
        )
    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
        ab?.title = actionBarTitle
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val closeAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        closeAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })
        binding.colorPicker.startAnimation(closeAnim)
    }
}