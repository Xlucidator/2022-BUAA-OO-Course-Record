import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashSet;

public class OperationModel {
    private final Visibility visibility;
    private final String name;

    private final ArrayList<UmlParameter> inPara = new ArrayList<>();

    private final HashSet<String> referenceParaSet = new HashSet<>(); // <id>

    private boolean namedTypeError = false;
    public static final HashSet<String> STANDARD_NAME = new HashSet<String>() {
        {
            add("byte");
            add("short");
            add("int");
            add("long");
            add("float");
            add("double");
            add("char");
            add("boolean");
            add("String");
            add("void");
        }
    };

    public OperationModel(String name, Visibility visibility, String fatherReferenceId) {
        this.name = name;
        this.visibility = visibility;
        referenceParaSet.add(fatherReferenceId);
        /* add self-ReferenceType first, reduce when used to ignore potential self-ReferenceType */
    }

    public void addPara(UmlParameter para) {
        if (para.getDirection() != Direction.RETURN) {
            this.inPara.add(para);
        }

        NameableType paraType = para.getType();
        if (paraType instanceof NamedType) {    // NamedType
            String nameType = ((NamedType) paraType).getName();
            if ((!STANDARD_NAME.contains(nameType)) ||
                    (para.getDirection() != Direction.RETURN && nameType.equals("void"))) {
                namedTypeError = true;
            }
        } else {    // ReferenceType
            referenceParaSet.add(((ReferenceType) paraType).getReferenceId());
        }
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    /* can only use after modeling */
    public boolean isNamedTypeError() {
        return namedTypeError;
    }

    /* can only use after modeling */
    public Integer getCouplingDegree() {
        return referenceParaSet.size() - 1;
    }

    /* can only use after modeling */
    public boolean isOperationEqual(OperationModel other) {
        if (!name.equals(other.getName())) {
            return false;
        }
        if (inPara.size() != other.getInPara().size()) {
            return false;
        }
        return isInParaEqual(other.getInPara());
    }

    // post-condition: inPara.size() equals
    private boolean isInParaEqual(ArrayList<UmlParameter> otherPara) {
        ArrayList<Object> selves = new ArrayList<>();
        for (UmlParameter umlParameter: inPara) {
            selves.add(umlParameter.getType());
        }
        ArrayList<Object> others = new ArrayList<>();
        for (UmlParameter umlParameter: otherPara) {
            others.add(umlParameter.getType());
        }

        for (int i = selves.size() - 1; i >= 0; --i) {
            Object selfType = selves.remove(i);
            if (!others.contains(selfType)) {
                return false;
            }
            others.remove(selfType);
        }
        return true;
    }

    public ArrayList<UmlParameter> getInPara() {
        return inPara;
    }
}
