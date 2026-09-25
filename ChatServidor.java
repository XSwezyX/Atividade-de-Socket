import java.io.*;
import java.net.*;

public class ChatServidor {

    static final int PORTA = 10752;

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA + ". Aguardando conexões...");

            while (true) {
                
                Socket conexaoCliente = servidor.accept();
                System.out.println("\nNovo cliente conectado: " + conexaoCliente.getInetAddress());

                Thread threadDoCliente = new Thread(new AtendenteDeCliente(conexaoCliente));
                threadDoCliente.start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}

class AtendenteDeCliente implements Runnable {

    private final Socket socket;

    public AtendenteDeCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saidaParaCliente = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String mensagemDoCliente;

            while ((mensagemDoCliente = entradaCliente.readLine()) != null) {

                System.out.println("[" + socket.getInetAddress() + "]: " + mensagemDoCliente);

                if (mensagemDoCliente.equalsIgnoreCase("QUIT")) {
                    System.out.println("Cliente " + socket.getInetAddress() + " enviou QUIT e encerrou.");
                    saidaParaCliente.println("QUIT");
                    break;
                }

                saidaParaCliente.println("Recebido: " + mensagemDoCliente);
            }

        } catch (IOException e) {
            System.out.println("Cliente " + socket.getInetAddress() + " desconectado.");
        } finally {
            try {
                socket.close();
                System.out.println("Conexão com " + socket.getInetAddress() + " finalizada.");
            } catch (IOException e) {
                System.err.println("Erro ao fechar socket: " + e.getMessage());
            }
        }
    }
}