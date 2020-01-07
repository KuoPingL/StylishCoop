package app.appworks.school.stylish.ext

import androidx.fragment.app.Fragment
import app.appworks.school.stylish.StylishApplication
import app.appworks.school.stylish.catalog.CatalogTypeFilter
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.data.User
import app.appworks.school.stylish.factory.CatalogItemViewModelFactory
import app.appworks.school.stylish.factory.ProductViewModelFactory
import app.appworks.school.stylish.factory.ProfileViewModelFactory
import app.appworks.school.stylish.factory.ViewModelFactory
import app.appworks.school.stylish.network.Order
import app.appworks.school.stylish.network.Sort

/**
 * Created by Wayne Chen in Jul. 2019.
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(user: User?): ProfileViewModelFactory {
    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
    return ProfileViewModelFactory(repository, user)
}

fun Fragment.getVmFactory(product: Product): ProductViewModelFactory {
    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
    return ProductViewModelFactory(repository, product)
}

//pass two more parameters (sort and order) to function getVmFactory
fun Fragment.getVmFactory(catalogType: CatalogTypeFilter, sort: Sort, order: Order): CatalogItemViewModelFactory {
    val repository = (requireContext().applicationContext as StylishApplication).stylishRepository
    return CatalogItemViewModelFactory(repository, catalogType, sort, order)
}