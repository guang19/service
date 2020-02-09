# 提供更加便捷的服务(Service Tool)
关于服务2字的含义,我想可以既可以是普通服务,也可以是其他类型的服务,如:云服务...

正在考虑 COS-with-SpringBoot... 

#### 计划中的服务包括:
1. COS(云对象存储服务,腾讯云的COS和阿里云的OSS)
2. ShortMessage-Service(短信服务,阿里云和腾讯云)
3. Email-Service(邮箱服务)
4. ...

计划会把其中常用的服务与SpringBoot做一个整合.


### 1. COS(OSS)服务
>腾讯云COS的参考文档: [文档1](https://cloud.tencent.com/document/product/436/7751)
                  [文档2](https://cloud.tencent.com/document/product/436/10199)
                  
>阿里云OSS的参考文档: [文档1](https://help.aliyun.com/document_detail/31837.html)
                  [文档2](https://help.aliyun.com/document_detail/31827.html)
                  [文档3](https://help.aliyun.com/document_detail/32010.html)
 
 #### 使用:
 引入依赖:
 ````java
maven:
<dependency>
  <groupId>com.github.guang19</groupId>
  <artifactId>cloud-storage-service</artifactId>
  <version>1.0.0</version>
</dependency>

gradle:
implementation 'com.github.guang19:cloud-storage-service:1.0.0'

````
>COS服务分为存储桶操作和对象操作.
>
>这2个操作我都分别为他们建立了一个模板,有了模板,就大大简化了对存储桶和对象的处理.
>在资源目录下创建一个配置文件,随意命名,如:cos-config.properties
>配置文件内容参照:[配置文件](https://github.com/guang19/service/tree/master/cloud-storage-service/src/main/resources/tencent_cloud-cos-config.properties)

存储桶操作(bucket):
1. 创建存储桶模板.
2. 使用存储桶模板.
````java
//创建存储桶模板顶级通用接口
COSBucketTemplate cosBucketTemplate = new COSBucketTemplateBuilder().build("cos-config.properties");

//创建腾讯云或阿里云的存储桶模板取决于: cos.service.type 属性 
TenCloudCOSBucketTemplate cosBucketTemplate = (TenCloudCOSBucketTemplate)new COSBucketTemplateBuilder().build("cos-config.properties");
AliyunOSSBucketTemplate ossBucketTemplate = (AliyunOSSBucketTemplate) new COSBucketTemplateBuilder().build("cos-config.properties");

//bucket 操作
         System.out.println(cosBucketTemplate.existBucket("a"));
         cosBucketTemplate.deleteBucket("test");
         System.out.println(cosBucketTemplate.getBucketAccessControllerList("a"));
         System.out.println(cosBucketTemplate.getBucketLocation("a"));
         System.out.println(cosBucketTemplate.getAllBuckets());
         ...
````

对象操作(object):
1. 创建对象模板.
2. 使用对象模板
````java
//创建对象模板顶级通用接口
COSObjectTemplate cosObjectTemplate = new COSObjectTemplateBuilder().build("cos-config.properties");

//创建腾讯云或阿里云的存储桶模板取决于: cos.service.type 属性 
TenCloudCOSObjectTemplate cosObjectTemplate = (TenCloudCOSObjectTemplate)new COSObjectTemplateBuilder().build("cos-config.properties");
AliyunOSSObjectTemplate ossObjectTemplate  = (AliyunOSSObjectTemplate) new COSObjectTemplateBuilder().build("cos-config.properties");

//object操作
System.out.println(ossObjectTemplate.getAllObjects());
System.out.println(ossObjectTemplate.getAllFileKeys());
System.out.println(ossObjectTemplate.getAllFileNames());
System.out.println(ossObjectTemplate.getAllObjectsWithKeyPrefix("img"));
System.out.println(ossObjectTemplate.getAllFileNamesWithKeyPrefix("img"));
System.out.println(ossObjectTemplate.getObjectMetaData("b"));
...
````

>具体的object和bucket操作,我相信如果各位同学熟悉OSS或COS就知道该怎么测试了.不过我个人是不推荐使用API来
>操作bucket的,控制台更加方面和直观.
 
 
 
>以下是全部配置属性:
 
| 属性:                                                        |       腾讯云        |      阿里云      |   默认值   |
| :----------------------------------------------------------- | :-----------------: | :--------------: | :--------: |
| cos.service.type : 对象存储服务类型                          | 需要(tencent cloud) |   需要(aliyun)   |     无     |
| cos.service.secret-id:云服务访问secret id                    |        需要         |       需要       |     无     |
| cos.service.secret-key:云服务访问secret key                  |        需要         |       需要       |     无     |
| cos.service.region: 要操作的存储桶地域                       |        需要         |      不需要      |     无     |
| cos.service.app-id:腾讯云的app id                            |        需要         |      不需要      |     无     |
| cos.service.endpoint: 阿里云的存储桶对外访问的域名           |       不需要        |  与cname二选一   |     无     |
| cos.service.cname:阿里云的存储桶自定义域名,推荐使用endpoint  |       不需要        | 与endpoint二选一 |     无     |
| cos.service.object-template-bucket:要操作的存储桶,此属性是构造对象模板所需,指定此属性就是让对象模板操作此存储桶里的对象.如果是想创建存储桶模板可不填 |        需要         |       需要       |     无     |
| cos.service.upload-limit-size:允许上传对象的大小限制.单位:MB |        可选         |       可选       |    20M     |
| cos.service.protocol:连接cos服务器的协议                     |        可选         |       可选       |    http    |
| cos.service.socket-timeout: 客户端传输数据的超时时间.单位:ms |        可选         |       可选       |  30000ms   |
| cos.service.connection-timeout:客户端与cos服务器的连接超时时间.单位:ms |        可选         |       可选       |  30000ms   |
| cos.service.connection-request-timeout:从连接池中获取连接的超时时间.单位:ms |        可选         |       可选       | -1(不超时) |
| cos.service.max-connection:允许打开的最大http连接数.         |        可选         |       可选       |    1024    |
| cos.service.proxy-ip:代理服务器的ip.一般不做配置             |        可选         |       可选       |     无     |
| cos.service.proxy-port:代理服务器的主机端口.一般不做配置     |        可选         |       可选       |     无     |
| cos.service.proxy-username:代理服务器验证的用户名.一般不做配置 |        可选         |       可选       |     无     |
| cos.service.proxy-password:代理服务器验证的密码,一般不做配置 |        可选         |       可选       |     无     |                  

