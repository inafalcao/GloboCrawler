package enumeration;

/**
 * Created by inafalcao on 2/19/17.
 */
public enum Mes {

    JAN("Janeiro", 1),
    FEV("Fevereiro", 2),
    MAR("Março", 3),
    ABR("Abril", 4),
    MAI("Maio", 5),
    JUN("Junho", 6),
    JUL("Julho", 7),
    AGO("Agosto", 8),
    SET("Setembro", 9),
    OUT("Outubro", 10),
    NOV("Novembro", 11),
    DEZ("Dezembro", 12);

    private String nome;
    private Integer valor;

    Mes(String nome, Integer valor) {
        this.nome = nome;
        this.valor = valor;
    }

    /**
     * Given a month name, e.g. "Janeiro", returns a string containing 0 whether the month is less than 10, e.g. "01".
     * @param name
     * @return the zeroed month string, e.g. "01" for Janeiro
     */
    public static String getZeroedMonthByName(String name) {
        for (Mes mes : Mes.values()) {
            if(name.toLowerCase().equals(mes.getNome().toLowerCase())) {
                return mes.getValor().toString().length() == 1 ? "0" + mes.getValor() : mes.getValor() + "";
            }
        }
        return null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
