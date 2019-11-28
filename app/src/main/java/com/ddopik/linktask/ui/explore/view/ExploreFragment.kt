package com.ddopik.linktask.ui.explore.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ddopik.attendonb.base.BaseFragment
import com.ddopik.linktask.R
import com.ddopik.linktask.ui.explore.model.Article
import com.ddopik.linktask.ui.explore.viewmodel.ExploreViewModel
import com.ddopik.linktask.ui.main.OnSearchAction
import kotlinx.android.synthetic.main.fragment_explore.*


class ExploreFragment : BaseFragment(), OnSearchAction {


    var exploreViewModel: ExploreViewModel? = null
    var articlesAdapter: ArticlesAdapter? = null
    private val articlesList = mutableListOf<Article>()

    companion object {
        private var INSTANCE: ExploreFragment? = null

        val TAG = ExploreFragment::javaClass.name
        fun getInstance(): ExploreFragment {
            INSTANCE = ExploreFragment()
            return INSTANCE!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_explore, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.ic_search)?.isVisible = true
    }

    override fun initView() {


        articlesAdapter = ArticlesAdapter(articlesList)
        rv_articles_list.adapter = articlesAdapter

        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        val yInches: Float = metrics.heightPixels / metrics.ydpi
        val xInches: Float = metrics.widthPixels / metrics.xdpi
        val diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches.toDouble())

        /**
         * Detect whither to view A single view or split view in case Tablet detected
         */
////////////////////////////
        if (diagonalInches >= 6.5) {
            //Tablet
            fragment_article_details_container_view.visibility = View.VISIBLE
        } else {
            //Phone
            fragment_article_details_container_view.visibility = View.GONE
        }

        articlesAdapter?.onArticleSelected = object : ArticlesAdapter.OnArticleSelected {
            override fun onArticleClickListener(article: Article?) {
                if (diagonalInches >= 6.5) {
                    //Tablet
                    rv_articles_list.visibility = View.VISIBLE
                } else {
                    //phone
                    rv_articles_list.visibility = View.GONE
                }
                fragment_article_details_container_view.visibility = View.VISIBLE
                activity?.supportFragmentManager!!.beginTransaction()
                    .replace(
                        R.id.fragment_article_details_container_view,
                        ArticleDetailsFragment.getInstance(article!!),
                        ArticleDetailsFragment.TAG
                    )
                    .commit()
            }
        }
//////////////////////
        exploreViewModel = ExploreViewModel.getInstance(this)
        exploreViewModel?.getArticlesList()
    }

    override fun initObservers() {
        exploreViewModel?.onArticleProgressChanged()?.observe(viewLifecycleOwner, Observer {
            if (it) {
                pb_articles.visibility = View.VISIBLE
            } else {
                pb_articles.visibility = View.GONE
            }
        })


        exploreViewModel?.onArticleListChanged()?.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                showToast("Error getting data")
            } else {
                articlesList.clear()
                articlesList.addAll(it)
                articlesAdapter?.notifyDataSetChanged()
            }
        })
    }


    override fun onQueryComplete(query: String?) {
        articlesAdapter!!.filter.filter(query)

    }
}