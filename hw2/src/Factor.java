import java.util.HashMap;

public interface Factor {
    Factor substitute(HashMap<String, Factor> inputs);

    Polynomial toPoly();
}
