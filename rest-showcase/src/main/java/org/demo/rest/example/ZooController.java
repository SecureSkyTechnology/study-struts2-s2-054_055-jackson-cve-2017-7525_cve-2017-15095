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

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ValidationAwareSupport;

@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "zoo" }) })
public class ZooController extends ValidationAwareSupport implements ModelDriven<Object> {

    private static final Logger log = LogManager.getLogger(ZooController.class);

    private Zoo model = new Zoo();
    private String id;
    private Collection<Zoo> list;
    private ZooService zooService = new ZooService();

    // GET /zoo
    public HttpHeaders index() {
        list = zooService.getAll();
        return new DefaultHttpHeaders("index").disableCaching();
    }

    // GET /zoo/1
    public HttpHeaders show() {
        model = zooService.get(model.id);
        return new DefaultHttpHeaders("show");
    }

    // POST /zoo
    public HttpHeaders create() {
        log.debug("Create new zoo {}", model);
        zooService.save(model);
        return new DefaultHttpHeaders("show").setLocationId(model.id);
    }

    // PUT /zoo/1
    public HttpHeaders update() {
        zooService.save(model);
        return new DefaultHttpHeaders("show").setLocationId(model.id);
    }

    // DELETE /zoo/1
    public HttpHeaders destroy() {
        log.debug("Delete zoo with id: {}", model.id);
        zooService.remove(model.id);
        return new DefaultHttpHeaders("index").disableCaching();
    }

    public Object getModel() {
        return (list != null ? list : model);
    }

}
