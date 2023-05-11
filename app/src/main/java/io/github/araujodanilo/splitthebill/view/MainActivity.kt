package io.github.araujodanilo.splitthebill.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import io.github.araujodanilo.splitthebill.R
import io.github.araujodanilo.splitthebill.adapter.PersonAdapter
import io.github.araujodanilo.splitthebill.controller.PersonController
import io.github.araujodanilo.splitthebill.databinding.ActivityMainBinding
import io.github.araujodanilo.splitthebill.model.Person

class MainActivity : BaseActivity() {
    private val amb: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val personController: PersonController by lazy { PersonController(this) }
    private var personList = mutableListOf<Person>()
    private lateinit var billAdapter: PersonAdapter
    private lateinit var carl: ActivityResultLauncher<Intent>

    fun updatePersonDiferrence() {
        supportActionBar?.subtitle = ""

        if (personList.isEmpty()) return

        val total = personList
            .map { person -> person.value }
            .reduce { a, b -> a + b }
        val limitPerPerson = total / personList.size.toDouble()

        supportActionBar?.subtitle = "Total: R$ ${String.format("%.2f", total)}"

        personList.forEach { person ->
            person.difference = limitPerPerson - person.value
        }
    }

    fun refreshPersonList() {
        personController.list { persons ->
            personList.clear()
            personList.addAll(persons)
            runOnUiThread {
                updatePersonDiferrence()
                billAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        billAdapter = PersonAdapter(this, personList)
        amb.personLv.adapter = billAdapter
        refreshPersonList()

        carl =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    refreshPersonList()
                }
            }

        amb.personLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val person = personList[position]
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_PERSON, person)
                personIntent.putExtra(EXTRA_OPERATION, PersonActivity.Operations.EDIT)
                carl.launch(personIntent)
            }

        registerForContextMenu(amb.personLv)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addPerson -> {
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_OPERATION, PersonActivity.Operations.CREATE)
                carl.launch(personIntent)
                true
            }
            R.id.clearAll -> {
                AlertDialog.Builder(this)
                    .setMessage("Apagar todas as pessoas?")
                    .setPositiveButton(
                        "Sim"
                    ) { _, _ ->
                        personController.clear {
                            refreshPersonList()
                        }
                    }
                    .setNegativeButton("Não", null)
                    .show()
                true
            }
            else -> false
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.menu_person, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val person = personList[position]

        return when (item.itemId) {
            R.id.editPerson -> {
                val personIntent = Intent(this@MainActivity, PersonActivity::class.java)
                personIntent.putExtra(EXTRA_OPERATION, PersonActivity.Operations.EDIT)
                personIntent.putExtra(EXTRA_PERSON, person)
                carl.launch(personIntent)
                true
            }
            R.id.deletePerson -> {
                personController.delete(person) {
                    runOnUiThread {
                        refreshPersonList()
                        Toast.makeText(this, "Pessoa deletada", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}