import java.io.*;
import java.net.*;

public class ChatCliente {

    static final String IP_SERVIDOR = "127.0.0.1";
    static final int PORTA = 10752;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(IP_SERVIDOR, PORTA);
            PrintWriter saidaParaServidor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Conectado ao servidor " + IP_SERVIDOR + ":" + PORTA);
            System.out.println("Digite suas mensagens (ou 'QUIT' para sair):");

            while (true) {
                System.out.print("Você: ");
                String mensagem = teclado.readLine();

                if (mensagem == null) break;

                // Envia a mensagem para o servidor
                saidaParaServidor.println(mensagem);

                if (mensagem.equalsIgnoreCase("QUIT")) {
                    System.out.println("Você encerrou a conexão.");
                    break;
                }

                // Aguarda e exibe a resposta do servidor
                String resposta = entradaServidor.readLine();
                if (resposta == null || resposta.equalsIgnoreCase("QUIT")) {
                    System.out.println("Servidor encerrou a conexão.");
                    break;
                }

                System.out.println("Servidor: " + resposta);
            }

        } catch (ConnectException e) {
            System.err.println("Erro: Não foi possível conectar ao servidor. Verifique se o servidor está rodando.");
        } catch (IOException e) {
            System.err.println("Erro na comunicação: " + e.getMessage());
        }
    }
}