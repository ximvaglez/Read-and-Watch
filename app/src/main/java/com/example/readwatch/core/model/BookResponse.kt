package com.example.readwatch.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
data class BookResponse(
    val items: List<BookItem>?
)
@Parcelize
data class BookItem(
    val volumeInfo: VolumeInfo
) : Parcelable


@Parcelize
data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
) : Parcelable

@Parcelize
data class ImageLinks(
    val thumbnail: String?
) : Parcelable