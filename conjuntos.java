import java.util.stream.IntStream;
import java.util.*;

class Limite {
    int val;
    int state; // 0 = aberto, 1 = fechado

    public Limite(int INval, int INstate) {
        val = INval;
        state = INstate;
    }
}

class Intervalo {
    // state 0 = aberto; state 1 = fechado
    int inf, inf_state;
    int sup, sup_state;
    int[] range;
    List<Limite> esq = new ArrayList<>();
    List<Limite> dir = new ArrayList<>();

    // Definir valores
    public Intervalo(int INinf, int INinf_state, int INsup, int INsup_state, int has_subdiv, int sub_esq,
            int sub_esq_state, int sub_dir, int sub_dir_state) {
        inf = INinf;
        inf_state = INinf_state;
        sup = INsup;
        sup_state = INsup_state;
        esq.add(new Limite(inf, inf_state));
        dir.add(new Limite(sup, sup_state));

        if (has_subdiv == 1) {
            esq.add(new Limite(sub_esq, sub_esq_state));
            dir.add(new Limite(sub_dir, sub_dir_state));

            int auxinf = inf;
            int auxsup = sub_dir;
            if (inf_state == 0) {
                auxinf++;
            }
            if (sub_dir_state == 0) {
                auxsup--;
            }
            range = IntStream.rangeClosed(auxinf, auxsup).toArray();
        } else {
            int auxinf = inf;
            int auxsup = sup;
            if (inf_state == 0) {
                auxinf++;
            }
            if (sup_state == 0) {
                auxsup--;
            }
            range = IntStream.rangeClosed(auxinf, auxsup).toArray();
        }
    }
    // List<Integer> range_list = new ArrayList<Integer>(Arrays.asList(range));

    // v pertence a A?
    public int contem(float v) {
        for (int i = 0; i < this.esq.size() || i < this.dir.size(); i++) {
            if ((v == esq.get(i).val && esq.get(i).state == 1) || (v == dir.get(i).val && dir.get(i).state == 1)) {
                return 1;
            }
            if (v > esq.get(i).val && v < dir.get(i).val) {
                return 1;
            }
        }
        return 0;
    }

    // Interceção
    public int intercepta(Intervalo b) {
        if ((this.inf < b.sup && this.sup > b.sup) || (this.sup > b.inf && this.inf < b.inf)) {
            return 1;
        }
        if ((this.inf == b.sup && this.inf_state == 1 && b.sup_state == 1)
                || (this.sup == b.inf && this.sup_state == 1 && b.inf_state == 1)) {
            return 1;
        }
        if ((this.inf == b.inf && this.inf_state == 1 && b.inf_state == 1)
                || (this.sup == b.sup && this.sup_state == 1 && b.sup_state == 1)) {
            return 1;
        }
        return 0;
    }

    // Média
    public int media() {
        int aux = 0;
        int tam = 0;
        for (int i = 0; i < this.range.length; i++) {
            if (this.contem((float) range[i]) == 1) {
                aux += range[i];
                tam++;
            }
        }
        return aux / tam;
    }

    // Produto
    public Intervalo produto(Intervalo b) {
        Integer[] temp = { this.inf * b.inf, this.inf * b.sup, this.sup * b.inf, this.sup * b.sup };
        int min = (int) Collections.min(Arrays.asList(temp));
        int max = (int) Collections.max(Arrays.asList(temp));
        return new Intervalo(min, this.inf_state | b.inf_state, max, this.sup_state | b.sup_state, 0, 0, 0, 0, 0);
    }

    // Uniao
    public Intervalo uniao(Intervalo b) {
        int tempInf = Math.min(this.inf, b.inf);
        int tempSup = Math.max(this.sup, b.sup);
        int tempInf_state = (tempInf == this.inf) ? this.inf_state : b.inf_state;
        int tempSup_state = (tempSup == this.sup) ? this.sup_state : b.sup_state;

        if (intercepta(b) == 1) {
            System.out.println("A U B = "
                    + ((tempInf == this.inf && this.inf_state == 1 || tempInf == b.inf &&
                            b.inf_state == 1) ? "[" : "(")
                    + tempInf + "..." + tempSup
                    + ((tempSup == this.sup && this.sup_state == 1 || tempSup == b.sup &&
                            b.sup_state == 1) ? "]"
                                    : ")"));
        } else {
            System.out.println("A U B = " + ((this.sup < b.inf) ? (((this.inf_state == 1)
                    ? "["
                    : "(")
                    + this.inf + "..." + this.sup
                    + ((this.sup_state == 1) ? "]" : ")") + " U "
                    + ((b.inf_state == 1) ? "[" : "(")
                    + b.inf + "..." + b.sup
                    + ((b.sup_state == 1) ? "]" : ")"))
                    : (((b.inf_state == 1) ? "[" : "(")
                            + b.inf + "..." + b.sup
                            + ((b.sup_state == 1) ? "]" : ")") + " U "
                            + ((this.inf_state == 1) ? "[" : "(")
                            + this.inf + "..." + this.sup
                            + ((this.sup_state == 1) ? "]" : ")"))));
        }

        if (this.intercepta(b) == 1) {
            return new Intervalo(tempInf, tempInf_state, tempSup, tempSup_state, 0, 0, 0, 0, 0);
        } else {
            if (tempInf == this.inf) {
                return new Intervalo(this.inf, this.inf_state, this.sup, this.sup_state, 1, b.inf, b.inf_state, b.sup,
                        b.sup_state);
            } else {
                return new Intervalo(b.inf, b.inf_state, b.sup, b.sup_state, 1, this.inf, this.inf_state, this.sup,
                        this.sup_state);
            }
        }
    }

    public void print_valores() {
        for (int i = 0; i < this.range.length; i++) {
            if (this.contem(this.range[i]) == 1) {
                System.out.println(this.getClass().getSimpleName() + "[" + i + "]" + " = " + this.range[i]);
            }
        }
        System.out.println("\n");
    }

    // Teste
    public static void main(String[] args) {
        Intervalo a = new Intervalo(0, 1, 10, 0, 0, 0, 0, 0, 0);
        a.print_valores();
        
        Intervalo b = new Intervalo(20, 0, 40, 1, 0, 0, 0, 0, 0);
        b.print_valores();
        
        Intervalo c = a.produto(b);
        c.print_valores();
        
        Intervalo d = b.uniao(a);
        d.print_valores();        
        System.out.println("D contem -1? " + d.contem(-1));
        System.out.println("D contem 15? " + d.contem(15));
        System.out.println("D contem 40? " + d.contem(40));
        System.out.println("A intercepta D? " + a.intercepta(d));
        System.out.println("A intercepta B? " + a.intercepta(b));
        System.out.println("Média de C = " + c.media());
        System.out.println("30.01 pertence a A? " + a.contem((float) 30.01));
    }
}
