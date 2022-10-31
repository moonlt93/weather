package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //전역예외시 붙인다.
public class globalExceptionHandler {


    //동작하던 예외 시점이 클라 -> 서버 api 호출일수도?
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class) // 얘는 컨트롤러 단 예외만 처리하지
    public Exception handlerException(){

        System.out.println("error from GlobalHandlerException");
    return new Exception();
    }
}
//advice로 전역의 예외를 끌어와서 , @ExceptionHandler가 포함된 클래스 것을 처리하므로 얘가 처리함.