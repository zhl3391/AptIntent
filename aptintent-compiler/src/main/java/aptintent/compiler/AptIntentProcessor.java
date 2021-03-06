package aptintent.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.lang.annotation.Annotation;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import aptintent.lib.CreateIntent;
import aptintent.lib.Extra;
import aptintent.lib.ExtraField;
import aptintent.lib.NecessaryField;
import aptintent.lib.OptionField;

import static aptintent.compiler.UsedClassName.CONTEXT;
import static aptintent.compiler.UsedClassName.INTENT;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
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
        annotationTypes.add(ExtraField.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        parseBindingClass(roundEnv);
        parseCreator(roundEnv);

        return true;
    }

    private TypeName getTargetTypeName(Element element) {
        TypeElement typeElement = null;
        try {
            element.getAnnotation(CreateIntent.class).value();
        } catch (MirroredTypeException e) {
            typeElement = (TypeElement) mTypeUtils.asElement(e.getTypeMirror());
        }

        if (typeElement != null) {
            return ClassName.get(typeElement);
        }

        return null;
    }

    private void parseCreator(RoundEnvironment roundEnv) {
        Map<TypeElement, CreatorClass> creatorClassMap = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(CreateIntent.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            CreatorClass creatorClass = getOrCreateCreatorClass(creatorClassMap, enclosingElement);
            String methodName = element.getSimpleName().toString();
            TypeName targetTypeName = getTargetTypeName(element);
            if (targetTypeName == null) {
                error(element, "Unable to get class value");
                break;
            }
            CreatorMethod creatorMethod = new CreatorMethod(targetTypeName, INTENT, methodName);
            boolean isHasContext = false;
            for (VariableElement variableElement : ((ExecutableElement)element).getParameters()) {
                Extra extra = variableElement.getAnnotation(Extra.class);
                String keyName = null;
                String fieldName = variableElement.getSimpleName().toString();
                TypeName typeName = TypeName.get(variableElement.asType());
                if (extra != null) {
                    keyName = extra.value();
                }
                if (typeName.toString().equals(CONTEXT.toString())) {
                    isHasContext = true;
                }
                TargetField field = new TargetField(keyName, fieldName, typeName);
                creatorMethod.paramList.add(field);
            }
            if (!isHasContext) {
                error(element, "the method : %s() must have android.content.Context", methodName);
            }
            creatorClass.addCreatorMethod(creatorMethod);
        }

        for (Map.Entry<TypeElement, CreatorClass> entry : creatorClassMap.entrySet()) {
            try {
                entry.getValue().brewJava().writeTo(mFiler);
            } catch (IOException e) {
                error(entry.getKey(), "Unable to write CreatorClass for type %s: %s", entry.getKey(),
                        e.getMessage());
            }
        }
    }

    private void parseBindingClass(RoundEnvironment roundEnv) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();
        boolean hasError = false;
        for (Element element : roundEnv.getElementsAnnotatedWith(ExtraField.class)) {
            hasError = isInaccessibleViaGeneratedCode(ExtraField.class, "fields", element);
            if (hasError) {
                break;
            }
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            BindingClass bindingClass = getOrCreateBindingClass(targetClassMap, enclosingElement);
            String keyName = element.getAnnotation(ExtraField.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeName typeName = TypeName.get(element.asType());
            bindingClass.addTargetField(new TargetField(keyName, fieldName, typeName));
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(NecessaryField.class)) {
            hasError = isInaccessibleViaGeneratedCode(NecessaryField.class, "fields", element);
            if (hasError) {
                break;
            }
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            BindingClass bindingClass = getOrCreateBindingClass(targetClassMap, enclosingElement);
            String keyName = element.getAnnotation(NecessaryField.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeName typeName = TypeName.get(element.asType());
            TargetField targetField = new TargetField(keyName, fieldName, typeName);
            targetField.isNecessary = true;
            bindingClass.addTargetField(targetField);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(OptionField.class)) {
            hasError = isInaccessibleViaGeneratedCode(OptionField.class, "fields", element);
            if (hasError) {
                break;
            }
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            BindingClass bindingClass = getOrCreateBindingClass(targetClassMap, enclosingElement);
            String keyName = element.getAnnotation(OptionField.class).value();
            String fieldName = element.getSimpleName().toString();
            TypeName typeName = TypeName.get(element.asType());
            TargetField targetField = new TargetField(keyName, fieldName, typeName);
            bindingClass.addTargetField(targetField);
        }

        if (hasError) {
            return;
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

    private CreatorClass getOrCreateCreatorClass(Map<TypeElement, CreatorClass> creatorClassMap,
                                                 TypeElement enclosingElement) {
        CreatorClass creatorClass = creatorClassMap.get(enclosingElement);
        if (creatorClass == null) {
            TypeName superInterface = TypeName.get(enclosingElement.asType());
            if (superInterface instanceof ParameterizedTypeName) {
                superInterface = ((ParameterizedTypeName) superInterface).rawType;
            }
            String packageName = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, packageName);
            ClassName creatorClassName = ClassName.get(packageName, className + "_Imp");
            creatorClass = new CreatorClass(superInterface, creatorClassName);
            creatorClassMap.put(enclosingElement, creatorClass);
        }

        return creatorClass;
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
            targetClassMap.put(enclosingElement, bindingClass);
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

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass,
                                                   String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Verify method modifiers.
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing type.
        if (enclosingElement.getKind() != CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
                    element.getSimpleName());
            hasError = true;
        }

        return hasError;
    }
}
