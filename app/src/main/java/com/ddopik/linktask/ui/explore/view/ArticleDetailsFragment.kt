package com.ddopik.linktask.ui.explore.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import com.bumptech.glide.Glide
import com.ddopik.attendonb.app.LinkTaskApp
import com.ddopik.attendonb.base.BaseFragment
import com.ddopik.linktask.R
import com.ddopik.linktask.appUtilites.Utilities
import com.ddopik.linktask.ui.explore.model.Article
import kotlinx.android.synthetic.main.fragment_article_detail.*
 import kotlin.math.sqrt


class ArticleDetailsFragment : BaseFragment() {


    companion object {
        val TAG = ArticleDetailsFragment::javaClass.name
        private var INSTANCE: ArticleDetailsFragment? = null
        const val ARTICLE_OBJ = "article_obj"
        fun getInstance(article: Article): ArticleDetailsFragment {
            INSTANCE = ArticleDetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARTICLE_OBJ, article)
            INSTANCE?.arguments = bundle
            return INSTANCE!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_search)?.isVisible = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_article_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun initView() {
        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        val yInches: Float = metrics.heightPixels / metrics.ydpi
        val xInches: Float = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt(xInches * xInches + yInches * yInches.toDouble())
        if (diagonalInches >= 6.5) {
            Utilities.setMargins(btn_view_webPage, 12, 12, 12, 12)
        }
        val article = arguments?.getParcelable<Article>(ARTICLE_OBJ)
        article?.let {
            Glide.with(LinkTaskApp.app?.baseContext!!)
                .load(it.urlToImage)
                .placeholder(R.drawable.ic_image_search)
                .error(R.drawable.ic_image_search)
                .into(img_article)
            article_title.text = it.title
            article_made_by.text = it.publishedAt?.substringBeforeLast("-")
            article_description.text = it.description

            btn_view_webPage.setOnClickListener {
                try {
                    val webpage: Uri = Uri.parse(article.url)
                    val myIntent = Intent(Intent.ACTION_VIEW, webpage)
                    startActivity(myIntent)
                } catch (e: ActivityNotFoundException) {
                    showToast(getString(R.string.error_open_webpage))

                    e.printStackTrace()
                }
            }
        }
    }



    override fun initObservers() {
    }


}