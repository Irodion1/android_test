package ru.skillbranch.skillarticles.ui.custom.behaviors

import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.skillbranch.skillarticles.ui.custom.ArticleSubmenu

class SubmenuBehavior : CoordinatorLayout.Behavior<ArticleSubmenu>(){
//    override fun layoutDependsOn(
//            parent: CoordinatorLayout,
//            child: ArticleSubmenu,
//            dependency: View
//    ): Boolean {
//        return dependency is Bottombar
//    }
//
//    override fun onDependentViewChanged(
//            parent: CoordinatorLayout,
//            child: ArticleSubmenu,
//            dependency: View
//    ): Boolean {
//        return if (child.isOpen && dependency is Bottombar && dependency.translationY >= 0) {
//            animate(child, dependency)
//            true
//        } else {
//            false
//        }
//    }
//
//    private fun animate(child: ArticleSubmenu, dependency: Bottombar) {
//        val fraction = dependency.translationY / dependency.minHeight
//        val offset = (child.width + child.marginRight) * fraction
//        if (offset != child.translationX) {
//            child.translationX = offset
//        }
//    }
}