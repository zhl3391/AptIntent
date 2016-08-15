package aptintent.compiler;

import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

final class CreatorMethod {

    public TypeName returnTypeName;
    public TypeName targetTypeName;
    public String methodName;
    public List<TargetField> paramList = new ArrayList<>();

    CreatorMethod(TypeName targetTypeName, TypeName returnTypeName, String methodName) {
        this.targetTypeName = targetTypeName;
        this.returnTypeName = returnTypeName;
        this.methodName = methodName;
    }
}
