import java.math.BigInteger;

public class Number implements Factor {
    private BigInteger num;

    public Number(BigInteger num) {
        this.num = num;
    }

    public BigInteger GetNum() {
        return this.num;
    }

    public Number MultWithNumber(Number number) {
        BigInteger resultnumber = this.num.multiply(number.GetNum());
        Number result = new Number(resultnumber);
        return result;
    }

    public void AddWithNumber(Number number) {
        this.num = this.num.add(number.GetNum());
    }

    public String toString() {
        return this.num.toString();
    }

    public Number Copy() {
        Number number = new Number(this.num);
        return number;
    }
}
