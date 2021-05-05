package parsing.html

def properties = new Properties();
getClass().getResource('/flashscore.properties').withInputStream {
    properties.load(it)
}
String mainUrl = properties.'main.url';
String allMatchesToday = '/x/feed/f_1_0_3_ru_1'
String eventPartUrl = properties.'common.match.regex.url'
String anchorH2h = properties.'anchor.match.h2h'
String xFsignCode = properties.'secure.code.x.fsign'

def request = new URL(mainUrl + allMatchesToday).openConnection()
request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:84.0) Gecko/20100101 Firefox/84.0")
request.setRequestProperty("X-Fsign", xFsignCode)

def file = new File('text.html')
file.write request.getInputStream().getText().replaceAll('~', '\n\n~')

def eventIds = file.getText().findAll(/~AA÷(\w*)¬/) {match, firstWord -> firstWord}.collect()

// TODO может все же загрузить через селениум? Хотя мне нужны только ID матчей и их статус.
// нужны коды
// ~AA÷(\w*)¬ - это Id матча
// CX÷([\w\sа-я]*)¬ - это команда хозяев
// AF÷([\w\sа-я]*)¬JB - это команда гостей

// Если есть голы.
// AG÷(\d*)¬ - голы хозяев
// BC÷(\d*)¬ - голы гостей

def side = 'home'

/*
def parser = new SAXParser()
def eventIds = new XmlParser(parser).parse(file).with { page ->
    page.'**'.DIV.grep { it.'@id'?.contains 'g_1_' }.collect {
        it.'@id'?.split('_')?.last()
    }
}


def eventRefs = eventIds.collect {
    String.join('/', mainUrl, format(eventPartUrl, it), anchorH2h)
}
def dataUrl = "https://flashscore.ru/x/feed/d_hh_${eventIds.get(0)}_ru_1"
println dataUrl


new File("matches\\1-match.html").write request.getInputStream().getText()

        */
//id матчей находится в <div id='g_1_${id}' .. парсим Id вставляем в ${common.match.regex.url}
// составляем запрос изщ всех элементов с якорем ${anchor.match.h2h}

// кликать на названия команд
// смотреть последние 15-20 матчей и их счет
