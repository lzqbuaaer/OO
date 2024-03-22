import java.math.BigInteger;

public class Monomial {
    private BigInteger ratio;
    private BigInteger index;
    private Polynomial exp;

    public Monomial(BigInteger ratio, BigInteger index, Polynomial exp) {
        this.ratio = ratio;
        this.index = index;
        this.exp = exp;
    }

    public BigInteger getRatio() {
        return this.ratio;
    }

    public BigInteger getIndex() {
        return this.index;
    }

    public Polynomial getExp() {
        return this.exp;
    }

    public boolean equal(Monomial mono) {
        if (this.ratio.compareTo(mono.getRatio()) != 0) {
            return false;
        } else if (this.index.compareTo(mono.getIndex()) != 0) {
            return false;
        } else {
            return this.exp.equal(mono.getExp());
        }
    }

    public Monomial add(Monomial mono) {
        return new Monomial(this.ratio.add(mono.getRatio()), this.index, this.exp);
    }

    public Monomial opposite() {
        return new Monomial(this.ratio.multiply(new BigInteger("-1")), this.index, this.exp);
    }

    public Monomial mul(Monomial mono) {
        return new Monomial(this.ratio.multiply(mono.getRatio()),
                this.index.add(mono.getIndex()), this.exp.addPoly(mono.getExp()));
    }

    public String toString() {
        StringBuilder ind = new StringBuilder();
        StringBuilder exp = this.expToString();
        if (this.index.compareTo(BigInteger.ONE) == 0) {
            ind.append("x");
        } else if (this.index.compareTo(BigInteger.ZERO) != 0) {
            ind.append("x^");
            ind.append(this.index);
        }
        StringBuilder sb = new StringBuilder();
        if (this.ratio.compareTo(BigInteger.ZERO) == 0) {
            return "0";
        } else if (this.ratio.compareTo(BigInteger.ONE) == 0) {
            sb.append(ind);
            if (sb.length() != 0 && exp.length() != 0) {
                sb.append("*");
            }
            sb.append(exp);
            if (sb.length() == 0) {
                sb.append("1");
            }
        } else if (this.ratio.compareTo(BigInteger.valueOf(-1)) == 0) {
            sb.append("-");
            sb.append(ind);
            if (sb.length() != 1 && exp.length() != 0) {
                sb.append("*");
            }
            sb.append(exp);
            if (sb.length() == 1) {
                sb.append("1");
            }
        } else {
            sb.append(this.ratio);
            if (ind.length() != 0) {
                sb.append("*");
            }
            sb.append(ind);
            if (exp.length() != 0) {
                sb.append("*");
            }
            sb.append(exp);
        }
        return sb.toString();
    }

    public StringBuilder expToString() {
        String expr = this.exp.toString();
        StringBuilder exp = new StringBuilder();
        if (!expr.equals("0")) {
            if (this.exp.isFactor()) {
                exp.append("exp(").append(expr).append(")");
            } else if (this.exp.specialFormat() != null) {
                exp.append(this.exp.specialFormat());
            } else if (this.exp.size() > 1 && this.exp.hasGcd().compareTo(BigInteger.ONE) > 0) {
                BigInteger gcd = this.exp.hasGcd();
                String exp1 = "exp((" + expr + "))";
                String exp2 = "exp((" + this.exp.divGcd(gcd) + "))^" + gcd;
                if (exp2.length() < exp1.length()) {
                    exp.append(exp2);
                } else {
                    exp.append(exp1);
                }
            } else {
                exp.append("exp((").append(expr).append("))");
            }
        }
        return exp;
    }

    public boolean isFactor() {
        int cnt = 0;
        if (this.ratio.compareTo(BigInteger.ZERO) == 0) {
            return true;
        }
        if (this.ratio.compareTo(BigInteger.ONE) == 0) {
            cnt++;
        }
        if (this.index.compareTo(BigInteger.ZERO) == 0) {
            cnt++;
        }
        if (this.exp.toString().equals("0")) {
            cnt++;
        }
        return cnt >= 2;
    }

    public boolean isBlank() {
        return (this.ratio.compareTo(BigInteger.ZERO) == 0);
    }

    public String specialFormat() {
        boolean jud1 = this.ratio.compareTo(BigInteger.ONE) > 0;
        boolean jud2 = this.index.compareTo(BigInteger.ZERO) > 0;
        boolean jud3 = !this.exp.toString().equals("0");
        if (jud1 && (jud2 ^ jud3)) {
            if (jud2) {
                if (this.index.compareTo(BigInteger.ONE) == 0) {
                    return "exp(x)^" + this.ratio;
                } else {
                    return "exp(x^" + this.index + ")^" + this.ratio;
                }
            } else {
                return "exp(" + this.expToString() + ")^" + this.ratio;
            }
        } else {
            return null;
        }
    }

    public Polynomial dx() {
        Polynomial ans = new Polynomial();
        ans.addMono(new Monomial(this.ratio.multiply(index),
                this.index.add(BigInteger.valueOf(-1)), this.exp));
        Polynomial poly = new Polynomial();
        poly.addMono(this);
        poly = poly.mulPoly(this.exp.dx());
        ans = ans.addPoly(poly);
        ans.clearBlank();
        return ans;
    }
}
