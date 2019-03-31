package de.platon42.intellij.plugins.cajon.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiType

class AssertThatObjectIsNotNullInspection : AbstractAssertJInspection() {

    companion object {
        private const val DISPLAY_NAME = "Asserting non-null"
    }

    override fun getDisplayName() = DISPLAY_NAME

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                super.visitMethodCallExpression(expression)
                if (!IS_NOT_EQUAL_TO_OBJECT.test(expression)) {
                    return
                }

                if (expression.argumentList.expressions[0].type == PsiType.NULL) {
                    registerSimplifyMethod(holder, expression, "isNotNull()")
                }
            }
        }
    }
}