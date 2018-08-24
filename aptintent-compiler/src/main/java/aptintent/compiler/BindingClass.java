package aptintent.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static aptintent.compiler.UsedClassName.*;

final class BindingClass {
    private TypeName mTargetTypeName;
    private ClassName mBinderClassName;
    private List<TargetField> mTargetFieldList = new ArrayList<>();

    BindingClass(TypeName targetTypeName, ClassName binderClassName) {
        mTargetTypeName = targetTypeName;
        mBinderClassName = binderClassName;
    }

    public void addTargetField(TargetField targetField) {
        mTargetFieldList.add(targetField);
    }

    public JavaFile brewJava() {

        TypeSpec.Builder targetClass = TypeSpec.classBuilder(mBinderClassName)
                .addSuperinterface(ParameterizedTypeName.get(BINDER, mTargetTypeName))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder method = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mTargetTypeName, "target")
                .addStatement("$T bundle = $N.getIntent().getExtras()", BUNDLE, "target");
        if (hasNecessaryField()) {
            method.addStatement("if (bundle == null){ $T.makeText($N, \"bundle is empty\", $T.LENGTH_SHORT).show(); return;} ",
                    TOAST, "target", TOAST);
        } else {
            method.addStatement("if (bundle == null) return;");
        }

        for (TargetField targetField : mTargetFieldList) {
            if (targetField.isNecessary) {
                method.addStatement("if (bundle.containsKey($S) && bundle.get($S) != null) {target.$L = ($T) bundle.get($S);}" +
                                " else {$T.makeText($N, \"$L is empty\", $T.LENGTH_SHORT).show() ;}",
                        targetField.keyName, targetField.keyName, targetField.fieldName, targetField.typeName, targetField.keyName, TOAST, "target", targetField.fieldName, TOAST);
            } else {
                method.addStatement("if (bundle.containsKey($S)) { target.$L = ($T) bundle.get($S); } ",
                        targetField.keyName, targetField.fieldName, targetField.typeName, targetField.keyName);
            }
        }

        targetClass.addMethod(method.build());

        return JavaFile.builder(mBinderClassName.packageName(), targetClass.build())
                .addFileComment("Generated code from AptIntent. Do not modify!").build();
    }

    private boolean hasNecessaryField() {
        for (TargetField targetField : mTargetFieldList) {
            if (targetField.isNecessary) {
                return true;
            }
        }
        return false;
    }

}
