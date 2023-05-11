package io.github.araujodanilo.splitthebill.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.github.araujodanilo.splitthebill.R
import io.github.araujodanilo.splitthebill.databinding.TilePersonBinding
import io.github.araujodanilo.splitthebill.model.Person

class PersonAdapter(context: Context, private val personList: MutableList<Person>): ArrayAdapter<Person>(context, R.layout.tile_person, personList) {
    private lateinit var tilePersonBinding: TilePersonBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val person = personList[position]

        var tilePersonView = convertView
        if (tilePersonView == null) {
            tilePersonBinding = TilePersonBinding.inflate(context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater, parent, false)
            tilePersonView = tilePersonBinding.root

            val personView = personView(
                tilePersonBinding.pName,
                tilePersonBinding.pValue,
                tilePersonBinding.pDifference,
            )

            tilePersonView.tag = personView
        }

        (tilePersonView.tag as personView).personNameText.text = person.name
        (tilePersonView.tag as personView).personSpentValueText.text = String.format(context.getString(R.string.value_template), String.format("%.2f", person.value))
        (tilePersonView.tag as personView).personToPayText.text = String.format(context.getString(R.string.difference_template), String.format("%.2f", person.difference))

        return tilePersonView
    }

    private data class personView(
        val personNameText: TextView,
        val personSpentValueText: TextView,
        val personToPayText: TextView
    )
}