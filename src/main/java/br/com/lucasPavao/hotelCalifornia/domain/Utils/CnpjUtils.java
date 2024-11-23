package br.com.lucasPavao.hotelCalifornia.domain.Utils;

import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.InvalidCnpjException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;

import java.util.regex.Pattern;

public class CnpjUtils {
    
     public static void validate(String cnpj){
          isValid(cnpj);
     }

    private static boolean isValid(String cnpj){
        boolean retorno = true;
        try {
            cnpj = removerMascaraCNPJ(cnpj);

            String regex = "^[A-Za-z0-9]{12}[0-9]{2}$";
            if (!Pattern.matches(regex, cnpj)) {
                retorno = false;
            }


            if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
                    cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
                    cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
                    cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
                    cnpj.equals("88888888888888") || cnpj.equals("99999999999999")) {
                retorno = false;
            }


            int[] valores = new int[12];

            for (int i = 0; i < 12; i++) {
                char c = cnpj.charAt(i);
                if (Character.isDigit(c)) {
                    valores[i] = c - '0'; // Para números (0-9)
                } else if (Character.isLetter(c)) {
                    valores[i] = c - 'A' + 17; // Para letras (A-Z), ajustado conforme regras
                }
            }


            int peso = 5; // Pesos começam em 5
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += valores[i] * peso;
                peso--;
                if (peso < 2) {
                    peso = 9;
                }
            }

            int resto = soma % 11;
            char dig13 = (resto < 2) ? '0' : (char) (11 - resto + '0');


            peso = 6;
            soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += valores[i] * peso;
                peso--;
                if (peso < 2) {
                    peso = 9;
                }
            }

            soma += (dig13 - '0') * 2;
            resto = soma % 11;
            char dig14 = (resto < 2) ? '0' : (char) (11 - resto + '0');


            retorno = dig13 == cnpj.charAt(12) && dig14 == cnpj.charAt(13);

            if (!retorno) {
                throw new IllegalArgumentException("Cnpj Invalido");
            }
        } catch (IllegalArgumentException ex) {
            throw new InvalidCnpjException("CNPJ inválido: " + ex.getMessage());
        }
        return retorno;
    }


    public static String aplicarMascaraCNPJ(String cnpj) {

        if (cnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter exatamente 14 caracteres");
        }
        return cnpj.substring(0, 2) + "." +
                cnpj.substring(2, 5) + "." +
                cnpj.substring(5, 8) + "/" +
                cnpj.substring(8, 12) + "-" +
                cnpj.substring(12, 14);
    }


    public static String removerMascaraCNPJ(String cnpjComMascara) {

        return cnpjComMascara.replaceAll("[^A-Za-z0-9]", "");
    }
}
