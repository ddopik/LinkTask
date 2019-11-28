package com.ddopik.linktask.ui.explore.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class ArticlesResponse {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("source")
    @Expose
    var source: String? = null
    @SerializedName("sortBy")
    @Expose
    var sortBy: String? = null
    @SerializedName("articles")
    @Expose
    var articles: MutableList<Article>? = null
}