/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.demo.rest.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZooService {

    private static Map<String, Zoo> zoos = new HashMap<String, Zoo>();
    private static int nextId = 3;
    static {
        Zoo z1 = new Zoo();
        z1.id = "1";
        Dog d1 = new Dog();
        d1.name = "dog1";
        d1.barkVolume = 1.2;
        z1.animal = d1;
        zoos.put("1", z1);
        Zoo z2 = new Zoo();
        z2.id = "2";
        Cat c1 = new Cat();
        c1.name = "cat1";
        c1.likesCream = true;
        c1.lives = 10;
        z2.animal = c1;
        zoos.put("2", z2);
    }

    public Zoo get(String id) {
        return zoos.get(id);
    }

    public List<Zoo> getAll() {
        return new ArrayList<Zoo>(zoos.values());
    }

    public void save(Zoo zoo) {
        if (zoo.id == null) {
            zoo.id = String.valueOf(nextId++);
        }

        zoos.put(zoo.id, zoo);
    }

    public void remove(String id) {
        zoos.remove(id);
    }

}
