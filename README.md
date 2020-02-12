# 提供更加便捷的服务(Service Tool)

关于服务2字的含义,我想可以既可以是普通服务,也可以是其他类型的服务,如:云服务...

#### 计划中的服务包括:
1. COS(Cloud Object Storage:云对象存储服务.腾讯云的COS和阿里云的OSS)
2. SMS(Short-Message-Service: 短信服务,阿里云和腾讯云(目前只支持阿里云))
3. COS-With-SpringBoot
4. SMS-With-SpringBoot

### 1.COS(Cloud Object Storage or Object Storage Service)
云对象存储服务

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
  <version>1.0.2</version>
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

//或者创建腾讯云/阿里云的存储桶模板取决于: cos.service.type 属性 
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

//或者创建腾讯云/阿里云的存储桶模板取决于: cos.service.type 属性 
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

#### COS-With-SpringBoot
>在SpringBoot中使用COS,只剩二字: 习惯.

引入依赖
````java
maven:
<dependency>
  <groupId>com.github.guang19</groupId>
  <artifactId>cos-spring-boot-starter</artifactId>
  <version>1.0.2</version>
</dependency>

gradle:
implementation 'com.github.guang19:cos-spring-boot-starter:1.0.2'
````
>在SpringBoot中的配置与上面普通配置相似,只是加上spring前缀即可
````java
//此配置决定容器该加载哪种类型模板,当选择aliyun时,腾讯云的所有模板不会加载,当选择tencent cloud时,亦是如此.
spring.cos.service.type=aliyun/tencent cloud

关键属性需要配置...
````
使用:
````java
  @Autowired
  private AliyunOSSBucketTemplate bucketTemplate;

  @Autowired
  private AliyunOSSObjectTemplate objectTemplate;

接下来直接调api就行了...
````

### 2. SMS(Short Message Service)
短信服务.(因为腾讯云需要购买套餐包才能发送短信,连调试都不行,所以我暂时不写它的短信服务了)
                         
>阿里云SMS的参考文档: [文档](https://help.aliyun.com/document_detail/108064.html)

>斗胆猜测一下:我想没有同学愿意以API的方式来操作短信服务的签名和模板,如果想这么做的同学我还是建议参考文档吧,此项目是对SMS的一个简单的封装,毕竟
短信接口的API不多.

#### 使用:
引入依赖:
````java
maven:
<dependency>
  <groupId>com.github.guang19</groupId>
  <artifactId>short-message-service</artifactId>
  <version>1.0.2</version>
</dependency>

gradle:
implementation 'com.github.guang19:short-message-service:1.0.1'
````
短信接口的API比较少,但我仍然采用模板+配置的方式来编写.
在资源文件夹下创建一个叫 sms.properties的文件
>配置文件内容参照:[配置文件](https://github.com/guang19/service/tree/master/short-message-service/src/main/resources/short-message.properties)

使用:
1. 创建模板
2. 使用模板
````java
//创建顶级模板
SMSTemplate smsTemplate = new SMSTemplateBuilder().build("sms.properties");
//或者创建阿里云短信服务操作模板,模板类型取决于:sm.service.type
DefaultAliyunSMSTemplate smsTemplate = (DefaultAliyunSMSTemplate)new SMSTemplateBuilder().build("short-message.properties");

 //使用单条签名,发送单条短信
ResponseDTO responseDTO = smsTemplate.sendMessage("1123123123123", new ParamDTO().put("code", "123456"));
System.out.println(responseDTO.getMessage());
System.out.println(responseDTO.getCode());
System.out.println(responseDTO.getBizId());

String[] phoneNumbers = new String[]{"123123123123","12312312323"};
  //使用单条签名,发送多条短信
ResponseDTO responseDTO = smsTemplate.sendMessage(phoneNumbers, new ParamDTO().put("code", "123456"));
System.out.println(responseDTO.getMessage());
System.out.println(responseDTO.getCode());
System.out.println(responseDTO.getBizId());

//使用多条签名,发送多条短信
ResponseDTO responseDTO = smsTemplate.sendBatchMessage(phoneNumbers, new ParamDTO[]{new ParamDTO().put("code", "123456"), new ParamDTO().put("code", "123456")});
System.out.println(responseDTO.getMessage());
System.out.println(responseDTO.getCode());
System.out.println(responseDTO.getBizId());

 //查询短信明细
ResponseDTO responseDTO = smsTemplate.querySendDetails("123123123", LocalDate.now(Clock.systemDefaultZone()), 1);
System.out.println(Arrays.toString(responseDTO.getSmsSendDetailDTOs().getSmsSendDetailDTO()));

//查询指定流水的短信明细
ResponseDTO responseDTO = smsTemplate.querySendDetail("123123123213", LocalDate.now(Clock.systemDefaultZone()), 1, "123123123");
System.out.println(Arrays.toString(responseDTO.getSmsSendDetailDTOs().getSmsSendDetailDTO()));
````
>关于签名和模板,使用控制台申请和操作就行了,个人认为使用API来申请签名和模板是多余的,当然,也可能是我暂时能力不够.

>以下是短信服务所有配置:

| 属性                                                         | 阿里云       | 默认值 |
| ------------------------------------------------------------ | ------------ | :----- |
| sm.service.type:腾讯云还是阿里云,目前只支持阿里云            | 需要(aliyun) | 无     |
| sm.service.secret-id:阿里云的AccesskeyId                     | 需要         | 无     |
| sm.service.secret-key:阿里云的Access Key Secret              | 需要         | 无     |
| sm.service.region:发送短信的ECS的地域,此配置必填,但是只有在配置专属域名的情况下才有用 | 需要         | 无     |
| sm.service.sign-name:短信签名,如果想批量发送短信,要多个签名,以逗号分隔 | 需要         | 无     |
| sm.service.message-template:短信模板,阿里云的短信模板code,个人认为在项目中可能会使用多中模板,所以建议以模板API的方式 set | 需要         | 无     |
| sm.service.query-page-size:当使用模板进行查询操作时,此参数就指定查询结果每页的数量,如果不指定,默认为 50 | 可选         | 50     |

#### SMS-With-SpringBoot
引入依赖
````java
<dependency>
  <groupId>com.github.guang19</groupId>
  <artifactId>sms-spring-boot-starter</artifactId>
  <version>1.0.2</version>
</dependency>

gradle:
implementation 'com.github.guang19:sms-spring-boot-starter:1.0.2'
````
>在SpringBoot中的配置与上面普通配置相似,只是加上spring前缀即可
````java
//此配置决定容器该加载哪种类型模板,当选择aliyun时才加载SMS模板,暂且不支持腾讯云的短信服务,所以此选项只有aliyun
spring.cos.service.type=aliyun

关键属性需要配置...
````
使用:
````java
  @Autowired
  private AliyunSMSTemplate smsTemplate;
````
>接下来直接调api就行了...

其他:
本来想把美团Leaf也做一下,可准备写的时候,才想起来我是要做一个工具的项目,而Leaf已经很好的融合了SpringBoot成为了
一个Server,所以就算了.写到这里有点累了,学学其他东西去了,以后有好的工具我都会考虑融入到这个项目来.

Leaf参考: [Leaf](https://github.com/Meituan-Dianping/Leaf)
