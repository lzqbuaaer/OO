public class Variable implements Factor {
    private String var;

    private int index;

    public Variable(String var) {
        this.var = var;
        this.index = 1;
    }

    public String GetVar() {
        return this.var;
    }

    public int GetIndex() {
        return this.index;
    }

    public void SetIndex(int index) {
        this.index = index;
    }

    public Variable MultWithVariable(Variable variable) {
        Variable result = new Variable(this.var);
        result.SetIndex(this.index + variable.GetIndex());
        return result;
    }

    public String toString() {
        if (index == 1) {
            return var;
        } else if (index == 0) {
            return ("");
        } else {
            return (var + "^" + String.valueOf(index));
        }
    }

    public Variable Copy() {
        Variable variable = new Variable(this.var);
        variable.SetIndex(this.index);
        return variable;
    }
}
