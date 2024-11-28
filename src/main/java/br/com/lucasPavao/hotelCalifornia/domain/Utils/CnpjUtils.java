package br.com.lucasPavao.hotelCalifornia.domain.Utils;

import br.com.lucasPavao.hotelCalifornia.infraestructure.exception.InvalidCnpjException;
import br.com.lucasPavao.hotelCalifornia.infraestructure.model.HotelCaliforniaModel;

import java.util.regex.Pattern;

public class CnpjUtils {
    
     public static void validate(String cnpj){
         if (cnpj == null || cnpj.isEmpty()) {
             throw new InvalidCnpjException("CNPJ não pode ser nulo ou vazio.");
         }
          isValid(cnpj);
     }

    private static void isValid(String cnpj){

        try {
            cnpj = removerMascaraCNPJ(cnpj);

            String regex = "^[A-Za-z0-9]{12}[0-9]{2}$";
            if (!Pattern.matches(regex, cnpj)) {
                throw new InvalidCnpjException("CNPJ inválido: deve conter apenas 14 dígitos");
            }


            String[] sequenciasInvalidas = {
                    "00000000000000", "11111111111111", "22222222222222", "33333333333333",
                    "44444444444444", "55555555555555", "66666666666666", "77777777777777",
                    "88888888888888", "99999999999999"
            };
            for (String seq : sequenciasInvalidas) {
                if (cnpj.equals(seq)) {
                    throw new InvalidCnpjException("CNPJ inválido: CNPJ com valores repetidos!");
                }
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


           boolean valid = dig13 == cnpj.charAt(12) && dig14 == cnpj.charAt(13);

            if (!valid) {
                throw new InvalidCnpjException("Cnpj Invalido");
            }
        } catch (IllegalArgumentException ex) {
            throw new InvalidCnpjException("CNPJ inválido: " + ex.getMessage());
        }

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
