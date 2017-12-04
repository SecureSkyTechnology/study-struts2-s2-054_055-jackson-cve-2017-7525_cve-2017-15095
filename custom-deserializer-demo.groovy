@Grab('com.fasterxml.jackson.core:jackson-databind:2.8.8')
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

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

class AnimalDeserializer extends StdDeserializer<Animal> {
    AnimalDeserializer() {
        super(Animal.class);
    }
    @Override
    Animal deserialize(JsonParser jp, DeserializationContext ctxt) {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get('name').asText();
        if (node.has('barkVolume')) {
            Dog dog = new Dog();
            dog.name = name;
            dog.barkVolume = node.get('barkVolume').asDouble();
            return dog;
        } else if (node.has('likesCream') && node.has('lives')) {
            Cat cat = new Cat();
            cat.name = name;
            cat.likesCream = node.get('likesCream').asBoolean();
            cat.lives = node.get('lives').asInt();
            return cat;
        } else {
            throw ctxt.weirdKeyException(Animal.class, 'animal', 'missing required field');
        }
    }
}

ObjectMapper mapper = new ObjectMapper();
SimpleModule module = new SimpleModule();
module.addDeserializer(Animal.class, new AnimalDeserializer());
mapper.registerModule(module);

def z0 = mapper.readValue('{"animal":{"name":"dog1","barkVolume":1.2}}', Zoo.class);
assert z0.animal.class == Dog.class;
assert z0.animal.name == 'dog1';
assert z0.animal.barkVolume == 1.2;
z0 = mapper.readValue('{"animal":{"name":"cat1","likesCream":true,"lives":10}}', Zoo.class);
assert z0.animal.class == Cat.class;
assert z0.animal.name == 'cat1';
assert z0.animal.likesCream == true;
assert z0.animal.lives == 10;
