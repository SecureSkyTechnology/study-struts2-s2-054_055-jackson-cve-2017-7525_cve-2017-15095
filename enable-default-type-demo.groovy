@Grab('com.fasterxml.jackson.core:jackson-databind:2.8.8')
import com.fasterxml.jackson.databind.ObjectMapper;

class Zoo {
    Animal animal;
}

// Animalをabstractとしたクラス構成
abstract class Animal {
    String name;
    protected Animal() { }
}

class Dog extends Animal {
    double barkVolume;
    Dog() { }
}

class Cat extends Animal {
    boolean likesCream;
    int lives;
    Cat() { }
}

ObjectMapper mapper = new ObjectMapper();
// 素直なJSONでは、DogとCatのどちらにマッピングするのか判断できず JsonMappingException がthrowされる。
try {
    mapper.readValue('{"animal":{"name":"dog1","barkVolume":1.2}}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}
try {
    mapper.readValue('{"animal":{"name":"cat1","likesCream":true,"lives":10}}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}

// (既存のインスタンスで enableDefaultTyping() を呼んでも反映されなかったため、新しいインスタンスを用意する。)
mapper = new ObjectMapper();

// これでJSON中の追加クラス情報が認識される。
mapper.enableDefaultTyping();

// JSON中の追加クラス情報により、正しくDog/Catクラスのインスタンスにマッピングされた。
def z0 = mapper.readValue('{"animal":["Dog",{"name":"dog1","barkVolume":1.2}]}', Zoo.class);
assert z0.animal.class == Dog.class;
assert z0.animal.name == 'dog1';
assert z0.animal.barkVolume == 1.2;
z0 = mapper.readValue('{"animal":["Cat",{"name":"cat1","likesCream":true,"lives":10}]}', Zoo.class);
assert z0.animal.class == Cat.class;
assert z0.animal.name == 'cat1';
assert z0.animal.likesCream == true;
assert z0.animal.lives == 10;

// enableDefaultTyping() 無しで追加クラス情報を含むJSONをdeserializeしても、JsonMappingExceptionがthrowされる。
mapper = new ObjectMapper();
try {
    mapper.readValue('{"animal":["Dog",{"name":"dog1","barkVolume":1.2}]}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}
try {
    mapper.readValue('{"animal":["Cat",{"name":"cat1","likesCream":true,"lives":10}]}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}

