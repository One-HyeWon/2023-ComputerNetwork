# 2023-ComputerNetwork

</br>
</br>
</br>

-------
</br>

##Java 계산기 프로그램

</br>
-------
</br>

### 프로젝트 설명
</br>

Socket을 사용하여 클라이언트에서 요청하는 연산을, 서버에서 처리하여 결과를 반환해주는 프로그램    
2개의 숫자만 입력 가능하며, 사칙 연산만 수행한다.    


**[Client]**   

</br>

클라이언트에서 연산자와 숫자 두 개를 입력 한다   
서버에서 계산 결과를 반환 받는다   

</br>

**[Server]**   

클라이언트에서 전달 받은 연산자를 가지고 숫자 두 개를 계산한다   

</br>


</br>
</br>


</br>
</br>

-------------    

</br>

### 프로그래밍 요구사항

1. 계산할 숫자는 2개만 입력한다. 3개 이상일 경우 에러를 발생시킨다.

2. 0으로 나누면 에러가 발생시킨다.

3. 소켓 통신에서 경로가 올바르지 않은 경우, 에러 메세지를 출력한다.

4. 존재하지 않는 연산자를 입력 할 경우, 에러 메세지를 출력한다.

5. Respons Type과 ErrorType을 정의하여 사용한다.    

</br>
</br>

####Type 정의 예시

```java
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
```

</br>

```java
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

```

</br>

```java
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
```

</br>
</br>

*작성자 : 조혜원*    

*gitHub :* <https://github.com/One-HyeWon>

