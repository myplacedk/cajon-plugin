package de.platon42.intellij.plugins.cajon.inspections

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiType
import de.platon42.intellij.plugins.cajon.quickfixes.ReplaceSimpleMethodCallQuickFix
import org.jetbrains.annotations.NonNls

class AssertThatObjectIsNotNullInspection : AbstractAssertJInspection() {

    companion object {
        @NonNls
        private val DISPLAY_NAME = "Asserting non-null"

        @NonNls
        private val INSPECTION_MESSAGE = "isNotEqualTo(null) can be simplified to isNotNull()"

        @NonNls
        private val QUICKFIX_DESCRIPTION = "Replace isNotEqualTo(null) with isNotNull()"
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
                    holder.registerProblem(
                        expression,
                        INSPECTION_MESSAGE,
                        ProblemHighlightType.INFORMATION,
                        null as TextRange?,
                        ReplaceSimpleMethodCallQuickFix(QUICKFIX_DESCRIPTION, "isNotNull()")
                    )
                }
            }
        }
    }
}