import java.util.stream.IntStream;
import java.util.Arrays;
import java.util.Collections;

class Intervalo {
    // AorF, 0 indica aberto, 1 indica fechado
    int inf, inf_AorF;
    int sup, sup_AorF;
    int[] range;

    // Definir valores
    public Intervalo(int INinf, int INinf_AorF, int INsup, int INsup_AorF) {
        inf = INinf;
        inf_AorF = INinf_AorF;
        sup = INsup;
        sup_AorF = INsup_AorF;

        int auxinf = inf;
        int auxsup = sup;
        if (inf_AorF == 0) {
            auxinf++;
        }
        if (sup_AorF == 0) {
            auxsup--;
        }
        range = IntStream.rangeClosed(auxinf, auxsup).toArray();
    }

    // v pertence a A?
    public int contem(float v) {
        if (inf_AorF == 1) {
            if (v < (float) inf) {
                return 0;
            }
        } else if (v <= (float) inf) {
            return 0;
        }
        if (sup_AorF == 1) {
            if (v > (float) sup) {
                return 0;
            }
        } else if (v >= (float) sup) {
            return 0;
        }
        return 1;
    }

    // Interceção
    public int intercepta(Intervalo b) {
        if ((this.inf < b.sup && this.sup > b.sup) || (this.sup > b.inf && this.inf < b.inf)) {
            return 1;
        }
        if ((this.inf == b.sup && this.inf_AorF == 1 && b.sup_AorF == 1)
                || (this.sup == b.inf && this.sup_AorF == 1 && b.inf_AorF == 1)) {
            return 1;
        }
        return 0;
    }

    // Média
    public int media() {
        int aux = 0;
        for (int i = 0; i < this.range.length; i++) {
            aux += range[i];
        }
        return aux / range.length;
    }

    // Produto
    public Intervalo produto(Intervalo b) {
        Integer[] temp = { this.inf * b.inf, this.inf * b.sup, this.sup * b.inf, this.sup * b.sup };
        int min = (int) Collections.min(Arrays.asList(temp));
        int max = (int) Collections.max(Arrays.asList(temp));
        return new Intervalo(min, this.inf_AorF | b.inf_AorF, max, this.sup_AorF | b.sup_AorF);
    }

    // Uniao
    public void uniao(Intervalo b) {
        int tempInf = Math.min(this.inf, b.inf);
        int tempSup = Math.max(this.sup, b.sup);
        if (intercepta(b) == 1) {
            System.out.println("A U B = "
                    + ((tempInf == this.inf && this.inf_AorF == 1 || tempInf == b.inf && b.inf_AorF == 1) ? "[" : "(")
                    + tempInf + "..." + tempSup
                    + ((tempSup == this.sup && this.sup_AorF == 1 || tempSup == b.sup && b.sup_AorF == 1) ? "]" : ")"));
        } else {
            System.out.println("A U B = " + ((this.sup < b.inf) ? (((this.inf_AorF == 1) ? "[" : "(")
                    + this.inf + "..." + this.sup
                    + ((this.sup_AorF == 1) ? "]" : ")") + " U "
                    + ((b.inf_AorF == 1) ? "[" : "(")
                    + b.inf + "..." + b.sup
                    + ((b.sup_AorF == 1) ? "]" : ")"))
                    : (((b.inf_AorF == 1) ? "[" : "(")
                            + b.inf + "..." + b.sup
                            + ((b.sup_AorF == 1) ? "]" : ")") + " U "
                            + ((this.inf_AorF == 1) ? "[" : "(")
                            + this.inf + "..." + this.sup
                            + ((this.sup_AorF == 1) ? "]" : ")"))));
        }
    }

    // Teste
    public static void main(String[] args) {
        Intervalo a = new Intervalo(0, 1, 10, 0);
        Intervalo b = new Intervalo(20, 0, 40, 1);
        Intervalo c = a.produto(b);
        for (int i = 0; i < c.range.length; i++) {
            System.out.println(c.range[i]);
        }
        System.out.println("Média = " + c.media());
        System.out.println("A intercepta B? " + a.intercepta(b));
        System.out.println("Pertence a A? " + a.contem((float) 30.01));
        b.uniao(a);
    }
}