package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.entity.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController  // http 요청을 보낼때 , controller에서 지정을 해서 내려 줄수있음.
@RequiredArgsConstructor

public class DiaryController {

    private final DiaryService diaryService;

    @ApiOperation(value="일기 텍스트와 날씨를 이용해서 일기 저장", notes = "이 url을 참고해보세요") // 코멘트를 칠수 있네
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestBody String text){

        diaryService.createDiary(date,text);

    }
    @ApiOperation("선택한 날짜의 모든 일기 데이터를 가져온다.")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,@RequestBody String text){
     return  diaryService.readDiary(date);
    }
    @ApiOperation("해당 날짜기간의 일기를 가져옵니다.")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)@ApiParam(value="조회할 기간의 첫번째날",example = "2020-02-02") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)@ApiParam(value="조회할 기간의 마지막날",example = "2020-02-02") LocalDate endDate
                            ){

        return diaryService.readBetweenDiaries(startDate,endDate);
    }


    @PutMapping("/update/diary")
    public void updateDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
                            @RequestBody String text){
         diaryService.updateDiary(date,text);
    }


    @DeleteMapping("/delete/diary")
    public void deleteDiary(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.deleteDiary(date);
    }

}
