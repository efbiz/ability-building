![夜班敲声-凌晨三点](http://upload-images.jianshu.io/upload_images/3167229-8e35d1a47bdfdfa5.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 动机

自从我把Github+Hexo的博客“交给”Google之后，每天都有几十位的访客2333，访客少的原因有许多，一个是文章较少，二是百度虫子没有爬到我的页面，就会导致即就算直接搜索博客里面的内容，百度都不会返回我的地址，就这个情况我已经使用百度和谷歌去处理了，至于文章较少的问题，我打算做个博客迁移！
我大三到现在的所有博客全部都在CSDN上，所以要对CSDN做一个文章导出功能，官方提供了工具，然而我昨天试了并不行。有博友自己提供了工具，是用python写的，本来打算用的，但是自己电脑没有装python，一时兴起决定用java写个小程序，自己动手丰衣足食，说干就干，整体的思路如下图：
![大体的流程](http://upload-images.jianshu.io/upload_images/3167229-35f1d8729e51c7f8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
**使用Jsoup去获取页面并解析—>将对应html代码转换成MD文件存储到本地—>最后放到对应位置，执行

hexo g
和

hexo d
命令发布到Github上**

### 过程

### 解析CSDN

比较庆幸的是CSDN并没有对“爬虫”做相关的处理，也就是说，我使用Jsoup获取页面并解析是没有任何限制的(至少我没有遇到)，这里需要做个简单的逻辑来获取

article/list
页面的所有文章列表url包括摘要。在接着根据url将对应文章的内容结构化解析出来，代码如下

```
	String url = HOST_URL + username +  + ;
Document parse = Jsoup.parse( URL(url), );
Element element = parse.select().get();
String papelistInfo = element.text();
String totalPage = papelistInfo.split()[].split()[]; 
 total = Integer.valueOf(totalPage);
System.out.println(total);
Map<String,String> map =  HashMap<>();
 ( i = ; i <= total; i++) {
    String listUrl = HOST_URL + username +  + i;
    Document doc = Jsoup.parse( URL(listUrl), );
    Elements topSelect = doc.select(TOP_XPATH);
    Elements normalSelect = doc.select(NOMAIL_QUERY);
     (Element item : topSelect) {
        map.put(item.select().get().attr(),item.select().get().text());
    }
     (Element item : normalSelect) {
        map.put(item.select().get().attr(),item.select().get().text());
    }
}
Set<String> strings = map.keySet();
(String item:strings){
    Document doc = Jsoup.parse( URL(HOST_URL + item), );
    BlogModel bm =  BlogModel();
    bm.setTitle(doc.select().text());
    bm.setDesc(map.get(item));
    bm.setPublishDate(doc.select().text());
    Elements select = doc.select();
    List<String> cat =  ArrayList<>();
    (Element ele : select){
        String text = ele.select().get().text();
         i = text.lastIndexOf();
        (i>=){
            String substring = text.substring(, i);
            cat.add(substring);
        }{
            cat.add(text);
        }
        ;
    }
    bm.setCategories(cat);
    Elements tagEles = doc.select();
    List<String> tags =  ArrayList<>();
    (Element ele : tagEles){
        String text = ele.text();
        tags.add(text);
    }
    bm.setTags(tags);
    doc.getElementsByTag().remove();
    bm.setContent(doc.select().html());
    {
        buildHexo(bm);
    } (Exception e){
        e.printStackTrace();
    }

}
```

代码可能较糙。
这里结构化我是按照Hexo的方式来定的，

```
	
```

more上面为文章摘要，下面为正文。

### html2md

走到这一步的时候，我并没有打算自己去写，我知道这不是一个简单的程序，我在Github上找了许久，大部分是js写的，期间我也尝试使用java去调用js代码去完成转换，但是失败了，最后决定使用这个[https://github.com/pnikosis/jHTML2Md](https://github.com/pnikosis/jHTML2Md)，
看来这位哥们写这个的本意也是要搞博客迁移，因为里面有这样一个方法

```
	htmlToHexoMd(String htmlPath, String mdPath, String charset)
```

当然了我没有用到，我用的是这个方法

```
	String convertHtml(String html, String charset)
```

很顺利，文章成功导出，一些文章的标题创建不了文件，也就三篇，我就没有去管他
![结果.png](http://upload-images.jianshu.io/upload_images/3167229-3fdc4ec10cafa351.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

让人高兴的是，你只要知道一个人的博客昵称如“u010850027”，你都可以把他的文章导出来~~~

### hexo发布

按理说，将对应的md文件放在“hexodir/source/_posts”下，然后执行发布命令就ok了，然而，此次最大的问题出现了，html转换md文件的问题！！！
对于一些简单的html代码，可以很完美的转换为md文件，但是对于复杂的就不行了，单单csdn里的代码块，都解析不了，后来我改了下作者的源码，然而也不行，因为CSDN有两种代码块方式，一个是markdown的一个是代码高亮，这一下问题就变的稍复杂起来。由于时间的问题，我打算先放一放，平时思考思考再看看能否解决。

### 后记

开始实现这个想法是周日凌晨开始的，真的，半夜听歌喝茶写代码会“高潮”！不知不觉搞到了凌晨三点多，然后睡觉，脑海里一直在想着这个问题，第二天上午也花了时间去解决问题，我想要是再花再这个上面，周末就玩完了。所以就暂且放一放！
最后白板上，蓝色虚线内的已经走通，红色区域内的问题是要重点解决的，待问题解决，就一路畅通了~~~

代码放在了这里：[Github](https://github.com/vector4wang/spring-boot-quick/tree/master/quick-crawler/src/main/java/com/quick/csdn2md)
想关注后续的欢迎star和fork

欢迎来我的博客[参观](http://vector4wang.tk)