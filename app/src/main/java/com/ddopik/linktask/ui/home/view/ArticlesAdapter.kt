package com.ddopik.linktask.ui.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ddopik.attendonb.app.LinkTaskApp
import com.ddopik.linktask.R
import com.ddopik.linktask.ui.home.model.Article

class ArticlesAdapter(val userList: MutableList<Article>) : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {

    lateinit var filteredArticleList: MutableList<Article>
    var onArticleSelected: OnArticleSelected? = null

    init {
        filteredArticleList = userList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
            LayoutInflater.from(LinkTaskApp.app?.applicationContext).inflate(
                R.layout.view_holder_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return filteredArticleList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {


        Glide.with(LinkTaskApp.app?.baseContext!!)
            .load(filteredArticleList[position].urlToImage)
            .placeholder(R.drawable.ic_image_search)
            .error(R.drawable.ic_image_search)
            .into(holder.articleImg)


        holder.articleTitle.text = filteredArticleList[position].title
        holder.articleMadeBy.text =LinkTaskApp.app!!.baseContext.resources.getString(R.string.by)+ filteredArticleList[position].author
        holder.articleDate.text = filteredArticleList[position].publishedAt?.substringBeforeLast("-")



        onArticleSelected?.let {
            holder.articleContainer.setOnClickListener {
                onArticleSelected?.onArticleClickListener(filteredArticleList[position])
            }
        }


    }


    class ArticlesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val articleContainer = view.findViewById<View>(R.id.article_container)
        val articleImg = view.findViewById<ImageView>(R.id.img_article)
        val articleTitle = view.findViewById<TextView>(R.id.article_title)
        val articleMadeBy = view.findViewById<TextView>(R.id.article_made_by)
        val articleDate = view.findViewById<TextView>(R.id.article_date)
    }


    interface OnArticleSelected {
        fun onArticleClickListener(article: Article?)
    }
}