@Grab('com.fasterxml.jackson.core:jackson-databind:2.8.8')
import com.fasterxml.jackson.databind.ObjectMapper;

class Zoo {
    Animal animal;
}

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

Dog dog1 = new Dog();
dog1.name = 'dog1';
dog1.barkVolume = 1.2;

Cat cat1 = new Cat();
cat1.name = 'cat1';
cat1.likesCream = true;
cat1.lives = 10;

Zoo z0 = new Zoo();

z0.animal = dog1;
assert mapper.writeValueAsString(z0) == '{"animal":{"name":"dog1","barkVolume":1.2}}';

z0.animal = cat1;
assert mapper.writeValueAsString(z0) == '{"animal":{"name":"cat1","likesCream":true,"lives":10}}';

try {
    Zoo zax = mapper.readValue('{"animal":{"name":"dog1","barkVolume":1.2}}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}

try {
    Zoo zax = mapper.readValue('{"animal":{"name":"cat1","likesCream":true,"lives":10}}', Zoo.class);
    assert false;
} catch (com.fasterxml.jackson.databind.JsonMappingException e) {
    assert e.message.startsWith('Can not construct instance of Animal: abstract types either need to be mapped to concrete types, have custom deserializer, or contain additional type information');
}

mapper = new ObjectMapper(); // NEED new ObjectMapper instance to activate enableDefaultTyping()
mapper.enableDefaultTyping(); // equivalant to enableObjectTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);

z0.animal = dog1;
assert mapper.writeValueAsString(z0) == '{"animal":["Dog",{"name":"dog1","barkVolume":1.2}]}';
z0 = mapper.readValue('{"animal":["Dog",{"name":"dog1","barkVolume":1.2}]}', Zoo.class);
assert z0.animal.class == Dog.class;
assert z0.animal.name == 'dog1';
assert z0.animal.barkVolume == 1.2;

z0.animal = cat1;
assert mapper.writeValueAsString(z0) == '{"animal":["Cat",{"name":"cat1","likesCream":true,"lives":10}]}';
z0 = mapper.readValue('{"animal":["Cat",{"name":"cat1","likesCream":true,"lives":10}]}', Zoo.class);
assert z0.animal.class == Cat.class;
assert z0.animal.name == 'cat1';
assert z0.animal.likesCream == true;
assert z0.animal.lives == 10;

