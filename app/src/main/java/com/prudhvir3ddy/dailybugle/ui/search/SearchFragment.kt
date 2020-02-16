package com.prudhvir3ddy.dailybugle.ui.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.prudhvir3ddy.dailybugle.MyApplication
import com.prudhvir3ddy.dailybugle.R
import com.prudhvir3ddy.dailybugle.ui.BaseFragment
import com.prudhvir3ddy.dailybugle.ui.NewsAdapter
import com.prudhvir3ddy.dailybugle.ui.SearchFragmentDirections
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : BaseFragment<SearchViewModel>() {

    override fun getViewModelClass(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        (context?.applicationContext as MyApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        val newsAdapter = NewsAdapter()

        viewModel.foundNews.observe(this, Observer {
            newsAdapter.submitList(it)
        })


        viewModel.status.observe(this, Observer {
            if (it) {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToNoInternetFragment())
                viewModel.resetStatus()
            }
        })

        rootView.apply {
            search_input.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchNews(search_input.text.toString())
                    search_input.text.clear()
                }
                false
            }
            recycler_view_search_news.adapter = newsAdapter


            bottom_navigation.setOnNavigationItemSelectedListener {
                when (it.itemId) {

                    R.id.menu_item_home -> {
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    R.id.menu_item_save -> {
                        val action =
                            SearchFragmentDirections.actionSearchFragmentToSaveFragment()
                        findNavController().navigate(action)
                    }
                }
                false
            }
        }
        return rootView
    }


}