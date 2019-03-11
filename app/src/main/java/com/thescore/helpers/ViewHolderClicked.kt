package com.thescore.helpers

import android.view.View

interface ViewHolderClicked<T> {
    fun onItemViewClicked(view: View, item: T)
}