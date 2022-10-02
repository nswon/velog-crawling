package velogview.velogview.title.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import velogview.velogview.title.presentation.dto.request.TitleRequestDto;
import velogview.velogview.title.service.TitleService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TitleApiController {

    private final TitleService titleService;

    @PostMapping("/title")
    public Map<String, Integer> title(@RequestBody TitleRequestDto requestDto) {
        return titleService.title(requestDto);
    }
}
