package com.ddopik.linktask.ui.explore.viewmodel

import CustomErrorUtils
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ddopik.attendonb.network.BaseNetWorkApi
import com.ddopik.linktask.ui.explore.model.Article
import com.ddopik.linktask.ui.explore.model.ArticlesResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers


class ExploreViewModel : ViewModel() {

    private val TAG=ExploreViewModel::class.java.name


    companion object{

        fun getInstance(fragment:Fragment):ExploreViewModel{
            return ViewModelProviders.of(fragment).get(ExploreViewModel::class.java)
        }
    }

    private val articleList: MutableLiveData<MutableList<Article>> = MutableLiveData()
    private val articlesStats: MutableLiveData<Boolean> = MutableLiveData()

    fun onArticleListChanged(): LiveData<MutableList<Article>> = articleList
    fun onArticleProgressChanged(): LiveData<Boolean> = articlesStats

    fun getArticles() {
        articlesStats.postValue(true)
//        Observable.zip(BaseNetWorkApi.getArticleList_1(),BaseNetWorkApi.getArticleList_2(),object :BiFunction() <MutableList<Article>,MutableList<Article> >)
    }

    @SuppressLint("CheckResult")
     fun getArticlesList() { // here we are using zip operator to combine both request
        articlesStats.postValue(true)
        Observable.zip(BaseNetWorkApi.getArticleList1(), BaseNetWorkApi.getArticleList2(),
            BiFunction<ArticlesResponse, ArticlesResponse, MutableList<Article>> { article_1, article_2 ->


                article_1.articles.apply {
                    article_2.articles?.let { this?.addAll(it) }
                }!!
            })
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                articlesStats.postValue(false)
                articleList.postValue(it)
            },{
                t: Throwable? ->
                articleList.postValue(null)
                articlesStats.postValue(false)
                CustomErrorUtils.setError(TAG,t)
            })
    }

}
