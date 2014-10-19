package com.muzhiliwu;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.UrlMappingBy;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

import com.muzhiliwu.init.MyUrlMapImpl;
import com.muzhiliwu.init.SetupAndInitParam;

//入口函数

@Modules(scanPackage = true)
@Ok("json")
@IocBy(type = ComboIocProvider.class, args = {
	"*org.nutz.ioc.loader.json.JsonLoader", "ioc/",
	"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "com.muzhiliwu"
})
@SetupBy(value = SetupAndInitParam.class)
@UrlMappingBy(value = MyUrlMapImpl.class)
@Encoding(input = "utf-8", output = "utf-8")
public class MainModule {

}
