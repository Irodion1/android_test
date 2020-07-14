package ru.skillbranch.gameofthrones.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.fragment_houses.toolbar
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.HouseType
import ru.skillbranch.gameofthrones.ui.RootActivity

class CharacterFragment : Fragment() {
    private val args: CharacterFragmentArgs by navArgs()
    private lateinit var viewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, CharacterViewModelFactory(args.id))
            .get(CharacterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val houseType = HouseType.fromString(args.house)
        val arms = houseType.arms

        val rootActivity = requireActivity() as RootActivity
        rootActivity.setSupportActionBar(toolbar)
        rootActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = args.title
        }

        iv_arms.setImageResource(arms)

        collapsing_layout.post { collapsing_layout.requestLayout() }

        viewModel.getCharacter().observe(viewLifecycleOwner, Observer<CharacterFull> { character ->
            if (character == null) return@Observer

            tv_words.text = character.words
            tv_born.text = character.born
            tv_titles.text = character.titles.filter { it.isNotEmpty() }.joinToString("\n")
            tv_aliases.text = character.aliases.filter { it.isNotEmpty() }.joinToString("\n")


            character.father?.let {
                group_father.visibility = View.VISIBLE
                btn_father.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    it.house,
                    it.name
                )
                btn_father.setOnClickListener { findNavController().navigate(action) }
            }

            character.mother?.let {
                group_mother.visibility = View.VISIBLE
                btn_mother.text = it.name
                val action = CharacterFragmentDirections.actionCharacterFragmentSelf(
                    it.id,
                    it.house,
                    it.name
                )
                btn_mother.setOnClickListener { findNavController().navigate(action) }
            }

            if (character.died.isNotBlank()) {
                Snackbar.make(
                    coordinator,
                    "Died in : ${character.died}",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }

        })
    }
}