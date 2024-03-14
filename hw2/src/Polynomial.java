import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Polynomial {
    private HashMap<BigInteger, HashMap<Polynomial, Monomial>> monos;

    public Polynomial() {
        this.monos = new HashMap<>();
    }

    public HashMap<BigInteger, HashMap<Polynomial, Monomial>> getMonos() {
        return this.monos;
    }

    public boolean equal(Polynomial poly) {
        this.clearBlank();
        poly.clearBlank();
        if (this.monos.size() != poly.getMonos().size()) {
            return false;
        }
        for (BigInteger index1 : poly.getMonos().keySet()) {
            boolean f = false;
            for (BigInteger index2 : this.monos.keySet()) {
                if (index1.compareTo(index2) == 0) {
                    f = true;
                } else {
                    continue;
                }
                HashMap<Polynomial, Monomial> value1 = this.monos.get(index1);
                HashMap<Polynomial, Monomial> value2 = poly.getMonos().get(index2);
                if (value1.size() != value2.size()) {
                    return false;
                }
                for (Polynomial p1 : value1.keySet()) {
                    boolean flag = false;
                    for (Polynomial p2 : value2.keySet()) {
                        if (value1.get(p1).equal(value2.get(p2))) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                }
            }
            if (!f) {
                return false;
            }
        }
        return true;
    }

    public void addMono(Monomial mono) {
        BigInteger index = mono.getIndex();
        if (this.monos.containsKey(index)) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            Polynomial poly1 = mono.getExp();
            for (Polynomial poly : value.keySet()) {
                if (poly.equal(poly1)) {
                    Monomial myMono = value.get(poly);
                    value.put(poly, myMono.add(mono));
                    return;
                }
            }
            value.put(poly1, mono);
        } else {
            HashMap<Polynomial, Monomial> value = new HashMap<>();
            value.put(mono.getExp(), mono);
            monos.put(index, value);
        }
    }

    public void subMono(Monomial mono) {
        this.addMono(mono.opposite());
    }

    public Polynomial addPoly(Polynomial poly) {
        Polynomial newPoly = new Polynomial();
        for (BigInteger index : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            for (Polynomial p : value.keySet()) {
                newPoly.addMono(value.get(p));
            }
        }
        for (BigInteger index : poly.getMonos().keySet()) {
            HashMap<Polynomial, Monomial> value = poly.getMonos().get(index);
            for (Polynomial p : value.keySet()) {
                newPoly.addMono(value.get(p));
            }
        }
        return newPoly;
    }

    public Polynomial subPoly(Polynomial poly) {
        Polynomial newPoly = new Polynomial();
        for (BigInteger index : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            for (Polynomial p : value.keySet()) {
                newPoly.addMono(value.get(p));
            }
        }
        for (BigInteger index : poly.getMonos().keySet()) {
            HashMap<Polynomial, Monomial> value = poly.getMonos().get(index);
            for (Polynomial p : value.keySet()) {
                newPoly.subMono(value.get(p));
            }
        }
        return newPoly;
    }

    public Polynomial mulPoly(Polynomial poly) {
        Polynomial newPoly = new Polynomial();
        for (BigInteger index1 : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value1 = this.monos.get(index1);
            for (Polynomial p1 : value1.keySet()) {
                for (BigInteger index2 : poly.getMonos().keySet()) {
                    HashMap<Polynomial, Monomial> value2 = poly.getMonos().get(index2);
                    for (Polynomial p2 : value2.keySet()) {
                        newPoly.addMono(value1.get(p1).mul(value2.get(p2)));
                    }
                }
            }
        }
        return newPoly;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BigInteger index : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            for (Polynomial p : value.keySet()) {
                Monomial mono = value.get(p);
                String str = mono.toString();
                if (!str.equals("0")) {
                    if (str.charAt(0) == '-') {
                        sb.append(str);
                    } else {
                        if (sb.length() > 0 && sb.charAt(0) != '-' && sb.charAt(0) != '+') {
                            sb.insert(0, "+");
                        }
                        sb.insert(0, str);
                    }
                }
            }
        }
        if (sb.length() == 0) {
            sb.append("0");
        }
        return sb.toString();
    }

    public boolean isFactor() {
        if (this.monos.size() > 1) {
            return false;
        }
        for (BigInteger index : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            if (value.size() > 1) {
                return false;
            }
            for (Polynomial poly : value.keySet()) {
                return value.get(poly).isFactor();
            }
        }
        return true;
    }

    public void clearBlank() {
        Iterator<Map.Entry<BigInteger, HashMap<Polynomial, Monomial>>> outerIte =
                this.monos.entrySet().iterator();
        while (outerIte.hasNext()) {
            Map.Entry<BigInteger, HashMap<Polynomial, Monomial>> outerEntry = outerIte.next();
            HashMap<Polynomial, Monomial> innerMap = outerEntry.getValue();
            Iterator<Map.Entry<Polynomial, Monomial>> innerIte = innerMap.entrySet().iterator();
            while (innerIte.hasNext()) {
                Map.Entry<Polynomial, Monomial> innerEntry = innerIte.next();
                Monomial innerValue = innerEntry.getValue();
                if (innerValue.isBlank()) {
                    innerIte.remove();
                }
            }
            if (innerMap.isEmpty()) {
                outerIte.remove();
            }
        }
    }

    public String specialFormat() {
        if (this.monos.size() > 1) {
            return null;
        }
        for (BigInteger index : this.monos.keySet()) {
            HashMap<Polynomial, Monomial> value = this.monos.get(index);
            if (value.size() > 1) {
                return null;
            }
            for (Polynomial poly : value.keySet()) {
                Monomial mono = value.get(poly);
                return mono.specialFormat();
            }
        }
        return null;
    }
}
