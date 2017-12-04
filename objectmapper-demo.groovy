@Grab('com.fasterxml.jackson.core:jackson-databind:2.8.8')
import com.fasterxml.jackson.databind.ObjectMapper;

// Zoo と Animal クラスを定義
class Zoo {
    Animal animal;
}

class Animal {
    String name;
    Animal() { }
}

ObjectMapper mapper = new ObjectMapper();
// JSON を deserialize して、Zooクラスのオブジェクトにマッピングする。
Zoo z0 = mapper.readValue('{"animal":{"name":"animal1"}}', Zoo.class);

// Groovy独自のassertionで、生成されたオブジェクトの内容をチェック
assert z0.animal.class == Animal.class;
assert z0.animal.name == 'animal1';
