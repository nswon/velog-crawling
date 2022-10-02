package velogview.velogview.title.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import velogview.velogview.title.presentation.dto.request.TitleRequestDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TitleService {

    public Map<String, Integer> title(TitleRequestDto requestDto) {
        /*
        벨로그에서 사용자 프로필을 들어갈 때 공통적으로 https://velog.io/@사용자닉네임 url로 접근한다.
        그래서 먼저 사용자의 닉네임을 받는다.
         */
        String velogUrl = "https://velog.io/@" + requestDto.getNickname();
        Connection conn = Jsoup.connect(velogUrl);

        List<String> list = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> textCount = new ArrayList<>();
        Map<String, Integer> map = new LinkedHashMap<>();
        try {
            Document document = conn.get();

            //무한페이징이긴 한데 한 페이지(약 스물몇개)의 포스트 제목을 가져온다.
            Elements title = document.getElementsByTag("h2");

            for(Element element : title) {
                String s = String.valueOf(element);

                //<h2></h2>를 제거한다.
                s = s.substring(4, s.length()-5);

                //그리고 이쁘게 보여질 제목들은 따로 list에 저장한다.
                titles.add(s);

                //기본적으로 해당 포스트에 들어갈 때는 제목 띄어쓰기를 다 -로 바꿔야 한다.
                s = s.replace(" ", "-");
                list.add(s);
            }

            for (String s : list) {
                //다시 해당 url로 접근해서
                String detailUrl = velogUrl + "/" + s;
                Connection detailConn = Jsoup.connect(detailUrl);
                Document detailDocument = detailConn.get();
                //<p> 태그의 문자들의 길이를 저장한다. <p> 만 저장되기 때문에 정확한 길이가 나올 수 없다.
                int text = detailDocument.text().length();
                textCount.add(text);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i=0; i<list.size(); i++) {
            map.put(titles.get(i), textCount.get(i));
        }
        return map;
    }
}
