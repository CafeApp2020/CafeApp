package com.cafeapp.mycafe.frameworks.view.dishesadd

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.picasso.setImage
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedMsg
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.less.repository.db.room.DishesEntity
import kotlinx.android.synthetic.main.fragment_dishesadd.*
import org.koin.androidx.scope.currentScope

// Экран для добавления/редактирования блюда
class DishesAddFragment : Fragment() {
    var currentCategoryID: Long = -1L
    private var currentDishId: Long = -1L
    private val dishViewModel: DishViewModel by currentScope.inject()
    var currentImagePath: String = ""

    private val sharedModel by lazy {
        activity?.let { ViewModelProvider(it).get(SharedViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dishesadd, container, false)

        dishViewModel.dishViewState.observe(viewLifecycleOwner, { state ->
            state.error?.let { error ->
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                return@observe
            }

            if (state.saveOk) {
                Toast.makeText(activity, getString(R.string.saveok_title), Toast.LENGTH_LONG).show()

                state.dish?.category_id?.let { category_id ->
                    sharedModel?.select(SharedMsg(MsgState.DISHESLIST, category_id))
                }
            }

            if (state.loadOk) {
                state.dish?.let { dish -> showDish(dish) }
            }
        })

        sharedModel?.getSelected()?.observe(viewLifecycleOwner, { msg ->
            when (msg.stateName) {
                MsgState.ADDDISH ->
                    if (msg.value is Long) {
                        currentCategoryID = msg.value
                    }
                MsgState.EDITDISH ->
                    if (msg.value is Long) {
                        dishViewModel.getDish(msg.value)
                        currentDishId = msg.value
                    }
            }
        })

        val fab = activity?.findViewById<FloatingActionButton>(R.id.activityFab)

        fab?.setImageResource(android.R.drawable.ic_menu_save)
        fab?.setOnClickListener {
            saveDish()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_dish_image_imageview.setOnClickListener { showSelectionDialog() }
    }

    private fun showDish(dish: DishesEntity) {
        dish.name?.let { name -> sharedModel?.select(SharedMsg(MsgState.SETTOOLBARTITLE, name)) }

        currentCategoryID = dish.category_id
        dishNameTIT.setText(dish.name)
        priceTIT.setText(dish.price.toString())
        weightTIT.setText(dish.weight.toString())
        descriptionTIT.setText(dish.description)

        val imagePath = dish.imagepath.toString()

        if (imagePath.isNotEmpty()) {
            currentImagePath = imagePath
            setImage(imagePath, fragment_dish_image_imageview)
        }
    }

    private fun saveDish() {
        if (!dishNameTIT.text.toString().isNullOrBlank() && !priceTIT.text.toString()
                .isNullOrBlank()
        ) {
            val dish: DishesEntity?

            if (weightTIT.text.toString().isEmpty()) {
                weightTIT.setText(getString(R.string.weight_not_specified))
            }

            dish = DishesEntity(
                category_id = currentCategoryID,
                name = dishNameTIT.text.toString(),
                description = descriptionTIT.text.toString(),
                price = priceTIT.text.toString().toFloat(),
                weight = weightTIT.text.toString().toFloat(),
                imagepath = currentImagePath
            )

            if (currentDishId > 0)  // обновляем блюдо иначе добавляем новое
                dish.id = currentDishId

            dish.let { dishViewModel.saveDish(dish) }
        } else {
            Toast.makeText(
                activity,
                getString(R.string.required_fields_is_blank),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSelectionDialog() {
        val dialog = ImageSelectionDialogFragment.newInstance()
        dialog.show(childFragmentManager, "ImageSelectionDialogFragment")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    currentImagePath = data?.data.toString()
                    data?.data?.let {
                        activity?.contentResolver?.takePersistableUriPermission(it,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    setImage(currentImagePath, fragment_dish_image_imageview)
                }
                REQUEST_CODE_CAMERA -> {
                    currentImagePath = fragment_dish_image_imageview.tag.toString()
                    setImage(currentImagePath, fragment_dish_image_imageview)
                }
                REQUEST_CODE_DELETE_IMAGE -> {
                    currentImagePath = ""
                    fragment_dish_image_imageview?.setImageResource(R.drawable.ic_96px_add_photo)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 100
        private const val REQUEST_CODE_GALLERY = 200
        private const val REQUEST_CODE_DELETE_IMAGE = 300
    }
}