package aptintent.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import static aptintent.compiler.UsedClassName.INTENT;

final class CreatorClass {

    private TypeName mSuperInterface;
    private ClassName mCreatorClassName;
    private List<CreatorMethod> mCreatorMethodList = new ArrayList<>();

    CreatorClass(TypeName superInterface, ClassName creatorClassName) {
        mSuperInterface = superInterface;
        mCreatorClassName = creatorClassName;
    }

    public void addCreatorMethod(CreatorMethod creatorMethod) {
        mCreatorMethodList.add(creatorMethod);
    }

    public JavaFile brewJava() {
        TypeSpec.Builder creatorClass = TypeSpec.classBuilder(mCreatorClassName)
                .addSuperinterface(mSuperInterface)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        for (CreatorMethod creatorMethod : mCreatorMethodList) {
            MethodSpec.Builder method = MethodSpec.methodBuilder(creatorMethod.methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(INTENT);
            method.addStatement("Intent intent = new Intent(context, $T.class)", creatorMethod.targetTypeName);
            for (TargetField parameter : creatorMethod.paramList) {
                method.addParameter(parameter.typeName, parameter.fieldName);
                if (parameter.keyName != null) {
                    method.addStatement("intent.putExtra($S, $N)", parameter.keyName, parameter.fieldName);
                }
            }
            method.addStatement("return intent");
            creatorClass.addMethod(method.build());
        }

        return JavaFile.builder(mCreatorClassName.packageName(), creatorClass.build())
                .addFileComment("Generated code from AptIntent. Do not modify!").build();
    }
}
