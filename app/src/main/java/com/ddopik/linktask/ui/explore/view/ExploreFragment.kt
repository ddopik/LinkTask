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
import com.ddopik.linktask.ui.explore.model.ArticlesResponse
import com.ddopik.linktask.ui.explore.viewmodel.ExploreViewModel
import com.ddopik.linktask.ui.main.OnSearchAction
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_explore.*


class ExploreFragment : BaseFragment(), OnSearchAction {


    var exploreViewModel: ExploreViewModel? = null
    var articlesAdapter: ArticlesAdapter? = null
    var articlesList: MutableList<Article> = mutableListOf()
    var diagonalInches: Double? = null

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

        initializeRecyclerView()

        val metrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        val yInches: Float = metrics.heightPixels / metrics.ydpi
        val xInches: Float = metrics.widthPixels / metrics.xdpi
        diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches.toDouble())

        /**
         * Detect whither to view A single view or split view in case Tablet detected
         */
////////////////////////////
        if (diagonalInches!! >= 6.5) {
            //Tablet
            fragment_article_details_container_view.visibility = View.VISIBLE
        } else {
            //Phone
            fragment_article_details_container_view.visibility = View.GONE
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
        articlesAdapter?.notifyDataSetChanged()

    }

    private fun initializeRecyclerView() {
        articlesAdapter = ArticlesAdapter(articlesList)
        rv_articles_list.adapter = articlesAdapter

        articlesAdapter?.onArticleSelected = object : ArticlesAdapter.OnArticleSelected {
            override fun onArticleClickListener(article: Article?) {
                if (diagonalInches!! >= 6.5) {
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
    }

    fun getTestLet(): ArticlesResponse {


        val obj = Gson().fromJson(
            "{\n" +
                    "    \"status\": \"ok\",\n" +
                    "    \"source\": \"the-next-web\",\n" +
                    "    \"sortBy\": \"top\",\n" +
                    "    \"articles\": [\n" +
                    "        {\n" +
                    "            \"author\": \"Mix\",\n" +
                    "            \"title\": \"Astro’s A50 wireless headset feels and sounds great (but it costs more than it should)\",\n" +
                    "            \"description\": \"When it comes to gaming, few things infuriate me more than an enemy catching me off-guard. But there’s not much you can do to prevent that from happening if your audio gear sucks — and I’ve had enough. For the past few weeks, I’ve been testing out the fourth iteration of the Astro A50 gaming […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/plugged/2019/11/27/astro-a50-review-headset-gaming/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/plugged?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2Flogitech-astro-a50-headset-gaming.jpg&signature=fb620123b3a68a2842fc6deb9f468bfc\",\n" +
                    "            \"publishedAt\": \"2019-11-27T15:25:09Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Callum Booth\",\n" +
                    "            \"title\": \"An iPhone 11 Pro Max review especially for you, human\",\n" +
                    "            \"description\": \"Reviewing an iPhone is not like covering any other device. People who are already in the Apple ecosystem will probably upgrade when they’re ready, not when you say so. And Android folks? Well, they aren’t going to suddenly jump ship because of a single article. Then you have specs. While companies like Huawei, Samsung, and […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/plugged/2019/11/27/apple-iphone-11-pro-max-review-human/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/plugged?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2Fheader-image-iphone-11-pro-max.png&signature=b1324b79f68b9ffa3e8873f515a85818\",\n" +
                    "            \"publishedAt\": \"2019-11-27T14:36:38Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Matthew Beedham\",\n" +
                    "            \"title\": \"Hackers mass-scan for Docker vulnerability to mine Monero cryptocurrency\",\n" +
                    "            \"description\": \"A hacking group is reportedly performing a mass-scan of the internet in search of vulnerable ports on systems using enterprise sandbox software Docker to mine cryptocurrency. According to security researchers at Bad Packets, the scans, which began over the weekend, identify vulnerabilities that allow bad actors to inject malicious code that deploys a cryptocurrency miner […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/hardfork/2019/11/27/hackers-mass-scan-for-docker-vulnerability-to-mine-monero-cryptocurrency/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/hardfork?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FDocker-cryptocurrency-miner-crypto-monero-xmrig-blockchain-hard-fork-scan-malware-vulnerability.jpg&signature=949e07a5d41be4b86416a474707c5cf8\",\n" +
                    "            \"publishedAt\": \"2019-11-27T13:47:09Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Cara Curtis\",\n" +
                    "            \"title\": \"TikTok denies suspending makeup tutorial teen for raising awareness of China's detention camps\",\n" +
                    "            \"description\": \"A 17-year old Muslim girl on TikTok has been uploading now-viral makeup tutorials, but with a political twist. In a video shared by “@getmefamouspartthree,” Feroza Aziz seemingly ...\",\n" +
                    "            \"url\": \"https://thenextweb.com/tech/2019/11/27/tiktok-denies-suspending-makeup-tutorial-teen-for-raising-awareness-of-chinas-detention-camps/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/tnw?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FCopy-of-Copy-of-Copy-of-...-2-1.png&signature=d7923e8edd520c18c2373fbf63507ccd\",\n" +
                    "            \"publishedAt\": \"2019-11-27T13:14:56Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Yessi Bello Perez\",\n" +
                    "            \"title\": \"Judge orders Telegram founder Pavel Durov to testify over its crypto ICO\",\n" +
                    "            \"description\": \"Telegram’s founder and CEO Pavel Durov should testify in the US Securities Exchange Commission‘s (SEC) case against the company’s controversial digital token, a US federal judge has ruled. A document signed by District Judge P. Kevin Castel at the New York Southern District Court earlier this week, says Durov will be deposed “on January 7 […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/hardfork/2019/11/27/telegram-founder-pavel-durov-testify-cryptocurrency-ico/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/hardfork?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2Ftelegram-judge-court.jpg&signature=02dd2d557677291a7686678a9719e513\",\n" +
                    "            \"publishedAt\": \"2019-11-27T13:08:03Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Mix\",\n" +
                    "            \"title\": \"This YUGE font generator lets you write like Trump\",\n" +
                    "            \"description\": \"\\\"I WANT NOTHING. I WANT NOTHING,\\\" US President Donald Trump wrote in a note addressing US Ambassador Gordon Sondland's impeachment testimony. \\\"I WANT NO QUID PRO QUO.\\\"\\r\\n\\r\\nThe ...\",\n" +
                    "            \"url\": \"https://thenextweb.com/shareables/2019/11/27/donald-trump-font-generator-note/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/tnw?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FScreen-Shot-2019-11-27-at-1.49.07-PM.png&signature=e8c98e83f86081134fb311e6ca53b9c8\",\n" +
                    "            \"publishedAt\": \"2019-11-27T12:35:37Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"The Conversation\",\n" +
                    "            \"title\": \"The sixth mass extinction has already claimed its first victims: Humans\",\n" +
                    "            \"description\": \"Nine human species walked the Earth 300,000 years ago. Now there is just one. The Neanderthals, Homo neanderthalensis, were stocky hunters adapted to Europe’s cold steppes. The related ...\",\n" +
                    "            \"url\": \"https://thenextweb.com/syndication/2019/11/27/the-sixth-mass-extinction-has-already-claimed-its-first-victims-humans/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/tnw?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2Fs-copy-4.jpg&signature=fe35e982ebf9be1354e4a6ab8fe2dca2\",\n" +
                    "            \"publishedAt\": \"2019-11-27T12:45:58Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Ivan Mehta\",\n" +
                    "            \"title\": \"CHEAP: Don’t be a fool. Get Sony’s legendary WH-1000Xm3 cans for just \$278\",\n" +
                    "            \"description\": \"Welcome to CHEAP, our series about things that are good, but most of all, cheap. CHEAP! There are times when you see a product and think “I may buy it because it’s good for its price.” Then, there are some products that you just have to get when they’re discounted. Sony’s famous WH-1000Xm3 wireless noise […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/plugged/2019/11/27/cheap-dont-be-a-fool-get-sonys-legendary-wh-1000xm3-cans-for-just-278/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/plugged?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FSonydeal.jpg&signature=600bad209fe96b5d16443018e5c4a921\",\n" +
                    "            \"publishedAt\": \"2019-11-27T11:46:54Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Amanda Milligan\",\n" +
                    "            \"title\": \"How to make your brand’s content people-centric\",\n" +
                    "            \"description\": \"Content might be made up of words, pictures, code, and video. But good content is also made up of some kind of human connection, as well — it addresses a concern, taps into an emotion, or bonds through some sort of commonality. Content is created by people and for people (sorry, I think this is […]\",\n" +
                    "            \"url\": \"https://thenextweb.com/podium/2019/11/27/how-to-make-your-brands-content-people-centric/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/podium?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FUntitled-design15.png&signature=93af496108c203b4cb118f8ab990b086\",\n" +
                    "            \"publishedAt\": \"2019-11-27T11:00:24Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"author\": \"Cara Curtis\",\n" +
                    "            \"title\": \"Facebook's only fact-checker for Dutch news quits over political ad controversy\",\n" +
                    "            \"description\": \"Yesterday, the Dutch digital newspaper, NU.nl, which was Facebook’s only partner, cut ties with the social network and published a blog post, written by Gert-Jaap Heokman, NU.nl’s ...\",\n" +
                    "            \"url\": \"https://thenextweb.com/tech/2019/11/27/facebooks-only-fact-checker-for-dutch-news-quits-over-political-ad-controversy/\",\n" +
                    "            \"urlToImage\": \"https://img-cdn.tnwcdn.com/image/tnw?filter_last=1&fit=1280%2C640&url=https%3A%2F%2Fcdn0.tnwcdn.com%2Fwp-content%2Fblogs.dir%2F1%2Ffiles%2F2019%2F11%2FCopy-of-Copy-of-Copy-of-...-1-6.png&signature=09336b1aa4529dd370a0391ede76f2a8\",\n" +
                    "            \"publishedAt\": \"2019-11-27T10:27:27Z\"\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}", ArticlesResponse::class.java
        )

        return obj
    }
}