import java.util.List;

public class Regra {
    private List<String> antecedentes;
    private String consequente;

    public Regra(List<String> antecedentes, String consequente) {
        this.antecedentes = antecedentes;
        this.consequente = consequente;
    }

    public List<String> getAntecedentes() {
        return antecedentes;
    }

    public String getConsequente() {
        return consequente;
    }

    @Override
    public String toString() {
        return String.join(", ", antecedentes) + " => " + consequente;
    }
}
