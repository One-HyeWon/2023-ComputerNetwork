package nwServer;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class nwserver {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1025)) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(() -> handleClientRequest(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // 클라이언트로부터 HTTP 스타일의 요청 받기
            String request = reader.readLine();
            System.out.println("Received request: " + request);

            // 요청 파싱
            String[] requestParts = request.split(" ");
            if (requestParts.length == 3 && requestParts[0].equals("GET")) {
                String[] queryParts = requestParts[1].split("\\?");
                if (queryParts.length == 2 && queryParts[0].equals("/calculate")) {
                    // 쿼리 파싱
                    String[] queryParams = queryParts[1].split("&");
                    if (queryParams.length > 3) {
                        throw new Exception(ErrorType.TOO_MANY_ARGUMENTS.getMessage());
                    } else {
                        String operation = null;
                        int operand1 = 0;
                        int operand2 = 0;

                        for (String param : queryParams) {
                            String[] paramParts = param.split("=");
                            if (paramParts.length == 2) {
                                if (paramParts[0].equals("operation")) {
                                    operation = paramParts[1];
                                } else if (paramParts[0].equals("operand1")) {
                                    operand1 = Integer.parseInt(paramParts[1]);
                                } else if (paramParts[0].equals("operand2")) {
                                    operand2 = Integer.parseInt(paramParts[1]);
                                }
                            }
                        }

                        // 계산 및 응답 전송
                        String result = calculate(operation, operand1, operand2);
                        System.out.println("This is result: " + result);
                        if (result.startsWith("Error")) {
                            writer.println("HTTP/1.1 " + ResponseType.BAD_REQUEST.getCode() + " " + ResponseType.BAD_REQUEST.getMessage() + " /ResultCode: /ResultValue: " + result);
                        } else {
                            writer.println("HTTP/1.1 " + ResponseType.OK.getCode() + " " + ResponseType.OK.getMessage() + " /ResultCode: /ResultValue: " + result);
                        }
                    }
                } else {
                    throw new Exception(ErrorType.INVALID_PATH.getMessage());
                }
            } else {
                throw new Exception(ErrorType.INVALID_REQUEST.getMessage());
            }

            // 연결 종료
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //연산 시작
    private static String calculate(String operation, int operand1, int operand2) {
        switch (operation) {
            case "ADD":
                return String.valueOf(operand1 + operand2);
            case "SUB":
                return String.valueOf(operand1 - operand2);
            case "MUL":
                return String.valueOf(operand1 * operand2);
            case "DIV":
                // 0으로 나눠주려고 하는 경우 예외처리
                if (operand2 == 0) {
                    return ErrorType.DIVISION_BY_ZERO.getMessage();
                }
                return String.valueOf(operand1 / operand2);
            default:
                return ErrorType.UNKNOWN_OPERATION.getMessage();
        }
    }

    public enum ResponseType {
        OK(200, "OK"),
        BAD_REQUEST(400, "Bad Request"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int code;
        private final String message;

        ResponseType(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum ErrorType {
        TOO_MANY_ARGUMENTS("Too many arguments"),
        INVALID_PATH("Invalid path"),
        INVALID_REQUEST("Invalid request"),
        DIVISION_BY_ZERO("Error: Division by zero"),
        UNKNOWN_OPERATION("Error: Unknown operation");

        private final String message;

        ErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
