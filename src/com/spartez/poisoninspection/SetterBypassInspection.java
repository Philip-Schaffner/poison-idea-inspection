package com.spartez.poisoninspection;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.*;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SetterBypassInspection extends LocalInspectionTool {
    @Nls
    @NotNull
    public String getGroupDisplayName() {
        return "Encapsulation issues";
    }

    @Nls
    @NotNull
    public String getDisplayName() {
        return "Field access bypasses defined getter/setter.";
    }

    @NonNls
    @NotNull
    public String getShortName() {
        return "SetterBypass";
    }

    @Override
    @NotNull
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.ERROR;
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @Override
    @Nullable
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile file, @NotNull final InspectionManager manager, boolean isOnTheFly) {
        final IsPoisonousResolver resolver = new IsPoisonousResolver();
        final List<ProblemDescriptor> problemDescriptors = new ArrayList<ProblemDescriptor>();
        file.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
                PsiElement referred = expression.resolve();
                if (referred instanceof PsiField) {
                    final PsiField field = (PsiField) referred;
                    final boolean isWrite = PsiUtil.isAccessedForWriting(expression);
                    final boolean isRead = PsiUtil.isAccessedForReading(expression);

                    PsiMethod enclosingMethod = PsiTreeUtil.getParentOfType(expression, PsiMethod.class);

                    if (isWrite && resolver.isPoisonous(field, true) && !isImmune(field, enclosingMethod, true)) {
                        final ProblemDescriptor problemDescriptor = manager.createProblemDescriptor(
                                expression,
                                "Access bypasses a defined non-trivial setter",
                                LocalQuickFix.EMPTY_ARRAY,
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        problemDescriptors.add(problemDescriptor);
                    }
                    if (isRead && resolver.isPoisonous(field, false) && !isImmune(field, enclosingMethod, false)) {
                        final ProblemDescriptor problemDescriptor = manager.createProblemDescriptor(
                                expression,
                                "Access bypasses a defined non-trivial getter",
                                LocalQuickFix.EMPTY_ARRAY,
                                ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                        problemDescriptors.add(problemDescriptor);
                    }
                }
                super.visitReferenceExpression(expression);
            }
        });
        return problemDescriptors.isEmpty() ? null : problemDescriptors.toArray(new ProblemDescriptor[problemDescriptors.size()]);
    }

    private static boolean isImmune(PsiField field, PsiMethod context, boolean isWrite) {
        if (context == null) return false;

        final String methodName = context.getName();
        final String beanName = IsPoisonousResolver.getFieldBeanName(field.getName());

        if (isWrite) {
            return methodName.equals("set" + beanName);
        } else {
            return methodName.equals("get" + beanName) || methodName.equals("is" + beanName);
        }
    }

}

