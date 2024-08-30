import java.util.ArrayList;
import java.util.List;

public class BaseConhecimento {
    private List<Regra> regras;

    public BaseConhecimento() {
        regras = new ArrayList<>();
    }

    public void adicionarRegra(Regra regra) {
        regras.add(regra);
    }

    public List<Regra> obterRegras() {
        return regras;
    }

    public void removerRegra(int indice) {
        if (indice >= 0 && indice < regras.size()) {
            regras.remove(indice);
        }
    }
}
