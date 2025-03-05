package ru.morozovit.recipeviewer.mobile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.morozovit.recipeviewer.mobile.databinding.HomeFragmentBinding

class HomeFragment: Fragment(), Page {
    private var binding: HomeFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding!!) {
            createYourRecipeButton.setOnClickListener {
                startActivity(Intent(activity, CreateActivity::class.java))
            }
            viewARecipeButton.setOnClickListener {
                Snackbar.make(
                    homeMainLayout,
                    R.string.not_implemented,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}