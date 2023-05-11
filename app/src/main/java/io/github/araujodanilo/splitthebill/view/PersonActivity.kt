package io.github.araujodanilo.splitthebill.view

import android.app.Activity
import android.os.Build
import android.os.Bundle
import io.github.araujodanilo.splitthebill.controller.PersonController
import io.github.araujodanilo.splitthebill.databinding.ActivityPersonBinding
import io.github.araujodanilo.splitthebill.model.Person

class PersonActivity : BaseActivity() {
    enum class Operations {
        CREATE,
        EDIT
    }

    private val personController: PersonController by lazy { PersonController(this) }

    private val apb: ActivityPersonBinding by lazy { ActivityPersonBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        val receivedPerson = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_PERSON, Person::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_PERSON)
        }

        val operations = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_OPERATION, Operations::class.java)
        } else {
            intent.getSerializableExtra(EXTRA_OPERATION)
        }

        apb.createButton.text = if (operations == Operations.CREATE) {
            "Criar"
        } else {
            "Atualizar"
        }

        if (receivedPerson != null) {
            apb.nameEditText.setText(receivedPerson.name)
            apb.valueEditText.setText(receivedPerson.value.toString())
            apb.descriptionEditText.setText(receivedPerson.description)
        }

        apb.createButton.setOnClickListener {
            if (operations == Operations.CREATE) {
                personController.create(
                    Person(
                        null,
                        apb.nameEditText.text.toString(),
                        apb.descriptionEditText.text.toString(),
                        apb.valueEditText.text.toString().toDouble(),
                        0.0,
                    )
                ) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else if (operations == Operations.EDIT) {
                if (receivedPerson != null) {
                    personController.update(
                        Person(
                            receivedPerson.id,
                            apb.nameEditText.text.toString(),
                            apb.descriptionEditText.text.toString(),
                            apb.valueEditText.text.toString().toDouble(),
                            receivedPerson.difference,
                        )
                    ) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }
}