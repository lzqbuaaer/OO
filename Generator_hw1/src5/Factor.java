public interface Factor {

    Factor negate();

    Factor powered(int exponent);

    LegalExpr turnLegal();

}
