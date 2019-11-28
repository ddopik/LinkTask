package com.ddopik.linktask.ui.explore.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ddopik.attendonb.app.LinkTaskApp
import com.ddopik.linktask.R
import com.ddopik.linktask.ui.explore.model.Article
import java.util.ArrayList

class ArticlesAdapter(val articlesList: MutableList<Article>) : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>(),
    Filterable {

    lateinit var filteredArticleList: MutableList<Article>
    var onArticleSelected: OnArticleSelected? = null

    init {
        filteredArticleList = articlesList
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredArticleList = articlesList
                } else {
                    val filteredList = ArrayList<Article>()
                    for (row in articlesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.title?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    filteredArticleList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredArticleList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                filteredArticleList = filterResults.values as ArrayList<Article>
                filteredArticleList.clear()
                notifyDataSetChanged()
            }
        }
    }

}