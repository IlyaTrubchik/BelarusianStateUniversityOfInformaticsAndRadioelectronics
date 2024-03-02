import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.w3c.dom.events.Event;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Handler;

public class Server {
    private static String dir = Paths.get("").toAbsolutePath().toString() + "\\Storage\\";


    public static void main(String[] args) {
        System.out.println("Write port");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new ServerHandler());
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            System.out.println("Cannot start server on this port");
        }

    }

    static class ServerHandler implements HttpHandler {
        private HashMap<String, Consumer<HttpExchange>> methodHandlers = new HashMap<>();
        public ServerHandler(){
            initHandlers();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            Consumer<HttpExchange> methodHandler = methodHandlers.get(exchange.getRequestMethod());
            if(methodHandler!=null)
            {
                methodHandler.accept(exchange);
            }
            else {
                System.out.println("No such method handlers");
            }
        }

        private void handleGet(HttpExchange exchange)  {
            try {
                String filename = exchange.getRequestURI().getPath().substring(1);
                Path path = Paths.get(dir + filename);
                if (!Files.exists(path)) {
                    sendError(exchange, 405, "");
                } else sendResponse(exchange, 200, Base64.getEncoder().encode(Files.readAllBytes(path)));
            }
            catch (IOException e)
            {
                System.out.println("IOException occurs");
            }
        }

        public  void initHandlers(){
            methodHandlers.put("GET",this::handleGet);
            methodHandlers.put("PUT",this::handlePut);
            methodHandlers.put("POST",this::handlePost);
            methodHandlers.put("DELETE",this::handleDelete);
        }


        private void handlePut(HttpExchange exchange) {
            String filename = exchange.getRequestURI().getPath().substring(1);
            Path path = Paths.get(dir+filename);
                if(filename.contains("/")) {
                    String pathToFile = dir + filename.substring(0, filename.lastIndexOf("/"));
                    if (!Files.exists(Path.of(pathToFile))) {
                        new File(pathToFile).mkdirs();
                    }
                }
                try {
                    OutputStream outputStream = new FileOutputStream(path.toFile());
                    byte[] content = exchange.getRequestBody().readAllBytes();
                    outputStream.write(Base64.getDecoder().decode(content));
                    outputStream.close();
                    sendResponse(exchange, 200, "OK".getBytes());
                } catch (Exception e) {
                    System.out.println("File not found");
                    try {
                        sendError(exchange, 404, "File not found");
                    } catch (IOException ex) {
                       System.out.println("Cannot send response");
                     }
                }
        }

        private void handlePost(HttpExchange exchange)  {
            try {
                String filename = exchange.getRequestURI().getPath().substring(1);
                Path path = Paths.get(dir + filename);
                if (Files.exists(path)) {
                    byte[] content = exchange.getRequestBody().readAllBytes();
                    Files.write(path, Base64.getDecoder().decode(content), StandardOpenOption.APPEND);
                    sendResponse(exchange, 200, "(OK)".getBytes());
                } else {
                    sendError(exchange, 404, "File not found");
                }
            }
            catch(IOException e){
                System.out.println("IOException occurs");
            }
        }

        public void handleDelete(HttpExchange exchange) {
            String filename = exchange.getRequestURI().getPath().substring(1);
            Path path =Paths.get(dir+filename);
            if(Files.exists(path))
            {
                try {
                    Files.delete(path);
                    sendResponse(exchange, 200, "".getBytes());
                }catch (IOException e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("Cant delete file");
                }
            }
            else
            {
                try {
                    sendError(exchange, 404, "File not found");
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("Cant send response");
                }
            }
        }

        private void sendResponse(HttpExchange exchange, int code, byte[] response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            if(code!=204) {
                exchange.sendResponseHeaders(code, response.length);
            }
            else exchange.sendResponseHeaders(code,-1);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.flush();
            outputStream.close();
        }

        private void sendError(HttpExchange exchange, int code, String message) throws IOException {
            String body = code + " " + message;
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(code, body.length());
            exchange.getResponseBody().write(body.getBytes());
            exchange.close();
        }
    }
}
