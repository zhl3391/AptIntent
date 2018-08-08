package aptintent.compiler;

import com.squareup.javapoet.TypeName;

final class TargetField {

    public String keyName;
    public String fieldName;
    public TypeName typeName;
    public boolean isNecessary = false;

    TargetField(String keyName, String fieldName, TypeName typeName) {
        this.keyName = keyName;
        this.fieldName = fieldName;
        this.typeName = typeName;
    }

}
