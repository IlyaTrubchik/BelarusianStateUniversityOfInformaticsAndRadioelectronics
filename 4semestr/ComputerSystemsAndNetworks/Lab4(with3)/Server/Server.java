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
        public void handle(HttpExchange exchange)  {
            try {
                String path = exchange.getRequestURI().getPath();
                Consumer<HttpExchange> methodHandler = methodHandlers.get(exchange.getRequestMethod());
                if (methodHandler != null) {
                    methodHandler.accept(exchange);
                } else {
                    System.out.println("No such method handlers");
                }
            }
            catch (Exception e)
            {
                System.out.println("Exception");
            }
        }
        private void handleBigGet(HttpExchange exchange)
        {
            try {
                String filename = exchange.getRequestURI().getPath().substring(1);
                Path path = Paths.get(dir + filename);
                File file = new File(path.toUri());
                long fileSize = file.length();
                long startRange = 0;
                long endRange = fileSize - 1;
                String rangeHeader = exchange.getRequestHeaders().getFirst("Range");
                if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                    String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
                    if (ranges.length == 2) {
                        startRange = Long.parseLong(ranges[0]);
                        endRange = Math.min(Long.parseLong(ranges[1]), endRange);
                    }
                    long contentLength = endRange - startRange + 1;
                    exchange.getResponseHeaders().set("Content-Type", "application/octet-stream");
                    exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                    exchange.getResponseHeaders().set("Content-Length", String.valueOf(contentLength));
                    exchange.getResponseHeaders().set("Accept-Ranges", "bytes");
                    exchange.getResponseHeaders().set("Content-Range", "bytes " + startRange + "-" + endRange + "/" + fileSize);
                    try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
                        randomAccessFile.seek(startRange);
                        byte[] buffer = new byte[(int)(endRange-startRange+1)];
                        long offsite = startRange;
                        randomAccessFile.readFully(buffer);
                        sendResponse(exchange,206,buffer);
                        offsite = offsite+buffer.length;
                        System.out.print("Sended:"+offsite+"\r");
                    } catch (IOException e) {
                        System.out.println("Error");
                    }

                }
                else {
                    try {
                        if (!Files.exists(path)) {
                            sendError(exchange, 405, "");
                        } else sendResponse(exchange, 200, Base64.getEncoder().encode(Files.readAllBytes(path)));
                    }
                    catch (IOException e)
                    {
                        System.out.println("IOException occurs");
                    }
                }
            }catch (Exception ex)
            {
                System.out.println("Response error");
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
            methodHandlers.put("GET",this::handleBigGet);
            methodHandlers.put("PUT",this::handlePut);
            methodHandlers.put("POST",this::handlePost);
            methodHandlers.put("DELETE",this::handleDelete);
            methodHandlers.put("HEAD",this::handleHead);
        }


        private void handleHead(HttpExchange exchange)
        {
            try {
                String filename = exchange.getRequestURI().getPath().substring(1);
                Path path = Paths.get(dir + filename);
                if (!Files.exists(path)) {
                    sendError(exchange, 405, "");
                } else {
                    File file = new File(path.toUri());
                    exchange.getResponseHeaders().set("Content-Length",Long.toString(file.length()));
                    exchange.sendResponseHeaders(200,-1);
                }
            }
            catch (IOException e)
            {
                System.out.println("IOException occurs");
            }
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
            //exchange.getResponseHeaders().set("Content-Type", "text/plain");
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
