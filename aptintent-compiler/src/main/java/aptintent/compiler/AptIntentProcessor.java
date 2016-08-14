package aptintent.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import aptintent.annotation.CreateIntent;
import aptintent.annotation.Field;

import static javax.tools.Diagnostic.Kind.ERROR;

@AutoService(Processor.class)
public class AptIntentProcessor extends AbstractProcessor{

    private Elements mElementUtils;
    private Types mTypeUtils;
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mTypeUtils = processingEnv.getTypeUtils();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(CreateIntent.class.getCanonicalName());
        annotationTypes.add(Field.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        parseBindingClass(roundEnv);

        return true;
    }

    private void parseBindingClass(RoundEnvironment roundEnv) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Field.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            BindingClass bindingClass = getOrCreateBindingClass(targetClassMap, enclosingElement);
            String keyName = element.getAnnotation(Field.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeName typeName = TypeName.get(element.asType());
            bindingClass.addTargetField(new TargetField(keyName, fieldName, typeName));
            targetClassMap.put(enclosingElement, bindingClass);
        }

        for (Map.Entry<TypeElement, BindingClass> entry : targetClassMap.entrySet()) {
            try {
                entry.getValue().brewJava().writeTo(mFiler);
            } catch (IOException e) {
                error(entry.getKey(), "Unable to write BindingClass for type %s: %s", entry.getKey(),
                        e.getMessage());
            }
        }
    }

    private BindingClass getOrCreateBindingClass(Map<TypeElement, BindingClass> targetClassMap,
                                                 TypeElement enclosingElement) {
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            TypeName targetType = TypeName.get(enclosingElement.asType());
            if (targetType instanceof ParameterizedTypeName) {
                targetType = ((ParameterizedTypeName) targetType).rawType;
            }
            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, packageName);
            ClassName binderClassName = ClassName.get(packageName, className + "_Binder");
            bindingClass = new BindingClass(targetType, binderClassName);
        }

        return bindingClass;
    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(ERROR, message, element);
    }
}
