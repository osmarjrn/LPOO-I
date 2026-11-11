/*
 * GRR20250999
 * OSMAR NUNES JÚNIOR
 */
package locadora.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Formatação de valores para as telas, para que todas as tabelas mostrem
 * dinheiro, ano e data do mesmo jeito.
 *
 * @author Osmar Nunes Júnior
 */
public final class Formatador {

    /**
     * Se {@code true}, valores acima de mil usam ponto de milhar
     * ("R$45.000,00"); se {@code false}, não usam ("R$45000,00").
     */
    public static final boolean USAR_SEPARADOR_MILHAR = true;

    /** Formato brasileiro (vírgula decimal). */
    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    /** Impede a criação de instâncias (classe só com métodos estáticos). */
    private Formatador() {
    }

    /**
     * Formata um valor em reais como "R$XXX,XX" (sempre duas casas decimais).
     *
     * @param valor valor em reais
     * @return texto como "R$100,00" ou "R$45.000,00"
     */
    public static String moeda(double valor) {
        String formato = USAR_SEPARADOR_MILHAR ? "R$%,.2f" : "R$%.2f";
        return String.format(BR, formato, valor);
    }

    /**
     * Formata o ano com quatro dígitos.
     *
     * @param ano ano modelo
     * @return texto como "2021"
     */
    public static String ano(int ano) {
        return String.format("%04d", ano);
    }

    /**
     * Formata uma data como dd/MM/aaaa.
     *
     * @param data data a formatar
     * @return texto como "05/11/2026"
     */
    public static String data(Calendar data) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(data.getTime());
    }

    /**
     * Converte um texto dd/MM/aaaa em data. Datas que não existem (como
     * 31/02/2026) ou fora do formato são recusadas.
     *
     * @param texto texto digitado pelo usuário
     * @return a data, ou {@code null} se o texto não for uma data válida
     */
    public static Calendar converterData(String texto) {
        if (texto == null || !texto.trim().matches("\\d{2}/\\d{2}/\\d{4}")) {
            return null;
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        formato.setLenient(false);
        try {
            Calendar data = Calendar.getInstance();
            data.setTime(formato.parse(texto.trim()));
            return data;
        } catch (ParseException e) {
            return null;
        }
    }
}
