import java.math.BigInteger;
import java.util.Objects;

public class Term {
    private Factor varnum;
    private Integer intnum;

    public Term() {
        BigInteger zero = new BigInteger("0");
        varnum = new Num(zero);
        intnum = 0;
    }

    public Term(Integer b) {
        varnum = new Var("x");
        intnum = b;
    }

    public boolean equalTerm(Term term1) {
        return (Objects.equals(intnum, term1.getPow()));
    }

    public Integer getPow() {
        return intnum;
    }

}
