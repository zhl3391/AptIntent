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
                .addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder method = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mTargetTypeName, "target")
                .addStatement("$T bundle = $N.getIntent().getExtras()", BUNDLE, "target")
                .addStatement("if (bundle == null) return");

        for (TargetField targetField : mTargetFieldList) {
            method.addStatement("target.$L = ($T) bundle.get($S)",
                    targetField.fieldName, targetField.typeName, targetField.keyName);
        }

        targetClass.addMethod(method.build());

        return JavaFile.builder(mBinderClassName.packageName(), targetClass.build())
                .addFileComment("Generated code from AptIntent. Do not modify!").build();
    }
}
