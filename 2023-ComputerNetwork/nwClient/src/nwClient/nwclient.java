package nwClient;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class nwclient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1025);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // 사용자로부터 수식 입력 받기
            System.out.print("Enter operation and operands (e.g., ADD 10 20): ");
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            String[] expressionParts = userInput.readLine().split(" ");
            if (expressionParts.length == 3) {
                String operation = expressionParts[0];
                String operand1 = expressionParts[1];
                String operand2 = expressionParts[2];

                // 서버로 HTTP 스타일의 요청 전송
                writer.println("GET /calculate?operation=" + operation + "&operand1=" + operand1 + "&operand2=" + operand2 + " HTTP/1.1");
            } else {
                System.out.println("Invalid input format");
            }

            // 서버로부터 HTTP 스타일의 응답 받기
            String response = reader.readLine();
            System.out.println("Received response: " + response);

            // 결과 파싱 및 출력
            String[] responseParts = response.split(" /");
            String response0 = responseParts[0];
            String[] responseHeader = response0.split(" ");
            if (responseParts.length >= 2 && responseHeader[0].equals("HTTP/1.1")) {
                System.out.println(responseParts[responseParts.length - 1]);
            } else {
                System.out.println("Invalid response format");
            }

            // 연결 종료
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
