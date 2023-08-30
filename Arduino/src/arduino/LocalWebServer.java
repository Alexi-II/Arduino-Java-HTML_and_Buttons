package arduino;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.fazecast.jSerialComm.SerialPort;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class LocalWebServer {
    private static boolean ledStatus1 = false;
    private static boolean ledStatus2 = false;
    private static SerialPort serialPort;

    public static void main(String[] args) throws IOException {
        serialPort = SerialPort.getCommPort("COM4"); // Substitua 'COM4' pela porta serial correta
        serialPort.openPort();
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        // Exibir a mensagem na janela JFrame
        JFrame frame = new JFrame("Mensagem");
        JLabel label = new JLabel("Site aberto na porta 88");
        frame.add(label);
        frame.setSize(300, 100);
        frame.setLocationRelativeTo(null); // Centralizar a janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        HttpServer server = HttpServer.create(new InetSocketAddress(88), 0);
        server.createContext("/", new LEDControlHandler());
        server.start();
        System.out.println("Site rodando na porta 88");
    }


    static class LEDControlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
            	String response = "<!DOCTYPE html>\n"
            	        + "<html>\n"
            	        + "<head><title>Controle de LED</title>"
            	        + "<style>"
            	        + "body { text-align: center; background-color: silver;}"
            	        + "h1 { font-family: Arial; font-size: 5vw; color: white;}"
            	        + "form { display: inline-block; margin: 4vw; }"
            	        + "button { font-size: 18px; padding: 5vw 8vw;}"
            	        + "</style>"
            	        + "</head>\n"
            	        + "<body>\n"
            	        + "<h1>Controle de LED</h1>\n"
            	        + "<form method='post' action='/toggle'>\n"
            	        + "<button type='submit'>Ligar/Desligar LED</button>\n"
            	        + "</form>\n"
            	        + "<br>\n"
            	        + "<form method='post' action='/toggle2'>\n"
            	        + "<button type='submit'>Ligar/Desligar LED 2</button>\n"
            	        + "</form>\n"
            	        + "</body>\n"
            	        + "</html>";
            	exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
                
            } else if (requestMethod.equalsIgnoreCase("POST") && exchange.getRequestURI().getPath().equals("/toggle")) {
                if (ledStatus1) {
                    ledStatus1 = false;
                    sendSerialCommand("1"); // Desliga o LED
                } else {
                    ledStatus1 = true;
                    sendSerialCommand("0"); // Liga o LED
                    try {
                        Thread.sleep(200); // Aguarda 500 ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ledStatus1 = false;
                    sendSerialCommand("1"); // Desliga o LED após 500 ms
                }
            } else if (requestMethod.equalsIgnoreCase("POST") && exchange.getRequestURI().getPath().equals("/toggle2")) {
            	
            	 if (ledStatus2) {
                     ledStatus2 = false;
                     sendSerialCommand("2"); // Desliga o LED 2
            	 } else {
                     ledStatus2 = true;
                     sendSerialCommand("3"); // Liga o LED
                     try {
                         Thread.sleep(200); // Aguarda 500 ms
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                     ledStatus2 = false;
                     sendSerialCommand("2"); // Desliga o LED após 500 ms
                 }
                 exchange.getResponseHeaders().set("Location", "/");
                 exchange.sendResponseHeaders(302, -1); // Código 302 para redirecionamento
                 exchange.getResponseBody().close();
             }
         }

        private void sendSerialCommand(String command) {
            try {
                serialPort.getOutputStream().write(command.getBytes(), 0, command.length());
                serialPort.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}